package luckystore.datn.service;

import luckystore.datn.model.request.ChatLieuRequest;
import luckystore.datn.model.response.ChatLieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatLieuService {
    List<ChatLieuResponse> getAll();

    Page<ChatLieuResponse> getPage(int page, String searchText, Integer status);

    ChatLieuResponse addChatLieu(ChatLieuRequest chatLieuRequest);

    ChatLieuResponse updateChatLieu(Long id, ChatLieuRequest chatLieuRequest);

    ChatLieuResponse findById(Long id);
}
