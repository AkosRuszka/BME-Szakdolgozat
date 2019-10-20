package hu.bme.szakdolgozat.projectmanager.entity;

import hu.bme.szakdolgozat.projectmanager.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "name")
@AllArgsConstructor
public class Role extends BaseEntity {
    @Id
    @NotBlank
    private String name;
}
