package hu.bme.szakdolgozat.projectmanager.security;

import hu.bme.szakdolgozat.projectmanager.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) {
        return userRepository.findByEmail(s).map(UserPrincipal::new).orElse(null);
    }
}
