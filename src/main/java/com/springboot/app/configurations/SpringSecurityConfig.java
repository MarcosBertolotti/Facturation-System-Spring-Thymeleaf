package com.springboot.app.configurations;

import com.springboot.app.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // habilita el uso de la anotacion @Secured en los Controller
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter { // adaptador donde vamos guardar/registrar los users.

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {  // para registrar nuestros users.

        PasswordEncoder encoder = this.passwordEncoder;
        // UserBuilder users = User.buithilder().passwordEncoder(password -> encoder.encode(password))
        UserBuilder users = User.builder().passwordEncoder(encoder::encode); // llamada estatica, :: obtiene el argumento (password) de esta funcion lambda y se la pasas al metodo encode para encriptarla

        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))  // creamos el user con su username, password, y roles. UserBuilder automaticamente encriptara la password pasada.
                .withUser(users.username("marcos").password("12345").roles("USER"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // spring ejecuta un interceptor antes de cargar una pagina (aceptar peticion) y valida los permisos
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/images/**", "/list").permitAll() // asignamos nuestras rutas publicas para el acceso de cualquier rol. (ruta raiz, recursos staticos, acceso publico)
                //.antMatchers("/see/**").hasAnyRole("USER") // solo los usuarios logeados con "User" Rol puede ir la ruta
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

