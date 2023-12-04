package luckystore.datn.infrastructure.security.session;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailToken {

    private Long id;

    private String tenDangNhap;

    private String role;

}
