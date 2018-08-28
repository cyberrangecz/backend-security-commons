package cz.muni.ics.kypo.commons.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.controllers.RoleController;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoleController.class)
@ComponentScan(basePackages = "cz.muni.ics.kypo.commons")
public class RoleControllerTest {

    @Autowired
    private RoleController roleController;

    @MockBean
    private RoleFacade roleFacade;

    @MockBean
    @Qualifier("objMapperRESTApi")
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RoleDTO roleDTO;
    private int page, size;
    private PageResultResource<RoleDTO> rolePageResultResource;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() throws RuntimeException {
        MockitoAnnotations.initMocks(this);
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

        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        given(objectMapper.getSerializationConfig()).willReturn(obj.getSerializationConfig());

    }

    @Test
    public void contextLoads() {
        assertNotNull(roleController);
    }

    @Test
    public void testFindRoleByRoleType() throws Exception {
        String valueAs = convertObjectToJsonBytes(roleDTO);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(roleFacade.getByRoleType(anyString())).willReturn(roleDTO);
        mockMvc.perform(get(ApiEndpointsSecurityCommons.ROLES_URL)
                .param("roleType", "GUEST")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(roleDTO))));
    }

    @Test
    public void testFindRoleByRoleTypeWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(roleFacade).getByRoleType(anyString());
        Exception exception = mockMvc.perform(get(ApiEndpointsSecurityCommons.ROLES_URL)
                .param("roleType", "GUEST")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    public void testFindAllRoles() throws Exception {
        String valueAs = convertObjectToJsonBytes(rolePageResultResource);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(roleFacade.getAllRoles(any(Predicate.class),any(Pageable.class))).willReturn(rolePageResultResource);

        mockMvc.perform(get(ApiEndpointsSecurityCommons.ROLES_URL + "/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(rolePageResultResource))));

    }

    @Test
    public void testAssignRoleToGroupRef() throws Exception {
        mockMvc.perform(put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/assign/to/{groupId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        then(roleFacade).should().assignRoleToGroup(1L,1L);
    }

    @Test
    public void testAssignRoleToGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(roleFacade).assignRoleToGroup(anyLong(), anyLong());
        Exception exception = mockMvc.perform(
                put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/assign/to/{groupId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotModified())
                .andReturn().getResolvedException();

        assertEquals(ResourceNotModifiedException.class, exception.getClass());
    }

    @Test
    public void testRemoveRoleFromGroupRef() throws Exception {
        mockMvc.perform(put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/cancel/to/{groupId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        then(roleFacade).should().removeRoleFromGroup(1L,1L);
    }

    @Test
    public void testRemoveRoleFromGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(roleFacade).removeRoleFromGroup(anyLong(), anyLong());
        Exception exception = mockMvc.perform(
                put(ApiEndpointsSecurityCommons.ROLES_URL + "/{roleId}/cancel/to/{groupId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotModified())
                .andReturn().getResolvedException();

        assertEquals(ResourceNotModifiedException.class, exception.getClass());
    }

    @Test
    public void testGetRolesOfGroups() throws Exception {
        String valueAs = convertObjectToJsonBytes(new HashSet<>(Arrays.asList(roleDTO)));
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(roleFacade.getRolesOfGroups(Arrays.asList(1L))).willReturn(new HashSet<>(Arrays.asList(roleDTO)));

        mockMvc.perform(get(ApiEndpointsSecurityCommons.ROLES_URL + "/of")
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
