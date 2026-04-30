package pt.ul.fc.di.css.javafxtascaeats.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import pt.ul.fc.css.tascaeats.grpc.LoginRequest;
import pt.ul.fc.css.tascaeats.grpc.TascaEatsServiceGrpc;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;

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
    } catch (StatusRuntimeException e) { // servidor não conseguiu autenticar
      if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
        throw new IllegalArgumentException("Invalid credentials");
      }
      throw e;
    }
  }
}
