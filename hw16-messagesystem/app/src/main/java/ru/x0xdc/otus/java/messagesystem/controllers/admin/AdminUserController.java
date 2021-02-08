package ru.x0xdc.otus.java.messagesystem.controllers.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserForm;
import ru.x0xdc.otus.java.messagesystem.messagesystem.frontend.FrontendService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.x0xdc.otus.java.messagesystem.config.WebSocketConfig.*;

@Controller
public class AdminUserController {
    private static final String TEMPLATE_CREATE_USER = "admin/userCreate.html";
    private static final String TEMPLATE_ADMIN_USER_LIST = "admin/userList.html";

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final FrontendService frontendService;

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public AdminUserController(FrontendService frontendService, SimpMessageSendingOperations messagingTemplate) {
        this.frontendService = frontendService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/admin")
    public String userListView() {
        return TEMPLATE_ADMIN_USER_LIST;
    }

    @GetMapping("/admin/user/create")
    public String createUserFormView(@ModelAttribute("user") UserForm user) {
        return TEMPLATE_CREATE_USER;
    }

    @MessageMapping(WS_DEST_USER_LIST_REQUEST)
    public void broadcastUserList(@Header("simpSessionId") String sessionId) {
        frontendService.getAllUsers(new GetUserListResponseHandler(messagingTemplate, sessionId));
    }

    @MessageMapping(WS_DEST_CREATE_USER_REQUEST)
    public void createUser(@Valid UserForm userForm, @Header("simpSessionId") String sessionId) {
        frontendService.createUser(userForm, new CreateUserResponseHandler(messagingTemplate, sessionId));
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser(WS_DEST_ERROR_UNICAST_RESPONSE)
    public WebSocketErrorResponse handleFormValidationException(MethodArgumentNotValidException exception) {
        final List<String> messages;
        if (exception.getBindingResult() != null) {
            messages = exception.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .sorted(Comparator.comparing(field -> {
                        int index = UserForm.FIELD_ORDER.indexOf(field.getField());
                        if (index < 0) throw new IllegalStateException("Field not in sort order list:" + field.getField());
                        return index;
                    }))
                    .map(fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "")
                    .collect(Collectors.toUnmodifiableList());
        } else {
            messages = Collections.singletonList(exception.getMessage());
        }

        return new WebSocketErrorResponse(messages);
    }

    @MessageExceptionHandler
    @SendToUser(WS_DEST_ERROR_UNICAST_RESPONSE)
    public WebSocketErrorResponse handleException(Exception exception) {
        logger.error("Admin Controller exception", exception);
        return new WebSocketErrorResponse(Collections.singletonList(exception.getMessage()));
    }

}
