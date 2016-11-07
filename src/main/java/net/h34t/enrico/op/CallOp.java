package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class CallOp implements Operation, Operation.AddressTranslator {

    private Ref label;

    public CallOp(Ref label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm) {
        if (vm.callStack.size() >= vm.maxCallStackSize)
            throw new RuntimeException("Call Stack size exceeded");

        vm.callStack.push(vm.ip + (vm.interpreterMode ? 1 : 3));
        vm.ip = label.getValue(vm);
        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, CALL, label);
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public void translate(LabelOffsetTranslator translator) {
        if (label instanceof Label)
            label = new Constant(translator.get((Label) label));
    }

    @Override
    public String toString() {
        return "call " + label.toString();
    }
}
