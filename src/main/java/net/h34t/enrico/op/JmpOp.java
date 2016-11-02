package net.h34t.enrico.op;

import net.h34t.enrico.*;

/**
 * Unconditional jump
 */
public class JmpOp implements Operation {

    private final Ref label;

    public JmpOp(Ref label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        vm.ip = label.getValue(vm);
        return null;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(JMP);
    }

    @Override
    public String toString() {
        return "jmp to " + label.toString();
    }
}
