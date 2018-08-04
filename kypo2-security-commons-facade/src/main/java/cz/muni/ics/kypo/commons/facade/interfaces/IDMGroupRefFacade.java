package cz.muni.ics.kypo.commons.facade.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDMGroupRefFacade {

    /**
     * Gets IDMGroupRef with given group id from database.
     * @param id of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    public IDMGroupRefDTO getByIdmGroupId(long id) throws CommonsServiceException;

    /**
     * Find all IDMGroup ref.
     *
     * @return all a IDMGroups Ref
     */
    public PageResultResource<IDMGroupRefDTO> findAll(Predicate predicate, Pageable pageable);

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
     * Assign role to group.
     * @param groupRefId id of idm group ref to assign roles to
     * @param roleType type of role to be assigned to group
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO assignRoleToGroup(long groupRefId, String roleType) throws CommonsFacadeException;

    /**
     * Add users to group.
     * @param groupRefId id of group ref to assign users to
     * @param userRefLogins list of user refs logins to be added to group
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO addUsersToGroupRef(long groupRefId, List<String> userRefLogins) throws CommonsFacadeException;

    /**
     * Remove users from group.
     * @param groupRefId id of group ref to assign users to
     * @param userRefLogins list of user refs logins to be removed from group
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO removeUsersFromGroupRef(long groupRefId, List<String> userRefLogins) throws CommonsFacadeException;

    /**
     * Gets IDMGroupRef with given idm group id from database and with users ref as attribute.
     * @param groupRefId of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO getIDMGroupRefWithUsers(long groupRefId) throws CommonsFacadeException;

    /**
     * Gets IDMGroupRef with given idm group id from database and with roles as attribute.
     * @param groupRefId of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    IDMGroupRefDTO getIDMGroupRefWithRoles(long groupRefId) throws CommonsFacadeException;
}
