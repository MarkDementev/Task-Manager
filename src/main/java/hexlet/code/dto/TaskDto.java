package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long executorId;

    @NotNull
    private Long taskStatusId;

    private List<Long> labelIds;

    @NotBlank
    private String name;

    private String description;
}
