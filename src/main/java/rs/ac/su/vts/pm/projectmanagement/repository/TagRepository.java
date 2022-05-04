package rs.ac.su.vts.pm.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Tag;

import java.util.Optional;

@Repository
public interface TagRepository
        extends JpaRepository<Tag, Long>, QuerydslPredicateExecutor<Tag>
{

    Optional<Tag> findByNameIgnoreCase(String name);

    Optional<Tag> findByNameIgnoreCaseAndDeletedIsFalse(String name);
}
