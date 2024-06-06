package me.nolanjames.halo.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.nolanjames.halo.security.auth.model.AuthRequest;
import me.nolanjames.halo.security.auth.model.AuthResponse;
import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.model.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    private static final String USERS_ENDPOINT = "/api/users";
    private static final String GET_ACCESS_TOKEN_ENDPOINT = "/api/oauth/token";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testListAllUsersFail() throws Exception {
        mockMvc.perform(get(USERS_ENDPOINT).header("Authorization", "Bearer invalid "))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[0]").isNotEmpty());
    }

    @Test
    public void testListAllUsersSuccess() throws Exception {
        AuthRequest request = new AuthRequest("Jim", "mij");
        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc
                .perform(post(GET_ACCESS_TOKEN_ENDPOINT)
                        .contentType("application/json")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseBody, AuthResponse.class);
        String bearerToken = "Bearer " + authResponse.accessToken();

        mockMvc.perform(get(USERS_ENDPOINT).header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").isString())
                .andExpect(jsonPath("$[1].role").isString());
    }

    @Test
    @Transactional
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest("Hudson", passwordEncoder.encode("password"));
        String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(USERS_ENDPOINT).contentType("application/json").content(requestBody)
                        .with(jwt().authorities(new SimpleGrantedAuthority("admin"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.role").isString());
        ;
    }

//    @Test
//    @Transactional
//    public void testUpdateUser() {
//        UserRequest userRequest
//    }

}