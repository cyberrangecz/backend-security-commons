package cz.muni.ics.kypo.commons.repository;

import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import cz.muni.ics.kypo.commons.model.UserRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan(basePackages = {"cz.muni.ics.kypo.commons.model"})
public class UserRefRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRefRepository userRefRepository;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @Test
    public void findByIDMUserId() throws Exception {
        Long expectedId = 1L;
        UserRef userRef = new UserRef();
        userRef.setIdmUserId(1L);
        this.entityManager.persist(userRef);
        Optional<UserRef> userRefOptional = this.userRefRepository.findByIdmUserId(expectedId);
        UserRef g = userRefOptional.orElseThrow(() -> new Exception("Group should be found"));
        assertEquals(userRef, g);
    }

    @Test
    public void findByGroupIdNotFound() {
        assertFalse(this.userRefRepository.findByIdmUserId(1L).isPresent());
    }
}
