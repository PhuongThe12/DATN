package luckystore.datn.service.user.impl;

import luckystore.datn.entity.*;
import luckystore.datn.infrastructure.constraints.TrangThaiYeuCau;
import luckystore.datn.infrastructure.constraints.TrangThaiYeuCauChiTiet;
import luckystore.datn.infrastructure.security.session.SessionService;
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

    private final YeuCauChiTietRepository yeuCauChiTietRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final LyDoRepository lyDoRepository;

    private final SessionService sessionService;
    private final BienTheGiayRepository bienTheGiayRepository;



    @Autowired
    public YeuCauKhachHangServiceImpl(HoaDonRepository hoaDonRepository, YeuCauRepository yeuCauRepository, YeuCauChiTietRepository yeuCauChiTietRepository, HoaDonChiTietRepository hoaDonChiTietRepository, LyDoRepository lyDoRepository, SessionService sessionService, BienTheGiayRepository bienTheGiayRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.yeuCauRepository = yeuCauRepository;
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.lyDoRepository = lyDoRepository;
        this.sessionService = sessionService;
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
            hoaDonChiTiet.setSoLuongTra(hoaDonChiTiet.getSoLuongTra()+1);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            LyDo lyDo = lyDoRepository.findById(ycct.getLyDo()).orElse(null);
            BienTheGiay bienTheGiayDoi = ycct.getBienTheGiay() == null ? null : bienTheGiayRepository.findById(ycct.getBienTheGiay()).orElse(null);
            YeuCauChiTiet yeuCauChiTiet = new YeuCauChiTiet(yeuCauSave,hoaDonChiTiet,bienTheGiayDoi,lyDo,ycct.getTienGiam(),ycct.getThanhTien(),ycct.getTrangThai(),ycct.getLoaiYeuCauChiTiet(),ycct.getTinhTrangSanPham(),ycct.getGhiChu());
            yeuCauChiTietList.add(yeuCauChiTiet);
        }
        yeuCauSave.setListYeuCauChiTiet(yeuCauChiTietList);
        return new YeuCauResponse(yeuCauRepository.save(yeuCauSave));
    }

    @Override
    public YeuCauResponse updateYeuCau(YeuCauRequest yeuCauRequest) {
        YeuCau yeuCauSave = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);
        //ngày sửa
        yeuCauSave.setNgaySua(LocalDateTime.now());

        yeuCauSave.setGhiChu(yeuCauRequest.getGhiChu());

        List<YeuCauChiTiet> yeuCauChiTietList = new ArrayList<>();
        for (YeuCauChiTietRequest ycct: yeuCauRequest.getListYeuCauChiTiet()) {
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycct.getHoaDonChiTiet()).orElse(null);
            hoaDonChiTiet.setSoLuongTra(hoaDonChiTiet.getSoLuongTra()-1);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            LyDo lyDo = lyDoRepository.findById(ycct.getLyDo()).orElse(null);
            BienTheGiay bienTheGiayDoi = ycct.getBienTheGiay() == null ? null : bienTheGiayRepository.findById(ycct.getBienTheGiay()).orElse(null);
            YeuCauChiTiet yeuCauChiTiet = YeuCauChiTiet.builder()
                    .id(ycct.getId())
                    .yeuCau(yeuCauSave)
                    .hoaDonChiTiet(hoaDonChiTiet)
                    .bienTheGiay(bienTheGiayDoi)
                    .lyDo(lyDo)
                    .tienGiam(ycct.getTienGiam())
                    .thanhTien(ycct.getThanhTien())
                    .trangThai(ycct.getTrangThai())
                    .tinhTrangSanPham(false)
                    .loaiYeuCauChiTiet(ycct.getLoaiYeuCauChiTiet())
                    .ghiChu(ycct.getGhiChu())
                    .build();
            yeuCauChiTietList.add(yeuCauChiTiet);
        }
        yeuCauSave.setListYeuCauChiTiet(yeuCauChiTietList);

//        return new YeuCauResponse(yeuCauRepository.save(yeuCauSave));
        return null;
    }

    @Override
    public YeuCauResponse cancelYeuCau(YeuCauRequest yeuCauRequest) {
        YeuCau yeuCauSaved = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);
        //ngày từ chối
        yeuCauSaved.setNgaySua(LocalDateTime.now());
        //người sửa
        //người từ chối
        yeuCauSaved.setGhiChu(yeuCauRequest.getGhiChu());
        yeuCauSaved.setTrangThai(TrangThaiYeuCau.BI_HUY);

        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            YeuCauChiTiet yeuCauChiTiet = yeuCauChiTietRepository.findById(ycctRequest.getId()).orElse(null);
            yeuCauChiTiet.setLoaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet());
            yeuCauChiTiet.setTrangThai(TrangThaiYeuCauChiTiet.HUY_TRA);
            yeuCauChiTiet.setTinhTrangSanPham(ycctRequest.getTinhTrangSanPham());
            yeuCauChiTietRepository.save(yeuCauChiTiet);
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycctRequest.getHoaDonChiTiet()).orElse(null);
            hoaDonChiTiet.setSoLuongTra(hoaDonChiTiet.getSoLuongTra() - 1);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }

    @Override
    public List<YeuCauResponse> getListYeuCau(Long idHoaDon) {
        return yeuCauRepository.getListYeuCau(idHoaDon);
    }

}
