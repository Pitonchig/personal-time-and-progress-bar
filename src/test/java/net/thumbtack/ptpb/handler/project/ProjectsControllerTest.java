package net.thumbtack.ptpb.handler.project;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
@ContextConfiguration(classes = {
//        ProjectsController.class
})
public class ProjectsControllerTest {
/*
    @MockBean
    private ProjectsService projectsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetProjects() throws Exception {
        String url = "/api/projects";
        String uuid = UUID.randomUUID().toString();
        List<? extends Response> responses = Collections.singletonList(new GetProjectResponse());
        doReturn(responses).when(projectsService).getUserProjects(uuid);

        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<? extends Response> resultResponses = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), GetProjectResponse[].class));
        verify(projectsService, times(1)).getUserProjects(uuid);
        assertEquals(responses.size(), resultResponses.size());
    }

    @Test
    void testCreateProject() throws Exception {
        long projectId = System.nanoTime();
        String url = "/api/projects";
        String uuid = UUID.randomUUID().toString();
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("test")
                .build();
        CreateProjectResponse response = new CreateProjectResponse(projectId);
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
        long projectId = System.nanoTime();
        String url = String.format("/api/projects/%d", projectId);
        String uuid = UUID.randomUUID().toString();
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
        long projectId = System.nanoTime();
        String url = String.format("/api/projects/%d", projectId);
        String uuid = UUID.randomUUID().toString();
        Response response = GetProjectResponse.builder()
                .id(projectId)
                .name("name")
                .item(new ItemDto())
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
    void testGetItemsByProjectId() throws Exception {
        long projectId = System.nanoTime();
        String url = String.format("/api/projects/%d/items", projectId);
        String uuid = UUID.randomUUID().toString();
        List<? extends Response> responses = Collections.singletonList(new GetItemResponse());
        doReturn(responses).when(projectsService).getItemsByProjectId(projectId, uuid);


        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<? extends Response> resultResponses = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), GetItemResponse[].class));
        verify(projectsService, times(1)).getItemsByProjectId(projectId, uuid);
        assertEquals(responses.size(), resultResponses.size());
        assertTrue(responses.containsAll(resultResponses));
    }

    @Test
    void testCreateItem() throws Exception {
        long projectId = System.nanoTime();
        long itemId = System.nanoTime();
        int priority = 3;
        String url = String.format("/api/projects/%d/items", projectId);
        String uuid = UUID.randomUUID().toString();
        CreateItemRequest request = CreateItemRequest.builder()
                .content("content")
                .due(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .priority(priority)
                .build();
        Response response = new CreateItemResponse(itemId);
        doReturn(response).when(projectsService).createItem(request, projectId, uuid);

        MvcResult result = mockMvc.perform(post(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CreateItemResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).createItem(request, projectId, uuid);
        assertEquals(response, resultResponse);
    }


    @Test
    void testDeleteItem() throws Exception {
        long itemId = System.nanoTime();
        String url = String.format("/api/projects/items/%d", itemId);
        String uuid = UUID.randomUUID().toString();
        Response response = new EmptyResponse();
        doReturn(response).when(projectsService).deleteItemById(itemId, uuid);

        MvcResult result = mockMvc.perform(delete(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), EmptyResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).deleteItemById(itemId, uuid);
    }


    @Test
    void testGetItem() throws Exception {
        long itemId = System.nanoTime();
        String url = String.format("/api/projects/items/%d", itemId);
        String uuid = UUID.randomUUID().toString();
        Response response = GetItemResponse.builder()
                .id(itemId)
                .content("content")
                .build();
        doReturn(response).when(projectsService).getItemById(itemId, uuid);

        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Response resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GetItemResponse.class);
        assertNotNull(resultResponse);
        verify(projectsService, times(1)).getItemById(itemId, uuid);
        assertEquals(response, resultResponse);
    }
*/
}
