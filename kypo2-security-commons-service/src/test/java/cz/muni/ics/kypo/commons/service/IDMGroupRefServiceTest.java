package cz.muni.ics.kypo.commons.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.exceptions.CommonsServiceException;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.Role;
import cz.muni.ics.kypo.commons.repository.IDMGroupRefRepository;
import cz.muni.ics.kypo.commons.repository.RoleRepository;
import cz.muni.ics.kypo.commons.service.interfaces.IDMGroupRefService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service"})
public class IDMGroupRefServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private IDMGroupRefService groupRefService;

	@MockBean
	private IDMGroupRefRepository groupRefRepository;

	private IDMGroupRef groupRef1, groupRef2;


	@SpringBootApplication
	static class TestConfiguration {
	}

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
		given(groupRefRepository.findByIdmGroupId(1L)).willReturn(Optional.of(groupRef1));
		groupRefService.delete(1L);
		then(groupRefRepository).should().delete(groupRef1);
	}





}
