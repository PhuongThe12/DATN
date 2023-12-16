package luckystore.datn.service.impl;

import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.exception.RestApiException;
import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.infrastructure.security.session.SessionService;
import luckystore.datn.model.request.FindPhieuGiamGiaRequest;
import luckystore.datn.model.request.PhieuGiamGiaRequest;
import luckystore.datn.model.response.PhieuGiamGiaResponse;
import luckystore.datn.repository.HangKhachHangRepository;
import luckystore.datn.repository.NhanVienRepository;
import luckystore.datn.repository.PhieuGiamGiaRepository;
import luckystore.datn.service.PhieuGiamGiaService;
import luckystore.datn.util.ConvertDateToLong;
import luckystore.datn.util.JsonString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuGiamGiaServiceimpl implements PhieuGiamGiaService {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;

    private final HangKhachHangRepository hangKhachHangRepository;

    private final SessionService sessionService;

    @Override
    public List<PhieuGiamGiaResponse> getAll() {
        return phieuGiamGiaRepository.getAll();
    }

    @Override
    public Page<PhieuGiamGiaResponse> getpage(int page, String searchText, Integer status) {
        return phieuGiamGiaRepository.getPage(PageRequest.of(page - 1, 5), searchText, status);
    }

    @Override
    public PhieuGiamGiaResponse getPhieuResponseById(Long id) {
        PhieuGiamGiaResponse phieuGiamGiaResponse;
        if (id == null) {
            throw new NullException();
        } else {
            phieuGiamGiaResponse = phieuGiamGiaRepository.getPhieuResponse(id).orElseThrow(() ->
                    new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        return phieuGiamGiaResponse;
    }

    @Override
    public PhieuGiamGia addPhieuGiamGia(PhieuGiamGiaRequest request) {

        checkWhenAdd(request);
        PhieuGiamGia phieuGiamGia = getPhieuGiamGia(new PhieuGiamGia(), request);
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    @Override
    public PhieuGiamGia updatePhieuGiamGia(Long id, PhieuGiamGiaRequest request) {

        PhieuGiamGia phieuGiamGia;
        if (id == null) {
            throw new NullException();
        } else {
            phieuGiamGia = phieuGiamGiaRepository.findById(id).orElseThrow(() ->
                    new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        checkWhenUpdate(request);
        phieuGiamGia = getPhieuGiamGia(phieuGiamGia, request);
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    @Override
    public Page<PhieuGiamGiaResponse> getListSearchPhieu(FindPhieuGiamGiaRequest request) {
        return phieuGiamGiaRepository.getListSearchPhieu(PageRequest.of(request.getCurrentPage() -1,
                request.getSize()),request);
    }

    @Override
    public List<PhieuGiamGiaResponse> getListPhieuByHangKhachHang(String hangKhachHang) {
        return phieuGiamGiaRepository.getListPhieuByHangKhachHang(hangKhachHang);
    }

    private PhieuGiamGia getPhieuGiamGia(PhieuGiamGia phieuGiamGia, PhieuGiamGiaRequest request) {

        Optional<HangKhachHang> hangKhachHangCheck = hangKhachHangRepository.getHangKhachHangByTenHang(request.getHangKhachHang());
        LocalDateTime currentTimes = LocalDateTime.now();
        phieuGiamGia.setMaGiamGia(request.getMaGiamGia());
        phieuGiamGia.setPhanTramGiam(request.getPhanTramGiam());
        phieuGiamGia.setSoLuongPhieu(request.getSoLuongPhieu());
        phieuGiamGia.setNgayBatDau(request.getNgayBatDau());
        phieuGiamGia.setNgayKetThuc(request.getNgayKetThuc());
        phieuGiamGia.setGiaTriDonToiThieu(request.getGiaTriDonToiThieu());
        phieuGiamGia.setGiaTriGiamToiDa(request.getGiaTriGiamToiDa());
        if (hangKhachHangCheck.isPresent()) {
            phieuGiamGia.setDoiTuongApDung(hangKhachHangCheck.get());
        }

        Instant currentInstant = Instant.ofEpochMilli(System.currentTimeMillis());
        Instant ngayBdInstant = Instant.ofEpochMilli(request.getNgayBatDau());
        Instant ngayKtInstant = Instant.ofEpochMilli(request.getNgayKetThuc());

        LocalDateTime localDateTimeCurr = LocalDateTime.ofInstant(currentInstant, ZoneId.of("UTC"));
        LocalDateTime localDateTimeNgayBatDau = LocalDateTime.ofInstant(ngayBdInstant, ZoneId.of("UTC"));
        LocalDateTime localDateTimeNgayKetThuc = LocalDateTime.ofInstant(ngayKtInstant, ZoneId.of("UTC"));

        if (localDateTimeNgayBatDau.isBefore(localDateTimeCurr)) {
            throw new RestApiException(JsonString.stringToJson(JsonString.errorToJsonObject
                    ("ngayBatDau", "Ngày bắt đầu không được là ngày trong quá khứ!")));
        }

        if (request.getSoLuongPhieu() == 0) {

        }

        if (localDateTimeCurr.isBefore(localDateTimeNgayKetThuc)) {
            phieuGiamGia.setTrangThai(0);
        } else {
            phieuGiamGia.setTrangThai(1);
        }
        phieuGiamGia.setNgayTao(currentTimes);
        phieuGiamGia.setNguoiTao(sessionService.getAdmintrator());
        return phieuGiamGia;
    }

    private void checkWhenAdd(PhieuGiamGiaRequest request) {
        Boolean phieuGiamGiacheck = phieuGiamGiaRepository.existsByMaGiamGia(request.getMaGiamGia());
        if (phieuGiamGiacheck) {
            String errorObject = JsonString.errorToJsonObject("maGiamGia", "Phiếu giảm giá đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(PhieuGiamGiaRequest request) {
        Boolean phieuGiamGiacheck = phieuGiamGiaRepository.existsByMaGiamGiaAndIdNot(request.getMaGiamGia(), request.getId());
        if (phieuGiamGiacheck) {
            String errorObject = JsonString.errorToJsonObject("maGiamGia", "Phiếu giảm giá đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

}
