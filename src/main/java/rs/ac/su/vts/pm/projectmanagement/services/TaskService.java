package rs.ac.su.vts.pm.projectmanagement.services;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rs.ac.su.vts.pm.projectmanagement.exception.NotFoundException;
import rs.ac.su.vts.pm.projectmanagement.model.common.Status;
import rs.ac.su.vts.pm.projectmanagement.model.dto.task.TaskCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.task.TaskListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.task.TaskMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.task.TaskUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Task;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.repository.TaskRepository;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@AllArgsConstructor
@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    private final ProjectService projectService;

    private final UserService userService;

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Task not found: %s", id)));
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Page<Task> listTasks(TaskListRequest request) {
        BooleanBuilder predicate = request.predicate();
        return taskRepository.findAll(predicate, request.pageable());
    }

    public List<Task> getAllByProject(Project project) {
        return taskRepository.getTasksByProject(project);
    }

    public Task createTask(TaskCreateRequest taskCreateRequest) {
        Task task = TaskMapper.INSTANCE.from(taskCreateRequest);
        task.setProject(projectService.getProjectById(taskCreateRequest.getProjectId()));
        task.setAuthor(userService.getLoggedUser());
        if (taskCreateRequest.getUserId() != null) {
            User assignee = userService.getUserById(taskCreateRequest.getUserId());
            task.setUser(assignee);
        } else {
            task.setUser(userService.getLoggedUser());
        }
        return taskRepository.save(task);
    }

    public Task updateTask(TaskUpdateRequest taskUpdateRequest) {
        Task task = getTaskById(taskUpdateRequest.getId());
        TaskMapper.INSTANCE.fromUpdateRequest(taskUpdateRequest, task);
        Long userId = taskUpdateRequest.getUserId();
        if (userId != null && task.getUser() != null && !Objects.equals(task.getUser().getId(), userId)) {
            User assignee = userService.getUserById(userId);
            task.setUser(assignee);
        }
        return taskRepository.save(task);
    }

    public void updateTaskStatus(Long taskId, Status status) {
        Task task = getTaskById(taskId);
        task.setStatus(status);
        taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        Task task = getTaskById(taskId);
        task.setDeleted(true);
        taskRepository.save(task);
    }

    public void undeleteTask(Long taskId) {
        Task task = getTaskById(taskId);
        task.setDeleted(false);
        taskRepository.save(task);
    }
}
