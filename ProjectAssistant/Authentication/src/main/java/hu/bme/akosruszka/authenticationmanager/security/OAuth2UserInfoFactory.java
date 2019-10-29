package hu.bme.akosruszka.authenticationmanager.security;

import hu.bme.akosruszka.authenticationmanager.entity.AuthProvider;
import hu.bme.akosruszka.authenticationmanager.exception.OAuth2AuthenticationProcessingException;
import hu.bme.akosruszka.authenticationmanager.helper.FacebookOAuth2UserInfo;
import hu.bme.akosruszka.authenticationmanager.helper.GoogleOAuth2UserInfo;

import java.util.Map;

public abstract class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        }
        throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
}
