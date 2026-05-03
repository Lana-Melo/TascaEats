package pt.ul.fc.css.tascaeats.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientUpdateRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.LoginRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.AddressDto;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.services.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private UserService userService;
  @Autowired private ObjectMapper objectMapper;

  private AddressDto address() { return new AddressDto("Rua A", "Lisboa", "1000-001"); }
  private UserResponseDto userResponse() {
    return new UserResponseDto(1L, "CLIENT", "john", "John Doe", "912345678", "111222333");
  }
  private ClientResponseDto clientResponse() {
    return new ClientResponseDto(1L, "john", "John Doe", "912345678", "111222333", List.of(address()));
  }
  private DeliveryPersonResponseDto deliveryPersonResponse() {
    return new DeliveryPersonResponseDto(1L, "dp", "DP Name", "912000000", "111111112", "AVAILABLE");
  }

  // ─── POST /api/users/login ───────────────────────────────────────────────────

  @Test
  void login_shouldReturn200_whenUserExists() throws Exception {
    LoginRequestDto req = new LoginRequestDto();
    req.setUsername("john"); req.setPassword("pass");
    when(userService.login("john", "pass")).thenReturn(userResponse());

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("john"))
        .andExpect(jsonPath("$.role").value("CLIENT"));
  }

  @Test
  void login_shouldReturn404_whenUserNotFound() throws Exception {
    LoginRequestDto req = new LoginRequestDto();
    req.setUsername("ghost"); req.setPassword("pass");
    when(userService.login("ghost", "pass")).thenThrow(new ResourceNotFoundException("Not found"));

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound());
  }

  // ─── POST /api/users/register/client ────────────────────────────────────────

  @Test
  void registerClient_shouldReturn200_whenSuccessful() throws Exception {
    ClientRequestDto req = new ClientRequestDto();
    req.setUsername("jane"); req.setPassword("pass"); req.setName("Jane Doe");
    req.setPhoneNumber("912000000"); req.setNif("111222333");
    req.setCity("Porto"); req.setStreet("Rua B"); req.setPostalCode("4000-001");
    when(userService.registerNewClient(any())).thenReturn(clientResponse());

    mockMvc.perform(post("/api/users/register/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("john"));
  }

  @Test
  void registerClient_shouldReturn400_whenUsernameExists() throws Exception {
    ClientRequestDto req = new ClientRequestDto();
    req.setUsername("jane"); req.setPassword("pass"); req.setName("Jane");
    req.setPhoneNumber("912000000"); req.setNif("111222333");
    req.setCity("Porto"); req.setStreet("Rua B"); req.setPostalCode("4000-001");
    when(userService.registerNewClient(any())).thenThrow(new IllegalArgumentException("Username exists"));

    mockMvc.perform(post("/api/users/register/client")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  // ─── POST /api/users/register/admin ─────────────────────────────────────────

  @Test
  void registerAdmin_shouldReturn200_whenSuccessful() throws Exception {
    UserRequestDto req = new UserRequestDto();
    req.setUsername("admin2"); req.setPassword("pass"); req.setName("Admin");
    req.setPhoneNumber("912000001"); req.setNif("999888777");
    UserResponseDto resp = new UserResponseDto(2L, "ADMIN", "admin2", "Admin", "912000001", "999888777");
    when(userService.registerNewAdmin(any())).thenReturn(resp);

    mockMvc.perform(post("/api/users/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  // ─── POST /api/users/register/delivery ──────────────────────────────────────

  @Test
  void registerDelivery_shouldReturn200_whenSuccessful() throws Exception {
    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setUsername("dp"); req.setPassword("pass"); req.setName("DP");
    req.setPhoneNumber("912000002"); req.setNif("777666555");
    when(userService.registerNewDeliveryPerson(any())).thenReturn(deliveryPersonResponse());

    mockMvc.perform(post("/api/users/register/delivery")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("AVAILABLE"));
  }

  // ─── GET /api/users/{id} ─────────────────────────────────────────────────────

  @Test
  void getUserById_shouldReturn200_whenFound() throws Exception {
    when(userService.getUserById(1L)).thenReturn(userResponse());

    mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void getUserById_shouldReturn404_whenNotFound() throws Exception {
    when(userService.getUserById(99L)).thenThrow(new ResourceNotFoundException("Not found"));

    mockMvc.perform(get("/api/users/99")).andExpect(status().isNotFound());
  }

  // ─── GET /api/users/search ───────────────────────────────────────────────────

  @Test
  void searchUser_shouldReturn200_withResults() throws Exception {
    when(userService.searchBy(any(), any(), any(), any())).thenReturn(List.of(userResponse()));

    mockMvc.perform(get("/api/users/search").param("name", "John"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("John Doe"));
  }

  @Test
  void searchUser_shouldReturn200_withEmptyList() throws Exception {
    when(userService.searchBy(any(), any(), any(), any())).thenReturn(List.of());

    mockMvc.perform(get("/api/users/search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  // ─── PUT /api/users/client/{id} ──────────────────────────────────────────────

  @Test
  void updateClient_shouldReturn200_whenSuccessful() throws Exception {
    ClientUpdateRequestDto req = new ClientUpdateRequestDto();
    req.setName("New Name"); req.setUsername("newjohn"); req.setPhoneNumber("912000000");
    req.setNif("111222333"); req.setPassword("newpass");
    when(userService.updateClient(eq(1L), any())).thenReturn(clientResponse());

    mockMvc.perform(put("/api/users/client/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());
  }

  @Test
  void updateClient_shouldReturn404_whenNotFound() throws Exception {
    ClientUpdateRequestDto req = new ClientUpdateRequestDto();
    req.setName("X"); req.setUsername("x"); req.setPhoneNumber("900000000");
    req.setNif("000000000"); req.setPassword("x");
    when(userService.updateClient(eq(99L), any())).thenThrow(new ResourceNotFoundException("Not found"));

    mockMvc.perform(put("/api/users/client/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound());
  }

  // ─── PUT /api/users/admin/{id} ───────────────────────────────────────────────

  @Test
  void updateAdmin_shouldReturn200_whenSuccessful() throws Exception {
    UserRequestDto req = new UserRequestDto();
    req.setName("Admin2"); req.setUsername("admin2"); req.setPhoneNumber("900000001");
    req.setNif("999000001"); req.setPassword("pass2");
    UserResponseDto resp = new UserResponseDto(1L, "ADMIN", "admin2", "Admin2", "900000001", "999000001");
    when(userService.updateAdmin(eq(1L), any())).thenReturn(resp);

    mockMvc.perform(put("/api/users/admin/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("admin2"));
  }

  // ─── PUT /api/users/delivery/{id} ────────────────────────────────────────────

  @Test
  void updateDeliveryPerson_shouldReturn200_whenSuccessful() throws Exception {
    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setName("DP2"); req.setUsername("dp2"); req.setPhoneNumber("900000002");
    req.setNif("777000002"); req.setPassword("pass2");
    when(userService.updateDeliveryPerson(eq(1L), any())).thenReturn(deliveryPersonResponse());

    mockMvc.perform(put("/api/users/delivery/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());
  }

  // ─── DELETE /api/users/{id} ──────────────────────────────────────────────────

  @Test
  void deleteUser_shouldReturn204_whenDeleted() throws Exception {
    when(userService.deleteUser(1L)).thenReturn(true);
    mockMvc.perform(delete("/api/users/1")).andExpect(status().isNoContent());
  }

  @Test
  void deleteUser_shouldReturn404_whenNotFound() throws Exception {
    when(userService.deleteUser(99L)).thenReturn(false);
    mockMvc.perform(delete("/api/users/99")).andExpect(status().isNotFound());
  }
}