package hu.bme.akosruszka.authenticationmanager.service;

import hu.bme.akosruszka.authenticationmanager.dao.UserRepository;
import hu.bme.akosruszka.authenticationmanager.entity.User;
import hu.bme.akosruszka.authenticationmanager.exception.OAuth2AuthenticationProcessingException;
import hu.bme.akosruszka.authenticationmanager.security.OAuth2UserInfo;
import hu.bme.akosruszka.authenticationmanager.security.OAuth2UserInfoFactory;
import hu.bme.akosruszka.authenticationmanager.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user = userOptional.orElseGet(() -> registerNewUser(oAuth2UserInfo));

        return UserPrincipal.create(user);
    }

    private User registerNewUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());

        String[] splitted = user.getName().split(" ");
        String firstName = splitted[0];
        String lastName = splitted[1];
        user.setInnerEmail(checkUser(firstName + "." + lastName, 0));
        return userRepository.save(user);
    }

    private String checkUser(String email, Integer counter) {
        if (userRepository.findByEmail(email + (counter == 0 ? "" : counter.toString())).isPresent()) {
            checkUser(email, ++counter);
        }
        return email + counter.toString();
    }
}
