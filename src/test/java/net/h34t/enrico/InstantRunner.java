package net.h34t.enrico;

import org.junit.Before;

public abstract class InstantRunner {

    protected VM vm;

    public static Integer compileAndRun(VM vm, String source) {
        return vm.load(new Compiler().compile(new Parser().parse(source))).exec();
    }

    public static Integer compileAndRun(VM vm, Program program) {
        return vm.load(new Compiler().compile(program)).exec();
    }

    public static int[] c(String source) {
        return new Compiler().compile(new Parser().parse(source));
    }

    @Before
    public void runBeforeTestMethod() {
        // prepares a fresh VM before each test
        vm = new VM(512);
    }


}
