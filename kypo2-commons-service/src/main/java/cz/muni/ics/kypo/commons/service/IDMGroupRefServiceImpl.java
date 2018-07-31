package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.repository.IDMGroupRefRepository;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class IDMGroupRefServiceImpl implements IDMGroupRefService {

    private static Logger LOG = LoggerFactory.getLogger(IDMGroupRefServiceImpl.class);

    private IDMGroupRefRepository idmGroupRefRepository;

    @Autowired
    public IDMGroupRefServiceImpl(IDMGroupRefRepository idmGroupRefRepository) {
        this.idmGroupRefRepository = idmGroupRefRepository;
    }

    @Override
    public Optional<IDMGroupRef> findByIdmGroupId(long id) {
        LOG.info("Find by idm group id {}.", id);
        try {
            return idmGroupRefRepository.findByIdmGroupId(id);
        } catch (HibernateException ex) {
            LOG.error("Error while loading idm group ref by group id");
            throw new CommonsServiceException("Error while loading idm group ref by group id " + ex.getMessage());
        }
    }

    @Override
    public Page<IDMGroupRef> findAll(Predicate predicate, Pageable pageable) {
        LOG.info("Finding all idm group ref");
        try {
            return idmGroupRefRepository.findAll(predicate, pageable);
        } catch (HibernateException ex) {
            LOG.error("Error while loading all idm group ref");
            throw new CommonsServiceException("Error while loading all idm group ref " + ex.getMessage());
        }
    }

    @Override
    public Optional<IDMGroupRef> create(IDMGroupRef idmGroupRef) {
        Assert.notNull(idmGroupRef, "Input idm group ref must not be null");
        IDMGroupRef ref = idmGroupRefRepository.save(idmGroupRef);
        LOG.info("IDMGroupRef with id: " + ref.getId() + " created.");
        return Optional.of(ref);
    }

    @Override
    public Optional<IDMGroupRef> update(IDMGroupRef idmGroupRef) {
        Assert.notNull(idmGroupRef, "Input idm group ref must not be null");
        IDMGroupRef updatedRef = idmGroupRefRepository.saveAndFlush(idmGroupRef);
        LOG.info("IDMGroupRef with id: " + updatedRef.getId() + "updated");
        return Optional.of(updatedRef);
    }

    @Override
    public void delete(IDMGroupRef idmGroupRef) {
        Assert.notNull(idmGroupRef, "Input idm group ref must not be null");
        idmGroupRefRepository.delete(idmGroupRef);
        LOG.info("IDMGroupRef with id: " + idmGroupRef.getId() + " deleted." );
    }

    @Override
    public Optional<IDMGroupRef> assignRolesToGroup(IDMGroupRef idmGroupRef, Set<Role> roles) {
        Assert.notEmpty(roles, "Input list of roles cannot be empty");
        Assert.notNull(idmGroupRef, "Input idm group ref cannot be null");
        Set<Role> groupRoles = idmGroupRef.getRoles();
        groupRoles.addAll(roles);
        idmGroupRef.setRoles(groupRoles);
        IDMGroupRef ref = idmGroupRefRepository.saveAndFlush(idmGroupRef);
        LOG.info("Roles have been assigned to group.");
        return Optional.of(ref);
    }

    @Override
    public Optional<IDMGroupRef> addUsersToGroup(IDMGroupRef idmGroupRef, List<UserRef> userRefList) {
        Assert.notEmpty(userRefList, "Input list of users cannot be empty");
        Assert.notNull(idmGroupRef, "Input idm group ref cannot be null");
        List<UserRef> usersRefsInGroup = idmGroupRef.getUsers();
        userRefList.stream().forEach(userRef -> {
            if (!usersRefsInGroup.contains(userRef)) {
            usersRefsInGroup.add(userRef);
        }});
        idmGroupRef.setUsers(usersRefsInGroup);
        idmGroupRef = idmGroupRefRepository.saveAndFlush(idmGroupRef);
        LOG.info("Users have been added to group.");
        return Optional.of(idmGroupRef);
    }
}
