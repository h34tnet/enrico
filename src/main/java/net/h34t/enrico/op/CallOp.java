package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class CallOp implements Operation, Compiler.AddressTranslator {

    private Ref label;

    public CallOp(Ref label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm) {
        if (vm.callStack.size() >= vm.maxCallStackSize)
            throw new RuntimeException("Call Stack size exceeded");

        // return to the instruction after the call
        vm.callStack.push(vm.ip + 3);

        vm.ip = label.getValue(vm);
        return null;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, CALL, label);
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public void translate(LabelOffsetTranslator translator) {
        if (label instanceof Label)
            label = new Const(translator.get((Label) label));
    }

    @Override
    public String toString() {
        return "call " + label.toString();
    }
}
