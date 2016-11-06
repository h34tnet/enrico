package net.h34t.enrico;

import net.h34t.enrico.op.LabelOp;

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
        // first pass gathers the label offsets
        LabelOffsetTranslator lot = new LabelOffsetTranslator();
        int offset = 1;

        for (Operation op : program.getOperations()) {
            if (op instanceof LabelOp)
                lot.put((Label) ((LabelOp) op).getLabel(), offset);
            else
                offset += op.encode(lot).length;
        }

        // System.out.println(lot.toString());

        // second pass generates the actual bytecode
        List<Integer> bytecode = new ArrayList<>();

        for (Operation op : program.getOperations()) {
            if (op instanceof Operation.AddressTranslator)
                ((Operation.AddressTranslator) op).translate(lot);

            // int paramCount = op.getClass().getConstructors()[0].getParameterCount();
            // size += paramCount * 2;
            int[] encop = op.encode(lot);
            for (int iop : encop)
                bytecode.add(iop);
        }

        // add the program size as the first instruction
        bytecode.add(0, bytecode.size());

        int[] bc = new int[bytecode.size()];
        for (int i = 0, ii = bc.length; i < ii; i++)
            bc[i] = bytecode.get(i);

        return bc;
    }
}
