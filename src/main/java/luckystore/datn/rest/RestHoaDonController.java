package luckystore.datn.rest;

import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/rest/hoa-don")
public class RestHoaDonController {

    @Autowired
    private final HoaDonService hoaDonService;

    public RestHoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    @GetMapping
    public ResponseEntity getHashTagPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "search", required = false) String searchText,
                                         @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(hoaDonService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @PostMapping("/yeu-cau")
    public ResponseEntity getPageHoaDonYeuCauPage(@RequestBody HoaDonSearch hoaDonSearch) {
        System.out.println(hoaDonSearch);
        return new ResponseEntity<>(hoaDonService.getPageHoaDonYeuCau(hoaDonSearch), HttpStatus.OK);
    }

    @GetMapping("/yeu-cau/{id}")
    public ResponseEntity getHoaDonYeuCauPage(@PathVariable("id") Long id) {
        return new ResponseEntity(hoaDonService.getHoaDonYeuCau(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(hoaDonService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getHoaDon(@PathVariable("id") Long id) {
        return new ResponseEntity(hoaDonService.findById(id), HttpStatus.OK);
    }


}
