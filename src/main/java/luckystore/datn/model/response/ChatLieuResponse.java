package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.ChatLieu;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatLieuResponse {

    private Long id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public ChatLieuResponse(ChatLieu chatLieu) {
        if (chatLieu != null) {
            this.id = chatLieu.getId();
            this.ten = chatLieu.getTen();
            this.moTa = chatLieu.getMoTa();
            this.trangThai = chatLieu.getTrangThai();
        }
    }

    public ChatLieuResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

}
