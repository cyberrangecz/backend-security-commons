package cz.muni.ics.kypo.commons.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade",  "cz.muni.ics.kypo.commons.service"})
public class IDMGroupRefFacadeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private IDMGroupRefFacade groupRefFacade;

    @MockBean
    private IDMGroupRefService groupRefService;

    @MockBean
    private BeanMapping beanMapping;

    private IDMGroupRef groupRef1, groupRef2;
    private IDMGroupRefDTO groupRefDTO;
    private UserRef userRef1;
    private Role role1, role2;
    private Predicate predicate;
    private Pageable pageable;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Before
    public void init() {
        userRef1 = new UserRef();
        userRef1.setId(1L);
        userRef1.setLogin("user1");

        groupRef1 = new IDMGroupRef();
        groupRef1.setId(1L);
        groupRef1.setIdmGroupId(5L);
        groupRef1.addUser(userRef1);

        groupRefDTO = new IDMGroupRefDTO();
        groupRefDTO.setId(1L);
        groupRefDTO.setIdmGroupId(5L);
        groupRefDTO.setUsers(new ArrayList<>());

        groupRef2 = new IDMGroupRef();
        groupRef2.setId(2L);
        groupRef2.setIdmGroupId(2L);

        role1 = new Role();
        role1.setId(1L);
        role1.setRoleType("ADMINISTRATOR");

        role2 = new Role();
        role2.setId(2L);
        role2.setRoleType("GUEST");


        pageable = PageRequest.of(0, 10);

    }

    @Test
    public void testCreateIDMGroupRef() {
        given(beanMapping.mapTo(any(NewGroupRefDTO.class), eq(IDMGroupRef.class))).willReturn(groupRef1);
        given(groupRefService.create(any(IDMGroupRef.class))).willReturn(groupRef1);
        given(beanMapping.mapTo(groupRef1, IDMGroupRefDTO.class)).willReturn(groupRefDTO);
        IDMGroupRefDTO g = groupRefFacade.create(getNewGroupRefDTO());
        assertEquals(groupRefDTO.getIdmGroupId(), g.getIdmGroupId());
        assertEquals(groupRefDTO.getId(), g.getId());
    }

    @Test
    public void testDeleteIDMGroupRef() {
        given(groupRefService.getByIdmGroupId(anyLong())).willReturn(groupRef1);
        groupRefFacade.delete(1L);
        then(groupRefService).should().delete(1L);
    }

    @Test
    public void testGetByIDMGroupId() {
        given(groupRefService.getByIdmGroupId(1L)).willReturn(groupRef1);
        given(beanMapping.mapTo(any(IDMGroupRef.class), eq(IDMGroupRefDTO.class))).willReturn(groupRefDTO);
        IDMGroupRefDTO gRDTO = groupRefFacade.getByIdmGroupId(1L);
        assertEquals(groupRefDTO.getIdmGroupId(), gRDTO.getIdmGroupId());
        assertEquals(groupRefDTO.getId(), gRDTO.getId());
    }

    @Test
    public void testGetByIDMGroupIdWithServiceException() {
        given(groupRefService.getByIdmGroupId(1L)).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        groupRefFacade.getByIdmGroupId(1L);
    }

    @Test
    public void testGetAllIDMGroupRef() {
        Page<IDMGroupRef> groupRefPage = new PageImpl<IDMGroupRef>(Arrays.asList(groupRef1));
        PageResultResource<IDMGroupRefDTO> pages = new PageResultResource<>();
        pages.setContent(Arrays.asList(groupRefDTO));

        given(groupRefService.findAll(predicate, pageable)).willReturn(groupRefPage);
        given(beanMapping.mapToPageResultDTO(groupRefPage, IDMGroupRefDTO.class)).willReturn(pages);
        PageResultResource<IDMGroupRefDTO> pageResultResource = groupRefFacade.findAll(predicate,pageable);

        assertEquals(1, pageResultResource.getContent().size());
        assertEquals(groupRefDTO, pageResultResource.getContent().get(0));
    }

    @Test
    public void testAssignRoleToGroup() {
        groupRef1.addRole(role1);
        given(groupRefService.assignRoleToGroup(1L, "ADMINISTRATOR")).willReturn(groupRef1);
        groupRefDTO.setRoles(getSetOfRolesDTO());
        given(beanMapping.mapTo(any(IDMGroupRef.class), eq(IDMGroupRefDTO.class))).willReturn(groupRefDTO);
        IDMGroupRefDTO grDTO = groupRefFacade.assignRoleToGroup(1L, "ADMINISTRATOR");
        assertTrue(grDTO.getRoles().contains(getRoleDTO()));
        assertEquals(1, grDTO.getRoles().size());
    }

    @Test
    public void testAssignRoleToGroupWithServiceException() {
        given(groupRefService.assignRoleToGroup(anyLong(), anyString())).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        groupRefFacade.assignRoleToGroup(1L, "ADMINISTRATOR");
    }

    @Test
    public void testAddUsersToGroup() {
        given(groupRefService.addUsersToGroupRef(1L, Arrays.asList("user1"))).willReturn(groupRef1);
        groupRefDTO.setUsers(getListOfUsersDTO());
        given(beanMapping.mapTo(any(IDMGroupRef.class), eq(IDMGroupRefDTO.class))).willReturn(groupRefDTO);
        IDMGroupRefDTO grDTO = groupRefFacade.addUsersToGroupRef(1L, Arrays.asList("user1"));

        assertEquals(getListOfUsersDTO().get(0).getLogin(),grDTO.getUsers().get(0).getLogin());
        assertEquals(1, grDTO.getUsers().size());
    }

    @Test
    public void testAddUsersToGroupWithServiceException() {
        given(groupRefService.addUsersToGroupRef(1L, Arrays.asList("user1"))).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        IDMGroupRefDTO grDTO = groupRefFacade.addUsersToGroupRef(1L, Arrays.asList("user1"));

    }

    @Test
    public void testRemoveUsersFromGroup() {
        given(groupRefService.removeUsersFromGroupRef(1L, Arrays.asList("user1"))).willReturn(groupRef1);
        given(beanMapping.mapTo(any(IDMGroupRef.class), eq(IDMGroupRefDTO.class))).willReturn(groupRefDTO);
        IDMGroupRefDTO grDTO = groupRefFacade.removeUsersFromGroupRef(1L, Arrays.asList("user1"));
        assertEquals(0, grDTO.getUsers().size());
    }

    @Test
    public void testRemoveUsersFromGroupWithServiceException() {
        given(groupRefService.removeUsersFromGroupRef(1L, Arrays.asList("user1"))).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        IDMGroupRefDTO grDTO = groupRefFacade.removeUsersFromGroupRef(1L, Arrays.asList("user1"));
    }

    public NewGroupRefDTO getNewGroupRefDTO() {
        NewGroupRefDTO groupRefDTO = new NewGroupRefDTO();
        groupRefDTO.setIdmGroupId(1L);
        return groupRefDTO;
    }

    public Set<RoleDTO> getSetOfRolesDTO() {
        Set<RoleDTO> roleDTOS = new HashSet<>();
        roleDTOS.add(getRoleDTO());
        return roleDTOS;
    }

    public RoleDTO getRoleDTO() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setRoleType("ADMINISTRATOR");
        return roleDTO;
    }

    public List<UserRefDTO> getListOfUsersDTO() {
        UserRefDTO userRefDTO = new UserRefDTO();
        userRefDTO.setId(1L);
        userRefDTO.setLogin("user1");

        List<UserRefDTO> userRefDTOList = new ArrayList<>();
        userRefDTOList.add(userRefDTO);
        return userRefDTOList;
    }
}
