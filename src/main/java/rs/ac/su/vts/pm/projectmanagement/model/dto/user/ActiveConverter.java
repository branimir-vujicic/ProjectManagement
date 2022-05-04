package rs.ac.su.vts.pm.projectmanagement.model.dto.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ActiveConverter
        implements Converter<String, UserListRequest.Active>
{

    @Override
    public UserListRequest.Active convert(String value)
    {
        return UserListRequest.Active.valueOf(value.toUpperCase());
    }
}
