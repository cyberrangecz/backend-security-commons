package cz.muni.ics.kypo.commons.facade.impl;

import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IDMGroupRefFacadeImpl implements IDMGroupRefFacade {

	private IDMGroupRefService groupRefService;

	@Autowired
	public IDMGroupRefFacadeImpl(IDMGroupRefService groupRefService) {
		this.groupRefService = groupRefService;
	}

	@Override
	public void delete(Long idmGroupId) {
		groupRefService.delete(idmGroupId);
	}




}
