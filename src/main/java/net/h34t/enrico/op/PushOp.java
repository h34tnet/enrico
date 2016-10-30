package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

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
}
