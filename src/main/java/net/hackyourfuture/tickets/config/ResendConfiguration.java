package net.hackyourfuture.tickets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ResendConfiguration {

    @Bean
    public RestClient resendClient() {
        return RestClient.builder()
                .baseUrl("https://api.resend.com")
                .build();
    }
}