package hu.bme.akos.ruszkabanyai.dto;

import hu.bme.akos.ruszkabanyai.entity.helper.TaskDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinutesDTO {

    private MeetingDTO meeting;
    private List<UserDTO> absentList;
    private List<TaskDescription> taskList;
}
