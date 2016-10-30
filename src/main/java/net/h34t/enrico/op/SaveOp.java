package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

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
}
