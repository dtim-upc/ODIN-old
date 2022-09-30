package edu.upc.essi.dtim.odin.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    public UserDetailsServiceImp(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            /*Here we are using dummy data, you need to load user data from
            database or other third party application*/
//        User user = findUserbyUsername(username);
        User user = userRepository.findByUsername(username);

        UserBuilder builder = null;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
//            builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
//            password is already encoded
            builder.password(user.getPassword());
            builder.authorities(user.getRoles());
//            builder.roles(user.getRoles());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }


//    private User findUserbyUsername(String username) {
//        if(username.equalsIgnoreCase("admin")) {
//            return new User(username, "admin123", "ADMIN");
//        }
//        return null;
//    }
}
