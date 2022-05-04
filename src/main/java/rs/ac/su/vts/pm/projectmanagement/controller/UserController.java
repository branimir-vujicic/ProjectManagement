package rs.ac.su.vts.pm.projectmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.su.vts.pm.projectmanagement.exception.NotAuthorizedException;
import rs.ac.su.vts.pm.projectmanagement.exception.NotFoundException;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.CreatePasswordRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.ForgotPasswordRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UpdatePasswordRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.services.UserService;
import rs.ac.su.vts.pm.projectmanagement.services.auth.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController
{

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/me")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public UserDto me(@RequestHeader("Authorization") String token)
    {
        try {
            return UserMapper.INSTANCE.toDto(authService.getUser(token));
        }
        catch (NotFoundException e) {
            throw new NotAuthorizedException();
        }
    }

    @GetMapping("/{id}/get")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public UserDto get(@Valid @PathVariable("id") Long id)
    {
        return UserMapper.INSTANCE.toDto(userService.getUserById(id));
    }

    @PostMapping
    @Operation(method = "POST", summary = "Creates new User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "Unit and/or Position not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public UserDto create(@RequestBody @Valid UserCreateRequest user)
    {
        return UserMapper.INSTANCE.toDto(userService.addUser(user));
    }

    @PutMapping(value = "/{id}")
    @Operation(method = "PUT", summary = "Updates User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User DTO"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "404", description = "Unit and/or Position not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public UserDto update(@Valid @PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest userUpdateRequest)
    {
        return UserMapper.INSTANCE.toDto(userService.updateUser(id, userUpdateRequest));
    }

    @PutMapping("/forgot-password")
    @Operation(summary = "sends create password email to user",
            description = "Used when user is forgotten password and method sends create password email to user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message sent"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest)
    {
        userService.forgotPassword(forgotPasswordRequest);
    }

    @PutMapping("/create-password")
    @Operation(summary = "creates new password for user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password created"),
            @ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    public void createPassword(@RequestBody @Valid CreatePasswordRequest createPasswordRequest)
    {
        userService.createPassword(createPasswordRequest);
    }

    @PutMapping("/change-password")
    @Operation(summary = "changes current password for user")
    public void changePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest)
    {
        userService.updatePassword(updatePasswordRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deletes user - NOT USED")
    public void delete(@Valid @PathVariable("id") Long id)
    {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/enable")
    @Operation(summary = "sets user to status active")
    public void enable(@Valid @PathVariable("id") Long id)
    {
        userService.enableUser(id);
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "sets user to status inactive")
    public void disable(@Valid @PathVariable("id") Long id)
    {
        userService.disableUser(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lists users by given filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page wrapper for User"),
    })
    public Page<UserDto> listUsers(UserListRequest request)
    {
        return userService.listUsers(request).map(UserMapper.INSTANCE::toDto);
    }
}
