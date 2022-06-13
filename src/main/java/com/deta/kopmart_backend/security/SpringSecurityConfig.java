package com.deta.kopmart_backend.security;

import com.deta.kopmart_backend.security.JWT.JwtEntryPoint;
import com.deta.kopmart_backend.security.JWT.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * @author deta
 * @description Class for Spring Security Configuration
 */
@Configuration
@EnableWebSecurity
@DependsOn("passwordEncoder")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    private JwtEntryPoint accessDeniedHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;


    /**
     * @param auth AuthenticationManagerBuilder
     * @throws Exception Exception
     * @description Method for configure AuthenticationManager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * @return AuthenticationManager
     * @throws Exception Exception
     * @description Method for configure AuthenticationManager
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * @param http HttpSecurity
     * @throws Exception Exception
     * @description Method for configure access to resources by role
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/profile/**").authenticated()
                .antMatchers("/cart/**").access("hasAnyRole('CUSTOMER')")
                .antMatchers("/order/finish/**").access("hasAnyRole('EMPLOYEE', 'MANAGER')")
                .antMatchers("/order/**").authenticated()
                .antMatchers("/profiles/**").authenticated()
                .antMatchers("/seller/product/new").access("hasAnyRole('MANAGER')")
                .antMatchers("/seller/**/delete").access("hasAnyRole( 'MANAGER')")
                .antMatchers("/seller/**").access("hasAnyRole('EMPLOYEE', 'MANAGER')")
                .anyRequest().permitAll()

                .and()
                .exceptionHandling().authenticationEntryPoint(accessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
