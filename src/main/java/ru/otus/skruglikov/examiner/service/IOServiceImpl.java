package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.provider.InputOutputProvider;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class IOServiceImpl implements IOService {

    private final InputOutputProvider<InputStream, PrintStream> ioProvider;

    @Override
    public void write(String outputData) {
        ioProvider.getOutput().println(outputData);
    }

    @Override
    public String read() {
        final Scanner scanner = new Scanner(ioProvider.getInput());
        return scanner.nextLine();
    }
}
