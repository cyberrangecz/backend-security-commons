package cz.muni.ics.kypo.commons.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
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

import java.util.List;
import java.util.Set;

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
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRoleById(@ApiParam(name = "Role type") @PathVariable("id") Long roleId,
                                                    @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                    @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findRoleByRoleType({},{})", roleId, fields);
        try {
            RoleDTO r = roleFacade.getById(roleId);
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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


    @ApiOperation(httpMethod = "PUT",
            value = "Assign role to group reference.",
            nickname = "assignRoleToGroupRef",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "assign role to group reference",
                                            description = "allows returning group reference by IDMGroup Id."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "The requested resource was not modified.")
    })
    @PutMapping(value = "/{roleId}/assign/to/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignRoleToGroupRef(@ApiParam(name = "Role Id") @PathVariable long roleId,
                                                       @ApiParam(name = "IDMGroup Id") @PathVariable long groupId,
                                                       @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                       @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("assignRoleToGroupRef({},{},{})", roleId, groupId, fields);
        try {
            roleFacade.assignRoleToGroup(roleId, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get roles of groups.",
            response = RoleDTO.class,
            responseContainer = "Page",
            nickname = "getRolesOfGroups",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find set of roles of given groups",
                                            description = "allows returning groups references."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(value = "/of/groups" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRolesOfGroups(
            @ApiParam(value = "Fields which should be returned in REST API response", required = false) @RequestParam(value = "fields", required = false) String fields,
            @ApiParam(value = "List of groups ids to get roles", required = true) @RequestBody List<Long> groupsIds)
    {
        LOG.debug("findRolesOfGroups({},{})", groupsIds, fields);
        try {
            Set<RoleDTO> roles = roleFacade .getRolesOfGroups(groupsIds);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, roles), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Remove role from group reference.",
            nickname = "removeRoleFromGroupRef",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "remove role from group reference",
                                            description = "allows returning group reference by IDMGroup Id."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "The requested resource was not modified.")
    })
    @PutMapping(value = "/{roleId}/remove/from/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeRoleFromGroupRef(@ApiParam(name = "Role Id") @PathVariable long roleId,
                                                       @ApiParam(name = "IDMGroup Id") @PathVariable long groupId,
                                                       @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                       @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("removeRoleFromGroupRef({},{},{})", roleId, groupId, fields);
        try {
            roleFacade.removeRoleFromGroup(roleId, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }

}
