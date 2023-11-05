package luckystore.datn.constraints;

import luckystore.datn.infrastructure.constant.Contants;

public class ErrorMessage {

    public static final String NOT_FOUND = "Không tìm thấy";

    public static final String NHAN_VIEN_NOT_EXIST = "Nhân viên không tồn tại";

    public static final String NHAN_VIEN_ALREADY_EXIST = "Nhân viên đã tồn tại";

    public static final String HANG_KHACH_HANG_NOT_EXIST = "Hạng khách hàng không tồn tại";

    public static final String HANG_KHACH_HANG_ALREADY_EXIST = "Hạng khách hàng đã tồn tại";

    public static final String PHIEU_GIAM_GIA_NOT_EXIST = "Phiếu giảm giá không tồn tại";

    public static final String PHIEU_GIAM_GIA_ALREADY_EXIST = "Phiếu giảm giá đã tồn tại";

    public static final String INVALID_NGAY_BAT_DAU = "Ngày bắt đầu phải trước ngày kết thúc";

    public static final String INVALID_NGAY_KET_THUC = "Ngày kết thúc phải sau ngày bắt đầu";

    public static final String MOC_THOI_GIAN_PHIEU_GIAM_GIA = "Thời hạn phiếu giảm giá phải >= "
            + Contants.MOC_THOI_HAN;

    public static final String SO_LUONG_PHIEU_TOI_DA = "Số lượng phiếu tối đa phải <="
            + Contants.SO_LUONG_PHIEU_TOI_DA;

    public static final String SO_LUONG_PHIEU_TOI_THIEU = "Số lượng phiếu tối thiểu phải >="
            + Contants.SO_LUONG_PHIEU_TOI_THIEU;
}
