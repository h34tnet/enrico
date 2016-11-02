package net.h34t.enrico;

public class Label implements Ref {

    private final String name;
    private int offs = -1;

    public Label(String name) {
        this.name = name;
    }

    public Label(String name, int offs) {
        this.name = name;
        this.offs = offs;
    }

    public void setOffset(int offset) {
        this.offs = offset;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Label label1 = (Label) o;

        return name != null ? name.equals(label1.name) : label1.name == null;
    }

    @Override
    public int encode() {
        return offs;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int getValue(VM vm) {
        return offs;
    }

    @Override
    public void setValue(VM vm, int val) {
        throw new RuntimeException("Can't assign a value to a name.");
    }

    @Override
    public String toString() {
        return "label \"" + name + "\" @ " + offs;
    }
}
