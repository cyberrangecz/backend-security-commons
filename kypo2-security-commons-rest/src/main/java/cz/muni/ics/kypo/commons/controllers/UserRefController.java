package cz.muni.ics.kypo.commons.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.api.dto.user.NewUserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotCreatedException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.UserRefFacade;
import cz.muni.ics.kypo.commons.model.UserRef;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons.USERS_REF_URL;

@RestController
@RequestMapping(path = USERS_REF_URL)
@Api(value = "Endpoint for users references")
public class UserRefController {
    private static Logger LOG = LoggerFactory.getLogger(RoleController.class);

    private UserRefFacade userRefFacade;
    private ObjectMapper objectMapper;

    @Autowired
    public UserRefController(UserRefFacade userRefFacade, @Qualifier("objMapperRESTApi") ObjectMapper objectMapper) {
        this.userRefFacade = userRefFacade;
        this.objectMapper = objectMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create user reference.",
            response = UserRefDTO.class,
            nickname = "createRole",
            produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "create user reference",
                                            description = "allows returning created user reference."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUserRef(@ApiParam(name = "User reference to be created") @RequestBody NewUserRefDTO userRefDTO,
                                             @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                             @RequestParam(value = "fields", required = false) String fields) {
        LOG.info("Creating user reference ({}, {})", userRefDTO, fields);
        try {
            UserRefDTO uR = userRefFacade.create(userRefDTO);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, uR), HttpStatus.CREATED);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotCreatedException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get user reference by user login.",
            response = RoleDTO.class,
            nickname = "findUserRefByUserLogin",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find user reference by user login",
                                            description = "allows returning user reference by user login."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserRefByUserLogin(@ApiParam(name = "Login of user") @RequestParam("userLogin") String userLogin,
                                                    @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                    @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("getUserRefByLogin({},{})", userLogin, fields);
        try {
            UserRefDTO uR = userRefFacade.getByLogin(userLogin);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, uR), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get users references.",
            response = RoleDTO.class,
            responseContainer = "Page",
            nickname = "findAllUserRef",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find all users references",
                                            description = "allows returning users references."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllUserRef(@QuerydslPredicate(root = UserRef.class) Predicate predicate, Pageable pageable,
                                                @RequestParam MultiValueMap<String, String> parameters,
                                                @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                              @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findAllUserRef({})", fields);
        try {
            PageResultResource<UserRefDTO> userRefResource = userRefFacade.getAllUserRef(predicate, pageable);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, userRefResource), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete user reference.",
            response = RoleDTO.class,
            nickname = "deleteUserRef",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "delete user reference",
                                            description = "allows deleting user reference."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGroupReference(@ApiParam(name = "User login whose user reference should be deleted.") @RequestParam("userLogin") String userLogin) {
        LOG.debug("Deleting user reference ({})", userLogin);
        try {
            userRefFacade.delete(userLogin);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }
}
