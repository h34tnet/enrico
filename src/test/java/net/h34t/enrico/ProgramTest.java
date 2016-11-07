package net.h34t.enrico;

import com.sun.xml.internal.messaging.saaj.util.CharReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class ProgramTest {

    @Test
    public void testAddAll() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("addtest.enr")));

        VM vm = new VM(new int[]{1, 2, 3, 4, 5, 0, 1, 2, 3});

        int res = new Interpreter().run(vm, p);
        Assert.assertEquals(15, res);
    }

    @Test
    public void testAddAll2() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("addtest.enr")));

        VM vm = new VM(new int[]{0, 1, 0});

        int res = new Interpreter().run(vm, p);
        Assert.assertEquals(0, res);
    }


    @Test
    public void testFibonacci() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci.enr")));

        // special cases
        Assert.assertEquals(0, (int) new Interpreter().run(new VM(new int[]{1024}).setInputReader(new CharReader(new char[]{1}, 1)), p));
        Assert.assertEquals(1, (int) new Interpreter().run(new VM(new int[]{1024}).setInputReader(new CharReader(new char[]{2}, 1)), p));
        Assert.assertEquals(1, (int) new Interpreter().run(new VM(new int[]{1024}).setInputReader(new CharReader(new char[]{3}, 1)), p));
        Assert.assertEquals(2, (int) new Interpreter().run(new VM(new int[]{1024}).setInputReader(new CharReader(new char[]{4}, 1)), p));

        // the 7th fibonacci number should be 8
        Assert.assertEquals(8, (int) new Interpreter().run(new VM(new int[]{1024}).setInputReader(new CharReader(new char[]{7}, 1)), p));

        // the 13th fibonacci number should be 144
        Assert.assertEquals(144, (int) new Interpreter().run(new VM(new int[]{1024}).setInputReader(new CharReader(new char[]{13}, 1)), p));
    }

    @Test
    public void testFibonacciVar() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci-var.enr")));

        // special cases
        Assert.assertEquals(0, (int) new Interpreter().run(new VM(new int[4]).setInputReader(new CharReader(new char[]{1}, 1)), p));
        Assert.assertEquals(1, (int) new Interpreter().run(new VM(new int[4]).setInputReader(new CharReader(new char[]{2}, 1)), p));
        Assert.assertEquals(1, (int) new Interpreter().run(new VM(new int[4]).setInputReader(new CharReader(new char[]{3}, 1)), p));
        Assert.assertEquals(2, (int) new Interpreter().run(new VM(new int[4]).setInputReader(new CharReader(new char[]{4}, 1)), p));
        Assert.assertEquals(8, (int) new Interpreter().run(new VM(new int[4]).setInputReader(new CharReader(new char[]{7}, 1)), p));
        Assert.assertEquals(144, (int) new Interpreter().run(new VM(new int[4]).setInputReader(new CharReader(new char[]{13}, 1)), p));
    }

}
