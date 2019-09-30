package hu.bme.akos.ruszkabanyai.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @Version
    @JsonIgnore
    private Long version;
}
