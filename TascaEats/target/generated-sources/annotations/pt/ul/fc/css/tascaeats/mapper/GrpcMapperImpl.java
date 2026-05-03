package pt.ul.fc.css.tascaeats.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest;
import pt.ul.fc.css.tascaeats.grpc.UserRequest;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-03T16:49:05+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Homebrew)")
@Component
public class GrpcMapperImpl implements GrpcMapper {

  @Override
  public UserResponse mapUserResponseDtoToResponse(UserResponseDto user) {
    if (user == null) {
      return null;
    }

    UserResponse.Builder userResponse = UserResponse.newBuilder();

    if (user.id() != null) {
      userResponse.setId(user.id());
    }
    userResponse.setUsername(user.username());
    userResponse.setName(user.name());
    userResponse.setPhoneNumber(user.phoneNumber());
    userResponse.setNif(user.nif());
    userResponse.setRole(user.role());

    return userResponse.build();
  }

  @Override
  public UserRequestDto mapAdminRegisterReqToReqDto(UserRequest request) {
    if (request == null) {
      return null;
    }

    UserRequestDto userRequestDto = new UserRequestDto();

    userRequestDto.setUsername(request.getUsername());
    userRequestDto.setPassword(request.getPassword());
    userRequestDto.setName(request.getName());
    userRequestDto.setPhoneNumber(request.getPhoneNumber());
    userRequestDto.setNif(request.getNif());

    return userRequestDto;
  }

  @Override
  public DeliveryPersonRequestDto mapDeliveryPersonRegisterReqToReqDto(UserRequest request) {
    if (request == null) {
      return null;
    }

    DeliveryPersonRequestDto deliveryPersonRequestDto = new DeliveryPersonRequestDto();

    deliveryPersonRequestDto.setUsername(request.getUsername());
    deliveryPersonRequestDto.setPassword(request.getPassword());
    deliveryPersonRequestDto.setName(request.getName());
    deliveryPersonRequestDto.setPhoneNumber(request.getPhoneNumber());
    deliveryPersonRequestDto.setNif(request.getNif());

    return deliveryPersonRequestDto;
  }

  @Override
  public DeliveryPersonRequestDto mapDeliveryPersonUpdateReqToReqDto(
      DeliveryPersonUpdateRequest request) {
    if (request == null) {
      return null;
    }

    DeliveryPersonRequestDto deliveryPersonRequestDto = new DeliveryPersonRequestDto();

    deliveryPersonRequestDto.setUsername(request.getUsername());
    deliveryPersonRequestDto.setPassword(request.getPassword());
    deliveryPersonRequestDto.setName(request.getName());
    deliveryPersonRequestDto.setPhoneNumber(request.getPhoneNumber());
    deliveryPersonRequestDto.setNif(request.getNif());
    deliveryPersonRequestDto.setStatus(request.getStatus());

    return deliveryPersonRequestDto;
  }

  @Override
  public DeliveryPersonResponse mapDeliveryPersonResponseDtoToResponse(
      DeliveryPersonResponseDto user) {
    if (user == null) {
      return null;
    }

    DeliveryPersonResponse.Builder deliveryPersonResponse = DeliveryPersonResponse.newBuilder();

    if (user.id() != null) {
      deliveryPersonResponse.setId(user.id());
    }
    deliveryPersonResponse.setUsername(user.username());
    deliveryPersonResponse.setName(user.name());
    deliveryPersonResponse.setPhoneNumber(user.phoneNumber());
    deliveryPersonResponse.setNif(user.nif());
    deliveryPersonResponse.setStatus(user.status());

    return deliveryPersonResponse.build();
  }

  @Override
  public RestaurantRequestDto mapCreateRestaurantRequestToDto(CreateRestaurantRequest request) {
    if (request == null) {
      return null;
    }

    RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto();

    restaurantRequestDto.setDesignation(request.getDesignation());
    restaurantRequestDto.setNif(request.getNif());
    restaurantRequestDto.setCity(request.getCity());
    restaurantRequestDto.setPostalCode(request.getPostalCode());
    restaurantRequestDto.setStreet(request.getStreet());
    restaurantRequestDto.setAdminId(request.getAdminId());
    restaurantRequestDto.setOpenHours(request.getOpenHours());
    restaurantRequestDto.setCloseHours(request.getCloseHours());

    return restaurantRequestDto;
  }
}
