package net.h34t.enrico;

import com.sun.xml.internal.messaging.saaj.util.CharReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProgramTest extends InstantRunner {

    @Test
    public void testAddAll() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("addtest.enr")));

        int[] bc = new Compiler().enableDebugOutput(false).compile(p);

        int res = vm.load(bc)
                .enableDebugMode(false)
                .setPrintWriter(new PrintWriter(System.out))
                .data(new int[]{1, 2, 3, 4, 5, 0, 1, 2, 3}).exec();

        Assert.assertEquals(15, res);
    }

    @Test
    public void testAddAll2() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("addtest.enr")));

        int res = vm.load(c(p)).data(new int[]{0, 1, 0}).exec();

        Assert.assertEquals(0, res);
    }


    @Test
    public void testFibonacci() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci.enr")));

        // special cases
        Assert.assertEquals(0, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{1}, 1)).exec());
        Assert.assertEquals(1, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{2}, 1)).exec());
        Assert.assertEquals(1, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{3}, 1)).exec());
        Assert.assertEquals(2, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{4}, 1)).exec());

        // the 7th fibonacci number should be 8
        Assert.assertEquals(8, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{7}, 1)).exec());

        // the 13th fibonacci number should be 144
        Assert.assertEquals(144, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{13}, 1)).exec());
    }

    @Test
    public void testFibonacciVar() throws IOException {
        Program p = new Parser().parse(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fibonacci-var.enr")));

        // special cases
        Assert.assertEquals(0, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{1}, 1)).exec());
        Assert.assertEquals(1, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{2}, 1)).exec());
        Assert.assertEquals(1, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{3}, 1)).exec());
        Assert.assertEquals(2, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{4}, 1)).exec());

        // the 7th fibonacci number should be 8
        Assert.assertEquals(8, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{7}, 1)).exec());

        // the 13th fibonacci number should be 144
        Assert.assertEquals(144, (int) vm.load(c(p)).setInputReader(new CharReader(new char[]{13}, 1)).exec());
    }

}
