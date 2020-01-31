package com.springboot.app.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // debe llamarse asi. Metodo Para registrar un controlador de vista. que serian controladores parametrizables o estaticos que cargan la vista sin logica de controlador
    public void addViewControllers(ViewControllerRegistry registry){

        registry.addViewController("/error_403").setViewName("error_403"); // ruta url, getMapping
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public LocaleResolver localeResolver() { // se encarga de guardar el objeto locale con nuestra internacionalizacion, con el codigo del locale y pais, y lo guardamos en un resolver (session locale resolver) (guarda en la sesion http)

        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("es", "ES"));

        return localeResolver;
    }

    // interceptor para cambiar el locale cada vez que se envie el parametro del lenguaje con el nuevo idioma, para cambiar el texto de la pagina.
    public LocaleChangeInterceptor localeChangeInterceptor() {

        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang"); // cada vez que se pase por url por metodo get, el parametro lang (languaje) se va a ejecutar el interceptory realiza el cambio

        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // registramos el interceptor

        registry.addInterceptor(localeChangeInterceptor());
    }


    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){ // para serializar/convertir nuestras clases/objeto a xml
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
         marshaller.setClassesToBeBound(new Class[] {com.springboot.app.view.xml.ClientList.class});
         return marshaller;
    }

}
