package cz.muni.ics.kypo.commons.repository;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.commons.model.UserRef;
import org.apache.catalina.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRefRepository extends JpaRepository<UserRef, Long>, QuerydslPredicateExecutor<UserRef> {

    Optional<UserRef> findByIdmUserRef(Long aLong);
}
