package rs.ac.su.vts.pm.projectmanagement.model.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Schema(description = "Project Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDto
{

    @Schema(description = "project id")
    private Long id;

    @Schema(description = "project title")
    private String title;

    @Schema(description = "project description")
    private String description;

    @Schema(description = "is unit archived")
    private Boolean archived;

    @Schema(description = "project start date", example = "2021-10-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "project start time", example = "16:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime timeFrom;

    @Schema(description = "project end time", example = "17:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime timeTo;

    @Schema(description = "project members")
    private List<UserDto> members;

    @Schema(description = "project organizers")
    private List<UserDto> organizers;

    @Schema(description = "project tags")
    private Set<TagDto> tags;

    @Schema(description = "last update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    @Schema(description = "is project user's (current user) favorite")
    private boolean favorite;
}
