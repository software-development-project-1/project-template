package fi.haagahelia.quizzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fi.haagahelia.quizzer.service.UserDetailsServiceImpl;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
            .requestMatchers(
                antMatcher("/"),
                antMatcher("/api/**"),
                antMatcher("/registration/**"),
                antMatcher("/h2-console/**"),
                // Swagger documentation paths
                antMatcher("/v3/api-docs/**"),
                antMatcher("/configuration/ui"),
                antMatcher("/swagger-resources/**"),
                antMatcher("/configuration/security"),
                antMatcher("/swagger-ui/**"))
            .permitAll()
            .anyRequest()
            .authenticated());

        http.formLogin((form) -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll());
        http.logout((logout) -> logout
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout")
            .permitAll());
        http.cors(Customizer.withDefaults());
        http.csrf((csrf) -> csrf
        .ignoringRequestMatchers(antMatcher("/api/**")));

        http.headers(headers -> headers.disable());
        return http.build();
    }
}
