package pt.ul.fc.css.tascaeats.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.mapper.GrpcMapper;
import pt.ul.fc.css.tascaeats.services.UserService;

@GrpcService
public class TascaEatsGrpcService extends TascaEatsServiceGrpc.TascaEatsServiceImplBase {

  @Autowired private UserService userService;

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
  public void registerAdmin(
      RegisterRequest request, StreamObserver<UserResponse> responseObserver) {
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
}
