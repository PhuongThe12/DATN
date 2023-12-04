package luckystore.datn.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckystore.datn.model.request.DayGiayRequest;
import luckystore.datn.model.request.FindPhieuGiamGiaRequest;
import luckystore.datn.model.request.PhieuGiamGiaRequest;
import luckystore.datn.service.PhieuGiamGiaService;
import luckystore.datn.util.JsonString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/rest/phieu-giam-gia")
@RequiredArgsConstructor
public class PhieuGiamGiaRestController {

    private final PhieuGiamGiaService phieuGiamGiaService;

//    @PostMapping
//    public ResponseEntity<?> addChatLieu(@Valid @RequestBody ChatLieuRequest chatLieuRequest, BindingResult result) {
//        ResponseEntity<?> errorJson = getErrorJson(result);
//        if (errorJson != null) return errorJson;
//
//        return new ResponseEntity<>(chatLieuService.addChatLieu(chatLieuRequest), HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateChatLieu(@PathVariable("id") Long id, @Valid @RequestBody ChatLieuRequest chatLieuRequest, BindingResult result) {
//        ResponseEntity<?> errorJson = getErrorJson(result);
//        if (errorJson != null) return errorJson;
//
//        return new ResponseEntity<>(chatLieuService.updateChatLieu(id, chatLieuRequest), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getChatLieu(@PathVariable("id") Long id) {
//        return new ResponseEntity<>(chatLieuService.findById(id), HttpStatus.OK);
//    }
//
    @GetMapping("")
    public ResponseEntity<?> getPagePhieuGiamGia(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "search", required = false) String searchText,
                                                 @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity<>(phieuGiamGiaService.getpage(page, searchText, status), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPhieuGiamGia(@PathVariable("id") Long id) {
        return new ResponseEntity<>(phieuGiamGiaService.getPhieuResponseById(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(phieuGiamGiaService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPhieuGiamGia(@Valid @RequestBody PhieuGiamGiaRequest request, BindingResult result) {
        ResponseEntity<?>errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(phieuGiamGiaService.addPhieuGiamGia(request), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> getListSearchPhieu(@RequestBody FindPhieuGiamGiaRequest request, BindingResult result) {

        return new ResponseEntity<>(phieuGiamGiaService.getListSearchPhieu(request), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDayGiay(@PathVariable("id") Long id,
                                           @Valid @RequestBody PhieuGiamGiaRequest request, BindingResult result) {
        ResponseEntity<?>errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(phieuGiamGiaService.addPhieuGiamGia(request), HttpStatus.OK);
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
