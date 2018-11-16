package cz.muni.ics.kypo.commons.rest.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.GroupsRefDTO;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
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

/**
 * @author Pavel Seda
 */
@Api(value = "/groups",
        consumes = "application/json"
)
@RestController
@RequestMapping(path = "/groups")
public class GroupsRestController {

    private static Logger LOG = LoggerFactory.getLogger(GroupsRestController.class);

    private IDMGroupRefFacade groupFacade;
    private RoleFacade roleFacade;
    private ObjectMapper objectMapper;

    @Autowired
    public GroupsRestController(IDMGroupRefFacade groupFacade, RoleFacade roleFacade, @Qualifier("objMapperRESTApi") ObjectMapper objectMapper) {
        this.roleFacade = roleFacade;
        this.objectMapper = objectMapper;
        this.groupFacade = groupFacade;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get all groups including their roles.",
            response = RoleDTO.class,
            responseContainer = "Page",
            nickname = "getAllGroups",
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllGroups(
            @QuerydslPredicate(root = IDMGroupRef.class) Predicate predicate,
            Pageable pageable,
            @RequestParam MultiValueMap<String, String> parameters,
            @ApiParam(value = "Fields which should be returned in REST API response", required = false)
            @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findRolesOfGroups({})", fields);
        try {
            PageResultResource<GroupsRefDTO> groupResource = groupFacade.getAllGroups(predicate, pageable);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, groupResource), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete group reference.",
            nickname = "deleteGroupReference",
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The requested resource was not found.")
    })
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGroupReference(
            @ApiParam(name = "Id of group whose reference to be deleted") @PathVariable("id") long id) {
        try {
            groupFacade.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Remove role from group reference.",
            nickname = "removeRoleFromGroupRef",
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "The requested resource was not modified.")
    })
    @DeleteMapping(value = "/{groupId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(httpMethod = "PUT",
            value = "Assign role to group reference.",
            nickname = "assignRoleToGroupRef",
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "The requested resource was not modified.")
    })
    @PutMapping(value = "/{groupId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignRoleToGroupRef(@ApiParam(name = "Role Id") @PathVariable long roleId,
                                                     @ApiParam(name = "IDMGroup Id") @PathVariable long groupId) {
        LOG.debug("assignRoleToGroupRef({},{})", roleId, groupId);
        try {
            roleFacade.assignRoleToGroup(roleId, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotModifiedException(ex.getLocalizedMessage());
        }
    }


}
