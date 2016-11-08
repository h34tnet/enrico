package net.h34t.enrico;

import org.junit.Test;

import java.io.CharArrayReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class IOTest {

    @Test
    public void testReadPrint() {
        VM vm = new VM(256);

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
        vm.setInputReader(new CharArrayReader(input));
        int res = vm.load(new Compiler().compile(p)).exec();
        assertEquals("hello world", out.toString());
        assertEquals(1, res);
    }

}
