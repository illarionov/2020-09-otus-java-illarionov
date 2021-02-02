package ru.otus.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminUsersController {

    private final DBServiceUser userService;

    @Autowired
    public AdminUsersController(DBServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getUserList(Model model) throws IOException {
        List<User> users = userService.findAllUsers();

        model.addAttribute("users", users);
        return "admin/userList.html";
    }
}
