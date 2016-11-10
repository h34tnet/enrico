package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class RetOp implements Operation {

    public RetOp() {
    }

    @Override
    public Integer exec(VM vm) {
        // one instruction after the call
        vm.ip = vm.callStack.pop();
        return null;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, RET);
    }

    @Override
    public String toString() {
        return "ret";
    }

}
