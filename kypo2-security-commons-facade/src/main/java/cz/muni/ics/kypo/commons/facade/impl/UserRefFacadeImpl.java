package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.GroupRefForUsersDTO;
import cz.muni.ics.kypo.commons.api.dto.user.NewUserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.user.UserRefWithGroupsDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.facade.interfaces.UserRefFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.UserRef;
import cz.muni.ics.kypo.commons.service.interfaces.UserRefService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;

import java.util.ArrayList;
import java.util.List;

public class UserRefFacadeImpl implements UserRefFacade {

    private static final Logger LOG = LoggerFactory.getLogger(RoleFacadeImpl.class);

    private UserRefService userRefService;
    private BeanMapping beanMapping;

    @Autowired
    public void UserRefFacadeImpl(UserRefService userRefService, BeanMapping beanMapping) {
        this.beanMapping = beanMapping;
        this.userRefService = userRefService;
    }

    @Override
    public UserRefDTO create(NewUserRefDTO userRefDTO) {
        UserRef userRef = userRefService.create(beanMapping.mapTo(userRefDTO, UserRef.class));
        LOG.info("UserRef with login: " + userRefDTO.getLogin() + " has been created.");
        return beanMapping.mapTo(userRef, UserRefDTO.class);
    }

    @Override
    public void delete(String userLogin) {
        LOG.info("Deleting userRef with login: " + userLogin + ".");
        userRefService.delete(userLogin);
    }

    @Override
    public UserRefDTO getById(Long id) throws CommonsFacadeException {
        try {
            UserRef userRef = userRefService.getById(id);
            LOG.info("UserRef with id: " + id +  " has been loaded.");
            return beanMapping.mapTo(userRef, UserRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading userRef with id: " + id + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public UserRefDTO getByLogin(String login) throws CommonsFacadeException {
        try {
            UserRef userRef = userRefService.getByLogin(login);
            LOG.info("UserRef with login: " + login +  " has been loaded.");
            return beanMapping.mapTo(userRef, UserRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading userRef with login: " + login + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public PageResultResource<UserRefDTO> getAllUserRef(Predicate predicate, Pageable pageable) {
        Page<UserRef> userRefs = userRefService.getAllUserRef(predicate, pageable);
        LOG.info("Users has been loaded.");
        return beanMapping.mapToPageResultDTO(userRefs, UserRefDTO.class);
    }

    @Override
    public UserRefDTO getUserWithGroupsRef(String userLogin) throws CommonsFacadeException {
        try {
            UserRef userRef = userRefService.getUserWithGroupsRef(userLogin);
            LOG.info("UserRef with login: " + userLogin +  " has been loaded.");
            return mapToUserRefWithGroupsDTO(userRef);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading userRef with login: " + userLogin + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    private UserRefWithGroupsDTO mapToUserRefWithGroupsDTO(UserRef userRef) {
        UserRefWithGroupsDTO userRefWithGroupsDTO = beanMapping.mapTo(userRef, UserRefWithGroupsDTO.class);
        List<GroupRefForUsersDTO> groups = new ArrayList<>();
        for (IDMGroupRef uR: userRef.getGroups()) {
            groups.add(beanMapping.mapTo(uR, GroupRefForUsersDTO.class));
        }
        userRefWithGroupsDTO.setGroups(groups);
        return userRefWithGroupsDTO;
    }
}
