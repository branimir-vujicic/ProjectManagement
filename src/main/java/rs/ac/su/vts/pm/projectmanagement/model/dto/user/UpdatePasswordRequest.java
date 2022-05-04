package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest
{

    @Email
    @Schema(description = "User's email")
    private String email;

    @NotEmpty
    @Schema(description = "User's current password")
    private String oldPassword;

    @NotEmpty
    @Schema(description = "User's new password")
    private String password;

    @NotEmpty
    @Schema(description = "User's new confirmed password")
    private String confirmedPassword;
}
