package hu.bme.akosruszka.authenticationmanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Data
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }

    @Data
    public static class OAuth2 {
        private String authorizedRedirectUri;

        public String getAuthorizedRedirectUri() {
            return authorizedRedirectUri;
        }

        public OAuth2 authorizedRedirectUri(String authorizedRedirectUri) {
            this.authorizedRedirectUri = authorizedRedirectUri;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

}
