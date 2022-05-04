package rs.ac.su.vts.pm.projectmanagement.model.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rs.ac.su.vts.pm.projectmanagement.model.common.UserRole;
import rs.ac.su.vts.pm.projectmanagement.model.dto.PageableFilter;
import rs.ac.su.vts.pm.projectmanagement.model.entity.QProject;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "list of filters")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListRequest
        extends PageableFilter
{

    @Schema(description = "list of project ids", example = "1,2,3")
    private List<Long> ids;
    @Schema(description = "part of project's title")
    private String title;
    @Schema(description = "is project active")
    @Builder.Default
    private Boolean active = true;
    @Schema(description = "is project deleted")
    @Builder.Default
    private Boolean deleted = false;
    @Schema(description = "the date range start date", example = "2021-10-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;
    @Schema(description = "the date range end date", example = "2021-10-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;

    @Schema(description = "the year part of the date", example = "2022")
    @Range(min = 2000, max = 2030)
    private Integer year;
    @Schema(description = "the month part of project date", example = "1 - for january")
    @Range(min = 1, max = 12)
    private Integer month;
    @Schema(description = "the day part of date", example = "1 - for first day in month")
    @Range(min = 1, max = 31)
    private Integer day;
    @Schema(description = "list of Tag ids", example = "1,2,3")
    private List<Long> tagIds;

    @Hidden
    private User user;

    @Hidden
    private boolean favoriteSort;

    @Schema(description = "is project user's favorite", example = "true")
    private boolean favorite;

    @Schema(description = "in case of ADMIN user filters only projects in which ADMIN user is member or organizer, for regular user param is ignored", example = "true")
    @Builder.Default
    private boolean own = false;

    @JsonIgnore
    @Schema(description = "for internal use only", hidden = true)
    public BooleanBuilder predicate()
    {
        BooleanBuilder predicate = new BooleanBuilder();
        final var project = QProject.project;
        if (StringUtils.hasText(title)) {
            predicate.and(project.title.containsIgnoreCase(title));
        }
        if (!CollectionUtils.isEmpty(ids)) {
            predicate.and(project.id.in(ids));
        }
        if (!CollectionUtils.isEmpty(tagIds)) {
            predicate.and(project.tags.any().id.in(tagIds));
        }
        if (dateFrom != null) {
            predicate.and(project.date.eq(dateFrom).or(project.date.after(dateFrom)));
        }
        if (dateTo != null) {
            predicate.and(project.date.eq(dateTo).or(project.date.before(dateTo)));
        }
        if (year != null) {
            predicate.and(project.date.year().eq(year));
        }
        if (month != null) {
            predicate.and(project.date.month().eq(month));
        }
        if (day != null) {
            predicate.and(project.date.dayOfMonth().eq(day));
        }
        if (favorite) {
            predicate.and(project.usersFavorites.contains(user));
        }

        if ((!user.hasRole(UserRole.ADMIN)) || (user.hasRole(UserRole.ADMIN) && own)) {
//			if (!user.hasRole(UserRole.ADMIN)) {
            BooleanExpression memberPred = project.projectMembers.any().user.eq(user);
            memberPred = memberPred.or(project.projectOrganizers.any().user.eq(user));
            predicate.and(memberPred);
        }
        if (deleted == null || !deleted) {
            predicate.and(project.deleted.eq(false));
        }
        else {
            predicate.and(project.deleted.eq(true));
        }

        return predicate;
    }
}
