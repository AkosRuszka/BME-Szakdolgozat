package hu.bme.akos.ruszkabanyai.security;

import hu.bme.akos.ruszkabanyai.helper.StringConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtCookieStore {

    public Optional<Authentication> retrieveToken(HttpServletRequest request) {
        Optional<Cookie> cookie = findCookie(request);
        if (cookie.isPresent()) {
            return Optional.empty();
        }
        String token = cookie.get().getValue();

        Claims claims = Jwts.parser().setSigningKey(StringConstants.SECRET).parseClaimsJws(token).getBody();
        String username = claims.getSubject();

        if (username != null) {
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.get("authorities");

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

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
