package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper
{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings(
            @Mapping(target = "passwordRecoveryTime",
                    expression = "java((user.getPasswordRecoveryTime() == null) ? null : (java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(user.getPasswordRecoveryTime()), java.time.ZoneId.systemDefault())))")
    )
    UserDto toDto(User user);

    UserLoginDto toLoginDto(User user);

    @Mapping(target = "passwordRecoveryTime", ignore = true)
    void fromDto(UserDto userDto, @MappingTarget User user);

    @Mapping(target = "passwordRecoveryTime", ignore = true)
    User fromDto(UserDto userDto);

    User from(UserCreateRequest userCreateRequest);

    User from(UserUpdateRequest userUpdateRequest);
}
