package luckystore.datn.rest;

import luckystore.datn.model.request.GiayExcelRequest;
import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.request.GiaySearch;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.util.JsonString;
import luckystore.datn.validation.groups.CreateGroup;
import luckystore.datn.validation.groups.UpdateGiaGroup;
import luckystore.datn.validation.groups.UpdateSoLuongGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/rest/giay")
public class RestGiayController {

    @Autowired
    private GiayService giayService;

    @Autowired
    private ImageHubService imageHubService;

    @PostMapping("/add-excel")
    public ResponseEntity<?> addExcel(@RequestBody List<GiayExcelRequest> lst) {
        giayService.addExcel(lst);
        return ResponseEntity.ok("{\"data\":\"Done\"}");
    }

    @PostMapping("/update-excel")
    public ResponseEntity<?> test(@RequestBody List<GiayExcelRequest> lst) {
        giayService.updateExcel(lst);
        return ResponseEntity.ok("{\"data\":\"Done\"}");
    }

    @PostMapping("/get-page")
    public ResponseEntity<?> getPage(
//            @RequestBody GiaySearch giaySearch
    ) {
        return ResponseEntity.ok(giayService.getPage());
    }

    @GetMapping("/getData")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok(imageHubService.getBase64FromFile("demo.txt"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(giayService.getResponseById(id));
    }

    @PostMapping("/get-all-active")
    public ResponseEntity<?> getAllActive(@RequestBody GiaySearch giaySearch) {
        return new ResponseEntity<>(giayService.getAllActive(giaySearch), HttpStatus.OK);
    }

    @PostMapping("/get-all-giay")
    public ResponseEntity<?> getAllGiay(@RequestBody GiaySearch giaySearch) {
        return new ResponseEntity<>(giayService.findAllForList(giaySearch), HttpStatus.OK);
    }

    @PostMapping("/get-simple-by-search")
    public ResponseEntity<?> findSimpleBySearch(@RequestBody GiaySearch giaySearch) {
        return new ResponseEntity<>(giayService.findSimpleBySearch(giaySearch), HttpStatus.OK);
    }

    @PostMapping("/find-all-by-search")
    public ResponseEntity<?> findAllBySearch(@RequestBody GiaySearch giaySearch) {
        return new ResponseEntity<>(giayService.findAllBySearch(giaySearch), HttpStatus.OK);
    }

    @GetMapping("/bien-the/{barcode}")
    public ResponseEntity<?> findBienTheByBarcode(@PathVariable("barcode") String barcode) {
        return new ResponseEntity<>(giayService.getBienTheByBarcode(barcode), HttpStatus.OK);
    }

    @PostMapping("/get-giay-contains")
    public ResponseEntity<?> getAllContains(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(giayService.getAllContains(ids), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> uploadData(@Validated({CreateGroup.class}) @RequestBody GiayRequest giayRequest, BindingResult result) {

        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return ResponseEntity.ok(giayService.addGiay(giayRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGiay(@PathVariable("id") Long id,
                                        @Validated({UpdateGiaGroup.class}) @RequestBody GiayRequest giayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return ResponseEntity.ok(giayService.updateGiay(id, giayRequest));
    }

    @PutMapping("/update-so-luong")
    public ResponseEntity<?> updateSoLuong(@Validated({UpdateSoLuongGroup.class}) @RequestBody GiayRequest giayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return ResponseEntity.ok(giayService.updateSoLuong(giayRequest));
    }

    @PutMapping("/update-gia")
    public ResponseEntity<?> updateGia(@Validated({UpdateGiaGroup.class}) @RequestBody GiayRequest giayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return ResponseEntity.ok(giayService.updateGia(giayRequest));
    }

    @GetMapping("/{id}/so-luong")
    public ResponseEntity<?> getSoLuong(@PathVariable("id") Long id) {
        return ResponseEntity.ok(giayService.getSoLuong(id));
    }

    private ResponseEntity<?> getErrorJson(BindingResult result) {
        if (result.hasErrors()) {
            List<String> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String errorMessage = JsonString.errorToJsonObject(fieldError.getField(), fieldError.getDefaultMessage());
                fieldErrors.add(errorMessage);
            }
            String errorJson = JsonString.stringToJson(String.join(",", fieldErrors));
            return new ResponseEntity<>(errorJson, HttpStatus.BAD_REQUEST);
        }
        return null;

    }


}
