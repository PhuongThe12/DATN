package luckystore.datn.rest;

import jakarta.mail.MessagingException;
import luckystore.datn.model.request.IdListRequest;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.GioHangRepository;
import luckystore.datn.service.impl.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/rest/admin/test")
public class restTest {

   @Autowired
    EmailSenderService emailSenderService;

   @Autowired
    BienTheGiayRepository bienTheGiayRepository;

   @Autowired
    GioHangRepository gioHangRepository;

   @GetMapping()
    public String sendMail() throws MessagingException {
        emailSenderService.triggerMail();
        return "";
    }

    @PostMapping("/bienThe")
    public ResponseEntity<?> getBienThe(@RequestBody IdListRequest list){
        List<Long> ids = list.getIds();
        return new ResponseEntity(bienTheGiayRepository.findAllByIdIn(ids), HttpStatus.OK);
    }

    @GetMapping("/gio-hang/{id}")
    public ResponseEntity<?> getGioHang(@PathVariable("id") Long id){
       return new ResponseEntity(gioHangRepository.findAll(),HttpStatus.OK);
    }


}
