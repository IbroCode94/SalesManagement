package org.sales.salesmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sales.salesmanagement.Controller.AuthController;
import org.sales.salesmanagement.Dto.dtorequest.LoginRequest;
import org.sales.salesmanagement.Dto.response.LoginResponse;
import org.sales.salesmanagement.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testAuthenticateUser_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        LoginRequest loginRequest = new LoginRequest("passworD@", "jamiu0@gmail.com");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("token");
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(loginResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        LoginResponse response = new ObjectMapper().readValue(responseBody, LoginResponse.class);
        assert response.getAccessToken().equals("token");
    }
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
