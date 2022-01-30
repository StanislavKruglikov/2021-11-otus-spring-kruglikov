package ru.otus.skruglikov.examiner.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.skruglikov.examiner.service.ExaminerService;

@ShellComponent
@RequiredArgsConstructor
public class ExaminerCommands {
    private final ExaminerService examinerService;

    private String examName;

    @ShellMethod(value = "Run command", key = { "r" , "run"})
    @ShellMethodAvailability(value = "isTakeExamAvailable")
    public String runCommand() {
        examinerService.takeExam(examName);
        return String.format("\nthe exam %s is finished.\n",this.examName);
    }

    @ShellMethod(value = "Select exam name", key = { "s" , "select"})
    public String setExamNameCommand(@ShellOption(defaultValue = "MicrobiologyIntro") String examName) {
        this.examName = examName;
        return String.format("\nNow the \"%s\" selected.\n",examName);
    }

    private Availability isTakeExamAvailable() {
        return examName == null ? Availability.unavailable("you should select exam name before") : Availability.available();
    }
}
