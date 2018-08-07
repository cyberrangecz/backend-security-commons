package cz.muni.ics.kypo.commons.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.user.NewUserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefWithGroupsDTO;
import cz.muni.ics.kypo.commons.controllers.UserRefController;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotCreatedException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.UserRefFacade;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRefController.class)
@ComponentScan(basePackages = "cz.muni.ics.kypo.commons")
public class UsersRefControllerTest {

    @Autowired
    private UserRefController userRefController;

    @MockBean
    private UserRefFacade userRefFacade;

    @MockBean
    @Qualifier("objMapperRESTApi")
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private UserRefDTO userRefDTO;
    private UserRefWithGroupsDTO userRefWithGroupsDTO;
    private NewUserRefDTO newUserRefDTO;
    private int page, size;
    private PageResultResource<UserRefDTO> userRefPageResultResource;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() throws RuntimeException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRefController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(), new QuerydslPredicateArgumentResolver(new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE),
                        Optional.empty()))
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

        page = 0;
        size = 10;

        userRefDTO = new UserRefDTO();
        userRefDTO.setId(1L);
        userRefDTO.setLogin("user1");

        newUserRefDTO = new NewUserRefDTO();
        newUserRefDTO.setLogin("user1");

        userRefPageResultResource = new PageResultResource<UserRefDTO>(Arrays.asList(userRefDTO));

        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        given(objectMapper.getSerializationConfig()).willReturn(obj.getSerializationConfig());

    }

    @Test
    public void contextLoads() {
        assertNotNull(userRefController);
    }

    @Test
    public void testCreateUserRef() throws Exception {
        String valueAs = convertObjectToJsonBytes(userRefDTO);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(userRefFacade.create(any(NewUserRefDTO.class))).willReturn(userRefDTO);
        mockMvc.perform(
                post(ApiEndpointsSecurityCommons.USERS_REF_URL)
                        .content(convertObjectToJsonBytes(newUserRefDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(userRefDTO))));

    }

    @Test
    public void testCreateUserRefWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(userRefFacade).create(any(NewUserRefDTO.class));
        Exception exception = mockMvc.perform(
                post(ApiEndpointsSecurityCommons.USERS_REF_URL)
                        .content(convertObjectToJsonBytes(newUserRefDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn().getResolvedException();

        assertEquals(ResourceNotCreatedException.class, exception.getClass());
    }

    @Test
    public void testFindUserRefByUserLogin() throws Exception {
        String valueAs = convertObjectToJsonBytes(userRefDTO);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(userRefFacade.getByLogin(anyString())).willReturn(userRefDTO);
        mockMvc.perform(get(ApiEndpointsSecurityCommons.USERS_REF_URL)
                .param("userLogin", "user1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(userRefDTO))));
    }

    @Test
    public void testFindUserRefByUserLoginWithFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(userRefFacade).getByLogin(anyString());
        Exception exception = mockMvc.perform(get(ApiEndpointsSecurityCommons.USERS_REF_URL)
                .param("userLogin", "user1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    public void testFindAllUserRef() throws Exception {
        String valueAs = convertObjectToJsonBytes(userRefPageResultResource);
        given(objectMapper.writeValueAsString(any(Object.class))).willReturn(valueAs);
        given(userRefFacade.getAllUserRef(any(Predicate.class),any(Pageable.class))).willReturn(userRefPageResultResource);

        mockMvc.perform(get(ApiEndpointsSecurityCommons.USERS_REF_URL+ "/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(convertObjectToJsonBytes(convertObjectToJsonBytes(userRefPageResultResource))));

    }

    @Test
    public void deleteUserRef() throws Exception {

        mockMvc.perform(delete(ApiEndpointsSecurityCommons.USERS_REF_URL)
                .param("userLogin", "user1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserRefFacadeException() throws Exception {
        willThrow(CommonsFacadeException.class).given(userRefFacade).delete(anyString());
        Exception exception = mockMvc.perform(delete(ApiEndpointsSecurityCommons.USERS_REF_URL)
                .param("userLogin", "user1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotModified())
                .andReturn().getResolvedException();
        assertEquals(ResourceNotModifiedException.class, exception.getClass());
    }

    private static String convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
