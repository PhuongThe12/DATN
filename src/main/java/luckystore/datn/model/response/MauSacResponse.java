package luckystore.datn.model.response;

//@Projection(types = {VaiTro.class, CoSo.class}) --  có thể join nhiều bảng để lấy field của bảng khác
public interface MauSacResponse {

    Long getId();

    String getTen();

    String getMoTa();
}
