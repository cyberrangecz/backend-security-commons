package cz.muni.ics.kypo.commons.service.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IDMGroupRefServiceImpl implements IDMGroupRefService {

    private static Logger LOG = LoggerFactory.getLogger(IDMGroupRefServiceImpl.class);

    private IDMGroupRefRepository idmGroupRefRepository;



    @Autowired
    public IDMGroupRefServiceImpl(IDMGroupRefRepository idmGroupRefRepository) {
        this.idmGroupRefRepository = idmGroupRefRepository;
    }

    @Override
    public void delete(long id) {
        IDMGroupRef idmGroupRef = idmGroupRefRepository.findByIdmGroupId(id).orElseThrow(() -> new CommonsServiceException("Idm group ref with id: " + id + " not found."));
        idmGroupRefRepository.delete(idmGroupRef);
        LOG.info("IDMGroupRef with id: " + idmGroupRef.getId() + " deleted." );
    }


}
