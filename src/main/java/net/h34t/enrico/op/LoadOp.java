package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class LoadOp implements Operation {

    private final Ref register, addr;

    public LoadOp(Ref register, Ref addr) {
        this.register = register;
        this.addr = addr;
    }

    @Override
    public Integer exec(VM vm) {
        if (addr.getValue(vm) >= vm.memSize)
            throw new RuntimeException("OOM Access");

        this.register.setValue(vm, vm.memory[addr.getValue(vm)]);
        vm.next(2);

        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, LOAD, register, addr);
    }

    @Override
    public int length() {
        return 5;
    }

    @Override
    public String toString() {
        return "load " + register.toString() + " <= mem[" + addr.toString() + "]";
    }
}
