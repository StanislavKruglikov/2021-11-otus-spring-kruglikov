package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocaleIOServiceImpl implements LocaleIOService {

    private final LocaleService localeService;
    private final IOService ioService;

    @Override
    public void write(String outData) {
        ioService.write(outData);
    }

    @Override
    public void writeMessage(String message, String... arguments) {
        final String translatedMessage = localeService.getMessage(message,arguments);
        write(translatedMessage);
    }

    @Override
    public String read() {
        return ioService.read();
    }
}
