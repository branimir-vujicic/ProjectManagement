package rs.ac.su.vts.pm.projectmanagement.model.dto.task;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.ac.su.vts.pm.projectmanagement.model.dto.UserDtoMapper;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Task;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper
        extends UserDtoMapper
{

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto toDto(Task task);

    void fromDto(TaskDto taskDto, @MappingTarget Task task);

    Task from(TaskCreateRequest taskCreateRequest);

    Task from(TaskUpdateRequest taskUpdateRequest);

    //    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateRequest(TaskUpdateRequest request, @MappingTarget Task task);
}

