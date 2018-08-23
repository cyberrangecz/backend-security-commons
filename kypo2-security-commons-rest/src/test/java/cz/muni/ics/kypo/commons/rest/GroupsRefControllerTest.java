package cz.muni.ics.kypo.commons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.controllers.GroupsRefController;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotCreatedException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GroupsRefController.class)
@ComponentScan(basePackages = "cz.muni.ics.kypo.commons")
public class GroupsRefControllerTest {

    @InjectMocks
    private GroupsRefController groupsRefController;

    @MockBean
    private IDMGroupRefFacade groupRefFacade;

    @MockBean
    @Qualifier("objMapperRESTApi")
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private IDMGroupRefDTO groupDTO1;
    private NewGroupRefDTO newGroupDTO;
    private RoleDTO roleDTO;
    private int page, size;
    private PageResultResource<IDMGroupRefDTO> groupRefPageResultResource;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() throws RuntimeException {
        MockitoAnnotations.initMocks(this);
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(groupsRefController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(), new QuerydslPredicateArgumentResolver(new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE),
                        Optional.empty()))
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

        page = 0;
        size = 10;

        groupDTO1 = new IDMGroupRefDTO();
        groupDTO1.setId(1L);
        groupDTO1.setIdmGroupId(1L);

        newGroupDTO = new NewGroupRefDTO();
        newGroupDTO.setIdmGroupId(3L);

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setRoleType("GUEST");

        Set<RoleDTO> roles = new HashSet<>();
        roles.add(roleDTO);
        groupDTO1.setRoles(roles);

        groupRefPageResultResource = new PageResultResource<>(Arrays.asList(groupDTO1));

        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        given(objectMapper.getSerializationConfig()).willReturn(obj.getSerializationConfig());

    }

    @Test
    public void contextLoads() {
        assertNotNull(groupsRefController);
    }

    @Test
    public void testCreateGroupRef() throws Exception {
        String valueAs = convertObjectToJsonBytes(groupDTO1);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(groupRefFacade.create(any(NewGroupRefDTO.class))).willReturn(groupDTO1);
        mockMvc.perform(
                post(ApiEndpointsSecurityCommons.GROUPS_REF_URL)
                        .content(convertObjectToJsonBytes(newGroupDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(groupDTO1))));

    }

    @Test
    public void testCreateGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(groupRefFacade).create(any(NewGroupRefDTO.class));
        Exception exception = mockMvc.perform(
                post(ApiEndpointsSecurityCommons.GROUPS_REF_URL)
                        .content(convertObjectToJsonBytes(newGroupDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResolvedException();

        assertEquals(ResourceNotCreatedException.class, exception.getClass());
    }

    @Test
    public void testFindGroupRefByGroupId() throws Exception {
        String valueAs = convertObjectToJsonBytes(groupDTO1);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(groupRefFacade.getByIdmGroupId(anyLong())).willReturn(groupDTO1);
        mockMvc.perform(get(ApiEndpointsSecurityCommons.GROUPS_REF_URL + "/{groupId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(groupDTO1))));
    }

    @Test
    public void testFindGroupRefByGroupIdWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(groupRefFacade).getByIdmGroupId(anyLong());
        Exception exception = mockMvc.perform(get(ApiEndpointsSecurityCommons.GROUPS_REF_URL + "/{groupId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    public void testFindAllGroupsReferences() throws Exception {
        String valueAs = convertObjectToJsonBytes(groupRefPageResultResource);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(groupRefFacade.findAll(any(Predicate.class),any(Pageable.class))).willReturn(groupRefPageResultResource);

        MockHttpServletResponse result = mockMvc.perform(get(ApiEndpointsSecurityCommons.GROUPS_REF_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(convertObjectToJsonBytes(convertObjectToJsonBytes(groupRefPageResultResource)), result.getContentAsString());

    }

    @Test
    public void deleteGroupRef() throws Exception {

        mockMvc.perform(delete(ApiEndpointsSecurityCommons.GROUPS_REF_URL)
                .param("idmGroupId", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(groupRefFacade).delete(anyLong());
        Exception exception = mockMvc.perform(delete(ApiEndpointsSecurityCommons.GROUPS_REF_URL)
                .param("idmGroupId", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotModified())
                .andReturn().getResolvedException();
        assertEquals(ResourceNotModifiedException.class, exception.getClass());
    }

    @Test
    public void testAssignRoleToGroupRef() throws Exception {
        RoleDTO newRoleDTO = new RoleDTO();
        newRoleDTO.setId(2L);
        newRoleDTO.setRoleType("ADMINISTRATOR");
        Set<RoleDTO> rolesDTO = groupDTO1.getRoles();
        rolesDTO.add(newRoleDTO);
        groupDTO1.setRoles(rolesDTO);

        String valueAs = convertObjectToJsonBytes(groupDTO1);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(groupRefFacade.assignRoleToGroup(anyLong(), anyString())).willReturn(groupDTO1);

        mockMvc.perform(put(ApiEndpointsSecurityCommons.GROUPS_REF_URL + "/{groupId}/assignRole", 1L)
                .param("roleType", "GUEST")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(groupDTO1))));

    }

    @Test
    public void testAssignRoleToGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(groupRefFacade).assignRoleToGroup(anyLong(), anyString());
        Exception exception = mockMvc.perform(
                put(ApiEndpointsSecurityCommons.GROUPS_REF_URL + "/{groupId}/assignRole", 1L)
                        .param("roleType", "GUEST")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotModified())
                .andReturn().getResolvedException();

        assertEquals(ResourceNotModifiedException.class, exception.getClass());
    }

    @Test
    public void testGetRolesOfGroups() throws Exception {
        String valueAs = convertObjectToJsonBytes(new HashSet<>(Arrays.asList(roleDTO)));
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(groupRefFacade.getRolesOfGroups(Arrays.asList(1L))).willReturn(new HashSet<>(Arrays.asList(roleDTO)));

        mockMvc.perform(get(ApiEndpointsSecurityCommons.GROUPS_REF_URL + "/rolesOf")
                .content(convertObjectToJsonBytes(Arrays.asList(1L)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(new HashSet<>(Arrays.asList(roleDTO))))));

    }


    private static String convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
