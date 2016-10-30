package net.h34t.enrico;

import net.h34t.enrico.Interpreter;
import net.h34t.enrico.Parser;
import net.h34t.enrico.Program;
import net.h34t.enrico.VM;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class ProgramTest {

    @Test
    public void testAddAll() throws IOException {
        Program p = Parser.load(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("addtest.enr")));

        VM vm = new VM(new int[]{1, 2, 3, 4, 5, 0, 1, 2, 3});

        int res = new Interpreter().run(vm, p);
        Assert.assertEquals(15, res);
    }

    @Test
    public void testAddAll2() throws IOException {
        Program p = Parser.load(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("addtest.enr")));

        VM vm = new VM(new int[]{0, 1, 0});

        int res = new Interpreter().run(vm, p);
        Assert.assertEquals(0, res);
    }


    @Test
    public void testFibonacci() throws IOException {
        Program p = Parser.load(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci.enr")));

        // special cases
        Assert.assertEquals(0, (int) new Interpreter().run(new VM(new int[]{1}), p));
        Assert.assertEquals(1, (int) new Interpreter().run(new VM(new int[]{2}), p));
        Assert.assertEquals(1, (int) new Interpreter().run(new VM(new int[]{3}), p));
        Assert.assertEquals(2, (int) new Interpreter().run(new VM(new int[]{4}), p));

        // the 7th fibonacci number should be 8
        Assert.assertEquals(8, (int) new Interpreter().run(new VM(new int[]{7}), p));

        // the 13th fibonacci number should be 144
        Assert.assertEquals(144, (int) new Interpreter().run(new VM(new int[]{13}), p));
    }

}
