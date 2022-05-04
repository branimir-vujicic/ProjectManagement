package rs.ac.su.vts.pm.projectmanagement.model.dto.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Tag Details Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDetailsDto
{

    @Schema(description = "tag id", example = "111")
    private Long id;

    @Schema(description = "tag name", example = "Important")
    private String name;

    @Schema(description = "tag color", example = "Red")
    private String color;

    @Schema(description = "number of projects tag appears ", example = "22")
    private Long numberOfProject;

    @Schema(description = "date created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
