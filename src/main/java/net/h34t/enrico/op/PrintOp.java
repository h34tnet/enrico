package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

import java.io.IOException;

public class PrintOp implements Operation {

    private final Ref r;

    public PrintOp(Ref r) {
        this.r = r;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        try {
            vm.out.write(r.getValue(vm));
        } catch (IOException e) {
            e.printStackTrace();
        }
        vm.next();

        return null;
    }
}
