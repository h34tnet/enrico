package net.h34t.enrico;

public interface Ref {

    static Ref from(String token) {
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

     int getValue(VM vm);

    void setValue(VM vm, int val);

}
