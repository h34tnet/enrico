package net.h34t.enrico;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes the intermediate representation of a program and turns it into byte code.
 */
public class Compiler {

    /**
     * Translates the program into (virtual) machine code.
     *
     * @return the emitted machine code
     */
    public int[] compile(Program program) {
        // int size = 0;
        List<Integer> bytecode = new ArrayList<>();

        for (Operation op : program.getOperations()) {
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
