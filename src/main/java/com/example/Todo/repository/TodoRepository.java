package com.example.Todo.repository;

import com.example.Todo.models.Todo;

public interface TodoRepository {
    Todo addTodo(Todo todo);
    Todo updateTodo(Todo todo);
    Todo getTodoById(Long id);
}
