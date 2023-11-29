package luckystore.datn.service;

import luckystore.datn.model.response.DonMuaResponse;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDonChiTiet;

import luckystore.datn.model.response.DonMuaResponse;

import luckystore.datn.model.response.HoaDonChiTietResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HoaDonChiTietService {
    Page<DonMuaResponse> getAll(int page, Integer status);

    List<HoaDonChiTietResponse> getAll();

    List<HoaDonChiTietResponse> getAllByIdHoaDon(Long id);

    Page<HoaDonChiTietResponse> getPage(int page, String searchText, Integer status);

    HoaDonChiTietResponse findById(Long id);

    void deleteHoaDonChiTiet(Long idHdct);

    HoaDonChiTiet getHoaDonChiTiet (Long id);
}
