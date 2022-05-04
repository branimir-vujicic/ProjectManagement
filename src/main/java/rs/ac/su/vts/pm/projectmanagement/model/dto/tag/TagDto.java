package rs.ac.su.vts.pm.projectmanagement.model.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Tag Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto
{

    @Schema(description = "tag id", example = "111")
    private Long id;

    @Schema(description = "tag name", example = "Important")
    private String name;

    @Schema(description = "tag color", example = "Red")
    private String color;
}
