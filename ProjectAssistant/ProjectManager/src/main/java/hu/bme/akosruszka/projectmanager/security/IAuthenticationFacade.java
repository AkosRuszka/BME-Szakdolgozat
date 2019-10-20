package hu.bme.akosruszka.projectmanager.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    String getUserName();
}
