package cz.muni.ics.kypo.commons.facade.interfaces;

import cz.muni.ics.kypo.commons.facade.exception.CommonsFacadeException;

public interface IDMGroupRefFacade {
	/**
	 * Deletes given group ref from database.
	 * @param groupRefId group ref to be deleted
	 * @throws CommonsFacadeException
	 */
	void delete(Long groupRefId);

}
