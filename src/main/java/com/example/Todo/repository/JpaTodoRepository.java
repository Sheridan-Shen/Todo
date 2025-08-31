package com.example.Todo.repository;

import com.example.Todo.models.Todo;
;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface JpaTodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

}
