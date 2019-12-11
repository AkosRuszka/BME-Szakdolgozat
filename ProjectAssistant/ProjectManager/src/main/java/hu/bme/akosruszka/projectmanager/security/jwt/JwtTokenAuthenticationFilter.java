package hu.bme.akosruszka.projectmanager.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtCookieStore jwtCookieStore;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (jwtCookieStore == null) {
            ServletContext servletContext = httpServletRequest.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            jwtCookieStore = webApplicationContext.getBean(JwtCookieStore.class);
        }
        try {
            jwtCookieStore.retrieveToken(httpServletRequest)
                    .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
