package cz.muni.ics.kypo.commons.config;

import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.repository.UserRefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:${path-to-config-file}")
public class StartUpRunner {
    private static Logger LOGGER = LoggerFactory.getLogger(StartUpRunner.class);

    @Value("${path.to.file.with.initial.users.and.services}")
    private String pathToFileWithInitialUsersAndServices;

    private IDMGroupRefRepository groupRefRepository;
    private UserRefRepository userRefRepository;
    private RoleRepository roleRepository;

    private Role adminRole, userRole, guestRole;

    private IDMGroupRef adminGroup, userGroup, guestGroup;

    @Autowired
    public StartUpRunner(UserRefRepository userRepository, IDMGroupRefRepository groupRepository,
                         RoleRepository roleRepository) {
        this.userRefRepository = userRepository;
        this.groupRefRepository = groupRepository;
        this.roleRepository = roleRepository;
    }
}
