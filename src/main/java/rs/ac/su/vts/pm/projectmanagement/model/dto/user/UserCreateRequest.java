package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.su.vts.pm.projectmanagement.model.common.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Create Request Model")
public class UserCreateRequest
{

    @NotBlank(message = "Email is mandatory")
    @Schema(required = true, description = "User's email", example = "user@domain.com")
    @Email
    private String email;

    @NotBlank(message = "Name is mandatory")
    @Schema(required = true, description = "User's name", example = "John Smith")
    private String name;

    @Schema(description = "Comma separated list of user roles", example = "ADMIN,USER")
    private List<UserRole> roles;
}
