package luckystore.datn.service.impl;


import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.YeuCau;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.YeuCauRepository;
import luckystore.datn.service.YeuCauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class YeuCauServiceIplm implements YeuCauService {


    @Autowired
    private YeuCauRepository yeuCauRepo;

    public List<YeuCauResponse> getAll() {
        return yeuCauRepo.finAllResponse();
    }

    @Override
    public YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest) {
        YeuCau yeuCau = new YeuCau(yeuCauRequest);
        yeuCau.setNgayTao(new Date());
        yeuCau.setNgaySua(new Date());
        return new YeuCauResponse(yeuCauRepo.save(yeuCau));
    }

    @Override
    public YeuCauResponse updateYeuCau(Long id, YeuCauRequest yeuCauRequest) {
        YeuCau yeuCau;
        if (id == null) {
            throw new NullException();
        } else {
            yeuCau = yeuCauRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        yeuCau = new YeuCau(yeuCauRequest);
        yeuCau.setId(id);

        return new YeuCauResponse(yeuCauRepo.save(yeuCau));
    }

    @Override
    public YeuCauResponse findById(Long id) {
        return new YeuCauResponse(yeuCauRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public Page<YeuCauResponse> getPage(Integer page,String searchText ,Date ngayBatDau, Date ngayKetThuc, Integer loaiYeuCau, Integer trangThai) {
        return yeuCauRepo.getPageResponse(ngayBatDau,searchText,ngayKetThuc,loaiYeuCau,trangThai, PageRequest.of((page - 1), 5));
    }


    public Date fomatDate(Date date, String string){

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf1.format(date);
        //2023-10-26 + 23:59:59
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Chuyển đổi chuỗi thành đối tượng Date
            return sdf.parse(date1 + string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
