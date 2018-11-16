package cz.muni.ics.kypo.commons.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.rest.controllers.RoleRestController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
public class RoleControllerTest {

    @Mock
    private RoleFacade roleFacade;

    private RoleRestController roleController;

    private MockMvc mockMvc;
    private RoleDTO roleDTO;
    private int page, size;
    private PageResultResource<RoleDTO> rolePageResultResource;

    @Before
    public void setup() throws RuntimeException {
        MockitoAnnotations.initMocks(this);
        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        roleController = new RoleRestController(roleFacade, obj);
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
        mockMvc.perform(get("/roles" + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(roleDTO))));
    }

    @Test
    public void testFindRoleByRoleTypeWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(roleFacade).getById(ArgumentMatchers.anyLong());
        Exception exception = mockMvc.perform(get("/roles" + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResolvedException();
        Assert.assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    public void testFindAllRoles() throws Exception {
        String valueAs = convertObjectToJsonBytes(rolePageResultResource);
        BDDMockito.given(roleFacade.getAllRoles(ArgumentMatchers.any(Predicate.class), ArgumentMatchers.any(Pageable.class))).willReturn(rolePageResultResource);

        mockMvc.perform(get("/roles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(rolePageResultResource))));

    }

    private static String convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper.writeValueAsString(object);
    }

}
