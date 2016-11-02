package net.h34t.enrico.op;

import net.h34t.enrico.*;

import java.io.IOException;

public class ReadOp implements Operation {

    private final Ref r;

    public ReadOp(Ref r) {
        this.r = r;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        try {
            r.setValue(vm, vm.in.read());

        } catch (IOException e) {
            e.printStackTrace();
        }
        vm.next();

        return null;
    }

    @Override
    public int[] encode() {
        return Encoder.encode(READ, r);
    }

    @Override
    public String toString() {
        return "read into " + r.toString();
    }
}
