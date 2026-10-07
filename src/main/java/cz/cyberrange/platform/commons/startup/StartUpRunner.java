package cz.cyberrange.platform.commons.startup;

import cz.cyberrange.platform.commons.startup.mapping.RegisterMicroserviceDTO;
import cz.cyberrange.platform.commons.startup.mapping.RegisterRoleDTO;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * The StartUpRunner provides control over methods executed during start of application
 * (microservice) which import this project.
 */
@Slf4j
@Component
public class StartUpRunner implements ApplicationRunner {

  private final WebClient webClient;
  private final JsonMapper objectMapper =
      JsonMapper.builder()
          .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .enable(SerializationFeature.INDENT_OUTPUT)
          .build();

  @Value("${server.port}")
  private String servicePort;

  @Value("${server.servlet.context-path}")
  private String serviceContextPath;

  @Value("${microservice.name}")
  private String serviceName;

  @Value("classpath:roles.json")
  private Resource rolesFile;

  /** Instantiates a new StartUpRunner. */
  @Autowired
  public StartUpRunner(
      @Qualifier(value = "userManagementServiceWebClientSecurityCommons") WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    registerMicroserviceWithRoles(
        IOUtils.toString(rolesFile.getInputStream(), StandardCharsets.UTF_8));
    log.debug("Microservice with roles has been registered.");
  }

  private void registerMicroserviceWithRoles(String roles) {
    RegisterMicroserviceDTO newMicroservice = new RegisterMicroserviceDTO();
    newMicroservice.setName(serviceName);
    String endpoint = "BASE_URL:" + servicePort + serviceContextPath;
    newMicroservice.setEndpoint(endpoint);
    try {
      newMicroservice.setRoles(
          Arrays.stream(objectMapper.readValue(roles, RegisterRoleDTO[].class))
              .collect(Collectors.toCollection(HashSet::new)));
      webClient
          .post()
          .uri("/microservices")
          .headers(headers -> headers.setBasicAuth("microservice", "micros"))
          .body(Mono.just(objectMapper.writeValueAsString(newMicroservice)), String.class)
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (JacksonException ex) {
      throw new SecurityException("Error while parsing roles for microservices", ex);
    } catch (WebClientResponseException ex) {
      throw new SecurityException(
          "Error while register microservice in user and group microservice. Message: "
              + System.lineSeparator()
              + ex.getResponseBodyAsString());
    }
  }
}
