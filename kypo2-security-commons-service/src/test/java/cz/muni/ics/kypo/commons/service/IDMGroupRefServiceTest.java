package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.repository.UserRefRepository;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service"})
public class IDMGroupRefServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private IDMGroupRefService groupRefService;

    @MockBean
    private IDMGroupRefRepository groupRefRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRefRepository userRefRepository;

    private IDMGroupRef groupRef1, groupRef2;

    private Role role1, role2;

    private UserRef userRef1, userRef2;

    private Pageable pageable;
    private Predicate predicate;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Before
    public void init() {
        groupRef1 = new IDMGroupRef();
        groupRef1.setId(1L);
        groupRef1.setIdmGroupId(5L);

        groupRef2 = new IDMGroupRef();
        groupRef2.setId(2L);
        groupRef2.setIdmGroupId(2L);

        role1 = new Role();
        role1.setId(1L);
        role1.setRoleType("ADMINISTRATOR");

        role2 = new Role();
        role2.setId(2L);
        role2.setRoleType("GUEST");

        userRef1 = new UserRef();
        userRef1.setId(1L);
        userRef1.setLogin("user1");

        userRef2 = new UserRef();
        userRef2.setId(2L);
        userRef2.setLogin("user2");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testGetByIDMGroupId() {
        given(groupRefRepository.findByIdmGroupId(groupRef1.getIdmGroupId())).willReturn(Optional.of(groupRef1));

        IDMGroupRef gR = groupRefService.getByIdmGroupId(groupRef1.getIdmGroupId());
        assertEquals(groupRef1.getIdmGroupId(), gR.getIdmGroupId());

        then(groupRefRepository).should().findByIdmGroupId(groupRef1.getIdmGroupId());

    }

    @Test
    public void testGetByIDMGroupIdNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("IDMGroupRef with idm group id " + groupRef2.getIdmGroupId() + " not found");

        given(groupRefRepository.findByIdmGroupId(groupRef2.getIdmGroupId())).willReturn(Optional.empty());
        groupRefService.getByIdmGroupId(groupRef2.getIdmGroupId());

    }

    @Test
    public void testGetAllIDMGroupRef() {
        given(groupRefRepository.findAll(predicate, pageable))
                .willReturn(new PageImpl<>(Arrays.asList(groupRef1, groupRef2)));

        IDMGroupRef groupRef3 = new IDMGroupRef();

        List<IDMGroupRef> groups = groupRefService.findAll(predicate, pageable).getContent();
        assertEquals(2, groups.size());
        assertTrue(groups.contains(groupRef1));
        assertTrue(groups.contains(groupRef2));
        assertFalse(groups.contains(groupRef3));

        then(groupRefRepository).should().findAll(predicate, pageable);
    }

    @Test
    public void testCreateIDMGroupRef() {
        given(groupRefRepository.save(groupRef1)).willReturn(groupRef1);
        IDMGroupRef g = groupRefService.create(groupRef1);
        assertEquals(groupRef1.getIdmGroupId(), g.getIdmGroupId());
        assertEquals(groupRef1.getId(), g.getId());

        then(groupRefRepository).should().save(groupRef1);
    }

    @Test
    public void testCreateIDMGroupRefWithNullEntity() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Input idm group ref must not be null");
        groupRefService.create(null);
    }

    @Test
    public void testUpdateIDMGroupRef() {
        given(groupRefRepository.save(groupRef2)).willReturn(groupRef2);
        IDMGroupRef g = groupRefService.update(groupRef2);
        assertEquals(groupRef2.getIdmGroupId(), g.getIdmGroupId());
        assertEquals(groupRef2.getId(), g.getId());

        then(groupRefRepository).should().save(groupRef2);
    }

    @Test
    public void testUpdateIDMGroupRefWithNullEntity() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Input idm group ref must not be null");
        groupRefService.update(null);
    }

    @Test
    public void testDeleteIDMGroupRef() {
        given(groupRefRepository.findByIdmGroupId(1L)).willReturn(Optional.of(groupRef1));
        groupRefService.delete(1L);
        then(groupRefRepository).should().delete(groupRef1);
    }


    @Test
    public void testAssignRoleToGroup() {
        given(roleRepository.findByRoleType(anyString())).willReturn(Optional.of(role1));
        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.of(groupRef1));
        groupRef1.addRole(role1);
        given(groupRefRepository.save(groupRef1)).willReturn(groupRef1);
        IDMGroupRef groupRef = groupRefService.assignRoleToGroup(groupRef1.getId(), "ADMINISTRATOR");

        assertEquals(1, groupRef.getRoles().size());
        assertTrue(groupRef.getRoles().contains(role1));

        then(groupRefRepository).should().save(groupRef1);
    }

    @Test
    public void testAssignRoleToGroupWithRoleNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("Input role type " + role1.getRoleType() + " cannot be found");
        given(roleRepository.findByRoleType(anyString())).willReturn(Optional.empty());
        groupRefService.assignRoleToGroup(groupRef1.getId(), "ADMINISTRATOR");

    }

    @Test
    public void testAssignRoleToGroupWithGroupRefNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("IDMGroupRef with idm group id " + groupRef1.getIdmGroupId() + " not found");
        given(roleRepository.findByRoleType(anyString())).willReturn(Optional.of(role1));
        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.empty());
        groupRefService.assignRoleToGroup(groupRef1.getIdmGroupId(), "ADMINISTRATOR");

    }

    @Test
    public void testAssignRoleToGroupWithEmptyRole() {
        thrown.expect(IllegalArgumentException.class);
        groupRefService.assignRoleToGroup(groupRef1.getIdmGroupId(), "");

    }

    @Test
    public void testAddUsersToGroup() {
        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.of(groupRef1));
        given(userRefRepository.findByLogin(userRef1.getLogin())).willReturn(Optional.of(userRef1));
        groupRef1.addUser(userRef1);
        given(groupRefRepository.save(groupRef1)).willReturn(groupRef1);
        IDMGroupRef groupRef = groupRefService.addUsersToGroupRef(groupRef1.getId(), Collections.singletonList("user1"));

        assertEquals(1, groupRef.getUsers().size());
        assertTrue(groupRef.getUsers().contains(userRef1));

        then(groupRefRepository).should().save(groupRef1);
    }

    @Test
    public void testAddUsersToGroupWithEmptyListOfLogins() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Input list of users logins cannot be empty");
        IDMGroupRef groupRef = groupRefService.addUsersToGroupRef(groupRef1.getIdmGroupId(), Arrays.asList());
    }

    @Test
    public void testAddUsersToGroupWithGroupNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("IDMGroupRef with idm group id " + groupRef1.getIdmGroupId() + " not found");

        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.empty());
        IDMGroupRef groupRef = groupRefService.addUsersToGroupRef(groupRef1.getIdmGroupId(), Collections.singletonList("user1"));
    }

    @Test
    public void testAddUsersToGroupWithNotFoundUser() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("User with login " + userRef1.getLogin() + " not found.");
        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.of(groupRef1));
        given(userRefRepository.findByLogin(userRef1.getLogin())).willReturn(Optional.empty());
        IDMGroupRef groupRef = groupRefService.addUsersToGroupRef(groupRef1.getIdmGroupId(), Collections.singletonList("user1"));
    }

    @Test
    public void testRemoveUsersFromGroup() {
        groupRef1.addUser(userRef1);
        groupRef1.addUser(userRef2);
        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.of(groupRef1));
        given(userRefRepository.findByLogin(userRef1.getLogin())).willReturn(Optional.of(userRef1));
        groupRef1.removeUser(userRef1);
        given(groupRefRepository.save(groupRef1)).willReturn(groupRef1);
        IDMGroupRef groupRef = groupRefService.removeUsersFromGroupRef(groupRef1.getId(), Collections.singletonList("user1"));

        assertEquals(1, groupRef.getUsers().size());
        assertTrue(groupRef.getUsers().contains(userRef2));

        then(groupRefRepository).should().save(groupRef1);
    }

    @Test
    public void testRemoveUsersFromGroupWithEmptyListOfLogins() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Input list of users logins cannot be empty");
        IDMGroupRef groupRef = groupRefService.removeUsersFromGroupRef(groupRef1.getIdmGroupId(), Arrays.asList());
    }

    @Test
    public void testRemoveUsersFromGroupWithGroupNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("IDMGroupRef with idm group id " + groupRef1.getIdmGroupId() + " not found");

        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.empty());
        IDMGroupRef groupRef = groupRefService.removeUsersFromGroupRef(groupRef1.getIdmGroupId(), Collections.singletonList("user1"));
    }

    @Test
    public void testRemoveUsersFromGroupWithNotFoundUser() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("User with login " + userRef1.getLogin() + " not found.");
        given(groupRefRepository.findByIdmGroupId(anyLong())).willReturn(Optional.of(groupRef1));
        given(userRefRepository.findByLogin(userRef1.getLogin())).willReturn(Optional.empty());
        IDMGroupRef groupRef = groupRefService.removeUsersFromGroupRef(groupRef1.getIdmGroupId(), Collections.singletonList("user1"));
    }
}
