package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Value;

import java.util.Objects;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ToDoDto {
    Long id;
    String title;
    String created_at;
    Long owner_id;

    public ToDoDto(Long id, String title, String created_at, Long owner_id) {
        this.id = id;
        this.title = title;
        this.created_at = created_at;
        this.owner_id = owner_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoDto that = (ToDoDto) o;
        return Objects.equals(title, that.title) && Objects.equals(owner_id, that.owner_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, owner_id);
    }

    @Override
    public String toString() {
        return "ToDoTransformer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", created_at='" + created_at + '\'' +
                ", owner_id=" + owner_id +
                '}';
    }
}
