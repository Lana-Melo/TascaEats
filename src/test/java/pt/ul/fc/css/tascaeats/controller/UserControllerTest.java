package pt.ul.fc.css.tascaeats.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.LoginRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponse;
import pt.ul.fc.css.tascaeats.services.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- LOGIN ---
    @Test
    void login_shouldReturn200_whenUserExists() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("pass");
        UserResponse response = new UserResponse(1L,"john", "john", "123 456 678", "111 222 333");
        

        when(userService.login("john", "pass")).thenReturn(response);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void login_shouldReturn404_whenUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("123456");

        when(userService.login("ghost", "pass")).thenReturn(null);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    
    // --- REGISTER CLIENT ---
    @Test
    void registerClient_shouldReturn200_whenSuccessful() throws Exception {
        ClientRequest request = new ClientRequest();
        request.setUsername("jane");
        request.setPassword("pass");
        request.setName("Jane Doe");
        request.setPhoneNumber("912345678");
        request.setNif("123456789");
        request.setAddresses(List.of(List.of("Lisboa", "Rua A", "1000-001")));

        ClientResponse response = new ClientResponse(1L, "jane", "Jane Doe", "912345678", "123456789", List.of(List.of("Lisboa", "Rua A", "1000-001")));

        when(userService.registerNewClient(any())).thenReturn(response);

        mockMvc.perform(post("/api/users/register/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("jane"))
            .andExpect(jsonPath("$.city").value("Lisboa"));
    }

    @Test
    void registerClient_shouldReturn400_whenServiceFails() throws Exception {
        ClientRequest request = new ClientRequest();
        request.setUsername("jane");
        request.setPassword("pass");
        request.setName("Jane Doe");
        request.setPhoneNumber("912345678");
        request.setNif("123456789");
        request.setAddresses(List.of(List.of("Lisboa", "Rua A", "1000-001")));

        when(userService.registerNewClient(any())).thenReturn(null);

        mockMvc.perform(post("/api/users/register/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // --- REGISTER ADMIN ---
    @Test
    void registerAdmin_shouldReturn200_whenSuccessful() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        request.setPassword("pass");
        request.setName("Admin User");
        request.setPhoneNumber("912345678");
        request.setNif("987654321");

        UserResponse response = new UserResponse(2L, "admin", "Admin User", "912345678", "987654321");

        when(userService.registerNewAdmin(any())).thenReturn(response);

        mockMvc.perform(post("/api/users/register/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void registerAdmin_shouldReturn400_whenServiceFails() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        request.setPassword("pass");
        request.setName("Admin User");
        request.setPhoneNumber("912345678");
        request.setNif("987654321");

        when(userService.registerNewAdmin(any())).thenReturn(null);

        mockMvc.perform(post("/api/users/register/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // --- REGISTER DELIVERY ---
    @Test
    void registerDelivery_shouldReturn200_whenSuccessful() throws Exception {
        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery1");
        request.setPassword("pass");
        request.setName("João Entregador");
        request.setPhoneNumber("931234567");
        request.setNif("111222333");


        DeliveryPersonResponse response = new DeliveryPersonResponse(3L, "delivery1", "João Entregador", "931234567", "111222333", "AVAILABLE");

        when(userService.registerNewDeliveryPerson(any())).thenReturn(response);

        mockMvc.perform(post("/api/users/register/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("delivery1"))
            .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void registerDelivery_shouldReturn400_whenServiceFails() throws Exception {
        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery1");
        request.setPassword("pass");
        request.setName("João Entregador");
        request.setPhoneNumber("931234567");
        request.setNif("111222333");

        when(userService.registerNewDeliveryPerson(any())).thenReturn(null);

        mockMvc.perform(post("/api/users/register/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // --- UPDATE CLIENT ---
    @Test
    void updateClient_shouldReturn200_whenClientExists() throws Exception {
        ClientRequest request = new ClientRequest();
        request.setUsername("jane_updated");
        request.setPassword("pass");
        request.setName("Jane Updated");
        request.setPhoneNumber("912345678");
        request.setNif("123456789");
        request.setAddresses(List.of(List.of("Porto", "Rua B", "4000-001")));

        ClientResponse response = new ClientResponse(1L, "jane_updated", "Jane Updated", "912345678", "123456789", List.of(List.of("Porto", "Rua B", "4000-001")));

        when(userService.updateClient(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/users/client/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("jane_updated"))
            .andExpect(jsonPath("$.city").value("Porto"));
    }

    @Test
    void updateClient_shouldReturn404_whenClientNotFound() throws Exception {
        ClientRequest request = new ClientRequest();
        request.setUsername("jane_updated");
        request.setPassword("pass");
        request.setName("Jane Updated");
        request.setPhoneNumber("912345678");
        request.setNif("123456789");
        request.setAddresses(List.of(List.of("Porto", "Rua B", "4000-001")));

        when(userService.updateClient(eq(99L), any())).thenReturn(null);

        mockMvc.perform(put("/api/users/client/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    // --- UPDATE ADMIN ---
    @Test
    void updateAdmin_shouldReturn200_whenAdminExists() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("admin_updated");
        request.setPassword("pass");
        request.setName("Admin Updated");
        request.setPhoneNumber("912345678");
        request.setNif("987654321");

        UserResponse response = new UserResponse(1L, "admin_updated", "Admin Updated", "912345678", "987654321");

        when(userService.updateAdmin(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/users/admin/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("admin_updated"));
    }

    @Test
    void updateAdmin_shouldReturn404_whenAdminNotFound() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("admin_updated");
        request.setPassword("pass");
        request.setName("Admin Updated");
        request.setPhoneNumber("912345678");
        request.setNif("987654321");

        when(userService.updateAdmin(eq(99L), any())).thenReturn(null);

        mockMvc.perform(put("/api/users/admin/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    // --- UPDATE DELIVERY PERSON ---

    @Test
    void updateDeliveryPerson_shouldReturn200_whenDeliveryPersonExists() throws Exception {
        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery_updated");
        request.setPassword("pass");
        request.setName("Delivery Updated");
        request.setPhoneNumber("931234567");
        request.setNif("111222333");

        DeliveryPersonResponse response = new DeliveryPersonResponse(1L, "delivery_updated", "Delivery Updated", "931234567", "111222333", "BUSY");

        when(userService.updateDeliveryPerson(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/users/delivery/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("delivery_updated"))
            .andExpect(jsonPath("$.status").value("BUSY"));
    }

    @Test
    void updateDeliveryPerson_shouldReturn404_whenDeliveryPersonNotFound() throws Exception {
        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery_updated");
        request.setPassword("pass");
        request.setName("Delivery Updated");
        request.setPhoneNumber("931234567");
        request.setNif("111222333");

        when(userService.updateDeliveryPerson(eq(99L), any())).thenReturn(null);

        mockMvc.perform(put("/api/users/delivery/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    // --- DELETE 
    @Test
    void deleteUser_shouldReturn204_whenDeleted() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_shouldReturn404_whenNotFound() throws Exception {
        when(userService.deleteUser(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/99"))
            .andExpect(status().isNotFound());
    }
}