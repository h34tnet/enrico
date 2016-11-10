package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class AddOp implements Operation {

    private final Ref target, op1, op2;

    public AddOp(Ref target, Ref op1, Ref op2) {
        this.target = target;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Integer exec(VM vm) {
        target.setValue(vm, op1.getValue(vm) + op2.getValue(vm));
        vm.next(3);
        return null;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(ADD, target, op1, op2);
    }

    @Override
    public int length() {
        return 7;
    }

    @Override
    public String toString() {
        return "add " + target.toString() + " = " + op1.toString() + " + " + op2.toString();
    }
}
