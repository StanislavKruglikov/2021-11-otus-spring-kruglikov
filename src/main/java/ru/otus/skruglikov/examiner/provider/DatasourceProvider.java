package ru.otus.skruglikov.examiner.provider;

import java.io.IOException;
import java.io.InputStream;

public interface DatasourceProvider {
    InputStream getInputStream() throws IOException;
}
