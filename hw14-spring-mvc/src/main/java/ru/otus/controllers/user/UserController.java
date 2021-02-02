package ru.otus.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.service.DBServiceUser;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final DBServiceUser userService;

    @Autowired
    public UserController(DBServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public RedirectView getUser(Model model, HttpServletRequest request) {
        long currentUserId = 1L;
        return new RedirectView("/user/" + currentUserId, true);
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable(name="id") Long userId, Model model) {
        var user = userService.getUser(userId);

        model.addAttribute("user", user.orElseThrow());

        return "user/index.html";
    }
}
