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

    @Autowired
	private UserDetailsServiceImpl userDetailService;
    
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(antMatcher("/**")).permitAll()
                .requestMatchers(antMatcher("/css/**")).permitAll()
                .requestMatchers(antMatcher("/**")).permitAll()
                .requestMatchers(antMatcher("/api/**")).permitAll()
                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(antMatcher("/registration/**")).permitAll()
                .requestMatchers(antMatcher("/saveuser/**")).permitAll()
                .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(antMatcher("/configuration/ui")).permitAll()
                .requestMatchers(antMatcher("/swagger-resources/**")).permitAll()
                .requestMatchers(antMatcher("/configuration/security")).permitAll()
                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(antMatcher("/categories/**")).permitAll()
                .anyRequest().authenticated()
        ).formLogin(formlogin -> formlogin
                .loginPage("/login")
                .defaultSuccessUrl("/", true).permitAll()
        ).logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
        ).csrf((csrf) -> csrf.ignoringRequestMatchers(antMatcher("/h2-console/**")));

        http.csrf((csrf) -> csrf.ignoringRequestMatchers(antMatcher("/api/**")));

        http.headers(headers -> headers.disable());
        return http.build();
    }
    
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
