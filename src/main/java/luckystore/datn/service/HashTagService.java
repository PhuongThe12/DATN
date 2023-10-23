package luckystore.datn.service;

import luckystore.datn.model.request.HashTagRequest;
import luckystore.datn.model.response.HashTagResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HashTagService {
    List<HashTagResponse> getAll();

    Page<HashTagResponse> getPage(int page, String searchText, Integer status);

    HashTagResponse addHashTag(HashTagRequest hashTagRequest);

    HashTagResponse updateHashTag(Long id, HashTagRequest hashTagRequest);

    HashTagResponse findById(Long id);
}
