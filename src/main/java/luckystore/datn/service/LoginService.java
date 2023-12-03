package luckystore.datn.service;

import jakarta.validation.Valid;
import luckystore.datn.model.request.LoginRequest;
import luckystore.datn.model.response.JwtResponse;

public interface LoginService {

    String loginBasic(@Valid LoginRequest request);

    String logout(String token);
}
