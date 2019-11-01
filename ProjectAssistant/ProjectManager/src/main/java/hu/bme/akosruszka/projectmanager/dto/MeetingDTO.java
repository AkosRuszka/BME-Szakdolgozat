package hu.bme.akosruszka.projectmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDTO {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    @Valid
    private String projectName;
    @NotBlank
    private String location;
    @NotNull
    private LocalDateTime date;
    private String minuteName;
    @NotBlank
    @Valid
    private String chairPersonEmail;
    private Set<String> attendeeEmailSet;

}