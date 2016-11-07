package net.h34t.enrico;

import com.sun.xml.internal.messaging.saaj.util.CharReader;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class DivTest {

    @Test
    public void parseHex() {
        int res = new VM(256).load(new Compiler().compile(new Parser().parse("set a 0xFF\nres a"))).exec();
        assertEquals(255, res);
    }

    @Test
    public void parseBinary() {
        assertEquals(8, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set a b1000\nres a"))).exec());
        assertEquals(0, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set a b0\nres a"))).exec());
        assertEquals(1, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set b b00001\nres b"))).exec());
    }

    @Test
    public void testPrint() {
        VM vm = new VM(256);

        Program p = new Parser().parse(
                ":loop\n" +
                        "read a d\n" +
                        "jmpe :exit a 0\n" +
                        "print a\n" +
                        "add d d 1\n" +
                        "jmp :loop\n" +
                        ":exit\n" +
                        "res 1");

        char[] input = new char[]{'h', 'e', 'l', 'l', 'o', 0};

        StringWriter out = new StringWriter();
        vm.setPrintWriter(out);
        vm.setInputReader(new CharReader(input, input.length));

        int res = vm.load(new Compiler().compile(p)).exec();

        assertEquals("hello", out.toString());
        assertEquals(1, res);
    }

    @Test
    public void testReadPrint() {
        VM vm = new VM();

        Program p = new Parser().parse(
                ":loop\n" +
                        "read a\n" +
                        "jmpe :exit a 0\n" +
                        "print a\n" +
                        "jmp :loop\n" +
                        ":exit\n" +
                        "res 1");

        char[] input = "hello world\0".toCharArray();

        StringWriter out = new StringWriter();
        vm.setPrintWriter(out);
        vm.setInputReader(new CharReader(input, input.length));
        new Interpreter().run(vm, p);
        assertEquals("hello world", out.toString());
    }

}
