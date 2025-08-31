package com.example.Todo.repository;

import com.example.Todo.models.Todo;
import org.springframework.beans.factory.annotation.Autowired;

public class TodoDBRepository implements TodoRepository {
    @Autowired
    private JpaTodoRepository jpaTodoRepository;

    @Override
    public Todo addTodo(Todo todo) {
        return jpaTodoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Todo todo) {
        return jpaTodoRepository.save(todo);
    }

    @Override
    public Todo getTodoById(Long id) {
        return jpaTodoRepository.getTodoById(id);
    }
}

