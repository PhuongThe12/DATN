package luckystore.datn.service.impl;


import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.entity.HashTag;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.HashTagRequest;
import luckystore.datn.model.response.HashTagResponse;
import luckystore.datn.repository.HashTagRepository;
import luckystore.datn.service.HashTagService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashTagServiceImpl implements HashTagService {

    @Autowired
    private HashTagRepository hashTagRepo;

    @Override
    public List<HashTagResponse> getAll() {
        return hashTagRepo.findAllActive();
    }

    @Override
    public Page<HashTagResponse> getPage(int page, String searchText, Integer status) {
        return hashTagRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public HashTagResponse addHashTag(HashTagRequest hashTagRequest) {
        checkWhenInsert(hashTagRequest);
        HashTag hashTag = getHashTag(new HashTag(), hashTagRequest);
        return new HashTagResponse(hashTagRepo.save(hashTag));
    }

    @Override
    public HashTagResponse updateHashTag(Long id, HashTagRequest hashTagRequest) {
        HashTag hashTag;
        if (id == null) {
            throw new NullException();
        } else {
            hashTag = hashTagRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(hashTagRequest);
        hashTag = getHashTag(hashTag, hashTagRequest);
        return new HashTagResponse(hashTagRepo.save(hashTag));
    }

    @Override
    public HashTagResponse findById(Long id) {
        return new HashTagResponse(hashTagRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private HashTag getHashTag(HashTag hashTag, HashTagRequest hashTagRequest) {
        hashTag.setTen(hashTagRequest.getTen());
        hashTag.setMoTa(hashTagRequest.getMoTa());
        hashTag.setTrangThai(hashTagRequest.getTrangThai() == null || hashTagRequest.getTrangThai() == 0 ? 0 : 1);
        return hashTag;
    }

    private void checkWhenInsert(HashTagRequest hashTagRequest) {
        if (hashTagRepo.existsByTen(hashTagRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "HashTag đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(HashTagRequest hashTagRequest) {
        if (hashTagRepo.existsByTenAndIdNot(hashTagRequest.getTen(), hashTagRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

