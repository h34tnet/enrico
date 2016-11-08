package net.h34t.enrico;

import org.junit.Assert;
import org.junit.Test;

public class CompilationTest {

    public static final String TEST_PROGRAM = "set a 0\nset b 3\n:loop\nadd a a b\njmplte :loop a 10\nres a";

    @Test
    public void programTest() {
        VM vm = new VM(1024);
        Program program = new Parser()
                .parse(TEST_PROGRAM);

        int res = vm.load(new Compiler().compile(program)).exec();

        Assert.assertEquals(12, res);
    }

    @Test
    public void compileTest() {
        Program program = new Parser()
                .parse(TEST_PROGRAM);

        int[] byteCode = new Compiler().compile(program);

        Assert.assertEquals(27, byteCode.length);
    }

    @Test
    public void runCompiledTest() {
        Program program = new Parser()
                .parse(TEST_PROGRAM);

        int[] byteCode = new Compiler()
                .enableDebugOutput(false)
                .compile(program);

        VM vm = new VM(256);
        vm.load(byteCode);
        int res = vm.exec();
        Assert.assertEquals(12, res);
    }


    @Test(expected = RuntimeException.class)
    public void testJmpFail() {
        new Compiler().compile(new Parser().parse(":a\njmp :b"));
    }

}
