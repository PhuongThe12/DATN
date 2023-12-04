package luckystore.datn.infrastructure.security.session;

import lombok.*;
import luckystore.datn.infrastructure.Role;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailToken {

    private Long id;

    private String tenDangNhap;

    private String hoTen;

    private String email;

    private String role;

}
