package cz.muni.ics.kypo.commons.facade.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IDMGroupRefFacade {

    /**
     * Creates given group ref in database.
     * @param newGroupRefDTO group ref to be created
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO create(NewGroupRefDTO newGroupRefDTO);

    /**
     * Deletes given group ref from database.
     * @param groupRefId group ref to be deleted
     * @throws CommonsServiceException
     */
    void delete(Long groupRefId);

    /**
     * Gets IDMGroupRef with given group id from database.
     * @param id of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO getByIdmGroupId(long id) throws CommonsServiceException;

    /**
     * Find all IDMGroup ref.
     *
     * @return all a IDMGroups Ref
     */
    PageResultResource<IDMGroupRefDTO> findAll(Predicate predicate, Pageable pageable);


    /**
     * Assign role to group.
     * @param groupRefId id of idm group ref to assign roles to
     * @param roleType type of role to be assigned to group
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO assignRoleToGroup(long groupRefId, String roleType) throws CommonsFacadeException;

    /**
     * Returns set of roles of given groups
     *
     * @return roles
     */
    Set<RoleDTO> getRolesOfGroups(List<Long> groupsIds) ;

}
