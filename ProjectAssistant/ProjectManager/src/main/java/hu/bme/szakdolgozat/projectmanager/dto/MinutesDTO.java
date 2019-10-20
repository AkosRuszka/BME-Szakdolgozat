package hu.bme.szakdolgozat.projectmanager.dto;

import hu.bme.szakdolgozat.projectmanager.entity.helper.TaskDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinutesDTO {
    private String title;
    private String meetingName;
    private Set<String> absentEmailSet;
    private Set<TaskDescription> taskSet;
}
