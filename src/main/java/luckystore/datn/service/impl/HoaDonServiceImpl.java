package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.*;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.HoaDonChiTietRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.NhanVienRepository;
import luckystore.datn.service.HoaDonService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    BienTheGiayRepository bienTheGiayRepository;

    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepository.findAllResponse();
    }

    @Override
    public Page<HoaDonResponse> getPage(int page, String searchText, Integer status) {
        return hoaDonRepository.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public Page<HoaDonYeuCauRespone> getPageHoaDonYeuCau(HoaDonSearch hoaDonSearch) {
        Pageable pageable = PageRequest.of(hoaDonSearch.getCurrentPage() - 1, hoaDonSearch.getPageSize());
        return hoaDonRepository.getPageHoaDonYeuCauResponse(hoaDonSearch, pageable);
    }

    @Override
    public HoaDonYeuCauRespone getHoaDonYeuCau(Long id) {
        return hoaDonRepository.getHoaDonYeuCauResponse(id);
    }

    @Override
    public HoaDonResponse findById(Long id) {
        return new HoaDonResponse(hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public HoaDonResponse updateListHoaDon(List<HoaDonRequest> hoaDonRequestList) {

        List<HoaDon> hoaDonList = new ArrayList<>();
        for(HoaDonRequest hoaDonRequest : hoaDonRequestList){
            HoaDon hoaDon = new HoaDon();
            hoaDon = getHoaDon(hoaDon,hoaDonRequest);

            hoaDonList.add(hoaDon);
        }
        return new HoaDonResponse((HoaDon) hoaDonRepository.saveAll(hoaDonList));
    }

    private HoaDon getHoaDon(HoaDon hoaDon, HoaDonRequest hoaDonRequest) {

        NhanVien nhanVien = nhanVienRepository.findById(hoaDonRequest.getNhanVien().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("nhanVien", "Không tồn tại nhân viên này"))));
        KhachHang khachHang = khachHangRepository.findById(hoaDonRequest.getNhanVien().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khachHang", "Không tồn tại khách hàng này"))));
        hoaDon.setId(hoaDonRequest.getId());
        hoaDon.setHoaDonGoc(hoaDonRequest.getHoaDonGoc());
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setNgayTao(hoaDonRequest.getNgayTao());
        hoaDon.setNgayShip(hoaDonRequest.getNgayShip());
        hoaDon.setNgayNhan(hoaDonRequest.getNgayNhan());
        hoaDon.setNgayThanhToan(hoaDonRequest.getNgayThanhToan());
        hoaDon.setKenhBan(hoaDonRequest.getKenhBan());
        hoaDon.setLoaiHoaDon(hoaDonRequest.getLoaiHoaDon());
        hoaDon.setMaVanDon(hoaDonRequest.getMaVanDon());
        hoaDon.setEmail(hoaDonRequest.getEmail());
        hoaDon.setPhiShip(hoaDonRequest.getPhiShip());
        hoaDon.setSoDienThoaiNhan(hoaDonRequest.getSoDienThoaiNhan());
        hoaDon.setDiaChiNhan(hoaDonRequest.getDiaChiNhan());
        hoaDon.setTrangThai(hoaDonRequest.getTrangThai());
        hoaDon.setGhiChu(hoaDonRequest.getGhiChu());
        hoaDon.setListHoaDonChiTiet(getHoaDonChiTiet(hoaDonRequest.getListHoaDonChiTiet(),hoaDon));

        return hoaDon;
    }

    private Set<HoaDonChiTiet> getHoaDonChiTiet(Set<HoaDonChiTietRequest> hoaDonChiTietRequests, HoaDon hoaDon) {
        Set<HoaDonChiTiet> hoaDonChiTiets = new HashSet<>();
        for (HoaDonChiTietRequest h :hoaDonChiTietRequests) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setId(h.getId());
            hoaDonChiTiet.setHoaDon(hoaDon);

            BienTheGiay bienTheGiay = bienTheGiayRepository.findById(h.getBienTheGiay().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("bienTheGiay", "Không tồn tại biến thể giày này"))));
            System.out.println(bienTheGiay.toString());
            hoaDonChiTiet.setBienTheGiay(bienTheGiay);
            hoaDonChiTiet.setDonGia(h.getDonGia());
            hoaDonChiTiet.setSoLuong(h.getSoLuong());
            hoaDonChiTiet.setSoLuongTra(h.getSoLuongTra());
            hoaDonChiTiet.setTrangThai(h.getTrangThai());
            hoaDonChiTiet.setGhiChu(h.getGhiChu());
        }
        return hoaDonChiTiets;
    }
}
