package net.h34t.enrico;

import com.sun.xml.internal.messaging.saaj.util.CharReader;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class DivTest {

    @Test
    public void parseHex() {
        VM vm = new VM();
        int res = new Interpreter().run(vm, new Parser().load("set a 0xFF\nres a").compile());

        assertEquals(255, res);
    }

    @Test
    public void parseBinary() {
        assertEquals(8, (int) new Interpreter().run(new VM(), new Parser().load("set a b1000\nres a").compile()));
        assertEquals(0, (int) new Interpreter().run(new VM(), new Parser().load("set a b0\nres a").compile()));
        assertEquals(1, (int) new Interpreter().run(new VM(), new Parser().load("set b b00001\nres b").compile()));
    }

    @Test
    public void testPrint() {
        VM vm = new VM(new int[]{'h', 'e', 'l', 'l', 'o', 0});

        Program p = new Parser().load(
                ":loop\n" +
                        "load a d\n" +
                        "jmpe :exit a 0\n" +
                        "print a\n" +
                        "add d d 1\n" +
                        "jmp :loop\n" +
                        ":exit\n" +
                        "res 1").compile();

        StringWriter out = new StringWriter();
        vm.setPrintWriter(out);
        new Interpreter().run(vm, p);
        assertEquals("hello", out.toString());
    }

    @Test
    public void testReadPrint() {
        VM vm = new VM();

        Program p = new Parser().load(
                ":loop\n" +
                        "read a\n" +
                        "jmpe :exit a 0\n" +
                        "print a\n" +
                        "jmp :loop\n" +
                        ":exit\n" +
                        "res 1").compile();

        char[] input = "hello world\0".toCharArray();

        StringWriter out = new StringWriter();
        vm.setPrintWriter(out);
        vm.setInputReader(new CharReader(input, input.length));
        new Interpreter().run(vm, p);
        assertEquals("hello world", out.toString());
    }

}
