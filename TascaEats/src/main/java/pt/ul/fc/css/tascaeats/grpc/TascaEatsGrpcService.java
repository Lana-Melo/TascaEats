package pt.ul.fc.css.tascaeats.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.mapper.GrpcMapper;
import pt.ul.fc.css.tascaeats.services.RestaurantService;
import pt.ul.fc.css.tascaeats.services.UserService;

// representa o servidor gRPC, onde implementamos os métodos definidos no .proto
@GrpcService
public class TascaEatsGrpcService extends TascaEatsServiceGrpc.TascaEatsServiceImplBase {

  @Autowired private UserService userService;

  @Autowired private RestaurantService restaurantService;

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
  @Override
  public void updateClient(UserRequest request, StreamObserver<UserResponse> responseObserver) {
    try {
      Long id = request.getId();
      UserRequestDto requestDto = grpcMapper.mapAdminRegisterReqToReqDto(request);
      UserResponseDto user = userService.updateClient(id, requestDto);
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
}
