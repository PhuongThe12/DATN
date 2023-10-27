package luckystore.datn.rest;

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

    @PostMapping
    public ResponseEntity addNhanVien(@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant ngayBatDat) {
        System.out.println(ngayBatDat);
//        Instant instant = Instant.parse(ngayBatDat);
//
//        // Chuyển đổi Instant thành ZonedDateTime với múi giờ UTC
//        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
//
//        // Chuyển đổi ZonedDateTime thành LocalDateTime nếu cần
//        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
//
//        System.out.println("ZonedDateTime (UTC): " + zonedDateTime);
//        System.out.println("LocalDateTime: " + localDateTime);
        return null;
    }
}
