package pt.ul.fc.di.css.javafxtascaeats.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.List;
import pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest;
import pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse;
import pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest;
import pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest;
import pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest;
import pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest;
import pt.ul.fc.css.tascaeats.grpc.Empty;
import pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest;
import pt.ul.fc.css.tascaeats.grpc.LoginRequest;
import pt.ul.fc.css.tascaeats.grpc.MenusResponse;
import pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse;
import pt.ul.fc.css.tascaeats.grpc.OrderIdRequest;
import pt.ul.fc.css.tascaeats.grpc.OrderResponse;
import pt.ul.fc.css.tascaeats.grpc.OrdersResponse;
import pt.ul.fc.css.tascaeats.grpc.PaymentRequest;
import pt.ul.fc.css.tascaeats.grpc.ProductsResponse;
import pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse;
import pt.ul.fc.css.tascaeats.grpc.RestaurantItem;
import pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse;
import pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest;
import pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest;
import pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest;
import pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest;
import pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest;
import pt.ul.fc.css.tascaeats.grpc.TascaEatsServiceGrpc;
import pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest;
import pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest;
import pt.ul.fc.css.tascaeats.grpc.UserRequest;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;
import pt.ul.fc.di.css.javafxtascaeats.model.MenuModel;
import pt.ul.fc.di.css.javafxtascaeats.model.OrderModel;
import pt.ul.fc.di.css.javafxtascaeats.model.OrderProductItemModel;
import pt.ul.fc.di.css.javafxtascaeats.model.ProductModel;
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

  public static List<MenuModel> getAllMenus() {
    MenusResponse response = stub.getAllMenus(Empty.getDefaultInstance());
    return response.getMenusList().stream()
        .map(
            item ->
                new MenuModel(
                    item.getId(), item.getName(), item.getCuisine(), item.getProductIdsList()))
        .toList();
  }

  public static void assignMenu(long restaurantId, long menuId) {
    AssignMenuRequest request =
        AssignMenuRequest.newBuilder().setRestaurantId(restaurantId).setMenuId(menuId).build();
    stub.assignMenu(request);
  }

  public static void changeRestaurantStatus(long restaurantId, String status) {
    ChangeStatusRequest request =
        ChangeStatusRequest.newBuilder().setRestaurantId(restaurantId).setStatus(status).build();
    stub.changeRestaurantStatus(request);
  }

  public static RestaurantModel createRestaurant(
      long adminId,
      String designation,
      String nif,
      String city,
      String postalCode,
      String street,
      String openHours,
      String closeHours) {
    CreateRestaurantRequest request =
        CreateRestaurantRequest.newBuilder()
            .setAdminId(adminId)
            .setDesignation(designation)
            .setNif(nif)
            .setCity(city)
            .setPostalCode(postalCode)
            .setStreet(street)
            .setOpenHours(openHours)
            .setCloseHours(closeHours)
            .build();
    RestaurantItem item = stub.createRestaurant(request);
    return new RestaurantModel(
        item.getId(),
        item.getDesignation(),
        item.getStatus(),
        item.getNif(),
        item.getStreet(),
        item.getCity(),
        item.getPostalCode(),
        item.getOpenHours(),
        item.getCloseHours());
  }

  public static List<ProductModel> getRestaurantProducts(
      long restaurantId,
      String price,
      String designation,
      String description,
      String availability,
      String timesOrdered) {
    GetRestaurantProductsRequest request =
        GetRestaurantProductsRequest.newBuilder()
            .setRestaurantId(restaurantId)
            .setPrice(price != null ? price : "")
            .setDesignation(designation != null ? designation : "")
            .setDescription(description != null ? description : "")
            .setAvailability(availability != null ? availability : "")
            .setTimesOrdered(timesOrdered != null ? timesOrdered : "")
            .build();
    ProductsResponse response = stub.getRestaurantProducts(request);
    return response.getProductsList().stream()
        .map(
            item ->
                new ProductModel(
                    item.getId(),
                    item.getPrice(),
                    item.getDescription(),
                    item.getDesignation(),
                    "",
                    item.getStatus()))
        .toList();
  }

  public static void updateProductStatus(long restaurantId, long productId, String status) {
    UpdateProductStatusRequest request =
        UpdateProductStatusRequest.newBuilder()
            .setRestaurantId(restaurantId)
            .setProductId(productId)
            .setStatus(status)
            .build();
    stub.updateProductStatus(request);
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

  public static List<UserResponse> searchUsers(
      String name, String userType, String orderNumber, String deliveryNumber) {
    SearchUsersRequest request =
        SearchUsersRequest.newBuilder()
            .setName(name != null ? name : "")
            .setUserType(userType != null ? userType : "")
            .setOrderNumber(orderNumber != null ? orderNumber : "")
            .setDeliveryNumber(deliveryNumber != null ? deliveryNumber : "")
            .build();
    try {
      return stub.searchUsers(request).getUsersList();
    } catch (StatusRuntimeException e) {
      throw e;
    }
  }

  public static List<RestaurantModel> searchRestaurants(
      String name,
      String city,
      String street,
      String postalCode,
      String cuisine,
      String minOrders,
      String minReviews,
      String activeAt,
      String maxAvgPrice) {
    SearchRestaurantsRequest request =
        SearchRestaurantsRequest.newBuilder()
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
    RestaurantsResponse response = stub.searchRestaurants(request);
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

  public static List<MenuModel> searchMenus(String name, String productNumber, String average) {
    SearchMenusRequest request =
        SearchMenusRequest.newBuilder()
            .setName(name != null ? name : "")
            .setProductNumber(productNumber != null ? productNumber : "")
            .setAverage(average != null ? average : "")
            .build();
    MenusSearchResponse response = stub.searchMenus(request);
    return response.getMenusList().stream()
        .map(item -> new MenuModel(item.getId(), item.getName(), item.getCuisine(), List.of()))
        .toList();
  }

  private static OrderModel mapOrderResponse(OrderResponse item) {
    return new OrderModel(
        item.getId(),
        item.getStatus(),
        item.getTotalValue(),
        item.getClientId(),
        item.getClientName(),
        item.getClientNif(),
        item.getCity(),
        item.getStreet(),
        item.getPostalCode(),
        item.getCreatedAt(),
        item.getDeliveryPersonName(),
        item.getDeliveryPersonNif(),
        item.getProductsList().stream()
            .map(p -> new OrderProductItemModel(p.getId(), p.getDesignation(), p.getPrice()))
            .toList(),
        item.getPaymentAmount(),
        item.getPaymentAt(),
        item.getPaymentChange());
  }

  public static List<OrderModel> getAllOrders() {
    OrdersResponse response = stub.getAllOrders(Empty.getDefaultInstance());
    return response.getOrdersList().stream().map(ApiClient::mapOrderResponse).toList();
  }

  public static List<OrderModel> searchOrders(String status) {
    SearchOrdersRequest request =
        SearchOrdersRequest.newBuilder().setStatus(status != null ? status : "").build();
    OrdersResponse response = stub.searchOrders(request);
    return response.getOrdersList().stream().map(ApiClient::mapOrderResponse).toList();
  }

  public static OrderModel processPayment(
      Long orderId, String paymentMethod, double amount, String paymentInfo) {
    PaymentRequest request =
        PaymentRequest.newBuilder()
            .setOrderId(orderId)
            .setPaymentMethod(paymentMethod)
            .setAmount(amount)
            .setPaymentInfo(paymentInfo != null ? paymentInfo : "")
            .build();
    try {
      return mapOrderResponse(stub.processPayment(request));
    } catch (StatusRuntimeException e) {
      throw new IllegalArgumentException(e.getStatus().getDescription());
    }
  }

  public static OrderModel updateOrderStatus(Long orderId, String status) {
    UpdateOrderStatusRequest request =
        UpdateOrderStatusRequest.newBuilder().setOrderId(orderId).setStatus(status).build();
    try {
      return mapOrderResponse(stub.updateOrderStatus(request));
    } catch (StatusRuntimeException e) {
      throw new IllegalArgumentException(e.getStatus().getDescription());
    }
  }

  public static void cancelOrder(Long orderId) {
    OrderIdRequest request = OrderIdRequest.newBuilder().setOrderId(orderId).build();
    try {
      stub.cancelOrder(request);
    } catch (StatusRuntimeException e) {
      throw new IllegalArgumentException(e.getStatus().getDescription());
    }
  }

  public static List<ProductModel> searchProducts(
      String designation,
      String price,
      String description,
      String availability,
      String timesOrdered) {
    SearchProductsRequest request =
        SearchProductsRequest.newBuilder()
            .setDesignation(designation != null ? designation : "")
            .setPrice(price != null ? price : "")
            .setDescription(description != null ? description : "")
            .setAvailability(availability != null ? availability : "")
            .setTimesOrdered(timesOrdered != null ? timesOrdered : "")
            .build();
    ProductsSearchResponse response = stub.searchProducts(request);
    return response.getProductsList().stream()
        .map(
            item ->
                new ProductModel(
                    item.getId(),
                    item.getPrice(),
                    item.getDescription(),
                    item.getDesignation(),
                    "", // menu
                    item.getAvailability()))
        .toList();
  }
}
