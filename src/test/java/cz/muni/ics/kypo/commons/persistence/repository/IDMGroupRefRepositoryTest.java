package cz.muni.ics.kypo.commons.persistence.repository;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.persistence.config.PersistenceConfigTest;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(PersistenceConfigTest.class)
public class IDMGroupRefRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IDMGroupRefRepository groupRefRepository;

    private IDMGroupRef groupRef, groupRef2;

    private Role adminRole, userRole, guestRole;

    private Pageable pageable;

    private Predicate predicate;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Before
    public void init() {
        adminRole = new Role();
        adminRole.setRoleType("ADMINISTRATOR");

        userRole = new Role();
        userRole.setRoleType("RoleTypeTest.USER");

        guestRole = new Role();
        guestRole.setRoleType("GUEST");

        groupRef = new IDMGroupRef();
        groupRef.setIdmGroupId(1L);

        groupRef2 = new IDMGroupRef();
        groupRef2.setIdmGroupId(2L);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void findByIDMGroupId() throws Exception {
        Long expectedId = 1L;
        IDMGroupRef groupRef = new IDMGroupRef();
        groupRef.setIdmGroupId(1L);
        this.entityManager.persist(groupRef);
        Optional<IDMGroupRef> optionalGroup = this.groupRefRepository.findByIdmGroupId(expectedId);
        IDMGroupRef g = optionalGroup.orElseThrow(() -> new Exception("Group should be found"));
        assertEquals(groupRef, g);
    }

    @Test
    public void findByGroupIdNotFound() {
        assertFalse(this.groupRefRepository.findByIdmGroupId(1L).isPresent());
    }

    @Test
    public void findAllByRoleType() {
        entityManager.persistFlushFind(adminRole);
        groupRef.addRole(adminRole);
        entityManager.persistFlushFind(groupRef);

        List<IDMGroupRef> groups = groupRefRepository.findAllByRoleType("ADMINISTRATOR");
        assertEquals(1, groups.size());
        assertEquals(this.groupRef.getIdmGroupId(), groups.get(0).getIdmGroupId());
    }

    @Test
    public void getRolesOfGroup() {
        entityManager.persistFlushFind(adminRole);
        entityManager.persistFlushFind(userRole);
        entityManager.persistFlushFind(guestRole);
        groupRef.setRoles(Stream.of(adminRole, userRole).collect(Collectors.toSet()));
        entityManager.persistFlushFind(groupRef);

        Set<Role> rolesOfGroup = groupRefRepository.getRolesOfGroup(groupRef.getId());
        rolesOfGroup.forEach(role -> System.out.println(role.getRoleType()));
        assertEquals(2, rolesOfGroup.size());
        assertTrue(rolesOfGroup.contains(adminRole));
        assertTrue(rolesOfGroup.contains(userRole));
        assertFalse(rolesOfGroup.contains(guestRole));
    }

    @Test
    public void getRolesOfGroupsRef() {
        entityManager.persistFlushFind(adminRole);
        entityManager.persistFlushFind(userRole);
        entityManager.persistFlushFind(guestRole);
        groupRef.setRoles(Stream.of(adminRole, userRole).collect(Collectors.toSet()));
        entityManager.persistFlushFind(groupRef);

        groupRef2.setRoles(Stream.of(guestRole).collect(Collectors.toSet()));
        entityManager.persistFlushFind(groupRef2);

        Set<Role> rolesOfGroup = groupRefRepository.getRolesOfGroupsRef(Set.of(groupRef.getIdmGroupId(), groupRef2.getIdmGroupId()));
        rolesOfGroup.forEach(role -> System.out.println(role.getRoleType()));
        assertEquals(3, rolesOfGroup.size());
        assertTrue(rolesOfGroup.contains(adminRole));
        assertTrue(rolesOfGroup.contains(userRole));
        assertTrue(rolesOfGroup.contains(guestRole));
    }
}
