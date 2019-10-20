package hu.bme.szakdolgozat.projectmanager.entity.helper;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(of = "name")
@AllArgsConstructor
@NoArgsConstructor
public class TaskDescription implements Serializable {
    @NotBlank
    public String name;
    @NotBlank
    public String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date estimatingDate;
}
