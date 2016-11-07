package net.h34t.enrico;

import net.h34t.enrico.op.*;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Stack;

/**
 * Stores memory, stacks and registers.
 */
public class VM {

    private static final String NL = String.format("%n");

    public final Stack<Integer> stack;
    public final Stack<Integer> callStack;
    public final int memSize;
    public int[] memory;
    public int maxStackSize = 256;
    public int maxCallStackSize = 256;
    public int memOffs;

    public Writer out = new PrintWriter(System.out);
    public Reader in;

    public int ip = 0, a = 0, b = 0, c = 0, d = 0;
    public boolean interpreterMode = false;

    public boolean debugMode = false;

    public VM() {
        this.memSize = 0;
        this.memory = new int[0];
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
    }

    public VM(int memSize) {
        this.memSize = memSize;
        this.memory = new int[this.memSize];
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
    }

    public VM(int[] memory) {
        this.memSize = memory.length;
        this.memory = memory;
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
    }

    public VM(int[] memory, int ip, int a, int b, int c, int d) {
        this.memSize = memory.length;
        this.memory = memory;
        this.stack = new Stack<>();
        this.callStack = new Stack<>();

        this.ip = ip;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public VM load(int[] code) {
        System.arraycopy(code, 0, memory, 0, code.length);
        memOffs = code.length;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ip:  ").append(ip).append(NL);
        sb.append("a:   ").append(a).append(NL);
        sb.append("b:   ").append(b).append(NL);
        sb.append("c:   ").append(c).append(NL);
        sb.append("d:   ").append(d).append(NL);
        sb.append("mem: ");
        for (int m : memory) sb.append(m).append("|");
        sb.append(NL);

        sb.append("stk: ");
        for (int m : stack) sb.append(m).append("|");
        sb.append(NL);

        return sb.toString();
    }

    /**
     * When turned on the VM prints the current statement executed
     *
     * @param enabled true to enable debug output
     * @return this for chaining
     */
    public VM enableDebugMode(boolean enabled) {
        this.debugMode = enabled;
        return this;
    }

    public VM setStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public VM setCallStackSize(int maxCallStackSize) {
        this.maxCallStackSize = maxCallStackSize;
        return this;
    }

    public VM setPrintWriter(Writer writer) {
        this.out = writer;
        return this;
    }

    public VM setInputReader(Reader reader) {
        this.in = reader;
        return this;
    }

    public void next(int params) {
        ip += interpreterMode ? 1 : 1 + params * 2;
    }

    public Integer exec() {
        while (true) {
            // get the next operation
            int op = memory[ip];
            final Operation operation;

            switch (op) {
                case Operation.SET:
                    operation = new SetOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]));
                    break;

                case Operation.SWP:
                    operation = new SwpOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]));
                    break;

                case Operation.ADD:
                    operation = new AddOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.SUB:
                    operation = new SubOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.MUL:
                    operation = new MulOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.DIV:
                    operation = new DivOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.MOD:
                    operation = new ModOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.LOAD:
                    operation = new LoadOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]));
                    break;

                case Operation.SAVE:
                    operation = new SaveOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]));
                    break;

                case Operation.PUSH:
                    operation = new PushOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.POP:
                    operation = new PopOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.PEEK:
                    operation = new PeekOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.JMP:
                    operation = new JmpOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.JMPE:
                    operation = new JmpEOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.JMPNE:
                    operation = new JmpNEOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.JMPGT:
                    operation = new JmpGTOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.JMPGTE:
                    operation = new JmpGTEOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.JMPLT:
                    operation = new JmpLTOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.JMPLTE:
                    operation = new JmpLTEOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6]));
                    break;

                case Operation.CALL:
                    operation = new CallOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.RET:
                    operation = new RetOp();
                    break;

                case Operation.RES:
                    operation = new ResOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.PRINT:
                    operation = new PrintOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                case Operation.READ:
                    operation = new ReadOp(Encoder.decode(memory[ip + 1], memory[ip + 2]));
                    break;

                default:
                    throw new RuntimeException("Unexpected opcode " + op);
            }

            if (this.debugMode) {
                System.out.printf("%8d: %-64s%n", this.ip, operation.toString());
            }

            Integer result = operation.exec(this);

            if (result != null)
                return result;

        }
    }

    /**
     * this changes the IP adressing scheme
     *
     * @param enabled if true, ip is only ever increased by 1 instead of the number of INTs an instruction takes
     */
    public void setInterpreterMode(boolean enabled) {
        interpreterMode = enabled;
    }

}
