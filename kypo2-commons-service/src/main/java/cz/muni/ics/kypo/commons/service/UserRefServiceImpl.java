package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.repository.UserRefRepository;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.Optional;

public class UserRefServiceImpl implements UserRefService {

    private static Logger LOG = LoggerFactory.getLogger(UserRefServiceImpl.class);

    private UserRefRepository userRefRepository;

    @Override
    public UserRef create(UserRef userRef) {
        Assert.notNull(userRef, "Input user ref cannot be null.");
        Assert.notNull(userRef.getIdmUserRef(), "Id of user cannot be null.");
        UserRef createdRef = userRefRepository.save(userRef);
        LOG.info("Reference for user with id " + createdRef.getIdmUserRef() + " created.");
        return createdRef;
    }

    @Override
    public void delete(UserRef userRef) {
        Assert.notNull(userRef, "Input user ref cannot be null.");
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
    public UserRef getByIdmUserId(Long id) throws CommonsServiceException {
        Assert.notNull(id, "Input id must not be null.");
        Optional<UserRef> userRefOptional = userRefRepository.findByIdmUserRef(id);
        UserRef userRef = userRefOptional.orElseThrow(() -> new CommonsServiceException("User ref with user id "+ id + " cannot be found."));
        LOG.info(userRef + " loaded.");
        return userRef;
    }

    @Override
    public Page<UserRef> getAllUserRef(Predicate predicate, Pageable pageable) {
        LOG.debug("findAllUserRef() {} {}", predicate, pageable);
        return  userRefRepository.findAll(predicate,pageable);
    }
}
