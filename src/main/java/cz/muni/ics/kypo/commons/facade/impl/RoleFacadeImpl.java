package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.RoleDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.RoleFacade;
import cz.muni.ics.kypo.commons.facade.mapping.mapstruct.RoleMapper;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
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
	private RoleMapper roleMapper;

	@Autowired
	public RoleFacadeImpl(RoleService roleService, RoleMapper roleMapper) {
		this.roleService = roleService;
		this.roleMapper = roleMapper;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public RoleDTO getById(long id) {
		try {
			Role role = roleService.getById(id);
			LOG.info("Role with id: {} has been loaded.", id);
			return roleMapper.mapToRoleDTO(role);
		} catch(CommonsServiceException ex) {
			LOG.error("Error while loading role with id {}.", id);
			throw new CommonsFacadeException(ex.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public RoleDTO getByRoleType(String roleType) {
		try {
			Role role = roleService.getByRoleType(roleType);
			LOG.info("Role with role type: {} has been loaded.", roleType);
			return roleMapper.mapToRoleDTO(role);
		} catch (CommonsServiceException ex) {
			LOG.error("Error while loading role with role type: {}.", roleType);
			throw new CommonsFacadeException(ex.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public PageResultResource<RoleDTO> getAllRoles(Predicate predicate, Pageable pageable) {
		try {
			Page<Role> roles = roleService.getAllRoles(predicate,pageable);
			LOG.info("Roles has been loaded.");
			return roleMapper.mapToPageResultRoleDTO(roles);
		} catch (CommonsServiceException ex) {
			LOG.error("Error while loading roles.");
			throw new CommonsFacadeException(ex.getMessage());
		}
	}


}
