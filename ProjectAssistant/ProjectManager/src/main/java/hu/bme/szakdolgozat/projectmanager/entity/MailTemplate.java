package hu.bme.szakdolgozat.projectmanager.entity;

import hu.bme.szakdolgozat.projectmanager.entity.base.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of="name")
public class MailTemplate extends BaseEntity {
    @Id
    @NotBlank
    private String name;

    @NotBlank
    private String message;

    @NotBlank
    private String subject;
}
