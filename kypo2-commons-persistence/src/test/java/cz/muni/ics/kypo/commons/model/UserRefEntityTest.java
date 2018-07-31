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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRefEntityTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private TestEntityManager entityManager;

    @SpringBootApplication
    static class TestConfiguration {
    }
/*
    @Test
    public void createWhenIdmUserIdIsNullShouldThrowException() {
        thrown.expect(PersistenceException.class);
        UserRef userRef = new UserRef();
        this.entityManager.persistFlushFind(new UserRef());
    }
*/
    @Test
    public void saveShouldPersistData() {
        UserRef userRef = new UserRef();
        userRef.setIdmUserId(1L);
        this.entityManager.persistAndFlush(userRef);
    }
}
