package net.h34t.enrico;

import com.sun.xml.internal.messaging.saaj.util.CharReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

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

        int[] byteCode = new Compiler()
                .enableDebugOutput(true)
                .compile(program);

        System.out.println(Compiler.toString(byteCode));

        VM vm = new VM(256);
        vm.loadProgram(byteCode);
         int res = vm.exec();
         Assert.assertEquals(12, res);
    }

    @Test
    public void runFibonacciTest() throws IOException {
        Program program = new Parser()
                .parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci.enr")));

        VM vm = new VM(1024);
        vm.setInputReader(new CharReader(new char[]{13}, 1));
        vm.loadProgram(new Compiler().compile(program));
        Integer result = vm.exec();

        Assert.assertEquals(new Integer(144), result);

    }
}
