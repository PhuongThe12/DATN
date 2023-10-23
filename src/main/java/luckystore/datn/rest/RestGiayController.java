package luckystore.datn.rest;

import luckystore.datn.service.GiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestGiayController {

    @Autowired
    private GiayService giayService;

    @GetMapping("/admin/rest/giay/get-all-active")
    public ResponseEntity getAllActive() {
        return new ResponseEntity(giayService.getAllActive(), HttpStatus.OK);
    }

    @GetMapping("/admin/rest/giay/get-giay-contains")
    public ResponseEntity getAllContains(@RequestBody List<Long> ids) {
        return new ResponseEntity(giayService.getAllContains(ids), HttpStatus.OK);
    }
}
