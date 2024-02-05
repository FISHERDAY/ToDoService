package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.ToDoDto;
import com.softserve.itacademy.todolist.dto.ToDoTransformer;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users/{user-id}/todos")
public class ToDoController {

    private final ToDoService todoService;
    private final UserService userService;

    @Autowired
    public ToDoController(ToDoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || @authService.isCurrentUser(authentication, #userId)")
    public List<ToDoDto> getAll(@PathVariable("user-id") long userId) {
        log.info("Get all ToDo's of " + userService.readById(userId).getUsername());
        return todoService.getByUserId(userId).stream()
                .map(ToDoTransformer::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{todo-id}")
    @PreAuthorize("hasRole('ADMIN') || @authService.isCurrentUser(authentication, #userId)")
    ToDoDto getToDo(@PathVariable("user-id") long userId,
                    @PathVariable("todo-id") long todoId) {
        log.info("Get ToDo by Id");
        ToDo todo = todoService.readById(todoId);
        return ToDoTransformer.convertToDto(todo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') || @authService.isCurrentUser(authentication, #userId)")
    ToDoDto createToDo(@PathVariable("user-id") long userId,
                       @RequestBody ToDoDto toDoDto) {
        ToDo todo = ToDoTransformer.convertToEntity(toDoDto, userService.readById(userId));
        try {
            log.info("Create ToDo");
            ToDo createdToDo = todoService.create(todo);
            log.info("ToDo successfully created");
            return ToDoTransformer.convertToDto(createdToDo);
        } catch (DataIntegrityViolationException e) {
            log.warn("ToDo with this ID already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ToDo with this ID already exists.", e);
        }
    }

    @PutMapping("/{todo-id}")
    @PreAuthorize("hasRole('ADMIN') || @authService.isCurrentUser(authentication, #userId)")
    ToDoDto updateToDo(@PathVariable("user-id") long userId,
                       @PathVariable("todo-id") long todoId,
                       @RequestBody ToDoDto toDoDto) {
        log.info("Create ToDo");
        ToDo oldToDo = todoService.readById(todoId);
        ToDo todo = ToDoTransformer.convertToEntity(toDoDto, userService.readById(userId));
        todo.setId(oldToDo.getId());
        todo.setCreatedAt(oldToDo.getCreatedAt());
        ToDo updatedToDo = todoService.update(todo);
        log.info("ToDo successfully updated");
        return ToDoTransformer.convertToDto(updatedToDo);
    }

    @DeleteMapping("/{todo-id}")
    @PreAuthorize("hasRole('ADMIN') || @authService.isCurrentUser(authentication, #userId)")
    void deleteToDo(@PathVariable("user-id") String userId,
                    @PathVariable("todo-id") Long todoId) {
        log.info("Delete ToDo with id: " + todoId);
        todoService.delete(todoId);
    }
}
