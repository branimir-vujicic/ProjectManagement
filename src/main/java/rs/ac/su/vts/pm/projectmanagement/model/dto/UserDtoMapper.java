package rs.ac.su.vts.pm.projectmanagement.model.dto;

import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserMapper;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

public interface UserDtoMapper
{

    default UserDto userToUserDto(User user)
    {
        return UserMapper.INSTANCE.toDto(user);
    }

    default User userDtoToUser(UserDto userDto)
    {
        return UserMapper.INSTANCE.fromDto(userDto);
    }
}
