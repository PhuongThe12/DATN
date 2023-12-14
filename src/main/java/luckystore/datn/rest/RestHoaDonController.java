package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.DatHangTaiQuayRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.request.HoaDonSearchP;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.request.HuyDonRequest;
import luckystore.datn.model.request.TraMotPhanRequest;
import luckystore.datn.service.HoaDonChiTietService;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/rest/admin/hoa-don")
public class RestHoaDonController {

    private final HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
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
        return new ResponseEntity<>(hoaDonService.getPageHoaDonYeuCau(hoaDonSearch), HttpStatus.OK);
    }

    @GetMapping("/yeu-cau/{id}")
    public ResponseEntity getHoaDonYeuCauPage(@PathVariable("id") Long id) {
        return new ResponseEntity(hoaDonService.getHoaDonYeuCau(id), HttpStatus.OK);
    }

    @PostMapping("/update-list-hdct")
    public ResponseEntity updateListHoaDon(@RequestBody List<HoaDonRequest> hoaDonRequestList) {
        hoaDonService.updateListHoaDon(hoaDonRequestList);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hoaDonService.findById(id));
    }

    @GetMapping("/get-print/{id}")
    public ResponseEntity<?> getPrint(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hoaDonService.getPrint(id));
    }

    @GetMapping("/chua-thanh-toan-ban-hang")
    public ResponseEntity<?> getAllChuaThanhToan() {
        return ResponseEntity.ok(hoaDonService.getAllChuaThanhToanBanHang());
    }

    @GetMapping("/get-full-response/{id}")
    public ResponseEntity<?> getFullResponse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hoaDonService.getAllById(id));
    }

    @PostMapping("/new-hoa-don")
    public ResponseEntity<?> createNewHoadon() {
        return ResponseEntity.ok(hoaDonService.createNewHoaDon());
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody AddOrderProcuctRequest addOrderProcuctRequest) {
        return ResponseEntity.ok(hoaDonService.addProduct(addOrderProcuctRequest));
    }

    @PostMapping("/add-new-hdct")
    public ResponseEntity<?> addNewHdct(@Valid @RequestBody AddOrderProcuctRequest addOrderProcuctRequest) {
        return ResponseEntity.ok(hoaDonService.addNewHDCT(addOrderProcuctRequest));
    }

    @PostMapping("/add-khach-hang")
    public ResponseEntity<?> addKhachHang(@Valid @RequestBody AddOrderProcuctRequest addOrderProcuctRequest) {
        return ResponseEntity.ok(hoaDonService.addKhachHang(addOrderProcuctRequest));
    }

    @PostMapping("/thanh-toan-tai-quay")
    public ResponseEntity<?> thanhToanHoaDonTaiQuay(@Valid @RequestBody HoaDonThanhToanTaiQuayRequest request) {
        return ResponseEntity.ok(hoaDonService.thanhToanHoaDonTaiQuay(request));
    }

    @PostMapping("/thanh-toan-tai-quay-banking")
    public ResponseEntity<?> thanhToanTaiQuayBanking(@Valid @RequestBody HoaDonThanhToanTaiQuayRequest request) {
        return ResponseEntity.ok(hoaDonService.thanhToanHoaDonTaiQuayBanking(request));
    }

    @PostMapping("/dat-hang-tai-quay-banking")
    public ResponseEntity<?> datHangTaiQuayBanking(@Valid @RequestBody HoaDonThanhToanTaiQuayRequest request) {
        return ResponseEntity.ok(hoaDonService.datHangHoaDonTaiQuayBanking(request));
    }

    @PostMapping("/dat-hang-tai-quay")
    public ResponseEntity<?> datHangTaiQuay(@Valid @RequestBody DatHangTaiQuayRequest request) {
        return ResponseEntity.ok(hoaDonService.datHangTaiQuay(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHoaDon(@PathVariable("id") Long id) {
        hoaDonService.deleteHoaDon(id);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    @DeleteMapping("/delete-hdct/{id}")
    public ResponseEntity<?> deleteHoaDonChiTiet(@PathVariable("id") Long idHdct) {
        hoaDonChiTietService.deleteHoaDonChiTiet(idHdct);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    @DeleteMapping("/delete-all-hdct/{id}")
    public ResponseEntity<?> deleteAllHoaDonChiTiet(@PathVariable("id") Long idHd) {
        hoaDonService.deleteAllHoaDonChiTiet(idHd);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    @PostMapping("/get-all")
    public ResponseEntity<?> getAllActive(@RequestBody HoaDonSearchP hoaDonSearch) {
        return new ResponseEntity<>(hoaDonService.getAllBySearch(hoaDonSearch), HttpStatus.OK);
    }

    @PostMapping("/get-all-order-ngay-ship")
    public ResponseEntity<?> getAllOrderNgayShip(@RequestBody HoaDonSearchP hoaDonSearch) {
        return new ResponseEntity<>(hoaDonService.getAllBySearchOrderNgayShip(hoaDonSearch), HttpStatus.OK);
    }

    @PostMapping("/get-all-order-ngay-thanh-toan")
    public ResponseEntity<?> getAllOrderNgayThanhToan(@RequestBody HoaDonSearchP hoaDonSearch) {
        return new ResponseEntity<>(hoaDonService.getAllBySearchOrderNgayThanhToan(hoaDonSearch), HttpStatus.OK);
    }

    @GetMapping("/get-tra-cuu-don")
    public ResponseEntity<?> getTraCuuDon(@RequestParam("maHD") Long maHD, @RequestParam("sdt") String sdt) {
        return new ResponseEntity<>(hoaDonService.getTraCuuDon(maHD, sdt), HttpStatus.OK);
    }

    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity getDonHangByIdKhachHang(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "search", required = false) String searchText,
                                                  @RequestParam(value = "status", required = false) Integer status,
                                                  @PathVariable("idKhachHang") Long idKhachHang) {
        return new ResponseEntity(hoaDonService.getPageByIdKhachHang(page, searchText, status, idKhachHang), HttpStatus.OK);
    }

    @PostMapping("/xac-nhan-don-hang")
    public ResponseEntity<?> xacNhanDonHang(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(hoaDonService.xacNhanDonHang(ids));
    }

    @PostMapping("/xac-nhan-giao-hang")
    public ResponseEntity<?> xacNhanGiaoHang(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(hoaDonService.xacNhanGiaoHang(ids));
    }

    @PostMapping("/hoan-thanh-don-hang")
    public ResponseEntity<?> hoanThanhDonHang(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(hoaDonService.hoanThanhDonHang(ids));
    }

    @PostMapping("/tra-mot-phan")
    public ResponseEntity<?> traMotPhan(@RequestBody TraMotPhanRequest traMotPhanRequest) {
        return ResponseEntity.ok(hoaDonService.traMotPhan(traMotPhanRequest));
    }

    @PostMapping("/huy-don-hang")
    public ResponseEntity<?> huyDonHang(@Valid @RequestBody List<HuyDonRequest> requests) {
        return ResponseEntity.ok(hoaDonService.huyDonHang(requests));
    }

    @PostMapping("/xac-nhan-hoan")
    public ResponseEntity<?> xacNhanHoanDon(@RequestBody TraMotPhanRequest request) {
        return ResponseEntity.ok(hoaDonService.xacNhanHoanHang(request));
    }

    @GetMapping("/get-don-doi-tra/{id}")
    public ResponseEntity<?> getHoaDonDoiTra(@PathVariable("id")Long id) {
        return ResponseEntity.ok(hoaDonService.getHoaDonDoiTra(id));
    }

}
