package cz.muni.ics.kypo.commons.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.commons.ApiEndpointsSecurityCommons;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.ResourceNotModifiedException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete group reference.",
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


}
