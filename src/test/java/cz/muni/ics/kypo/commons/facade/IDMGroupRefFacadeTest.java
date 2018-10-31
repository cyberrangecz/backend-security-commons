package cz.muni.ics.kypo.commons.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.facade.impl.IDMGroupRefFacadeImpl;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IDMGroupRefFacadeTest {

	private IDMGroupRefFacade groupRefFacade;

	@Mock
	private IDMGroupRefService groupRefService;

	private IDMGroupRef groupRef1, groupRef2;
	private Predicate predicate;
	private Pageable pageable;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		groupRefFacade = new IDMGroupRefFacadeImpl(groupRefService);

		groupRef1 = new IDMGroupRef();
		groupRef1.setId(1L);
		groupRef1.setIdmGroupId(1L);


		pageable = PageRequest.of(0, 10);

	}

	@Test
	public void testDeleteIDMGroupRef() {
		groupRefFacade.delete(1L);
		BDDMockito.then(groupRefService).should().delete(1L);
	}



}
