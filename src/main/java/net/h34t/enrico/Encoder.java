package net.h34t.enrico;

public class Encoder {

    public static int[] encode(LabelOffsetTranslator program, int op, Ref... params) {
        int[] bytecode = new int[params.length * 2 + 1];

        bytecode[0] = op;
        for (int i = 0; i < params.length; i++) {
            Ref param = params[i];
            bytecode[1 + i * 2] = encRefType(param);
            bytecode[1 + i * 2 + 1] = param.encode(program);
        }

        return bytecode;
    }

    private static int encRefType(Ref ref) {
        if (ref instanceof Constant || ref instanceof Label) {
            return 0;

        } else if (ref instanceof Register) {
            return 1;

        } else if (ref instanceof Variable) {
            return 2;

        } else {
            throw new RuntimeException("Unknown reference type: " + ref.toString());
        }
    }

    public static Ref decode(int type, int value) {
        switch (type) {
            case 0:
                return new Constant(value);
            case 1:
                return new Register(value);
            case 2:
                return new Variable(null, value);
            default:
                throw new RuntimeException("failed decoding of type:" + type + ", value: " + value);
        }
    }
}
