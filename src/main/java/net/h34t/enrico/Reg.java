package net.h34t.enrico;

public class Reg implements Ref {

    private final R r;

    public Reg(R r) {
        if (r == null) throw new IllegalArgumentException("Invalid register");
        this.r = r;
    }

    public Reg(int reg) {
        switch (reg) {
            case 0:
                this.r = R.A;
                break;
            case 1:
                this.r = R.B;
                break;
            case 2:
                this.r = R.C;
                break;
            case 3:
                this.r = R.D;
                break;
            default:
                throw new RuntimeException("Unknown register #" + reg);
        }
    }

    @Override
    public int getValue(VM vm) {
        switch (r) {
            case A:
                return vm.a;
            case B:
                return vm.b;
            case C:
                return vm.c;
            case D:
                return vm.d;
            default:
                throw new RuntimeException("unknown register \"" + r.toString() + "\"");
        }
    }

    @Override
    public void setValue(VM vm, int val) {
        switch (r) {
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
                throw new RuntimeException("unknown register \"" + r.toString() + "\"");
        }
    }

    @Override
    public int encode() {
        switch (r) {
            case A:
                return 0;
            case B:
                return 1;
            case C:
                return 2;
            case D:
                return 3;
            default:
                throw new RuntimeException("unknown register \"" + r.toString() + "\"");
        }
    }

    @Override
    public String toString() {
        return "" + r.name().toLowerCase();
    }

    public enum R {A, B, C, D}
}
