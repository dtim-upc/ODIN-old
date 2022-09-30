package edu.upc.essi.dtim.odin.auth.user;

import edu.upc.essi.dtim.odin.utils.jena.GraphOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean saveUser(User _user) {
        User user = new User();
        user.setUsername(_user.getUsername());
        user.setFirstName(_user.getFirstName());
        user.setLastName(_user.getLastName());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(_user.getPassword()));

       String[] roles = {"USER"};
        user.setRoles(roles);

//        https://www.javaguides.net/2018/10/user-registration-module-using-springboot-springmvc-springsecurity-hibernate5-thymeleaf-mysql.html
//        Role role = roleRepository.findByName("ROLE_ADMIN");
//        if(role == null){
//            role = checkRoleExist();
//        }
//        user.setRoles(Arrays.asList(role));
        return userRepository.save(user);
    }


    public UserDetails loadUserByUsername(User user) throws UsernameNotFoundException {
            /*Here we are using dummy data, you need to load user data from
            database or other third party application*/
//        User user = findUserbyUsername(username);
//        User user = userRepository.findByUsername(username);

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
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


}
