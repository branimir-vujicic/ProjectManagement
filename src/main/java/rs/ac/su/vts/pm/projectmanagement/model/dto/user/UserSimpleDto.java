package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User response")
public class UserSimpleDto
{
    private Long id;
    private String email;
    private String name;
}
