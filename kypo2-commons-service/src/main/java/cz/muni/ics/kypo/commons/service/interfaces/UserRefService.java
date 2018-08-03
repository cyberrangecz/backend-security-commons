package cz.muni.ics.kypo.commons.service.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.model.UserRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRefService {

    /**
     * Creates given user ref
     *
     * @param userRef to be created
     * @return created user ref
     */
    UserRef create(UserRef userRef);

    /**
     * Deletes given user ref with given login
     * @param userLogin login of user to be deleted
     */
    void delete(String userLogin);

    /**
     * Returns user ref by id
     *
     * @param id of user ref
     * @return user ref with given id
     * @throws CommonsServiceException if user ref could not be found
     */
    UserRef getById(Long id) throws CommonsServiceException;

    /**
     * Return user ref by user login
     *
     * @param login of user
     * @return user ref with given user login
     * @throws CommonsServiceException when user with given login could not be found
     */
    UserRef getByLogin(String login) throws CommonsServiceException;

    /**
     * Returns all user refs
     *
     * @return all user refs
     */
    Page<UserRef> getAllUserRef(Predicate predicate, Pageable pageable);

    /**
     * Return user ref by user login with groups references
     *
     * @param userLogin login of user
     * @return user ref with groups ref by given user login
     * @throws CommonsServiceException when user with given login could not be found
     */
    UserRef getUserWithGroupsRef(String userLogin) throws CommonsServiceException;
}
