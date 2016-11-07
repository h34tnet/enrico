package net.h34t.enrico.op;

import net.h34t.enrico.*;

public class JmpGTEOp implements Operation, Operation.AddressTranslator {

    private final Ref op1, op2;
    private Ref label;

    public JmpGTEOp(Ref label, Ref op1, Ref op2) {
        this.label = label;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Integer exec(VM vm) {
        if (this.op1.getValue(vm) >= this.op2.getValue(vm))
            vm.ip = label.getValue(vm);
        else
            vm.next(3);
        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, JMPGTE, label, op1, op2);
    }

    @Override
    public int length() {
        return 7;
    }

    @Override
    public String toString() {
        return "jmpgte to " + label.toString() + " if " + op1.toString() + " >= " + op2.toString();
    }

    @Override
    public void translate(LabelOffsetTranslator translator) {
        if (label instanceof Label)
            label = new Constant(translator.get((Label) label));
    }
}
