package net.thumbtack.ptpb.handler.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.session.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.session.dto.responses.LoginUserResponse;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        SessionsController.class
})
public class SessionsControllerTest {

    @MockBean
    private SessionsService sessionsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoginUser() throws Exception {
        String url = "/api/sessions";

        LoginUserRequest request = LoginUserRequest.builder()
                .login("login")
                .password("password")
                .build();

        LoginUserResponse response = LoginUserResponse.builder()
                .id(UUID.randomUUID().toString())
                .build();

        when(sessionsService.loginUser(any(LoginUserRequest.class), anyString())).thenReturn(response);

        MvcResult result = mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(cookie().exists(Types.UUID))
                .andReturn();

        verify(sessionsService, times(1)).loginUser(any(LoginUserRequest.class), anyString());
        Cookie cookie = result.getResponse().getCookie(Types.UUID);
        assertNotNull(cookie);
        assertFalse(cookie.getValue().isEmpty());
    }

    @Test
    void testLogoutUser() throws Exception {
        String url = "/api/sessions";
        String uuid = UUID.randomUUID().toString();

        when(sessionsService.logoutUser(uuid)).thenReturn(new EmptyResponse());

        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(Types.UUID, uuid))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(cookie().doesNotExist(Types.UUID));

        verify(sessionsService, times(1)).logoutUser(uuid);
    }


}
