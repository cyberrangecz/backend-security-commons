package cz.muni.ics.kypo.commons.persistence.repository;

import cz.muni.ics.kypo.commons.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jan Duda & Pavel Seda
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {

    Optional<Role> findByRoleType(String roleType);

}