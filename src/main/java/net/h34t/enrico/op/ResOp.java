package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class ResOp implements Operation {

    private final Ref reg;

    public ResOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        int val = reg.getValue(vm);
        vm.next();
        return val;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(RES, reg);
    }

    @Override
    public String toString() {
        return "res " + reg.toString();
    }
}
