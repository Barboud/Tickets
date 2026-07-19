package net.hackyourfuture.tickets.controller;

import net.hackyourfuture.tickets.model.User;
import net.hackyourfuture.tickets.repository.UserRepository;
import net.hackyourfuture.tickets.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void insertData() {
        User user = new User();
        user.setName("Salem");
        user.setEmail("salem@test.com");


        userRepository.saveUser(user);

        savedUser = userRepository.findUserByEmail("salem@test.com");
        if (savedUser == null) {
            savedUser = user;
            savedUser.setId(1);
        }
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnUserById() throws Exception {

        mockMvc.perform(get("/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Salem"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {

        mockMvc.perform(get("/users/" + (savedUser.getId() + 9999)))
                .andExpect(status().isNotFound());
    }
}