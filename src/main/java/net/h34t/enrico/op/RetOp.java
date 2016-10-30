package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.VM;

public class RetOp implements Operation {

    public RetOp() {
    }

    @Override
    public Integer exec(VM vm, Program program) {
        // one instruction after the call
        vm.ip = vm.callStack.pop() + 1;
        return null;
    }
}
