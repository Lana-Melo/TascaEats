package pt.ul.fc.css.tascaeats.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderProductItemDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequestDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductItemResponseDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.mapper.GrpcMapper;
import pt.ul.fc.css.tascaeats.services.OrderService;
import pt.ul.fc.css.tascaeats.services.RestaurantService;
import pt.ul.fc.css.tascaeats.services.UserService;

// representa o servidor gRPC, onde implementamos os métodos definidos no .proto
@GrpcService
public class TascaEatsGrpcService extends TascaEatsServiceGrpc.TascaEatsServiceImplBase {

  @Autowired private UserService userService;

  @Autowired private RestaurantService restaurantService;

  @Autowired private OrderService orderService;

  @Autowired private pt.ul.fc.css.tascaeats.services.MenuService menuService;

  @Autowired private GrpcMapper grpcMapper;

  // ─── Login ───────────────────────────────────────────────────────
  @Override
  public void login(LoginRequest request, StreamObserver<UserResponse> responseObserver) {
    try {
      String username = request.getUsername();
      String password = request.getPassword();

      UserResponseDto user = userService.login(username, password);

      UserResponse response = grpcMapper.mapUserResponseDtoToResponse(user);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (IllegalArgumentException e) {
      responseObserver.onError(
          Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Register Admin ─────────────────────────────────────────────
  @Override
  public void registerAdmin(UserRequest request, StreamObserver<UserResponse> responseObserver) {
    try {
      UserRequestDto requestDto = grpcMapper.mapAdminRegisterReqToReqDto(request);

      UserResponseDto user = userService.registerNewAdmin(requestDto);

      if (user == null) {
        throw new IllegalArgumentException();
      }

      UserResponse response = grpcMapper.mapUserResponseDtoToResponse(user);

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (IllegalArgumentException e) {
      responseObserver.onError(Status.Code.ALREADY_EXISTS.toStatus().asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Register Delivery Person ─────────────────────────────────────────────
  @Override
  public void registerDeliveryPerson(
      UserRequest request, StreamObserver<DeliveryPersonResponse> responseObserver) {
    try {
      DeliveryPersonRequestDto requestDto =
          grpcMapper.mapDeliveryPersonRegisterReqToReqDto(request);

      DeliveryPersonResponseDto user = userService.registerNewDeliveryPerson(requestDto);

      if (user == null) {
        throw new IllegalArgumentException();
      }

      DeliveryPersonResponse response = grpcMapper.mapDeliveryPersonResponseDtoToResponse(user);

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (IllegalArgumentException e) {
      responseObserver.onError(Status.Code.ALREADY_EXISTS.toStatus().asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Update Admin ─────────────────────────────────────────────
  @Override
  public void updateAdmin(UserRequest request, StreamObserver<UserResponse> responseObserver) {
    try {
      Long id = request.getId();
      UserRequestDto requestDto = grpcMapper.mapAdminRegisterReqToReqDto(request);
      UserResponseDto user = userService.updateAdmin(id, requestDto);
      UserResponse response = grpcMapper.mapUserResponseDtoToResponse(user);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Update Client ─────────────────────────────────────────────
  /* @Override
  public void updateClient(UserRequest request, StreamObserver<UserResponse> responseObserver) {
    try {
      Long id = request.getId();
      UserRequestDto requestDto = grpcMapper.mapAdminRegisterReqToReqDto(request);
      ClientResponseDto user = userService.updateClient(id, requestDto);
      UserResponse response = grpcMapper.mapUserResponseDtoToResponse(user);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }*/

  // ─── Update Delivery Person ─────────────────────────────────────────────
  @Override
  public void updateDeliveryPerson(
      DeliveryPersonUpdateRequest request,
      StreamObserver<DeliveryPersonResponse> responseObserver) {
    try {
      Long id = request.getId();
      DeliveryPersonRequestDto requestDto = grpcMapper.mapDeliveryPersonUpdateReqToReqDto(request);
      DeliveryPersonResponseDto user = userService.updateDeliveryPerson(id, requestDto);
      DeliveryPersonResponse response = grpcMapper.mapDeliveryPersonResponseDtoToResponse(user);
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Delete Account ─────────────────────────────────────────────
  @Override
  public void deleteAccount(DeleteAccountRequest request, StreamObserver<Empty> responseObserver) {
    try {
      Long userId = request.getId();
      userService.deleteUser(userId);
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Search Users ─────────────────────────────────────────────
  @Override
  public void searchUsers(
      SearchUsersRequest request, StreamObserver<SearchUsersResponse> responseObserver) {
    try {
      String name = request.getName();
      String userType = request.getUserType();
      String orderNumber = request.getOrderNumber();
      String deliveryNumber = request.getDeliveryNumber();

      List<UserResponseDto> users =
          userService.searchBy(name, userType, orderNumber, deliveryNumber);

      SearchUsersResponse.Builder responseBuilder = SearchUsersResponse.newBuilder();
      for (UserResponseDto u : users) {
        responseBuilder.addUsers(grpcMapper.mapUserResponseDtoToResponse(u));
      }

      responseObserver.onNext(responseBuilder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Get All Menus ───────────────────────────────────────────────
  @Override
  public void getAllMenus(Empty request, StreamObserver<MenusResponse> responseObserver) {
    try {
      java.util.List<pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto> menus =
          menuService.searchBy("", "", "");
      MenusResponse.Builder builder = MenusResponse.newBuilder();
      for (pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto dto : menus) {
        builder.addMenus(
            MenuItemMessage.newBuilder()
                .setId(dto.id())
                .setName(dto.name())
                .setCuisine(dto.cuisine())
                .setDescription(dto.description())
                .addAllProductIds(dto.productIds())
                .build());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Assign Menu ─────────────────────────────────────────────────
  @Override
  public void assignMenu(AssignMenuRequest request, StreamObserver<Empty> responseObserver) {
    try {
      restaurantService.assignMenu(request.getRestaurantId(), request.getMenuId());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Change Restaurant Status ─────────────────────────────────────
  @Override
  public void changeRestaurantStatus(
      ChangeStatusRequest request, StreamObserver<Empty> responseObserver) {
    try {
      restaurantService.changeStatus(request.getRestaurantId(), request.getStatus());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Create Restaurant ───────────────────────────────────────────
  @Override
  public void createRestaurant(
      CreateRestaurantRequest request, StreamObserver<RestaurantItem> responseObserver) {
    try {
      RestaurantRequestDto requestDto = grpcMapper.mapCreateRestaurantRequestToDto(request);
      RestaurantResponseDto result = restaurantService.createRestaurant(requestDto);
      RestaurantItem item =
          RestaurantItem.newBuilder()
              .setId(result.id())
              .setDesignation(result.designation())
              .setStatus(result.status())
              .setNif(result.nif())
              .setStreet(result.street())
              .setCity(result.city())
              .setPostalCode(result.postalCode())
              .setOpenHours(result.openHours())
              .setCloseHours(result.closeHours())
              .build();
      responseObserver.onNext(item);
      responseObserver.onCompleted();
    } catch (pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Get Restaurant Products ─────────────────────────────────────
  @Override
  public void getRestaurantProducts(
      GetRestaurantProductsRequest request, StreamObserver<ProductsResponse> responseObserver) {
    try {
      List<ProductItemResponseDto> results =
          restaurantService.searchProductBy(
              request.getRestaurantId(),
              request.getPrice(),
              request.getDesignation(),
              request.getDescription(),
              request.getAvailability(),
              request.getTimesOrdered());

      ProductsResponse.Builder builder = ProductsResponse.newBuilder();
      for (ProductItemResponseDto dto : results) {
        builder.addProducts(
            ProductItemMessage.newBuilder()
                .setId(dto.id())
                .setDesignation(dto.designation())
                .setDescription(dto.description())
                .setStatus(dto.availability())
                .setPrice(dto.price())
                .build());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Update Product Status ────────────────────────────────────────
  @Override
  public void updateProductStatus(
      UpdateProductStatusRequest request, StreamObserver<Empty> responseObserver) {
    try {
      restaurantService.updateProductStatus(
          request.getRestaurantId(),
          request.getProductId(),
          ProductStatus.valueOf(request.getStatus().toUpperCase()));
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Search Admin Restaurants ────────────────────────────────────
  @Override
  public void searchAdminRestaurants(
      AdminRestaurantsRequest request, StreamObserver<AdminRestaurantsResponse> responseObserver) {
    try {
      List<RestaurantResponseDto> results =
          restaurantService.searchByWithAdminId(
              request.getAdminId(),
              request.getName(),
              request.getCity(),
              request.getStreet(),
              request.getPostalCode(),
              request.getCuisine(),
              request.getMinOrders(),
              request.getMinReviews(),
              request.getActiveAt(),
              request.getMaxAvgPrice());

      AdminRestaurantsResponse.Builder responseBuilder = AdminRestaurantsResponse.newBuilder();
      for (RestaurantResponseDto dto : results) {
        RestaurantItem item =
            RestaurantItem.newBuilder()
                .setId(dto.id())
                .setDesignation(dto.designation())
                .setStatus(dto.status())
                .setNif(dto.nif())
                .setStreet(dto.street())
                .setCity(dto.city())
                .setPostalCode(dto.postalCode())
                .setOpenHours(dto.openHours())
                .setCloseHours(dto.closeHours())
                .build();
        responseBuilder.addRestaurants(item);
      }

      responseObserver.onNext(responseBuilder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  @Override
  public void searchRestaurants(
      SearchRestaurantsRequest request, StreamObserver<RestaurantsResponse> responseObserver) {
    try {
      List<RestaurantResponseDto> results =
          restaurantService.searchBy(
              request.getName(),
              request.getCity(),
              request.getStreet(),
              request.getPostalCode(),
              request.getCuisine(),
              request.getMinOrders(),
              request.getMinReviews(),
              request.getActiveAt(),
              request.getMaxAvgPrice());

      RestaurantsResponse.Builder builder = RestaurantsResponse.newBuilder();
      for (RestaurantResponseDto dto : results) {
        RestaurantItem item =
            RestaurantItem.newBuilder()
                .setId(dto.id())
                .setDesignation(dto.designation())
                .setStatus(dto.status())
                .setNif(dto.nif())
                .setStreet(dto.street())
                .setCity(dto.city())
                .setPostalCode(dto.postalCode())
                .setOpenHours(dto.openHours())
                .setCloseHours(dto.closeHours())
                .build();
        builder.addRestaurants(item);
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Get All Orders (delegates to searchByStatus) ────────────────
  @Override
  public void getAllOrders(Empty request, StreamObserver<OrdersResponse> responseObserver) {
    try {
      List<OrderResponseDto> orders = orderService.searchByStatus(null);
      OrdersResponse.Builder builder = OrdersResponse.newBuilder();
      for (OrderResponseDto dto : orders) {
        builder.addOrders(buildOrderResponse(dto));
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Search Orders ────────────────────────────────────────────────
  @Override
  public void searchOrders(
      SearchOrdersRequest request, StreamObserver<OrdersResponse> responseObserver) {
    try {
      String status = request.getStatus().isEmpty() ? null : request.getStatus();
      List<OrderResponseDto> orders = orderService.searchByStatus(status);
      OrdersResponse.Builder builder = OrdersResponse.newBuilder();
      for (OrderResponseDto dto : orders) {
        builder.addOrders(buildOrderResponse(dto));
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Process Payment ─────────────────────────────────────────────
  @Override
  public void processPayment(
      PaymentRequest request, StreamObserver<OrderResponse> responseObserver) {
    try {
      PaymentRequestDto dto = new PaymentRequestDto();
      dto.setOrderId(request.getOrderId());
      dto.setPaymentMethod(request.getPaymentMethod());
      dto.setAmount(request.getAmount());
      dto.setPaymentInfo(request.getPaymentInfo().isEmpty() ? null : request.getPaymentInfo());
      OrderResponseDto order = orderService.processPayment(dto);
      responseObserver.onNext(buildOrderResponse(order));
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Update Order Status ─────────────────────────────────────
  @Override
  public void updateOrderStatus(
      UpdateOrderStatusRequest request, StreamObserver<OrderResponse> responseObserver) {
    try {
      OrderResponseDto order =
          orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
      responseObserver.onNext(buildOrderResponse(order));
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Helper: build OrderResponse proto from DTO ───────────────────
  private OrderResponse buildOrderResponse(OrderResponseDto dto) {
    OrderResponse.Builder builder =
        OrderResponse.newBuilder()
            .setId(dto.id())
            .setStatus(dto.status().name())
            .setTotalValue(dto.totalValue())
            .setClientId(dto.clientId())
            .setClientName(dto.clientName())
            .setClientNif(dto.clientNif() != null ? dto.clientNif() : "")
            .setCity(dto.city())
            .setStreet(dto.street())
            .setPostalCode(dto.postalCode())
            .setCreatedAt(dto.timestamp() != null ? dto.timestamp().toString() : "")
            .setDeliveryPersonName(dto.deliveryPersonName() != null ? dto.deliveryPersonName() : "")
            .setDeliveryPersonNif(dto.deliveryPersonNif() != null ? dto.deliveryPersonNif() : "")
            .setPaymentAmount(dto.paymentAmount() != null ? dto.paymentAmount() : 0.0)
            .setPaymentAt(dto.paymentTimestamp() != null ? dto.paymentTimestamp().toString() : "")
            .setPaymentChange(dto.paymentChange() != null ? dto.paymentChange() : 0.0);
    if (dto.products() != null) {
      for (OrderProductItemDto p : dto.products()) {
        builder.addProducts(
            OrderProductItem.newBuilder()
                .setId(p.id())
                .setDesignation(p.designation())
                .setPrice(p.price())
                .build());
      }
    }
    return builder.build();
  }

  // ─── Cancel Order ─────────────────────────────────────────────
  @Override
  public void cancelOrder(OrderIdRequest request, StreamObserver<Empty> responseObserver) {
    try {
      boolean cancelled = orderService.cancelOrder(request.getOrderId());
      if (!cancelled) {
        responseObserver.onError(
            Status.FAILED_PRECONDITION
                .withDescription("Order cannot be cancelled")
                .asRuntimeException());
        return;
      }
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Search Menus ─────────────────────────────────────────────
  @Override
  public void searchMenus(
      SearchMenusRequest request, StreamObserver<MenusSearchResponse> responseObserver) {
    try {
      List<MenuResponseDto> menus =
          menuService.searchBy(request.getName(), request.getProductNumber(), request.getAverage());

      MenusSearchResponse.Builder builder = MenusSearchResponse.newBuilder();
      for (MenuResponseDto dto : menus) {
        MenuSearchItem item =
            MenuSearchItem.newBuilder()
                .setId(dto.id())
                .setName(dto.name())
                .setCuisine(dto.cuisine())
                .setDescription(dto.description())
                .setProductCount(dto.productIds().size())
                .build();
        builder.addMenus(item);
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Search Products ─────────────────────────────────────────
  @Override
  public void searchProducts(
      SearchProductsRequest request, StreamObserver<ProductsSearchResponse> responseObserver) {
    try {
      List<ProductItemResponseDto> products =
          restaurantService.searchProductBy(
              null,
              request.getPrice().isEmpty() ? null : request.getPrice(),
              request.getDesignation().isEmpty() ? null : request.getDesignation(),
              request.getDescription().isEmpty() ? null : request.getDescription(),
              request.getAvailability().isEmpty() ? null : request.getAvailability(),
              request.getTimesOrdered().isEmpty() ? null : request.getTimesOrdered());

      ProductsSearchResponse.Builder builder = ProductsSearchResponse.newBuilder();
      for (ProductItemResponseDto dto : products) {
        ProductSearchItem item =
            ProductSearchItem.newBuilder()
                .setId(dto.id())
                .setDesignation(dto.designation())
                .setPrice(dto.price())
                .setDescription(dto.description())
                .setAvailability(dto.availability())
                .build();
        builder.addProducts(item);
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Get Menu Products ────────────────────────────────────────────
  @Override
  public void getMenuProducts(
      MenuIdRequest request, StreamObserver<MenuProductsResponse> responseObserver) {
    try {
      List<pt.ul.fc.css.tascaeats.dto.product.ProductResponseDto> products =
          menuService.searchProductBy(request.getMenuId(), null, null);

      MenuProductsResponse.Builder builder = MenuProductsResponse.newBuilder();
      for (var dto : products) {
        builder.addProducts(
            ProductResponse.newBuilder()
                .setId(dto.id())
                .setDesignation(dto.designation())
                .setDescription(dto.description())
                .setPrice(dto.price())
                .build());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Add Product ──────────────────────────────────────────────────
  @Override
  public void addProduct(
      AddProductRequest request, StreamObserver<ProductResponse> responseObserver) {
    try {
      ProductRequestDto dto = new ProductRequestDto();
      dto.setDesignation(request.getDesignation());
      dto.setDescription(request.getDescription());
      dto.setPrice(request.getPrice());

      var result = menuService.addProduct(request.getMenuId(), dto);

      responseObserver.onNext(
          ProductResponse.newBuilder()
              .setId(result.id())
              .setDesignation(result.designation())
              .setDescription(result.description())
              .setPrice(result.price())
              .build());
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Update Product ───────────────────────────────────────────────
  @Override
  public void updateProduct(
      UpdateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
    try {
      ProductRequestDto dto = new ProductRequestDto();
      dto.setDesignation(request.getDesignation());
      dto.setDescription(request.getDescription());
      dto.setPrice(request.getPrice());

      var result = menuService.updateProduct(request.getMenuId(), request.getProductId(), dto);

      responseObserver.onNext(
          ProductResponse.newBuilder()
              .setId(result.id())
              .setDesignation(result.designation())
              .setDescription(result.description())
              .setPrice(result.price())
              .build());
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Delete Product ───────────────────────────────────────────────
  @Override
  public void deleteProduct(DeleteProductRequest request, StreamObserver<Empty> responseObserver) {
    try {
      boolean deleted = menuService.deleteProduct(request.getMenuId(), request.getProductId());
      if (!deleted) {
        responseObserver.onError(
            Status.FAILED_PRECONDITION
                .withDescription("Cannot delete product")
                .asRuntimeException());
        return;
      }
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Create Menu ──────────────────────────────────────────────────
  @Override
  public void createMenu(
      CreateMenuRequest request, StreamObserver<MenuItemMessage> responseObserver) {
    try {
      MenuRequestDto dto = new MenuRequestDto();
      dto.setName(request.getName());
      dto.setCuisine(request.getCuisine());
      dto.setDescription(request.getDescription());

      MenuResponseDto result = menuService.createMenu(dto);

      responseObserver.onNext(
          MenuItemMessage.newBuilder()
              .setId(result.id())
              .setName(result.name())
              .setCuisine(result.cuisine())
              .setDescription(result.description())
              .build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Update Menu ──────────────────────────────────────────────────
  @Override
  public void updateMenu(
      UpdateMenuRequest request, StreamObserver<MenuItemMessage> responseObserver) {
    try {
      MenuRequestDto dto = new MenuRequestDto();
      dto.setName(request.getName());
      dto.setCuisine(request.getCuisine());
      dto.setDescription(request.getDescription());

      MenuResponseDto result = menuService.updateMenu(request.getMenuId(), dto);

      responseObserver.onNext(
          MenuItemMessage.newBuilder()
              .setId(result.id())
              .setName(result.name())
              .setCuisine(result.cuisine())
              .setDescription(result.description())
              .build());
      responseObserver.onCompleted();
    } catch (ResourceNotFoundException e) {
      responseObserver.onError(
          Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  // ─── Delete Menu ──────────────────────────────────────────────────
  @Override
  public void deleteMenu(DeleteMenuRequest request, StreamObserver<Empty> responseObserver) {
    try {
      boolean deleted = menuService.deleteMenu(request.getMenuId());
      if (!deleted) {
        responseObserver.onError(
            Status.NOT_FOUND.withDescription("Menu not found").asRuntimeException());
        return;
      }
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(
          Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }
}
