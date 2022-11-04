package edu.upc.essi.dtim.odin.auth.user;

import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.shaded.org.codehaus.jackson.annotate.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    // this is the id
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String[] roles;

    // this iri represents the entity. It does not represent the named graph.

    public User(String username, String password, String... roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @JsonIgnore
    public String getIri(){
        return Namespaces.USER.val() + "/" + this.username;
    }

}
