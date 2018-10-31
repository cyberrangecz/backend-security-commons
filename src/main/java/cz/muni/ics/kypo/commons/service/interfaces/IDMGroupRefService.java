package cz.muni.ics.kypo.commons.service.interfaces;

import cz.muni.ics.kypo.commons.service.exceptions.CommonsServiceException;

public interface IDMGroupRefService {

	/**
	 * Deletes given group ref from database.
	 * @param id group id of group ref to be deleted
	 * @throws CommonsServiceException
	 */
	void delete(long id);
}
