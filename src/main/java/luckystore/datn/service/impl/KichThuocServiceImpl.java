package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.KichThuoc;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.KichThuocRequest;
import luckystore.datn.model.response.KichThuocResponse;
import luckystore.datn.repository.KichThuocRepository;
import luckystore.datn.service.KichThuocService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichThuocServiceImpl implements KichThuocService {

    @Autowired
    private KichThuocRepository kichThuocRepo;

    @Override
    public List<KichThuocResponse> getAll() {
        return kichThuocRepo.findAllResponse();
    }

    @Override
    public Page<KichThuocResponse> getPage(int page, String searchText, Integer status) {
        return kichThuocRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public KichThuocResponse addKichThuoc(KichThuocRequest kichThuocRequest) {
        checkWhenInsert(kichThuocRequest);
        KichThuoc kichThuoc = getKichThuoc(new KichThuoc(), kichThuocRequest);
        return new KichThuocResponse(kichThuocRepo.save(kichThuoc));
    }

    @Override
    public KichThuocResponse updateKichThuoc(Long id, KichThuocRequest kichThuocRequest) {
        KichThuoc kichThuoc;
        if (id == null) {
            throw new NullException();
        } else {
            kichThuoc = kichThuocRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(kichThuocRequest);
        kichThuoc = getKichThuoc(kichThuoc, kichThuocRequest);
        return new KichThuocResponse(kichThuocRepo.save(kichThuoc));
    }

    @Override
    public KichThuocResponse findById(Long id) {
        return new KichThuocResponse(kichThuocRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private KichThuoc getKichThuoc(KichThuoc kichThuoc, KichThuocRequest kichThuocRequest) {
        kichThuoc.setTen(kichThuocRequest.getTen());
        kichThuoc.setMoTa(kichThuocRequest.getMoTa());
        kichThuoc.setTrangThai(kichThuocRequest.getTrangThai() == null || kichThuocRequest.getTrangThai() == 0 ? 0 : 1);
        return kichThuoc;
    }

    private void checkWhenInsert(KichThuocRequest kichThuocRequest) {
        if (kichThuocRepo.existsByTen(kichThuocRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(KichThuocRequest kichThuocRequest) {
        if (kichThuocRepo.existsByTenAndIdNot(kichThuocRequest.getTen(), kichThuocRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

