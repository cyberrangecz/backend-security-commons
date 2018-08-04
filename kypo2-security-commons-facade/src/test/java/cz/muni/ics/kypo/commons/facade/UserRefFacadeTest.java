package cz.muni.ics.kypo.commons.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.GroupRefForUsersDTO;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.api.dto.user.NewUserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefWithGroupsDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.facade.interfaces.UserRefFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade",  "cz.muni.ics.kypo.commons.service"})
public class UserRefFacadeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UserRefFacade userRefFacade;

    @MockBean
    private UserRefService userRefService;

    @MockBean
    private BeanMapping beanMapping;

    private UserRef userRef;
    private UserRefDTO userRefDTO;
    private IDMGroupRef groupRef;
    private GroupRefForUsersDTO groupRefDTO;
    private NewUserRefDTO newUserRefDTO;
    private Predicate predicate;
    private Pageable pageable;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Before
    public void init() {
        userRef = new UserRef();
        userRef.setId(1L);
        userRef.setLogin("user1");

        newUserRefDTO = new NewUserRefDTO();
        newUserRefDTO.setLogin("user1");

        userRefDTO = new UserRefDTO();
        userRefDTO.setId(1L);
        userRefDTO.setLogin("user1");

        groupRef = new IDMGroupRef();
        groupRef.setId(1L);
        groupRef.setIdmGroupId(5L);
        groupRef.addUser(userRef);

        groupRefDTO = new GroupRefForUsersDTO();
        groupRefDTO.setId(1L);
        groupRefDTO.setIdmGroupId(2L);

        pageable = PageRequest.of(0, 10);

    }

    @Test
    public void testCreateUserRef() {
        given(beanMapping.mapTo(any(NewUserRefDTO.class), eq(UserRef.class))).willReturn(userRef);
        given(userRefService.create(any(UserRef.class))).willReturn(userRef);
        given(beanMapping.mapTo(userRef, UserRefDTO.class)).willReturn(userRefDTO);
        UserRefDTO u = userRefFacade.create(newUserRefDTO);
        assertEquals(userRefDTO, u);
    }

    @Test
    public void testDeleteUserRef() {
        userRefFacade.delete("user1");
        then(userRefService).should().delete("user1");
    }

    @Test
    public void testGetById() {
        given(userRefService.getById(1L)).willReturn(userRef);
        given(beanMapping.mapTo(userRef, UserRefDTO.class)).willReturn(userRefDTO);
        UserRefDTO userDTO = userRefFacade.getById(1L);

        assertEquals(userRefDTO, userDTO);
    }

    @Test
    public void testGetByIdWithServiceException() {
        given(userRefService.getById(anyLong())).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        userRefFacade.getById(1L);
    }

    @Test
    public void testGetByLogin() {
        given(userRefService.getByLogin("user1")).willReturn(userRef);
        given(beanMapping.mapTo(userRef, UserRefDTO.class)).willReturn(userRefDTO);
        UserRefDTO userDTO = userRefFacade.getByLogin("user1");

        assertEquals(userRefDTO, userDTO);
    }

    @Test
    public void testGetBylLoginWithServiceException() {
        given(userRefService.getByLogin(anyString())).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        userRefFacade.getByLogin("user1");
    }

    @Test
    public void testAllUserRef() {
        Page<UserRef> userRefPage= new PageImpl<UserRef>(Arrays.asList(userRef));
        PageResultResource<UserRefDTO> pages = new PageResultResource<>();
        pages.setContent(Arrays.asList(userRefDTO));

        given(userRefService.getAllUserRef(predicate, pageable)).willReturn(userRefPage);
        given(beanMapping.mapToPageResultDTO(userRefPage, UserRefDTO.class)).willReturn(pages);
        PageResultResource<UserRefDTO> pageResultResource = userRefFacade.getAllUserRef(predicate,pageable);

        assertEquals(1, pageResultResource.getContent().size());
        assertEquals(userRefDTO, pageResultResource.getContent().get(0));
    }

    @Test
    public void testGetUserRefWithGroups() {
        userRef.setGroups(Arrays.asList(groupRef));
        given(userRefService.getByLogin("user1")).willReturn(userRef);

        UserRefWithGroupsDTO userRefWithGroupsDTO = new UserRefWithGroupsDTO();
        userRefWithGroupsDTO.setId(1L);
        userRefWithGroupsDTO.setLogin("user1");
        userRefWithGroupsDTO.setGroups(Arrays.asList(groupRefDTO));
        given(beanMapping.mapTo(userRef, UserRefDTO.class)).willReturn(userRefDTO);
        UserRefDTO userDTO = userRefFacade.getByLogin("user1");

        assertEquals(userRefDTO, userDTO);
    }
}
