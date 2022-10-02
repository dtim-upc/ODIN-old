package edu.upc.essi.dtim.odin.config.auth;

import edu.upc.essi.dtim.odin.auth.user.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHORITIES_CLAIM_NAME = "roles";
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/scss/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(configurer ->
                        configurer
                                .antMatchers(
                                        "/error",
                                        "/login",
                                        "/signup",
                                        "/triples",
                                        "/",
                                        "/assets/**",
                                        "/index.html",
                                        "/prueba",
                                        "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/scss/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                );
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        //  enable JWT authentication and set our custom JwtAuthenticationConverter
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authenticationConverter());
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
//        UserDetails user1 = User
//                .withUsername("user")
//                .authorities("USER")
//                .passwordEncoder(passwordEncoder::encode)
//                .password("1234")
//                .build();
//
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(user1);
//        return manager;

//        2nd

//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        UserDetails user1 = User
//                .withUsername("user1")
//                .authorities("ADMIN", "STAFF_MEMBER")
//                .passwordEncoder(passwordEncoder::encode)
//                .password("1234")
//                .build();
//        manager.createUser(user1);
//
//        UserDetails user2 = User
//                .withUsername("user2")
//                .authorities("STAFF_MEMBER")
//                .passwordEncoder(passwordEncoder::encode)
//                .password("1234")
//                .build();
//        manager.createUser(user2);
//
//        UserDetails user3 = User
//                .withUsername("user3")
//                .authorities("ASSISTANT_MANAGER", "STAFF_MEMBER")
//                .passwordEncoder(passwordEncoder::encode)
//                .password("1234")
//                .build();
//        manager.createUser(user3);
//
//        UserDetails user4 = User
//                .withUsername("user4")
//                .authorities("MANAGER", "STAFF_MEMBER")
//                .passwordEncoder(passwordEncoder::encode)
//                .password("1234")
//                .build();
//        manager.createUser(user4);
//
//        return manager;
        return new UserDetailsServiceImp();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        not sure if passwordEncoder() parameter is correct
        auth.userDetailsService(userDetailsService()).passwordEncoder(this.passwordEncoder);
    }

    protected JwtAuthenticationConverter authenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME);

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

}