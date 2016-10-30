package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class JmpGTOp implements Operation {

    private final Label label;
    private final Ref op1, op2;

    public JmpGTOp(Label label, Ref op1, Ref op2) {
        this.label = label;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (this.op1.getValue(vm) > this.op2.getValue(vm))
            vm.ip = program.getAddressOfLabel(label);
        else
            vm.next();
        return null;
    }
}
