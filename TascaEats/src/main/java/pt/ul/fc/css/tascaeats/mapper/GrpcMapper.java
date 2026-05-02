package pt.ul.fc.css.tascaeats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest;
import pt.ul.fc.css.tascaeats.grpc.OrderResponse;
import pt.ul.fc.css.tascaeats.grpc.UserRequest;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrpcMapper {

  UserResponse mapUserResponseDtoToResponse(UserResponseDto user);

  UserRequestDto mapAdminRegisterReqToReqDto(UserRequest request);

  DeliveryPersonRequestDto mapDeliveryPersonRegisterReqToReqDto(UserRequest request);

  DeliveryPersonRequestDto mapDeliveryPersonUpdateReqToReqDto(DeliveryPersonUpdateRequest request);

  DeliveryPersonResponse mapDeliveryPersonResponseDtoToResponse(DeliveryPersonResponseDto user);

  RestaurantRequestDto mapCreateRestaurantRequestToDto(CreateRestaurantRequest request);

  OrderResponse mapOrderResponseDtoToResponse(OrderResponseDto order);
}
