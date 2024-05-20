//package com.mzone.main;
//
//import com.mzone.main.entity.*;
//import com.mzone.main.grpc.service.GrpcProto.*;
//import com.mzone.main.grpc.service.HelloServiceGrpc.*;
//import com.mzone.main.security.*;
//import net.devh.boot.grpc.client.inject.*;
//import net.devh.boot.grpc.client.security.*;
//import org.junit.*;
//import org.junit.jupiter.params.*;
//import org.junit.jupiter.params.provider.*;
//import org.junit.runner.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.boot.test.context.*;
//import org.springframework.test.annotation.*;
//import org.springframework.test.context.junit4.*;
//
//import java.util.*;
//import java.util.stream.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(properties = {
//        "grpc.server.inProcessName=test", // Enable inProcess server
//        "grpc.server.port=-1", // Disable external server
//        "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
//})
//@DirtiesContext
//public class GRpcClientTest {
//
//    @GrpcClient("inProcess")
//    private HelloServiceBlockingStub grpcClient;
//
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    public static Stream<Arguments> source_shouldFail() {
//        return Stream.of(
//                Arguments.of(Set.of(UserRole.USER)),
//                Arguments.of(Set.of(UserRole.ADMIN))
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("source_shouldFail")
//    public void shouldFail(Set<UserRole> roles) {
//        runTest(jwtProvider.createToken("nickname51", roles));
//    }
//
//    @Test
//    public void shouldPass() {
//        runTest(jwtProvider.createToken("nickname51", Set.of(UserRole.ADMIN)));
//    }
//
//    private void runTest(final String token) {
//        final HelloReply response = grpcClient
//                .withCallCredentials(CallCredentialsHelper.bearerAuth(token))
//                .sayHello(HelloRequest.newBuilder()
//                        .setName("Denis")
//                        .build());
//        System.out.println(response.getMessage());
//    }
//}