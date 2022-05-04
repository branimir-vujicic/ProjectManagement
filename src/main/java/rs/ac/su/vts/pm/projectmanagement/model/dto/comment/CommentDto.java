package rs.ac.su.vts.pm.projectmanagement.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserSimpleDto;

import java.time.LocalDateTime;

@Schema(description = "Comment Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto
{

    @Schema(description = "Comment id")
    private Long id;

    @Schema(description = "Comment title")
    private String title;

    @Schema(description = "Comment text")
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Schema(description = "comment author")
    private UserSimpleDto user;
}
