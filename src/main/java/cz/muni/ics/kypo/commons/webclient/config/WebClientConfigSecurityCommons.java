package cz.muni.ics.kypo.commons.webclient.config;

import cz.muni.ics.kypo.commons.BeansConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Import({BeansConfiguration.class})
public class WebClientConfigSecurityCommons {

    @Value("${user-and-group-server.uri}")
    private String userAndGroupEndpoint;

    @Bean
    @Qualifier("userManagementServiceWebClientSecurityCommons")
    public WebClient userManagementServiceWebClientSecurityCommons() {
        return WebClient.builder()
                .baseUrl(userAndGroupEndpoint)
                .defaultHeaders(headers -> {
                    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }

}
