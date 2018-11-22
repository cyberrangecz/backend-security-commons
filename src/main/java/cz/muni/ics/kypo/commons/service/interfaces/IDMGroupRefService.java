package cz.muni.ics.kypo.commons.service.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author Pavel Seda & Dominik Pilar
 */
public interface IDMGroupRefService {

    /**
     * Deletes given group ref from database.
     *
     * @param id group id of group ref to be deleted
     * @throws CommonsServiceException
     */
    void delete(long id);

    Page<IDMGroupRef> getAllGroups(Predicate predicate, Pageable pageable);

    /**
     * Assign role to group.
     *
     * @param idmGroupId id of idm group ref to assign roles to
     * @param roleId     type of role to be assigned to group
     * @throws CommonsServiceException
     */
    void assignRoleToGroup(long roleId, long idmGroupId);

    /**
     * Returns set of roles of given groups
     *
     * @return roles
     */
    Set<Role> getRolesOfGroups(List<Long> groupsIds);

    /**
     * Remove given role from group and if the group no longer has role it is removed too.
     * @param roleId id of role to be removed
     * @param idmGroupId id of group to remove role from
     */
    void removeRoleFromGroup(long roleId, long idmGroupId);
}
