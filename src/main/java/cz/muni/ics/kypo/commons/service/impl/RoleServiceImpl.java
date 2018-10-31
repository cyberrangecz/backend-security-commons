package cz.muni.ics.kypo.commons.service.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.model.Role;
import cz.muni.ics.kypo.commons.persistence.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.persistence.repository.RoleRepository;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private static Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class.getName());

    private RoleRepository roleRepository;
    private IDMGroupRefRepository idmGroupRefRepository;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, IDMGroupRefRepository idmGroupRefRepository) {
			this.roleRepository = roleRepository;
			this.idmGroupRefRepository = idmGroupRefRepository;
    }

    @Override
    public Role getById(long id) {
			Optional<Role> optionalRole = roleRepository.findById(id);
			Role r = optionalRole.orElseThrow(() -> new CommonsServiceException("Role with id " + id + " could not be found"));
			LOG.info("{} loaded.", r);
			return r;
    }

    @Override
    public Role getByRoleType(String roleType) {
			Assert.hasLength(roleType, "Input role type must not be null");
			Optional<Role> optionalRole = roleRepository.findByRoleType(roleType);
			Role r = optionalRole.orElseThrow(() -> new CommonsServiceException("Role with role type " + roleType + " could not be found"));
			LOG.info("{} loaded.", r);
			return r;
    }

    @Override
    public Page<Role> getAllRoles(Predicate predicate, Pageable pageable) {
			Page<Role> roles = roleRepository.findAll(predicate, pageable);
			LOG.info("All Roles loaded");
			return roles;
    }

    @Override
    public void assignRoleToGroup(long roleId, long idmGroupId) {
			Optional<Role> optionalRoleToBeAssigned = roleRepository.findById(roleId);
			Role role = optionalRoleToBeAssigned.orElseThrow(() -> new CommonsServiceException("Input role with id " + roleId + " cannot be found"));

			Optional<IDMGroupRef> optIdmGroupRef = idmGroupRefRepository.findByIdmGroupId(idmGroupId);
			IDMGroupRef idmGroupRef = optIdmGroupRef.orElse(new IDMGroupRef());
			idmGroupRef.addRole(role);
			idmGroupRef.setIdmGroupId(idmGroupId);
			idmGroupRefRepository.save(idmGroupRef);
			LOG.info("Roles has been assigned to group.");
    }

    @Override
    public void removeRoleFromGroup(long roleId, long idmGroupId) {
			Optional<Role> optionalRoleToBeRemoved = roleRepository.findById(roleId);
			Role role = optionalRoleToBeRemoved.orElseThrow(() -> new CommonsServiceException("Input role with id " + roleId + " cannot be found"));

			Optional<IDMGroupRef> optIdmGroupRef = idmGroupRefRepository.findByIdmGroupId(idmGroupId);
			IDMGroupRef idmGroupRef = optIdmGroupRef.orElseThrow(() -> new CommonsServiceException("Idm group with id: " + idmGroupId + " cannot be found."));
			idmGroupRef.removeRole(role);
			if (idmGroupRef.getRoles().isEmpty()) {
					idmGroupRefRepository.delete(idmGroupRef);
					LOG.info("Role {} has been removed from group and group has been deleted because has no role.", role.getRoleType());
			} else {
					idmGroupRefRepository.save(idmGroupRef);
					LOG.info("Role {} has been removed from group.", role.getRoleType());
			}
    }

    @Override
    public Set<Role> getRolesOfGroups(List<Long> groupsIds) {
			Assert.notEmpty(groupsIds, "Input list of groups ids must not be empty.");
			Set<Role> roles = new HashSet<>();
			for (Long id: groupsIds) {
					Optional<IDMGroupRef> groupRef = idmGroupRefRepository.findByIdmGroupId(id);
					groupRef.ifPresent(group -> roles.addAll(group.getRoles()));
			}
			LOG.info("Roles of given groups have been loaded.");
			return roles;
    }
}
