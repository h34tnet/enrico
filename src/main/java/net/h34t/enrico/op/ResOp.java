package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

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
    public int length() {
        return 3;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, RES, reg);
    }

    @Override
    public String toString() {
        return "res " + reg.toString();
    }
}
