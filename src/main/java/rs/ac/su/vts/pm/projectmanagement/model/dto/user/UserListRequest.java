package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rs.ac.su.vts.pm.projectmanagement.model.dto.PageableFilter;
import rs.ac.su.vts.pm.projectmanagement.model.entity.QUser;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListRequest
        extends PageableFilter
{

    @Schema(description = "list of user ids", example = "1,2,3")
    private List<Long> ids;
    @Schema(description = "part of user's name")
    private String name;
    @Schema(description = "part of user's email")
    private String email;
    @Schema(description = "filter users by active", allowableValues = {"ALL", "ACTIVE", "INACTIVE"}, defaultValue = "ALL", example = "ALL")
    @Builder.Default
    private Active active = Active.ALL;
    @Schema(description = "should include deleted items", defaultValue = "false")
    @Builder.Default
    private Boolean includeDeleted = false;

    @JsonIgnore
    @Schema(description = "for internal use only", hidden = true)
    public BooleanBuilder predicate()
    {
        BooleanBuilder predicate = new BooleanBuilder();
        final var user = QUser.user;
        if (StringUtils.hasText(name)) {
            predicate.and(user.name.containsIgnoreCase(name));
        }
        if (StringUtils.hasText(email)) {
            predicate.and(user.email.containsIgnoreCase(email));
        }
        if (!CollectionUtils.isEmpty(ids)) {
            predicate.and(user.id.in(ids));
        }
        predicate.and(active.expression());
        if (!BooleanUtils.isTrue(includeDeleted)) {
            predicate.and(user.deleted.eq(false));
        }
        return predicate;
    }

    public enum Active
    {
        ALL(
                Expressions.asBoolean(true).isTrue()
        ),
        ACTIVE(
                QUser.user.active.eq(true).and(QUser.user.passwordRecoveryKey.isNull())
        ),
        INACTIVE(
                QUser.user.active.eq(false)
        );

        private final BooleanExpression expression;

        Active(BooleanExpression expression)
        {
            this.expression = expression;
        }

        BooleanExpression expression()
        {
            return expression;
        }
    }
}
