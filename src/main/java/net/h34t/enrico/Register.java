package net.h34t.enrico;

public class Register implements Ref {

    private final Reg reg;

    public Register(Reg reg) {
        if (reg == null) throw new IllegalArgumentException("Invalid register");
        this.reg = reg;
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
    public String toString() {
        return "Reg " + reg.name();
    }

    public enum Reg {A, B, C, D}
}
