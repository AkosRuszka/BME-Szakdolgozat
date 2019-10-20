package hu.bme.akosruszka.authenticationmanager.helper;

import hu.bme.akosruszka.authenticationmanager.entity.AuthProvider;
import hu.bme.akosruszka.authenticationmanager.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public abstract class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
}
