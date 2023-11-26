package luckystore.datn.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import luckystore.datn.infrastructure.security.token.JwtAuthenticationFilter;
import luckystore.datn.infrastructure.security.token.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AccountDetailsService accountDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(

                        request -> request.requestMatchers("/api/authentication/**","/login", "/client/**", "/cart/**",
                                        "/admin/poin/**", "/cart-detail/**", "/admin/promotion", "/admin/voucher"
                                        , "/ws/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyRole("ADMIN", "EMLOYEE")
//                                .requestMatchers("/admin/bill-detail/**").hasAnyRole("EMLOYEE", "ADMIN")
//                                .requestMatchers("/admin/bill-history/**").hasAnyRole("EMLOYEE", "ADMIN")
//                                .requestMatchers("/admin/bill/**").hasAnyRole("EMLOYEE", "ADMIN")
//                                .requestMatchers("/admin/customer/**").hasAnyRole("EMLOYEE", "ADMIN")
//                                .requestMatchers("/admin/payment/**").hasAnyRole("EMLOYEE", "ADMIN")
//                                .requestMatchers("/admin/product/**").hasAnyRole("EMLOYEE", "ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/admin/**").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/admin/**").hasAnyRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers("/admin/**").hasAuthority("ROLE_USER")
                                .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(accountDetailsService.userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**",
                "/angular-1.8.2/**", "/toastr/**", "/bootstrap/**", "/pagination/**", "/icon/**");
    }
}
