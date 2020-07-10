package net.thumbtack.ptpb.handler.todoist;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.project.dto.response.ProjectResponse;
import net.thumbtack.ptpb.handler.todoist.dto.request.SyncProjectsRequest;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import net.thumbtack.ptpb.handler.todoist.dto.response.UpdateTodoistTokenResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        TodoistController.class
})
public class TodoistControllerTest {

    @MockBean
    private TodoistService todoistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testUpdateTodoistToken() throws Exception {
        String url = "/api/users/tokens";
        String uuid = UUID.randomUUID().toString();
        final boolean isTodoistLinked = true;

        UpdateTodoistTokenRequest request = UpdateTodoistTokenRequest.builder()
                .token(UUID.randomUUID().toString())
                .build();
        UpdateTodoistTokenResponse responses = UpdateTodoistTokenResponse.builder()
                .isTodoistLinked(isTodoistLinked)
                .build();
        doReturn(responses).when(todoistService).updateTodoistToken(request, uuid);

        MvcResult result = mockMvc.perform(put(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UpdateTodoistTokenResponse resultResponses = objectMapper.readValue(result.getResponse().getContentAsString(), UpdateTodoistTokenResponse.class);
        verify(todoistService, times(1)).updateTodoistToken(request, uuid);
        assertEquals(isTodoistLinked, resultResponses.isTodoistLinked());
    }

    @Test
    void testSyncProjects() throws Exception {
        String url = "/api/users/services";
        String uuid = UUID.randomUUID().toString();
        final boolean isDoExport = true;
        final boolean isDoImport = true;

        SyncProjectsRequest request = SyncProjectsRequest.builder()
                .to(isDoExport)
                .from(isDoImport)
                .build();

        EmptyResponse response = new EmptyResponse();
        doReturn(response).when(todoistService).syncProjects(request, uuid);

        mockMvc.perform(put(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(todoistService, times(1)).syncProjects(request, uuid);
    }
}
