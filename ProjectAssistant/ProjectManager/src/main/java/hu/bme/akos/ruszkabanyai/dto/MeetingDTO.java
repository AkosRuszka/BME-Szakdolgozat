package hu.bme.akos.ruszkabanyai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDTO {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Valid
    private ProjectDTO project;
    @NotBlank
    private String location;
    @NotNull
    private Date date;
    private MinutesDTO minute;
    @NotNull
    @Valid
    private UserDTO chairPerson;
    private List<UserDTO> attendeeList;

}
