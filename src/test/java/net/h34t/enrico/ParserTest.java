package net.h34t.enrico;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ParserTest extends InstantRunner {

    @Test
    public void parseNegativeNumber() {
        int res = vm.load(new Compiler().compile(new Parser().parse("set a -10\nres a"))).exec();
        assertEquals(-10, res);
    }


    @Test(expected = RuntimeException.class)
    public void parseFloatInvalid() {
        int res = vm.load(new Compiler().compile(new Parser().parse("set a 1.2\nres a"))).exec();
        assertEquals(-10, res);
    }

    @Test
    public void parseHex() {
        int res = vm.load(new Compiler().compile(new Parser().parse("set a 0xFF\nres a"))).exec();
        assertEquals(255, res);
    }

    @Test(expected = RuntimeException.class)
    public void parseHexInvalid() {
        new VM(256).load(new Compiler().compile(new Parser().parse("set a 0xG\nres a"))).exec();
    }

    @Test
    public void parseBinary() {
        assertEquals(8, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set a b1000\nres a"))).exec());
        assertEquals(0, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set a b0\nres a"))).exec());
        assertEquals(1, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set b b00001\nres b"))).exec());
    }

    @Test(expected = RuntimeException.class)
    public void parseBinaryInvalid() {
        new VM(256).load(new Compiler().compile(new Parser().parse("set a b2\nres a"))).exec();
    }

    @Test
    public void parseChar() {
        // 98 is the ascii code of the character 'b'
        assertEquals(98, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set a 'b'\nres a"))).exec());
    }

    @Test
    public void parseChar2() {
        // 98 is the ascii code of the character 'b'
        assertEquals(0, (int) new VM(256).load(new Compiler().compile(new Parser().parse("set a '\0'\nres a"))).exec());
    }

    @Test(expected = RuntimeException.class)
    public void testParserInvalidOp() {
        new Parser().parse("foo");
    }


    @Test(expected = IOException.class)
    public void testParserFile() throws IOException {
        new Parser().parse(new File("boo"));
    }

    @Test(expected = RuntimeException.class)
    public void testParser1() throws IOException {
        new Parser().parse("set foo bar");
    }

    @Test(expected = RuntimeException.class)
    public void testParser2() throws IOException {
        new Parser().parse("set bar 45.23");
    }

    @Test(expected = RuntimeException.class)
    public void testParser3() throws IOException {
        new Parser().parse("set a");
    }

    @Test
    public void testParserComment() throws IOException {
        new Parser().parse("# hello world\n\nset a 1 # inc\nset b 2 #help\n");
    }

    @Test
    public void testMemoryOffset() throws IOException {
        new Parser().parse("def $a\ndef $b\nset $a @b\nres $a\n");
    }
}
