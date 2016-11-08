package net.h34t.enrico;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OpTest extends InstantRunner {

    @Test
    public void testAdd() {
        Program program = new Parser().parse("set a 1\nset b 2\nadd c a b\nres 0");
        vm.load(new Compiler().compile(program)).exec();
        assertEquals(3, vm.c);
        assertEquals(1, vm.a);
        assertEquals(2, vm.b);
    }

    @Test
    public void testSub() {
        Program program = new Parser().parse("set a 10\nset b 6\nsub c a b\nres 0");

        int[] code = new Compiler().compile(program);

        vm.load(code).exec();
        assertEquals(4, vm.c);
    }

    @Test
    public void testMul() {
        Program program = new Parser().parse("set a 10\nset b 6\nmul c a b\nres 0");
        vm.load(new Compiler().compile(program)).exec();
        assertEquals(60, vm.c);
    }

    @Test
    public void testDiv() {
        Program program = new Parser().parse("set a 20\nset b 10\ndiv c a b\nres 0");
        vm.load(new Compiler().compile(program)).exec();
        assertEquals(2, vm.c);
    }

    @Test
    public void testDiv2() {
        Program program = new Parser().parse("set a 10\nset b 3\ndiv c a b\nres 0");
        vm.load(new Compiler().compile(program)).exec();
        assertEquals(3, vm.c);
    }


    @Test
    public void testMod() {
        Program program = new Parser().parse("set a 10\nset b 3\nmod c a b\nres 0");
        vm.load(new Compiler().compile(program)).exec();
        assertEquals(1, vm.c);
    }

    @Test
    public void testMod2() {
        Program program = new Parser().parse("set a 10\nset b 10\nmod c a b\nres 0");
        vm.load(new Compiler().compile(program)).exec();
        assertEquals(0, vm.c);
    }

    @Test
    public void testPushPop() {
        Program program = new Parser().parse("push 10\npop a\nres 0");
        vm.load(new Compiler().compile(program)).exec();

        assertEquals(10, vm.a);
    }

    @Test(expected = RuntimeException.class)
    public void testPopEmpty() {
        Program program = new Parser().parse("pop a\nres 0");
        vm.load(new Compiler().compile(program)).exec();
    }

    @Test
    public void testPushPop2() {
        Program program = new Parser().parse("push 10\npush 20\npop a\npop b\nres 0");
        vm.load(new Compiler().compile(program)).exec();

        assertEquals(20, vm.a);
        assertEquals(10, vm.b);
    }

    @Test
    public void testPeek() {
        Program program = new Parser().parse("push 10\npeek a\npop b\nres 0");
        vm.load(new Compiler().compile(program)).exec();

        assertEquals(10, vm.a);
        assertEquals(10, vm.b);
    }

    @Test
    public void testRes() {
        Program program = new Parser().parse("set a 10\nres a");
        int res = vm.load(new Compiler().compile(program)).exec();
        assertEquals(10, res);
    }

    @Test
    public void testSwap() throws Exception {
        Program program = new Parser().parse(
                "set a 1\n" +
                        "set b 2\n" +
                        "swp a b\n" +
                        "res 0");

        vm.load(new Compiler().compile(program)).exec();

        Assert.assertEquals(2, vm.a);
        Assert.assertEquals(1, vm.b);
    }

    @Test
    public void testLoadSave() {
        // warning: this overwrites sections of code memory
        // at the time the code is overwritten it's already executed though,
        // so this still executes properly
        VM vm = new VM(256);

        vm.load(c("load a 0\nsave a 1\nres 0")).data(new int[]{3, 0}).exec();
        assertEquals(200, vm.a);
        assertEquals(200, vm.memory[1]);
    }

    @Test(expected = RuntimeException.class)
    public void testLoadOOMFail() {

        Program program = new Parser().parse("load a 2\nres 0");
        int[] code = new Compiler().compile(program);
        int[] data = {3, 0};

        VM vm = new VM(data.length + data.length);
        vm.load(code)
                .data(data)
                .exec();
    }

    @Test(expected = RuntimeException.class)
    public void testSaveOOMFail() {
        // tries to write outside the available memory
        // the following code needs 10 bytes (0-9) and we try to write one after this one

        int[] bc = c("save a 10\nres 0");

        VM vm = new VM(bc.length + 2);

        assertEquals(10, vm.memSize);

        vm.load(bc)
                .data(new int[]{3, 0})
                .exec();
    }


    @Test
    public void testSet() {
        compileAndRun(vm, "set a 3\nres 0");
        assertEquals(3, vm.a);
    }

    @Test(expected = RuntimeException.class)
    public void testSetConstantIllegal() {
        compileAndRun(vm, "set 3 a\nres 0");
    }

    @Test
    public void testRes2() {
        int res = compileAndRun(vm, "res 3");
        assertEquals(3, res);
    }

    @Test
    public void testCallRet() {
        int res = compileAndRun(vm,
                "call :a\n" +
                        "res a\n" +
                        ":a\n" +
                        "set a 3\n" +
                        "ret");

        assertEquals(3, res);
    }

    @Test
    public void testCallRet2() {
        int res = compileAndRun(vm, "call :foo\nres a\n:foo\ncall :bar\nret\n:bar\nset a 3\nret");

        assertEquals(3, res);
    }

    @Test
    public void testJmp() {
        Integer res = compileAndRun(vm,
                "jmp :a\n" +
                        "res 1\n" +
                        ":a\n" +
                        "res 3\n");

        assertEquals(new Integer(3), res);
    }

    @Test
    public void testJmpEPositive() {
        int res = compileAndRun(vm,
                "set a 1\nset b 1\njmpe :win a b\nres 0\n:win\nres 1");

        assertEquals(1, res);
    }

    @Test
    public void testJmpENegative() {
        int res = compileAndRun(vm,
                "set a 1\nset b 0\njmpe :win a b\nres 0\n:win\nres 1");
        assertEquals(0, res);
    }

    @Test
    public void testJmpNEPositive() {
        int res = compileAndRun(vm,
                "set a 1\nset b 1\njmpne :win a b\nres 0\n:win\nres 1");
        assertEquals(0, res);
    }

    @Test
    public void testJmpNENegative() {
        int res = compileAndRun(vm,
                "set a 1\nset b 0\njmpne :win a b\nres 0\n:win\nres 1");
        assertEquals(1, res);
    }

    @Test
    public void testJmpGTPositive() {
        int[] bc = new Compiler()
                .compile(new Parser().parse(
                        "def $v\nset $v 1\nset b 0\njmpgt :win $v b\nres 0\n:win\nres 1"));

        int res = vm.load(bc)
                .exec();

        assertEquals(1, res);
    }

    @Test
    public void testJmpGTNegative() {
        int res = compileAndRun(vm, new Parser().parse(
                "set a 1\nset b 0\njmpgt :win b a\nres 0\n:win\nres 1"));
        assertEquals(0, res);
    }

    @Test
    public void testJmpLTPositive() {
        int res = compileAndRun(vm, new Parser().parse(
                "set a 1\nset b 0\njmplt :win a b\nres 0\n:win\nres 1"));
        assertEquals(0, res);
    }

    @Test
    public void testJmpLTNegative() {
        int res = compileAndRun(vm, new Parser().parse(
                "set a 1\nset b 0\njmplt :win b a\nres 0\n:win\nres 1"));
        assertEquals(1, res);
    }

    @Test
    public void testVMInit() {
        VM vm = new VM(new int[]{1, 2, 3});
        assertEquals(1, vm.memory[0]);
        assertEquals(2, vm.memory[1]);
        assertEquals(3, vm.memory[2]);
    }

    @Test
    public void testVMInit2() {
        VM vm = new VM(new int[3]);
        assertEquals(0, vm.memory[0]);
        assertEquals(0, vm.memory[1]);
        assertEquals(0, vm.memory[2]);
    }

    @Test
    public void testVMInit3() {
        VM vm = new VM(new int[0], 1, 2, 3, 4, 5);
        assertEquals(1, vm.ip);
        assertEquals(2, vm.a);
        assertEquals(3, vm.b);
        assertEquals(4, vm.c);
        assertEquals(5, vm.d);
        assertEquals(0, vm.memSize);
    }

    @Test
    public void testVMNext() {
        VM vm = new VM(new int[0]);
        // one parameter
        vm.next(1); // moves ip 1 + 1*2 slots
        vm.next(2); // 1 + 2*2
        vm.next(0); // 1 + 0*2
        assertEquals(9, vm.ip);
    }

    @Test
    public void testVMToString() {
        VM vm = new VM(new int[0]);
        assertNotEquals("", vm.toString());
    }

    @Test
    public void testSetAllRegisters() {
        compileAndRun(vm, new Parser().parse(
                "set a 1\nset b 2\nset c 3\nset d 4\nres 0"));

        assertEquals(1, vm.a);
        assertEquals(2, vm.b);
        assertEquals(3, vm.c);
        assertEquals(4, vm.d);
    }

    @Test
    public void testReadAllRegisters() {
        assertEquals(1, (int) compileAndRun(new VM(128), "set a 1\nres a"));
        int resb = compileAndRun(new VM(128), new Parser().parse("set b 1\nres b"));
        assertEquals(1, resb);
        int resc = compileAndRun(new VM(128), new Parser().parse("set c 1\nres c"));
        assertEquals(1, resc);
        int resd = compileAndRun(new VM(128), new Parser().parse("set d 1\nres d"));
        assertEquals(1, resd);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidRegister() {
        Register r = new Register(null);
        r.getValue(new VM(0));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegister2() {
        Register r = new Register(null);
        r.setValue(new VM(0), 0);
    }

    @Test(expected = RuntimeException.class)
    public void testStackOverflow() {
        VM vm = new VM(128).setStackSize(0);
        Program p = new Parser().parse("push 1\nres 0");
        compileAndRun(vm, p);
    }

    @Test(expected = RuntimeException.class)
    public void testStackOverflow2() {
        VM vm = new VM(128).setStackSize(1);
        Program p = new Parser().parse("push 1\npush 2\nres 0");
        compileAndRun(vm, p);
    }

    @Test(expected = RuntimeException.class)
    public void testCallStackOverflow() {
        VM vm = new VM(128).setCallStackSize(0);
        Program p = new Parser().parse("call :foo\n:foo\nret\nres 0");
        compileAndRun(vm, p);
    }

    @Test(expected = RuntimeException.class)
    public void testCallStackOverflow2() {
        VM vm = new VM(128).setCallStackSize(1);
        Program p = new Parser().parse("call :foo\n:foo\ncall :bar\nret\n:bar\nret\nres 0");
        compileAndRun(vm, p);
    }
}