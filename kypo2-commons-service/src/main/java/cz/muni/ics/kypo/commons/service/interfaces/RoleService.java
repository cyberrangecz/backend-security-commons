package cz.muni.ics.kypo.commons.service.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;

public interface RoleService {

    /**
     * Creates given role
     *
     * @param role to be created
     * @return created role
     */
    Role create(Role role);

    /**
     * Deletes role with given role type
     * @param roleType to be deleted
     */
    void delete(String roleType);

    /**
     * Returns role by id
     *
     * @param id of role
     * @return role with given id
     * @throws CommonsServiceException if role could not be found
     */
    Role getById(long id) throws CommonsServiceException;

    /**
     * Return role by role type
     *
     * @param roleType of role
     * @return role with given roleType
     * @throws CommonsServiceException when role with given role type could not be found
     */
    Role getByRoleType(String roleType) throws CommonsServiceException;

    /**
     * Returns all roles
     *
     * @return all roles
     */
    Page<Role> getAllRoles(Predicate predicate, Pageable pageable);
}
