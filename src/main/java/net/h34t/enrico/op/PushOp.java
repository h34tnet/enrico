package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class PushOp implements Operation {

    private final Ref reg;

    public PushOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (vm.stack.size() >= vm.maxStackSize)
            throw new RuntimeException("Stack size exceeded");

        vm.stack.push(reg.getValue(vm));
        vm.next();
        return null;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(PUSH, reg);
    }

    @Override
    public String toString() {
        return "push " + reg.toString();
    }
}
