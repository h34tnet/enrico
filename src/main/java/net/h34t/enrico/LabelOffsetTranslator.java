package net.h34t.enrico;

import java.util.HashMap;
import java.util.Map;

public class LabelOffsetTranslator {
    private final Map<Label, Integer> labelOffsets;

    LabelOffsetTranslator() {
        labelOffsets = new HashMap<>();
    }

    public void put(Label label, int offs) {
        labelOffsets.put(label, offs);
    }

    public int get(Label label) {
        return labelOffsets.get(label);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Label, Integer> entry : labelOffsets.entrySet()) {
            sb.append(String.format("%s @ %d%n", entry.getKey().getName(), entry.getValue()));
        }

        return sb.toString();
    }
}
