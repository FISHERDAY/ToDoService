package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthService {
    private final ToDoService toDoService;
    private final UserService userService;

    public AuthService(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    public boolean isTodoOwner(long userId, long todoId) {
        return toDoService.readById(todoId).getOwner().getId() == userId;
    }

    public boolean isTodoCollaborator(long userId, long todoId) {
        return toDoService.readById(todoId).getCollaborators().contains(userService.readById(userId));
    }

    public boolean isCurrentUser(Authentication auth, long userId) {
        return userService.findByEmail(auth.getName()).getId() == userId;
    }
}
