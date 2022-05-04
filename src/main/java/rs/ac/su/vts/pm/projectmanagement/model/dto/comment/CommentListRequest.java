package rs.ac.su.vts.pm.projectmanagement.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.BooleanBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import rs.ac.su.vts.pm.projectmanagement.model.dto.PageableFilter;
import rs.ac.su.vts.pm.projectmanagement.model.entity.QComment;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "list of filters")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListRequest
        extends PageableFilter
{

    @Schema(description = "list of comment ids", example = "1,2,3")
    private List<Long> ids;
    @Schema(description = "list of User ids", example = "1,2,3")
    private List<Long> userIds;
    @Schema(description = "Project id")
    private Long projectId;
    @Schema(description = "should include deleted items")
    @Builder.Default
    private Boolean includeDeleted = false;

    @JsonIgnore
    @Schema(description = "for internal use only", hidden = true)
    public BooleanBuilder predicate()
    {
        BooleanBuilder predicate = new BooleanBuilder();
        final var comment = QComment.comment;

        if (!CollectionUtils.isEmpty(ids)) {
            predicate.and(comment.id.in(ids));
        }
        if (Objects.nonNull(projectId)) {
            predicate.and(comment.project.id.eq(projectId));
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            predicate.and(comment.user.id.in(userIds));
        }
        if (includeDeleted == null || !includeDeleted) {
            predicate.and(comment.deleted.eq(false));
        }
        return predicate;
    }
}
