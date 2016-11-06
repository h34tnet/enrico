package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class SwpOp implements Operation {

    private final Ref reg1, reg2;

    public SwpOp(Ref reg1, Ref reg2) {
        this.reg1 = reg1;
        this.reg2 = reg2;
    }

    @Override
    public Integer exec(VM vm) {
        int tmp = reg1.getValue(vm);
        reg1.setValue(vm, reg2.getValue(vm));
        reg2.setValue(vm, tmp);

        vm.next(2);

        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, SWP, reg1, reg2);
    }

    @Override
    public String toString() {
        return "swap " + reg1.toString() + " <-> " + reg2.toString();
    }

}
