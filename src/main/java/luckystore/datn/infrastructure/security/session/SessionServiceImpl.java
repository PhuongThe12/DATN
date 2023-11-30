package luckystore.datn.infrastructure.security.session;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService{

    private final HttpSession httpSession;
    @Override
    public UserDetailToken getCustomer() {
        return (UserDetailToken) httpSession.getAttribute("customer");
    }

    @Override
    public UserDetailToken getStaff() {
        return (UserDetailToken) httpSession.getAttribute("staff");
    }
}
