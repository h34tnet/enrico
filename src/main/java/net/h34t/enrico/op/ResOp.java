package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class ResOp implements Operation {

    private final Ref reg;

    public ResOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm) {
        // vm.next(1);
        return reg.getValue(vm);
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, RES, reg);
    }

    @Override
    public String toString() {
        return "res " + reg.toString();
    }
}
