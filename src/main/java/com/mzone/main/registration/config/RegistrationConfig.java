package com.mzone.main.registration.config;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrationConfig {
    @Bean
    public VelocityEngine velocityEngine() {
        final VelocityEngine velocityEngine = new VelocityEngine();

        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        velocityEngine.init();

        return velocityEngine;
    }
}
