/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.config;

import org.bcms.ecsrmsrp.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	UserDetailsService userDetailService() {
		return new UserService();
	}
	
	// Password Encoding 
    @Bean
    PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
    
 // Configuring HttpSecurity 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/profile/register", "/profile/save","/dashboard", "/login","/assets/**", "/fa/**").permitAll())
                .authorizeHttpRequests(requests -> requests
                		.requestMatchers("/**").authenticated())
                .authorizeHttpRequests(requests -> requests
                		.requestMatchers("/api-docs/**", "/swagger-ui/**", 
                				"/swagger-ui.html", "/actuator/**",
                				"/api-documentation").permitAll())
                .sessionManagement(management -> management
                        .maximumSessions(1))
                .authenticationProvider(authenticationProvider())
                //.addFilterBefore(authFilter, 
                //		UsernamePasswordAuthenticationFilter.class)
                .formLogin((form) -> form
        				.loginPage("/login")
        				.loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard")
        				.permitAll()
        			)
        			.logout(
        					(logout) -> logout
        					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        					.permitAll()
        				)
                .build(); 
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() { 
	        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
	        authenticationProvider.setUserDetailsService(userDetailService()); 
	        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
	        return authenticationProvider; 
	    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
	        return config.getAuthenticationManager(); 
    }

}
