package cz.muni.ics.kypo.commons.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotCreatedException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons.GROUPS_REF_URL;

@Api(value = ApiEndpointsSecurityCommons.GROUPS_REF_URL,
        consumes = "application/json",
        authorizations = {
                @Authorization(value = "sampleoauth",
                        scopes = {
                                @AuthorizationScope(
                                        scope = "HTTP operations on groups references resource",
                                        description = "allows operations on groups references resource."
                                )
                        }
                )
        }
)
@RestController
@RequestMapping(path = ApiEndpointsSecurityCommons.GROUPS_REF_URL)
public class GroupsRefController {

    private static Logger LOG = LoggerFactory.getLogger(GroupsRefController.class);

    private IDMGroupRefFacade groupFacade;
    private ObjectMapper objectMapper;

    @Autowired
    public GroupsRefController(IDMGroupRefFacade groupFacade, @Qualifier("objMapperRESTApi") ObjectMapper objectMapper) {
        this.groupFacade = groupFacade;
        this.objectMapper = objectMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create group reference.",
            response = IDMGroupRefDTO.class,
            nickname = "createGroupRef",
            produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "create group reference",
                                            description = "allows returning created group reference."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createGroupReference(@ApiParam(name = "Group reference to be created") @RequestBody NewGroupRefDTO groupRefDTO,
                                                        @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                        @RequestParam(value = "fields", required = false) String fields) {
        LOG.info("Creating group reference for group with id: " + groupRefDTO.getIdmGroupId());
        try {
            IDMGroupRefDTO gR = groupFacade.create(groupRefDTO);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, gR), HttpStatus.CREATED);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotCreatedException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get group reference by IDMGroup ID.",
            response = IDMGroupRefDTO.class,
            nickname = "findGroupRefByGroupId",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find group reference by IDMGroup Id",
                                            description = "allows returning group reference by IDMGroup Id."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findGroupRefByGroupId(@ApiParam(name = "IDMGroup Id") @PathVariable long groupId,
                                                          @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                          @RequestParam(value = "fields", required = false) String fields) {

        LOG.debug("findGroupRefByGroupId({},{})", groupId, fields);
        try {
            IDMGroupRefDTO gR = groupFacade.getByIdmGroupId(groupId);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, gR), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get all groups references.",
            response = IDMGroupRefDTO.class,
            responseContainer = "Page",
            nickname = "findAllGroupsReferences",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "find all groups references",
                                            description = "allows returning groups references."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllGroupsReferences(@QuerydslPredicate(root = IDMGroupRef.class) Predicate predicate, Pageable pageable,
                                                          @RequestParam MultiValueMap<String, String> parameters,
                                                          @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                          @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findAllGroupsReferences({})", fields);
        try {
            PageResultResource<IDMGroupRefDTO> groupRefResource = groupFacade.findAll(predicate, pageable);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, groupRefResource), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete group reference.",
            response = IDMGroupRefDTO.class,
            nickname = "deleteGroupReference",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "delete group reference",
                                            description = "allows deleting group reference."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGroupReference(@ApiParam(name = "Id of group whose reference to be deleted") @RequestParam("idmGroupId") long id) {
        try {
            groupFacade.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }

    }

    @ApiOperation(httpMethod = "PUT",
            value = "Assign role to group reference.",
            response = IDMGroupRefDTO.class,
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
    @PutMapping(value = "/{groupId}/assignRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> assignRoleToGroupRef(@ApiParam(name = "IDMGroup Id") @PathVariable long groupId,
                                                        @ApiParam(value = "Role type of role which should be assigned to group.") @RequestParam("roleType") String roleType,
                                                        @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                        @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("assignRoleToGroupRef({},{},{})", groupId, roleType, fields);
        try {
            IDMGroupRefDTO gR = groupFacade.assignRoleToGroup(groupId, roleType);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, gR), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Add users to group reference.",
            response = IDMGroupRefDTO.class,
            nickname = "addUsersToGroupRef",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "add users to group reference",
                                            description = "allows returning group reference by IDMGroup Id."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @PutMapping(value = "/{groupId}/addUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUsersToGroupRef(@ApiParam(name = "IDMGroup Id") @PathVariable long groupId,
                                                       @ApiParam(value = "List of users logins which should be added to group.") @RequestBody List<String> userLogins,
                                                       @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                       @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("assignRoleToGroupRef({},{},{})", groupId, userLogins, fields);
        try {
            IDMGroupRefDTO gR = groupFacade.addUsersToGroupRef(groupId, userLogins);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, gR), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Remove users from group reference.",
            response = IDMGroupRefDTO.class,
            nickname = "removeUsersFromGroupRef",
            produces = "application/json",
            authorizations = {
                    @Authorization(value = "sampleoauth",
                            scopes = {
                                    @AuthorizationScope(
                                            scope = "remove users from group reference",
                                            description = "allows returning group reference by IDMGroup Id."
                                    )
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @PutMapping(value = "/{groupId}/removeUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> removeUsersFromGroupRef(@ApiParam(name = "IDMGroup Id") @PathVariable long groupId,
                                                     @ApiParam(value = "List of users logins which should be removed from group.") @RequestBody List<String> userLogins,
                                                     @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                     @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("assignRoleToGroupRef({},{},{})", groupId, userLogins, fields);
        try {
            IDMGroupRefDTO gR = groupFacade.removeUsersFromGroupRef(groupId, userLogins);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, gR), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }
}
