package pt.ul.fc.css.tascaeats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.grpc.RegisterRequest;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrpcMapper {

  UserResponse mapUserResponseDtoToResponse(UserResponseDto user);

  UserRequestDto mapAdminRegisterReqToReqDto(RegisterRequest request);
}
