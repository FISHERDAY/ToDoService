package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.ToDo;
import lombok.Value;

import java.time.format.DateTimeFormatter;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToDoResponse {
    Long id;
    String title;
    String userName;
    String created;

    public ToDoResponse(ToDo toDo) {
        id = toDo.getId();
        title = toDo.getTitle();
        userName = toDo.getOwner().getUsername();
        created = toDo.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}

