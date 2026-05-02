package pt.ul.fc.di.css.javafxtascaeats.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.List;
import pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest;
import pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse;
import pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest;
import pt.ul.fc.css.tascaeats.grpc.LoginRequest;
import pt.ul.fc.css.tascaeats.grpc.TascaEatsServiceGrpc;
import pt.ul.fc.css.tascaeats.grpc.UserRequest;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;
import pt.ul.fc.di.css.javafxtascaeats.model.RestaurantModel;

public class ApiClient {

  private static final String HOST = "localhost";
  private static final int PORT = 9090;

  private static final ManagedChannel channel =
      ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();

  private static final TascaEatsServiceGrpc.TascaEatsServiceBlockingStub stub =
      TascaEatsServiceGrpc.newBlockingStub(channel);

  public static UserResponse login(String username, String password) {
    LoginRequest request =
        LoginRequest.newBuilder().setUsername(username).setPassword(password).build();
    try {
      return stub.login(request);
    } catch (StatusRuntimeException e) {
      if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
        throw new IllegalArgumentException("Invalid credentials");
      }
      throw e;
    }
  }

  public static UserResponse registerAdmin(
      String username, String password, String name, String phoneNumber, String nif) {
    UserRequest request =
        UserRequest.newBuilder()
            .setUsername(username)
            .setPassword(password)
            .setName(name)
            .setPhoneNumber(phoneNumber)
            .setNif(nif)
            .build();
    try {
      return stub.registerAdmin(request);
    } catch (StatusRuntimeException e) {
      if (e.getStatus().getCode() == Status.Code.ALREADY_EXISTS) {
        throw new IllegalArgumentException("Username already exists");
      }
      throw e;
    }
  }

  public static DeliveryPersonResponse registerDeliveryPerson(
      String username, String password, String name, String phoneNumber, String nif) {
    UserRequest request =
        UserRequest.newBuilder()
            .setUsername(username)
            .setPassword(password)
            .setName(name)
            .setPhoneNumber(phoneNumber)
            .setNif(nif)
            .build();
    try {
      return stub.registerDeliveryPerson(request);
    } catch (StatusRuntimeException e) {
      if (e.getStatus().getCode() == Status.Code.ALREADY_EXISTS) {
        throw new IllegalArgumentException("Username already exists");
      }
      throw e;
    }
  }

  public static void updateAdmin(
      Long userId,
      String username,
      String newPassword,
      String name,
      String phoneNumber,
      String nif) {
    UserRequest request =
        UserRequest.newBuilder()
            .setId(userId)
            .setUsername(username)
            .setPassword(newPassword)
            .setName(name)
            .setPhoneNumber(phoneNumber)
            .setNif(nif)
            .build();
    try {
      stub.updateAdmin(request);
    } catch (StatusRuntimeException e) {
      throw e;
    }
  }

  public static void updateClient(
      Long userId,
      String username,
      String newPassword,
      String name,
      String phoneNumber,
      String nif) {
    UserRequest request =
        UserRequest.newBuilder()
            .setId(userId)
            .setUsername(username)
            .setPassword(newPassword)
            .setName(name)
            .setPhoneNumber(phoneNumber)
            .setNif(nif)
            .build();
    try {
      stub.updateClient(request);
    } catch (StatusRuntimeException e) {
      throw e;
    }
  }

  public static void updateDeliveryPerson(
      Long userId,
      String username,
      String newPassword,
      String name,
      String phoneNumber,
      String nif,
      String status) {
    DeliveryPersonUpdateRequest request =
        DeliveryPersonUpdateRequest.newBuilder()
            .setId(userId)
            .setUsername(username)
            .setPassword(newPassword)
            .setName(name)
            .setPhoneNumber(phoneNumber)
            .setNif(nif)
            .setStatus(status)
            .build();
    try {
      stub.updateDeliveryPerson(request);
    } catch (StatusRuntimeException e) {
      throw e;
    }
  }

  public static void deleteAccount(Long userId) {
    DeleteAccountRequest request = DeleteAccountRequest.newBuilder().setId(userId).build();
    try {
      stub.deleteAccount(request);
    } catch (StatusRuntimeException e) {
      throw e;
    }
  }

  public static List<RestaurantModel> searchAdminRestaurants(
      long adminId,
      String name,
      String city,
      String street,
      String postalCode,
      String cuisine,
      String minOrders,
      String minReviews,
      String activeAt,
      String maxAvgPrice) {
    AdminRestaurantsRequest request =
        AdminRestaurantsRequest.newBuilder()
            .setAdminId(adminId)
            .setName(name != null ? name : "")
            .setCity(city != null ? city : "")
            .setStreet(street != null ? street : "")
            .setPostalCode(postalCode != null ? postalCode : "")
            .setCuisine(cuisine != null ? cuisine : "")
            .setMinOrders(minOrders != null ? minOrders : "")
            .setMinReviews(minReviews != null ? minReviews : "")
            .setActiveAt(activeAt != null ? activeAt : "")
            .setMaxAvgPrice(maxAvgPrice != null ? maxAvgPrice : "")
            .build();
    System.out.println("name: " + request.getName());
    AdminRestaurantsResponse response = stub.searchAdminRestaurants(request);
    return response.getRestaurantsList().stream()
        .map(
            item ->
                new RestaurantModel(
                    item.getId(),
                    item.getDesignation(),
                    item.getStatus(),
                    item.getNif(),
                    item.getStreet(),
                    item.getCity(),
                    item.getPostalCode(),
                    item.getOpenHours(),
                    item.getCloseHours()))
        .toList();
  }
}
