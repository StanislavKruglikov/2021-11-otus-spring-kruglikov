package ru.otus.skruglikov.examiner.provider;

import java.io.InputStream;
import java.io.PrintStream;

public interface InputOutputProvider {
    InputStream getInputStream();
    PrintStream getPrintStream();
}
