package rs.ac.su.vts.pm.projectmanagement.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserSimpleDto;

import java.time.LocalDateTime;

@Schema(description = "Task Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto
{

    @Schema(description = "Task id")
    private Long id;

    @Schema(description = "Task title")
    private String title;

    @Schema(description = "Task text")
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Schema(description = "task author")
    private UserSimpleDto author;

    @Schema(description = "task user")
    private UserSimpleDto user;
}
