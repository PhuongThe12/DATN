package luckystore.datn.rest.admin;

import lombok.RequiredArgsConstructor;
import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.LyDoService;
import luckystore.datn.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/admin/thong-ke")
@RequiredArgsConstructor
public class RestThongKeConTroller {

    private final GiayService giayService;
    private final LyDoService lyDoService;

    private final ThongKeService thongKeService;

//     Danh sách giày bán chạy
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

    @PostMapping("/tong-quan")
    public ResponseEntity<?> thongKeTongQuan(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(thongKeService.getThongKeTongQuan(thongKeRequest), HttpStatus.OK);
    }

    @PostMapping("/san-pham-ban-chay")
    public ResponseEntity<?> topGiayBanChay(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(thongKeService.getSanPhamBanChay(thongKeRequest), HttpStatus.OK);
    }

    @PostMapping("/danh-sach-hoa-don")
    public ResponseEntity<?> danhSachHoaDon(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(thongKeService.getListHoaDonThongKe(thongKeRequest), HttpStatus.OK);
    }

    @PostMapping("/theo-nam")
    public ResponseEntity<?> thongKeTongQuanTheoNam(@RequestParam Integer year){
        return new ResponseEntity<>(thongKeService.getThongKeTheoNam(year), HttpStatus.OK);
    }

}
