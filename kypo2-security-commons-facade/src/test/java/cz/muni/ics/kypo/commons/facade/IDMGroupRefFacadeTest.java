package cz.muni.ics.kypo.commons.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.config.FacadeTestConfiguration;
import cz.muni.ics.kypo.commons.facade.interfaces.IDMGroupRefFacade;
import cz.muni.ics.kypo.commons.model.IDMGroupRef;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.commons.repository"})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade", "cz.muni.ics.kypo.commons.service", "cz.muni.ics.kypo.commons.mapping"})
@Import(FacadeTestConfiguration.class)
public class IDMGroupRefFacadeTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private IDMGroupRefFacade groupRefFacade;

	@MockBean
	private IDMGroupRefService groupRefService;

	private IDMGroupRef groupRef1, groupRef2;
	private Predicate predicate;
	private Pageable pageable;

	@SpringBootApplication
	static class TestConfiguration {
	}

	@Before
	public void init() {


		groupRef1 = new IDMGroupRef();
		groupRef1.setId(1L);
		groupRef1.setIdmGroupId(1L);


		pageable = PageRequest.of(0, 10);

	}

	@Test
	public void testDeleteIDMGroupRef() {
		groupRefFacade.delete(1L);
		then(groupRefService).should().delete(1L);
	}



}
