package luckystore.datn.service.impl;


import luckystore.datn.entity.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.YeuCauChiTietService;
import luckystore.datn.service.YeuCauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class YeuCauServiceIplm implements YeuCauService {

    private final YeuCauRepository yeuCauRepository;
    private final YeuCauChiTietRepository yeuCauChiTietRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final BienTheGiayRepository bienTheGiayRepository;
    private final LyDoRepository lyDoRepository;
    private final YeuCauChiTietService yeuCauChiTietService;
    private final ImageHubService imageHubService;

    @Autowired
    public YeuCauServiceIplm(YeuCauRepository yeuCauRepo, YeuCauChiTietRepository yeuCauChiTietRepository, HoaDonRepository hoaDonRepository, HoaDonChiTietRepository hoaDonChiTietRepository, BienTheGiayRepository bienTheGiayRepository, LyDoRepository lyDoRepository, YeuCauChiTietService yeuCauChiTietService, ImageHubService imageHubService) {
        this.yeuCauRepository = yeuCauRepo;
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.bienTheGiayRepository = bienTheGiayRepository;
        this.lyDoRepository = lyDoRepository;
        this.yeuCauChiTietService = yeuCauChiTietService;
        this.imageHubService = imageHubService;
    }

    public List<YeuCauResponse> getAll() {
        return yeuCauRepository.finAllResponse();
    }

    @Override
    public YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(yeuCauRequest.getHoaDon()).orElse(null);
        YeuCau yeuCauSaved = new YeuCau(yeuCauRequest, hoaDon,null,LocalDateTime.now(), LocalDateTime.now());
        List<YeuCauChiTiet> listYeuCauChiTiet = new ArrayList<>();
        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            LyDo lyDo = lyDoRepository.findById(ycctRequest.getLyDo()).orElse(null);
            BienTheGiay bienTheGiay = ycctRequest.getBienTheGiay() != null ? bienTheGiayRepository.findById(ycctRequest.getBienTheGiay()).orElse(null) : null;
            if (bienTheGiay != null) {
                bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() - 1);
                bienTheGiayRepository.save(bienTheGiay).getSoLuong();
            }
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycctRequest.getHoaDonChiTiet()).orElse(null);
            YeuCauChiTiet ycct = new YeuCauChiTiet(ycctRequest, hoaDonChiTiet, bienTheGiay, lyDo, yeuCauSaved);
            listYeuCauChiTiet.add(ycct);
        }
        yeuCauSaved.setListYeuCauChiTiet(listYeuCauChiTiet);
        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }

    private Integer updateSoluongBienTheGiay(int soLuongDoi, Long idBienTheGiay) {
        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(idBienTheGiay).orElse(null);
        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() - soLuongDoi);
        return bienTheGiayRepository.save(bienTheGiay).getSoLuong();
    }

    @Override
    public YeuCauResponse confirmYeuCau(YeuCauRequest yeuCauRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(yeuCauRequest.getHoaDon()).orElse(null);
        YeuCau yeuCauSaved = YeuCau.builder().id(yeuCauRequest.getId()).hoaDon(hoaDon).nguoiThucHien(yeuCauRequest.getNguoiThucHien()).trangThai(yeuCauRequest.getTrangThai()).ngayTao(yeuCauRequest.getNgayTao()).ngaySua(LocalDateTime.now()).ghiChu(yeuCauRequest.getGhiChu()).build();
        List<YeuCauChiTiet> listYeuCauChiTiet = new ArrayList<>();

        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {

            HoaDonChiTiet hoaDonChiTietUpdate = hoaDonChiTietRepository.findById(ycctRequest.getHoaDonChiTiet()).orElse(null);
            BienTheGiay bienTheGiayTra = bienTheGiayRepository.findById(ycctRequest.getBienTheGiayTra()).orElse(null);
            BienTheGiay bienTheGiayDoi = ycctRequest.getBienTheGiay() == null ? null : bienTheGiayRepository.findById(ycctRequest.getBienTheGiay()).orElse(null);
            LyDo lyDoUpdate = lyDoRepository.findById(ycctRequest.getLyDo()).orElse(null);

            if (ycctRequest.getLoaiYeuCauChiTiet() != 3) {
                if (ycctRequest.getTinhTrangSanPham() == false) { //giày không lỗi
                    bienTheGiayTra.setSoLuong(bienTheGiayTra.getSoLuong() + 1);
                    bienTheGiayRepository.save(bienTheGiayTra);
                } else if (ycctRequest.getTinhTrangSanPham() == true) { // giày lỗi
                    bienTheGiayTra.setSoLuongLoi(bienTheGiayTra.getSoLuongLoi() + 1);
                    bienTheGiayRepository.save(bienTheGiayTra);
                }

                hoaDonChiTietUpdate.setSoLuongTra(hoaDonChiTietUpdate.getSoLuongTra() + 1);


                if (ycctRequest.getLoaiYeuCauChiTiet() == 1) { //nếu đổi
                    YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(1).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                    listYeuCauChiTiet.add(yeuCauChiTietSaved);
                } else if (ycctRequest.getLoaiYeuCauChiTiet() == 2) { //nếu trả
                    if (bienTheGiayDoi != null) { //trả từ hủy đổi
                        bienTheGiayDoi.setSoLuong(bienTheGiayDoi.getSoLuong() + 1);
                        YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(4).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                        listYeuCauChiTiet.add(yeuCauChiTietSaved);
                    } else { //trả thờng
                        YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(null).lyDo(lyDoUpdate).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(1).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                        listYeuCauChiTiet.add(yeuCauChiTietSaved);
                    }
                }
            } else { // nếu là hủy
                if (bienTheGiayDoi != null) {
                    bienTheGiayDoi.setSoLuong(bienTheGiayDoi.getSoLuong() + 1);
                }
                YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(ycctRequest.getTrangThai()).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                listYeuCauChiTiet.add(yeuCauChiTietSaved);
            }
        }
        yeuCauSaved.setListYeuCauChiTiet(listYeuCauChiTiet);
        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }

    @Override
    public YeuCauResponse unConfirmYeuCau(YeuCauRequest yeuCauRequest) {
        YeuCau yeuCauSaved = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);
        yeuCauSaved.setNgaySua(LocalDateTime.now());
        yeuCauSaved.setGhiChu(yeuCauRequest.getGhiChu());
        yeuCauSaved.setTrangThai(3);

        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            YeuCauChiTiet yeuCauChiTiet = yeuCauChiTietRepository.findById(ycctRequest.getId()).orElse(null);
            yeuCauChiTiet.setLoaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet());
            yeuCauChiTiet.setTrangThai(ycctRequest.getTrangThai());
            yeuCauChiTiet.setTinhTrangSanPham(ycctRequest.getTinhTrangSanPham());
            if (ycctRequest.getBienTheGiay()!=null) {
                BienTheGiay bienTheGiayDoi = bienTheGiayRepository.findById(ycctRequest.getBienTheGiay()).orElse(null);
                bienTheGiayDoi.setSoLuong(bienTheGiayDoi.getSoLuong() + 1);
                bienTheGiayRepository.save(bienTheGiayDoi);
            }
            yeuCauChiTietRepository.save(yeuCauChiTiet);
        }

        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }

    @Override
    public YeuCauResponse updateYeuCau(YeuCauRequest yeuCauRequest) {

        YeuCau yeuCauSaved = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);
        yeuCauSaved.setNgaySua(LocalDateTime.now());
        yeuCauSaved.setGhiChu(yeuCauRequest.getGhiChu());

        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            YeuCauChiTiet yeuCauChiTiet = yeuCauChiTietRepository.findById(ycctRequest.getId()).orElse(null);
            yeuCauChiTiet.setGhiChu(ycctRequest.getGhiChu());
            yeuCauChiTiet.setLyDo(lyDoRepository.findById(ycctRequest.getLyDo()).orElse(null));
            if (yeuCauChiTiet.getTinhTrangSanPham() == true && ycctRequest.getTinhTrangSanPham() == false) {
                BienTheGiay bienTheGiayTra = bienTheGiayRepository.findById(ycctRequest.getBienTheGiayTra()).orElse(null);
                bienTheGiayTra.setSoLuongLoi(bienTheGiayTra.getSoLuongLoi() - 1);
                bienTheGiayTra.setSoLuong(bienTheGiayTra.getSoLuong() + 1);
                bienTheGiayRepository.save(bienTheGiayTra);
            } else if (yeuCauChiTiet.getTinhTrangSanPham() == false && ycctRequest.getTinhTrangSanPham() == true) {
                BienTheGiay bienTheGiayTra = bienTheGiayRepository.findById(ycctRequest.getBienTheGiayTra()).orElse(null);
                bienTheGiayTra.setSoLuongLoi(bienTheGiayTra.getSoLuongLoi() + 1);
                bienTheGiayTra.setSoLuong(bienTheGiayTra.getSoLuong() - 1);
                bienTheGiayRepository.save(bienTheGiayTra);
            }
            yeuCauChiTiet.setTinhTrangSanPham(ycctRequest.getTinhTrangSanPham());

            yeuCauChiTietRepository.save(yeuCauChiTiet);
        }

        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }



    @Override
    public YeuCauResponse findById(Long id) {
        return new YeuCauResponse(yeuCauRepository.findById(id).orElse(null));
    }

    @Override
    public YeuCauResponse findByStatus() {
        return new YeuCauResponse(yeuCauRepository.findResponseByStatus());
    }

    @Override
    public Page<YeuCauResponse> getPage(Integer page, Long searchText, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai) {
        return yeuCauRepository.getPageResponse(PageRequest.of((page - 1), 6), searchText, ngayBatDau, ngayKetThuc, trangThai);
    }


    public Date fomatDate(Date date, String string) {

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
