package rs.ac.su.vts.pm.projectmanagement.model.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Schema(description = "Tag Create Request Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagUpdateRequest
{

    @Schema(hidden = true)
    private Long id;

    @NotBlank
    @Length(max = 64, message = "Name is too long")
    @Schema(description = "tag name", example = "Important")
    private String name;

    @Schema(description = "tag color", example = "Red")
    private String color;
}
