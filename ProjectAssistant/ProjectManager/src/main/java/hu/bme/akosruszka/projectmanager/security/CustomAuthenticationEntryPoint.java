package hu.bme.akosruszka.projectmanager.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authEx) throws IOException, ServletException {
        response.addHeader("redirect_path", request.getRequestURI());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/authentication");
        dispatcher.forward(request, response);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("ProjectAssistant");
        super.afterPropertiesSet();
    }
}
