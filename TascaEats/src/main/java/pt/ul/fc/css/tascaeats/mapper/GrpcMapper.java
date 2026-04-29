package pt.ul.fc.css.tascaeats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrpcMapper {

  UserResponse mapUserResponseDtoToResponse(UserResponseDto user);
}
