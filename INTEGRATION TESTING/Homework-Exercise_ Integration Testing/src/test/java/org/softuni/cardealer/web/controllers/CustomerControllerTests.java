package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    @WithMockUser
    public void add_withCorrectParameters_addsCustomerToDatabase() throws Exception {
        this.mockMvc.perform(post("/customers/add")
                .param("name", "pesho")
                .param("birthDate", String.valueOf(LocalDate.now()))
                .param("isYoungDriver", String.valueOf(true)));
        Assert.assertEquals("pesho", this.customerRepository.findAll().get(0).getName());
    }

    @Test
    @WithMockUser
    public void add_withCorrectParameters_redirectsToAll() throws Exception {
        this.mockMvc.perform(post("/customers/add")
                .param("name", "pesho")
                .param("birthDate", String.valueOf(LocalDate.now()))
                .param("isYoungDriver", String.valueOf(true)))
        .andExpect(view().name("redirect:all"));
    }

    @Test
    @WithMockUser
    public void all_returnsCorrectView() throws Exception {
        this.mockMvc
                .perform(get("/customers/all"))
                .andExpect(view().name("all-customers"));
    }
}
