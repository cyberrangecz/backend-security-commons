package cz.muni.ics.kypo.commons.repository;

import cz.muni.ics.kypo.commons.model.IDMGroupRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDMGroupRefRepository extends JpaRepository<IDMGroupRef, Long>, QuerydslPredicateExecutor<IDMGroupRef> {

    Optional<IDMGroupRef> findByIdmGroupRef(Long id);
}
