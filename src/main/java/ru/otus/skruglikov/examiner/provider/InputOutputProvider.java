package ru.otus.skruglikov.examiner.provider;

public interface InputOutputProvider<InputType,OutputType> {
    InputType getInput();
    OutputType getOutput();
}
