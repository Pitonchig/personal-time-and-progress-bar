package net.thumbtack.ptpb.handler.project;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;
import net.thumbtack.ptpb.handler.project.dto.request.UpdateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.response.ProjectResponse;
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
@ContextConfiguration(classes = {
        ProjectsController.class
})
public class ProjectsControllerTest {

    @MockBean
    private ProjectsService projectsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUserProjects() throws Exception {
        String url = "/api/projects";
        String uuid = UUID.randomUUID().toString();
        List<? extends Response> responses = Collections.singletonList(new ProjectResponse());
        doReturn(responses).when(projectsService).getUserProjects(uuid);

        MvcResult result = mockMvc.perform(get(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<? extends Response> resultResponses = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), ProjectResponse[].class));
        verify(projectsService, times(1)).getUserProjects(uuid);
        assertEquals(responses.size(), resultResponses.size());
    }

    @Test
    void testUpdateUserProjects() throws Exception {
        String url = "/api/projects";
        String uuid = UUID.randomUUID().toString();

        UpdateProjectRequest project = UpdateProjectRequest.builder()
                .id(UUID.randomUUID().toString())
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item1").build())
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item2").build())
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item3").build())
                .build();
        List<UpdateProjectRequest> request = Collections.singletonList(project);

        List<? extends Response> responses = Collections.singletonList(new ProjectResponse());
        doReturn(responses).when(projectsService).updateUserProjects(request, uuid);


        MvcResult result = mockMvc.perform(put(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<? extends Response> resultResponses = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), ProjectResponse[].class));
        verify(projectsService, times(1)).updateUserProjects(request, uuid);
        assertEquals(responses.size(), resultResponses.size());
    }
}
