package luckystore.datn.infrastructure.security.config;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountDetailsService {

    UserDetailsService userDetailsService();
}
