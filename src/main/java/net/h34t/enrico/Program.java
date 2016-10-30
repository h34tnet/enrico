package net.h34t.enrico;

import net.h34t.enrico.op.LabelOp;

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
        if (op instanceof LabelOp)
            this.labelOffsets.put(((LabelOp) op).getLabel(), operations.size());

        this.operations.add(op);
    }

    public Operation get(int offs) {
        return this.operations.get(offs);
    }

    public int size() {
        return this.operations.size();
    }

    public int getAddressOfLabel(Label label) {
        Integer addr = labelOffsets.get(label);

        if (addr != null) return addr;
        else throw new RuntimeException("Unknown label \"" + label.getLabel() + "\"");
    }
}
