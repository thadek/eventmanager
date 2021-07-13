package com.m8.event.manager;


import com.m8.event.manager.rest.security.JwtAuthenticationFilter;
import com.m8.event.manager.rest.security.JwtAuthorizationFilter;
import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class MultipleSecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    //Seguridad de la API-REST
    @Configuration
    @EnableWebSecurity
    @Order(1)
    public class ApiSecurityAdapter extends WebSecurityConfigurerAdapter {


        private UserDetailsService userDetailsService;

        public ApiSecurityAdapter(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

      /*  @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(usuarioService)
                    .passwordEncoder(encoder);
        }*/
        /*

        private TokenProvider tokenProvider;

        public ApiSecurityAdapter(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
        }*/

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public JwtAuthorizationFilter jwtAuthorizationFilterBean() {
            return new JwtAuthorizationFilter();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Configuración de la clase que recupera los usuarios y algoritmo para procesar las passwords
            auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {


            http    .antMatcher("/api/**")
                  //SI QUIERO MANEJARME SIN LAS COOKIES DEL NAVEGADOR, AGREGAR ESTA LINEA  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                    .antMatchers("/api/iniciarsesion","/api/eventos/vertodos").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().disable()
                    .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()),
                            UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class);





          /*  http .csrf().disable()
                    .antMatcher("/api/*") //<= Security only available for /api/**
                    .authorizeRequests()
                    .antMatchers("/**")
                    .au


                    .permitAll();
                    .antMatchers("/api/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .apply(new JWTConfigurer(this.tokenProvider))
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
        }

    }



    //Seguridad del Sitio Web
    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(usuarioService)
                    .passwordEncoder(encoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .authorizeRequests()
                    .antMatchers("/adm/**").access("hasRole('ROLE_ADMIN')")
                    .antMatchers("/perfil").authenticated()
                    .antMatchers("/assets/**",  "/*","/eventos/**").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/logincheck")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                    .deleteCookies("JSESSIONID")
                    .and()
                    .exceptionHandling().accessDeniedPage("/error-403")
                    .and()

                    .csrf().disable();
        }


    }



}