package net.h34t.enrico;

public class Constant implements Ref {

    private final int value;

    public Constant(int value) {
        this.value = value;
    }

    public int getValue(VM vm) {
        return value;
    }

    @Override
    public void setValue(VM vm, int val) {
        throw new RuntimeException("can't write to constant");
    }
}
