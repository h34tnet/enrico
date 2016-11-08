# Enrico

Enrico is a weekend-project toy VM for a made up processor with a very
limited instruction set, integers only and a pretty stupid memory
model.

All operations operate on the linear memory (an array of ints) of the 
virtual machine with the exception of the 2 stacks, one for the 
stack operations, the other is the call stack. Hopefully i find a better
way soon.

Currently the VM consists of:

* Four general purpose registers: a, b, c and d. They serve no real 
  reason because variables can do the same and there's no 
  *real* tangible performance benefit (maybe a tiny, tiny one). 
* The instruction pointer register, which can be only manipulated by 
  `jmp*`, the `call` and the `ret` operations. Otherwise it's 
  incremented after every operation.
* Main memory: just an array of integers. Currently, integers are the 
  only data type. It's accessed through the `load reg offs` and 
  `save val offs` operations or by defining and accessing variables
   denoted by the $ prefix. 
  * Variables have to be defined by `def $foo`. `def` is not an 
    operation, it gets translated to memory offsets during by the 
    parser.
  * The address of a variable can be accessed by prefixing the variable
    name with an '@'. e.g. `set a @foo` stores the address of $foo in 
    the a-register.
  * Memory offsets during parse time are zero-based. When loading the
    code the VM moves the `memOffs` (not accessible) to the position
      after the code. All memory accesses are relative to the `memOffs`.
      I'll probably change this to an absolute addressing scheme
      generated at the compile time.
  * There is no data section for constants yet.
  * Variables can't be preset with a certain value yet.
* Stack: a simple stack, accessible through the `push`, `pop` and `peek`
  operations. Will turn that into a stack operating on the main memory
  asap.
* CallStack: the `call` operation stores the current IP on the call 
  stack. The `ret` operation pops it and resumes program execution at 
  the next instruction.

## Program

A program consists of a list of operations, it's practically the AST.
 It removes

Most operations have parameters which are either registers, constants,
 variables, variable offsets or labels.

## Parser

Parses valid program text (from a file, stream or string) into a 
program. One operation per line. Empty lines and everything after a 
`#` (comment) is discarded.

## Program execution
 
The program is loaded into memory at offset 0 and the `memOffs` for
 variable addressing is set to the position after the code. Execution
 begins at 0 and will continue until
 
 a. a `res x` operator is executed which halts execution and returns
 the value of x.
 a. or an error occurs (invalid byte code, out of memory access, ...).
  
If none of those happen, it'll execute infinitely.

## Operations

* `nop`: does nothing.

### Register operations

* `set reg value`: sets the value of reg to value, which might be 
  another register or a constant
* `swp reg reg`: swaps values of two registers

### Memory operations

* `def $variable`: defines a variable. Variables 
    are accessed by prefixing a name with a $. Right now you have to 
    initialize the VM with enough memory for the variables defined
    in the program, otherwise there'll be an access violation.
    **Note:** this is not an operation! It just ties a memory offset
      to a variable name during parse time.
* `load ref value`: sets the register to the value of the memory at 
  offset `value`
* `save ref value`: sets the value of the memory at offset `value` to 
  register
* `push ref`: pushes the register on the stack
* `pop ref`: pops the register from the stack
* `peek ref`: sets the register to the topmost element of the stack 
  without removing it

### Program flow

* `:label`: defines a label which can be used as the target of program 
  flow operations. Labels are always `:`-prefixed.
  **Note:** This is not an operation. Labels are turned into constants 
  after parsing.
* `jmp :label`: an unconditional jump
* `jmpe :label op1 op2`: jumps to label if op1 == op2
* `jmpne :label op1 op2`: jumps to label if op1 != op2
* `jmplt :label op1 op2`: jumps to label if op1 < op2
* `jmplte :label op1 op2`: jumps to label if op1 <= op2
* `jmpgt :label op1 op2`: jumps to label if op1 > op2
* `jmpgte :label op1 op2`: jumps to label if op1 >= op2
* `call :label`: unconditional jump that stores the ip on the call 
  stack, to be used in combination with `ret`
