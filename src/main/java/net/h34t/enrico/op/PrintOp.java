package net.h34t.enrico.op;

import net.h34t.enrico.*;

import java.io.IOException;

public class PrintOp implements Operation {

    private final Ref r;

    public PrintOp(Ref r) {
        this.r = r;
    }

    @Override
    public Integer exec(VM vm) {
        try {
            vm.out.write(r.getValue(vm));
        } catch (IOException e) {
            e.printStackTrace();
        }
        vm.next(1);

        return null;
    }

    @Override
    public int[] encode(LabelOffsetTranslator lot) {
        return Encoder.encode(lot, PRINT, r);
    }

    @Override
    public String toString() {
        return "print " + r.toString();
    }
}
