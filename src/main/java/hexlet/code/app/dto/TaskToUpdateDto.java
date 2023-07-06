package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskToUpdateDto {
    @NotBlank
    private String name;

    private String description;

    private Long executorId;

    @NotNull
    private Long taskStatusId;
}
