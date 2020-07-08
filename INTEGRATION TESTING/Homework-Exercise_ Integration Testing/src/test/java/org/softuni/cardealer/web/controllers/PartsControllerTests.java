package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    private Supplier supplier;

    @Before
    public void init() {
        supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        supplierRepository.saveAndFlush(supplier);
    }

    @Test
    @WithMockUser()
    public void add_withCorrectParameters_createsSupplier() throws Exception {
        this.partRepository.deleteAll();
        this.supplierRepository.deleteAll();
        Supplier newSupplier = new Supplier();
        newSupplier.setName("pesho");
        newSupplier.setIsImporter(true);
        supplierRepository.saveAndFlush(newSupplier);
        this.mockMvc.perform(post("/parts/add")
                .param("name", "myNewPart")
                .param("price", String.valueOf(new BigDecimal(2.5)))
                .param("supplier", String.valueOf(newSupplier.getName())));
        Assert.assertEquals("myNewPart", this.partRepository.findAll().get(0).getName());
    }

    @Test
    @WithMockUser()
    public void edit_withCorrectParameters_editsSupplier() throws Exception {
        this.partRepository.deleteAll();
        Part part = new Part();
        part.setName("asd");
        part.setPrice(new BigDecimal(2.5));
        part.setSupplier(supplier);
        part = this.partRepository.saveAndFlush(part);
        this.mockMvc
                .perform(post("/parts/edit/" + part.getId())
                        .param("name", "newName")
                        .param("price", String.valueOf(new BigDecimal(2.5)))
                        .param("supplier", supplier.getName()));
        Assert.assertEquals("newName", this.partRepository.findAll().get(0).getName());
    }

    @Test
    @WithMockUser
    public void delete_withCorrectId_deletesSupplier() throws Exception {
        this.partRepository.deleteAll();
        Part part = new Part();
        part.setName("asd");
        part.setPrice(new BigDecimal(2.5));
        part.setSupplier(supplier);
        part = this.partRepository.saveAndFlush(part);
        this.mockMvc.perform(post("/parts/delete/" + part.getId()));
        Assert.assertEquals(0, this.partRepository.count());
    }

    @Test
    @WithMockUser
    public void all_returnsCorrectView() throws Exception {
        this.mockMvc
                .perform(get("/parts/all"))
                .andExpect(view().name("all-parts"));
    }

    @Test
    @WithMockUser
    public void fetch_withSuppliersInDatabase_returnsList() throws Exception {
        this.partRepository.deleteAll();
        Part part = new Part();
        part.setName("asd");
        part.setPrice(new BigDecimal(2.5));
        part.setSupplier(supplier);
        part = this.partRepository.saveAndFlush(part);
        mockMvc
                .perform(get("/parts/fetch"))
                .andExpect(content().json("[{'name':'asd', 'price':2.5, 'supplier':{'name':pesho, 'isImporter':true}}]"));
    }

    @Test
    @WithMockUser
    public void fetch_withNoSuppliersInDatabase_returnsEmptyList() throws Exception {
        this.partRepository.deleteAll();
        mockMvc.perform(get("/parts/fetch")).andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    public void add_redirectsToAll() throws Exception {
        this.partRepository.deleteAll();
        this.supplierRepository.deleteAll();
        Supplier newSupplier = new Supplier();
        newSupplier.setName("pesho");
        newSupplier.setIsImporter(true);
        supplierRepository.saveAndFlush(newSupplier);
        this.mockMvc.perform(post("/parts/add")
                .param("name", "anotherNewPart")
                .param("price", String.valueOf(new BigDecimal(2.5)))
                .param("supplier", newSupplier.getName()))
                .andExpect(redirectedUrl("all"));
    }

    @Test
    @WithMockUser
    public void edit_redirectsToAll() throws Exception {
        this.partRepository.deleteAll();
        Part part = new Part();
        part.setName("asd");
        part.setPrice(new BigDecimal(2.5));
        part.setSupplier(supplier);
        part = this.partRepository.saveAndFlush(part);
        this.mockMvc
                .perform(post("/parts/edit/" + part.getId())
                        .param("name", "asd")
                        .param("price", String.valueOf(new BigDecimal(2.5)))
                        .param("supplier", String.valueOf(supplier)))
                .andExpect(redirectedUrl("/parts/all"));
    }

    @Test
    @WithMockUser
    public void delete_redirectsToAll() throws Exception {
        this.partRepository.deleteAll();
        Part part = new Part();
        part.setName("asd");
        part.setPrice(new BigDecimal(2.5));
        part.setSupplier(supplier);
        part = this.partRepository.saveAndFlush(part);
        this.mockMvc.perform(post("/parts/delete/" + part.getId()))
                .andExpect(redirectedUrl("/parts/all"));
    }
}
