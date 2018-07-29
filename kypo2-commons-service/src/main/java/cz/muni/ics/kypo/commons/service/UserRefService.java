package cz.muni.ics.kypo.commons.service;

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
     * Deletes given user ref
     * @param userRef to be deleted
     */
    void delete(UserRef userRef);

    /**
     * Returns user ref by id
     *
     * @param id of user ref
     * @return user ref with given id
     * @throws CommonsServiceException if user ref could not be found
     */
    UserRef getById(Long id) throws CommonsServiceException;

    /**
     * Return user ref by user id
     *
     * @param id of user
     * @return user ref with given user id
     * @throws CommonsServiceException when role with given role type could not be found
     */
    UserRef getByIdmUserId(Long id) throws CommonsServiceException;

    /**
     * Returns all user refs
     *
     * @return all user refs
     */
    Page<UserRef> getAllUserRef(Predicate predicate, Pageable pageable);
}
