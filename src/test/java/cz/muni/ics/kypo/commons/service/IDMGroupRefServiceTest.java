package cz.muni.ics.kypo.commons.service;

import cz.muni.ics.kypo.commons.service.config.ServiceCommonsConfigTest;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.persistence.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {ServiceCommonsConfigTest.class})
public class IDMGroupRefServiceTest {



	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private IDMGroupRefService groupRefService;

	@MockBean
	private IDMGroupRefRepository groupRefRepository;

	private IDMGroupRef groupRef1, groupRef2;

	@Before
	public void init() {
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
