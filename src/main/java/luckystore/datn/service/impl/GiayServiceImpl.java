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
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.BienTheGiayRequest;
import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.request.GiaySearch;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.ChatLieuRepository;
import luckystore.datn.repository.CoGiayRepository;
import luckystore.datn.repository.DayGiayRepository;
import luckystore.datn.repository.DeGiayRepository;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.repository.HashTagRepository;
import luckystore.datn.repository.HinhAnhRepository;
import luckystore.datn.repository.KichThuocRepository;
import luckystore.datn.repository.LotGiayRepository;
import luckystore.datn.repository.MauSacRepository;
import luckystore.datn.repository.MuiGiayRepository;
import luckystore.datn.repository.ThuongHieuRepository;
import luckystore.datn.service.GiayService;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.util.JsonString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final BienTheGiayRepository bienTheGiayRepository;
    private final HinhAnhRepository hinhAnhRepository;

    @Override
    public Page<GiayResponse> getAllActive(GiaySearch giaySearch) {
        Pageable pageable = PageRequest.of(giaySearch.getCurrentPage() - 1, giaySearch.getPageSize());
        return giayRepository.findPageForList(giaySearch, pageable);
    }

    @Override
    public List<GiayResponse> getAllContains(List<Long> ids) {
        List<GiayResponse> giayResponses = giayRepository.findAllContains(ids);

        Map<Long, GiayResponse> result = new HashMap<>();
        for (GiayResponse giayResponse : giayResponses) {
            if (result.containsKey(giayResponse.getId()) && !giayResponse.getLstBienTheGiay().isEmpty()) {
                GiayResponse giay = result.get(giayResponse.getId());
                giay.getLstBienTheGiay().add(giay.getLstBienTheGiay().get(0));
            } else {
                result.put(giayResponse.getId(), giayResponse);
            }
        }

        return new ArrayList<>(result.values());
    }

    @Transactional
    @Override
    public GiayResponse addGiay(GiayRequest giayRequest) {

        Giay giay = new Giay();
        getGiay(giay, giayRequest);

        checkForInsert(giayRequest);

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
                    .giay(giay)
                    .giaBan(bienTheGiayRequest.getGiaBan())
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
    public Page<GiayResponse> findAllForList(GiaySearch giaySearch) {
        Pageable pageable = PageRequest.of(giaySearch.getCurrentPage() - 1, giaySearch.getPageSize());
//
//        Page<GiayResponse> giayResponsePage = giayRepository.findPageForList(giaySearch, pageable);
//        giayResponsePage.stream().forEach(giayResponse -> {
//            giayResponse.setLstBienTheGiay(bienTheGiayRepository.getSimpleByIdGiay(giayResponse.getId()));
//            giayResponse.getLstAnh().add(imageHubService.getBase64FromFile(hinhAnhRepository.findThubmailByIdGiay(giayResponse.getId())));
//        });
        return giayRepository.findPageForList(giaySearch, pageable);
    }

    @Override
    public Page<GiayResponse> getPage() {
        return giayRepository.findAllByTrangThai(PageRequest.of(0, 5));
    }

    @Override
    @Transactional
    public GiayResponse updateSoLuong(GiayRequest giayRequest) {
        Giay giay = giayRepository.findById(giayRequest.getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(
                JsonString.errorToJsonObject("giay", "Không tồn tại giày này"))));

        giay.getLstBienTheGiay().forEach(bienTheGiay -> {
            giayRequest.getBienTheGiays().forEach(bienTheRequest -> {
                if (Objects.equals(bienTheRequest.getId(), bienTheGiay.getId())) {
                    bienTheGiay.setSoLuong(bienTheRequest.getSoLuong());
                }
            });
        });

        return new GiayResponse(giayRepository.save(giay));

    }

    @Override
    public GiayResponse updateGia(GiayRequest giayRequest) {

        Giay giay = giayRepository.findById(giayRequest.getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(
                JsonString.errorToJsonObject("giay", "Không tồn tại giày này"))));

        giay.getLstBienTheGiay().forEach(bienTheGiay -> {
            giayRequest.getBienTheGiays().forEach(bienTheRequest -> {
                if (Objects.equals(bienTheRequest.getId(), bienTheGiay.getId())) {
                    bienTheGiay.setGiaBan(bienTheRequest.getGiaBan());
                }
            });
        });

        return new GiayResponse(giayRepository.save(giay));
    }

    @Override
    public GiayResponse updateGiay(Long id, GiayRequest giayRequest) {

        Giay giay = giayRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy"));

        getGiay(giay, giayRequest);

        List<String> errors = new ArrayList<>();
        List<String> barCodes = new ArrayList<>();
        if (giayRepository.existsByTenAndIdNot(giayRequest.getTen(), id)) {
            errors.add("ten: Tên đã tồn tại");
        }

        List<HinhAnh> hinhAnhs = giay.getLstAnh();
//        Set<String> removeFiles = new HashSet<>();  // rollback nên không cần xóa hình ảnh
        if (giayRequest.getImage1() == null) {
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 1) {
                    hinhAnhs.remove(i);
                    break;
                }
            }
        } else if (!Objects.equals(giayRequest.getImage1(), "1")) {
            boolean finded = false;
            String file = imageHubService.base64ToFile(giayRequest.getImage1());
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 1) {
                    hinhAnhs.get(i).setLink(file);
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                hinhAnhs.add(HinhAnh.builder().giay(giay).link(file).uuTien(1).build());
            }
        }

        if (giayRequest.getImage2() == null) {
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 2) {
                    hinhAnhs.remove(i);
                    break;
                }
            }
        } else if (!Objects.equals(giayRequest.getImage2(), "1")) {
            boolean finded = false;
            String file = imageHubService.base64ToFile(giayRequest.getImage2());
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 2) {
//                    removeFiles.add(hinhAnhs.get(i).getLink());
//                    hinhAnhs.add(i, HinhAnh.builder().giay(giay).link(file).uuTien(2).build());
                    hinhAnhs.get(i).setLink(file);
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                hinhAnhs.add(HinhAnh.builder().giay(giay).link(file).uuTien(2).build());
            }
        }

        if (giayRequest.getImage3() == null) {
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 3) {
                    hinhAnhs.remove(i);
                    break;
                }
            }
        } else if (!Objects.equals(giayRequest.getImage3(), "1")) {
            boolean finded = false;
            String file = imageHubService.base64ToFile(giayRequest.getImage3());
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 3) {
//                    removeFiles.add(hinhAnhs.get(i).getLink());
//                    hinhAnhs.add(i, HinhAnh.builder().giay(giay).link(file).uuTien(3).build());
                    hinhAnhs.get(i).setLink(file);
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                hinhAnhs.add(HinhAnh.builder().giay(giay).link(file).uuTien(3).build());
            }
        }
        if (giayRequest.getImage4() == null) {
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 4) {
                    hinhAnhs.remove(i);
                    break;
                }
            }
        } else if (!Objects.equals(giayRequest.getImage4(), "1")) {
            boolean finded = false;
            String file = imageHubService.base64ToFile(giayRequest.getImage4());
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 4) {
//                    removeFiles.add(hinhAnhs.get(i).getLink());
                    hinhAnhs.get(i).setLink(file);
//                    hinhAnhs.add(i, HinhAnh.builder().giay(giay).link(file).uuTien(4).build());
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                hinhAnhs.add(HinhAnh.builder().giay(giay).link(file).uuTien(4).build());
            }
        }
        if (giayRequest.getImage5() == null) {
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 5) {
                    hinhAnhs.remove(i);
                    break;
                }
            }
        } else if (!Objects.equals(giayRequest.getImage5(), "1")) {
            String file = imageHubService.base64ToFile(giayRequest.getImage5());
            boolean finded = false;
            for (int i = 0; i < hinhAnhs.size(); i++) {
                if (hinhAnhs.get(i).getUuTien() == 5) {
//                    removeFiles.add(hinhAnhs.get(i).getLink());
                    hinhAnhs.add(HinhAnh.builder().giay(giay).link(file).uuTien(5).build());
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                hinhAnhs.add(HinhAnh.builder().giay(giay).link(file).uuTien(5).build());
            }
        }

        giay.setLstAnh(hinhAnhs);

        Map<Long, String> files = new HashMap<>();
        for (Map.Entry<Long, String> mauSacImage : giayRequest.getMauSacImages().entrySet()) {
            if (!Objects.equals(mauSacImage.getValue(), "1") && mauSacImage.getValue() != null) {
                String file = imageHubService.base64ToFile(mauSacImage.getValue());
                files.put(mauSacImage.getKey(), file);
            }
        }


        for (BienTheGiayRequest bienTheGiayRequest : giayRequest.getBienTheGiays()) {
            boolean exists = false;

            for (BienTheGiay bienThe : giay.getLstBienTheGiay()) {
                if (Objects.equals(bienThe.getMauSac().getId(), bienTheGiayRequest.getMauSacId())
                        && Objects.equals(bienThe.getKichThuoc().getId(), bienTheGiayRequest.getKichThuocId())) {

                    if (bienTheGiayRepository.getBienTheGiayByBarCodeUpdate(bienTheGiayRequest.getBarcode(), bienThe.getId())) {
                        errors.add(bienThe.getMauSac().getId() + ", "
                                + bienThe.getKichThuoc().getId() + ": Barcode đã tồn tại");
                    }
                    bienThe.setGiaBan(bienTheGiayRequest.getGiaBan());
                    bienThe.setSoLuong(bienTheGiayRequest.getSoLuong());
                    bienThe.setTrangThai(bienTheGiayRequest.getTrangThai());
                    bienThe.setBarCode(bienTheGiayRequest.getBarcode());

                    bienThe.setHinhAnh(files.getOrDefault(bienThe.getMauSac().getId(), null));
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                barCodes.add(bienTheGiayRequest.getBarcode());
                MauSac mauSac = mauSacRepository.findById(bienTheGiayRequest.getMauSacId()).orElseThrow(() ->
                        new InvalidIdException(JsonString.stringToJson(
                                JsonString.errorToJsonObject("mauSac", "Không tồn tại màu sắc này"))));

                KichThuoc kichThuoc = kichThuocRepository.findById(bienTheGiayRequest.getKichThuocId()).orElseThrow(() ->
                        new InvalidIdException(JsonString.stringToJson(
                                JsonString.errorToJsonObject("kichThuoc", "Không tồn tại kích thước này"))));


                BienTheGiay bienTheGiay = BienTheGiay.builder()
                        .barCode(bienTheGiayRequest.getBarcode())
                        .giay(giay)
                        .giaBan(bienTheGiayRequest.getGiaBan())
                        .mauSac(mauSac)
                        .kichThuoc(kichThuoc)
                        .trangThai(bienTheGiayRequest.getTrangThai())
                        .soLuong(bienTheGiayRequest.getSoLuong())
                        .build();

                bienTheGiay.setHinhAnh(files.getOrDefault(bienTheGiay.getMauSac().getId(), null));

                giay.getLstBienTheGiay().add(bienTheGiay);
            }
        }

        checkForUpdate(barCodes, errors);
//        imageHubService.deleteFile(removeFiles); // xóa ảnh

        return new GiayResponse(giayRepository.save(giay));
    }

    @Override
    public List<GiayResponse> findAllBySearch(GiaySearch giaySearch) {
        return giayRepository.findAllBySearch(giaySearch);
    }

    @Override
    public GiayResponse getResponseById(Long id) {
        GiayResponse giayResponse = giayRepository.findResponseById(id);
        if (giayResponse == null) {
            throw new NotFoundException("Không tìm thấy giày này");
        }
        Map<Long, String> mauSacImages = new HashMap<>();

        giayResponse.getLstBienTheGiay().forEach(bienThe -> {
            if (!mauSacImages.containsKey(bienThe.getMauSac().getId())) {
                mauSacImages.put(bienThe.getMauSac().getId(), imageHubService.getBase64FromFile(bienThe.getHinhAnh()));
            }
        });
        giayResponse.setMauSacImages(mauSacImages);

        return giayResponse;
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
                HashTagChiTiet.builder().giay(giay).hashTag(hashTag).build()).collect(Collectors.toList());

        giay.setHashTagChiTiets(hashTagChiTiets);
        giay.setTrangThai(giayRequest.getTrangThai());
        giay.setNamSX(giayRequest.getNamSX());
        giay.setMoTa(giayRequest.getMoTa());
        giay.setTen(giayRequest.getTen());

    }

    private void checkForInsert(GiayRequest giayRequest) {
        List<String> errors = new ArrayList<>();

        List<String> lstBarcode = giayRequest.getBienTheGiays().stream().map(BienTheGiayRequest::getBarcode).toList();
        if (giayRepository.existsByTen(giayRequest.getTen())) {
            errors.add("ten: Tên đã tồn tại");
        }

        List<BienTheGiayResponse> lstBienTheBarcode = bienTheGiayRepository.getBienTheGiayByListBarCode(lstBarcode);

        for (BienTheGiayResponse bienTheGiayResponse : lstBienTheBarcode) {
            errors.add(bienTheGiayResponse.getMauSac().getId() + ", "
                    + bienTheGiayResponse.getKichThuoc().getId() + ": Barcode đã tồn tại");
        }


        if (!errors.isEmpty()) {
            throw new ConflictException(errors);
        }

    }

    private void checkForUpdate(List<String> lstBarcode, List<String> errors) {

        List<BienTheGiayResponse> lstBienTheBarcode = bienTheGiayRepository.getBienTheGiayByListBarCode(lstBarcode);

        for (BienTheGiayResponse bienTheGiayResponse : lstBienTheBarcode) {
            errors.add(bienTheGiayResponse.getMauSac().getId() + ", "
                    + bienTheGiayResponse.getKichThuoc().getId() + ": Barcode đã tồn tại");
        }


        if (!errors.isEmpty()) {
            throw new ConflictException(errors);
        }
    }

}
