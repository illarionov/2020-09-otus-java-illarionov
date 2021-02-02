package ru.otus.controllers.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.core.dao.LoginAlreadyExistsException;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceException;

import javax.validation.Valid;

@Controller
public class CreateUserController {
    private static final String TEMPLATE_CREATE_USER = "admin/userCreate.html";
    private static final String ATTRIBUTE_USER_FORM = "user";
    private static final String ATTRIBUTE_ERRORS = "errors";

    private static final Logger logger = LoggerFactory.getLogger(CreateUserController.class);
    
    private final DBServiceUser userService;

    @Autowired
    public CreateUserController(DBServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserForm(@ModelAttribute("user") UserForm user) {
        return TEMPLATE_CREATE_USER;
    }

    @PostMapping("/admin/user/create")
    public String createUser(@Valid @ModelAttribute("user") UserForm user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TEMPLATE_CREATE_USER;
        }

        try {
            User newUser = user.buildUser();
            long userId = userService.saveUser(newUser);
            return "redirect:/admin";
        } catch (DbServiceException e) {
            if (e.getCause() instanceof LoginAlreadyExistsException) {
                bindingResult.addError(new FieldError("user", "login", "Логин уже существует"));
            } else {
                bindingResult.addError(new FieldError("user", "", "Ошибка создания пользователя"));
                logger.error("Failed to create user", e);
            }
        }

        return TEMPLATE_CREATE_USER;
    }
}
