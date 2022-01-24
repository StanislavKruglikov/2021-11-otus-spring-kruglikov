package ru.otus.skruglikov.examiner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.skruglikov.examiner.service.ExaminerService;

@SpringBootApplication
public class SpringExaminerApplication {

    public static void main(String[] args) {
        final ApplicationContext ctx = SpringApplication.run(SpringExaminerApplication.class, args);
        final ExaminerService examinerService = ctx.getBean(ExaminerService.class);
        examinerService.takeExam("MicrobiologyIntro");
    }
}
