package hu.bme.akos.ruszkabanyai.dto;

import hu.bme.akos.ruszkabanyai.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private Set<Role> roleSet;
    private Set<String> projectNameSet;
    private Set<String> meetingNameSet;
    private Set<String> taskNameSet;
}
