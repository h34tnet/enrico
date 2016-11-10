package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

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
    public int length() {
        return 3;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, PUSH, reg);
    }

    @Override
    public String toString() {
        return "push " + reg.toString();
    }
}
