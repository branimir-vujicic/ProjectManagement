package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Data
@Schema(description = "User response")
public class UserDto
{

    private Long id;
    private String email;
    private String name;

    private Date lastLoginTime;
    private boolean active = true;

    private Set<String> roles;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime createdOn;

    @Schema(description = "password recovery key")
    private String passwordRecoveryKey;

    @Schema(description = "password recovery deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passwordRecoveryTime;
}
