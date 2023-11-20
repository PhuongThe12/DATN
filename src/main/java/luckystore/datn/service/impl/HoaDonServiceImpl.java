package luckystore.datn.service.impl;

import jakarta.transaction.Transactional;
import luckystore.datn.constraints.ErrorMessage;

import luckystore.datn.entity.*;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.HoaDonChiTietRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.KhuyenMaiChiTietResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.HoaDonService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    KhuyenMaiChiTietRepository khuyenMaiChiTietRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

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
    public void updateListHoaDon(List<HoaDonRequest> hoaDonRequestList) {

        List<HoaDon> hoaDonList = new ArrayList<>();
        for (HoaDonRequest hoaDonRequest : hoaDonRequestList) {
            HoaDon hoaDon = new HoaDon();
            hoaDon = getHoaDon(hoaDon, hoaDonRequest);

            hoaDonList.add(hoaDon);
        }
        hoaDonRepository.saveAll(hoaDonList);
    }

    private HoaDon getHoaDon(HoaDon hoaDon, HoaDonRequest hoaDonRequest) {

        NhanVien nhanVien = nhanVienRepository.findById(hoaDonRequest.getNhanVien().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("nhanVien", "Không tồn tại nhân viên này"))));
        KhachHang khachHang = khachHangRepository.findById(hoaDonRequest.getKhachHang().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khachHang", "Không tồn tại khách hàng này"))));
        hoaDon.setId(hoaDonRequest.getId());
//        hoaDon.setHoaDonGoc(hoaDonRequest.getHoaDonGoc());
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
        hoaDon.setListHoaDonChiTiet(getHoaDonChiTiet(hoaDonRequest.getListHoaDonChiTiet(), hoaDon));

        return hoaDon;
    }

    private Set<HoaDonChiTiet> getHoaDonChiTiet(Set<HoaDonChiTietRequest> hoaDonChiTietRequests, HoaDon hoaDon) {
        Set<HoaDonChiTiet> hoaDonChiTiets = new HashSet<>();
        for (HoaDonChiTietRequest h : hoaDonChiTietRequests) {
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

    public List<HoaDonBanHangResponse> getAllChuaThanhToan() {
        return hoaDonRepository.getAllChuaThanhToan();
    }

    @Override
    public HoaDonBanHangResponse getAllById(Long id) {
        List<HoaDonBanHangResponse> list = hoaDonRepository.getAllById(id);

        if (list.isEmpty()) {
            throw new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn")));
        }

        Map<Long, HoaDonBanHangResponse> responseMap = new HashMap<>();

        int count = 0;
        for (HoaDonBanHangResponse hd : list) {
            if (responseMap.containsKey(hd.getId())) {
                responseMap.get(hd.getId()).getHoaDonChiTiets().add(hd.getHoaDonChiTiets().get(0));
            } else {
                responseMap.put(hd.getId(), hd);
                count++;
            }
        }

        List<Long> idBienThes = new ArrayList<>();

        if (count > 1) {
            throw new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn")));
        }
        HoaDonBanHangResponse hoaDonBanHangResponse = new ArrayList<>(responseMap.values()).get(0);
        for (HoaDonChiTietResponse hc : hoaDonBanHangResponse.getHoaDonChiTiets()) {
            idBienThes.add(hc.getBienTheGiay().getId());
        }

        List<KhuyenMaiChiTietResponse> khuyenMaiChiTiets = khuyenMaiChiTietRepository.getAllByIdBienThe(idBienThes);
        for (KhuyenMaiChiTietResponse kmct : khuyenMaiChiTiets) {
            for (HoaDonChiTietResponse hoaDonChiTiet : hoaDonBanHangResponse.getHoaDonChiTiets()) {
                if (hoaDonChiTiet.getBienTheGiay().getId().equals(kmct.getBienTheGiayResponsel().getId())) {
                    hoaDonChiTiet.getBienTheGiay().setKhuyenMai(kmct.getPhanTramGiam());
                }
            }

        }

        return hoaDonBanHangResponse;
    }


    @Override
    public HoaDonBanHangResponse createNewHoaDon() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(0);
        hoaDon.setNgayTao(LocalDateTime.now());

        hoaDon = hoaDonRepository.save(hoaDon);
        return new HoaDonBanHangResponse(hoaDon, hoaDon.getTrangThai());
    }

    @Override
    public HoaDonChiTietResponse addProduct(AddOrderProcuctRequest addOrderProcuctRequest) {
        Optional<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findById(addOrderProcuctRequest.getIdHoaDon());

        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(addOrderProcuctRequest.getIdGiay()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy giày"))));

        int soLuongCu;

        HoaDonChiTiet hdct;
        if (hoaDonChiTiet.isPresent()) {
            hdct = hoaDonChiTiet.get();
            if (addOrderProcuctRequest.getSoLuong() == 0) {
                hoaDonChiTietRepository.deleteById(hdct.getId());
                return HoaDonChiTietResponse.builder().id(-1L).build();
            } else {
                soLuongCu = hdct.getSoLuong();
                hdct.setSoLuong(addOrderProcuctRequest.getSoLuong());
            }
        } else {
            throw new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn")));
        }

        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() + soLuongCu - addOrderProcuctRequest.getSoLuong());
        if (bienTheGiay.getSoLuong() < 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng đã hết")));
        }
        bienTheGiayRepository.save(bienTheGiay);

        hdct = hoaDonChiTietRepository.save(hdct);

        HoaDonChiTietResponse hoaDonChiTietResponse = new HoaDonChiTietResponse(hdct);
        KhuyenMaiChiTietResponse khuyenMaiChiTiet = khuyenMaiChiTietRepository.getByIdBienThe(bienTheGiay.getId());
        if (khuyenMaiChiTiet != null) {
            hoaDonChiTietResponse.getBienTheGiay().setKhuyenMai(khuyenMaiChiTiet.getPhanTramGiam());
        }

        return hoaDonChiTietResponse;
    }

    @Override
    public HoaDonChiTietResponse addNewHDCT(AddOrderProcuctRequest addOrderProcuctRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(addOrderProcuctRequest.getIdHoaDon()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(addOrderProcuctRequest.getIdGiay()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy giày"))));

        for (HoaDonChiTiet hoaDonChiTiet : hoaDon.getListHoaDonChiTiet()) {
            if (Objects.equals(hoaDonChiTiet.getBienTheGiay().getId(), bienTheGiay.getId())) {
                throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không hợp lệ")));
            }
        }

        HoaDonChiTiet hdct = new HoaDonChiTiet();
        hdct.setSoLuong(addOrderProcuctRequest.getSoLuong());
        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() - addOrderProcuctRequest.getSoLuong());
        if (bienTheGiay.getSoLuong() < 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng đã hết")));
        }
        bienTheGiayRepository.save(bienTheGiay);

        hdct.setHoaDon(hoaDon);
        hdct.setBienTheGiay(bienTheGiay);
        hdct = hoaDonChiTietRepository.save(hdct);

        HoaDonChiTietResponse hoaDonChiTietResponse = new HoaDonChiTietResponse(hdct.getId(), hoaDon.getId(), new BienTheGiayResponse(bienTheGiay), hdct.getSoLuong());
        KhuyenMaiChiTietResponse khuyenMaiChiTiet = khuyenMaiChiTietRepository.getByIdBienThe(bienTheGiay.getId());
        if (khuyenMaiChiTiet != null) {
            hoaDonChiTietResponse.getBienTheGiay().setKhuyenMai(khuyenMaiChiTiet.getPhanTramGiam());
        }

        return hoaDonChiTietResponse;
    }

    @Override
    @Transactional
    public String deleteHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        Set<HoaDonChiTiet> lstHoaDonCT = hoaDon.getListHoaDonChiTiet();
        List<BienTheGiay> bienTheGiays = new ArrayList<>();
        for (HoaDonChiTiet hd : lstHoaDonCT) {
            BienTheGiay bienThe = bienTheGiayRepository.findById(hd.getBienTheGiay().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
            bienThe.setSoLuong(bienThe.getSoLuong() + hd.getSoLuong());
            bienTheGiays.add(bienThe);
        }
        bienTheGiayRepository.saveAll(bienTheGiays);

        hoaDonRepository.delete(hoaDon);
        return "Xóa thành công";
    }

    @Override
    public void deleteAllHoaDonChiTiet(Long idHd) {
        HoaDon hoaDon = hoaDonRepository.findById(idHd).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        Set<HoaDonChiTiet> lstHoaDonCT = hoaDon.getListHoaDonChiTiet();
        List<BienTheGiay> bienTheGiays = new ArrayList<>();
        for (HoaDonChiTiet hd : lstHoaDonCT) {
            BienTheGiay bienThe = bienTheGiayRepository.findById(hd.getBienTheGiay().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
            bienThe.setSoLuong(bienThe.getSoLuong() + hd.getSoLuong());
            bienTheGiays.add(bienThe);
            if (bienThe.getSoLuong() < 0) {
                throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng đã hết")));
            }
        }
        bienTheGiayRepository.saveAll(bienTheGiays);

    }
}
