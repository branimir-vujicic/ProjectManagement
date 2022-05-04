package rs.ac.su.vts.pm.projectmanagement.model.dto.tag;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Tag;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper
{

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagDto toDto(Tag tag);

    //    @Mappings({
//            @Mapping(target = "createdOn", expression = "java( (tag.getCreatedOn() == null) ? null : (tag.getCreatedOn().toLocalDateTime()) )")
//    })
    TagDetailsDto toDetailsDto(Tag tag);

    void fromDto(TagDto tagDto, @MappingTarget Tag tag);

    Tag from(TagCreateRequest tagCreateRequest);

    Tag from(TagUpdateRequest tagUpdateRequest);
}

