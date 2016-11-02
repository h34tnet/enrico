package net.h34t.enrico;

/**
 * Operation on 0-n Values. Operations should only change state on the VM (i.e. on registers, memory, stacks).
 */
public interface Operation {

    int SET = 0;
    int SWP = 1;

    int ADD = 100;
    int SUB = 101;
    int MUL = 102;
    int DIV = 103;
    int MOD = 104;

    int LOAD = 200;
    int SAVE = 201;
    int PUSH = 202;
    int POP = 203;
    int PEEK = 204;

    int JMP = 300;
    int JMPE = 301;
    int JMPNE = 302;
    int JMPGT = 303;
    int JMPGTE = 304;
    int JMPLT = 305;
    int JMPLTE = 306;
    int CALL = 307;
    int RET = 308;
    int RES = 309;

    int PRINT = 400;
    int READ = 401;




    Integer exec(VM vm, Program program);

    int[] encode();

}
