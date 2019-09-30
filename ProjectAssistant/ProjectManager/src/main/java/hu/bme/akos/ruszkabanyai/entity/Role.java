package hu.bme.akos.ruszkabanyai.entity;

import hu.bme.akos.ruszkabanyai.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    @NotBlank
    @Column(unique = true)
    private String name;
}
