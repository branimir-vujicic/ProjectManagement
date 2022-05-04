package rs.ac.su.vts.pm.projectmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.su.vts.pm.projectmanagement.exception.NotAuthorizedException;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.project.ProjectUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserMapper;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.services.ProjectService;
import rs.ac.su.vts.pm.projectmanagement.services.UserService;
import rs.ac.su.vts.pm.projectmanagement.services.auth.ContextService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
@AllArgsConstructor
@Tag(name = "Project Controller", description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Projects.")
@Slf4j
public class ProjectController
{

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "retrieves details about Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public ProjectDto getById(@Valid @Parameter(description = "Project id") @PathVariable("id") Long id)
    {
        User user = userService.getUserById(ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new).getId());
        ProjectDto projectDto = ProjectMapper.INSTANCE.toDto(projectService.getProjectById(id));
        projectDto.setFavorite(user.isFavorite(projectDto.getId()));
        return projectDto;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lists Projects by given filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page wrapper for Project"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public Page<ProjectDto> listProjects(ProjectListRequest request)
    {
        User user = userService.getUserById(ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new).getId());
        request.setUser(user);
        int size = request.getSize();
        int page = request.getPage();
        final Page<Project> projects = projectService.listProjects(request);
        final Long count = projectService.countProjects(request);
        List<ProjectDto> projectDtoList = projects.getContent().stream().map(ProjectMapper.INSTANCE::toDto).collect(Collectors.toList());

        return new PageImpl<>(projectDtoList, PageRequest.of(page, size, projects.getPageable().getSort()), count);
    }

    @GetMapping(value = "/{id}/members", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lists Project Members")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page wrapper for User"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public List<UserDto> listProjectMembers(@Valid @Parameter(description = "Project id") @PathVariable("id") Long projectId)
    {
        List<User> members = userService.listMembersByProject(projectId);
        return members.stream().map(UserMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}/organizers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lists Project Organizers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page wrapper for User"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public List<UserDto> listProjectOrganizers(@Valid @Parameter(description = "Project id") @PathVariable("id") Long projectId)
    {
        List<User> organizers = userService.listOrganizersByProject(projectId);
        return organizers.stream().map(UserMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates new Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public ProjectDto create(@RequestBody @Valid ProjectCreateRequest request)
    {
        return ProjectMapper.INSTANCE.toDto(projectService.createProject(request));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates Project with new name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public ProjectDto update(@Parameter(description = "Project update request") @RequestBody ProjectUpdateRequest projectUpdateRequest,
            @Parameter(description = "Project id") @PathVariable("id") Long projectId)
    {
        Project project = projectService.getProjectById(projectId);
        // ContextService.isOrganizer(project);
        return ProjectMapper.INSTANCE.toDto(projectService.updateProject(project, projectUpdateRequest));
    }

    @PutMapping(value = "/{id}/leave", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Leave Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User left the Project"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void leaveProject(@Parameter(description = "Project id") @PathVariable("id") Long id)
    {
        projectService.leaveProject(id, ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new));
    }

    @PutMapping(value = "/{id}/favorite/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add Project to favorites ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Added Project to favorites"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void addFavorite(@Parameter(description = "Project id") @PathVariable("id") Long id)
    {
        User loggedUser = ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new);
        projectService.addToFavorites(id, loggedUser);
    }

    @PutMapping(value = "/{id}/favorite/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add Project to favorites ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Added Project to favorites"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void removeFavorite(@Parameter(description = "Project id") @PathVariable("id") Long id)
    {
        projectService.removeFavorite(id, ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes (sets active to false) Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project deleted"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @RolesAllowed("ADMIN")
    public void delete(@Parameter(description = "Project id") @PathVariable("id") Long id)
    {
        projectService.deleteProject(id, ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new));
    }

    @PutMapping("/{id}/undelete")
    @Operation(summary = "Deletes (sets active to false) Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project is undeleted"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @RolesAllowed({"ADMIN"})
    public void undelete(@Parameter(description = "Project id") @PathVariable("id") Long id)
    {
        projectService.undeleteProject(id, ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new));
    }

    @PutMapping(value = "/{id}/tag/{tagId}/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds Tag to Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag added to project"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void addTag(@Parameter(description = "Project id") @PathVariable("id") Long projectId,
            @Parameter(description = "Tag id") @PathVariable("tagId") Long tagId)
    {
        Project project = projectService.getProjectById(projectId);
        projectService.addTag(project, tagId);
    }

    @DeleteMapping(value = "/{id}/tag/{tagId}/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Removed Tag fromm Project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag removed from project"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void removeTag(@Parameter(description = "Project id") @PathVariable("id") Long projectId,
            @Parameter(description = "Tag id") @PathVariable("tagId") Long tagId)
    {
        Project project = projectService.getProjectById(projectId);
        projectService.removeTag(project, tagId);
    }

    @PutMapping(value = "/{id}/member/{userId}/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds User to Project as member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User added to Project as Member"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void addMember(@Parameter(description = "Project id") @PathVariable("id") Long projectId,
            @Parameter(description = "Tag id") @PathVariable("userId") Long userId)
    {
        Project project = projectService.getProjectById(projectId);
        projectService.addMember(project, userId);
    }

    @DeleteMapping(value = "/{id}/member/{userId}/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Removes User from Project as member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User removed from Project as Member"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void removeMember(@Parameter(description = "Project id") @PathVariable("id") Long projectId,
            @Parameter(description = "Tag id") @PathVariable("userId") Long userId)
    {
        Project project = projectService.getProjectById(projectId);
        projectService.removeMember(project, userId, true);
    }

    @PutMapping(value = "/{id}/organizer/{userId}/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds User to Project as organizer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User added to Project as Organizer"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void addOrganizer(@Parameter(description = "Project id") @PathVariable("id") Long projectId,
            @Parameter(description = "Tag id") @PathVariable("userId") Long userId)
    {
        Project project = projectService.getProjectById(projectId);
        projectService.addOrganizer(project, userId);
    }

    @DeleteMapping(value = "/{id}/organizer/{userId}/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Removes User from Project as organizer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User removed from Project as Organizer"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void removeOrganizer(@Parameter(description = "Project id") @PathVariable("id") Long projectId,
            @Parameter(description = "Tag id") @PathVariable("userId") Long userId)
    {
        Project project = projectService.getProjectById(projectId);
        projectService.removeOrganizer(project, userId, true);
    }
}
