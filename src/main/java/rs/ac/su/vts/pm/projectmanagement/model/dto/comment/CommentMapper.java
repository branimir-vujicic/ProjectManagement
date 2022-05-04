package rs.ac.su.vts.pm.projectmanagement.model.dto.comment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.ac.su.vts.pm.projectmanagement.model.dto.UserDtoMapper;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Comment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper
        extends UserDtoMapper
{

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toDto(Comment comment);

    void fromDto(CommentDto commentDto, @MappingTarget Comment comment);

    Comment from(CommentCreateRequest commentCreateRequest);

    Comment from(CommentUpdateRequest commentUpdateRequest);

    //    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateRequest(CommentUpdateRequest request, @MappingTarget Comment comment);
}

