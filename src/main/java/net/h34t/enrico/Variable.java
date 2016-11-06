package net.h34t.enrico;

public class Variable implements Ref {

    public final String name;
    public final int offs;

    public Variable(String name, int offs) {
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
        return "Var " + name + " @ " + offs;
    }

    @Override
    public int encode(LabelOffsetTranslator lot) {
        return offs;
    }
}
