package rs.ac.su.vts.pm.projectmanagement.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rs.ac.su.vts.pm.projectmanagement.exception.NotFoundException;
import rs.ac.su.vts.pm.projectmanagement.exception.ObjectInUseException;
import rs.ac.su.vts.pm.projectmanagement.exception.TagExistsException;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagDetailsDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Tag;
import rs.ac.su.vts.pm.projectmanagement.repository.ProjectRepository;
import rs.ac.su.vts.pm.projectmanagement.repository.TagRepository;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService
{

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    public Tag getTagById(final Long id)
    {
        return tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("Tag not found: %s", id)));
    }

    public Page<Tag> listTags(TagListRequest request)
    {
        return tagRepository.findAll(request.predicate(), request.pageable());
    }

    public Tag updateTag(TagUpdateRequest request)
    {
        Tag tag = getTagById(request.getId());
        validateTagName(request.getName(), tag);
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        return tagRepository.save(tag);
    }

    public Tag createTag(TagCreateRequest request)
    {
        validateTagName(request.getName(), null);
        Tag tag = TagMapper.INSTANCE.from(request);
        return tagRepository.save(tag);
    }

    private void validateTagName(String name, Tag tag)
    {
        Optional<Tag> fromDb = tagRepository.findByNameIgnoreCaseAndDeletedIsFalse(name);
        if (fromDb.isPresent()) {
            if (Objects.isNull(tag) || !Objects.equals(fromDb.get().getId(), tag.getId())) {
                throw new TagExistsException(format("Tag with name %s already exists !", name));
            }
        }
    }

    public void deleteTag(Long id)
    {
        Tag tag = getTagById(id);

        long projectNo = projectRepository.countAllByTags_id(id);
        if (projectNo > 0) {
            throw new ObjectInUseException("Tag in use", tag.getId());
        }

        tag.setDeleted(true);
        tagRepository.save(tag);
    }

    public Page<TagDetailsDto> listTagDetails(TagListRequest request)
    {
        return listTags(request).map(TagMapper.INSTANCE::toDetailsDto);
    }
}
