package net.h34t.enrico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {

    private final List<Operation> operations;
    private final Map<Label, Integer> labelOffsets;


    public Program() {
        this.operations = new ArrayList<>();
        this.labelOffsets = new HashMap<>();
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

    public List<Operation> getOperations() {
        return operations;
    }

    public int getAddressOfLabel(Label label) {
        return labelOffsets.get(label);
    }

    public void setAddressOfLabel(Label label, int offset) {
        labelOffsets.put(label, offset);
    }

}
