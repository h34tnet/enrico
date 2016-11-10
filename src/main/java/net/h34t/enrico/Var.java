package net.h34t.enrico;

public class Var implements Ref {

    public final String name;
    public final int offs;

    public Var(String name, int offs) {
        this.name = name;
        this.offs = offs;
    }

    @Override
    public int getValue(VM vm) {
        return vm.memory[vm.memOffs + offs];
    }

    @Override
    public void setValue(VM vm, int val) {
        vm.memory[vm.memOffs + offs] = val;
    }

    @Override
    public String toString() {
        return "mem[" + offs + "]";
    }

    @Override
    public int encode() {
        return offs;
    }
}
