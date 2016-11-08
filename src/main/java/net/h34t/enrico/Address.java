package net.h34t.enrico;

public class Address implements Ref {

    public final String name;
    public final int offs;

    public Address(String name, int offs) {
        this.name = name;
        this.offs = offs;
    }

    @Override
    public int getValue(VM vm) {
        return vm.memOffs + offs;
    }

    @Override
    public void setValue(VM vm, int val) {
        throw new RuntimeException(String.format("Illegal write to variable address %d/%d", offs, vm.memOffs + offs));
    }

    @Override
    public String toString() {
        return "VarAddr " + name + " @ " + offs;
    }

    @Override
    public int encode(LabelOffsetTranslator lot) {
        return offs;
    }
}
