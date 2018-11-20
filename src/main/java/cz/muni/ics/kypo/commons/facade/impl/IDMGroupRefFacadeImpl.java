package cz.muni.ics.kypo.commons.facade.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.facade.mapping.mapstruct.IDMGroupRefMapper;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Pavel Seda
 */
@Service
@Transactional
public class IDMGroupRefFacadeImpl implements IDMGroupRefFacade {

    private IDMGroupRefService groupRefService;
    private IDMGroupRefMapper idmGroupRefMapper;

    @Autowired
    public IDMGroupRefFacadeImpl(IDMGroupRefService groupRefService, IDMGroupRefMapper idmGroupRefMapper) {
        this.groupRefService = groupRefService;
        this.idmGroupRefMapper = idmGroupRefMapper;
    }

    @Override
    public void delete(Long idmGroupId) {
        groupRefService.delete(idmGroupId);
    }

    @Override
    public PageResultResource<IDMGroupRefDTO> getAllGroups(Predicate predicate, Pageable pageable) {
        try {
            Page<IDMGroupRef> groups = groupRefService.getAllGroups(predicate, pageable);
            return idmGroupRefMapper.mapToPageResultGroupDTO(groups);
        } catch (CommonsServiceException ex) {
            throw new CommonsFacadeException(ex);
        }
    }


}
