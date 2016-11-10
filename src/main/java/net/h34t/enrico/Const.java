package net.h34t.enrico;

@Deprecated
public class Const implements Ref {

    private final int value;

    public Const(int value) {
        this.value = value;
    }

    public int getValue(VM vm) {
        return value;
    }

    @Override
    public void setValue(VM vm, int val) {
        throw new RuntimeException("Can't assign a value to a constant.");
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public int encode() {
        return value;
    }
}
