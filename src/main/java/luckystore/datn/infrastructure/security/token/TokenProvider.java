package luckystore.datn.infrastructure.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.infrastructure.security.session.UserDetailToken;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.NhanVienRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final KhachHangRepository khachHangRepository;

    private final NhanVienRepository nhanVienRepository;

    private final HttpServletRequest request;

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

    public UserDetailToken decodeTheToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long id = claims.get("id", Long.class);
        String tenDangNhap = claims.get("tenDangNhap", String.class);
        String role = claims.get("role", String.class);
        UserDetailToken userDetailToken;
        HttpSession session = request.getSession();
        if (role.equalsIgnoreCase("ROLE_USER")) {
            KhachHang getKhachHangByToken = khachHangRepository.getKhachHangByTaiKhoanId(id);
            userDetailToken = UserDetailToken.builder().id(getKhachHangByToken.getId()).tenDangNhap(tenDangNhap)
                    .hoTen(getKhachHangByToken.getHoTen()).email(getKhachHangByToken.getEmail()).role(role).build();
            session.setAttribute("customer", getKhachHangByToken);
        } else {
            NhanVien getNhanVienByToken = nhanVienRepository.findNhanVienByIdTaiKhoan(id);
            userDetailToken = UserDetailToken.builder().id(getNhanVienByToken.getId()).tenDangNhap(tenDangNhap)
                    .hoTen(getNhanVienByToken.getHoTen()).email(getNhanVienByToken.getEmail()).role(role).build();
            session.setAttribute("employee", getNhanVienByToken);
        }
        return userDetailToken;
    }
}
