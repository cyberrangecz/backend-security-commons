package cz.muni.ics.kypo.commons.facade.interfaces;

import cz.muni.ics.kypo.commons.rest.exceptions.CommonsServiceException;

public interface IDMGroupRefFacade {
	/**
	 * Deletes given group ref from database.
	 * @param groupRefId group ref to be deleted
	 * @throws CommonsServiceException
	 */
	void delete(Long groupRefId);

}
