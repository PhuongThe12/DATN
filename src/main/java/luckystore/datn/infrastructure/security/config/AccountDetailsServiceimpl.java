package luckystore.datn.infrastructure.security.config;

import luckystore.datn.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceimpl implements AccountDetailsService{

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return taiKhoanRepository.findByTenDangNhap(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
            }
        };
    }
}
