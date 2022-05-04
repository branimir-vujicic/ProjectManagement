package rs.ac.su.vts.pm.projectmanagement.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest
{

	@NotEmpty
	private String email;
	@NotEmpty
	private String password;
}
