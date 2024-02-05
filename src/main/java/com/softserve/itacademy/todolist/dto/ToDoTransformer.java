package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;

import java.time.LocalDateTime;

public class ToDoTransformer {

    public static ToDoDto convertToDto(ToDo toDo) {
        return new ToDoDto(
                toDo.getId(),
                toDo.getTitle(),
                toDo.getCreatedAt().toString(),
                toDo.getOwner().getId()
        );
    }

    public static ToDo convertToEntity(ToDoDto toDoDto, User user) {
        ToDo toDo = new ToDo();
        toDo.setId(toDoDto.getId());
        toDo.setTitle(toDoDto.getTitle());
        toDo.setOwner(user);
        return toDo;
    }
}
