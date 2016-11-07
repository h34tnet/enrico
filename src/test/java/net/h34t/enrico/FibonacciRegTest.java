package net.h34t.enrico;

import com.sun.xml.internal.messaging.saaj.util.CharReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class FibonacciRegTest {

    @Test
    public void runFibonacciTest() throws IOException {
        Program program = new Parser()
                .parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci.enr")));

        VM vm = new VM(1024);
        vm.setInputReader(new CharReader(new char[]{13}, 1));

        int[] bc = new Compiler()
                .enableDebugOutput(true)
                .compile(program);

        vm.load(bc);
        Integer result = vm.exec();
        Assert.assertEquals(new Integer(144), result);
    }

    @Test
    public void runFibonacciVarTest() throws IOException {
        Program program = new Parser()
                .parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci-var.enr")));

        VM vm = new VM(1024);
        vm.setInputReader(new CharReader(new char[]{13}, 1));

        int[] bc = new Compiler()
                .enableDebugOutput(true)
                .compile(program);

        vm.load(bc);
        Integer result = vm.exec();
        Assert.assertEquals(new Integer(144), result);
    }
}
