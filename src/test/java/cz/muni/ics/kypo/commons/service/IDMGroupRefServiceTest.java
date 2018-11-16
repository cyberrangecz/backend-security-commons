package cz.muni.ics.kypo.commons.service;

import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.service.impl.IDMGroupRefServiceImpl;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class IDMGroupRefServiceTest {

    private IDMGroupRefService groupRefService;

    @Mock
    private IDMGroupRefRepository groupRefRepository;

    private IDMGroupRef groupRef1, groupRef2;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        groupRefService = new IDMGroupRefServiceImpl(groupRefRepository);
        groupRef1 = new IDMGroupRef();
        groupRef1.setId(1L);
        groupRef1.setIdmGroupId(5L);

        groupRef2 = new IDMGroupRef();
        groupRef2.setId(2L);
        groupRef2.setIdmGroupId(2L);
    }

    @Test
    public void testDeleteIDMGroupRef() {
        BDDMockito.given(groupRefRepository.findByIdmGroupId(1L)).willReturn(Optional.of(groupRef1));
        groupRefService.delete(1L);
        BDDMockito.then(groupRefRepository).should().delete(groupRef1);
    }


}
