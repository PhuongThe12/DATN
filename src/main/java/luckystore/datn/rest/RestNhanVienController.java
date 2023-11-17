package luckystore.datn.rest;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.constraints.SystemHistory;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.NhanVienRequest;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.NhanVienService;
import luckystore.datn.service.impl.EmailSenderService;
import luckystore.datn.service.impl.SystemHistoryLogger;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin/rest/nhan-vien")
public class RestNhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @GetMapping("/hdct/{id}")
    public ResponseEntity getOne(@PathVariable Long id){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id).get();
        return new ResponseEntity(hoaDonChiTiet,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addNhanVien(@Valid @RequestBody NhanVienRequest nhanVienRequest, BindingResult result) {
        System.out.println(nhanVienRequest.toString());
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(nhanVienService.addNhanVien(nhanVienRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateNhanVien(@PathVariable("id") Long id,
                                         @Valid @RequestBody NhanVienRequest nhanVienRequest,
                                         BindingResult result) {
        System.out.println(nhanVienRequest);
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        if ((nhanVienRequest.getId() == SystemHistory.nhanVien.getId()) && (nhanVienRequest.getUpdateAccount() == null)) {
            return new ResponseEntity("Tài khoản đang được sử dụng, Vui lòng sử dụng 1 tài khoản khác để chỉnh sửa", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(nhanVienService.updateNhanVien(id, nhanVienRequest), HttpStatus.OK);
    }

    @GetMapping("/find-tai-khoan/{id}")
    public ResponseEntity findNhanVienByIdTaiKhoan(@PathVariable("id") Long id) {
        return new ResponseEntity(nhanVienService.findNhanVienByIdTaiKhoan(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getNhanVienPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "search", required = false) String searchText,
                                          @RequestParam(value = "status", required = false) Integer status,
                                          @RequestParam(value = "chucVu", required = false) Integer chucVu) throws InterruptedException {
//        Thread.sleep(2000); // Fake Lag - Tạm dừng 2s trước khi gọi đến DB
        return new ResponseEntity(nhanVienService.getPage(page, searchText, status, chucVu), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getNhanVien(@PathVariable("id") Long id) {
        return new ResponseEntity(nhanVienService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(nhanVienService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/check-logged")
    public ResponseEntity checkLoggedNhanVien() {
        if (SystemHistory.nhanVien == null) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        } else {
            return new ResponseEntity(nhanVienService.findById(SystemHistory.nhanVien.getId()), HttpStatus.OK);
        }
    }


    @GetMapping("/logger")
    public ResponseEntity getLogger() {
        return new ResponseEntity(nhanVienService.getLogger(), HttpStatus.OK);
    }

    @GetMapping("/send-report-daily")
    public ResponseEntity sendReportDaily() throws MessagingException {
        SystemHistoryLogger.sendDaiLyReport();
        emailSenderService.triggerMail();
        return new ResponseEntity(HttpStatus.OK);
    }

    private ResponseEntity getErrorJson(BindingResult result) {
        if (result.hasErrors()) {
            List<String> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String errorMessage = JsonString.errorToJsonObject(fieldError.getField(), fieldError.getDefaultMessage());
                fieldErrors.add(errorMessage);
            }
            String errorJson = JsonString.stringToJson(String.join(",", fieldErrors));
            return new ResponseEntity(errorJson, HttpStatus.BAD_REQUEST);
        }
        return null;
    }


}
