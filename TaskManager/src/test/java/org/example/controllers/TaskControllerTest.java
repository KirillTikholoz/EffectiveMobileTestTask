package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.configs.SecurityConfig;
import org.example.dtos.TaskDto;
import org.example.dtos.ValueDto;
import org.example.dtos.filter.TaskFilter;
import org.example.exception.TaskNotFoundException;
import org.example.exception.UserNotEnoughAuthorities;
import org.example.filters.JwtRequestFilter;
import org.example.services.TaskService;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = {
        TaskController.class,
        SecurityConfig.class,
        JwtRequestFilter.class,
        JwtTokenUtils.class,
        GlobalExceptionHandler.class
})
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TaskService taskService;
    @Value("${jwt.secret-access-key}")
    private String secretKey;

    String validAdminToken;
    String validUserToken;
    @BeforeEach
    public void setUp(){
        validAdminToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("roles", List.of("ROLE_ADMIN"))
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .compact();

        validUserToken =
                Jwts
                        .builder()
                        .subject("user@mail.ru")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("roles", List.of("ROLE_USER"))
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .compact();
    }

    @Test
    public void successfulCreateTaskHandler() throws Exception {
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "MEDIUM",
                "Kirill@mail.ru"
        );
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void createTaskHandlerWithUserRole() throws Exception {
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "MEDIUM",
                "Kirill@mail.ru"
        );
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + validUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createTaskHandlerWithoutToken() throws Exception {
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "MEDIUM",
                "Kirill@mail.ru"
        );
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createTaskHandlerWithoutIncorrectData() throws Exception {
        TaskDto taskDto = new TaskDto(
                "",
                "description",
                "COMPLETED",
                "MEDIUM",
                "Kirill@mail.ru"
        );
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void successfulGetTaskHandler() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(get("/tasks/{taskId}", taskId))
                .andExpect(status().isOk());
    }

    @Test
    public void getTaskHandlerWithTaskNotFoundException() throws Exception {
        Long taskId = 1L;

        when(taskService.readTask(taskId)).thenThrow(new TaskNotFoundException());

        mockMvc.perform(get("/tasks/{taskId}", taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void successfulGetTasksByFilterHandler() throws Exception {
        TaskFilter taskFilter = new TaskFilter(
                "author",
                "executor"
        );
        String taskFilterJson = objectMapper.writeValueAsString(taskFilter);

        mockMvc.perform(post("/tasks/filter")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskFilterJson))
                .andExpect(status().isOk());
    }

    @Test
    public void successfulUpdateTaskHandler() throws Exception {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "MEDIUM",
                "Kirill@mail.ru"
        );
        String taskDtoJson = objectMapper.writeValueAsString(taskDto);

        mockMvc.perform(put("/tasks/{taskId}", taskId)
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void successfulUpdateStatusHandler() throws Exception {
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("IN_WAITING");
        String valueDtoJson = objectMapper.writeValueAsString(valueDto);

        mockMvc.perform(patch("/tasks/{taskId}/status", taskId)
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void successfulUpdateStatusWithUserNotEnoughAuthorities() throws Exception {
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("IN_WAITING");
        String valueDtoJson = objectMapper.writeValueAsString(valueDto);

        doThrow(new UserNotEnoughAuthorities())
                .when(taskService).updateStatus(taskId, valueDto);

        mockMvc.perform(patch("/tasks/{taskId}/status", taskId)
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void successfulUpdatePriorityHandler() throws Exception {
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("HIGH");
        String valueDtoJson = objectMapper.writeValueAsString(valueDto);

        mockMvc.perform(patch("/tasks/{taskId}/priority", taskId)
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void successfulUpdateExecutorHandler() throws Exception {
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("new_executor");
        String valueDtoJson = objectMapper.writeValueAsString(valueDto);

        mockMvc.perform(patch("/tasks/{taskId}/executor", taskId)
                        .header("Authorization", "Bearer " + validAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void successfulDeleteHandler() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(delete("/tasks/{taskId}", taskId)
                        .header("Authorization", "Bearer " + validAdminToken))
                .andExpect(status().isOk());
    }
}
