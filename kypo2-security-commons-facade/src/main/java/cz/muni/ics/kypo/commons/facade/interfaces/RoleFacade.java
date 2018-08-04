package cz.muni.ics.kypo.commons.facade.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.role.NewRoleDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleFacade {

    /**
     * Creates given role
     *
     * @param newRoleDTO to be created
     * @return created role
     */
    RoleDTO create(NewRoleDTO newRoleDTO);

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
    RoleDTO getById(long id) throws CommonsFacadeException;

    /**
     * Return role by role type
     *
     * @param roleType of role
     * @return role with given roleType
     * @throws CommonsServiceException when role with given role type could not be found
     */
    RoleDTO getByRoleType(String roleType) throws CommonsFacadeException;

    /**
     * Returns all roles
     *
     * @return all roles
     */
    PageResultResource<RoleDTO> getAllRoles(Predicate predicate, Pageable pageable);
}
