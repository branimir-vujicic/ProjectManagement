package rs.ac.su.vts.pm.projectmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.services.CommentService;
import rs.ac.su.vts.pm.projectmanagement.services.auth.ContextService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project/{projectId}/comment")
@AllArgsConstructor
@Tag(name = "Comment Controller", description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Comments.")
public class CommentController
{

    private final CommentService commentService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "retrieves details about Comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public CommentDto getById(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
            @Valid @Parameter(description = "Comment id") @PathVariable("id") Long chatId)
    {
        return CommentMapper.INSTANCE.toDto(commentService.getCommentById(projectId, chatId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lists Comments by given filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page wrapper for Comment"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public Page<CommentDto> listComments(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
            CommentListRequest request)
    {
        request.setProjectId(projectId);
        return commentService.listComments(request).map(CommentMapper.INSTANCE::toDto);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates new Comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public CommentDto create(
            @Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
            @RequestBody @Valid CommentCreateRequest request)
    {
        User user = ContextService.getLoggedUser().orElseThrow(NotAuthorizedException::new);
        request.setProjectId(projectId);
        request.setUserId(user.getId());
        return CommentMapper.INSTANCE.toDto(commentService.createComment(request));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates Comment with new name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public CommentDto update(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
            @Parameter(description = "Comment update request") @RequestBody CommentUpdateRequest CommentUpdateRequest,
            @Parameter(description = "Comment id") @PathVariable("id") Long id)
    {
        CommentUpdateRequest.setId(id);
        return CommentMapper.INSTANCE.toDto(commentService.updateComment(CommentUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes (sets active to false) Comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void delete(@Valid @Parameter(description = "Project id") @PathVariable("projectId") Long projectId,
            @Parameter(description = "Comment id") @PathVariable("id") Long id)
    {
        commentService.deleteComment(id);
    }
}
