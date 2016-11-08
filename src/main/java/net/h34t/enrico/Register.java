package net.h34t.enrico;

public class Register implements Ref {

    private final Reg reg;

    public Register(Reg reg) {
        if (reg == null) throw new IllegalArgumentException("Invalid register");
        this.reg = reg;
    }

    public Register(int reg) {
        switch (reg) {
            case 0:
                this.reg = Reg.A;
                break;
            case 1:
                this.reg = Reg.B;
                break;
            case 2:
                this.reg = Reg.C;
                break;
            case 3:
                this.reg = Reg.D;
                break;
            default:
                throw new RuntimeException("Unknown register #" + reg);
        }
    }

    @Override
    public int getValue(VM vm) {
        switch (reg) {
            case A:
                return vm.a;
            case B:
                return vm.b;
            case C:
                return vm.c;
            case D:
                return vm.d;
            default:
                throw new RuntimeException("unknown register \"" + reg.toString() + "\"");
        }
    }

    @Override
    public void setValue(VM vm, int val) {
        switch (reg) {
            case A:
                vm.a = val;
                break;
            case B:
                vm.b = val;
                break;
            case C:
                vm.c = val;
                break;
            case D:
                vm.d = val;
                break;
            default:
                throw new RuntimeException("unknown register \"" + reg.toString() + "\"");
        }
    }

    @Override
    public int encode(LabelOffsetTranslator lot) {
        switch (reg) {
            case A:
                return 0;
            case B:
                return 1;
            case C:
                return 2;
            case D:
                return 3;
            default:
                throw new RuntimeException("unknown register \"" + reg.toString() + "\"");
        }
    }

    @Override
    public String toString() {
        return "" + reg.name().toLowerCase();
    }

    public enum Reg {A, B, C, D}
}
