//package com.mzone.main.controller;
//
//import com.mzone.main.grpc.service.GrpcProto.*;
//import com.mzone.main.grpc.service.HelloServiceGrpc.*;
//import io.grpc.stub.*;
//import net.devh.boot.grpc.server.service.*;
//import org.springframework.security.core.*;
//import org.springframework.security.core.context.*;
//
//import java.util.*;
//
//@GrpcService
//public class HelloGrpcController extends HelloServiceImplBase {
//
//    @Override
//    public void sayHello(HelloRequest request,
//                         StreamObserver<HelloReply> responseObserver) {
//        final Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext()
//                .getAuthentication());
//
//        responseObserver.onNext(HelloReply.newBuilder()
//                .setMessage("hello, %s. %s"
//                        .formatted(request.getName(),
//                                authentication.map(it -> "Your nickname: ".concat(it.getPrincipal().toString()))
//                                        .orElse("Your are unauthorized.")))
//                .build());
//        responseObserver.onCompleted();
//    }
//}
