package com.springboot.app.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager(); // para registrar un flashMap. Arreglo asociativo que contenga los mensajes flash success. administrador de map para los mensajes flash

        FlashMap flashMap = new FlashMap();

        flashMap.put("success", "Hello " + authentication.getName() + ",Has successfully logged in!");

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        if(authentication != null){
            logger.info("The user '" + authentication.getName() + "' has successfully logged in!");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
