package net.h34t.enrico.op;

import net.h34t.enrico.Compiler;
import net.h34t.enrico.Operation;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class LoadOp implements Operation {

    private final Ref register;
    private final String var;

    public LoadOp(Ref register, String var) {
        this.register = register;
        this.var = var;
    }

    @Override
    public Integer exec(VM vm) {
//        if (addr.getValue(vm) >= vm.memSize)
//            throw new RuntimeException("OOM Access");
//
//        this.register.setValue(vm, vm.memory[addr.getValue(vm)]);
//        vm.next(2);

        return null;
    }

    @Override
    public int[] encode(Compiler lot) {
        return new int[]{LOAD, register.encode(), lot.getVarAddr(var)};
    }

    @Override
    public int length() {
        return 5;
    }

    @Override
    public String toString() {
        return "load " + register.toString() + " <= mem[" + var + "]";
    }
}
