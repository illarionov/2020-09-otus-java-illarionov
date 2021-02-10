package ru.otus.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import java.util.Optional;

@Controller
public class UserController {

    private final DBServiceUser userService;

    @Autowired
    public UserController(DBServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public RedirectView currentUserView() {
        long currentUserId = 1L;
        return new RedirectView("/user/" + currentUserId, true);
    }

    @GetMapping("/user/{id}")
    public String userView(@PathVariable(name="id") Long userId, Model model) {
        Optional<User> user = userService.getUser(userId);
        model.addAttribute("user", user.orElseThrow());
        return "user/index.html";
    }
}
