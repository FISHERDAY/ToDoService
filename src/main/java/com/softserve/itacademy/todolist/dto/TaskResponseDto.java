package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.Task;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponseDto {
    Long id;
    String name;
    String priority;
    Long todoId;
    Long stateId;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.priority = task.getPriority().name();
        this.todoId = task.getTodo().getId();
        this.stateId = task.getState().getId();
    }
}
