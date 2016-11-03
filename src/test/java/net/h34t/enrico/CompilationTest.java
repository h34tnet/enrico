package net.h34t.enrico;

import org.junit.Assert;
import org.junit.Test;

public class CompilationTest {

    @Test
    public void compileTest() {
        VM vm = new VM();
        Program program = new Parser()
                .load("set a 2\nset b 3\nadd a a b\nres a")
                .compile();

        int[] byteCode = new Compiler().compile(program);

        for (int aByteCode : byteCode)
            System.out.print(aByteCode + " ");
        
        System.out.println();

        int res = new Interpreter().run(vm, program);

        Assert.assertEquals(5, res);
        Assert.assertEquals(20, byteCode.length);

    }
}
