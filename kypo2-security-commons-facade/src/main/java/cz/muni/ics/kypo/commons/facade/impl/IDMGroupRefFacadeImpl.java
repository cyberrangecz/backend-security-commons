package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class IDMGroupRefFacadeImpl implements IDMGroupRefFacade {

    private static final Logger LOG = LoggerFactory.getLogger(RoleFacadeImpl.class);

    private IDMGroupRefService groupRefService;
    private BeanMapping beanMapping;

    @Autowired
    public IDMGroupRefFacadeImpl(IDMGroupRefService groupRefService, BeanMapping beanMapping) {
        this.groupRefService = groupRefService;
        this.beanMapping = beanMapping;
    }

    @Override
    public IDMGroupRefDTO create(NewGroupRefDTO newGroupRefDTO) {
        LOG.info("Creating IDMGroupRef with group id: " + newGroupRefDTO.getIdmGroupId());
        IDMGroupRef groupRef = groupRefService.create(beanMapping.mapTo(newGroupRefDTO, IDMGroupRef.class));
        System.out.println(groupRef);
        return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
    }

    @Override
    public void delete(Long idmGroupId) {
        groupRefService.delete(groupRefService.getByIdmGroupId(idmGroupId));

    }

    @Override
    public IDMGroupRefDTO getByIdmGroupId(long id) throws CommonsServiceException {
        try {
            IDMGroupRef groupRef = groupRefService.getByIdmGroupId(id);
            LOG.info("IDMGroupRef with group id: " + id + " has been loaded.");
            return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading IDMGroupRef with group id: " + id + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public PageResultResource<IDMGroupRefDTO> findAll(Predicate predicate, Pageable pageable) {
        try {
            Page<IDMGroupRef> groups = groupRefService.findAll(predicate,pageable);
            LOG.info("Groups has been loaded.");
            return beanMapping.mapToPageResultDTO(groups, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading groups.");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public IDMGroupRefDTO assignRoleToGroup(long groupRefId, String roleType) throws CommonsFacadeException {
        try {
            IDMGroupRef groupRef = groupRefService.assignRoleToGroup(groupRefId, roleType);
            LOG.info("Role type: "+ roleType + " has been assigned to group with id: " + groupRefId);
            return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while assigning role type: " + roleType + " to group with id: " + groupRefId);
            throw new CommonsFacadeException(ex.getMessage());
        }

    }

    @Override
    public IDMGroupRefDTO addUsersToGroupRef(long groupRefId, List<String> userRefLogins) throws CommonsFacadeException {
        try {
            IDMGroupRef groupRef = groupRefService.addUsersToGroupRef(groupRefId, userRefLogins);
            LOG.info("Users have been added to group ref with group id: "+ groupRefId);
            return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while adding users to group ref with group id: " + groupRefId);
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public IDMGroupRefDTO removeUsersFromGroupRef(long groupRefId, List<String> userRefLogins) throws CommonsFacadeException {
        try {
            IDMGroupRef groupRef = groupRefService.removeUsersFromGroupRef(groupRefId, userRefLogins);
            LOG.info("Users have been removed from group ref with group id: "+ groupRefId);
            return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while removing users from group ref with group id: "+ groupRefId);
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public IDMGroupRefDTO getIDMGroupRefWithUsers(long groupRefId) throws CommonsFacadeException {
        try {
            IDMGroupRef groupRef = groupRefService.getIDMGroupRefWithUsers(groupRefId);
            LOG.info("Group ref with group id: " + groupRefId + " has been loaded also with users");
            return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading user ref with users with group id: " + groupRefId + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }

    @Override
    public IDMGroupRefDTO getIDMGroupRefWithRoles(long groupRefId) throws CommonsFacadeException {
        try {
            IDMGroupRef groupRef = groupRefService.getIDMGroupRefWithRoles(groupRefId);
            LOG.info("Group ref with group id: " + groupRefId + " has been loaded also with roles");
            return beanMapping.mapTo(groupRef, IDMGroupRefDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading user ref with roles with group id: " + groupRefId + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }
}
