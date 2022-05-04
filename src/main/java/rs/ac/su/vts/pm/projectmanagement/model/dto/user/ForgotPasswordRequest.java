package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest
{

    @NotEmpty
    @Email
    private String email;
}
