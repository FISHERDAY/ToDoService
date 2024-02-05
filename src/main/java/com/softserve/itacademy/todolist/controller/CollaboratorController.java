package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users/{user-id}/todos/{todo-id}/collaborators")
public class CollaboratorController {

    UserService userService;
    ToDoService todoService;

    public CollaboratorController(UserService userService, ToDoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId)" +
            "&& @authService.isTodoOwner(#userId, #todoId))")
    public List<UserResponse> addCollaborator(@PathVariable("user-id") long userId, @PathVariable("todo-id") long todoId, @PathVariable long id) {
        log.info("Adding Collaborator" +userService.readById(userId).getUsername());
        ToDo todo = todoService.readById(todoId);
        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(id));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return todo.getCollaborators().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId)" +
            "&& @authService.isTodoOwner(#userId, #todoId))")
    public List<UserResponse> removeCollaborator(@PathVariable("user-id") long userId, @PathVariable("todo-id") long todoId, @PathVariable long id) {
        ToDo todo = todoService.readById(todoId);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(id));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return todo.getCollaborators().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}
