package net.h34t.enrico.op;

import net.h34t.enrico.Compiler;
import net.h34t.enrico.Operation;
import net.h34t.enrico.VM;

/**
 * Defines a target for jmp* operations.
 * <p>
 * Note that label doesn't emit byte code - it's converted to a memory offset to jump to during the compile step.
 */
public class LabelOp implements Operation {

    private final String label;

    public LabelOp(String label) {
        this.label = label;
    }

    @Override
    public Integer exec(VM vm) {
        vm.next(1);
        return null;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public int[] encode(Compiler lot) {
        return new int[]{};
    }

    @Override
    public String toString() {
        return label;
    }
}
