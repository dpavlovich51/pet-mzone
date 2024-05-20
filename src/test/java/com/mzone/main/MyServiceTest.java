//package com.mzone.main;
//
//import com.mzone.main.controller.*;
//import com.mzone.main.grpc.service.GrpcProto.*;
//import io.grpc.internal.testing.*;
//import org.junit.Test;
//import org.junit.jupiter.api.*;
//import org.junit.runner.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.boot.test.context.*;
//import org.springframework.test.annotation.*;
//import org.springframework.test.context.junit4.*;
//
//import java.util.*;
//import java.util.concurrent.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = {MyServiceUnitTestConfiguration.class}, properties = {
//        "grpc.server.inProcessName=test", // Enable inProcess server
//        "grpc.server.port=-1", // Disable external server
//        "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
//})
//@DirtiesContext
//public class MyServiceTest {
//
//    @Autowired
//    private HelloGrpcController myService;
//
//    @Test
//    public void testSayHello() throws Exception {
//        HelloRequest request = HelloRequest.newBuilder()
//                .setName("Test")
//                .build();
//        StreamRecorder<HelloReply> responseObserver = StreamRecorder.create();
//        myService.sayHello(request, responseObserver);
//        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
//            fail("The call did not terminate in time");
//        }
//        assertNull(responseObserver.getError());
//        List<HelloReply> results = responseObserver.getValues();
//        Assertions.assertEquals(1, results.size());
//        HelloReply response = results.get(0);
//        assertEquals(HelloReply.newBuilder()
//                .setMessage("hello, ".concat(request.getName()))
//                .build(), response);
//    }
//
//}