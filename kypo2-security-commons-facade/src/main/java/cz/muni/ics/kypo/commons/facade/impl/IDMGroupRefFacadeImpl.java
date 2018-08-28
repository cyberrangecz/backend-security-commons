package cz.muni.ics.kypo.commons.facade.impl;

import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.mapping.BeanMapping;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void delete(Long idmGroupId) {
        groupRefService.delete(idmGroupId);
    }





}
