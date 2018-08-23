package cz.muni.ics.kypo.commons.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.config.FacadeTestConfiguration;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade", "cz.muni.ics.kypo.commons.service", "cz.muni.ics.kypo.commons.mapping"})
@Import(FacadeTestConfiguration.class)
public class IDMGroupRefFacadeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private IDMGroupRefFacade groupRefFacade;

    @MockBean
    private IDMGroupRefService groupRefService;

    private IDMGroupRef groupRef1, groupRef2;
    private IDMGroupRefDTO groupRefDTO;
    private Role role1, role2;
    private Predicate predicate;
    private Pageable pageable;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Before
    public void init() {


        groupRef1 = new IDMGroupRef();
        groupRef1.setId(1L);
        groupRef1.setIdmGroupId(1L);

        groupRefDTO = new IDMGroupRefDTO();
        groupRefDTO.setId(1L);
        groupRefDTO.setIdmGroupId(1L);

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
        given(groupRefService.create(any(IDMGroupRef.class))).willReturn(groupRef1);
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
        PageResultResource<IDMGroupRefDTO> pageResultResource = groupRefFacade.findAll(predicate,pageable);

        assertEquals(1, pageResultResource.getContent().size());
        assertEquals(groupRefDTO.getIdmGroupId(), pageResultResource.getContent().get(0).getIdmGroupId());
    }

    @Test
    public void testAssignRoleToGroup() {
        groupRef1.addRole(role1);
        given(groupRefService.assignRoleToGroup(1L, "ADMINISTRATOR")).willReturn(groupRef1);
        groupRefDTO.setRoles(getSetOfRolesDTO());
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

    public NewGroupRefDTO getNewGroupRefDTO() {
        NewGroupRefDTO groupRefDTO = new NewGroupRefDTO();
        groupRefDTO.setIdmGroupId(1L);
        return groupRefDTO;
    }

    @Test
    public void testGetRolesOfGroups() {
        given(groupRefService.getRolesOfGroups(Arrays.asList(1L, 2L))).willReturn(new HashSet<>(Arrays.asList(role1,role2)));
        Set<RoleDTO> roleDTOS = groupRefFacade.getRolesOfGroups(Arrays.asList(1L,2L));
        assertTrue(roleDTOS.contains(getRoleDTO()));
        assertEquals(2, roleDTOS.size());
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

}
