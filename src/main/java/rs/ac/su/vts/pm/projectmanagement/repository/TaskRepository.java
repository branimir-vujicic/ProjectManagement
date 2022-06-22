package rs.ac.su.vts.pm.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository
        extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task> {
    List<Task> getTasksByProject(Project project);

}
