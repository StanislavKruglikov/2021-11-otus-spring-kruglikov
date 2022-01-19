package ru.otus.skruglikov.examiner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.skruglikov.examiner.domain.Student;

@DisplayName("Класс RegistrationService")
class RegistrationServiceImplTest {

    @Mock
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        //registrationService = new RegistrationServiceImpl();
    }

    @DisplayName("При регистрации корректно создается объект Student")
    @Test
    void shouldCreateCorrectObjectStudent() {
        try {
            Student student = registrationService.register();
            System.out.println(student.getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}