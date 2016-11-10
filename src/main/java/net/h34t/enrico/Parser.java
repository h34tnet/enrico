package net.h34t.enrico;

import net.h34t.enrico.op.*;

import java.io.*;
import java.util.regex.Matcher;
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

    public static final Pattern PATTERN_HEX_CONST = Pattern.compile("^0x[0-9a-fA-F]+$");
    public static final Pattern PATTERN_BIN_CONST = Pattern.compile("^b[0|1]+$");
    public static final Pattern PATTERN_CHAR_CONST = Pattern.compile("^'.'$");
    public static final Pattern PATTERN_VAR = Pattern.compile("^\\$([a-zA-Z0-9]+)$");
    public static final Pattern PATTERN_VAR_ADDR = Pattern.compile("^@([a-zA-Z0-9]+)$");
    public static final Pattern PATTERN_LABEL = Pattern.compile("^:([a-zA-Z0-9]+)$");

    private int lineNumber;
    private String line;

    public Parser() {
    }

    public Program parse(Reader reader) throws IOException {
        Program program = new Program();
        LineNumberReader r = new LineNumberReader(reader);
        String instr;

        while ((line = r.readLine()) != null) {
            line = line.trim();
            this.lineNumber = r.getLineNumber();

            if (!line.isEmpty() && !line.startsWith("#")) {
                // remove everything after the comment
                instr = line.contains("#")
                        ? line.substring(0, line.indexOf("#")).trim()
                        : line;

                String[] operands = instr.split("\\s+");

                try {
                    Operation op = null;

                    if (PATTERN_LABEL.matcher(instr).matches()) {
                        op = new LabelOp(operands[0]);
                    }

                    switch (operands[0]) {
                        case "set":
                            op = new SetOp(reg(operands[1]), reg(operands[2]));
                            break;

                        case "add":
                            op = new AddOp(reg(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "sub":
                            op = new SubOp(reg(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "mul":
                            op = new MulOp(reg(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "div":
                            op = new DivOp(reg(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "mod":
                            op = new ModOp(reg(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "res":
                            op = new ResOp(reg(operands[1]));
                            break;

                        case "load":
                            op = new LoadOp(reg(operands[1]), operands[2]);
                            break;

                        case "save":
                            op = new SaveOp(reg(operands[1]), operands[2]);
                            break;

                        case "jmp":
                            op = new JmpOp(ref(operands[1]));
                            break;

                        case "jmpgt":
                            op = new JmpGTOp(ref(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "jmpgte":
                            op = new JmpGTEOp(ref(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "jmplt":
                            op = new JmpLTOp(ref(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "jmplte":
                            op = new JmpLTEOp(ref(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "jmpe":
                            op = new JmpEOp(ref(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "jmpne":
                            op = new JmpNEOp(ref(operands[1]), reg(operands[2]), reg(operands[3]));
                            break;

                        case "swp":
                            op = new SwpOp(reg(operands[1]), reg(operands[2]));
                            break;

                        case "push":
                            op = new PushOp(reg(operands[1]));
                            break;

                        case "pop":
                            op = new PopOp(reg(operands[1]));
                            break;

                        case "peek":
                            op = new PeekOp(reg(operands[1]));
                            break;

                        case "call":
                            op = new CallOp(ref(operands[1]));
                            break;

                        case "ret":
                            op = new RetOp();
                            break;

                        case "print":
                            op = new PrintOp(reg(operands[1]));
                            break;

                        case "read":
                            op = new ReadOp(reg(operands[1]));
                            break;

                        case "def":
                            // defines a variable, optionally with a size argument
                            Matcher matcher = PATTERN_VAR.matcher(operands[1]);
                            if (matcher.matches()) {

                                String name = matcher.group(1);

                                // all variable definitions are made here
                                int size = operands.length > 2
                                        ? Integer.parseInt(operands[2])
                                        : 1;

                                op = new DefOp(name, size);
                                // defineVariable(name, size);

                            } else {
                                throw new RuntimeException(
                                        String.format("Variable definition parse error: %d: %s", lineNumber, line));
                            }
                            break;
                    }

                    if (op != null) {
                        program.add(op);
                    } else {
                        throw new RuntimeException(String.format("Parse error at %d: %s", lineNumber, line));
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
        Matcher labelMatcher = PATTERN_LABEL.matcher(token),
                varMatcher = PATTERN_VAR.matcher(token),
                adrMatcher = PATTERN_VAR_ADDR.matcher(token);

        try {
            if (PATTERN_HEX_CONST.matcher(token).matches()) {
                return new Const(Integer.parseInt(token.substring(2), 16));

            } else if (PATTERN_BIN_CONST.matcher(token).matches()) {
                return new Const(Integer.parseInt(token.substring(1), 2));

            } else if (PATTERN_CHAR_CONST.matcher(token).matches()) {
                return new Const(token.charAt(1));

            } else if (varMatcher.matches()) {
                String name = varMatcher.group(1);
                // return new Variable(name, getMemOffsetForVariable(name));
                return new Var(name, 0);

            } else if (adrMatcher.matches()) {
                String name = adrMatcher.group(1);
                return new Addr(name, 0);

            }
//            else if (labelMatcher.matches()) {
//                if (!this.referencedLabels.containsKey(token)) {
//                    Label label = new Label(token);
//                    this.referencedLabels.put(token, label);
//                    return label;
//                } else
//                    return this.referencedLabels.get(token);
//            }

            switch (token) {
                case "a":
                    return new Reg(Reg.R.A);
                case "b":
                    return new Reg(Reg.R.B);
                case "c":
                    return new Reg(Reg.R.C);
                case "d":
                    return new Reg(Reg.R.D);
                default:
                    int i = Integer.parseInt(token);
                    return new Const(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Parse error at line %d: %s", lineNumber, line));
        }
    }

    private String label(String token) {
        return token;
    }

    private Reg reg(String token) {
        switch (token) {
            case "a":
                return new Reg(Reg.R.A);
            case "b":
                return new Reg(Reg.R.B);
            case "c":
                return new Reg(Reg.R.C);
            case "d":
                return new Reg(Reg.R.D);
            default:
                throw new RuntimeException("Unknown register \"" + token + "\"");
        }
    }
}
