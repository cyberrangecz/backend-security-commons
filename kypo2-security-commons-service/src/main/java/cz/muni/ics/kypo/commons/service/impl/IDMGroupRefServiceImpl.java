package cz.muni.ics.kypo.commons.service.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
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
public class IDMGroupRefServiceImpl implements IDMGroupRefService {

    private static Logger LOG = LoggerFactory.getLogger(IDMGroupRefServiceImpl.class);

    private IDMGroupRefRepository idmGroupRefRepository;
    private RoleRepository roleRepository;



    @Autowired
    public IDMGroupRefServiceImpl(IDMGroupRefRepository idmGroupRefRepository, RoleRepository roleRepository) {
        this.idmGroupRefRepository = idmGroupRefRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public IDMGroupRef getByIdmGroupId(long id) throws CommonsServiceException {
        Optional<IDMGroupRef> groupRef = idmGroupRefRepository.findByIdmGroupId(id);
        IDMGroupRef idmGroupRef = groupRef.orElseThrow(() -> new CommonsServiceException("IDMGroupRef with idm group id " + id + " not found"));
        LOG.info("IDMGroupRef with id " + id + " loaded.");
        return idmGroupRef;
    }

    @Override
    public Page<IDMGroupRef> findAll(Predicate predicate, Pageable pageable) {
        Page<IDMGroupRef> groupRefs = idmGroupRefRepository.findAll(predicate, pageable);
        LOG.info("All group refs loaded.");
        return groupRefs;
    }

    @Override
    public IDMGroupRef create(IDMGroupRef idmGroupRef) {
        Assert.notNull(idmGroupRef, "Input idm group ref must not be null");
        IDMGroupRef ref = idmGroupRefRepository.save(idmGroupRef);
        LOG.info("IDMGroupRef with id: " + ref.getId() + " created.");
        return ref;
    }

    @Override
    public void delete(long id) {
        IDMGroupRef idmGroupRef = this.getByIdmGroupId(id);
        idmGroupRefRepository.delete(idmGroupRef);
        LOG.info("IDMGroupRef with id: " + idmGroupRef.getId() + " deleted." );
    }

    @Override
    public IDMGroupRef assignRoleToGroup(long groupRefId, String roleType) {
        Assert.hasLength(roleType, "Input role type cannot be empty");
        Optional<Role> optionalRoleToBeAssigned = roleRepository.findByRoleType(roleType);
        Role role = optionalRoleToBeAssigned.orElseThrow(() -> new CommonsServiceException("Input role type " + roleType + " cannot be found"));

        IDMGroupRef idmGroupRef = this.getIDMGroupRefWithRoles(groupRefId);
        idmGroupRef.addRole(role);
        IDMGroupRef ref = idmGroupRefRepository.save(idmGroupRef);
        LOG.info("Roles have been assigned to group.");
        return ref;
    }

    @Override
    public IDMGroupRef getIDMGroupRefWithRoles(long id) throws CommonsServiceException{
        IDMGroupRef groupRef = getByIdmGroupId(id);
        groupRef.getRoles().size();
        return groupRef;
    }


    @Override
    public Set<Role> getRolesOfGroups(List<Long> groupsIds) {
        Assert.notEmpty(groupsIds, "Input list of groups ids must not be empty.");
        Set<Role> roles = new HashSet<>();
        for (Long id: groupsIds) {
            IDMGroupRef groupRef = getIDMGroupRefWithRoles(id);
            roles.addAll(groupRef.getRoles());
        }
        LOG.info("Roles of given groups have been loaded.");
        return roles;
    }
}
