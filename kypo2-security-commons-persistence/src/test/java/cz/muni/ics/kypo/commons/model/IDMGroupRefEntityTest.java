package cz.muni.ics.kypo.commons.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;

import java.security.acl.Group;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IDMGroupRefEntityTest {


	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private TestEntityManager entityManager;

	@SpringBootApplication
	static class TestConfiguration {
	}

    /*@Test
    public void createWhenIdmGroupIdIsNullShouldThrowException() {
        thrown.expect(PersistenceException.class);
        IDMGroupRef groupRef = new IDMGroupRef();
        this.entityManager.persistFlushFind(groupRef);
    }*/

	@Test
	public void saveShouldPersistData() {
		IDMGroupRef groupRef = new IDMGroupRef();
		groupRef.setIdmGroupId(2L);
		IDMGroupRef g = this.entityManager.persistFlushFind(groupRef);
		assertEquals(2L, g.getIdmGroupId());
	}


}
