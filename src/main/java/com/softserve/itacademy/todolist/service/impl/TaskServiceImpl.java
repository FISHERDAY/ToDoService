package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.repository.TaskRepository;
import com.softserve.itacademy.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task) {
        if (task != null) {
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public Task readById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            EntityNotFoundException exception = new EntityNotFoundException("Task with id " + id + " not found");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
        return task.get();
    }

    @Override
    public Task update(Task task) {
        if (task != null) {
            readById(task.getId());
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Task task = readById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        return taskRepository.getByTodoId(todoId);
    }
}
