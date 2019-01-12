package cz.muni.ics.kypo.commons.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotFoundException;
import cz.muni.ics.kypo.commons.rest.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.persistence.model.Role;
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

import java.util.*;

@Api(value = "/roles", consumes = "application/json", tags = "Roles")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Full authentication is required to access this resource.")
})
@RestController
@RequestMapping(path = "/roles")
public class RoleRestController {

    private static Logger LOG = LoggerFactory.getLogger(RoleRestController.class);

    private RoleFacade roleFacade;
    private ObjectMapper objectMapper;

    @Autowired
    public RoleRestController(RoleFacade roleFacade, ObjectMapper objectMapper) {
        this.roleFacade = roleFacade;
        this.objectMapper = objectMapper;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get role by Id.",
            response = RoleDTO.class,
            nickname = "findRoleById",
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role loaded.", response = RoleDTO.class),
            @ApiResponse(code = 404, message = "Role cannot be found."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRoleById(@ApiParam(value = "Role Id") @PathVariable("id") Long roleId,
                                              @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                              @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("getRoleById({},{})", roleId, fields);
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
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All roles found.", response = RoleDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllRoles(@QuerydslPredicate(root = Role.class) Predicate predicate, Pageable pageable,
                                              @RequestParam(required = false) MultiValueMap<String, String> parameters,
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

    @ApiOperation(httpMethod = "GET",
            value = "Get roles of given groups.",
            response = RoleDTO.class,
            responseContainer = "Page",
            nickname = "findRolesOfGivenGroups",
            produces = "application/json"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Roles of given groups found.", response = RoleDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.")
    })
    @GetMapping(value = "/roles-of-groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRolesOfGroups(
            @ApiParam(value = "List of groups ids to get roles", required = true) @RequestParam(name = "ids") List<Long> groupsIds,
            @ApiParam(value = "Fields which should be returned in REST API response", required = false)
            @RequestParam(value = "fields", required = false) String fields) {
        LOG.debug("findRolesOfGroups({},{})", groupsIds, fields);
        try {
            Set<RoleDTO> roles = roleFacade .getRolesOfGroups(groupsIds);
            Squiggly.init(objectMapper, fields);
            return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, roles), HttpStatus.OK);
        } catch (CommonsFacadeException ex) {
            throw new ResourceNotFoundException(ex.getLocalizedMessage());
        }

    }

}
