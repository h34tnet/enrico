package net.h34t.enrico;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DivTest {

    @Test
    public void parseHex() {
        VM vm = new VM();
        int res = new Interpreter().run(vm, Parser.load("set a 0xFF\nres a"));

        assertEquals(255, res);
    }

    @Test
    public void parseBinary() {
        assertEquals(8, (int) new Interpreter().run(new VM(), Parser.load("set a b1000\nres a")));
        assertEquals(0, (int) new Interpreter().run(new VM(), Parser.load("set a b0\nres a")));
        assertEquals(1, (int) new Interpreter().run(new VM(), Parser.load("set b b00001\nres b")));
    }

}
