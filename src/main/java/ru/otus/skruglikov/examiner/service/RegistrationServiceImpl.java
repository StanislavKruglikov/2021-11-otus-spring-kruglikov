package ru.otus.skruglikov.examiner.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.skruglikov.examiner.dao.StudentDao;
import ru.otus.skruglikov.examiner.domain.Student;
import ru.otus.skruglikov.examiner.exception.RegistrationException;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final StudentDao studentDao;
    private final LocaleIOService localeIOService;

    @Override
    public Student register() throws RegistrationException {
        final String firstName = getStudentProperty("firstname");
        final String lastName = getStudentProperty("lastname");
        return studentDao.createStudent(firstName,lastName);
    }

    private String getStudentProperty(final String propertyName) throws RegistrationException {
        localeIOService.writeMessage("examiner.createStudent.prompt."+propertyName);
        final String result = localeIOService.read();
        if (StringUtils.isBlank(result)) {
            throw new RegistrationException(
                String.format("registration error: not specified student properties %s",propertyName));
        }
        return result;
    }
}
