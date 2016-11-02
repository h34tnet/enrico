package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class JmpLTEOp implements Operation {


    private final Ref label, op1, op2;

    public JmpLTEOp(Ref label, Ref op1, Ref op2) {
        this.label = label;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (this.op1.getValue(vm) <= this.op2.getValue(vm))
            vm.ip = label.getValue(vm);
        else
            vm.next();
        return null;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(JMPLTE, label, op1, op2);
    }

    @Override
    public String toString() {
        return "jmp to " + label.toString() + " if " + op1.toString() + " <= " + op2.toString();
    }
}
