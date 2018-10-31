package cz.muni.ics.kypo.commons.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.rest.controllers.RoleController;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
public class RoleControllerTest {

	@Mock
	private RoleFacade roleFacade;

	private RoleController roleController;

	private MockMvc mockMvc;
	private RoleDTO roleDTO;
	private int page, size;
	private PageResultResource<RoleDTO> rolePageResultResource;

	@Before
	public void setup() throws RuntimeException {
		MockitoAnnotations.initMocks(this);
		ObjectMapper obj = new ObjectMapper();
		obj.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		roleController = new RoleController(roleFacade, obj);
		this.mockMvc = MockMvcBuilders.standaloneSetup(roleController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(), new QuerydslPredicateArgumentResolver(new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE),
						Optional.empty()))
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

		page = 0;
		size = 10;

		roleDTO = new RoleDTO();
		roleDTO.setId(1L);
		roleDTO.setRoleType("GUEST");

		rolePageResultResource = new PageResultResource<RoleDTO>(Arrays.asList(roleDTO));

	}

	@Test
	public void contextLoads() {
		Assert.assertNotNull(roleController);
	}

	@Test
	public void testGetRoleById() throws Exception {
		String valueAs = convertObjectToJsonBytes(roleDTO);
		BDDMockito.given(roleFacade.getById(ArgumentMatchers.anyLong())).willReturn(roleDTO);
		mockMvc.perform(MockMvcRequestBuilders.get(ApiEndpointsSecurityCommons.ROLES_URL + "/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(roleDTO))));
	}

	@Test
	public void testFindRoleByRoleTypeWithFacadeException() throws Exception {
		willThrow(CommonsFacadeException.class).given(roleFacade).getById(ArgumentMatchers.anyLong());
		Exception exception = mockMvc.perform(MockMvcRequestBuilders.get(ApiEndpointsSecurityCommons.ROLES_URL + "/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn().getResolvedException();
		Assert.assertEquals(ResourceNotFoundException.class, exception.getClass());
	}

	@Test
	public void testFindAllRoles() throws Exception {
		String valueAs = convertObjectToJsonBytes(rolePageResultResource);
		BDDMockito.given(roleFacade.getAllRoles(ArgumentMatchers.any(Predicate.class), ArgumentMatchers.any(Pageable.class))).willReturn(rolePageResultResource);

		mockMvc.perform(get(ApiEndpointsSecurityCommons.ROLES_URL))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(rolePageResultResource))));

	}

	@Test
	public void testAssignRoleToGroupRef() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/assign/to/{groupId}", 1L, 1L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		BDDMockito.then(roleFacade).should().assignRoleToGroup(1L,1L);
	}

	@Test
	public void testAssignRoleToGroupRefWithFacadeException() throws Exception {
		willThrow(CommonsFacadeException.class).given(roleFacade).assignRoleToGroup(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
		Exception exception = mockMvc.perform(
				MockMvcRequestBuilders.put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/assign/to/{groupId}", 1L, 1L)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotModified())
				.andReturn().getResolvedException();

		Assert.assertEquals(ResourceNotModifiedException.class, exception.getClass());
	}

	@Test
	public void testRemoveRoleFromGroupRef() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/remove/from/{groupId}", 1L, 1L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		BDDMockito.then(roleFacade).should().removeRoleFromGroup(1L,1L);
	}

	@Test
	public void testRemoveRoleFromGroupRefWithFacadeException() throws Exception {
		willThrow(CommonsFacadeException.class).given(roleFacade).removeRoleFromGroup(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
		Exception exception = mockMvc.perform(
				MockMvcRequestBuilders.put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/remove/from/{groupId}", 1L, 1L)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotModified())
				.andReturn().getResolvedException();

		Assert.assertEquals(ResourceNotModifiedException.class, exception.getClass());
	}

	@Test
	public void testGetRolesOfGroups() throws Exception {
		String valueAs = convertObjectToJsonBytes(new HashSet<>(Arrays.asList(roleDTO)));
		BDDMockito.given(roleFacade.getRolesOfGroups(Arrays.asList(1L))).willReturn(new HashSet<>(Arrays.asList(roleDTO)));

		mockMvc.perform(MockMvcRequestBuilders.get(ApiEndpointsSecurityCommons.ROLES_URL + "/of/groups")
				.content(convertObjectToJsonBytes(Arrays.asList(1L)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(new HashSet<>(Arrays.asList(roleDTO))))));

	}

	private static String convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper.writeValueAsString(object);
	}

}
