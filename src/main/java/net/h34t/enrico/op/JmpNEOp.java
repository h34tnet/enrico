package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class JmpNEOp implements Operation {

    private final Label label;
    private final Ref b, c;

    public JmpNEOp(Label label, Ref op1, Ref op2) {
        this.label = label;
        this.b = op1;
        this.c = op2;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (this.b.getValue(vm) != this.c.getValue(vm))
            vm.ip = program.getAddressOfLabel(label);
        else
            vm.next();

        return null;
    }
}
