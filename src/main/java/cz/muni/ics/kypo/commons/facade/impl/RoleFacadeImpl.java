package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.facade.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
	public RoleDTO getById(long id) {
		try {
			Role role = roleService.getById(id);
			LOG.info("Role with id: {} has been loaded.", id);
			return beanMapping.mapTo(role, RoleDTO.class);
		} catch(CommonsServiceException ex) {
			LOG.error("Error while loading role with id {}.", id);
			throw new CommonsFacadeException(ex.getMessage());
		}
	}

	@Override
	public RoleDTO getByRoleType(String roleType) {
		try {
			Role role = roleService.getByRoleType(roleType);
			LOG.info("Role with role type: {} has been loaded.", roleType);
			return beanMapping.mapTo(role, RoleDTO.class);
		} catch (CommonsServiceException ex) {
			LOG.error("Error while loading role with role type: {}.", roleType);
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

	@Override
	public Set<RoleDTO> getRolesOfGroups(List<Long> groupsIds) {
		try {
			Set<Role> roles = roleService.getRolesOfGroups(groupsIds);
			LOG.info("Roles of given groups with ids: {} have been loaded.", groupsIds);
			return beanMapping.mapToSet(roles, RoleDTO.class);
		} catch (CommonsServiceException ex) {
			LOG.error("Error while loading roles of groups with ids: {}.", groupsIds);
			throw new CommonsFacadeException(ex.getMessage());
		}
	}

	@Override
	public void assignRoleToGroup(long roleId, long idmGroupId) {
		try {
			roleService.assignRoleToGroup(roleId, idmGroupId);
			LOG.info("Role with id: {} has been assigned to group with id: {}.", roleId, idmGroupId);
		} catch (CommonsServiceException ex) {
			LOG.error("Error while assigning role with id: {} to group with id: {}.", roleId, idmGroupId);
			throw new CommonsFacadeException(ex.getMessage());
		}

	}

	@Override
	public void removeRoleFromGroup(long roleId, long idmGroupId) {
		try {
			roleService.removeRoleFromGroup(roleId, idmGroupId);
			LOG.info("Role with id: {} has been removed." , roleId);
		} catch (CommonsServiceException ex) {
			LOG.error("Error while removing role with id: {} from group with id: {}.", roleId, idmGroupId);
			throw new CommonsFacadeException(ex.getMessage());
		}
	}
}
