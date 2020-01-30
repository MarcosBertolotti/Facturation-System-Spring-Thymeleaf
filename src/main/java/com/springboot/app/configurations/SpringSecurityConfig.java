package com.springboot.app.configurations;

import com.springboot.app.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // habilita el uso de la anotacion @Secured en los Controller
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter { // adaptador donde vamos guardar/registrar los users.

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {  // para registrar nuestros users.

        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // spring ejecuta un interceptor antes de cargar una pagina (aceptar peticion) y valida los permisos

        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/images/**", "/list", "/locale").permitAll() // asignamos nuestras rutas publicas para el acceso de cualquier rol. (ruta raiz, recursos staticos, acceso publico)
                //.antMatchers("/see/**").hasAnyRole("USER")
                //.antMatchers("/uploads/**").hasAnyRole("USER")
                //.antMatchers("/form/**").hasAnyRole("ADMIN")
                //.antMatchers("/delete/**").hasAnyRole("ADMIN")
                //.antMatchers("/bill/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403"); // pagina de error
    }
}

