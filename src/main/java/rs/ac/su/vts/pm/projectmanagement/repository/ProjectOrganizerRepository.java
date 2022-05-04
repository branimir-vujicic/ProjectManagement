package rs.ac.su.vts.pm.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rs.ac.su.vts.pm.projectmanagement.model.entity.ProjectOrganizer;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectOrganizerRepository
        extends JpaRepository<ProjectOrganizer, Long>, QuerydslPredicateExecutor<ProjectOrganizer>
{

    List<ProjectOrganizer> findByUser(User user);

    Optional<ProjectOrganizer> getProjectOrganizerByUserIdAndProjectId(Long userId, Long projectId);
}
