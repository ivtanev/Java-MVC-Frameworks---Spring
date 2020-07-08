package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.CarRepository;
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
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    private Part part1;
    private Part part2;
    private Supplier supplier;

    @Before
    public void init() {
        supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);
        this.supplierRepository.saveAndFlush(supplier);

        part1 = new Part();
        part1.setName("newPart");
        part1.setPrice(new BigDecimal(2.5));
        part1.setSupplier(supplier);
        this.partRepository.saveAndFlush(part1);

        part2 = new Part();
        part2.setName("anotherPart");
        part2.setPrice(new BigDecimal(205.25));
        part2.setSupplier(supplier);
        this.partRepository.saveAndFlush(part2);
    }

    @Test
    @WithMockUser()
    public void add_withCorrectParameters_createsCar() throws Exception {
        this.carRepository.deleteAll();
        this.mockMvc.perform(post("/cars/add")
                .param("make", "Ford")
                .param("model", "Fiesta")
                .param("travelledDistance", String.valueOf(2000))
                .param("parts", part1.getId()));
        Assert.assertEquals("Ford", this.carRepository.findAll().get(0).getMake());
    }

    @Test
    @WithMockUser()
    public void edit_withCorrectParameters_editsCar() throws Exception {
        this.carRepository.deleteAll();
        Car car = new Car();
        car.setMake("Fiat");
        car.setModel("Punto");
        car.setTravelledDistance(200000L);
        car.setParts(new ArrayList<>() {{
            add(part1);
            add(part2);
        }});
        car = this.carRepository.saveAndFlush(car);
        this.mockMvc
                .perform(post("/cars/edit/" + car.getId())
                        .param("make", "Fiat")
                        .param("model", "Altea")
                        .param("travelledDistance", String.valueOf(2000000)));
        Assert.assertEquals("Altea", this.carRepository.findAll().get(0).getModel());
    }

    @Test
    @WithMockUser
    public void delete_withCorrectId_deletesCar() throws Exception {
        this.carRepository.deleteAll();
        Car car = new Car();
        car.setMake("Fiat");
        car.setModel("Punto");
        car.setTravelledDistance(200000L);
        car.setParts(new ArrayList<>() {{
            add(part1);
            add(part2);
        }});
        car = this.carRepository.saveAndFlush(car);
        this.mockMvc.perform(post("/cars/delete/" + car.getId()));
        Assert.assertEquals(0, this.carRepository.count());
    }

    @Test
    @WithMockUser
    public void all_returnsCorrectView() throws Exception {
        this.mockMvc
                .perform(get("/cars/all"))
                .andExpect(view().name("all-cars"));
    }

    @Test
    @WithMockUser
    public void add_redirectsToAll() throws Exception {
        this.carRepository.deleteAll();
        this.mockMvc.perform(post("/cars/add")
                .param("make", "Ford")
                .param("model", "Fiesta")
                .param("travelledDistance", String.valueOf(2000))
                .param("parts", part1.getId()))
                .andExpect(redirectedUrl("all"));
    }

    @Test
    @WithMockUser
    public void edit_redirectsToAll() throws Exception {
        this.carRepository.deleteAll();
        Car car = new Car();
        car.setMake("Fiat");
        car.setModel("Punto");
        car.setTravelledDistance(200000L);
        car.setParts(new ArrayList<>() {{
            add(part1);
            add(part2);
        }});
        car = this.carRepository.saveAndFlush(car);
        this.mockMvc
                .perform(post("/cars/edit/" + car.getId())
                        .param("make", "Ford")
                        .param("model", "Fiesta")
                        .param("travelledDistance", String.valueOf(2000))
                        .param("parts", part1.getId()))
                .andExpect(redirectedUrl("/cars/all"));
    }

    @Test
    @WithMockUser
    public void delete_redirectsToAll() throws Exception {
        this.carRepository.deleteAll();
        Car car = new Car();
        car.setMake("Fiat");
        car.setModel("Punto");
        car.setTravelledDistance(200000L);
        car.setParts(new ArrayList<>() {{
            add(part1);
            add(part2);
        }});
        car = this.carRepository.saveAndFlush(car);
        this.mockMvc.perform(post("/cars/delete/" + car.getId()))
                .andExpect(redirectedUrl("/cars/all"));
    }
}
