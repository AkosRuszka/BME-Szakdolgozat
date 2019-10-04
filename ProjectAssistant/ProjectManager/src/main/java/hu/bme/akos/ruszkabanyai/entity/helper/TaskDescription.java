package hu.bme.akos.ruszkabanyai.entity.helper;

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
    public Date estimatingDate;
}
