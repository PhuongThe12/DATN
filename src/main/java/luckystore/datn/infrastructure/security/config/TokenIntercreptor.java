package luckystore.datn.infrastructure.security.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import luckystore.datn.infrastructure.security.token.TokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenIntercreptor implements HandlerInterceptor {

    private final TokenProvider provider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = extractTokenFromCookies(request);
        String requestedPath = request.getRequestURI();
        if (token == null || token.isEmpty()) {
            response.sendRedirect("/login");
            return false;
        }

        String requestUri = request.getRequestURI();
        String userRoles = provider.decodeTheToken(token).getRole();
        Map<String, Integer> permissionStaff = new HashMap<>();
        permissionStaff.put("/admin/ban-hang", 1);

        Map<String, Integer> permissionAdmin = new HashMap<>();
        permissionAdmin.put("/admin", 1);



        if (userRoles.contains("ROLE_STAFF") && permissionStaff.containsKey(requestUri)) {
            return true;
        }
//        else if (userRoles.contains("ROLE_ADMIN") && permissionAdmin.containsKey(requestUri)) {
//            return true;
//        }
        else if (userRoles.contains("ROLE_USER") && requestUri.startsWith("/user")) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.sendRedirect("/access-denied");
        return false;

    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
