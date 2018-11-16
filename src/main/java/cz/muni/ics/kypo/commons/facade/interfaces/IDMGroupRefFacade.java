package cz.muni.ics.kypo.commons.facade.interfaces;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.GroupsRefDTO;
import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;
import org.springframework.data.domain.Pageable;

/**
 * @author Pavel Seda
 */
public interface IDMGroupRefFacade {

    /**
     * Deletes given group ref from database.
     *
     * @param groupRefId group ref to be deleted
     * @throws CommonsFacadeException
     */
    void delete(Long groupRefId);

    PageResultResource<GroupsRefDTO> getAllGroups(Predicate predicate, Pageable pageable);
}
