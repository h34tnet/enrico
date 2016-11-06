package net.h34t.enrico;

import org.junit.Assert;
import org.junit.Test;

public class CompilationTest {

    public static final String TEST_PROGRAM = "set a 0\nset b 3\n:loop\nadd a a b\njmplte :loop a 10\nres a";

    @Test
    public void programTest() {
        VM vm = new VM();
        Program program = new Parser()
                .parse(TEST_PROGRAM);

        int res = new Interpreter().run(vm, program);

        Assert.assertEquals(12, res);
    }

    @Test
    public void compileTest() {
        Program program = new Parser()
                .parse(TEST_PROGRAM);

        int[] byteCode = new Compiler().compile(program);

        for (int aByteCode : byteCode)
            System.out.print(aByteCode + " ");

        System.out.println();

        Assert.assertEquals(27, byteCode.length);
    }

    @Test
    public void runCompiledTest() {
        Program program = new Parser()
                .parse(TEST_PROGRAM);

        int[] byteCode = new Compiler().compile(program);


        VM vm = new VM(byteCode);
        int res = vm.exec();
        Assert.assertEquals(12, res);
    }
}
