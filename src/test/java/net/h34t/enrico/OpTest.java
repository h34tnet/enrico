package net.h34t.enrico;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OpTest {

    private VM vm;

    @Before
    public void runBeforeTestMethod() {
        vm = new VM(0);
    }

    @Test
    public void testAdd() {
        Program program = new Parser().parse("set a 1\nset b 2\nadd c a b");
        new Interpreter().run(vm, program);
        assertEquals(3, vm.c);
        assertEquals(1, vm.a);
        assertEquals(2, vm.b);
    }

    @Test
    public void testSub() {
        Program program = new Parser().parse("set a 10\nset b 6\nsub c a b");
        new Interpreter().run(vm, program);
        assertEquals(4, vm.c);
    }

    @Test
    public void testMul() {
        Program program = new Parser().parse("set a 10\nset b 6\nmul c a b");
        new Interpreter().run(vm, program);
        assertEquals(60, vm.c);
    }

    @Test
    public void testDiv() {
        Program program = new Parser().parse("set a 20\nset b 10\ndiv c a b");
        new Interpreter().run(vm, program);
        assertEquals(2, vm.c);
    }

    @Test
    public void testDiv2() {
        Program program = new Parser().parse("set a 10\nset b 3\ndiv c a b");
        new Interpreter().run(vm, program);
        assertEquals(3, vm.c);
    }


    @Test
    public void testMod() {
        Program program = new Parser().parse("set a 10\nset b 3\nmod c a b");
        new Interpreter().run(vm, program);
        assertEquals(1, vm.c);
    }

    @Test
    public void testMod2() {
        new Interpreter().run(vm, new Parser().parse("set a 10\nset b 10\nmod c a b"));
        assertEquals(0, vm.c);
    }

    @Test
    public void testPushPop() {
        new Interpreter().run(vm, new Parser().parse("push 10\npop a"));
        assertEquals(10, vm.a);
    }

    @Test(expected = RuntimeException.class)
    public void testPopEmpty() {
        new Interpreter().run(vm, new Parser().parse("pop a"));
    }

    @Test
    public void testPushPop2() {
        new Interpreter().run(vm, new Parser().parse("push 10\npush 20\npop a\npop b"));
        assertEquals(20, vm.a);
        assertEquals(10, vm.b);
    }

    @Test
    public void testPeek() {
        new Interpreter().run(vm, new Parser().parse("push 10\npeek a\npop b"));
        assertEquals(10, vm.a);
        assertEquals(10, vm.b);
    }

    @Test
    public void testRes() {
        int res = new Interpreter().run(vm, new Parser().parse("set a 10\nres a"));
        assertEquals(10, res);
    }

    @Test
    public void testSwap() throws Exception {
        Program program = new Parser().parse(
                "set a 1\n" +
                        "set b 2\n" +
                        "swp a b");

        new Interpreter().run(vm, program);

        Assert.assertEquals(2, vm.a);
        Assert.assertEquals(1, vm.b);
    }

    @Test
    public void testLoadSave() {
        VM vm = new VM(new int[]{3, 0});
        new Interpreter().run(vm, new Parser().parse("load a 0\nsave a 1"));
        assertEquals(3, vm.a);
        assertEquals(3, vm.memory[1]);
    }

    @Test(expected = RuntimeException.class)
    public void testLoadOOMFail() {
        VM vm = new VM(new int[]{3, 0});
        new Interpreter().run(vm, new Parser().parse("load a 2"));
    }

    @Test(expected = RuntimeException.class)
    public void testSaveOOMFail() {
        VM vm = new VM(new int[]{3, 0});
        new Interpreter().run(vm, new Parser().parse("save a 2"));
    }


    @Test
    public void testSet() {
        VM vm = new VM(new int[]{3, 0});
        new Interpreter().run(vm, new Parser().parse("set a 3"));
        assertEquals(3, vm.a);
    }

    @Test(expected = RuntimeException.class)
    public void testSetConstantIllegal() {
        VM vm = new VM(new int[]{3, 0});
        new Interpreter().run(vm, new Parser().parse("set 3 a"));
    }

    @Test
    public void testRes2() {
        int res = new Interpreter().run(vm, new Parser().parse("res 3"));
        assertEquals(3, res);
    }

    @Test
    public void testCallRet() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "call :a\n" +
                        "res a\n" +
                        ":a\n" +
                        "set a 3\n" +
                        "ret"));

        assertEquals(3, res);
    }

    @Test
    public void testCallRet2() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "call :foo\nres a\n:foo\ncall :bar\nret\n:bar\nset a 3\nret"));

        assertEquals(3, res);
    }

    @Test
    public void testJmp() {
        Integer res = new Interpreter().run(vm, new Parser().parse(
                "jmp :a\n" +
                        "res 1\n" +
                        ":a\n" +
                        "res 3\n"));

        assertEquals(new Integer(3), res);
    }

    @Test(expected = RuntimeException.class)
    public void testJmpFail() {
        new Interpreter().run(vm, new Parser().parse(
                ":a\njmp :b\n"));
    }

    @Test
    public void testJmpEPositive() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 1\njmpe :win a b\nres 0\n:win\nres 1"));
        assertEquals(1, res);
    }

    @Test
    public void testJmpENegative() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 0\njmpe :win a b\nres 0\n:win\nres 1"));
        assertEquals(0, res);
    }

    @Test
    public void testJmpNEPositive() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 1\njmpne :win a b\nres 0\n:win\nres 1"));
        assertEquals(0, res);
    }

    @Test
    public void testJmpNENegative() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 0\njmpne :win a b\nres 0\n:win\nres 1"));
        assertEquals(1, res);
    }

    @Test
    public void testJmpGTPositive() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 0\njmpgt :win a b\nres 0\n:win\nres 1"));
        assertEquals(1, res);
    }

    @Test
    public void testJmpGTNegative() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 0\njmpgt :win b a\nres 0\n:win\nres 1"));
        assertEquals(0, res);
    }

    @Test
    public void testJmpLTPositive() {
        int res = new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 0\njmplt :win a b\nres 0\n:win\nres 1"));
        assertEquals(0, res);
    }

    @Test
    public void testJmpLTNegative() {
        int res = new Interpreter().run(vm, new Parser().parse(
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
        vm.setInterpreterMode(true);
        vm.next(1);
        vm.next(1);
        assertEquals(2, vm.ip);
    }

    @Test
    public void testVMToString() {
        VM vm = new VM(new int[0]);
        assertNotEquals("", vm.toString());
    }

    @Test
    public void testSetAllRegisters() {
        new Interpreter().run(vm, new Parser().parse(
                "set a 1\nset b 2\nset c 3\nset d 4"));

        assertEquals(1, vm.a);
        assertEquals(2, vm.b);
        assertEquals(3, vm.c);
        assertEquals(4, vm.d);
    }

    @Test
    public void testReadAllRegisters() {
        int resa = new Interpreter().run(new VM(), new Parser().parse("set a 1\nres a"));
        assertEquals(1, resa);
        int resb = new Interpreter().run(new VM(), new Parser().parse("set b 1\nres b"));
        assertEquals(1, resb);
        int resc = new Interpreter().run(new VM(), new Parser().parse("set c 1\nres c"));
        assertEquals(1, resc);
        int resd = new Interpreter().run(new VM(), new Parser().parse("set d 1\nres d"));
        assertEquals(1, resd);
    }

    @Test(expected = RuntimeException.class)
    public void testInterpreterExceedsMaxSteps() {
        int resa = new Interpreter().run(new VM(), new Parser().parse("set a 1\nset b 2"), 1);
        assertEquals(1, resa);
    }

    @Test(expected = RuntimeException.class)
    public void testParserInvalidOp() {
        new Parser().parse("foo");
    }

    @Test(expected = IOException.class)
    public void testParserFile() throws IOException {
        new Parser().parse(new File("boo"));
    }

    @Test(expected = RuntimeException.class)
    public void testParser1() throws IOException {
        new Parser().parse("set foo bar");
    }

    @Test(expected = RuntimeException.class)
    public void testParser2() throws IOException {
        new Parser().parse("set bar 45.23");
    }

    @Test(expected = RuntimeException.class)
    public void testParser3() throws IOException {
        new Parser().parse("set a");
    }

    @Test
    public void testParserComment() throws IOException {
        new Parser().parse("# hello world\n\nset a 1 # inc\nset b 2 #help\n");
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidRegister() {
        Register r = new Register(null);
        r.getValue(new VM());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegister2() {
        Register r = new Register(null);
        r.setValue(new VM(), 0);
    }

    @Test(expected = RuntimeException.class)
    public void testStackOverflow() {
        VM vm = new VM().setStackSize(0);
        Program p = new Parser().parse("push 1");
        new Interpreter().run(vm, p);
    }

    @Test(expected = RuntimeException.class)
    public void testStackOverflow2() {
        VM vm = new VM().setStackSize(1);
        Program p = new Parser().parse("push 1\npush 2");
        new Interpreter().run(vm, p);
    }

    @Test(expected = RuntimeException.class)
    public void testCallStackOverflow() {
        VM vm = new VM().setCallStackSize(0);
        Program p = new Parser().parse("call :foo\n:foo\nret");
        new Interpreter().run(vm, p);
    }

    @Test(expected = RuntimeException.class)
    public void testCallStackOverflow2() {
        VM vm = new VM().setCallStackSize(1);
        Program p = new Parser().parse("call :foo\n:foo\ncall :bar\nret\n:bar\nret");
        new Interpreter().run(vm, p);
    }
}