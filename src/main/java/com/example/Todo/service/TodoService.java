package com.example.Todo.service;

import com.example.Todo.models.Todo;
import com.example.Todo.repository.TodoDBRepository;
import com.example.Todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private TodoRepository todoRepository;

    public TodoService(TodoDBRepository todoDBRepository) {
        this.todoRepository = todoDBRepository;
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.addTodo(todo);
    }

    public Todo updateTodo(Long id, Todo todo) {
        Todo optionalTodo = todoRepository.findById(id);
        return todoRepository.updateTodo(todo);
    }
}
