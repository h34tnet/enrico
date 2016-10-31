package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class ResOp implements Operation {

    private final Ref reg;

    public ResOp(Ref reg) {
        this.reg = reg;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        int val = reg.getValue(vm);
        vm.next();
        return val;
    }

    @Override
    public String toString() {
        return "res " + reg.toString();
    }
}
