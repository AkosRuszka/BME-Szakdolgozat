package hu.bme.akos.ruszkabanyai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Set<String> meetingSet;
    private Set<String> participantSet;
    @NotNull
    private String ownerName;
    private Set<String> taskSet;
}
