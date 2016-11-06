package net.h34t.enrico.op;

import net.h34t.enrico.LabelOffsetTranslator;
import net.h34t.enrico.Operation;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class LabelOp implements Operation {

    private final Ref label;

    public LabelOp(Ref label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm) {
        vm.next(1);
        return null;
    }

    public Ref getLabel() {
        return label;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return new int[]{};
    }

    @Override
    public String toString() {
        return "res " + label.toString();
    }
}
