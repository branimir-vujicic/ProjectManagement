package rs.ac.su.vts.pm.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository
        extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project>
{

    Project findOneByProjectMembersUser(User user);

    List<Project> findAllByTags_id(Long tagId);

    long countAllByTags_id(Long tagId);

    Optional<Project> getByTitle(String title);

    boolean existsByTitle(String title);

    Optional<Project> getByDirectory(String directory);
}
