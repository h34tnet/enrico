package net.h34t.enrico.op;

import net.h34t.enrico.Label;
import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.VM;

public class CallOp implements Operation {

    private final Label label;

    public CallOp(Label label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (vm.callStack.size() >= vm.maxCallStackSize)
            throw new RuntimeException("Call Stack size exceeded");

        vm.callStack.push(vm.ip);
        vm.ip = program.getAddressOfLabel(label);
        return null;
    }
}
