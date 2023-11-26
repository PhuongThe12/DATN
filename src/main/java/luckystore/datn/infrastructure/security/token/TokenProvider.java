package luckystore.datn.infrastructure.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.infrastructure.security.session.UserDetailToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenProvider {

    public static final String SECRET = "7A25432A462D4A614E645266556A586E3272357538782F413F4428472B4B6250";

    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(TaiKhoan taiKhoan) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", taiKhoan.getId());
        claims.put("tenDangNhap", taiKhoan.getTenDangNhap());
        claims.put("role", taiKhoan.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(taiKhoan.getTenDangNhap())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 60 * 1000))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String genetateRefreshToken(Map<String, Object> extractClaims, TaiKhoan taiKhoan) {
        extractClaims.put("id", taiKhoan.getId());
        extractClaims.put("tenDangNhap", taiKhoan.getTenDangNhap());
        extractClaims.put("role", taiKhoan.getRole());
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(taiKhoan.getTenDangNhap())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 60 * 1000))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSiginKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // todo kiểm tra hết hạn
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public void decodeTheToken(String token, HttpServletRequest request) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Integer id = claims.get("id", Integer.class);
        String tenDangNhap = claims.get("tenDangNhap", String.class);
        String role = claims.get("role", String.class);

        HttpSession session = request.getSession();
        var user = UserDetailToken.builder().id(id).role(role).tenDangNhap(tenDangNhap).build();
        if (user.getRole().equals("ROLE_USER")) {
            session.setAttribute("customer", user);
        } else {
            session.setAttribute("staff", user);
        }
    }
}
