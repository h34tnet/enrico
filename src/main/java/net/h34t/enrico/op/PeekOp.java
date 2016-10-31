package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class PeekOp implements Operation {

    private final Ref reg;

    public PeekOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (vm.stack.size() == 0) throw new RuntimeException("peek on empty stack");
        reg.setValue(vm, vm.stack.peek());
        vm.next();
        return null;
    }

    @Override
    public String toString() {
        return "peek " + reg.toString();
    }
}
