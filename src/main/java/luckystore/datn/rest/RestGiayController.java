package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.impl.ImageHubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/rest/giay")
public class RestGiayController {

    @Autowired
    private GiayService giayService;

    @Autowired
    private ImageHubService imageHubService;

    @GetMapping("/get-all-active")
    public ResponseEntity<?> getAllActive() {
        return new ResponseEntity<>(giayService.getAllActive(), HttpStatus.OK);
    }

    @GetMapping("/get-all-giay")
    public ResponseEntity<?> getAllGiay() {
        return new ResponseEntity<>(giayService.getAllGiay(), HttpStatus.OK);
    }

    @PostMapping("/get-giay-contains")
    public ResponseEntity<?> getAllContains(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(giayService.getAllContains(ids), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> uploadData(@Valid @RequestBody GiayRequest giayRequest, BindingResult result) {

        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        return ResponseEntity.ok(giayService.addGiay(giayRequest));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getImage(@PathVariable("name") String name) {
        return ResponseEntity.ok(new ImageHubServiceImpl().getImage(name));
    }

    @GetMapping("/getData")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok(imageHubService.getBase64FromFile("demo.txt"));
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> add(@RequestPart("giayRequest") GiayRequest giayRequest) {
//        System.out.println("sended: " + giayRequest);
////        if(result.hasErrors()) {
////            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
////        }
//
//        return new ResponseEntity<>(giayRequest, HttpStatus.OK);
//    }
}
