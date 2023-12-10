package luckystore.datn.rest.admin;

import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.LyDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/admin/thong-ke")
public class RestThongKeConTroller {

    private final GiayService giayService;
    private final LyDoService lyDoService;


    @Autowired
    public RestThongKeConTroller(GiayService giayService, LyDoService lyDoService) {
        this.giayService = giayService;
        this.lyDoService = lyDoService;
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
        return new ResponseEntity<>(giayService.findTopCartVariants(thongKeRequest), HttpStatus.OK);
    }

    //Top x Lý Do có tỷ lệ Xuất Hiện cao Trong Yêu Cầu Chi Tiết
    @PostMapping("/top-ly-do")
    public ResponseEntity<?> TopReasonsResponse(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(lyDoService.findReasonsForReturn(thongKeRequest), HttpStatus.OK);
    }

    //Top x Biến Thể Giày có tỷ lệ đổi trả cao
    @PostMapping("/top-bien-the-ty-le-tra")
    public ResponseEntity<?> findVariantReturnRates(@RequestBody ThongKeRequest thongKeRequest){
        return new ResponseEntity<>(giayService.findVariantReturnRates(thongKeRequest), HttpStatus.OK);
    }




}
