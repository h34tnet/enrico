package net.h34t.enrico.op;

import net.h34t.enrico.Compiler;
import net.h34t.enrico.Operation;
import net.h34t.enrico.VM;

public class DefOp implements Operation {

    private final String name;
    private int size;

    public DefOp(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public Integer exec(VM vm) {
        return null;
    }

    @Override
    public int[] encode(Compiler lot) {
        return new int[]{};
    }

    @Override
    public int length() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "def " + name + ": " + size;
    }
}
