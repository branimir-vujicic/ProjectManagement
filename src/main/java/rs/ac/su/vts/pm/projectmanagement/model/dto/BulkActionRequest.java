package rs.ac.su.vts.pm.projectmanagement.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Bulk Action Model")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkActionRequest
{

    @Schema(description = "list of ids", example = "1,2,3")
    private List<Long> ids;
}
