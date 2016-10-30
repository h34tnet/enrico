package net.h34t.enrico;

import net.h34t.enrico.op.*;

import java.io.*;

public class Parser {

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
                            op = new SetOp(Ref.from(operands[1]), Ref.from(operands[2]));
                            break;

                        case "add":
                            op = new AddOp(Ref.from(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "sub":
                            op = new SubOp(Ref.from(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "mul":
                            op = new MulOp(Ref.from(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "div":
                            op = new DivOp(Ref.from(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "mod":
                            op = new ModOp(Ref.from(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "res":
                            op = new ResOp(Ref.from(operands[1]));
                            break;

                        case "load":
                            op = new LoadOp(Ref.from(operands[1]), Ref.from(operands[2]));
                            break;

                        case "save":
                            op = new SaveOp(Ref.from(operands[1]), Ref.from(operands[2]));
                            break;

                        case "jmp":
                            op = new JmpOp(new Label(operands[1]));
                            break;

                        case "jmpgt":
                            op = new JmpGTOp(new Label(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "jmplt":
                            op = new JmpLTOp(new Label(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "jmpe":
                            op = new JmpEOp(new Label(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "jmpne":
                            op = new JmpNEOp(new Label(operands[1]), Ref.from(operands[2]), Ref.from(operands[3]));
                            break;

                        case "swp":
                            op = new SwpOp(Ref.from(operands[1]), Ref.from(operands[2]));
                            break;

                        case "push":
                            op = new PushOp(Ref.from(operands[1]));
                            break;

                        case "pop":
                            op = new PopOp(Ref.from(operands[1]));
                            break;

                        case "peek":
                            op = new PeekOp(Ref.from(operands[1]));
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
}
