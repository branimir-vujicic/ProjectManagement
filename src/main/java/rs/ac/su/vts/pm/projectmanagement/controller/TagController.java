package rs.ac.su.vts.pm.projectmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagCreateRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagDetailsDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagListRequest;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagMapper;
import rs.ac.su.vts.pm.projectmanagement.model.dto.tag.TagUpdateRequest;
import rs.ac.su.vts.pm.projectmanagement.services.TagService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project/tag")
@AllArgsConstructor
@Tag(name = "Tag Controller", description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Tags.")
public class TagController
{

	private final TagService tagService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "retrieves details about Tag")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Tag"),
			@ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
			@ApiResponse(responseCode = "404", description = "Tag not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
	})
	public TagDto getById(@Valid @Parameter(description = "Tag id") @PathVariable("id") Long id)
	{
		return TagMapper.INSTANCE.toDto(tagService.getTagById(id));
	}

	@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Lists Tags by given filters")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Page wrapper for Tag"),
			@ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
	})
	public Page<TagDetailsDto> listTagDetails(TagListRequest request)
	{
		return tagService.listTagDetails(request);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Lists Tags by given filters")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Page wrapper for Tag"),
			@ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
	})
	public Page<TagDto> listTags(TagListRequest request)
	{
		return tagService.listTags(request).map(TagMapper.INSTANCE::toDto);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates new Tag")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Tag DTO"),
			@ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
	})
	public TagDto create(@RequestBody @Valid TagCreateRequest request)
	{
		return TagMapper.INSTANCE.toDto(tagService.createTag(request));
	}

	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates Tag with new name")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Tag DTO"),
			@ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
			@ApiResponse(responseCode = "404", description = "Tag not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
	})
	public TagDto update(@Parameter(description = "Tag update request") @RequestBody TagUpdateRequest tagUpdateRequest,
			@Parameter(description = "Tag id") @PathVariable("id") Long id)
	{
		tagUpdateRequest.setId(id);
		return TagMapper.INSTANCE.toDto(tagService.updateTag(tagUpdateRequest));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletes (sets active to false) Tag")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Tag deleted"),
			@ApiResponse(responseCode = "400", description = "Validation error : invalid argument"),
			@ApiResponse(responseCode = "404", description = "Tag not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
	})
	public void delete(@Parameter(description = "Tag id") @PathVariable("id") Long id)
	{
		tagService.deleteTag(id);
	}
}
