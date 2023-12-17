package luckystore.datn.service.impl;

import luckystore.datn.entity.DanhGia;
import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.model.request.DanhGiaRequest;
import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.model.response.DanhGiaResponse;
import luckystore.datn.repository.DanhGiaRepository;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.service.DanhGiaService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class DanhGiaServiceImpl implements DanhGiaService {

    @Autowired
    DanhGiaRepository danhGiaRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    GiayRepository giayRepository;

    @Override
    public Page<DanhGiaResponse> getPage(int page, Integer star) {
        return danhGiaRepository.getPageResponse(star, PageRequest.of((page - 1), 5));
    }

    @Override
    public void deleteDanhGia(Long id) {
        DanhGia danhGia = danhGiaRepository.findById(id).orElseThrow(() -> new RuntimeException());
        danhGiaRepository.delete(danhGia);
    }

    @Override
    public DanhGiaResponse addDanhGia(DanhGiaRequest danhGiaRequest) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date currentDate = calendar.getTime();
        long milliseconds = currentDate.getTime();
        java.sql.Date ngayHienTai = new java.sql.Date(milliseconds);

        checkWhenInsert(danhGiaRequest);
        DanhGia danhGia = new DanhGia();
        danhGia.setSaoDanhGia(danhGiaRequest.getSaoDanhGia());
        danhGia.setBinhLuan(danhGiaRequest.getBinhLuan());
        danhGia.setTrangThai(1);
        Giay giay =  giayRepository.findById(danhGiaRequest.getIdGiay()).orElseThrow(() -> new RuntimeException());
        KhachHang khachHang = khachHangRepository.findById(danhGiaRequest.getIdKhachHang()).orElseThrow(() -> new RuntimeException());;
        danhGia.setGiay(giay);
        danhGia.setKhachHang(khachHang);
        danhGia.setNgayTao(ngayHienTai);
        danhGia.setThoiGian(ngayHienTai);
        return new DanhGiaResponse(danhGiaRepository.save(danhGia));
    }

    @Override
    public DanhGiaResponse findByIdKhAndIdGiay(Long idKhachHang, Long idGiay) {
        return danhGiaRepository.findByIdKhAndIdGiay(idKhachHang,idGiay);
    }

    @Override
    public List<DanhGiaResponse> getAllByIdKhachHang(Long idKhachHang) {
        return danhGiaRepository.findByIdKhachHang(idKhachHang);
    }

    private void checkWhenInsert(DanhGiaRequest danhGiaRequest) {
        if (danhGiaRepository.existsByKhachHangIdAndGiayId(danhGiaRequest.getIdKhachHang(), danhGiaRequest.getIdGiay())) {
            String errorObject = JsonString.errorToJsonObject("binhLuan", "Bạn chỉ đuọc đánh giá sản phẩm này  một lần");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

}
