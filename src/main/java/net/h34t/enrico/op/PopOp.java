package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class PopOp implements Operation {

    private final Ref reg;

    public PopOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm) {
        if (vm.stack.size() == 0) throw new RuntimeException("pop on empty stack");
        reg.setValue(vm, vm.stack.pop());
        vm.next(1);
        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, POP, reg);
    }

    @Override
    public String toString() {
        return "pop " + reg.toString();
    }
}
