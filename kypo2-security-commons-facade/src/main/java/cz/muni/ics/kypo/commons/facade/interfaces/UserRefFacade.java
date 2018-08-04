package cz.muni.ics.kypo.commons.facade.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.user.NewUserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.UserRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRefFacade {
    /**
     * Creates given user ref
     *
     * @param newUserRefDTO to be created
     * @return created user ref
     */
    UserRefDTO create(NewUserRefDTO newUserRefDTO);

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
    UserRefDTO getById(Long id) throws CommonsFacadeException;

    /**
     * Return user ref by user login
     *
     * @param login of user
     * @return user ref with given user login
     * @throws CommonsServiceException when user with given login could not be found
     */
    UserRefDTO getByLogin(String login) throws CommonsFacadeException;

    /**
     * Returns all user refs
     *
     * @return all user refs
     */
    PageResultResource<UserRefDTO> getAllUserRef(Predicate predicate, Pageable pageable);

    /**
     * Return user ref by user login with groups references
     *
     * @param userLogin login of user
     * @return user ref with groups ref by given user login
     * @throws CommonsServiceException when user with given login could not be found
     */
    UserRefDTO getUserWithGroupsRef(String userLogin) throws CommonsFacadeException;
}
