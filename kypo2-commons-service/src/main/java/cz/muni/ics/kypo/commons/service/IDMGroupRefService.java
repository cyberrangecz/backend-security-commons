package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.Optional;

public interface IDMGroupRefService {

    /**
     * Gets IDMGroupRef with given group id from database.
     * @param id of the IDMGroup whose reference should be loaded
     * @return IDMGroup with given groupId
     * @throws CommonsServiceException
     */
    public Optional<IDMGroupRef> findByIdmGroupId(long id);

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
    Optional<IDMGroupRef> create(IDMGroupRef idmGroupRef);

    /**
     * Updates given group ref in database.
     * @param idmGroupRef group ref to be updated
     * @throws CommonsServiceException
     */
    Optional<IDMGroupRef> update(IDMGroupRef idmGroupRef);

    /**
     * Deletes given group ref from database.
     * @param idmGroupRef group ref to be deleted
     * @throws CommonsServiceException
     */
    void delete(IDMGroupRef idmGroupRef);

    /**
     * Assign role to group.
     * @param idmGroupRef idm group ref to assign roles to
     * @param roles list of roles to be assigned to group
     * @throws CommonsServiceException
     */
    Optional<IDMGroupRef> assignRolesToGroup(IDMGroupRef idmGroupRef, Set<Role> roles);

    /**
     * Add users to group.
     * @param idmGroupRef idm group ref to assign users to
     * @param userRefList list of users to be added to group
     * @throws CommonsServiceException
     */
    Optional<IDMGroupRef> addUsersToGroup(IDMGroupRef idmGroupRef, List<UserRef> userRefList);
}
