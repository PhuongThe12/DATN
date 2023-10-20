package luckystore.datn.service;

import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MauSacService {

    List<MauSacResponse> getAll();

    MauSacResponse addMauSac(MauSacRequest mauSacRequest);

    MauSacResponse updateMauSac(Long id, MauSacRequest mauSacRequest);

    MauSacResponse findById(Long id);

}
