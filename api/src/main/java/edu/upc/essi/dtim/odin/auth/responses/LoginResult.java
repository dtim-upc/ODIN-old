package edu.upc.essi.dtim.odin.auth.responses;

import edu.upc.essi.dtim.odin.auth.user.User;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResult {

    @NonNull
    private String accessToken;
    private String username;
    private String firstName;
    private String lastName;

    public LoginResult(String jwt, User user ){

        accessToken = jwt;
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
    }
//    private String username;
    //missing user data!!!
}