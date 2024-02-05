package com.softserve.itacademy.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class TaskRequestDto {
    @NotEmpty(message = "The 'name' cannot be empty")
    String name;
    @NotEmpty(message = "The 'priority' cannot be empty")
    String priority;
    Long stateId;
}
