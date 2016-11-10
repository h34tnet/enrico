package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

/**
 * Writes the value of register to the memory location of addr.
 */
public class SaveOp implements Operation {

    private final Reg register;
    private final String var;

    public SaveOp(Reg register, String var) {
        this.register = register;
        this.var = var;
    }

    @Override
    public Integer exec(VM vm) {
//        if (addr.getValue(vm) >= vm.memSize)
//            throw new RuntimeException("OOM Access");
//
//        vm.memory[addr.getValue(vm)] = this.register.getValue(vm);
//        vm.next(2);
        return null;
    }

    @Override
    public int length() {
        return 5;
    }

    @Override
    public int[] encode(Compiler c) {
        return new int[]{SAVE, register.encode(), c.getVarAddr(var)};
    }

    @Override
    public String toString() {
        return "save " + register.toString() + " => mem[" + var + "]";
    }
}
