package cz.muni.ics.kypo.commons.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.role.NewRoleDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotCreatedException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.model.Role;
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

import static cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons.ROLES_URL;
@Api(value = ApiEndpointsSecurityCommons.ROLES_URL,
        consumes = "application/json",
        authorizations = {
                @Authorization(value = "sampleoauth",
                        scopes = {
                                @AuthorizationScope(
                                        scope = "HTTP operations on roles resource",
                                        description = "allows operations on roles resource."
                                )
                        }
                )
        }
)
@RestController
@RequestMapping(path = ROLES_URL)
public class RoleController {

    private static Logger LOG = LoggerFactory.getLogger(RoleController.class);

    private RoleFacade roleFacade;
    private ObjectMapper objectMapper;

    @Autowired
    public RoleController(RoleFacade roleFacade, @Qualifier("objMapperRESTApi") ObjectMapper objectMapper) {
        this.roleFacade = roleFacade;
        this.objectMapper = objectMapper;
    }


    @ApiOperation(httpMethod = "GET",
            value = "Get role by role type.",
            response = RoleDTO.class,
            nickname = "findRoleByRoleType",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find role by role type",
                                            description = "allows returning role by role type."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRoleByRoleType(@ApiParam(name = "Role type") @RequestParam("roleType") String roleType,
                                                    @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                    @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findRoleByRoleType({},{})", roleType, fields);
        try {
            RoleDTO r = roleFacade.getByRoleType(roleType);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, r), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get roles.",
            response = RoleDTO.class,
            responseContainer = "Page",
            nickname = "findAllRoles",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find all roles",
                                            description = "allows returning roles."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllRoles(@QuerydslPredicate(root = Role.class) Predicate predicate, Pageable pageable,
                                              @RequestParam MultiValueMap<String, String> parameters,
                                              @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                          @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findAllRoles({})", fields);
        try {
            PageResultResource<RoleDTO> roleResource = roleFacade.getAllRoles(predicate, pageable);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, roleResource), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

}
