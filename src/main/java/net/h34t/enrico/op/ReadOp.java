package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

import java.io.IOException;

public class ReadOp implements Operation {

    private final Ref r;

    public ReadOp(Ref r) {
        this.r = r;
    }

    @Override
    public Integer exec(VM vm) {
        try {
            r.setValue(vm, vm.in.read());

        } catch (IOException e) {
            e.printStackTrace();
        }
        vm.next(1);

        return null;
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, READ, r);
    }

    @Override
    public String toString() {
        return "read into " + r.toString();
    }
}
