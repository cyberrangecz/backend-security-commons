package cz.muni.ics.kypo.commons.repository;

import cz.muni.ics.kypo.commons.model.UserRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRefRepository extends JpaRepository<UserRef, Long>, QuerydslPredicateExecutor<UserRef> {

    Optional<UserRef> findByLogin(String login);
}