* `ret`: unconditional jump to one instruction after the last stored ip 
  on the call stack
* `res op`: exits the program, returning the value of op

Note: now that labels are translated to int ip-offsets, it should be
 possible to jump to arbitrary locations by passing registers, variables
 or constants.

### Arithmetic

* `add reg p1 p2`: sets reg to p1 + p2
* `sub reg p1 p2`: sets reg to p1 - p2
* `mul reg p1 p2`: sets reg to p1 * p2
* `div reg p1 p2`: sets reg to p1 / p2
* `mod reg p1 p2`: sets reg to p1 % p2 (the remainder)

### IO

* `print op`: prints the value converted to an unicode char to the 
  writer provided to the VM
* `read op`: reads a char from the reader provided to the VM

# Example programs

## Fibonacci

    # calculates the nth fibonacci number (0, 1, 1, 2, 3, 5, ...)
    
    read d              # reads which of the desired fibonacci number from IO. d means the n'th number.
    jmpe :def0 d 1      # special cases for 0
    jmpe :def1 d 2      # and 1
    
    set c 2             # initialize the current iteration to 2, because 0 and 1 are special cases
    set a 0             # predefine the first two fib nums
    set b 1
    
    :loop
    add a a b           # add the two numbers before
    swp a b             # thanks to this swap we don't need memory
    add c c 1           # current iteration counter
    jmpne :loop c d     # if the current iteration isn't there yet, repeat
    res b               # otherwise we've got a result!
    
    :def0
    res 0
    
    :def1
    res 1

to run the program:

    Program p = Parser.load(new File("fibonacci.enr"));
    int[] byteCode = new Compiler().compile(p);
    
    // creates a new VM with 512 ints of ram.
    VM vm = new VM(512);
    // we want the 13th fibonacci number
    vm.setInputReader(new CharReader(new char[]{13}, 1))
    
    // load the code into the VMs memory
    vm.load(byteCode);
    
    // execute the code
    // result should now equal 144
    int result = vm.exec()
    
## Debugging

Both the compiler and the VM itself have a debugging switch.

        int[] bc = new Compiler()
                .enableDebugOutput(true)
                .compile(new Parser().parse(
                        "def $v\nset $v 1\nset b 0\njmpgt :win $v b\nres 0\n:win\nres 1"));

        int res = vm.load(bc)
                .enableDebugMode(true)
                .exec();
                
This produces the following debug output: 

    LabelOffsetTranslation table:
    :win @ 20
    
    Compiler output:
           0: set mem[0] to 1                                                  [1, 2, 0, 0, 1]
           5: set b to 0                                                       [1, 1, 1, 0, 0]
          10: jmpgt to 20 if mem[0] > b                                        [303, 0, 20, 2, 0, 1, 1]
          17: res 0                                                            [309, 0, 0]
          20: label ":win"                                                     []
          20: res 1                                                            [309, 0, 1]
    
    VM exec debug output:
           0: set mem[0] to 1                                                 
           5: set b to 0                                                      
          10: jmpgt to 20 if mem[0] > b                                       
          20: res 1                                                           


First is the "label offset translation" table generated in the first
 compiler pass. It stores the memory offsets the `jmp*` operations 
 should jump to. 
 
Next is the compiler output. 
 Note that the next operation has the same offset
 because the label itself doesn't generate any byte code.
 
Finally a log of all VM operations executed. 
    
## Notes

### What is this?

Weekend project - I was bored. I don't really know anything about VMs, 
compilers, interpreters, etc., but working on this and exploring my own 
ideas how it "could work" is quite satisfying. Note: the interpreter is
now removed.
 
### Why "Enrico"?
 
Enrico is the name of a TV clown. And the project is a joke.

### Plans

* ~~Maybe make it more VM-ier (i.e. store code in the VM memory 
  itself, etc). Update: halfway there!~~
* Move away from "everything is an integer" and add support for 
  floating points.
* ~~Add "print"~~  
  IO is supported by the `read` and `print` operations.
* Redesign memory access (absolute addressing determined at compile 
  time). Look at DOS's .COM format? 
  * Implement vm-native stacks in the main memory instead of cheating.  