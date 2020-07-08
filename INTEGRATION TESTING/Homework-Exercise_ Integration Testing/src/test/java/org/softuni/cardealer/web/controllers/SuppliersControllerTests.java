package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SuppliersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    @WithMockUser()
    public void add_withCorrectParameters_createsSupplier() throws Exception {
        this.supplierRepository.deleteAll();
        this.mockMvc.perform(post("/suppliers/add")
                .param("name", "pesho")
                .param("isImporter", String.valueOf(true)));
        Assert.assertEquals(1, this.supplierRepository.count());
    }

    @Test
    @WithMockUser()
    public void edit_withCorrectParameters_editsSupplier() throws Exception {
        this.supplierRepository.deleteAll();
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        supplier = this.supplierRepository.saveAndFlush(supplier);
        this.mockMvc.perform(post("/suppliers/edit/" + supplier.getId())
                .param("name", "gosho")
                .param("isImporter", String.valueOf(false)));
        Assert.assertFalse(this.supplierRepository.findByName("gosho").get().getIsImporter());
    }

    @Test
    @WithMockUser
    public void delete_withCorrectId_deletesSupplier() throws Exception {
        this.supplierRepository.deleteAll();
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        supplier = this.supplierRepository.saveAndFlush(supplier);
        this.mockMvc.perform(post("/suppliers/delete/" + supplier.getId()));
        Assert.assertEquals(0, this.supplierRepository.count());
    }

    @Test
    @WithMockUser
    public void all_returnsCorrectView() throws Exception {
        this.mockMvc
                .perform(get("/suppliers/all"))
                .andExpect(view().name("all-suppliers"));
    }

    @Test
    @WithMockUser
    public void fetch_withSuppliersInDatabase_returnsList() throws Exception {
        this.supplierRepository.deleteAll();
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        supplier = this.supplierRepository.saveAndFlush(supplier);
        mockMvc.perform(get("/suppliers/fetch")).andExpect(content().json("[{'name':'pesho', 'isImporter':true}]"));
    }

    @Test
    @WithMockUser
    public void fetch_withNoSuppliersInDatabase_returnsEmptyList() throws Exception {
        this.supplierRepository.deleteAll();
        mockMvc.perform(get("/suppliers/fetch")).andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    public void add_redirectsToAll() throws Exception {
        this.supplierRepository.deleteAll();
        this.mockMvc.perform(post("/suppliers/add")
                .param("name", "pesho")
                .param("isImporter", String.valueOf(true)))
                .andExpect(view().name("redirect:all"));
    }

    @Test
    @WithMockUser
    public void edit_redirectsToAll() throws Exception {
        this.supplierRepository.deleteAll();
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        supplier = this.supplierRepository.saveAndFlush(supplier);
        this.mockMvc
                .perform(post("/suppliers/edit/" + supplier.getId())
                        .param("name", "gosho")
                        .param("isImporter", String.valueOf(false)))
                .andExpect(redirectedUrl("/suppliers/all"));
    }

    @Test
    @WithMockUser
    public void delete_redirectsToAll() throws Exception {
        this.supplierRepository.deleteAll();
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        supplier = this.supplierRepository.saveAndFlush(supplier);
        this.mockMvc.perform(post("/suppliers/delete/" + supplier.getId()))
                .andExpect(redirectedUrl("/suppliers/all"));
    }
}
