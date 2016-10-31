package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class MulOp implements Operation {

    private final Ref a, b, c;

    public MulOp(Ref target, Ref op1, Ref op2) {
        this.a = target;
        this.b = op1;
        this.c = op2;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        a.setValue(vm, b.getValue(vm) * c.getValue(vm));
        vm.next();
        return null;
    }

    @Override
    public String toString() {
        return "add " + a.toString() + " = " + b.toString() + " * " + c.toString();
    }

}
