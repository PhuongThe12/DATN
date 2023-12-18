package luckystore.datn.service.impl;


import luckystore.datn.entity.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.YeuCauChiTietService;
import luckystore.datn.util.ConvertDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class YeuCauChiTietServiceImpl implements YeuCauChiTietService {

    private final YeuCauChiTietRepository yeuCauChiTietRepository;
    private final LyDoRepository lyDoRepository;
    private final BienTheGiayRepository bienTheGiayRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final YeuCauRepository yeuCauRepository;
    private final ImageHubService imageHubService;
    @Autowired
    public YeuCauChiTietServiceImpl(YeuCauChiTietRepository yeuCauChiTietRepository, ImageHubService imageHubService, LyDoRepository lyDoRepository, BienTheGiayRepository bienTheGiayRepository, HoaDonChiTietRepository hoaDonChiTietRepository, YeuCauRepository yeuCauRepository) {
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
        this.imageHubService = imageHubService;
        this.lyDoRepository = lyDoRepository;
        this.bienTheGiayRepository = bienTheGiayRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.yeuCauRepository = yeuCauRepository;
    }

    @Override
    public YeuCauChiTietResponse addYeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest, YeuCau yeuCau) {
        LyDo lyDo =lyDoRepository.findById(yeuCauChiTietRequest.getLyDo()).orElse(null);
        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(yeuCauChiTietRequest.getBienTheGiay()).orElse(null);
        HoaDonChiTiet hoaDonChiTiet  = hoaDonChiTietRepository.findById(yeuCauChiTietRequest.getHoaDonChiTiet()).orElse(null);
        YeuCauChiTiet yeuCauChiTiet = new YeuCauChiTiet(yeuCauChiTietRequest,hoaDonChiTiet,bienTheGiay!= null ? bienTheGiay : null,lyDo,yeuCau);
        yeuCauChiTiet.setTrangThai(1);
        return new YeuCauChiTietResponse(yeuCauChiTietRepository.save(yeuCauChiTiet));
    }

    @Override
    public List<YeuCauChiTietResponse> getAllYeuCauChiTietResponse(Long id) {
        return  yeuCauChiTietRepository.getPageResponse(id);
    }

    @Override
    public Long countRequestDetailsByStatus(String ngay1) {
        Date date1 = ConvertDate.convertStringToSQLDate(ngay1);
        return yeuCauChiTietRepository.countRequestDetailsByStatus(date1);
    }
}
