package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
