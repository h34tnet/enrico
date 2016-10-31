package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class SetOp implements Operation {

    private final Ref a, b;

    public SetOp(Ref a, Ref b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        this.a.setValue(vm, this.b.getValue(vm));
        vm.next();

        return null;
    }

    @Override
    public String toString() {
        return "set " + a.toString() + " to " + b.toString();
    }
}
