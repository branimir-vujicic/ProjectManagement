package rs.ac.su.vts.pm.projectmanagement.model.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Project Create Request Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCreateRequest
{

    @NotBlank(message = "Title is mandatory")
    @Schema(required = true, description = "project title", example = "Test Project")
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Schema(required = true, description = "project description", example = "Description of a Test Project")
    private String description;

    @Builder.Default
    @Schema(description = "list of organizer (user) ids", example = "1, 2, 3")
    private Set<Long> organizerIds = new HashSet<>();
    @Builder.Default
    @Schema(description = "list of member (user) ids", example = "1, 2, 3")
    private Set<Long> memberIds = new HashSet<>();
    @Builder.Default
    @Schema(description = "list of tag ids", example = "1, 2, 3")
    private Set<Long> tagIds = new HashSet<>();

    @Schema(description = "project start date, format yyyy-MM-dd", example = "2021-10-24")
    private LocalDate date;

    @Schema(description = "project start time, format HH:mm:ss", example = "21:59:59")
    private LocalTime timeFrom;

    @Schema(description = "project end time, format HH:mm:ss", example = "21:59:59")
    private LocalTime timeTo;
}
