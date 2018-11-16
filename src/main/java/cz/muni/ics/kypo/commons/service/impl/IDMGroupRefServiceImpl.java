package cz.muni.ics.kypo.commons.service.impl;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Pavel Seda & Dominik Pilar
 */
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
        LOG.info("IDMGroupRef with id: {} deleted.", idmGroupRef.getId());
    }

    @Override
    public Page<IDMGroupRef> getAllGroups(Predicate predicate, Pageable pageable) {
        return idmGroupRefRepository.findAll(predicate, pageable);
    }


}
