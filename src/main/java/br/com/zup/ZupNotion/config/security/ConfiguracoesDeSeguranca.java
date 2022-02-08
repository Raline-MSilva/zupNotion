package br.com.zup.ZupNotion.config.security;

import br.com.zup.ZupNotion.config.security.JWT.FiltroDeAutenticacaoJWT;
import br.com.zup.ZupNotion.config.security.JWT.FiltroDeAutorizacaoJWT;
import br.com.zup.ZupNotion.config.security.JWT.JWTComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ConfiguracoesDeSeguranca extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTComponent jwtComponent;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final String[] ENDPOINT = {
            "/tarefas/**",
            "/usuario/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(configurarCORS());


        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/tarefas/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/usuario/**").permitAll()
                .antMatchers(HttpMethod.POST, "/usuario/cadastraradmin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/usuario/esqueciSenha").permitAll()
                .antMatchers(HttpMethod.GET, "/tarefas/**").permitAll()
                .antMatchers(HttpMethod.DELETE, ENDPOINT).hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/tarefas/alterarStatus/**").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/tarefas/**").hasRole("ADMIN")
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources",
                        "/swagger-resources/configuration/security", "/swagger-ui/**", "/webjars/**").permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new FiltroDeAutenticacaoJWT(jwtComponent, authenticationManager()));
        http.addFilter(new FiltroDeAutorizacaoJWT(authenticationManager(), jwtComponent, userDetailsService));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /*@Bean
    CorsConfigurationSource configurarCORS() {
        UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        cors.registerCorsConfiguration("/**", config);
        return cors;
    }*/

    @Bean
    CorsConfigurationSource configurarCORS() {
        UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource();
        cors.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return cors;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

}
