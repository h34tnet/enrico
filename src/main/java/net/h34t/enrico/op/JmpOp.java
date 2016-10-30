package net.h34t.enrico.op;

import net.h34t.enrico.*;

/**
 * Unconditional jump
 */
public class JmpOp implements Operation {

    private final Label label;

    public JmpOp(Label label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        vm.ip = program.getAddressOfLabel(label);
        return null;
    }
}
