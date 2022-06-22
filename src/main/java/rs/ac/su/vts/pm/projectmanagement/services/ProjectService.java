package rs.ac.su.vts.pm.projectmanagement.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.su.vts.pm.projectmanagement.exception.NotFoundException;
import rs.ac.su.vts.pm.projectmanagement.exception.ProjectExistsException;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.entity.*;
import rs.ac.su.vts.pm.projectmanagement.repository.ProjectMemberRepository;
import rs.ac.su.vts.pm.projectmanagement.repository.ProjectOrganizerRepository;
import rs.ac.su.vts.pm.projectmanagement.repository.ProjectRepository;
import rs.ac.su.vts.pm.projectmanagement.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectOrganizerRepository projectOrganizerRepository;
    private final UserRepository userRepository;

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(format("Project not found: %s", id)));
    }

    public Page<Project> listProjects(ProjectListRequest request) {
        BooleanBuilder predicate = request.predicate();
        return projectRepository.findAll(predicate, request.pageable());
    }

    public Long countProjects(ProjectListRequest request) {
        BooleanBuilder predicate = request.predicate();
        return projectRepository.count(predicate);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Project updateProject(Project project, ProjectUpdateRequest request) {
        boolean shouldUpdate = shouldUpdate(project, request);
        if (!shouldUpdate) {
            return project;
        }

        Optional<Project> projectByTile = projectRepository.getByTitle(request.getTitle());
        if (projectByTile.isPresent() && !Objects.equals(project.getId(), projectByTile.get().getId())) {
            throw new ProjectExistsException();
        }

        ProjectMapper.INSTANCE.fromUpdateRequest(request, project);
        project.setDate(request.getDate());
        project.setTimeFrom(request.getTimeFrom());
        project.setTimeTo(request.getTimeTo());
        project.setDeleted(false);

        return projectRepository.save(project);
    }

    private boolean shouldUpdate(Project project, ProjectUpdateRequest request) {
        //meta
        boolean metaSame = Objects.equals(project.getTitle(), request.getTitle()) &&
                Objects.equals(project.getDescription(), request.getDescription()) &&
                Objects.equals(project.getDate(), request.getDate()) &&
                Objects.equals(project.getTimeFrom(), request.getTimeFrom()) &&
                Objects.equals(project.getTimeTo(), request.getTimeTo());
        return !(metaSame);
    }

    public Project createProject(ProjectCreateRequest request) {
        if (projectRepository.existsByTitle(request.getTitle())) {
            throw new ProjectExistsException();
        }

        Project fromRequest = ProjectMapper.INSTANCE.from(request);

        Project project = projectRepository.save(fromRequest);
        Set<User> members = request.getMemberIds().stream().map(userService::getUserById).collect(Collectors.toSet());
        Set<User> organizers = request.getOrganizerIds().stream().map(userService::getUserById).collect(Collectors.toSet());
        Set<Tag> tags = request.getTagIds().stream().map(tagService::getTagById).collect(Collectors.toSet());

        List<ProjectMember> projectMembers = members.stream().map(u -> projectMemberRepository.save(new ProjectMember(project, u))).collect(Collectors.toList());
        List<ProjectOrganizer> projectOrganizers = organizers.stream().map(u -> projectOrganizerRepository.save(new ProjectOrganizer(project, u))).collect(Collectors.toList());

        project.setDate(request.getDate());
        project.setTimeFrom(request.getTimeFrom());
        project.setTimeTo(request.getTimeTo());

        project.setProjectMembers(projectMembers);
        project.setProjectOrganizers(projectOrganizers);
        project.setTags(tags);

        return projectRepository.save(project);
    }

    public void deleteProject(Long id, User user) {
        Project project = getProjectById(id);
        project.setDeleted(true);
        projectRepository.save(project);
    }

    public void undeleteProject(Long id, User user) {
        Project project = getProjectById(id);
        if (!project.isDeleted()) {
            return;
        }
        project.setDeleted(false);
        projectRepository.save(project);
    }

    public void leaveProject(Long id, User user) {
        user = userService.getUserById(user.getId());
        Project project = getProjectById(id);
        removeMember(project, user.getId(), false);
        if (project.getOrganizers().contains(user) && project.getOrganizers().size() > 1) {
            removeOrganizer(project, user.getId(), false);
        }
        projectRepository.save(project);
    }

    public void addToFavorites(Long projectId, User loggedUser) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(loggedUser.getId());
        user.addProject(project);
        userRepository.save(user);
    }

    public void removeFavorite(Long projectId, User loggedUser) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(loggedUser.getId());
        user.removeProject(project);
        userRepository.save(user);
    }

    public void addTag(Project project, Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        if (project.getTags().contains(tag)) {
            return;
        }
        project.addTag(tag);
        projectRepository.save(project);
    }

    public void removeTag(Project project, Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        if (!project.getTags().contains(tag)) {
            return;
        }
        project.removeTag(tag);
        projectRepository.save(project);
    }

    public void addMember(Project project, Long userId) {
        User user = userService.getUserById(userId);
        if (!user.isActive() || user.isDeleted()) {
            return;
        }
        if (project.getMembers().contains(user)) {
            return;
        }
        ProjectMember projectMember = projectMemberRepository.save(new ProjectMember(project, user));
        project.addProjectMember(projectMember);
        projectRepository.save(project);
    }

    public void removeMember(Project project, Long userId, boolean logEvent) {
        User user = userService.getUserById(userId);
        if (!project.getMembers().contains(user)) {
            return;
        }
        Optional<ProjectMember> projectMemberOptional = projectMemberRepository.getProjectMemberByUserIdAndProjectId(userId, project.getId());
        if (projectMemberOptional.isEmpty()) {
            return;
        }
        ProjectMember projectMember = projectMemberOptional.get();
        project.removeProjectMember(projectMember);
        projectRepository.save(project);
    }

    public void addOrganizer(Project project, Long userId) {
        User user = userService.getUserById(userId);
        if (!user.isActive() || user.isDeleted()) {
            return;
        }
        if (project.getOrganizers().contains(user)) {
            return;
        }
        ProjectOrganizer projectOrganizer = projectOrganizerRepository.save(new ProjectOrganizer(project, user));
        project.addProjectOrganizer(projectOrganizer);
        projectRepository.save(project);
    }

    public void removeOrganizer(Project project, Long userId, boolean logEvent) {
        User user = userService.getUserById(userId);
        if (!project.getOrganizers().contains(user)) {
            return;
        }
        Optional<ProjectOrganizer> projectOrganizerOptional = projectOrganizerRepository.getProjectOrganizerByUserIdAndProjectId(userId, project.getId());
        if (projectOrganizerOptional.isEmpty()) {
            return;
        }
        ProjectOrganizer projectOrganizer = projectOrganizerOptional.get();
        project.removeProjectOrganizer(projectOrganizer);
        projectRepository.save(project);
    }
}
