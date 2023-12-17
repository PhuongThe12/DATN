package luckystore.datn.service.user.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.infrastructure.constants.Role;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KhachHangRepository khachHangRepository;

    public String confirm(Long id, String token) {

        Optional<TaiKhoan> taiKhoanOptional = taiKhoanRepository.findById(id);
        if (taiKhoanOptional.isEmpty()) {
            return "Không tìm thấy tài khoản";
        }

        TaiKhoan taiKhoan = taiKhoanOptional.get();
        if (taiKhoan.getRole() != Role.ROLE_USER) {
            return "Không tìm thấy tài khoản";
        }

        if (taiKhoan.getTrangThai() == 1) {
            return "Tài khoản đã được xác nhận";
        }

        if (!taiKhoan.getPasswordToken().equals(token)) {
            return "Thông tin không hợp lệ";
        }

        taiKhoan.setTrangThai(1);
        taiKhoan.setPasswordToken(null);
        taiKhoanRepository.save(taiKhoan);

        return "Xác nhận tài khoản thành công";

    }

    @SneakyThrows
    public void forgotPassword(String email) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElseThrow(()
                -> new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản"))));

        if (taiKhoan.getRole() != Role.ROLE_USER) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản")));
        }

        if (taiKhoan.getTrangThai() == 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Tài khoản chưa được xác nhận")));
        }

        String token = RandomString.make(30);
        taiKhoan.setPasswordToken(token);

        taiKhoan = taiKhoanRepository.save(taiKhoan);
        sendEmail(taiKhoan.getId(), taiKhoan.getTenDangNhap(), taiKhoan.getPasswordToken(), 2);
    }

    private String getContent(Long id, String token) {
        String link = "http://localhost:8080/account/reset-password" + "/" + id + "?token=" + token;

        return "<p>Xin chào,</p>"
                + "<p>Bạn đã gửi yêu cầu đặt lại mật khẩu.</p>"
                + "<p>Ấn vào đường dẫn sau để reset mật khẩu của bạn:</p>"
                + "<p><a href=\"" + link + "\">Đặt lại mật khẩu</a></p>"
                + "<br>"
                + "<p>Bỏ qua nếu không phải là bạn , "
                + "hoặc đây không phải yêu cầu đặt lại mật khẩu của bạn.</p>";
    }

    private String getConfirmContent(Long id, String token) {
        String link = "http://localhost:8080/account/confirm/" + id + "/" + token;

        return "<p>Xin chào,</p>"
                + "<p>Bạn đã tạo tài khoản thành công.</p>"
                + "<p>Ấn vào đường dẫn sau để xác thực tài khoản của bạn:</p>"
                + "<p><a href=\"" + link + "\">Xác thực tài khoản</a></p>"
                + "<br>"
                + "<p>Bỏ qua nếu không phải là bạn , "
                + "hoặc đây không phải yêu cầu tạo tài khoản của bạn.</p>";
    }

    public void sendEmail(Long id, String recipientEmail, String token, Integer trangThai)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("luckystore@shop.com", "LuckyStore");
        helper.setTo(recipientEmail);
        String subject;
        String content;
        subject = "Đặt lại mật khẩu";
        if (trangThai == null) {
            content = "<strong>Mật khẩu mới của bạn là: " + token + "</strong>";
        } else if (trangThai == -1) {
            subject = "Xác nhận tài khoản";
            content = getConfirmContent(id, token);
        } else {
            content = getContent(id, token);
        }

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

    }

    @SneakyThrows
    public String resetPassword(Long id, String token) {

        Optional<TaiKhoan> taiKhoanOptional = taiKhoanRepository.findById(id);
        if (taiKhoanOptional.isEmpty()) {
            return "Không tìm thấy tài khoản";
        }

        TaiKhoan taiKhoan = taiKhoanOptional.get();
        if (taiKhoan.getRole() != Role.ROLE_USER) {
            return "Không tìm thấy tài khoản";
        }

        if (taiKhoan.getTrangThai() == 0) {
            return "Tài khoản chưa được xác nhận";
        }

        if (!taiKhoan.getPasswordToken().equals(token)) {
            return "Thông tin không hợp lệ";
        }

        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomString.append((char) (random.nextInt(10) + '0'));
        }

        taiKhoan.setMatKhau(passwordEncoder.encode(randomString.toString()));
        sendEmail(null, taiKhoan.getTenDangNhap(), randomString.toString(), null);
        taiKhoanRepository.save(taiKhoan);

        return "Reset mật khẩu thành công. Vui lòng kiểm tra email để nhận mật khẩu mới mới";


    }

    @SneakyThrows
    public void confirm(String email) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElseThrow(()
                -> new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản"))));

        if (taiKhoan.getRole() != Role.ROLE_USER) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản")));
        }

        if (taiKhoan.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Tài khoản đã được xử lý")));
        }

        String token = RandomString.make(30);
        taiKhoan.setPasswordToken(token);
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        sendEmail(taiKhoan.getId(), taiKhoan.getTenDangNhap(), taiKhoan.getPasswordToken(), -1);
        System.out.println("Send: " + taiKhoan.getId() + ", " + email + ", " + taiKhoan.getPasswordToken());
    }

    public String find(String email, String sdt) {
        KhachHang khachHang = khachHangRepository.findByEmailAndSdt(email, sdt).orElseThrow(()
                -> new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản"))));

        TaiKhoan taiKhoan = khachHang.getTaiKhoan();
        if(taiKhoan == null) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản")));
        }
        if (taiKhoan.getRole() != Role.ROLE_USER) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy tài khoản")));
        }

        if (taiKhoan.getTrangThai() == 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Tài khoản chưa được xác nhận")));
        }

        return khachHang.getEmail();
    }

    public static class RandomString {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        public static String make(int length) {
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder(length);

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }

            return sb.toString();
        }
    }

}
