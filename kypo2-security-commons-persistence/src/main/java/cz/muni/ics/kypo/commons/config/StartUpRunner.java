package cz.muni.ics.kypo.commons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.commons.mapping.RolesWrapper;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
@PropertySource("file:${path-to-config-file}")
public class StartUpRunner implements ApplicationRunner {
    private static Logger LOGGER = LoggerFactory.getLogger(StartUpRunner.class);

    @Value("${path.to.file.with.initial.users.and.services}")
    private String pathToFileWithInitialRoles;

    private RoleRepository roleRepository;


    @Autowired
    public StartUpRunner(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        RolesWrapper roles  = mapper.readValue(new File(pathToFileWithInitialRoles), RolesWrapper.class);

        loadRoles(roles.getRoles());

        LOGGER.info("Roles from external file were loaded and created in DB");
    }


    private void loadRoles(List<String> roles) {
        roles.forEach(roleString -> {
            Optional<Role> optionalRole = roleRepository.findByRoleType(roleString.toUpperCase());
            if (!optionalRole.isPresent()) {
                Role role = new Role();
                role.setRoleType(roleString.toUpperCase());
                roleRepository.save(role);
                LOGGER.info("Role {} were added.", roleString);
            } else {
                LOGGER.info("Role {} already exists in DB", roleString);
            }
        });
    }

}
