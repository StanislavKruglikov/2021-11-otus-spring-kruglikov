package ru.otus.skruglikov.examiner.service;

public interface LocaleIOService {

    void write(String outData);

    String read();

    void writeMessage(String message, String... arguments);
}
