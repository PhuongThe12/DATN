package luckystore.datn.infrastructure.security.session;

public interface SessionService {

    UserDetailToken getCustomer();

    UserDetailToken getStaff();
}
