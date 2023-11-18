package luckystore.datn.service.impl;


import luckystore.datn.entity.AnhGiayTra;
import luckystore.datn.entity.YeuCauChiTiet;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.repository.YeuCauChiTietRepository;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.YeuCauChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class YeuCauChiTietServiceImpl implements YeuCauChiTietService {

    private final YeuCauChiTietRepository yeuCauChiTietRepository;

    private final ImageHubService imageHubService;
    @Autowired
    public YeuCauChiTietServiceImpl(YeuCauChiTietRepository yeuCauChiTietRepository, ImageHubService imageHubService) {
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
        this.imageHubService = imageHubService;
    }

    @Override
    public YeuCauChiTietResponse addYeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest) {
        YeuCauChiTiet yeuCauChiTiet = new YeuCauChiTiet(yeuCauChiTietRequest);

        List<AnhGiayTra> anhGiayTras = new ArrayList<>();

        if(yeuCauChiTietRequest.getListAnhGiayTra() != null){
            for (String anh : yeuCauChiTietRequest.getListAnhGiayTra()) {
                String file = imageHubService.base64ToFile(anh);
                anhGiayTras.add(AnhGiayTra.builder().yeuCauChiTiet(yeuCauChiTiet).link(file).build());
            }
        }
        return new YeuCauChiTietResponse(yeuCauChiTietRepository.save(yeuCauChiTiet));
    }
}
