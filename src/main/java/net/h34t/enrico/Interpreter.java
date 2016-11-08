package net.h34t.enrico;

import net.h34t.enrico.op.LabelOp;

import java.io.IOException;

/**
 * The interpreter runs a program.
 */
@Deprecated
public class Interpreter {

    public Interpreter() {
    }

    private Integer exec(final VM vm, final Operation instruction, final Program program) {
        Integer res = instruction.exec(vm);

        if (res != null)
            return res;

        return null;
    }

    public Integer run(VM vm, Program program) {
        return run(vm, program, 0);
    }

    public Integer run(VM vm, Program program, long maxSteps) {
        // translate addresses before running
        LabelOffsetTranslator lot = new LabelOffsetTranslator();
        for (int i = 0, ii = program.getOperations().size(); i < ii; i++) {
            if (program.get(i) instanceof LabelOp) {
                lot.put((Label) ((LabelOp) program.get(i)).getLabel(), i);
            }
        }

        System.out.println(lot.toString());

        for (Operation op : program.getOperations())
            if (op instanceof Operation.AddressTranslator)
                ((Operation.AddressTranslator) op).translate(lot);

        // run program
        int instrCount = 0;

        vm.setInterpreterMode(true);

        Integer result;
        try {
            while ((result = exec(vm, program.get(vm.ip), program)) == null) {
                if (maxSteps != 0 && ++instrCount > maxSteps)
                    throw new RuntimeException("Program didn't finish in less than " + maxSteps + " steps");
            }

            return result;

        } catch (IndexOutOfBoundsException ioe) {
            // ran past the end
            return null;

        } finally {
            try {
                vm.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
