package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class SaveOp implements Operation {

    private final Ref register, addr;

    public SaveOp(Ref register, Ref addr) {
        this.register = register;
        this.addr = addr;
    }

    @Override
    public Integer exec(VM vm) {
        if (addr.getValue(vm) >= vm.memSize)
            throw new RuntimeException("OOM Access");

        vm.memory[addr.getValue(vm)] = this.register.getValue(vm);
        vm.next(2);
        return null;
    }

    @Override
    public int length() {
        return 5;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, SAVE, register, addr);
    }

    @Override
    public String toString() {
        return "save " + register.toString() + " => mem[" + addr.toString() + "]";
    }
}
