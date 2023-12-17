package luckystore.datn.rest.user;

import jakarta.mail.MessagingException;
import luckystore.datn.service.user.HoaDonKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/send-email/hoa-don")
public class RestSendEmailHoaDonController {

    @Autowired
    HoaDonKhachHangService hoaDonKhachHangService;

    @GetMapping("/{id}")
    public ResponseEntity sendEmailHoaDon(@PathVariable("id") Long id) throws MessagingException {
        return new ResponseEntity(hoaDonKhachHangService.sendMailHoaDon(id), HttpStatus.OK);
    }

}
