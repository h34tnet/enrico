package net.h34t.enrico;

public interface Ref {

    int getValue(VM vm);

    void setValue(VM vm, int val);

    int encode(LabelOffsetTranslator program);
}
