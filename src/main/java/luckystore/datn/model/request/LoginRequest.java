package luckystore.datn.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String tenDangNhap;

    @NotBlank
    private String matKhau;
}
