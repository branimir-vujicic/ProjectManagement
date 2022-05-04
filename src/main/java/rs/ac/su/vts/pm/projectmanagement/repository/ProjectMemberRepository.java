package rs.ac.su.vts.pm.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rs.ac.su.vts.pm.projectmanagement.model.entity.ProjectMember;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, Long>, QuerydslPredicateExecutor<ProjectMember>
{

    List<ProjectMember> findByUser(User user);

    Optional<ProjectMember> getProjectMemberByUserIdAndProjectId(Long userId, Long projectId);
}
