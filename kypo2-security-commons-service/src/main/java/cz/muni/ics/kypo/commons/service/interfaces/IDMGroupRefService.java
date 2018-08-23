package cz.muni.ics.kypo.commons.service.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IDMGroupRefService {

    /**
     * Gets IDMGroupRef with given group id from database.
     * @param id of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    public IDMGroupRef getByIdmGroupId(long id) throws CommonsServiceException;

    /**
     * Find all IDMGroup ref.
     *
     * @return all a IDMGroups Ref
     */
    public Page<IDMGroupRef> findAll(Predicate predicate, Pageable pageable);

    /**
     * Creates given group ref in database.
     * @param idmGroupRef group ref to be created
     * @throws CommonsServiceException
     */
    IDMGroupRef create(IDMGroupRef idmGroupRef);

    /**
     * Deletes given group ref from database.
     * @param id group id of group ref to be deleted
     * @throws CommonsServiceException
     */
    void delete(long id);

    /**
     * Assign role to group.
     * @param groupRefId id of idm group ref to assign roles to
     * @param roleType type of role to be assigned to group
     * @throws CommonsServiceException
     */
    IDMGroupRef assignRoleToGroup(long groupRefId, String roleType) throws CommonsServiceException;

    /**
     * Gets IDMGroupRef with given idm group id from database and with roles as attribute.
     * @param groupRefId of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    IDMGroupRef getIDMGroupRefWithRoles(long groupRefId) throws CommonsServiceException;

    /**
     * Returns set of roles of given groups
     *
     * @return roles
     */
    Set<Role> getRolesOfGroups(List<Long> groupsIds) ;

}
