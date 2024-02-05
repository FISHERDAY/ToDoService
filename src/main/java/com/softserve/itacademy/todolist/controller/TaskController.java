package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskRequestDto;
import com.softserve.itacademy.todolist.dto.TaskResponseDto;
import com.softserve.itacademy.todolist.dto.TaskTransformer;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/users/{user-id}/todos/{todo-id}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ToDoService toDoService;
    private final StateService stateService;

    public TaskController(TaskService taskService, ToDoService toDoService, StateService stateService) {
        this.toDoService = toDoService;
        this.taskService = taskService;
        this.stateService = stateService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId)" +
            "&& (@authService.isTodoOwner(#userId, #todoId) || @authService.isTodoCollaborator(#userId, #todoId)))")
    List<TaskResponseDto> getAll(@PathVariable("user-id") long userId,
                                 @PathVariable("todo-id") long todoId) {
        log.info("Get all tasks");
        return taskService.getByTodoId(todoId).stream()
                .map(TaskResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{task-id}")
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId)" +
            "&& (@authService.isTodoOwner(#userId, #todoId) || @authService.isTodoCollaborator(#userId, #todoId)))")
    TaskResponseDto read(@PathVariable("user-id") long userId,
                         @PathVariable("todo-id") long todoId,
                         @PathVariable("task-id") long taskId) {
        log.info("Read task with id " + taskId);
        return new TaskResponseDto(taskService.readById(taskId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId) && @authService.isTodoOwner(#userId, #todoId))")
    TaskResponseDto create(@PathVariable("user-id") long userId,
                           @PathVariable("todo-id") long todoId,
                           @Valid @RequestBody TaskRequestDto taskRequestDto) {
        log.info("Create task");
        Task task = taskService.create(TaskTransformer.convertToEntity(taskRequestDto, toDoService.readById(todoId), stateService.readById(1L)));
        log.info("Task successfully created");
        return new TaskResponseDto(task);
    }

    @PutMapping("/{task-id}")
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId) && @authService.isTodoOwner(#userId, #todoId))")
    public TaskResponseDto updateToDo(@PathVariable("user-id") long userId,
                                      @PathVariable("todo-id") long todoId,
                                      @PathVariable("task-id") long taskId,
                                      @Valid @RequestBody TaskRequestDto taskRequestDto) {
        log.info("Update task with id " + taskId);
        Task oldTask = taskService.readById(taskId);
        Task newTask = TaskTransformer.convertToEntity(taskRequestDto, toDoService.readById(todoId), stateService.readById(taskRequestDto.getStateId()));
        newTask.setId(oldTask.getId());
        newTask = taskService.update(newTask);
        log.info("Task with id " + taskId + " successfully updated");
        return new TaskResponseDto(newTask);
    }

    @DeleteMapping("/{task-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') || (@authService.isCurrentUser(authentication, #userId) && @authService.isTodoOwner(#userId, #todoId))")
    void delete(@PathVariable("user-id") long userId,
                @PathVariable("todo-id") long todoId,
                @PathVariable("task-id") long taskId) {
        log.info("Create task with id " + taskId);
        taskService.delete(taskId);
        log.info("Task successfully deleted");
    }

}

