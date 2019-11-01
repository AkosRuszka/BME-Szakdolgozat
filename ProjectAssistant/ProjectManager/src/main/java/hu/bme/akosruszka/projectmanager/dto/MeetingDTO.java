package hu.bme.akosruszka.projectmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    private String minuteName;
    @NotBlank
    @Valid
    private String chairPersonEmail;
    private Set<String> attendeeEmailSet;

}
