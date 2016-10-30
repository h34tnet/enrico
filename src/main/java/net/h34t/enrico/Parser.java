package net.h34t.enrico;

import net.h34t.enrico.op.*;

import java.io.*;
import java.util.regex.Pattern;

public class Parser {

    public static final Pattern PATTERN_HEX_CONST = Pattern.compile("0x[0-9a-fA-F]+");
    public static final Pattern PATTERN_BIN_CONST = Pattern.compile("b[0|1]+");

    public static Program load(Reader reader) throws IOException {
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
                    final Operation op;

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
                            op = new JmpOp(new Label(operands[1]));
                            break;

                        case "jmpgt":
                            op = new JmpGTOp(new Label(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmplt":
                            op = new JmpLTOp(new Label(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmpe":
                            op = new JmpEOp(new Label(operands[1]), ref(operands[2]), ref(operands[3]));
                            break;

                        case "jmpne":
                            op = new JmpNEOp(new Label(operands[1]), ref(operands[2]), ref(operands[3]));
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

                        case "label":
                            op = new LabelOp(new Label(operands[1]));
                            break;

                        case "call":
                            op = new CallOp(new Label(operands[1]));
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

                        default:
                            throw new RuntimeException("Unknown operation \"" + line + "\" at " + r.getLineNumber());
                    }

                    program.add(op);

                } catch (NumberFormatException a) {
                    throw new RuntimeException("Unknown operand \"" + line + "\" at " + r.getLineNumber());

                } catch (IndexOutOfBoundsException a) {
                    throw new RuntimeException("Wrong number of arguments for \"" + line + "\" at " + r.getLineNumber());

                }
            }
        }

        return program;
    }

    public static Program load(String program) throws RuntimeException {
        try {
            return load(new StringReader(program));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static Program load(File file) throws IOException {
        return load(new FileReader(file));
    }

    public static Ref ref(String token) {
        if (PATTERN_HEX_CONST.matcher(token).matches()) {
            return new Constant(Integer.parseInt(token.substring(2), 16));
        } else if (PATTERN_BIN_CONST.matcher(token).matches()) {
            return new Constant(Integer.parseInt(token.substring(1), 2));
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
}
