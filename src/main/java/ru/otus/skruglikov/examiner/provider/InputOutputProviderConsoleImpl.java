package ru.otus.skruglikov.examiner.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PrintStream;

@Component
public class InputOutputProviderConsoleImpl implements InputOutputProvider {

    private final InputStream inputStream;
    private final PrintStream printStream;

    public InputOutputProviderConsoleImpl(@Value("#{ T(java.lang.System).in }") InputStream inputStream,
                                          @Value("#{ T(java.lang.System).out }") PrintStream printStream) {
        this.inputStream = inputStream;
        this.printStream = printStream;
    }

    @Override
    public InputStream getInput() {
        return this.inputStream;
    }

    @Override
    public PrintStream getOutput() {
        return this.printStream;
    }
}
