package com.example.Todo.service;

import com.example.Todo.models.Todo;
import com.example.Todo.repository.TodoDBRepository;
import com.example.Todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Todo Todo = todoRepository.getTodoById(id);
        return todoRepository.updateTodo(todo);
    }

    public Todo getTodoById(Long id) {
        return todoRepository.getTodoById(id);
    }

    public List<Todo> getAllTodos() {
        return todoRepository.getTodos();
    }
}
