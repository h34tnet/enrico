package net.h34t.enrico;

/**
 * Operation on 0-n Values. Operations should only change state on the VM (i.e. on registers, memory, stacks).
 */
public interface Operation {

    Integer exec(VM vm, Program program);

}
