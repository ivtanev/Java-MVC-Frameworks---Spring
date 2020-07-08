package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void login_returnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/users/login")).andExpect(view().name("login"));
    }

    @Test
    public void register_returnsCorrectView() throws Exception {
        this.mockMvc.perform(get("/users/register")).andExpect(view().name("register"));
    }

    @Test
    public void register_withCorrectParameters_redirectsToLoginPage() throws Exception {
        userRepository.deleteAll();
        this.mockMvc.perform(post("/users/register")
                .param("username", "pesho")
                .param("password", "1")
                .param("confirmPassword", "1")
                .param("email", "pesho@abv.bg"))
        .andExpect(view().name("redirect:login"));
    }

    @Test
    public void register_withCorrectParams_registersUserInDatabase() throws Exception {
        userRepository.deleteAll();
        this.mockMvc
                .perform(post("/users/register")
                        .param("username", "pesho")
                        .param("password", "1")
                        .param("confirmPassword", "1")
                        .param("email", "pesho@abv.bg"));
        Assert.assertEquals("pesho", this.userRepository.findByUsername("pesho").get().getUsername());
    }

    @Test
    public void register_withIncorrectParameters_throwsException() throws Exception {
        userRepository.deleteAll();
        this.mockMvc
                .perform(post("/users/register")
                        .param("username", "")
                        .param("password", "")
                        .param("confirmPassword", "asd")
                        .param("email", ""));
    Assert.assertEquals(1, this.userRepository.count());
    }
}
