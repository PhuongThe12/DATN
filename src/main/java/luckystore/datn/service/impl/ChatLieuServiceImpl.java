package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.ChatLieu;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.ChatLieuRequest;
import luckystore.datn.model.response.ChatLieuResponse;
import luckystore.datn.repository.ChatLieuRepository;
import luckystore.datn.service.ChatLieuService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLieuServiceImpl implements ChatLieuService {
    @Autowired
    private ChatLieuRepository chatLieuRepo;

    @Override
    public List<ChatLieuResponse> getAll() {
        return chatLieuRepo.findAllActive();
    }

    @Override
    public Page<ChatLieuResponse> getPage(int page, String searchText, Integer status) {
        return chatLieuRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public ChatLieuResponse addChatLieu(ChatLieuRequest chatLieuRequest) {
        checkWhenInsert(chatLieuRequest);
        ChatLieu chatLieu = getChatLieu(new ChatLieu(), chatLieuRequest);
        return new ChatLieuResponse(chatLieuRepo.save(chatLieu));
    }

    @Override
    public ChatLieuResponse updateChatLieu(Long id, ChatLieuRequest chatLieuRequest) {
        ChatLieu chatLieu;
        if (id == null) {
            throw new NullException();
        } else {
            chatLieu = chatLieuRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(chatLieuRequest);
        chatLieu = getChatLieu(chatLieu, chatLieuRequest);
        return new ChatLieuResponse(chatLieuRepo.save(chatLieu));
    }

    @Override
    public ChatLieuResponse findById(Long id) {
        return new ChatLieuResponse(chatLieuRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private ChatLieu getChatLieu(ChatLieu chatLieu, ChatLieuRequest chatLieuRequest) {
        chatLieu.setTen(chatLieuRequest.getTen());
        chatLieu.setMoTa(chatLieuRequest.getMoTa());
        chatLieu.setTrangThai(chatLieuRequest.getTrangThai() == null || chatLieuRequest.getTrangThai() == 0 ? 0 : 1);
        return chatLieu;
    }

    private void checkWhenInsert(ChatLieuRequest chatLieuRequest) {
        if (chatLieuRepo.existsByTen(chatLieuRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(ChatLieuRequest chatLieuRequest) {
        if (chatLieuRepo.existsByTenAndIdNot(chatLieuRequest.getTen(), chatLieuRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}
