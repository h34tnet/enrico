package net.h34t.enrico.op;

import net.h34t.enrico.Label;
import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.VM;

public class LabelOp implements Operation {

    private final Label label;

    public LabelOp(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        vm.next();
        return null;
    }
}
