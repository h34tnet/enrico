package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class SetOp implements Operation {

    private final Ref a, b;

    public SetOp(Ref a, Ref b) {
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
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, SET, a, b);
    }

    @Override
    public String toString() {
        return "set " + a.toString() + " to " + b.toString();
    }
}
