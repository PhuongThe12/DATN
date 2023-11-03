package luckystore.datn.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.ChatLieu;
import luckystore.datn.entity.CoGiay;
import luckystore.datn.entity.DayGiay;
import luckystore.datn.entity.DeGiay;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.HashTag;
import luckystore.datn.entity.HashTagChiTiet;
import luckystore.datn.entity.HinhAnh;
import luckystore.datn.entity.KichThuoc;
import luckystore.datn.entity.LotGiay;
import luckystore.datn.entity.MauSac;
import luckystore.datn.entity.MuiGiay;
import luckystore.datn.entity.ThuongHieu;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.model.request.BienTheGiayRequest;
import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.repository.ChatLieuRepository;
import luckystore.datn.repository.CoGiayRepository;
import luckystore.datn.repository.DayGiayRepository;
import luckystore.datn.repository.DeGiayRepository;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.repository.HashTagRepository;
import luckystore.datn.repository.KichThuocRepository;
import luckystore.datn.repository.LotGiayRepository;
import luckystore.datn.repository.MauSacRepository;
import luckystore.datn.repository.MuiGiayRepository;
import luckystore.datn.repository.ThuongHieuRepository;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.util.JsonString;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GiayServiceImpl implements GiayService {

    private final GiayRepository giayRepository;
    private final DayGiayRepository dayGiayRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final CoGiayRepository coGiayRepository;
    private final DeGiayRepository deGiayRepository;
    private final HashTagRepository hashTagRepository;
    private final KichThuocRepository kichThuocRepository;
    private final LotGiayRepository lotGiayRepository;
    private final MauSacRepository mauSacRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final MuiGiayRepository muiGiayRepository;
    private final ImageHubService imageHubService;

    @Override
    public List<GiayResponse> getAllActive() {
        return giayRepository.findAllByTrangThai(1);
    }

    @Override
    public List<GiayResponse> getAllContains(List<Long> ids) {
        return giayRepository.findAllContains(ids);
    }

    @Transactional
    @Override
    public GiayResponse addGiay(GiayRequest giayRequest) {
        if (giayRepository.existsByTen(giayRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
        Giay giay = new Giay();
        getGiay(giay, giayRequest);

        List<HinhAnh> hinhAnhs = new ArrayList<>();
        if (giayRequest.getImage1() != null) {
            String file = imageHubService.base64ToFile(giayRequest.getImage1());
            HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(1).build();
            hinhAnhs.add(hinhAnh);
        }
        if (giayRequest.getImage2() != null) {
            String file = imageHubService.base64ToFile(giayRequest.getImage2());
            HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(2).build();
            hinhAnhs.add(hinhAnh);
        }
        if (giayRequest.getImage3() != null) {
            String file = imageHubService.base64ToFile(giayRequest.getImage3());
            HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(3).build();
            hinhAnhs.add(hinhAnh);
        }
        if (giayRequest.getImage4() != null) {
            String file = imageHubService.base64ToFile(giayRequest.getImage4());
            HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(4).build();
            hinhAnhs.add(hinhAnh);
        }
        if (giayRequest.getImage5() != null) {
            String file = imageHubService.base64ToFile(giayRequest.getImage5());
            HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(5).build();
            hinhAnhs.add(hinhAnh);
        }

        giay.setLstAnh(hinhAnhs);

        Map<Long, String> files = new HashMap<>();
        for (Map.Entry<Long, String> mauSacImage : giayRequest.getMauSacImages().entrySet()) {
            String file = imageHubService.base64ToFile(mauSacImage.getValue());
            files.put(mauSacImage.getKey(), file);
        }

        giay.setLstBienTheGiay(new ArrayList<>());
        for (BienTheGiayRequest bienTheGiayRequest : giayRequest.getBienTheGiays()) {
            MauSac mauSac = mauSacRepository.findById(bienTheGiayRequest.getMauSacId()).orElseThrow(() ->
                    new InvalidIdException(JsonString.stringToJson(
                            JsonString.errorToJsonObject("mauSac", "Không tồn tại màu sắc này"))));

            KichThuoc kichThuoc = kichThuocRepository.findById(bienTheGiayRequest.getKichThuocId()).orElseThrow(() ->
                    new InvalidIdException(JsonString.stringToJson(
                            JsonString.errorToJsonObject("kichThuoc", "Không tồn tại kích thước này"))));

            BienTheGiay bienTheGiay = BienTheGiay.builder()
                    .barCode(bienTheGiayRequest.getBarcode())
                    .giaBan(bienTheGiayRequest.getGiaBan())
                    .giaNhap(bienTheGiayRequest.getGiaNhap())
                    .mauSac(mauSac)
                    .kichThuoc(kichThuoc)
                    .hinhAnh(files.get(mauSac.getId()))
                    .trangThai(bienTheGiayRequest.getTrangThai())
                    .soLuong(bienTheGiayRequest.getSoLuong())
                    .build();

            giay.getLstBienTheGiay().add(bienTheGiay);
        }

        return new GiayResponse(giayRepository.save(giay));

    }

    @Override
    public List<GiayResponse> getAllGiay() {
        return giayRepository.findAllGiay();
    }

    private void getGiay(Giay giay, GiayRequest giayRequest) {

        DayGiay dayGiay = dayGiayRepository.findById(giayRequest.getDayGiayId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("dayGiay", "Không tồn tại dây giày này")))
        );
        giay.setDayGiay(dayGiay);

        LotGiay lotGiay = lotGiayRepository.findById(giayRequest.getLotGiayId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("lotGiay", "Không tồn tại lót giày này")))
        );
        giay.setLotGiay(lotGiay);

        MuiGiay muiGiay = muiGiayRepository.findById(giayRequest.getMuiGiayId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("muiGiay", "Không tồn tại mũi giày này")))
        );
        giay.setMuiGiay(muiGiay);

        CoGiay coGiay = coGiayRepository.findById(giayRequest.getCoGiayId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("coGiay", "Không tồn tại cổ giày này")))
        );
        giay.setCoGiay(coGiay);

        ThuongHieu thuongHieu = thuongHieuRepository.findById(giayRequest.getThuongHieuId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("thuongHieu", "Không tồn tại thương hiệu này")))
        );
        giay.setThuongHieu(thuongHieu);

        ChatLieu chatLieu = chatLieuRepository.findById(giayRequest.getChatLieuId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("chatLieu", "Không tồn tại chất liệu này")))
        );
        giay.setChatLieu(chatLieu);

        DeGiay deGiay = deGiayRepository.findById(giayRequest.getDeGiayId()).orElseThrow(() ->
                new InvalidIdException(JsonString.stringToJson(
                        JsonString.errorToJsonObject("deGiay", "Không tồn tại đế giày này")))
        );
        giay.setDeGiay(deGiay);

        List<HashTag> hashTags = hashTagRepository.findByIdIn(giayRequest.getHashTagIds());
        List<HashTagChiTiet> hashTagChiTiets = hashTags.stream().map(hashTag ->
                HashTagChiTiet.builder().giay(giay).hashTag(hashTag).build()).toList();

        giay.setHashTagChiTiets(hashTagChiTiets);
        giay.setTrangThai(giayRequest.getTrangThai());
        giay.setNamSX(giayRequest.getNamSX());
        giay.setMoTa(giayRequest.getMoTa());
        giay.setTen(giayRequest.getTen());

    }


}
