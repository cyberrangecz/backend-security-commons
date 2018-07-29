package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.Optional;

public class RoleServiceImpl implements RoleService  {

    private static Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class.getName());

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        Assert.notNull(role, "Input role must not be null");
        Assert.notNull(role.getRoleType(), "Role type of input role must not be null");

        Role r = roleRepository.save(role);
        LOG.info(r + " was created.");
        return r;
    }

    @Override
    public void delete(Role role) {
        Assert.notNull(role, "Input role must not be null");
        roleRepository.delete(role);
        LOG.info("Role with id: " + role.getId() + " was successfully deleted.");
    }

    @Override
    public Role getById(Long id) throws CommonsServiceException {
        Assert.notNull(id, "Input id must not be null");
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role r = optionalRole.orElseThrow(() -> new CommonsServiceException("Role with id " + id + " could not be found"));
        LOG.info(r + " loaded");
        return r;
    }

    @Override
    public Role getByRoleType(String roleType) throws CommonsServiceException {
        Assert.notNull(roleType, "Input role type must not be null");
        Optional<Role> optionalRole = roleRepository.findByRoleType(roleType);
        Role r = optionalRole.orElseThrow(() -> new ServiceException("Role with roleType " + roleType + " could not be found"));
        LOG.info(r + " loaded");
        return r;
    }

    @Override
    public Page<Role> getAllRoles(Predicate predicate, Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(predicate, pageable);
        LOG.info("All Roles loaded");
        return roles;
    }
}
