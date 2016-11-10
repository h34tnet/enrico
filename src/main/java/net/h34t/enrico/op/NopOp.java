package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

/**
 * Does nothing (except advancing the IP)
 */
public class NopOp implements Operation {

    public NopOp() {
    }

    @Override
    public Integer exec(VM vm) {
        vm.next(1);
        return null;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, NOP);
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public String toString() {
        return "nop";
    }
}
