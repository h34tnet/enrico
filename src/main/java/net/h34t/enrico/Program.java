package net.h34t.enrico;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private final List<Operation> operations;


    public Program() {
        this.operations = new ArrayList<>();
    }

    public void add(Operation op) {
        this.operations.add(op);
    }

    public Operation get(int offs) {
        return this.operations.get(offs);
    }

    public int size() {
        return this.operations.size();
    }

}
