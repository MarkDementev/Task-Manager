package hexlet.code.app.dto;

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
    @NotNull
    private Long authorId;

    private Long executorId;

    @NotNull
    private Long taskStatusId;

    private List<Long> labelsId;

    @NotBlank
    private String name;

    private String description;
}
