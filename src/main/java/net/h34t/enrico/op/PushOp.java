package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class PushOp implements Operation {

    private final Ref reg;

    public PushOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm) {
        if (vm.stack.size() >= vm.maxStackSize)
            throw new RuntimeException("Stack size exceeded");

        vm.stack.push(reg.getValue(vm));
        vm.next(1);
        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, PUSH, reg);
    }

    @Override
    public String toString() {
        return "push " + reg.toString();
    }
}
