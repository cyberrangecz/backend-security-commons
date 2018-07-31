package cz.muni.ics.kypo.commons.repository;


import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.RoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;


    @SpringBootApplication
    static class TestConfiguration {
    }

    @Test
    public void findByRoleType() throws Exception {
        Role role = new Role();
        role.setRoleType(RoleType.ADMINISTRATOR.name());
        this.entityManager.persist(role);
        Optional<Role> optionalRole = this.roleRepository.findByRoleType(RoleType.ADMINISTRATOR.name());
        Role r = optionalRole.orElseThrow(() -> new Exception("Role shoul be found"));
        assertEquals(role, r);
        assertEquals(RoleType.ADMINISTRATOR.name(), r.getRoleType());
    }

    @Test
    public void findByRoleTypeNotFound() {
        assertFalse(this.roleRepository.findByRoleType(RoleType.ADMINISTRATOR.name()).isPresent());
    }

}
