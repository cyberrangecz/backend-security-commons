package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.api.PageResultResource;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.group.NewGroupRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;
import cz.muni.ics.kypo.commons.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
        groupRefService.delete(idmGroupId);
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
    public Set<RoleDTO> getRolesOfGroups(List<Long> groupsIds) {
        try {
            Set<Role> roles = groupRefService.getRolesOfGroups(groupsIds);
            LOG.info("Roles of given groups with ids: " + groupsIds + " have been loaded." );
            return beanMapping.mapToSet(roles, RoleDTO.class);
        } catch (CommonsServiceException ex) {
            LOG.error("Error while loading roles of groups with ids: " + groupsIds + ".");
            throw new CommonsFacadeException(ex.getMessage());
        }
    }
}
