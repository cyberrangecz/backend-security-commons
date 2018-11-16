package cz.muni.ics.kypo.commons.service.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * @author Pavel Seda & Dominik Pilar
 */
public interface IDMGroupRefService {

    /**
     * Deletes given group ref from database.
     *
     * @param id group id of group ref to be deleted
     * @throws CommonsServiceException
     */
    void delete(long id);

    Page<IDMGroupRef> getAllGroups(Predicate predicate, Pageable pageable);
}
