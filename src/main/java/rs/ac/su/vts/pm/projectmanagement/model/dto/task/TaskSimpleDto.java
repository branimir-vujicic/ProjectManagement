package rs.ac.su.vts.pm.projectmanagement.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserSimpleDto;

import javax.persistence.Column;

import java.time.LocalDateTime;

@Schema(description = "Task Response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSimpleDto
{

    @Schema(description = "Task id")
    private Long id;

    @Schema(description = "Task title")
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Schema(description = "task author")
    private UserSimpleDto user;
}
