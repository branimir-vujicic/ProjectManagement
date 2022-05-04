package rs.ac.su.vts.pm.projectmanagement.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class PageableFilter
{

    @Schema(description = "response page number")
    protected Integer page = 0;
    @Schema(description = "response page size")
    protected Integer size = 20;

    @Schema(description = "result list sort direction", allowableValues = "ASC, DESC")
    protected Sort.Direction order = Sort.Direction.ASC;
    @Schema(description = "result list sort fields")
    protected List<String> sort;

    @JsonIgnore
    @Schema(hidden = true, accessMode = Schema.AccessMode.READ_ONLY)
    public Pageable pageable()
    {

        if (!CollectionUtils.isEmpty(sort)) {
            if (sort.get(0).contains(",")) {
                List<Sort.Order> orders = sort.stream().map(s -> {
                    String[] _sort = s.split("\\s*,\\s*");
                    String property = _sort[0].strip();

                    String orderPart = "";
                    if (_sort.length > 1) {
                        orderPart = Optional.ofNullable(_sort[1]).orElse("ASC").strip();
                    }

                    Sort.Direction direction = Sort.Direction.fromOptionalString(orderPart).orElse(Sort.Direction.ASC);
                    if (StringUtils.hasText(property)) {
                        return new Sort.Order(direction, property, Sort.NullHandling.NULLS_FIRST);
                    }
                    else {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(orders)) {
                    return PageRequest.of(page, size, Sort.by(orders));
                }
            }
            else {
                if (order != null) {
                    Sort sort = Sort.by(order, this.sort.toArray(new String[] {}));
                    return PageRequest.of(page, size, sort);
                }
            }
        }
        return PageRequest.of(page, size);
    }

    protected boolean sizeUpdated = false;

    public void setSize(Integer size)
    {
        this.size = size;
        sizeUpdated = true;
    }
}
