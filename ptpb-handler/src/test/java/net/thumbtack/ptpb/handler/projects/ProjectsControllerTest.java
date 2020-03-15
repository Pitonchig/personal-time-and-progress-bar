package net.thumbtack.ptpb.handler.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.projects.dto.TaskDto;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateTaskRequest;
import net.thumbtack.ptpb.handler.projects.dto.responses.CreateProjectResponse;
import net.thumbtack.ptpb.handler.projects.dto.responses.CreateTaskResponse;
import net.thumbtack.ptpb.handler.projects.dto.responses.GetProjectResponse;
import net.thumbtack.ptpb.handler.projects.dto.responses.GetTaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ProjectsController.class})
public class ProjectsControllerTest {

    @MockBean
    private ProjectsService projectsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetProjects() throws Exception {
        final String url = "/api/projects";
        final String uuid = UUID.randomUUID().toString();
        final List<? extends Response> responses = Collections.singletonList(new GetProjectResponse());
        doReturn(responses).when(projectsService).getProjects(uuid);

        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<? extends Response> resultResponses = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), GetProjectResponse[].class));
        verify(projectsService, times(1)).getProjects(uuid);
        assertEquals(responses.size(), resultResponses.size());
    }

    @Test
    void testCreateProject() throws Exception {
        final int projectId = 1;
        final String url = "/api/projects";
        final String uuid = UUID.randomUUID().toString();
        final CreateProjectRequest request = CreateProjectRequest.builder()
                .name("test")
                .build();
        final CreateProjectResponse response = new CreateProjectResponse(projectId);
        when(projectsService.createProject(request, uuid)).thenReturn(response);

        MvcResult result = mockMvc.perform(post(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreateProjectResponse resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CreateProjectResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).createProject(request, uuid);
        assertEquals(response, resultResponse);
    }

    @Test
    void testDeleteProject() throws Exception {
        final int projectId = 123;
        final String url = String.format("/api/projects/%d", projectId);
        final String uuid = UUID.randomUUID().toString();
        when(projectsService.deleteProject(projectId, uuid)).thenReturn(new EmptyResponse());

        mockMvc.perform(delete(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        verify(projectsService, times(1)).deleteProject(projectId, uuid);
    }

    @Test
    void testGetProject() throws Exception {
        final int projectId = 333;
        final String url = String.format("/api/projects/%d", projectId);
        final String uuid = UUID.randomUUID().toString();
        final Response response = GetProjectResponse.builder()
                .id(projectId)
                .name("name")
                .task(new TaskDto())
                .build();
        doReturn(response).when(projectsService).getProject(projectId, uuid);

        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GetProjectResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).getProject(projectId, uuid);
        assertEquals(response, resultResponse);
    }


    @Test
    void testGetTasksByProjectId() throws Exception {
        final int projectId = 333;
        final String url = String.format("/api/projects/%d/tasks", projectId);
        final String uuid = UUID.randomUUID().toString();
        final List<? extends Response> responses = Collections.singletonList(new GetTaskResponse());
        doReturn(responses).when(projectsService).getTasksByProjectId(projectId, uuid);


        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<? extends Response> resultResponses = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), GetTaskResponse[].class));
        verify(projectsService, times(1)).getTasksByProjectId(projectId, uuid);
        assertEquals(responses.size(), resultResponses.size());
        assertTrue(responses.containsAll(resultResponses));
    }

    @Test
    void testCreateTask() throws Exception {
        final int projectId = 333;
        final int taskId = 2;
        final String url = String.format("/api/projects/%d/tasks", projectId);
        final String uuid = UUID.randomUUID().toString();
        final CreateTaskRequest request = new CreateTaskRequest("task");
        final Response response = new CreateTaskResponse(taskId);
        doReturn(response).when(projectsService).createTask(request, projectId, uuid);

        MvcResult result = mockMvc.perform(post(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CreateTaskResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).createTask(request, projectId, uuid);
        assertEquals(response, resultResponse);
    }


    @Test
    void testDeleteTask() throws Exception {
        final int taskId = 22;
        final String url = String.format("/api/projects/tasks/%d", taskId);
        final String uuid = UUID.randomUUID().toString();
        final Response response = new EmptyResponse();
        doReturn(response).when(projectsService).deleteTask(taskId, uuid);

        MvcResult result = mockMvc.perform(delete(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmptyResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).deleteTask(taskId, uuid);
    }


    @Test
    void testGetTask() throws Exception {
        final int taskId = 22;
        final String url = String.format("/api/projects/tasks/%d", taskId);
        final String uuid = UUID.randomUUID().toString();
        final Response response = GetTaskResponse.builder()
                .id(taskId)
                .content("content")
                .build();
        doReturn(response).when(projectsService).getTask(taskId, uuid);

        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GetTaskResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).getTask(taskId, uuid);
        assertEquals(response, resultResponse);
    }

}
