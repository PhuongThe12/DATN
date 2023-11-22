package luckystore.datn.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.entity.UserToken;
import luckystore.datn.infrastructure.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Configuration
public class SecurityTokenProvider {

    private final String signInKey = "7A25432A462D4A614E645266556A586E3272357538782F413F4428472B4B6250";


    //Tạo ra SecretKey từ chuỗi signInKey đã được mã hóa Base64
    private SecretKey getSignInKey() {
        byte[] key = Base64.getDecoder().decode(signInKey);
        return Keys.hmacShaKeyFor(key);
    }

    //Tạo chuỗi JWT
    public String generateToken(TaiKhoan taiKhoanFind) {
        return Jwts
                .builder()
                .setSubject(taiKhoanFind.getTenDangNhap())
                .claim("role", taiKhoanFind.getRole())
                .claim("id", taiKhoanFind.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 2 * 60 * 60))
                .signWith(getSignInKey())
                .compact();
    }

    //Lấy thông tin từ token
    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String tenDangnhap = claims.getSubject();
        String role = claims.get("role", String.class);
        Long idTaiKhoan = claims.get("id", Long.class);


        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        TaiKhoan principal = new TaiKhoan();
        principal.setId(idTaiKhoan);
        principal.setTenDangNhap(tenDangnhap);
        principal.setRole(Role.valueOf(authority.getAuthority()));
        return new UsernamePasswordAuthenticationToken(principal, token, Collections.singletonList(authority));
    }

    private <T> T extractClaim(String token, Function<Claims, T> getClaimValue) {
        final Claims claims = getClaims(token);
        return getClaimValue.apply(claims);
    }

    public boolean validateToken(String token) {
        try {
            Date expirationDate = extractClaim(token, Claims::getExpiration);
            if (expirationDate.before(new Date())) {
                return false;
            }

//            UserToken userToken = userTokenService.findUserTokenByToken(token);
//            if (userToken == null) {
//                return false;
//            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
