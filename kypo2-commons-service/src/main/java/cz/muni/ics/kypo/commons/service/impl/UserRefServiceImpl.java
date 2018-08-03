package cz.muni.ics.kypo.commons.service.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.repository.UserRefRepository;
import cz.muni.ics.kypo.commons.service.interfaces.UserRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
@Service
public class UserRefServiceImpl implements UserRefService {

    private static Logger LOG = LoggerFactory.getLogger(UserRefServiceImpl.class);

    private UserRefRepository userRefRepository;

    @Autowired
    public void UserRefServiceImpl(UserRefRepository userRefRepository) {
        this.userRefRepository = userRefRepository;
    }

    @Override
    public UserRef create(UserRef userRef) {
        Assert.notNull(userRef, "Input user ref cannot be null.");
        Assert.notNull(userRef.getLogin(), "Login of user cannot be null.");
        UserRef createdRef = userRefRepository.save(userRef);
        LOG.info("Reference for user with login " + createdRef.getLogin() + " created.");
        return createdRef;
    }

    @Override
    public void delete(String userLogin) {
        UserRef userRef = this.getByLogin(userLogin);
        userRefRepository.delete(userRef);
        LOG.info("User reference has been deleted");
    }

    @Override
    public UserRef getById(Long id) throws CommonsServiceException {
        Assert.notNull(id, "Input id must not be null.");
        Optional<UserRef> userRefOptional = userRefRepository.findById(id);
        UserRef userRef = userRefOptional.orElseThrow(() -> new CommonsServiceException("User ref with id "+ id + " cannot be found."));
        LOG.info(userRef + " loaded.");
        return userRef;
    }

    @Override
    public UserRef getByLogin(String login) throws CommonsServiceException {
        Assert.notNull(login, "Input login must not be null.");
        Optional<UserRef> userRefOptional = userRefRepository.findByLogin(login);
        UserRef userRef = userRefOptional.orElseThrow(() -> new CommonsServiceException("User ref with user login "+ login + " cannot be found."));
        LOG.info(userRef + " loaded.");
        return userRef;
    }

    @Override
    public UserRef getUserWithGroupsRef(String userLogin) throws CommonsServiceException {
        UserRef userRef = this.getByLogin(userLogin);
        userRef.getGroups().size();
        return userRef;
    }

    @Override
    public Page<UserRef> getAllUserRef(Predicate predicate, Pageable pageable) {
        LOG.debug("findAllUserRef() {} {}", predicate, pageable);
        return  userRefRepository.findAll(predicate,pageable);
    }
}
