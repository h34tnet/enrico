package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class PeekOp implements Operation {

    private final Ref reg;

    public PeekOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm) {
        if (vm.stack.size() == 0) throw new RuntimeException("peek on empty stack");
        reg.setValue(vm, vm.stack.peek());
        vm.next(1);
        return null;
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, PEEK, reg);
    }

    @Override
    public String toString() {
        return "peek " + reg.toString();
    }
}
