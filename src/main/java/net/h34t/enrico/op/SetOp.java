package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class SetOp implements Operation {

    private final Reg a, b;

    public SetOp(Reg a, Reg b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Integer exec(VM vm) {
        this.a.setValue(vm, this.b.getValue(vm));
        vm.next(2);

        return null;
    }

    @Override
    public int length() {
        return 5;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, SET, a, b);
    }

    @Override
    public String toString() {
        return "set " + a.toString() + " to " + b.toString();
    }
}
