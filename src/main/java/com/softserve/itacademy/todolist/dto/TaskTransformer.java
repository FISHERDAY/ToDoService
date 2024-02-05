package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.State;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;

public class TaskTransformer {

    public static Task convertToEntity(TaskRequestDto taskRequestDto, ToDo todo, State state) {
        Task task = new Task();
        task.setName(taskRequestDto.getName());
        task.setPriority(Priority.valueOf(taskRequestDto.getPriority()));
        task.setTodo(todo);
        task.setState(state);
        return task;
    }
}
