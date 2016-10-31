package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class PopOp implements Operation {

    private final Ref reg;

    public PopOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (vm.stack.size() == 0) throw new RuntimeException("pop on empty stack");
        reg.setValue(vm, vm.stack.pop());
        vm.next();
        return null;
    }

    @Override
    public String toString() {
        return "pop " + reg.toString();
    }
}
