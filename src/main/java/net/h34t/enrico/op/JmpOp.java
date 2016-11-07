package net.h34t.enrico.op;

import net.h34t.enrico.*;

/**
 * Unconditional jump
 */
public class JmpOp implements Operation, Operation.AddressTranslator {

    private Ref label;

    public JmpOp(Ref label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm) {
        vm.ip = label.getValue(vm);
        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, JMP, label);
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public String toString() {
        return "jmp to " + label.toString();
    }

    @Override
    public void translate(LabelOffsetTranslator translator) {
        if (label instanceof Label)
            label = new Constant(translator.get((Label) label));
    }
}
