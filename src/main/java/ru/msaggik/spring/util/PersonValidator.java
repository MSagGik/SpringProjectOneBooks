package ru.msaggik.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.msaggik.spring.dao.PersonDAO;
import ru.msaggik.spring.models.Person;

@Component
public class PersonValidator implements Validator {
    // обращение к БД
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o; // даункастинг объекта о до поля person
        // мониторинг повторения ФИО в БД
        if (personDAO.getPersonByFullName(person.getFullName()).isPresent())
            // errors.rejectValue(поле, код ошибки, сообщение ошибки)
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");
    }
}