package ru.otus.skruglikov.examiner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

@SpringBootApplication
public class SpringExaminerApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("en-EN"));
        final ApplicationContext ctx = SpringApplication.run(SpringExaminerApplication.class, args);
    }
}
