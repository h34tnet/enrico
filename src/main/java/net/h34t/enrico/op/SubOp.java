package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class SubOp implements Operation {

    private final Ref a, b, c;

    public SubOp(Ref target, Ref op1, Ref op2) {
        this.a = target;
        this.b = op1;
        this.c = op2;
    }

    @Override
    public Integer exec(VM vm) {
        a.setValue(vm, b.getValue(vm) - c.getValue(vm));
        vm.next(3);
        return null;
    }

    @Override
    public int length() {
        return 7;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, SUB, a, b, c);
    }

    @Override
    public String toString() {
        return "sub " + a.toString() + " = " + b.toString() + " - " + c.toString();
    }

}
