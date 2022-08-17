package ru.msaggik.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.msaggik.spring.dao.PersonDAO;
import ru.msaggik.spring.models.Person;
import ru.msaggik.spring.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
    // внедрение бина PersonDAO в данный контроллер
    private final PersonDAO personDAO;
    // внедрение валидатора
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }
    // получение списка всех пользователей из DAO и передача его в представление
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }
    // получение информации одного пользователя по id из DAO и передача её в представление
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        // "@PathVariable("id") int id" - извлечение данных пользователя с id
        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("books", personDAO.getBooksByPersonId(id));

        return "people/show";
    }
    // возврат html формы для создания информации о новом пользователе
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }
    // принимает POST запрос и отправляет данные в БД для создания информации о пользователе
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) { // @Valid - задание валидности данных пользователя
        // возможная ошибка помещается в объект BindingResult
        // использование валидатора, ошибки с валидатора хранятся в BindingResult
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            // в случае ошибки происходит выброс на "people/new" с указанием произошедших ошибок
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }
    // редактирование данных пользователя:
    // 1) отображение данных имеющегося пользователя
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }
    // 2) приём PATH запроса
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        // использование валидатора, ошибки с валидатора хранятся в BindingResult
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }
    // удаление данных пользователя
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}

