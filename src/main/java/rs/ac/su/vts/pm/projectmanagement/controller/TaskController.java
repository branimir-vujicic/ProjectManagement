package rs.ac.su.vts.pm.projectmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.ac.su.vts.pm.projectmanagement.model.common.Status;
import rs.ac.su.vts.pm.projectmanagement.model.dto.task.*;
import rs.ac.su.vts.pm.projectmanagement.services.TaskService;
import rs.ac.su.vts.pm.projectmanagement.services.UserService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/project/{projectId}/task")
@AllArgsConstructor
@Tag(name = "Task Controller", description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Tasks.")
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "retrieves details about Task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public TaskDto getById(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                           @Valid @Parameter(description = "Task id") @PathVariable("id") Long id) {
        return TaskMapper.INSTANCE.toDto(taskService.getTaskById(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lists Tasks by given filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page wrapper for Task"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public Page<TaskDto> listTasks(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                                   @Valid TaskListRequest request) {
        request.setProjectId(projectId);
        return taskService.listTasks(request).map(TaskMapper.INSTANCE::toDto);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates new Task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public TaskDto create(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                          @RequestBody @Valid TaskCreateRequest request) {
        request.setProjectId(projectId);
        return TaskMapper.INSTANCE.toDto(taskService.createTask(request));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates Task with new name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public TaskDto update(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                          @Parameter(description = "Task update request") @Valid @RequestBody TaskUpdateRequest taskUpdateRequest,
                          @Parameter(description = "Task id") @PathVariable("id") Long id) {
        return TaskMapper.INSTANCE.toDto(taskService.updateTask(taskUpdateRequest));
    }

    @PutMapping(value = "/{id}/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates Task status ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updates Task status"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void updateStatus(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                             @Valid @Parameter(description = "Task id") @PathVariable("id") Long id,
                             @Valid @Parameter(description = "Status") @PathVariable("status") Status status) {
        taskService.updateTaskStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes (sets active to false) Task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @RolesAllowed("ADMIN")
    public void delete(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                       @Valid @Parameter(description = "Task id") @PathVariable("id") Long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}/undelete")
    @Operation(summary = "Deletes (sets active to false) Task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task is undeleted"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @RolesAllowed({"ADMIN"})
    public void undelete(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
                         @Valid @Parameter(description = "Task id") @PathVariable("id") Long id) {
        taskService.undeleteTask(id);
    }

}
