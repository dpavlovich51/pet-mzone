//package com.mzone.main.security;
//
//import com.mzone.main.grpc.service.*;
//import net.devh.boot.grpc.server.security.authentication.*;
//import net.devh.boot.grpc.server.security.check.*;
//import org.springframework.context.annotation.*;
//import org.springframework.security.access.*;
//import org.springframework.security.access.vote.*;
//import org.springframework.security.authentication.*;
//
//import java.util.*;
//
//@Configuration
//public class SecurityConfiguration {
//
//    @Bean
//    public AuthenticationManager authenticationManager(JwtAuthenticationProvider jwtProvider) {
//        return new ProviderManager(List.of(
//                jwtProvider
//        ));
//    }
//
//    @Bean
//    public GrpcAuthenticationReader grpcAuthenticationReader() {
//        return new CompositeGrpcAuthenticationReader(
//                List.of(new BearerAuthenticationReader(JwtTokenAuthenticationData::new))
//        );
//    }
//
//    @Bean
//    GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
//        final ManualGrpcSecurityMetadataSource source = new ManualGrpcSecurityMetadataSource();
////        source.setDefault(AccessPredicate.denyAll());
//        source.setDefault(AccessPredicate.authenticated());
//        source.set(HelloServiceGrpc.getSayHelloMethod(), AccessPredicate.permitAll());
//        return source;
//    }
//
//    @Bean
//    AccessDecisionManager accessDecisionManager() {
//        final List<AccessDecisionVoter<?>> voters = new ArrayList<>();
//        voters.add(new AccessPredicateVoter());
//        return new UnanimousBased(voters);
//    }
//
//}
