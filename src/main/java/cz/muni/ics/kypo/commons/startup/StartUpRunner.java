package cz.muni.ics.kypo.commons.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.commons.security.mapping.RegisterMicroserviceDTO;
import cz.muni.ics.kypo.commons.security.mapping.RegisterRoleDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Profile("PROD")
@Component
public class StartUpRunner implements ApplicationRunner {

    @Bean(name = "kypoSecurityCommonsRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigValues() {
        PropertySourcesPlaceholderConfigurer confPropertyPlaceholder = new PropertySourcesPlaceholderConfigurer();
        confPropertyPlaceholder.setIgnoreUnresolvablePlaceholders(true);
        return confPropertyPlaceholder;
    }

    private static Logger LOG = LoggerFactory.getLogger(StartUpRunner.class);

    @Value("${server.protocol}")
    private String serverProtocol;
    @Value("${server.ipadress}")
    private String serverIpAddress;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.servlet.context}")
    private String servletContext;
    @Value("${microservice.name}")
    private String microserviceName;
    @Value("${user-and-group-server.uri}")
    private String userAndGroupEndpoint;
    @Value("classpath:roles.json")
    private Resource rolesFile;

    @Autowired
    public StartUpRunner() {
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        registerMicroserviceWithRoles(IOUtils.toString(rolesFile.getInputStream()));
        LOG.debug("Microservice with roles has been registered.");
    }

    private void registerMicroserviceWithRoles(String roles) {
        RestTemplate restTemplate = restTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth("microservice", "micros");
        RegisterMicroserviceDTO newMicroservice = new RegisterMicroserviceDTO();
        newMicroservice.setName(microserviceName);
        String endpoint = serverPort + "://" +
                serverIpAddress + ":" +
                serverPort + "/" +
                servletContext;
        newMicroservice.setEndpoint(endpoint);
        ObjectMapper mapper = new ObjectMapper();
        try {
            LOG.info(httpHeaders.toString());
            newMicroservice.setRoles(Arrays.stream(mapper.readValue(roles, RegisterRoleDTO[].class)).collect(Collectors.toCollection(HashSet::new)));
            restTemplate.exchange(userAndGroupEndpoint + "/microservices", HttpMethod.POST, new HttpEntity<>(mapper.writeValueAsString(newMicroservice), httpHeaders), Void.class);
        } catch (IOException ex) {
            throw new SecurityException("Error while parsing roles for microservices", ex);
        } catch (HttpClientErrorException ex) {
            throw new SecurityException("Error while register microservice in user and group microservice. Message: " + System.lineSeparator() + ex.getResponseBodyAsString());
        }
    }


}
