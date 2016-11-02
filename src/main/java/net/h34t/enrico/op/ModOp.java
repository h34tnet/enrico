package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class ModOp implements Operation {

    private final Ref a, b, c;

    public ModOp(Ref target, Ref op1, Ref op2) {
        this.a = target;
        this.b = op1;
        this.c = op2;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        a.setValue(vm, b.getValue(vm) % c.getValue(vm));
        vm.next();
        return null;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(MOD, a, b, c);
    }

    @Override
    public String toString() {
        return "add " + a.toString() + " = " + b.toString() + " % " + c.toString();
    }

}
