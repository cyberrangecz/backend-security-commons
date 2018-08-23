package cz.muni.ics.kypo.commons.service.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.service.interfaces.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {

    private static Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class.getName());

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getById(long id) throws CommonsServiceException {
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role r = optionalRole.orElseThrow(() -> new CommonsServiceException("Role with id " + id + " could not be found"));
        LOG.info(r + " loaded");
        return r;
    }

    @Override
    public Role getByRoleType(String roleType) throws CommonsServiceException {
        Assert.hasLength(roleType, "Input role type must not be null");
        Optional<Role> optionalRole = roleRepository.findByRoleType(roleType);
        Role r = optionalRole.orElseThrow(() -> new CommonsServiceException("Role with role type " + roleType + " could not be found"));
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
