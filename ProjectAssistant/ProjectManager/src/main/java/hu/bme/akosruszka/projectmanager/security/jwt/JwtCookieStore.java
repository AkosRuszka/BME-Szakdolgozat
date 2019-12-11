package hu.bme.akosruszka.projectmanager.security.jwt;

import hu.bme.akosruszka.projectmanager.constans.StringConstants;
import hu.bme.akosruszka.projectmanager.dao.UserRepository;
import hu.bme.akosruszka.projectmanager.entity.Role;
import hu.bme.akosruszka.projectmanager.entity.User;
import hu.bme.akosruszka.projectmanager.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtCookieStore {

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    public Optional<Authentication> retrieveToken(HttpServletRequest request) {
        String token = findCookie(request)
                .map(Cookie::getValue)
                .orElseGet(() -> request.getHeader("Authorization").split(" ")[1]);

        Claims claims = Jwts.parser().setSigningKey(env.getProperty("app.auth.tokenSecret")).parseClaimsJws(token).getBody();
        String email = claims.getSubject();

        if (email != null) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Nem található a JWT-ben kapott user email."));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(new UserPrincipal(user), null,
                    user.getRoleSet()
                            .stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

            return Optional.of(auth);
        }

        return Optional.empty();
    }

    private Optional<Cookie> findCookie(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(StringConstants.COOKIE_NAME)).findAny();
    }

}
