package rs.ac.su.vts.pm.projectmanagement.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.su.vts.pm.projectmanagement.exception.NotFoundException;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.comment.CommentUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Comment;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.repository.CommentRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService
{

    private final CommentRepository commentRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public Comment getCommentById(final Long id)
    {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment not found: %s", id)));
    }

    public Page<Comment> listComments(CommentListRequest request)
    {
        BooleanBuilder predicate = request.predicate();
        return commentRepository.findAll(predicate, request.pageable());
    }

    public Comment updateComment(CommentUpdateRequest request)
    {
        Comment comment = getCommentById(request.getId());
        CommentMapper.INSTANCE.fromUpdateRequest(request, comment);
        commentRepository.save(comment);
        return comment;
    }

    public Comment createComment(CommentCreateRequest request)
    {
        Comment comment = CommentMapper.INSTANCE.from(request);
        comment.setTime(LocalDateTime.now());
        Project project = projectService.getProjectById(request.getProjectId());
        project.addComment(comment);
        User user = userService.getUserById(request.getUserId());
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id)
    {
        Comment comment = getCommentById(id);
        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    public Comment getCommentById(Long projectId, Long chatId)
    {
        return commentRepository.findCommentByProject_IdAndId(projectId, chatId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment not found: %s", chatId)));
    }
}
