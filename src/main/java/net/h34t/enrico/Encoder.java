package net.h34t.enrico;

public class Encoder {

    public static int[] encode(int op, Ref... params) {
        int[] bytecode = new int[params.length + 1];

        bytecode[0] = op;
        for (int i = 0; i < params.length; i++) {
            Ref param = params[i];
            bytecode[1 + i] = param.encode();
        }

        return bytecode;
    }

    public static Ref decode(int type, int value) {
        switch (type) {
            case 0:
                return new Const(value);

            case 1:
                return new Reg(value);

            case 2:
                return new Var(null, value);

            case 3:
                return new Addr(null, value);

            default:
                throw new RuntimeException("failed decoding of type:" + type + ", value: " + value);
        }
    }
}
