package rs.ac.su.vts.pm.projectmanagement.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "Comment Create Request Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentCreateRequest
{

    @Schema(description = "Project id")
    private Long projectId;

    @Schema(description = "User id")
    private Long userId;

    @Schema(required = true, description = "Comment title", example = "Test Comment")
    private String title;

    @NotBlank(message = "Text is mandatory")
    @Schema(required = true, description = "Comment text", example = "Text of a Test Comment")
    private String text;
}
