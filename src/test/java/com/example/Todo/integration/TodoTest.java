package com.example.Todo.integration;

import com.example.Todo.models.Todo;
import com.example.Todo.repository.JpaTodoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private JpaTodoRepository jpaTodoRepository;

    @BeforeEach
    public void setup() {
        jpaTodoRepository.deleteAll(); // 清空表

        // 插入 5 条测试数据
        for (int i = 1; i <= 5; i++) {
            Todo todo = new Todo();
            todo.setTitle("Test Todo " + i);
            todo.setDescription("Description for todo " + i);
            todo.setCreatedTime(LocalDateTime.now().minusDays(5 - i));
            todo.setDueTime(LocalDateTime.now().plusDays(i));
            todo.setCompleted(i % 2 == 0); // 偶数为 true
            jpaTodoRepository.save(todo);
        }
    }

    @AfterEach
    public void cleanup() {
        jpaTodoRepository.deleteAll();
    }

    @Test
    public void shouldCreateTodo() throws Exception {
        String title = "买牛奶";
        String description = "去超市买一盒牛奶";
        String createdTime = "2025-08-31T09:00:00";
        String dueTime = "2025-09-01T12:00:00";
        boolean completed = false;

        String todoJson = """
                {
                  "title": "%s",
                  "description": "%s",
                  "createdTime": "%s",
                  "dueTime": "%s",
                  "completed": %s
                }
                """.formatted(title, description, createdTime, dueTime, completed);

        client.perform(post("/todos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(todoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.createdTime").value(createdTime))
                .andExpect(jsonPath("$.dueTime").value(dueTime))
                .andExpect(jsonPath("$.completed").value(completed));
    }

    @Test
    public void shouldFindAllTodos() throws Exception {
        client.perform(get("/todos"))
                .andExpect(status().isOk())                     // 状态码 200
                .andExpect(content().contentType("application/json")) // 返回 JSON
                .andExpect(jsonPath("$.size()").value(5))       // 数组大小为 5
                .andExpect(jsonPath("$[0].title").value("Test Todo 1"))
                .andExpect(jsonPath("$[0].description").value("Description for todo 1"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[4].title").value("Test Todo 5"));
    }

    @Test
    public void shouldFindTodoById() throws Exception {
        List<Todo> todos = jpaTodoRepository.findAll();

        Todo firstTodo = todos.get(0);

        client.perform(get("/todos/{id}", firstTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstTodo.getId()))
                .andExpect(jsonPath("$.title").value(firstTodo.getTitle()))
                .andExpect(jsonPath("$.description").value(firstTodo.getDescription()))
                .andExpect(jsonPath("$.createdTime").value(
                        firstTodo.getCreatedTime().toString()))
                .andExpect(jsonPath("$.dueTime").value(
                        firstTodo.getDueTime().toString()))
                .andExpect(jsonPath("$.completed").value(firstTodo.getCompleted()));
    }

    @Test
    public void shouldUpdateTodo() throws Exception {
        List<Todo> todos = jpaTodoRepository.findAll();
        assertThat(todos).isNotEmpty();
        Long id = todos.get(0).getId();

        String updatedJson = """
        {
          "id": %d,
          "title": "更新：买牛奶和面包",
          "description": "去超市买牛奶和全麦面包",
          "createdTime": "2025-08-31T09:00:00",
          "dueTime": "2025-09-02T18:00:00",
          "completed": true
        }
        """.formatted(id);

        // 3. 发起 PUT 请求
        client.perform(put("/todos/{id}", id)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("更新：买牛奶和面包"))
                .andExpect(jsonPath("$.description").value("去超市买牛奶和全麦面包"))
                .andExpect(jsonPath("$.createdTime").value("2025-08-31T09:00:00"))
                .andExpect(jsonPath("$.dueTime").value("2025-09-02T18:00:00"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    public void shouldPartiallyUpdateTodo() throws Exception {
        List<Todo> todos = jpaTodoRepository.findAll();
        assertThat(todos).isNotEmpty();
        Long id = todos.get(0).getId();

        String patchJson = """
        {
          "title": "只改标题：买橙汁",
          "completed": true
        }
        """;

        client.perform(patch("/todos/{id}", id)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("只改标题：买橙汁"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.description").value(todos.get(0).getDescription()))
                .andExpect(jsonPath("$.dueTime").value(todos.get(0).getDueTime().toString()));
    }

    @Test
    public void shouldDeleteTodo() throws Exception {
        List<Todo> todos = jpaTodoRepository.findAll();
        assertThat(todos).isNotEmpty();
        Long id = todos.get(0).getId();

        client.perform(delete("/todos/{id}", id))
                .andExpect(status().isNoContent()); // ✅ 204 No Content
    }
}