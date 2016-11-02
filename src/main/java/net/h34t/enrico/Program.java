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

    /**
     * Translates the program into (virtual) machine code.
     *
     * @return the emitted machine code
     */
    public int[] compile() {
        // int size = 0;
        List<Integer> bytecode = new ArrayList<>();

        for (Operation op : operations) {
            // int paramCount = op.getClass().getConstructors()[0].getParameterCount();
            // size += paramCount * 2;
            int[] encop = op.encode();
            for (int iop : encop)
                bytecode.add(iop);
        }

        int[] bc = new int[bytecode.size()];
        for (int i = 0, ii = bc.length; i < ii; i++)
            bc[i] = bytecode.get(i);

        return bc;
    }

}
