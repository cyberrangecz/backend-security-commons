package cz.muni.ics.kypo.commons.facade.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author Pavel Seda
 */
public interface RoleFacade {
    /**
     * Returns role by id
     *
     * @param id of role
     * @return role with given id
     * @throws CommonsFacadeException if role could not be found
     */
    RoleDTO getById(long id);

    /**
     * Return role by role type
     *
     * @param roleType of role
     * @return role with given roleType
     * @throws CommonsFacadeException when role with given role type could not be found
     */
    RoleDTO getByRoleType(String roleType);

    /**
     * Returns all roles
     *
     * @return all roles
     */
    PageResultResource<RoleDTO> getAllRoles(Predicate predicate, Pageable pageable);


    /**
     * Returns set of roles of given groups
     *
     * @return roles
     */
    Set<RoleDTO> getRolesOfGroups(List<Long> groupsIds);


}