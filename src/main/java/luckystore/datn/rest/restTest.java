package luckystore.datn.rest;

import jakarta.mail.MessagingException;
import luckystore.datn.service.impl.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/admin/rest/test")
public class restTest {

   @Autowired
    EmailSenderService emailSenderService;

   @GetMapping()
    public String sendMail() throws MessagingException {
        emailSenderService.triggerMail();
        return "";
    }
}
