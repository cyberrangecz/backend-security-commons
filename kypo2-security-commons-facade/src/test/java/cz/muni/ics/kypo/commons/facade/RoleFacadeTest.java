package cz.muni.ics.kypo.commons.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.role.NewRoleDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.config.FacadeTestConfiguration;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
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
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade",  "cz.muni.ics.kypo.commons.service", "cz.muni.ics.kypo.commons.mapping"})
@Import(FacadeTestConfiguration.class)
public class RoleFacadeTest  {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private RoleFacade roleFacade;

    @MockBean
    private RoleService roleService;

    private Role role1, role2;
    private NewRoleDTO newRoleDTO;
    private RoleDTO roleDTO;
    private Predicate predicate;
    private Pageable pageable;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Before
    public void init() {
        role1 = new Role();
        role1.setId(1L);
        role1.setRoleType("ADMINISTRATOR");

        role2 = new Role();
        role2.setId(2L);
        role2.setRoleType("GUEST");

        newRoleDTO = new NewRoleDTO();
        newRoleDTO.setRoleType("ADMINISTRATOR");

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setRoleType("ADMINISTRATOR");

        pageable = PageRequest.of(0, 10);

    }

    @Test
    public void testGetById() {
        given(roleService.getById(1L)).willReturn(role1);
        RoleDTO rDTO = roleFacade.getById(1L);

        assertEquals(roleDTO.getId(), rDTO.getId());
        assertEquals(roleDTO.getRoleType(), rDTO.getRoleType());
    }

    @Test
    public void testGetByIdWithServiceException() {
        given(roleService.getById(anyLong())).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        roleFacade.getById(1L);
    }

    @Test
    public void testGetByRoleType() {
        given(roleService.getByRoleType("ADMINISTRATOR")).willReturn(role1);
        RoleDTO rDTO = roleFacade.getByRoleType("ADMINISTRATOR");

        assertEquals(roleDTO.getId(), rDTO.getId());
        assertEquals(roleDTO.getRoleType(), rDTO.getRoleType());
    }

    @Test
    public void testGetByRoleTypeWithServiceException() {
        given(roleService.getByRoleType("ADMINISTRATOR")).willThrow(new CommonsServiceException());
        thrown.expect(CommonsFacadeException.class);
        roleFacade.getByRoleType("ADMINISTRATOR");
    }

    @Test
    public void testGetAllRoles() {
        Page<Role> rolesPage = new PageImpl<Role>(Arrays.asList(role1));
        PageResultResource<RoleDTO> pages = new PageResultResource<>();
        pages.setContent(Arrays.asList(roleDTO));

        given(roleService.getAllRoles(predicate, pageable)).willReturn(rolesPage);
        PageResultResource<RoleDTO> pageResultResource = roleFacade.getAllRoles(predicate,pageable);

        assertEquals(1, pageResultResource.getContent().size());
        assertEquals(roleDTO, pageResultResource.getContent().get(0));
    }
}
