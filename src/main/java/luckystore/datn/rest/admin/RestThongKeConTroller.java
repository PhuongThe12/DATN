package luckystore.datn.rest.admin;

import lombok.RequiredArgsConstructor;
import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.HoaDonService;
import luckystore.datn.service.LyDoService;
import luckystore.datn.service.YeuCauChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.sql.Date;

@RestController
@RequestMapping("/rest/admin/thong-ke")
@RequiredArgsConstructor
public class RestThongKeConTroller {

    private final GiayService giayService;
    private final LyDoService lyDoService;
    private final HoaDonService hoaDonService;

    private final YeuCauChiTietService yeuCauChiTietService;



    // Danh sách giày bán chạy
    @PostMapping("/giay-ban-chay")
    public ResponseEntity<?> findTopSellingShoes(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(giayService.findTopSellingShoes(thongKeRequest), HttpStatus.OK);
    }

    //Top x giày bán chạy trong y ngày
    @PostMapping("/top-giay-ban-chay")
    public ResponseEntity<?> findTopSellingShoesInLastDays(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(giayService.findTopSellingShoesInLastDays(thongKeRequest), HttpStatus.OK);
    }

    //Top x biến thể giày bán chạy trong y ngày
    @PostMapping("/top-bien-the-ban-chay")
    public ResponseEntity<?> findTopSellingShoeVariantInLastDays(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(giayService.findTopSellingShoeVariantInLastDays(thongKeRequest), HttpStatus.OK);
    }

    //Top x giày xuất hiện trong sản phẩm yêu thích
    @PostMapping("/top-giay-yeu-thich")
    public ResponseEntity<?> findTopFavoritedShoes(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(giayService.findTopFavoritedShoes(thongKeRequest), HttpStatus.OK);
    }

    //Top x Biến Thể Giày Xuất Hiện Trong Giỏ Hàng
    @PostMapping("/top-giay-gio-hang")
    public ResponseEntity<?> findTopCartVariants(@RequestBody ThongKeRequest thongKeRequest){
        System.out.println("Giỏ hàng: "+thongKeRequest);
        return new ResponseEntity<>(giayService.findTopCartVariants(thongKeRequest), HttpStatus.OK);
    }

    //Top x Lý Do có tỷ lệ Xuất Hiện cao Trong Yêu Cầu Chi Tiết
    @PostMapping("/top-ly-do")
    public ResponseEntity<?> findTopReasons(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(lyDoService.findReasonsForReturn(thongKeRequest), HttpStatus.OK);
    }

    //Top x Biến Thể Giày có tỷ lệ đổi trả cao
    @PostMapping("/top-bien-the-ty-le-tra")
    public ResponseEntity<?> findTopVariantReturnRates(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(giayService.findVariantReturnRates(thongKeRequest), HttpStatus.OK);
    }

    @GetMapping("/cout-so-luong")
    public ResponseEntity<?> countRequestDetailsByStatus(@PathVariable("date") Date ngay1){
        return new ResponseEntity<>(yeuCauChiTietService.countRequestDetailsByStatus(ngay1), HttpStatus.OK);
    }

    @GetMapping("/hang_khach_hang")
    public ResponseEntity<?> thongKeByHangKhachHang() {
        return ResponseEntity.ok(hoaDonService.getDoanhThuByHangKhachHang());
    }

    @GetMapping("/thuong_hieu")
    public ResponseEntity<?> thongKeByThuongHieu() {
        return ResponseEntity.ok(hoaDonService.getDoanhThuByThuongHieu());
    }

    @GetMapping("/top-ban-chay")
    public ResponseEntity<?> getTopBanChay() {
        return ResponseEntity.ok(giayService.getTopGiayBanChay());
    }

    @GetMapping("/doanh-thu-theo-nam")
    public ResponseEntity<?> getDoanhThuTheoNam(@RequestParam("year") Integer year) {
        return ResponseEntity.ok(hoaDonService.getThongKeTheoNam(year));
    }

    @GetMapping("/tong-quan")
    public ResponseEntity<?> getTongQuan(@RequestParam("ngay1") String ngay1) {
        return ResponseEntity.ok(hoaDonService.getThongKeTongQuan(ngay1));
    }

}
