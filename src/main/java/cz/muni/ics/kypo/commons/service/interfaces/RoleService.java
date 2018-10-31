package cz.muni.ics.kypo.commons.service.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;

import java.util.List;
import java.util.Set;

public interface RoleService {

	/**
	 * Returns role by id
	 *
	 * @param id of role
	 * @return role with given id
	 * @throws CommonsServiceException if role could not be found
	 */
	Role getById(long id);

	/**
	 * Return role by role type
	 *
	 * @param roleType of role
	 * @return role with given roleType
	 * @throws CommonsServiceException when role with given role type could not be found
	 */
	Role getByRoleType(String roleType);

	/**
	 * Returns all roles
	 *
	 * @return all roles
	 */
	Page<Role> getAllRoles(Predicate predicate, Pageable pageable);

	/**
	 * Assign role to group.
	 * @param idmGroupId id of idm group ref to assign roles to
	 * @param roleId type of role to be assigned to group
	 * @throws CommonsServiceException
	 */
	void assignRoleToGroup(long roleId, long idmGroupId);

	/**
	 * Returns set of roles of given groups
	 *
	 * @return roles
	 */
	Set<Role> getRolesOfGroups(List<Long> groupsIds);

	void removeRoleFromGroup(long roleId, long idmGroupId);
}
