package cz.muni.ics.kypo.commons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.rest.controllers.GroupsRestController;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.rest.controllers.RoleRestController;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
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
import java.util.Optional;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class GroupsRefControllerTest {

    private MockMvc mockMvc;
    @Mock
    private RoleFacade roleFacade;
    @Mock
    private IDMGroupRefFacade groupRefFacade;
    private GroupsRestController groupsRefController;
    private RoleDTO roleDTO;
    private int page, size;

    @Before
    public void init() throws RuntimeException {
        MockitoAnnotations.initMocks(this);
        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        groupsRefController = new GroupsRestController(groupRefFacade, obj);

        page = 0;
        size = 10;

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setRoleType("GUEST");


        this.mockMvc = MockMvcBuilders.standaloneSetup(groupsRefController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                        new QuerydslPredicateArgumentResolver(new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE), Optional.empty()))
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void contextLoads() {
        Assert.assertNotNull(groupsRefController);
    }

    @Test
    public void deleteGroupRef() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups" + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(groupRefFacade).delete(ArgumentMatchers.anyLong());
        Exception exception = mockMvc.perform(MockMvcRequestBuilders.delete("/groups" + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResolvedException();
        Assert.assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    public void testRemoveRoleFromGroupRef() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups" + "/{groupId}/roles/{roleId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        then(groupRefFacade).should().removeRoleFromGroup(1L, 1L);
    }

    @Test
    public void testRemoveRoleFromGroupRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(groupRefFacade).removeRoleFromGroup(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        Exception exception = mockMvc.perform(
                MockMvcRequestBuilders.delete("/groups" + "/{groupId}/roles/{roleId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResolvedException();

        Assert.assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    private static String convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
