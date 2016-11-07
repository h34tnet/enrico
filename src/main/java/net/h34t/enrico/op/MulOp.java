package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class MulOp implements Operation {

    private final Ref a, b, c;

    public MulOp(Ref target, Ref op1, Ref op2) {
        this.a = target;
        this.b = op1;
        this.c = op2;
    }

    @Override
    public Integer exec(VM vm) {
        a.setValue(vm, b.getValue(vm) * c.getValue(vm));
        vm.next(3);
        return null;
    }

    @Override
    public int length() {
        return 7;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, MUL, a, b, c);
    }

    @Override
    public String toString() {
        return "add " + a.toString() + " = " + b.toString() + " * " + c.toString();
    }

}
