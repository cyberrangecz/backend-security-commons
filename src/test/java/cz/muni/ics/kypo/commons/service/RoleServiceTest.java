package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import cz.muni.ics.kypo.commons.persistence.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.persistence.repository.RoleRepository;
import cz.muni.ics.kypo.commons.service.impl.RoleServiceImpl;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class RoleServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private RoleService roleService;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	IDMGroupRefRepository groupRefRepository;

	private Role role1, role2;
	private IDMGroupRef groupRef1, groupRef2;

	private Pageable pageable;
	private Predicate predicate;


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		roleService = new RoleServiceImpl(roleRepository, groupRefRepository);
		role1 = new Role();
		role1.setId(1L);
		role1.setRoleType("ADMINISTRATOR");

		role2 = new Role();
		role2.setId(2L);
		role2.setRoleType("GUEST");

		groupRef1 = new IDMGroupRef();
		groupRef1.setId(1L);
		groupRef1.setIdmGroupId(5L);

		groupRef2 = new IDMGroupRef();
		groupRef2.setId(2L);
		groupRef2.setIdmGroupId(2L);
		groupRef2.setRoles(new HashSet<>(Arrays.asList(role1)));


		pageable = PageRequest.of(0, 10);
	}

	@Test
	public void testGetById() {
		BDDMockito.given(roleRepository.findById(role1.getId())).willReturn(Optional.of(role1));

		Role r = roleService.getById(role1.getId());
		assertEquals(role1.getId(), r.getId());
	}

	@Test
	public void testGetByIdNotFound() {
		thrown.expect(CommonsServiceException.class);
		thrown.expectMessage("Role with id " + role1.getId() + " could not be found");

		BDDMockito.given(roleRepository.findById(role1.getId())).willReturn(Optional.empty());
			roleService.getById(role1.getId());
	}

	@Test
	public void testFindByRoleType() {
		BDDMockito.given(roleRepository.findByRoleType(role1.getRoleType())).willReturn(Optional.of(role1));

		Role r = roleService.getByRoleType(role1.getRoleType());
		assertEquals(role1.getRoleType(), r.getRoleType());
	}

	@Test
	public void testGetByRoleTypeNotFound() {
		thrown.expect(CommonsServiceException.class);
		thrown.expectMessage("Role with role type " + role1.getRoleType() + " could not be found");

		BDDMockito.given(roleRepository.findByRoleType(role1.getRoleType())).willReturn(Optional.empty());
		roleService.getByRoleType(role1.getRoleType());
	}

	@Test
	public void testGetByRoleTypeWithEmptyParameterRoleType() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Input role type must not be null");
		roleService.getByRoleType("");
	}

	@Test
	public void testGetAllRoles() {
		BDDMockito.given(roleRepository.findAll(predicate, pageable))
						.willReturn(new PageImpl<>(Arrays.asList(role1, role2)));

		Role role3 = new Role();
		role3.setId(4L);

		List<Role> roles = roleService.getAllRoles(predicate, pageable).getContent();
		Assert.assertEquals(2, roles.size());
		Assert.assertTrue(roles.contains(role1));
		Assert.assertTrue(roles.contains(role2));
		Assert.assertFalse(roles.contains(role3));

		BDDMockito.then(roleRepository).should().findAll(predicate, pageable);
	}

	@Test
	public void testAssignRoleToGroup() {
		BDDMockito.given(roleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(role1));
		BDDMockito.given(groupRefRepository.findByIdmGroupId(ArgumentMatchers.anyLong())).willReturn(Optional.of(groupRef1));
		groupRef1.addRole(role1);
		BDDMockito.given(groupRefRepository.save(groupRef1)).willReturn(groupRef1);
		roleService.assignRoleToGroup(groupRef1.getId(), 1L);

		BDDMockito.then(groupRefRepository).should().save(groupRef1);
}

	@Test
	public void testAssignRoleToGroupWithRoleNotFound() {
		thrown.expect(CommonsServiceException.class);
		thrown.expectMessage("Input role with id " + role1.getId() + " cannot be found");
		BDDMockito.given(roleRepository.findById(1L)).willReturn(Optional.empty());
		roleService.assignRoleToGroup(1L, 1L);

	}

	@Test
	public void testAssignRoleToGroupWithGroupRefNotFound() {
		BDDMockito.given(roleRepository.findById(1L)).willReturn(Optional.of(role1));
		BDDMockito.given(groupRefRepository.findByIdmGroupId(ArgumentMatchers.anyLong())).willReturn(Optional.empty());
		roleService.assignRoleToGroup(1L, 1L);

		BDDMockito.then(groupRefRepository).should().save(ArgumentMatchers.any(IDMGroupRef.class));
	}

	@Test
	public void testRemoveRoleFromGroup() {
		BDDMockito.given(roleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(role1));
		groupRef2.addRole(role2);
		BDDMockito.given(groupRefRepository.findByIdmGroupId(ArgumentMatchers.anyLong())).willReturn(Optional.of(groupRef2));
		roleService.removeRoleFromGroup(1L, 2L);

		BDDMockito.then(groupRefRepository).should().save(groupRef2);
	}

	@Test
	public void testRemoveRoleFromGroupAndDelete() {
		BDDMockito.given(roleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(role1));
		BDDMockito.given(groupRefRepository.findByIdmGroupId(ArgumentMatchers.anyLong())).willReturn(Optional.of(groupRef2));
		groupRef2.removeRole(role1);
		roleService.removeRoleFromGroup(1L, 2L);

		BDDMockito.then(groupRefRepository).should().delete(groupRef2);
	}

	@Test
	public void testRemoveRoleFromGroupWithRoleNotFound() {
		thrown.expect(CommonsServiceException.class);
		thrown.expectMessage("Input role with id " + role1.getId() + " cannot be found");
		BDDMockito.given(roleRepository.findById(1L)).willReturn(Optional.empty());
		roleService.removeRoleFromGroup(1L, 1L);

	}

	@Test
	public void testRemoveRoleFromGroupWithGroupRefNotFound() {
		thrown.expectMessage("Idm group with id: " + 1L + " cannot be found.");
		thrown.expect(CommonsServiceException.class);
		BDDMockito.given(roleRepository.findById(1L)).willReturn(Optional.of(role1));
		BDDMockito.given(groupRefRepository.findByIdmGroupId(ArgumentMatchers.anyLong())).willReturn(Optional.empty());
		roleService.removeRoleFromGroup(1L, 1L);
	}

	@Test
	public void testGetRolesOfGroups() {
		groupRef1.addRole(role2);
		BDDMockito.given(groupRefRepository.findByIdmGroupId(1L)).willReturn(Optional.of(groupRef1));
		BDDMockito.given(groupRefRepository.findByIdmGroupId(2L)).willReturn(Optional.of(groupRef2));
		Set<Role> roles = roleService.getRolesOfGroups(Arrays.asList(1L, 2L));

		Assert.assertTrue(roles.contains(role1));
		Assert.assertTrue(roles.contains(role2));
		Assert.assertTrue(roles.size() == 2);
	}




}
