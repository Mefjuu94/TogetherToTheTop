package TTT.security;

import TTT.users.CustomUser;
import jakarta.ws.rs.HttpMethod;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserSecurityService securityService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public SecurityConfig(CustomUserSecurityService securityService) {
        this.securityService = securityService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.
                authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user","/map","/sendData","/updateField","/trips/","/addMe","/announcement", HttpMethod.POST).fullyAuthenticated()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll().
                        requestMatchers("/","/login","/register","/error/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form.loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .permitAll());
//                .logout((logout) -> logout
//                .logoutUrl("/logout")  // URL do wylogowania /logout
//                .logoutSuccessUrl("/login?logout=true")  // Po udanym wylogowaniu przekieruj na stronę logowania
//                .invalidateHttpSession(true)  // Inwalidacja sesji
//                .deleteCookies("JSESSIONID")  // Usunięcie ciasteczek sesji
//                .permitAll());  // Pozwolenie wszystkim użytkownikom na dostęp do endpointu wylogowania


        return security.build();
    }


    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(securityService);
        dao.setPasswordEncoder(encoder);

        return dao;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider());

        return authenticationManagerBuilder.build();
    }
}
