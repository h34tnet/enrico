package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class SaveOp implements Operation {

    private final Ref register, addr;

    public SaveOp(Ref register, Ref addr) {
        this.register = register;
        this.addr = addr;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (addr.getValue(vm) >= vm.memSize)
            throw new RuntimeException("OOM Access");

        vm.memory[addr.getValue(vm)] = this.register.getValue(vm);
        vm.next();
        return null;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(SAVE, register, addr);
    }

    @Override
    public String toString() {
        return "save " + register.toString() + " => mem[" + addr.toString() + "]";
    }
}
