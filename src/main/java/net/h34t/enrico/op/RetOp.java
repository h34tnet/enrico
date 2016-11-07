package net.h34t.enrico.op;

import net.h34t.enrico.Encoder;
import net.h34t.enrico.LabelOffsetTranslator;
import net.h34t.enrico.Operation;
import net.h34t.enrico.VM;

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
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, RET);
    }

    @Override
    public String toString() {
        return "ret";
    }

}
