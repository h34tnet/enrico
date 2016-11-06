package net.h34t.enrico;

import net.h34t.enrico.op.AddOp;
import net.h34t.enrico.op.JmpLTEOp;
import net.h34t.enrico.op.ResOp;
import net.h34t.enrico.op.SetOp;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Stack;

/**
 * Stores memory, stacks and registers.
 */
public class VM {

    private static final String NL = String.format("%n");

    public final Stack<Integer> stack;
    public final Stack<Integer> callStack;
    public final int memSize;
    public int[] memory;
    public int maxStackSize = 256;
    public int maxCallStackSize = 256;
    public int memOffs;

    public Writer out = new PrintWriter(System.out);
    public Reader in;

    public int ip = 0, a = 0, b = 0, c = 0, d = 0;
    public boolean interpreterMode = false;

    public VM() {
        this.memSize = 0;
        this.memory = new int[0];
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
    }

    public VM(int memSize) {
        this.memSize = memSize;
        this.memory = new int[this.memSize];
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
    }

    public VM(int[] memory) {
        this.memSize = memory.length;
        this.memory = memory;
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
    }

    public VM(int[] memory, int ip, int a, int b, int c, int d) {
        this.memSize = memory.length;
        this.memory = memory;
        this.stack = new Stack<>();
        this.callStack = new Stack<>();

        this.ip = ip;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ip:  ").append(ip).append(NL);
        sb.append("a:   ").append(a).append(NL);
        sb.append("b:   ").append(b).append(NL);
        sb.append("c:   ").append(c).append(NL);
        sb.append("d:   ").append(d).append(NL);
        sb.append("mem: ");
        for (int m : memory) sb.append(m).append("|");
        sb.append(NL);

        sb.append("stk: ");
        for (int m : stack) sb.append(m).append("|");
        sb.append(NL);

        return sb.toString();
    }

    public VM setStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public VM setCallStackSize(int maxCallStackSize) {
        this.maxCallStackSize = maxCallStackSize;
        return this;
    }

    public VM setPrintWriter(Writer writer) {
        this.out = writer;
        return this;
    }

    public VM setInputReader(Reader reader) {
        this.in = reader;
        return this;
    }

    public void next(int params) {
        ip += interpreterMode ? 1 : 1 + params * 2;
    }

    public Integer exec() {

        // read the program size
        memOffs = memory[ip];
        ip++;

        while (true) {
            // get the next operation
            int op = memory[ip];

            switch (op) {
                case Operation.SET:
                    new SetOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4])).exec(this);
                    break;
                case Operation.ADD:
                    new AddOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6])).exec(this);
                    break;
                case Operation.JMPLTE:
                    new JmpLTEOp(Encoder.decode(memory[ip + 1], memory[ip + 2]), Encoder.decode(memory[ip + 3], memory[ip + 4]), Encoder.decode(memory[ip + 5], memory[ip + 6])).exec(this);
                    break;
                case Operation.RES:
                    return new ResOp(Encoder.decode(memory[ip + 1], memory[ip + 2])).exec(this);
                default:
                    throw new RuntimeException("Unexpected opcode " + op);

            }
        }
    }

    public void setInterpreterMode(boolean enabled) {
        interpreterMode = enabled;
    }

}
