package edu.upc.essi.dtim.odin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Properties {

    @Value("${info.component}")
    private String info;


}
