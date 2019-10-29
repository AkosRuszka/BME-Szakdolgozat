package hu.bme.szakdolgozat.projectmanager.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

@Getter
@Setter
public abstract class BaseEntity {
    @Version
    @JsonIgnore
    private Long version;
}
