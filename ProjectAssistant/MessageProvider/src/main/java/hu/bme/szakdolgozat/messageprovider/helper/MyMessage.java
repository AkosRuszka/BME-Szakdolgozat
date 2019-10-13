package hu.bme.szakdolgozat.messageprovider.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMessage implements Serializable {

    private String to;
    private String subject;
    private String content;

}
