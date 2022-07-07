package rs.ac.su.vts.pm.projectmanagement.model.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.common.Status;

import javax.validation.constraints.NotBlank;

@Schema(description = "Task Create Request Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskUpdateRequest {

    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Schema(required = true, description = "Task title", example = "Test Task")
    private String title;

    @NotBlank(message = "Text is mandatory")
    @Schema(required = true, description = "Task text", example = "Text of a Test Task")
    private String text;

    @Schema(required = false, description = "Assignee", example = "Assignee Id")
    private Long userId;

    @Schema(description = "Task status")
    private Status status;

}
