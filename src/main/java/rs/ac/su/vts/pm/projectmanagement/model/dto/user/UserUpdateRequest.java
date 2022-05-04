package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.common.UserRole;

import javax.validation.constraints.Email;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest
{

    private String name;

    @Email
    private String email;

    @Schema(description = "Comma separated list of user roles", example = "ADMIN,USER")
    private Set<UserRole> roles;
}
