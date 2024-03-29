package luckystore.datn.service;

import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MauSacService {

    List<MauSacResponse> getAll();

    Page<MauSacResponse> getPage(int page, String searchText, Integer status);

    MauSacResponse addMauSac(MauSacRequest mauSacRequest);

    MauSacResponse updateMauSac(Long id, MauSacRequest mauSacRequest);

    MauSacResponse findById(Long id);

}
