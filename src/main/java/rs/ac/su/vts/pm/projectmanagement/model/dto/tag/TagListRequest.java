package rs.ac.su.vts.pm.projectmanagement.model.dto.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.BooleanBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rs.ac.su.vts.pm.projectmanagement.model.dto.PageableFilter;
import rs.ac.su.vts.pm.projectmanagement.model.entity.QTag;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "list of filters")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagListRequest
        extends PageableFilter
{

    @Schema(description = "list of tag ids", example = "1,2,3")
    private List<Long> ids;
    @Schema(description = "part of tag's name")
    private String name;
    @Schema(description = "tag color", example = "Red")
    private String color;
    @Schema(description = "should include deleted items")
    @Builder.Default
    private Boolean includeDeleted = false;

    @JsonIgnore
    @Schema(description = "for internal use only", hidden = true)
    public BooleanBuilder predicate()
    {
        BooleanBuilder predicate = new BooleanBuilder();
        final var tag = QTag.tag;
        if (StringUtils.hasText(name)) {
            predicate.and(tag.name.containsIgnoreCase(name));
        }
        if (StringUtils.hasText(color)) {
            predicate.and(tag.color.containsIgnoreCase(color));
        }
        if (!CollectionUtils.isEmpty(ids)) {
            predicate.and(tag.id.in(ids));
        }
        if (includeDeleted == null || !includeDeleted) {
            predicate.and(tag.deleted.eq(false));
        }
        return predicate;
    }
}
