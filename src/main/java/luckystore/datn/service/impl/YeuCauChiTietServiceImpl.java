package luckystore.datn.service.impl;


import luckystore.datn.entity.YeuCauChiTiet;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.repository.YeuCauChiTietRepository;
import luckystore.datn.service.YeuCauChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YeuCauChiTietServiceImpl implements YeuCauChiTietService {

    private final YeuCauChiTietRepository yeuCauChiTietRepository;

    @Autowired
    public YeuCauChiTietServiceImpl(YeuCauChiTietRepository yeuCauChiTietRepository) {
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
    }

    @Override
    public YeuCauChiTietResponse addYeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest) {
        YeuCauChiTiet yeuCauChiTiet = new YeuCauChiTiet(yeuCauChiTietRequest);
        return new YeuCauChiTietResponse(yeuCauChiTietRepository.save(yeuCauChiTiet));
    }
}
