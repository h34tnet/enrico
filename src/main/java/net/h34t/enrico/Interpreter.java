package net.h34t.enrico;

import java.io.IOException;

/**
 * The interpreter runs a program.
 */
public class Interpreter {

    public Interpreter() {
    }

    private Integer exec(final VM vm, final Operation instruction, final Program program, final int ipLen) {
        Integer res = instruction.exec(vm, program);

        if (res != null)
            return res;

        return null;
    }

    public Integer run(VM vm, Program program) {
        return run(vm, program, 0);
    }

    public Integer run(VM vm, Program program, long maxSteps) {
        final int ipLen = program.size();

        int instrCount = 0;

        Integer result;
        try {
            while ((result = exec(vm, program.get(vm.ip), program, ipLen)) == null) {
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
