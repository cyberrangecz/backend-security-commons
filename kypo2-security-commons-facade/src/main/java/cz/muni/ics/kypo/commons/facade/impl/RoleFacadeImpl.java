package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.role.NewRoleDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleFacadeImpl implements RoleFacade {

    private static final Logger LOG = LoggerFactory.getLogger(RoleFacadeImpl.class);

    private RoleService roleService;
    private BeanMapping beanMapping;

    @Autowired
    public RoleFacadeImpl(RoleService roleService, BeanMapping beanMapping) {
        this.roleService = roleService;
        this.beanMapping = beanMapping;
    }

    @Override
    public RoleDTO create(NewRoleDTO newRoleDTO) {
        Role role = roleService.create(beanMapping.mapTo(newRoleDTO, Role.class));
        return beanMapping.mapTo(role, RoleDTO.class);
    }

    @Override
    public void delete(String roleType) {
        roleService.delete(roleType);
        LOG.info("Role with role type: " + roleType + " deleted.");
    }

    @Override
    public RoleDTO getById(long id) throws CommonsFacadeException {
        try {
            Role role = roleService.getById(id);
            LOG.info("Role with id: " + id + " has been loaded.");
            return beanMapping.mapTo(role, RoleDTO.class);
        } catch(CommonsServiceException ex) {
            LOG.error("Error while loading role with id " + id + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public RoleDTO getByRoleType(String roleType) throws CommonsFacadeException {
        try {
            Role role = roleService.getByRoleType(roleType);
            LOG.info("Role with role type: " + roleType + " has been loaded.");
            return beanMapping.mapTo(role, RoleDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading role with role type: " + roleType + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public PageResultResource<RoleDTO> getAllRoles(Predicate predicate, Pageable pageable) {
        try {
            Page<Role> roles = roleService.getAllRoles(predicate,pageable);
            LOG.info("Roles has been loaded.");
            return beanMapping.mapToPageResultDTO(roles, RoleDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading roles.");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }
}
