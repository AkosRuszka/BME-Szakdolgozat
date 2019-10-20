package hu.bme.szakdolgozat.projectmanager.entity.helper;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Message implements Serializable {

    private String to;
    private String subject;
    private String content;

}
