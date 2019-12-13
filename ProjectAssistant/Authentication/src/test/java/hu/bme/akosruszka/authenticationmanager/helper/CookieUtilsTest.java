package hu.bme.akosruszka.authenticationmanager.helper;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CookieUtilsTest {

    @Test
    public void getCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String paramName = "cookie";

        Cookie saved = new Cookie(paramName, "FIX");
        when(request.getCookies()).thenReturn(new Cookie[]{saved});

        Optional<Cookie> result = CookieUtils.getCookie(request, paramName);

        assertTrue(result.isPresent());
        assertEquals(paramName, result.get().getName());
    }

    @Test
    public void addCookie() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        String name = "CookieName";
        String value = "CookieValue";

        ArgumentCaptor<Cookie> arg = ArgumentCaptor.forClass(Cookie.class);

        CookieUtils.addCookie(response, name, value, 0);
        verify(response).addCookie(arg.capture());

        assertNotNull(arg.getAllValues());
        assertEquals(1, arg.getAllValues().size());
        assertEquals(name, arg.getValue().getName());
    }

    @Test
    public void deleteCookieTest() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        String name = "CookieName";
        Cookie cookie = new Cookie("otherName", "Value");
        Cookie deletableCookie = new Cookie(name, "Value");

        when(request.getCookies()).thenReturn(new Cookie[]{cookie, deletableCookie});

        ArgumentCaptor<Cookie> arg = ArgumentCaptor.forClass(Cookie.class);

        CookieUtils.deleteCookie(request, response, name);
        verify(response).addCookie(arg.capture());

        assertNotNull(arg.getAllValues());
        assertEquals(1, arg.getAllValues().size());
        assertEquals(name, arg.getValue().getName());
    }
}
