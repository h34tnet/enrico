package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class CallOp implements Operation {

    private final Ref label;

    public CallOp(Ref label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (vm.callStack.size() >= vm.maxCallStackSize)
            throw new RuntimeException("Call Stack size exceeded");

        vm.callStack.push(vm.ip);
        vm.ip = label.getValue(vm);
        return null;
    }

    @Override
    public String toString() {
        return "call " + label.toString();
    }

}
