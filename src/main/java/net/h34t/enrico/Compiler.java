package net.h34t.enrico;

import net.h34t.enrico.op.DefOp;
import net.h34t.enrico.op.LabelOp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Takes the intermediate representation of a program and turns it into byte code.
 */
public class Compiler {

    /**
     * var offset table: stores memory addresses of variables
     * <p>
     * memory addresses can be calculated after gathering the size of the code segment
     */
    final Map<String, VariableDefinition> vot;

    /**
     * label offset table: stores memory addresses of label locations
     */
    final Map<String, Integer> lot;

    private boolean enableDebug;


    public Compiler() {
        this.vot = new HashMap<>();
        this.lot = new HashMap<>();
    }

    public static String toString(int[] byteCode) {
        StringBuilder sb = new StringBuilder();

        for (int code : byteCode)
            sb.append(code).append(" ");

        return sb.toString();
    }

    public static String bcDebug(int[] bc) {
        return Arrays.stream(bc)
                .mapToObj(b -> String.format("%d", b))
                .collect(Collectors.joining(", "));
    }

    /**
     * Concatenates two int arrays and returns the resulting one.
     *
     * @param code the first one
     * @param data the second one
     * @return the resulting array
     */
    public static int[] concat(int[] code, int[] data) {
        int al = code.length;
        int bl = data.length;
        int[] res = new int[al + bl];
        System.arraycopy(code, 0, res, 0, al);
        System.arraycopy(data, 0, res, al, bl);
        return res;
    }

    /**
     * Emits the compiled instructions incl. byte code rep
     *
     * @param enableDebug true to enable debug output
     * @return this for chaining
     */
    public Compiler enableDebugOutput(boolean enableDebug) {
        this.enableDebug = enableDebug;
        return this;
    }

    /**
     * Translates the program into (virtual) machine code.
     *
     * @return the emitted machine code
     */
    public int[] compile(Program program) {
        int insOffset = 0;
        int memOffset = 0;

        for (Operation op : program.getOperations()) {
            if (op instanceof LabelOp) {
                lot.put(((LabelOp) op).getLabel(), insOffset);

            } else if (op instanceof DefOp) {
                DefOp defOp = (DefOp) op;
                vot.put(defOp.getName(), new VariableDefinition(defOp.getName(), defOp.getSize(), memOffset));
                memOffset += defOp.getSize();

            } else {
                // mock encoding to get the size
                // this is constant for each operator, so this wouldn't be *needed*
                // but currently i'm too lazy to build and maintain a table by hand
                insOffset += op.encode(this).length;
            }
        }

        if (enableDebug) {
            System.out.printf("Calculated program length: " + insOffset);

            System.out.printf("LabelOffsetTranslation table:%n%s%nCompiler output:%n", lot.toString());
        }

        // the second pass is about variable memory allocation


        // third pass generates the actual byte code
        // after applying the label address translation if needed
        List<Integer> byteCode = new ArrayList<>();

        for (Operation op : program.getOperations()) {
            int[] encOp = op.encode(this);
            for (int iop : encOp) {
                byteCode.add(iop);
            }

            if (enableDebug)
                System.out.printf("%8d: %-64s [%s]%n", insOffset, op.toString(), bcDebug(encOp));
        }

        if (enableDebug)
            System.out.println();

        int[] bc = new int[byteCode.size()];
        for (int i = 0, ii = bc.length; i < ii; i++)
            bc[i] = byteCode.get(i);

        return bc;
    }

    public int getVarAddr(String varname) {
        return vot.get(varname).memOffset;
    }

    public interface AddressTranslator {
        void translate(LabelOffsetTranslator translator);
    }

    private class VariableDefinition {

        public final String name;
        public final int size;
        public int memOffset;

        public VariableDefinition(String name, int size, int memOffset) {
            this.name = name;
            this.size = size;
            this.memOffset = memOffset;
        }
    }

}
