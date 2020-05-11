package net.thumbtack.ptpb.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.user.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.user.dto.requests.RegisterUserRequest;
import net.thumbtack.ptpb.handler.user.dto.responses.RegisterUserResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        UsersController.class
})
public class UsersControllerTest {

    @MockBean
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegisterUser() throws Exception {
        String url = "/api/users";
        String name = "user";
        String uuid = UUID.randomUUID().toString();
        RegisterUserRequest request = RegisterUserRequest.builder()
                .login(name)
                .password("password")
                .email("email@gmail.com")
                .build();

        RegisterUserResponse response = RegisterUserResponse.builder()
                .id(System.nanoTime())
                .build();
        when(usersService.registerUser(any(RegisterUserRequest.class), anyString())).thenReturn(response);

        MvcResult result = mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(Types.UUID))
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        RegisterUserResponse resultResponse = objectMapper.readValue(result.getResponse().getContentAsString(), RegisterUserResponse.class);
        assertNotNull(resultResponse);
        verify(usersService, times(1)).registerUser(any(RegisterUserRequest.class), anyString());
        assertEquals(response, resultResponse);
    }

    @Test
    void testDeleteUser() throws Exception {
        long id = System.nanoTime();
        String url = String.format("/api/users/%d", id);

        String uuid = UUID.randomUUID().toString();
        DeleteUserRequest request = DeleteUserRequest.builder()
                .login("user")
                .password("password")
                .build();
        when(usersService.deleteUser(request, id, uuid)).thenReturn(new EmptyResponse());

        mockMvc.perform(delete(url)
                .cookie(new Cookie(Types.UUID, uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
