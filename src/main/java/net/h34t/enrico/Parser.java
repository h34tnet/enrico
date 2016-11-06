package net.h34t.enrico;

import net.h34t.enrico.op.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The Parser turns source code into an intermediate representation (AST) that can be either interpreted or
 * compiled to machine code.
 * <p>
 * TODO: add an option to get addresses from variables, i.e.
 * <p>
 * <code>
 * def $hello
 * set a @hello
 * </code>
 * <p>
 * this assigns the memory position of $hello to register a.
 */
public class Parser {

    public static final Pattern PATTERN_HEX_CONST = Pattern.compile("0x[0-9a-fA-F]+");
    public static final Pattern PATTERN_BIN_CONST = Pattern.compile("b[0|1]+");
    public static final Pattern PATTERN_VARIABLE = Pattern.compile("\\$[a-zA-Z0-9]+");
    public static final Pattern PATTERN_LABEL = Pattern.compile(":[a-zA-Z0-9]+");

    private final Map<String, Label> referencedLabels;
    private final Map<String, Integer> variableOffsets;

    private int memCounter = 0;

    public Parser() {
        this.referencedLabels = new HashMap<>();
        this.variableOffsets = new HashMap<>();
    }

    public Program parse(Reader reader) throws IOException {
        Program program = new Program();
        LineNumberReader r = new LineNumberReader(reader);
        String line, instr;

        while ((line = r.readLine()) != null) {
            line = line.trim();

            if (!line.isEmpty() && !line.startsWith("#")) {
                // remove everything after the comment
                instr = line.contains("#")
                        ? line.substring(0, line.indexOf("#")).trim()
                        : line;

                String[] operands = instr.split("\\s+");

                try {
                    Operation op = null;

                    if (PATTERN_LABEL.matcher(instr).matches()) {
                        op = new LabelOp(ref(operands[0]));
                    }

                    switch (operands[0]) {
                        case "set":
                            op = new SetOp(ref(operands[1]), ref(operands[2]));
                            break;

                        case "add":
                            op = new AddOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "sub":
                            op = new SubOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "mul":
                            op = new MulOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "div":
                            op = new DivOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "mod":
                            op = new ModOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "res":
                            op = new ResOp(ref(operands[1]));
                            break;

                        case "load":
                            op = new LoadOp(ref(operands[1]), ref(operands[2]));
                            break;

                        case "save":
                            op = new SaveOp(ref(operands[1]), ref(operands[2]));
                            break;

                        case "jmp":
                            op = new JmpOp(ref(operands[1]));
                            break;

                        case "jmpgt":
                            op = new JmpGTOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmpgte":
                            op = new JmpGTEOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmplt":
                            op = new JmpLTOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmplte":
                            op = new JmpLTEOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmpe":
                            op = new JmpEOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmpne":
                            op = new JmpNEOp(ref(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "swp":
                            op = new SwpOp(ref(operands[1]), ref(operands[2]));
                            break;

                        case "push":
                            op = new PushOp(ref(operands[1]));
                            break;

                        case "pop":
                            op = new PopOp(ref(operands[1]));
                            break;

                        case "peek":
                            op = new PeekOp(ref(operands[1]));
                            break;

                        case "call":
                            op = new CallOp(ref(operands[1]));
                            break;

                        case "ret":
                            op = new RetOp();
                            break;

                        case "print":
                            op = new PrintOp(ref(operands[1]));
                            break;

                        case "read":
                            op = new ReadOp(ref(operands[1]));
                            break;

                        case "def":
                            // all variable definitions are made here
                            int size = operands.length > 2
                                    ? Integer.parseInt(operands[1])
                                    : 1;

                            defineVariable(operands[1], size);
                            continue;
                    }

                    if (op != null) {
                        program.add(op);
                    } else {
                        throw new RuntimeException("Unknown operation \"" + line + "\" at " + r.getLineNumber());
                    }

                } catch (NumberFormatException a) {
                    throw new RuntimeException("Unknown operand \"" + line + "\" at " + r.getLineNumber());

                } catch (IndexOutOfBoundsException a) {
                    throw new RuntimeException("Wrong number of arguments for \"" + line + "\" at " + r.getLineNumber());

                }
            }
        }

        return program;
    }

    public Program parse(String program) throws RuntimeException {
        try {
            return parse(new StringReader(program));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public Program parse(File file) throws IOException {
        return parse(new FileReader(file));
    }

    private Ref ref(String token) {
        if (PATTERN_HEX_CONST.matcher(token).matches()) {
            return new Constant(Integer.parseInt(token.substring(2), 16));

        } else if (PATTERN_BIN_CONST.matcher(token).matches()) {
            return new Constant(Integer.parseInt(token.substring(1), 2));

        } else if (PATTERN_VARIABLE.matcher(token).matches()) {
            return new Variable(token, getMemOffsetForVariable(token));

        } else if (PATTERN_LABEL.matcher(token).matches()) {

            if (!this.referencedLabels.containsKey(token)) {
                Label label = new Label(token);
                this.referencedLabels.put(token, label);
                return label;
            } else
                return this.referencedLabels.get(token);
        }

        switch (token) {
            case "a":
                return new Register(Register.Reg.A);
            case "b":
                return new Register(Register.Reg.B);
            case "c":
                return new Register(Register.Reg.C);
            case "d":
                return new Register(Register.Reg.D);
            default:
                int i = Integer.parseInt(token);
                return new Constant(i);
        }
    }

    /**
     * Stores memory offsets for variables.
     * <p>
     * Only works with single integers for now
     *
     * @param name the name of the variable
     */
    public void defineVariable(String name, int size) {
        variableOffsets.put(name, memCounter);
        memCounter += size;
    }

    public int getMemOffsetForVariable(String name) {
        Integer addr = variableOffsets.get(name);
        if (addr != null) return addr;
        else throw new RuntimeException("Undefined variable \"" + name + "\"");
    }
}
