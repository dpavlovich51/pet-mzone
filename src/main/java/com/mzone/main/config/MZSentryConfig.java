package com.mzone.main.config;

import io.sentry.*;
import org.springframework.context.annotation.*;

@Configuration
public class MZSentryConfig {

    @Bean
    void sentryConfig() {
        Sentry.init(options -> {
            options.setDsn("https://24a2d2334910481893bc76f790da3c14@o1076478.ingest.sentry.io/6078337");
            // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
            // We recommend adjusting this value in production.
            options.setTracesSampleRate(1.0);
            // When first trying Sentry it's good to see what the SDK is doing:
            options.setDebug(true);
        });
    }

}
