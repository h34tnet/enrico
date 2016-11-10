package net.h34t.enrico;

public class Label implements Ref {

    private final String name;

    public Label(String name) {
        this.name = name;
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
        return program.get(this);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int getValue(VM vm) {
        throw new RuntimeException("Raw label value access for \"" + name + "\"");
    }

    @Override
    public void setValue(VM vm, int val) {
        throw new RuntimeException("Can't assign a value to a name.");
    }

    @Override
    public String toString() {
        return "label \"" + name + "\"";
    }
}
