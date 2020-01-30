package com.springboot.app.auth.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager(); // para registrar un flashMap. Arreglo asociativo que contenga los mensajes flash success. administrador de map para los mensajes flash

        FlashMap flashMap = new FlashMap();

        Locale locale = localeResolver.resolveLocale(request);
        String message = String.format(messageSource.getMessage("text.login.success", null, locale), authentication.getName());

        flashMap.put("success", message);

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        if(authentication != null) {
            logger.info(message);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
