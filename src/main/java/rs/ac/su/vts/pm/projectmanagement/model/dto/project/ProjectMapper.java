package rs.ac.su.vts.pm.projectmanagement.model.dto.project;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.ac.su.vts.pm.projectmanagement.model.dto.UserDtoMapper;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper
        extends UserDtoMapper
{

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    //    @Mappings({
//            @Mapping(target = "updatedOn", expression = "java((project.getUpdatedOn() == null) ? (project.getCreatedOn() == null ? null : project.getCreatedOn().toLocalDateTime()) : (project.getUpdatedOn().toLocalDateTime()))")
//    })
    ProjectDto toDto(Project project);

    void fromDto(ProjectDto projectDto, @MappingTarget Project project);

    void fromUpdateRequest(ProjectUpdateRequest request, @MappingTarget Project project);

    Project from(ProjectCreateRequest projectCreateRequest);

    Project fromDto(ProjectDto projectDto);
}

