package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.repository.UserRefRepository;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
import cz.muni.ics.kypo.commons.service.interfaces.UserRefService;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service"})
public class UserRefServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UserRefService userRefService;

    @MockBean
    private UserRefRepository userRefRepository;

    private UserRef userRef1, userRef2;
    private IDMGroupRef groupRef1, groupRef2;

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

        userRef1 = new UserRef();
        userRef1.setId(1L);
        userRef1.setLogin("user1");
        userRef1.setGroups(Arrays.asList(groupRef1));

        userRef2 = new UserRef();
        userRef2.setId(2L);
        userRef2.setLogin("user2");
        userRef2.setGroups(Arrays.asList(groupRef2));

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testCreateUserRef() {
        given(userRefRepository.save(userRef1)).willReturn(userRef1);
        UserRef uR = userRefService.create(userRef1);
        assertEquals(userRef1.getId(), uR.getId());
        assertEquals(userRef1.getLogin(), uR.getLogin());
        assertEquals(userRef1.getGroups().size(), uR.getGroups().size());
        then(userRefRepository).should().save(userRef1);
    }

    @Test
    public void testDeleteUserRef() {
        given(userRefRepository.findByLogin(anyString())).willReturn(Optional.of(userRef1));
        userRefService.delete(userRef1.getLogin());
        then(userRefRepository).should().delete(userRef1);
    }

    @Test
    public void testGetById() {
        given(userRefRepository.findById(userRef1.getId())).willReturn(Optional.of(userRef1));

        UserRef uR = userRefService.getById(userRef1.getId());
        assertEquals(userRef1.getId(), uR.getId());
        assertEquals(userRef1.getLogin(), uR.getLogin());
    }

    @Test
    public void testGetByIdNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("User ref with id "+ userRef1.getId() + " cannot be found.");
        given(userRefRepository.findById(userRef1.getId())).willReturn(Optional.empty());
        UserRef uR = userRefService.getById(userRef1.getId());
    }

    @Test
    public void testGetByLogin() {
        given(userRefRepository.findByLogin(userRef1.getLogin())).willReturn(Optional.of(userRef1));

        UserRef uR = userRefService.getByLogin(userRef1.getLogin());
        assertEquals(userRef1.getId(), uR.getId());
        assertEquals(userRef1.getLogin(), uR.getLogin());
    }

    @Test
    public void testGetByLoginNotFound() {
        thrown.expect(CommonsServiceException.class);
        thrown.expectMessage("User ref with user login "+ userRef1.getLogin() + " cannot be found.");
        given(userRefRepository.findByLogin(userRef1.getLogin())).willReturn(Optional.empty());
        UserRef uR = userRefService.getByLogin(userRef1.getLogin());
    }

    @Test
    public void testGetAllUserRef() {
        given(userRefRepository.findAll(predicate, pageable))
                .willReturn(new PageImpl<>(Arrays.asList(userRef1, userRef2)));

        UserRef userRef3 = new UserRef();
        userRef3.setId(4L);

        List<UserRef> ref = userRefService.getAllUserRef(predicate, pageable).getContent();
        assertEquals(2, ref.size());
        assertTrue(ref.contains(userRef1));
        assertTrue(ref.contains(userRef2));
        assertFalse(ref.contains(userRef3));

        then(userRefRepository).should().findAll(predicate, pageable);
    }
}
