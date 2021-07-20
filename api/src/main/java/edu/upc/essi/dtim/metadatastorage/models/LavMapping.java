package edu.upc.essi.dtim.metadatastorage.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class LavMapping {

    @Id
    private String id;
    private String globalGraphId;
    private String wrapperId;

    public LavMapping(String globalGraphId, String wrapperId) {
        this.globalGraphId = globalGraphId;
        this.wrapperId = wrapperId;
    }

    @Override
    public String toString() {
        return String.format(
                "LavMapping[globalGraphId=%s, wrapperId='%s']",
                this.globalGraphId, this.wrapperId);
    }



}
