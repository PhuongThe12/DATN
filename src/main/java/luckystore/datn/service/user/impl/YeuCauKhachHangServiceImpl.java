package luckystore.datn.service.user.impl;

import luckystore.datn.entity.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.user.YeuCauKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class YeuCauKhachHangServiceImpl implements YeuCauKhachHangService {
    private final HoaDonRepository hoaDonRepository;
    private final YeuCauRepository yeuCauRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final LyDoRepository lyDoRepository;

    private final BienTheGiayRepository bienTheGiayRepository;



    @Autowired
    public YeuCauKhachHangServiceImpl(HoaDonRepository hoaDonRepository, YeuCauRepository yeuCauRepository, HoaDonChiTietRepository hoaDonChiTietRepository, LyDoRepository lyDoRepository, BienTheGiayRepository bienTheGiayRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.yeuCauRepository = yeuCauRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.lyDoRepository = lyDoRepository;
        this.bienTheGiayRepository = bienTheGiayRepository;
    }

    @Override
    public HoaDonYeuCauRespone getOneHoaDonYeuCauRespone(Long id) {
        return hoaDonRepository.getOneHoaDonYeuCau(id);
    }

    @Override
    public YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(yeuCauRequest.getHoaDon()).orElse(null);
        YeuCau yeuCauSave = new YeuCau(yeuCauRequest,hoaDon,null, LocalDateTime.now(),LocalDateTime.now());
        List<YeuCauChiTiet> yeuCauChiTietList = new ArrayList<>();
        for (YeuCauChiTietRequest ycct: yeuCauRequest.getListYeuCauChiTiet()) {
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycct.getHoaDonChiTiet()).orElse(null);
            LyDo lyDo = lyDoRepository.findById(ycct.getLyDo()).orElse(null);
            BienTheGiay bienTheGiayDoi = ycct.getBienTheGiay() == null ? null : bienTheGiayRepository.findById(ycct.getBienTheGiayTra()).orElse(null);
            YeuCauChiTiet yeuCauChiTiet = new YeuCauChiTiet(yeuCauSave,hoaDonChiTiet,bienTheGiayDoi,lyDo,ycct.getTienGiam(),ycct.getThanhTien(),ycct.getTrangThai(),ycct.getLoaiYeuCauChiTiet(),ycct.getTinhTrangSanPham(),ycct.getGhiChu());
            yeuCauChiTietList.add(yeuCauChiTiet);
        }

        yeuCauSave.setListYeuCauChiTiet(yeuCauChiTietList);
        return new YeuCauResponse(yeuCauRepository.save(yeuCauSave));
    }
}
