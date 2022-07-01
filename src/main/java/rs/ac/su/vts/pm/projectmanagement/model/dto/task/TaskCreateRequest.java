package rs.ac.su.vts.pm.projectmanagement.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.common.Status;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "Task Create Request Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCreateRequest {

    @Schema(description = "Project id")
    private Long projectId;

    @Schema(description = "Author id")
    private Long authorId;

    @Schema(description = "User id")
    private Long userId;

    @Schema(required = true, description = "Task title", example = "Test Task")
    private String title;

    @NotBlank(message = "Text is mandatory")
    @Schema(required = true, description = "Task text", example = "Text of a Test Task")
    private String text;

    @Schema(description = "Task status")
    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
