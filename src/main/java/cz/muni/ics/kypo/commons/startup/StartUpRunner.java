package cz.muni.ics.kypo.commons.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.commons.security.mapping.RegisterMicroserviceDTO;
import cz.muni.ics.kypo.commons.security.mapping.RegisterRoleDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * The StartUpRunner provides control over methods executed during start of application (microservice) which import this project.
 */
@Component
public class StartUpRunner implements ApplicationRunner {

    private static Logger LOG = LoggerFactory.getLogger(StartUpRunner.class);

    @Value("${server.protocol}")
    private String serverProtocol;
    @Value("${server.ipaddress}")
    private String serverIpAddress;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.servlet.context}")
    private String servletContext;
    @Value("${microservice.name}")
    private String microserviceName;
    @Value("classpath:roles.json")
    private Resource rolesFile;
    private WebClient webClient;
    private ObjectMapper objectMapper;

    /**
     * Instantiates a new StartUpRunner.
     */
    @Autowired
    public StartUpRunner(@Qualifier(value = "userManagementServiceWebClientSecurityCommons") WebClient webClient,
                         @Qualifier("kypoSecurityCommonsObjectMapper") ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        registerMicroserviceWithRoles(IOUtils.toString(rolesFile.getInputStream()));
        LOG.debug("Microservice with roles has been registered.");
    }

    private void registerMicroserviceWithRoles(String roles) {
        RegisterMicroserviceDTO newMicroservice = new RegisterMicroserviceDTO();
        newMicroservice.setName(microserviceName);
        String endpoint = serverProtocol + "://" + serverIpAddress + ":" + serverPort + "/" + servletContext;
        newMicroservice.setEndpoint(endpoint);
        try {
            newMicroservice.setRoles(
                    Arrays.stream(objectMapper.readValue(roles, RegisterRoleDTO[].class))
                            .collect(Collectors.toCollection(HashSet::new)));
            webClient
                    .post()
                    .uri("/microservices")
                    .headers(headers -> headers.setBasicAuth("microservice", "micros"))
                    .body(objectMapper.writeValueAsString(newMicroservice), String.class)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (IOException ex) {
            throw new SecurityException("Error while parsing roles for microservices", ex);
        } catch (HttpClientErrorException ex) {
            throw new SecurityException("Error while register microservice in user and group microservice. Message: " + System.lineSeparator() + ex.getResponseBodyAsString());
        }
    }

}
