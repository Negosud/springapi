package fr.negosud.springapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.desktop.username}")
    private String desktopUsername;

    @Value("${security.desktop.password}")
    private String desktopPassword;

    @Value("${security.web.username}")
    private String webUsername;

    @Value("${security.web.password}")
    private String webPassword;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).httpBasic(Customizer.withDefaults());
        SecurityFilterChain securityFilterChain = http.build();
        return securityFilterChain;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userManager = new InMemoryUserDetailsManager();
        userManager.createUser(User.builder()
                .username(webUsername)
                .password(passwordEncoder().encode(webPassword))
                .roles("WEB")
                .build());
        userManager.createUser(User.builder()
                .username(desktopUsername)
                .password(passwordEncoder().encode(desktopPassword))
                .roles("DESKTOP")
                .build());

        return userManager;
    }
}
