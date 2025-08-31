package com.example.Todo.repository;

import com.example.Todo.models.Todo;

import java.util.List;

public interface TodoRepository {
    Todo addTodo(Todo todo);
    Todo updateTodo(Todo todo);
    Todo getTodoById(Long id);
    List<Todo> getTodos();
    void deleteTodo(Todo todo);
}
