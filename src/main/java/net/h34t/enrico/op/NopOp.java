package net.h34t.enrico.op;

import net.h34t.enrico.Encoder;
import net.h34t.enrico.LabelOffsetTranslator;
import net.h34t.enrico.Operation;
import net.h34t.enrico.VM;

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
    public int[] encode(LabelOffsetTranslator lot) {
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
