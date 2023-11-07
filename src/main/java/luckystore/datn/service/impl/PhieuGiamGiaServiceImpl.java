package luckystore.datn.service.impl;

import luckystore.datn.common.PageableRequest;
import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.infrastructure.constant.Contants;
import luckystore.datn.infrastructure.constant.PaginationConstant;
import luckystore.datn.model.request.PhieuGiamGiaRequest;
import luckystore.datn.model.response.PhieuGiamGiaResponse;
import luckystore.datn.repository.HangKhachHangRepository;
import luckystore.datn.repository.NhanVienRepository;
import luckystore.datn.repository.PhieuGiamGiaRepository;
import luckystore.datn.service.PhieuGiamGiaService;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PhieuGiamGiaServiceImpl implements PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private HangKhachHangRepository hangKhachHangRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PhieuGiamGiaResponse> getAll() {
        return phieuGiamGiaRepository.getAll();
    }

    @Override
    public Page<PhieuGiamGiaResponse> getPagePhieuGiamGia(int page) {

        Pageable pageable = PageRequest.of(page -1 , PaginationConstant.DEFAULT_SIZE);
        Page<PhieuGiamGiaResponse> res = phieuGiamGiaRepository.getPagePhieuGiamGia(pageable);
        return res;
    }

    @Override
    public PhieuGiamGiaResponse findPhieuGiamGiaById(Long id) {

        PhieuGiamGiaResponse response = phieuGiamGiaRepository.getPhieuGiamGiaById(id);
        if (response == null) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
        return response;
    }

    @Override
    public PhieuGiamGia addPhieuGiamGia(PhieuGiamGiaRequest request) {

        PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
        checkToAdd(request);
        phieuGiamGia = convertToPhieuGiamGia(phieuGiamGia, request);
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    @Override
    public PhieuGiamGia updatePhieuGiamGia(Long id, PhieuGiamGiaRequest request) {

        PhieuGiamGia phieuGiamGia;
        if (id == null) {
            throw new NullException();
        } else {
            phieuGiamGia = phieuGiamGiaRepository.findById(id).
                    orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        checkToUpdate(request);
        phieuGiamGia = convertToPhieuGiamGia(phieuGiamGia, request);
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    public void checkToAdd(PhieuGiamGiaRequest request) {

        JSONObject error = new JSONObject();
        if (!hangKhachHangRepository.existsHangKhachHangByTenHang(request.getDoiTuongApDung())) {
            error.put("doiTuongApDung", ErrorMessage.HANG_KHACH_HANG_NOT_EXIST);
        }

        if (request.getNgayBatDau().isAfter(request.getNgayKetThuc())) {
            error.put("ngayBatDau", ErrorMessage.INVALID_NGAY_BAT_DAU);
        }

        if (request.getNgayKetThuc().isBefore(request.getNgayBatDau())) {
            error.put("ngayketThuc", ErrorMessage.INVALID_NGAY_KET_THUC);
        }

        if (ChronoUnit.DAYS.between(request.getNgayBatDau(), request.getNgayKetThuc()) < Contants.MOC_THOI_HAN) {
            error.put("ngayketThuc", ErrorMessage.MOC_THOI_GIAN_PHIEU_GIAM_GIA);
        }

        if (!error.isEmpty()) {
            throw new DuplicateException(error);
        }
    }

    public void checkToUpdate(PhieuGiamGiaRequest request) {

        JSONObject error = new JSONObject();
        if (!hangKhachHangRepository.existsHangKhachHangByTenHang(request.getDoiTuongApDung())) {
            error.put("doiTuongApDung", ErrorMessage.HANG_KHACH_HANG_NOT_EXIST);
        }

        if (phieuGiamGiaRepository.existsPhieuGiamGiaByMaGiamGia(request.getMaGiamGia())) {
            error.put("maPhieuGiamGia", ErrorMessage.PHIEU_GIAM_GIA_ALREADY_EXIST);
        }

        if (request.getNgayBatDau().isAfter(request.getNgayKetThuc())) {
            error.put("ngayBatDau", ErrorMessage.INVALID_NGAY_BAT_DAU);
        }

        if (request.getNgayKetThuc().isBefore(request.getNgayBatDau())) {
            error.put("ngayketThuc", ErrorMessage.INVALID_NGAY_KET_THUC);
        }

        if (ChronoUnit.DAYS.between(request.getNgayBatDau(), request.getNgayKetThuc()) < Contants.MOC_THOI_HAN) {
            error.put("ngayketThuc", ErrorMessage.MOC_THOI_GIAN_PHIEU_GIAM_GIA);
        }

        if (!error.isEmpty()) {
            throw new DuplicateException(error);
        }
    }

    public PhieuGiamGia convertToPhieuGiamGia(PhieuGiamGia phieuGiamGia, PhieuGiamGiaRequest request) {

        NhanVien nhanVien = nhanVienRepository.findNhanVienByEmail(request.getNguoiTao());
        HangKhachHang hangKhachHang = hangKhachHangRepository.findHangKhachHangByTenHang(request.getDoiTuongApDung());
        phieuGiamGia.setMaGiamGia(request.getMaGiamGia());
        phieuGiamGia.setPhanTramGiam(request.getPhanTramGiam());
        phieuGiamGia.setSoLuongPhieu(request.getSoLuongPhieu());
        phieuGiamGia.setNgayBatDau(request.getNgayBatDau());
        phieuGiamGia.setNgayKetThuc(request.getNgayKetThuc());
        phieuGiamGia.setGiaTriDonToiThieu(request.getGiaTriDonToiThieu());
        phieuGiamGia.setGiaTriGiamToiDa(request.getGiaTriGiamToiDa());
        phieuGiamGia.setDoiTuongApDung(hangKhachHang);
        phieuGiamGia.setNguoiTao(nhanVien);
        phieuGiamGia.setNgayTao(LocalDateTime.now());
        phieuGiamGia.setTrangThai(request.getTrangThai());

        return phieuGiamGia;
    }

}
