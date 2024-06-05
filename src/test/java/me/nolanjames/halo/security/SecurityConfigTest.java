package me.nolanjames.halo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nolanjames.halo.security.auth.model.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {
    private static final String GET_ACCESS_TOKEN_ENDPOINT = "/api/oauth/token";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getBaseUriShouldReturn401() throws Exception {
        mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAccessTokenBadRequest() throws Exception {
        AuthRequest request = new AuthRequest("Hello", "");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc
                .perform(post(GET_ACCESS_TOKEN_ENDPOINT)
                        .contentType("application/json")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAccessTokenFail() throws Exception {
        AuthRequest request = new AuthRequest("Jim", "1234567");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc
                .perform(post(GET_ACCESS_TOKEN_ENDPOINT)
                        .contentType("application/json")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAccessTokenSuccess() throws Exception {
        AuthRequest request = new AuthRequest("Jim", "mij");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc
                .perform(post(GET_ACCESS_TOKEN_ENDPOINT)
                        .contentType("application/json")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }
}