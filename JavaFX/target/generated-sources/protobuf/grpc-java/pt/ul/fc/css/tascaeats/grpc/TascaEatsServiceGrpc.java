package pt.ul.fc.css.tascaeats.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: tascaeats.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TascaEatsServiceGrpc {

  private TascaEatsServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "tascaeats.TascaEatsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.LoginRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = pt.ul.fc.css.tascaeats.grpc.LoginRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.UserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.LoginRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.LoginRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse> getLoginMethod;
    if ((getLoginMethod = TascaEatsServiceGrpc.getLoginMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getLoginMethod = TascaEatsServiceGrpc.getLoginMethod) == null) {
          TascaEatsServiceGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.LoginRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("Login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getRegisterAdminMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterAdmin",
      requestType = pt.ul.fc.css.tascaeats.grpc.UserRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.UserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getRegisterAdminMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse> getRegisterAdminMethod;
    if ((getRegisterAdminMethod = TascaEatsServiceGrpc.getRegisterAdminMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getRegisterAdminMethod = TascaEatsServiceGrpc.getRegisterAdminMethod) == null) {
          TascaEatsServiceGrpc.getRegisterAdminMethod = getRegisterAdminMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterAdmin"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("RegisterAdmin"))
              .build();
        }
      }
    }
    return getRegisterAdminMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> getRegisterDeliveryPersonMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterDeliveryPerson",
      requestType = pt.ul.fc.css.tascaeats.grpc.UserRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> getRegisterDeliveryPersonMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> getRegisterDeliveryPersonMethod;
    if ((getRegisterDeliveryPersonMethod = TascaEatsServiceGrpc.getRegisterDeliveryPersonMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getRegisterDeliveryPersonMethod = TascaEatsServiceGrpc.getRegisterDeliveryPersonMethod) == null) {
          TascaEatsServiceGrpc.getRegisterDeliveryPersonMethod = getRegisterDeliveryPersonMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterDeliveryPerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("RegisterDeliveryPerson"))
              .build();
        }
      }
    }
    return getRegisterDeliveryPersonMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteAccount",
      requestType = pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteAccountMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteAccountMethod;
    if ((getDeleteAccountMethod = TascaEatsServiceGrpc.getDeleteAccountMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getDeleteAccountMethod = TascaEatsServiceGrpc.getDeleteAccountMethod) == null) {
          TascaEatsServiceGrpc.getDeleteAccountMethod = getDeleteAccountMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("DeleteAccount"))
              .build();
        }
      }
    }
    return getDeleteAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getUpdateAdminMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateAdmin",
      requestType = pt.ul.fc.css.tascaeats.grpc.UserRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.UserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getUpdateAdminMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse> getUpdateAdminMethod;
    if ((getUpdateAdminMethod = TascaEatsServiceGrpc.getUpdateAdminMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateAdminMethod = TascaEatsServiceGrpc.getUpdateAdminMethod) == null) {
          TascaEatsServiceGrpc.getUpdateAdminMethod = getUpdateAdminMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateAdmin"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateAdmin"))
              .build();
        }
      }
    }
    return getUpdateAdminMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getUpdateClientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateClient",
      requestType = pt.ul.fc.css.tascaeats.grpc.UserRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.UserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest,
      pt.ul.fc.css.tascaeats.grpc.UserResponse> getUpdateClientMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse> getUpdateClientMethod;
    if ((getUpdateClientMethod = TascaEatsServiceGrpc.getUpdateClientMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateClientMethod = TascaEatsServiceGrpc.getUpdateClientMethod) == null) {
          TascaEatsServiceGrpc.getUpdateClientMethod = getUpdateClientMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UserRequest, pt.ul.fc.css.tascaeats.grpc.UserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateClient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateClient"))
              .build();
        }
      }
    }
    return getUpdateClientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest,
      pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> getUpdateDeliveryPersonMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateDeliveryPerson",
      requestType = pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest,
      pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> getUpdateDeliveryPersonMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest, pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> getUpdateDeliveryPersonMethod;
    if ((getUpdateDeliveryPersonMethod = TascaEatsServiceGrpc.getUpdateDeliveryPersonMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateDeliveryPersonMethod = TascaEatsServiceGrpc.getUpdateDeliveryPersonMethod) == null) {
          TascaEatsServiceGrpc.getUpdateDeliveryPersonMethod = getUpdateDeliveryPersonMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest, pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateDeliveryPerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateDeliveryPerson"))
              .build();
        }
      }
    }
    return getUpdateDeliveryPersonMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest,
      pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse> getSearchUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchUsers",
      requestType = pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest,
      pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse> getSearchUsersMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest, pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse> getSearchUsersMethod;
    if ((getSearchUsersMethod = TascaEatsServiceGrpc.getSearchUsersMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getSearchUsersMethod = TascaEatsServiceGrpc.getSearchUsersMethod) == null) {
          TascaEatsServiceGrpc.getSearchUsersMethod = getSearchUsersMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest, pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("SearchUsers"))
              .build();
        }
      }
    }
    return getSearchUsersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest,
      pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse> getSearchRestaurantsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchRestaurants",
      requestType = pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest,
      pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse> getSearchRestaurantsMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest, pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse> getSearchRestaurantsMethod;
    if ((getSearchRestaurantsMethod = TascaEatsServiceGrpc.getSearchRestaurantsMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getSearchRestaurantsMethod = TascaEatsServiceGrpc.getSearchRestaurantsMethod) == null) {
          TascaEatsServiceGrpc.getSearchRestaurantsMethod = getSearchRestaurantsMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest, pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchRestaurants"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("SearchRestaurants"))
              .build();
        }
      }
    }
    return getSearchRestaurantsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest,
      pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse> getSearchAdminRestaurantsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchAdminRestaurants",
      requestType = pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest,
      pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse> getSearchAdminRestaurantsMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest, pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse> getSearchAdminRestaurantsMethod;
    if ((getSearchAdminRestaurantsMethod = TascaEatsServiceGrpc.getSearchAdminRestaurantsMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getSearchAdminRestaurantsMethod = TascaEatsServiceGrpc.getSearchAdminRestaurantsMethod) == null) {
          TascaEatsServiceGrpc.getSearchAdminRestaurantsMethod = getSearchAdminRestaurantsMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest, pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchAdminRestaurants"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("SearchAdminRestaurants"))
              .build();
        }
      }
    }
    return getSearchAdminRestaurantsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest,
      pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse> getSearchMenusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchMenus",
      requestType = pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest,
      pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse> getSearchMenusMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest, pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse> getSearchMenusMethod;
    if ((getSearchMenusMethod = TascaEatsServiceGrpc.getSearchMenusMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getSearchMenusMethod = TascaEatsServiceGrpc.getSearchMenusMethod) == null) {
          TascaEatsServiceGrpc.getSearchMenusMethod = getSearchMenusMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest, pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchMenus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("SearchMenus"))
              .build();
        }
      }
    }
    return getSearchMenusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse> getSearchProductsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchProducts",
      requestType = pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse> getSearchProductsMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest, pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse> getSearchProductsMethod;
    if ((getSearchProductsMethod = TascaEatsServiceGrpc.getSearchProductsMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getSearchProductsMethod = TascaEatsServiceGrpc.getSearchProductsMethod) == null) {
          TascaEatsServiceGrpc.getSearchProductsMethod = getSearchProductsMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest, pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchProducts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("SearchProducts"))
              .build();
        }
      }
    }
    return getSearchProductsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.Empty,
      pt.ul.fc.css.tascaeats.grpc.MenusResponse> getGetAllMenusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllMenus",
      requestType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.MenusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.Empty,
      pt.ul.fc.css.tascaeats.grpc.MenusResponse> getGetAllMenusMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.Empty, pt.ul.fc.css.tascaeats.grpc.MenusResponse> getGetAllMenusMethod;
    if ((getGetAllMenusMethod = TascaEatsServiceGrpc.getGetAllMenusMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getGetAllMenusMethod = TascaEatsServiceGrpc.getGetAllMenusMethod) == null) {
          TascaEatsServiceGrpc.getGetAllMenusMethod = getGetAllMenusMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.Empty, pt.ul.fc.css.tascaeats.grpc.MenusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllMenus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.MenusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("GetAllMenus"))
              .build();
        }
      }
    }
    return getGetAllMenusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getAssignMenuMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AssignMenu",
      requestType = pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getAssignMenuMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getAssignMenuMethod;
    if ((getAssignMenuMethod = TascaEatsServiceGrpc.getAssignMenuMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getAssignMenuMethod = TascaEatsServiceGrpc.getAssignMenuMethod) == null) {
          TascaEatsServiceGrpc.getAssignMenuMethod = getAssignMenuMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AssignMenu"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("AssignMenu"))
              .build();
        }
      }
    }
    return getAssignMenuMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getChangeRestaurantStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ChangeRestaurantStatus",
      requestType = pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getChangeRestaurantStatusMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getChangeRestaurantStatusMethod;
    if ((getChangeRestaurantStatusMethod = TascaEatsServiceGrpc.getChangeRestaurantStatusMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getChangeRestaurantStatusMethod = TascaEatsServiceGrpc.getChangeRestaurantStatusMethod) == null) {
          TascaEatsServiceGrpc.getChangeRestaurantStatusMethod = getChangeRestaurantStatusMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ChangeRestaurantStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("ChangeRestaurantStatus"))
              .build();
        }
      }
    }
    return getChangeRestaurantStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest,
      pt.ul.fc.css.tascaeats.grpc.RestaurantItem> getCreateRestaurantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateRestaurant",
      requestType = pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.RestaurantItem.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest,
      pt.ul.fc.css.tascaeats.grpc.RestaurantItem> getCreateRestaurantMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest, pt.ul.fc.css.tascaeats.grpc.RestaurantItem> getCreateRestaurantMethod;
    if ((getCreateRestaurantMethod = TascaEatsServiceGrpc.getCreateRestaurantMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getCreateRestaurantMethod = TascaEatsServiceGrpc.getCreateRestaurantMethod) == null) {
          TascaEatsServiceGrpc.getCreateRestaurantMethod = getCreateRestaurantMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest, pt.ul.fc.css.tascaeats.grpc.RestaurantItem>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateRestaurant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.RestaurantItem.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("CreateRestaurant"))
              .build();
        }
      }
    }
    return getCreateRestaurantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductsResponse> getGetRestaurantProductsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRestaurantProducts",
      requestType = pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.ProductsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductsResponse> getGetRestaurantProductsMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest, pt.ul.fc.css.tascaeats.grpc.ProductsResponse> getGetRestaurantProductsMethod;
    if ((getGetRestaurantProductsMethod = TascaEatsServiceGrpc.getGetRestaurantProductsMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getGetRestaurantProductsMethod = TascaEatsServiceGrpc.getGetRestaurantProductsMethod) == null) {
          TascaEatsServiceGrpc.getGetRestaurantProductsMethod = getGetRestaurantProductsMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest, pt.ul.fc.css.tascaeats.grpc.ProductsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRestaurantProducts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.ProductsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("GetRestaurantProducts"))
              .build();
        }
      }
    }
    return getGetRestaurantProductsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getUpdateProductStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateProductStatus",
      requestType = pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getUpdateProductStatusMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getUpdateProductStatusMethod;
    if ((getUpdateProductStatusMethod = TascaEatsServiceGrpc.getUpdateProductStatusMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateProductStatusMethod = TascaEatsServiceGrpc.getUpdateProductStatusMethod) == null) {
          TascaEatsServiceGrpc.getUpdateProductStatusMethod = getUpdateProductStatusMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateProductStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateProductStatus"))
              .build();
        }
      }
    }
    return getUpdateProductStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.Empty,
      pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getGetAllOrdersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllOrders",
      requestType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.OrdersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.Empty,
      pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getGetAllOrdersMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.Empty, pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getGetAllOrdersMethod;
    if ((getGetAllOrdersMethod = TascaEatsServiceGrpc.getGetAllOrdersMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getGetAllOrdersMethod = TascaEatsServiceGrpc.getGetAllOrdersMethod) == null) {
          TascaEatsServiceGrpc.getGetAllOrdersMethod = getGetAllOrdersMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.Empty, pt.ul.fc.css.tascaeats.grpc.OrdersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllOrders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.OrdersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("GetAllOrders"))
              .build();
        }
      }
    }
    return getGetAllOrdersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest,
      pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getSearchOrdersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchOrders",
      requestType = pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.OrdersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest,
      pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getSearchOrdersMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest, pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getSearchOrdersMethod;
    if ((getSearchOrdersMethod = TascaEatsServiceGrpc.getSearchOrdersMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getSearchOrdersMethod = TascaEatsServiceGrpc.getSearchOrdersMethod) == null) {
          TascaEatsServiceGrpc.getSearchOrdersMethod = getSearchOrdersMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest, pt.ul.fc.css.tascaeats.grpc.OrdersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchOrders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.OrdersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("SearchOrders"))
              .build();
        }
      }
    }
    return getSearchOrdersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.PaymentRequest,
      pt.ul.fc.css.tascaeats.grpc.OrderResponse> getProcessPaymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ProcessPayment",
      requestType = pt.ul.fc.css.tascaeats.grpc.PaymentRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.OrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.PaymentRequest,
      pt.ul.fc.css.tascaeats.grpc.OrderResponse> getProcessPaymentMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.PaymentRequest, pt.ul.fc.css.tascaeats.grpc.OrderResponse> getProcessPaymentMethod;
    if ((getProcessPaymentMethod = TascaEatsServiceGrpc.getProcessPaymentMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getProcessPaymentMethod = TascaEatsServiceGrpc.getProcessPaymentMethod) == null) {
          TascaEatsServiceGrpc.getProcessPaymentMethod = getProcessPaymentMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.PaymentRequest, pt.ul.fc.css.tascaeats.grpc.OrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ProcessPayment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.PaymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.OrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("ProcessPayment"))
              .build();
        }
      }
    }
    return getProcessPaymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest,
      pt.ul.fc.css.tascaeats.grpc.OrderResponse> getUpdateOrderStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateOrderStatus",
      requestType = pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.OrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest,
      pt.ul.fc.css.tascaeats.grpc.OrderResponse> getUpdateOrderStatusMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest, pt.ul.fc.css.tascaeats.grpc.OrderResponse> getUpdateOrderStatusMethod;
    if ((getUpdateOrderStatusMethod = TascaEatsServiceGrpc.getUpdateOrderStatusMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateOrderStatusMethod = TascaEatsServiceGrpc.getUpdateOrderStatusMethod) == null) {
          TascaEatsServiceGrpc.getUpdateOrderStatusMethod = getUpdateOrderStatusMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest, pt.ul.fc.css.tascaeats.grpc.OrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateOrderStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.OrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateOrderStatus"))
              .build();
        }
      }
    }
    return getUpdateOrderStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.OrderIdRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getCancelOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelOrder",
      requestType = pt.ul.fc.css.tascaeats.grpc.OrderIdRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.OrderIdRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getCancelOrderMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.OrderIdRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getCancelOrderMethod;
    if ((getCancelOrderMethod = TascaEatsServiceGrpc.getCancelOrderMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getCancelOrderMethod = TascaEatsServiceGrpc.getCancelOrderMethod) == null) {
          TascaEatsServiceGrpc.getCancelOrderMethod = getCancelOrderMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.OrderIdRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.OrderIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("CancelOrder"))
              .build();
        }
      }
    }
    return getCancelOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.MenuIdRequest,
      pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse> getGetMenuProductsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMenuProducts",
      requestType = pt.ul.fc.css.tascaeats.grpc.MenuIdRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.MenuIdRequest,
      pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse> getGetMenuProductsMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.MenuIdRequest, pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse> getGetMenuProductsMethod;
    if ((getGetMenuProductsMethod = TascaEatsServiceGrpc.getGetMenuProductsMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getGetMenuProductsMethod = TascaEatsServiceGrpc.getGetMenuProductsMethod) == null) {
          TascaEatsServiceGrpc.getGetMenuProductsMethod = getGetMenuProductsMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.MenuIdRequest, pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMenuProducts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.MenuIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("GetMenuProducts"))
              .build();
        }
      }
    }
    return getGetMenuProductsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AddProductRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductResponse> getAddProductMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddProduct",
      requestType = pt.ul.fc.css.tascaeats.grpc.AddProductRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.ProductResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AddProductRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductResponse> getAddProductMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.AddProductRequest, pt.ul.fc.css.tascaeats.grpc.ProductResponse> getAddProductMethod;
    if ((getAddProductMethod = TascaEatsServiceGrpc.getAddProductMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getAddProductMethod = TascaEatsServiceGrpc.getAddProductMethod) == null) {
          TascaEatsServiceGrpc.getAddProductMethod = getAddProductMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.AddProductRequest, pt.ul.fc.css.tascaeats.grpc.ProductResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddProduct"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.AddProductRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.ProductResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("AddProduct"))
              .build();
        }
      }
    }
    return getAddProductMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductResponse> getUpdateProductMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateProduct",
      requestType = pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.ProductResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest,
      pt.ul.fc.css.tascaeats.grpc.ProductResponse> getUpdateProductMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest, pt.ul.fc.css.tascaeats.grpc.ProductResponse> getUpdateProductMethod;
    if ((getUpdateProductMethod = TascaEatsServiceGrpc.getUpdateProductMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateProductMethod = TascaEatsServiceGrpc.getUpdateProductMethod) == null) {
          TascaEatsServiceGrpc.getUpdateProductMethod = getUpdateProductMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest, pt.ul.fc.css.tascaeats.grpc.ProductResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateProduct"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.ProductResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateProduct"))
              .build();
        }
      }
    }
    return getUpdateProductMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteProductMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteProduct",
      requestType = pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteProductMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteProductMethod;
    if ((getDeleteProductMethod = TascaEatsServiceGrpc.getDeleteProductMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getDeleteProductMethod = TascaEatsServiceGrpc.getDeleteProductMethod) == null) {
          TascaEatsServiceGrpc.getDeleteProductMethod = getDeleteProductMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteProduct"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("DeleteProduct"))
              .build();
        }
      }
    }
    return getDeleteProductMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> getCreateMenuMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateMenu",
      requestType = pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.MenuItemMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> getCreateMenuMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest, pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> getCreateMenuMethod;
    if ((getCreateMenuMethod = TascaEatsServiceGrpc.getCreateMenuMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getCreateMenuMethod = TascaEatsServiceGrpc.getCreateMenuMethod) == null) {
          TascaEatsServiceGrpc.getCreateMenuMethod = getCreateMenuMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest, pt.ul.fc.css.tascaeats.grpc.MenuItemMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateMenu"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.MenuItemMessage.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("CreateMenu"))
              .build();
        }
      }
    }
    return getCreateMenuMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> getUpdateMenuMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateMenu",
      requestType = pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.MenuItemMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> getUpdateMenuMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest, pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> getUpdateMenuMethod;
    if ((getUpdateMenuMethod = TascaEatsServiceGrpc.getUpdateMenuMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getUpdateMenuMethod = TascaEatsServiceGrpc.getUpdateMenuMethod) == null) {
          TascaEatsServiceGrpc.getUpdateMenuMethod = getUpdateMenuMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest, pt.ul.fc.css.tascaeats.grpc.MenuItemMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateMenu"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.MenuItemMessage.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("UpdateMenu"))
              .build();
        }
      }
    }
    return getUpdateMenuMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteMenuMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteMenu",
      requestType = pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest.class,
      responseType = pt.ul.fc.css.tascaeats.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest,
      pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteMenuMethod() {
    io.grpc.MethodDescriptor<pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest, pt.ul.fc.css.tascaeats.grpc.Empty> getDeleteMenuMethod;
    if ((getDeleteMenuMethod = TascaEatsServiceGrpc.getDeleteMenuMethod) == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        if ((getDeleteMenuMethod = TascaEatsServiceGrpc.getDeleteMenuMethod) == null) {
          TascaEatsServiceGrpc.getDeleteMenuMethod = getDeleteMenuMethod =
              io.grpc.MethodDescriptor.<pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest, pt.ul.fc.css.tascaeats.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteMenu"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ul.fc.css.tascaeats.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new TascaEatsServiceMethodDescriptorSupplier("DeleteMenu"))
              .build();
        }
      }
    }
    return getDeleteMenuMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TascaEatsServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TascaEatsServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TascaEatsServiceStub>() {
        @java.lang.Override
        public TascaEatsServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TascaEatsServiceStub(channel, callOptions);
        }
      };
    return TascaEatsServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TascaEatsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TascaEatsServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TascaEatsServiceBlockingStub>() {
        @java.lang.Override
        public TascaEatsServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TascaEatsServiceBlockingStub(channel, callOptions);
        }
      };
    return TascaEatsServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TascaEatsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TascaEatsServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TascaEatsServiceFutureStub>() {
        @java.lang.Override
        public TascaEatsServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TascaEatsServiceFutureStub(channel, callOptions);
        }
      };
    return TascaEatsServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void login(pt.ul.fc.css.tascaeats.grpc.LoginRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    default void registerAdmin(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterAdminMethod(), responseObserver);
    }

    /**
     */
    default void registerDeliveryPerson(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterDeliveryPersonMethod(), responseObserver);
    }

    /**
     */
    default void deleteAccount(pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteAccountMethod(), responseObserver);
    }

    /**
     */
    default void updateAdmin(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateAdminMethod(), responseObserver);
    }

    /**
     */
    default void updateClient(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateClientMethod(), responseObserver);
    }

    /**
     */
    default void updateDeliveryPerson(pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateDeliveryPersonMethod(), responseObserver);
    }

    /**
     */
    default void searchUsers(pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchUsersMethod(), responseObserver);
    }

    /**
     */
    default void searchRestaurants(pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchRestaurantsMethod(), responseObserver);
    }

    /**
     */
    default void searchAdminRestaurants(pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchAdminRestaurantsMethod(), responseObserver);
    }

    /**
     */
    default void searchMenus(pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchMenusMethod(), responseObserver);
    }

    /**
     */
    default void searchProducts(pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchProductsMethod(), responseObserver);
    }

    /**
     */
    default void getAllMenus(pt.ul.fc.css.tascaeats.grpc.Empty request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllMenusMethod(), responseObserver);
    }

    /**
     */
    default void assignMenu(pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAssignMenuMethod(), responseObserver);
    }

    /**
     */
    default void changeRestaurantStatus(pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getChangeRestaurantStatusMethod(), responseObserver);
    }

    /**
     */
    default void createRestaurant(pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.RestaurantItem> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateRestaurantMethod(), responseObserver);
    }

    /**
     */
    default void getRestaurantProducts(pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRestaurantProductsMethod(), responseObserver);
    }

    /**
     */
    default void updateProductStatus(pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateProductStatusMethod(), responseObserver);
    }

    /**
     */
    default void getAllOrders(pt.ul.fc.css.tascaeats.grpc.Empty request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrdersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllOrdersMethod(), responseObserver);
    }

    /**
     */
    default void searchOrders(pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrdersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchOrdersMethod(), responseObserver);
    }

    /**
     */
    default void processPayment(pt.ul.fc.css.tascaeats.grpc.PaymentRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrderResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProcessPaymentMethod(), responseObserver);
    }

    /**
     */
    default void updateOrderStatus(pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrderResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateOrderStatusMethod(), responseObserver);
    }

    /**
     */
    default void cancelOrder(pt.ul.fc.css.tascaeats.grpc.OrderIdRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelOrderMethod(), responseObserver);
    }

    /**
     */
    default void getMenuProducts(pt.ul.fc.css.tascaeats.grpc.MenuIdRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMenuProductsMethod(), responseObserver);
    }

    /**
     */
    default void addProduct(pt.ul.fc.css.tascaeats.grpc.AddProductRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddProductMethod(), responseObserver);
    }

    /**
     */
    default void updateProduct(pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateProductMethod(), responseObserver);
    }

    /**
     */
    default void deleteProduct(pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteProductMethod(), responseObserver);
    }

    /**
     */
    default void createMenu(pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateMenuMethod(), responseObserver);
    }

    /**
     */
    default void updateMenu(pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateMenuMethod(), responseObserver);
    }

    /**
     */
    default void deleteMenu(pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMenuMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TascaEatsService.
   */
  public static abstract class TascaEatsServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TascaEatsServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TascaEatsService.
   */
  public static final class TascaEatsServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TascaEatsServiceStub> {
    private TascaEatsServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TascaEatsServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TascaEatsServiceStub(channel, callOptions);
    }

    /**
     */
    public void login(pt.ul.fc.css.tascaeats.grpc.LoginRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerAdmin(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterAdminMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerDeliveryPerson(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterDeliveryPersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteAccount(pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateAdmin(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateAdminMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateClient(pt.ul.fc.css.tascaeats.grpc.UserRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateClientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateDeliveryPerson(pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateDeliveryPersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchUsers(pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchUsersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchRestaurants(pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchRestaurantsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchAdminRestaurants(pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchAdminRestaurantsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchMenus(pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchMenusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchProducts(pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchProductsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllMenus(pt.ul.fc.css.tascaeats.grpc.Empty request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllMenusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void assignMenu(pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAssignMenuMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changeRestaurantStatus(pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getChangeRestaurantStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createRestaurant(pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.RestaurantItem> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateRestaurantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRestaurantProducts(pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRestaurantProductsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateProductStatus(pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateProductStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllOrders(pt.ul.fc.css.tascaeats.grpc.Empty request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrdersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllOrdersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchOrders(pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrdersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchOrdersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void processPayment(pt.ul.fc.css.tascaeats.grpc.PaymentRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrderResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProcessPaymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateOrderStatus(pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrderResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateOrderStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelOrder(pt.ul.fc.css.tascaeats.grpc.OrderIdRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMenuProducts(pt.ul.fc.css.tascaeats.grpc.MenuIdRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMenuProductsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addProduct(pt.ul.fc.css.tascaeats.grpc.AddProductRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddProductMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateProduct(pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateProductMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteProduct(pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteProductMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createMenu(pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateMenuMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateMenu(pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateMenuMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteMenu(pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest request,
        io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMenuMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TascaEatsService.
   */
  public static final class TascaEatsServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TascaEatsServiceBlockingStub> {
    private TascaEatsServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TascaEatsServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TascaEatsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.UserResponse login(pt.ul.fc.css.tascaeats.grpc.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.UserResponse registerAdmin(pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterAdminMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse registerDeliveryPerson(pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterDeliveryPersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty deleteAccount(pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.UserResponse updateAdmin(pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateAdminMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.UserResponse updateClient(pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateClientMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse updateDeliveryPerson(pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateDeliveryPersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse searchUsers(pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchUsersMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse searchRestaurants(pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchRestaurantsMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse searchAdminRestaurants(pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchAdminRestaurantsMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse searchMenus(pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchMenusMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse searchProducts(pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchProductsMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.MenusResponse getAllMenus(pt.ul.fc.css.tascaeats.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllMenusMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty assignMenu(pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAssignMenuMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty changeRestaurantStatus(pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getChangeRestaurantStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.RestaurantItem createRestaurant(pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.ProductsResponse getRestaurantProducts(pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRestaurantProductsMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty updateProductStatus(pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateProductStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.OrdersResponse getAllOrders(pt.ul.fc.css.tascaeats.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllOrdersMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.OrdersResponse searchOrders(pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchOrdersMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.OrderResponse processPayment(pt.ul.fc.css.tascaeats.grpc.PaymentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProcessPaymentMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.OrderResponse updateOrderStatus(pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateOrderStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty cancelOrder(pt.ul.fc.css.tascaeats.grpc.OrderIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse getMenuProducts(pt.ul.fc.css.tascaeats.grpc.MenuIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMenuProductsMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.ProductResponse addProduct(pt.ul.fc.css.tascaeats.grpc.AddProductRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddProductMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.ProductResponse updateProduct(pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateProductMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty deleteProduct(pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteProductMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.MenuItemMessage createMenu(pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateMenuMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.MenuItemMessage updateMenu(pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMenuMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ul.fc.css.tascaeats.grpc.Empty deleteMenu(pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMenuMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TascaEatsService.
   */
  public static final class TascaEatsServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TascaEatsServiceFutureStub> {
    private TascaEatsServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TascaEatsServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TascaEatsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.UserResponse> login(
        pt.ul.fc.css.tascaeats.grpc.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.UserResponse> registerAdmin(
        pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterAdminMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> registerDeliveryPerson(
        pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterDeliveryPersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> deleteAccount(
        pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.UserResponse> updateAdmin(
        pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateAdminMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.UserResponse> updateClient(
        pt.ul.fc.css.tascaeats.grpc.UserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateClientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse> updateDeliveryPerson(
        pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateDeliveryPersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse> searchUsers(
        pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchUsersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse> searchRestaurants(
        pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchRestaurantsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse> searchAdminRestaurants(
        pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchAdminRestaurantsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse> searchMenus(
        pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchMenusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse> searchProducts(
        pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchProductsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.MenusResponse> getAllMenus(
        pt.ul.fc.css.tascaeats.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllMenusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> assignMenu(
        pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAssignMenuMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> changeRestaurantStatus(
        pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getChangeRestaurantStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.RestaurantItem> createRestaurant(
        pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateRestaurantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.ProductsResponse> getRestaurantProducts(
        pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRestaurantProductsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> updateProductStatus(
        pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateProductStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.OrdersResponse> getAllOrders(
        pt.ul.fc.css.tascaeats.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllOrdersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.OrdersResponse> searchOrders(
        pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchOrdersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.OrderResponse> processPayment(
        pt.ul.fc.css.tascaeats.grpc.PaymentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProcessPaymentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.OrderResponse> updateOrderStatus(
        pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateOrderStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> cancelOrder(
        pt.ul.fc.css.tascaeats.grpc.OrderIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelOrderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse> getMenuProducts(
        pt.ul.fc.css.tascaeats.grpc.MenuIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMenuProductsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.ProductResponse> addProduct(
        pt.ul.fc.css.tascaeats.grpc.AddProductRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddProductMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.ProductResponse> updateProduct(
        pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateProductMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> deleteProduct(
        pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteProductMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> createMenu(
        pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateMenuMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage> updateMenu(
        pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMenuMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ul.fc.css.tascaeats.grpc.Empty> deleteMenu(
        pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMenuMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_REGISTER_ADMIN = 1;
  private static final int METHODID_REGISTER_DELIVERY_PERSON = 2;
  private static final int METHODID_DELETE_ACCOUNT = 3;
  private static final int METHODID_UPDATE_ADMIN = 4;
  private static final int METHODID_UPDATE_CLIENT = 5;
  private static final int METHODID_UPDATE_DELIVERY_PERSON = 6;
  private static final int METHODID_SEARCH_USERS = 7;
  private static final int METHODID_SEARCH_RESTAURANTS = 8;
  private static final int METHODID_SEARCH_ADMIN_RESTAURANTS = 9;
  private static final int METHODID_SEARCH_MENUS = 10;
  private static final int METHODID_SEARCH_PRODUCTS = 11;
  private static final int METHODID_GET_ALL_MENUS = 12;
  private static final int METHODID_ASSIGN_MENU = 13;
  private static final int METHODID_CHANGE_RESTAURANT_STATUS = 14;
  private static final int METHODID_CREATE_RESTAURANT = 15;
  private static final int METHODID_GET_RESTAURANT_PRODUCTS = 16;
  private static final int METHODID_UPDATE_PRODUCT_STATUS = 17;
  private static final int METHODID_GET_ALL_ORDERS = 18;
  private static final int METHODID_SEARCH_ORDERS = 19;
  private static final int METHODID_PROCESS_PAYMENT = 20;
  private static final int METHODID_UPDATE_ORDER_STATUS = 21;
  private static final int METHODID_CANCEL_ORDER = 22;
  private static final int METHODID_GET_MENU_PRODUCTS = 23;
  private static final int METHODID_ADD_PRODUCT = 24;
  private static final int METHODID_UPDATE_PRODUCT = 25;
  private static final int METHODID_DELETE_PRODUCT = 26;
  private static final int METHODID_CREATE_MENU = 27;
  private static final int METHODID_UPDATE_MENU = 28;
  private static final int METHODID_DELETE_MENU = 29;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((pt.ul.fc.css.tascaeats.grpc.LoginRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_REGISTER_ADMIN:
          serviceImpl.registerAdmin((pt.ul.fc.css.tascaeats.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_REGISTER_DELIVERY_PERSON:
          serviceImpl.registerDeliveryPerson((pt.ul.fc.css.tascaeats.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse>) responseObserver);
          break;
        case METHODID_DELETE_ACCOUNT:
          serviceImpl.deleteAccount((pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        case METHODID_UPDATE_ADMIN:
          serviceImpl.updateAdmin((pt.ul.fc.css.tascaeats.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_UPDATE_CLIENT:
          serviceImpl.updateClient((pt.ul.fc.css.tascaeats.grpc.UserRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.UserResponse>) responseObserver);
          break;
        case METHODID_UPDATE_DELIVERY_PERSON:
          serviceImpl.updateDeliveryPerson((pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse>) responseObserver);
          break;
        case METHODID_SEARCH_USERS:
          serviceImpl.searchUsers((pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse>) responseObserver);
          break;
        case METHODID_SEARCH_RESTAURANTS:
          serviceImpl.searchRestaurants((pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse>) responseObserver);
          break;
        case METHODID_SEARCH_ADMIN_RESTAURANTS:
          serviceImpl.searchAdminRestaurants((pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse>) responseObserver);
          break;
        case METHODID_SEARCH_MENUS:
          serviceImpl.searchMenus((pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse>) responseObserver);
          break;
        case METHODID_SEARCH_PRODUCTS:
          serviceImpl.searchProducts((pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_MENUS:
          serviceImpl.getAllMenus((pt.ul.fc.css.tascaeats.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenusResponse>) responseObserver);
          break;
        case METHODID_ASSIGN_MENU:
          serviceImpl.assignMenu((pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        case METHODID_CHANGE_RESTAURANT_STATUS:
          serviceImpl.changeRestaurantStatus((pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        case METHODID_CREATE_RESTAURANT:
          serviceImpl.createRestaurant((pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.RestaurantItem>) responseObserver);
          break;
        case METHODID_GET_RESTAURANT_PRODUCTS:
          serviceImpl.getRestaurantProducts((pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductsResponse>) responseObserver);
          break;
        case METHODID_UPDATE_PRODUCT_STATUS:
          serviceImpl.updateProductStatus((pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        case METHODID_GET_ALL_ORDERS:
          serviceImpl.getAllOrders((pt.ul.fc.css.tascaeats.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrdersResponse>) responseObserver);
          break;
        case METHODID_SEARCH_ORDERS:
          serviceImpl.searchOrders((pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrdersResponse>) responseObserver);
          break;
        case METHODID_PROCESS_PAYMENT:
          serviceImpl.processPayment((pt.ul.fc.css.tascaeats.grpc.PaymentRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrderResponse>) responseObserver);
          break;
        case METHODID_UPDATE_ORDER_STATUS:
          serviceImpl.updateOrderStatus((pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.OrderResponse>) responseObserver);
          break;
        case METHODID_CANCEL_ORDER:
          serviceImpl.cancelOrder((pt.ul.fc.css.tascaeats.grpc.OrderIdRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        case METHODID_GET_MENU_PRODUCTS:
          serviceImpl.getMenuProducts((pt.ul.fc.css.tascaeats.grpc.MenuIdRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse>) responseObserver);
          break;
        case METHODID_ADD_PRODUCT:
          serviceImpl.addProduct((pt.ul.fc.css.tascaeats.grpc.AddProductRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductResponse>) responseObserver);
          break;
        case METHODID_UPDATE_PRODUCT:
          serviceImpl.updateProduct((pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.ProductResponse>) responseObserver);
          break;
        case METHODID_DELETE_PRODUCT:
          serviceImpl.deleteProduct((pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        case METHODID_CREATE_MENU:
          serviceImpl.createMenu((pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage>) responseObserver);
          break;
        case METHODID_UPDATE_MENU:
          serviceImpl.updateMenu((pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.MenuItemMessage>) responseObserver);
          break;
        case METHODID_DELETE_MENU:
          serviceImpl.deleteMenu((pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest) request,
              (io.grpc.stub.StreamObserver<pt.ul.fc.css.tascaeats.grpc.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.LoginRequest,
              pt.ul.fc.css.tascaeats.grpc.UserResponse>(
                service, METHODID_LOGIN)))
        .addMethod(
          getRegisterAdminMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UserRequest,
              pt.ul.fc.css.tascaeats.grpc.UserResponse>(
                service, METHODID_REGISTER_ADMIN)))
        .addMethod(
          getRegisterDeliveryPersonMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UserRequest,
              pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse>(
                service, METHODID_REGISTER_DELIVERY_PERSON)))
        .addMethod(
          getDeleteAccountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.DeleteAccountRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_DELETE_ACCOUNT)))
        .addMethod(
          getUpdateAdminMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UserRequest,
              pt.ul.fc.css.tascaeats.grpc.UserResponse>(
                service, METHODID_UPDATE_ADMIN)))
        .addMethod(
          getUpdateClientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UserRequest,
              pt.ul.fc.css.tascaeats.grpc.UserResponse>(
                service, METHODID_UPDATE_CLIENT)))
        .addMethod(
          getUpdateDeliveryPersonMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.DeliveryPersonUpdateRequest,
              pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse>(
                service, METHODID_UPDATE_DELIVERY_PERSON)))
        .addMethod(
          getSearchUsersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.SearchUsersRequest,
              pt.ul.fc.css.tascaeats.grpc.SearchUsersResponse>(
                service, METHODID_SEARCH_USERS)))
        .addMethod(
          getSearchRestaurantsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.SearchRestaurantsRequest,
              pt.ul.fc.css.tascaeats.grpc.RestaurantsResponse>(
                service, METHODID_SEARCH_RESTAURANTS)))
        .addMethod(
          getSearchAdminRestaurantsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsRequest,
              pt.ul.fc.css.tascaeats.grpc.AdminRestaurantsResponse>(
                service, METHODID_SEARCH_ADMIN_RESTAURANTS)))
        .addMethod(
          getSearchMenusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.SearchMenusRequest,
              pt.ul.fc.css.tascaeats.grpc.MenusSearchResponse>(
                service, METHODID_SEARCH_MENUS)))
        .addMethod(
          getSearchProductsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.SearchProductsRequest,
              pt.ul.fc.css.tascaeats.grpc.ProductsSearchResponse>(
                service, METHODID_SEARCH_PRODUCTS)))
        .addMethod(
          getGetAllMenusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.Empty,
              pt.ul.fc.css.tascaeats.grpc.MenusResponse>(
                service, METHODID_GET_ALL_MENUS)))
        .addMethod(
          getAssignMenuMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.AssignMenuRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_ASSIGN_MENU)))
        .addMethod(
          getChangeRestaurantStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.ChangeStatusRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_CHANGE_RESTAURANT_STATUS)))
        .addMethod(
          getCreateRestaurantMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.CreateRestaurantRequest,
              pt.ul.fc.css.tascaeats.grpc.RestaurantItem>(
                service, METHODID_CREATE_RESTAURANT)))
        .addMethod(
          getGetRestaurantProductsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.GetRestaurantProductsRequest,
              pt.ul.fc.css.tascaeats.grpc.ProductsResponse>(
                service, METHODID_GET_RESTAURANT_PRODUCTS)))
        .addMethod(
          getUpdateProductStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UpdateProductStatusRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_UPDATE_PRODUCT_STATUS)))
        .addMethod(
          getGetAllOrdersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.Empty,
              pt.ul.fc.css.tascaeats.grpc.OrdersResponse>(
                service, METHODID_GET_ALL_ORDERS)))
        .addMethod(
          getSearchOrdersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.SearchOrdersRequest,
              pt.ul.fc.css.tascaeats.grpc.OrdersResponse>(
                service, METHODID_SEARCH_ORDERS)))
        .addMethod(
          getProcessPaymentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.PaymentRequest,
              pt.ul.fc.css.tascaeats.grpc.OrderResponse>(
                service, METHODID_PROCESS_PAYMENT)))
        .addMethod(
          getUpdateOrderStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UpdateOrderStatusRequest,
              pt.ul.fc.css.tascaeats.grpc.OrderResponse>(
                service, METHODID_UPDATE_ORDER_STATUS)))
        .addMethod(
          getCancelOrderMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.OrderIdRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_CANCEL_ORDER)))
        .addMethod(
          getGetMenuProductsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.MenuIdRequest,
              pt.ul.fc.css.tascaeats.grpc.MenuProductsResponse>(
                service, METHODID_GET_MENU_PRODUCTS)))
        .addMethod(
          getAddProductMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.AddProductRequest,
              pt.ul.fc.css.tascaeats.grpc.ProductResponse>(
                service, METHODID_ADD_PRODUCT)))
        .addMethod(
          getUpdateProductMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UpdateProductRequest,
              pt.ul.fc.css.tascaeats.grpc.ProductResponse>(
                service, METHODID_UPDATE_PRODUCT)))
        .addMethod(
          getDeleteProductMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.DeleteProductRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_DELETE_PRODUCT)))
        .addMethod(
          getCreateMenuMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.CreateMenuRequest,
              pt.ul.fc.css.tascaeats.grpc.MenuItemMessage>(
                service, METHODID_CREATE_MENU)))
        .addMethod(
          getUpdateMenuMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.UpdateMenuRequest,
              pt.ul.fc.css.tascaeats.grpc.MenuItemMessage>(
                service, METHODID_UPDATE_MENU)))
        .addMethod(
          getDeleteMenuMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pt.ul.fc.css.tascaeats.grpc.DeleteMenuRequest,
              pt.ul.fc.css.tascaeats.grpc.Empty>(
                service, METHODID_DELETE_MENU)))
        .build();
  }

  private static abstract class TascaEatsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TascaEatsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return pt.ul.fc.css.tascaeats.grpc.TascaEatsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TascaEatsService");
    }
  }

  private static final class TascaEatsServiceFileDescriptorSupplier
      extends TascaEatsServiceBaseDescriptorSupplier {
    TascaEatsServiceFileDescriptorSupplier() {}
  }

  private static final class TascaEatsServiceMethodDescriptorSupplier
      extends TascaEatsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TascaEatsServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TascaEatsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TascaEatsServiceFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getRegisterAdminMethod())
              .addMethod(getRegisterDeliveryPersonMethod())
              .addMethod(getDeleteAccountMethod())
              .addMethod(getUpdateAdminMethod())
              .addMethod(getUpdateClientMethod())
              .addMethod(getUpdateDeliveryPersonMethod())
              .addMethod(getSearchUsersMethod())
              .addMethod(getSearchRestaurantsMethod())
              .addMethod(getSearchAdminRestaurantsMethod())
              .addMethod(getSearchMenusMethod())
              .addMethod(getSearchProductsMethod())
              .addMethod(getGetAllMenusMethod())
              .addMethod(getAssignMenuMethod())
              .addMethod(getChangeRestaurantStatusMethod())
              .addMethod(getCreateRestaurantMethod())
              .addMethod(getGetRestaurantProductsMethod())
              .addMethod(getUpdateProductStatusMethod())
              .addMethod(getGetAllOrdersMethod())
              .addMethod(getSearchOrdersMethod())
              .addMethod(getProcessPaymentMethod())
              .addMethod(getUpdateOrderStatusMethod())
              .addMethod(getCancelOrderMethod())
              .addMethod(getGetMenuProductsMethod())
              .addMethod(getAddProductMethod())
              .addMethod(getUpdateProductMethod())
              .addMethod(getDeleteProductMethod())
              .addMethod(getCreateMenuMethod())
              .addMethod(getUpdateMenuMethod())
              .addMethod(getDeleteMenuMethod())
              .build();
        }
      }
    }
    return result;
  }
}
