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
import luckystore.datn.exception.ExcelException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.BienTheGiayRequest;
import luckystore.datn.model.request.GiayExcelRequest;
import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.request.GiaySearch;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.ChatLieuResponse;
import luckystore.datn.model.response.CoGiayResponse;
import luckystore.datn.model.response.DayGiayResponse;
import luckystore.datn.model.response.DeGiayResponse;
import luckystore.datn.model.response.ExcelError;
import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.model.response.HashTagResponse;
import luckystore.datn.model.response.KhuyenMaiChiTietResponse;
import luckystore.datn.model.response.KichThuocResponse;
import luckystore.datn.model.response.LotGiayResponse;
import luckystore.datn.model.response.MauSacResponse;
import luckystore.datn.model.response.MuiGiayResponse;
import luckystore.datn.model.response.ThuongHieuResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.ChatLieuRepository;
import luckystore.datn.repository.CoGiayRepository;
import luckystore.datn.repository.DayGiayRepository;
import luckystore.datn.repository.DeGiayRepository;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.repository.HashTagRepository;
import luckystore.datn.repository.HinhAnhRepository;
import luckystore.datn.repository.KhuyenMaiChiTietRepository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
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
    private final KhuyenMaiChiTietRepository khuyenMaiChiTietRepository;

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
                giay.getLstBienTheGiay().add(giayResponse.getLstBienTheGiay().get(0));
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
            MauSac mauSac = mauSacRepository.findById(bienTheGiayRequest.getMauSacId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("mauSac", "Không tồn tại màu sắc này"))));

            KichThuoc kichThuoc = kichThuocRepository.findById(bienTheGiayRequest.getKichThuocId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("kichThuoc", "Không tồn tại kích thước này"))));

            BienTheGiay bienTheGiay = BienTheGiay.builder().barCode(bienTheGiayRequest.getBarcode()).giay(giay).giaBan(bienTheGiayRequest.getGiaBan()).mauSac(mauSac).kichThuoc(kichThuoc).hinhAnh(files.get(mauSac.getId())).trangThai(bienTheGiayRequest.getTrangThai()).soLuong(bienTheGiayRequest.getSoLuong()).build();

            giay.getLstBienTheGiay().add(bienTheGiay);
        }

        return new GiayResponse(giayRepository.save(giay));

    }

    @Override
    public Page<GiayResponse> findAllForList(GiaySearch giaySearch) {
        Pageable pageable = PageRequest.of(giaySearch.getCurrentPage() - 1, giaySearch.getPageSize());
//        Page<GiayResponse> giayResponsePage = giayRepository.findGiayBySearchForList(giaySearch, pageable);
//
//        List<Long> lstId = giayResponsePage.getContent().stream()
//                .map(GiayResponse::getId).toList();
//
//        List<GiayResponse> giayResponses = giayRepository.findListByInList(lstId);
//
//        System.out.println(giayResponses.size() + ": sizie");
//        Map<Long, GiayResponse> giayResponseMap = new HashMap<>();
//
//        for (GiayResponse giayResponse : giayResponses) {
//            GiayResponse giay = giayResponseMap.get(giayResponse.getId());
//            if (giay == null && !giayResponse.getLstBienTheGiay().isEmpty()) {
//                giayResponseMap.put(giayResponse.getId(), giayResponse);
//            } else if (!giayResponse.getLstBienTheGiay().isEmpty()) {
//                giay.getLstBienTheGiay().add(giayResponse.getLstBienTheGiay().get(0));
//            }
//        }

//        return new PageImpl<>(new ArrayList<>(giayResponseMap.values()), pageable, giayResponsePage.getTotalElements());
        return giayRepository.findPageForList(giaySearch, pageable);

    }

    @Override
    public Page<GiayResponse> getPage() {
        return giayRepository.findAllByTrangThai(PageRequest.of(0, 5));
    }

    @Override
    @Transactional
    public GiayResponse updateSoLuong(GiayRequest giayRequest) {
        Giay giay = giayRepository.findById(giayRequest.getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("giay", "Không tồn tại giày này"))));

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

        Giay giay = giayRepository.findById(giayRequest.getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("giay", "Không tồn tại giày này"))));

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
        Set<String> removeFiles = new HashSet<>();  // rollback nên không cần xóa hình ảnh
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
                    removeFiles.add(hinhAnhs.get(i).getLink());
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
                    removeFiles.add(hinhAnhs.get(i).getLink());
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
                    removeFiles.add(hinhAnhs.get(i).getLink());
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
                    removeFiles.add(hinhAnhs.get(i).getLink());
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
                if (Objects.equals(bienThe.getMauSac().getId(), bienTheGiayRequest.getMauSacId()) && Objects.equals(bienThe.getKichThuoc().getId(), bienTheGiayRequest.getKichThuocId())) {

                    if (bienTheGiayRepository.getBienTheGiayByBarCodeUpdate(bienTheGiayRequest.getBarcode(), bienThe.getId())) {
                        errors.add(bienThe.getMauSac().getId() + ", " + bienThe.getKichThuoc().getId() + ": Barcode đã tồn tại");
                    }
                    bienThe.setGiaBan(bienTheGiayRequest.getGiaBan());
                    bienThe.setSoLuong(bienTheGiayRequest.getSoLuong());
                    bienThe.setTrangThai(bienTheGiayRequest.getTrangThai());
                    bienThe.setBarCode(bienTheGiayRequest.getBarcode());
                    bienThe.setSoLuongLoi(bienTheGiayRequest.getSoLuongLoi());

                    bienThe.setHinhAnh(files.getOrDefault(bienThe.getMauSac().getId(), null));
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                barCodes.add(bienTheGiayRequest.getBarcode());
                MauSac mauSac = mauSacRepository.findById(bienTheGiayRequest.getMauSacId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("mauSac", "Không tồn tại màu sắc này"))));

                KichThuoc kichThuoc = kichThuocRepository.findById(bienTheGiayRequest.getKichThuocId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("kichThuoc", "Không tồn tại kích thước này"))));


                BienTheGiay bienTheGiay = BienTheGiay.builder().barCode(bienTheGiayRequest.getBarcode()).giay(giay).giaBan(bienTheGiayRequest.getGiaBan()).mauSac(mauSac).kichThuoc(kichThuoc).trangThai(bienTheGiayRequest.getTrangThai()).soLuong(bienTheGiayRequest.getSoLuong()).soLuongLoi(bienTheGiayRequest.getSoLuongLoi()).build();
                bienTheGiay.setHinhAnh(files.getOrDefault(bienTheGiay.getMauSac().getId(), null));

                giay.getLstBienTheGiay().add(bienTheGiay);
            }
        }

        checkForUpdate(barCodes, errors);
        imageHubService.deleteFile(removeFiles); // xóa ảnh

        return new GiayResponse(giayRepository.save(giay));
    }

    @Override
    public Page<GiayResponse> findAllBySearch(GiaySearch giaySearch) {
        Pageable pageable = PageRequest.of(giaySearch.getCurrentPage() - 1, giaySearch.getPageSize());
        return giayRepository.findAllBySearch(giaySearch,pageable);
    }

    @Transactional
    @Override
    public void addExcel(List<GiayExcelRequest> giayExcelRequests) {
        List<ExcelError> errors = new ArrayList<>();

        Set<String> tenGiays = new HashSet<>();
        Set<String> tenChatLieus = new HashSet<>();
        Set<String> tenCoGiays = new HashSet<>();
        Set<String> tenDayGiays = new HashSet<>();
        Set<String> tenDeGiays = new HashSet<>();
        Set<String> tenKichThuocs = new HashSet<>();
        Set<String> tenLotGiays = new HashSet<>();
        Set<String> tenMuiGiays = new HashSet<>();
        Set<String> tenMauSacs = new HashSet<>();
        Set<String> tenThuongHieus = new HashSet<>();
        AtomicReference<Set<String>> tenHashTags = new AtomicReference<>(new HashSet<>());
        Map<String, String> lstBarcodeMap = new HashMap<>();

        giayExcelRequests.forEach(request -> {
            tenGiays.add(request.getTen());
            tenChatLieus.add(request.getChatLieu());
            tenCoGiays.add(request.getCoGiay());
            tenDeGiays.add(request.getDeGiay());
            tenDayGiays.add(request.getDayGiay());
            tenLotGiays.add(request.getLotGiay());
            tenMuiGiays.add(request.getMuiGiay());
            tenThuongHieus.add(request.getThuongHieu());
            tenHashTags.set(request.getHashTags());

            request.getBienTheGiays().forEach(bienThe -> {
                tenMauSacs.add(bienThe.getMauSac());
                tenKichThuocs.add(bienThe.getKichThuoc());
                if (lstBarcodeMap.containsKey(bienThe.getBarcode())) {
                    errors.add(new ExcelError(bienThe.getRow(), bienThe.getColumn(), "Barcode không được trùng"));
                } else {
                    lstBarcodeMap.put(bienThe.getBarcode(), bienThe.getBarcode());
                }
            });
        });

        List<BienTheGiayResponse> lstBienTheBarcode = bienTheGiayRepository.getBienTheGiayByListBarCode(new ArrayList<>(lstBarcodeMap.values()));
        if (!lstBienTheBarcode.isEmpty()) {
            lstBienTheBarcode.forEach(bt -> {
                giayExcelRequests.forEach(request -> {
                    request.getBienTheGiays().forEach(bienThe -> {
                        if (lstBarcodeMap.containsKey(bt.getBarCode())
                                && bienThe.getKichThuoc().equals(bt.getKichThuoc().getTen())
                                && bienThe.getMauSac().equals(bt.getMauSac().getTen())
                        ) {
                            errors.add(new ExcelError(bienThe.getRow(), bienThe.getColumn(), "Barcode không được trùng"));
                        }
                    });
                });
            });
        }

        if (!errors.isEmpty()) {
            throw new ExcelException(errors);
        }

        Map<String, GiayResponse> giayIds = giayRepository.getIdsByName(tenGiays).stream().collect(Collectors.toMap(GiayResponse::getTen, giay -> giay));
        Map<String, ChatLieuResponse> chatLieuIds = chatLieuRepository.getIdsByName(tenChatLieus).stream().collect(Collectors.toMap(ChatLieuResponse::getTen, giay -> giay));
        Map<String, CoGiayResponse> coGiayIds = coGiayRepository.getIdsByName(tenCoGiays).stream().collect(Collectors.toMap(CoGiayResponse::getTen, giay -> giay));
        Map<String, DeGiayResponse> deGiayIds = deGiayRepository.getIdsByName(tenDeGiays).stream().collect(Collectors.toMap(DeGiayResponse::getTen, giay -> giay));
        Map<String, DayGiayResponse> dayGiayIds = dayGiayRepository.getIdsByName(tenDayGiays).stream().collect(Collectors.toMap(DayGiayResponse::getTen, giay -> giay));
        Map<String, HashTagResponse> hashTagIds = hashTagRepository.getIdsByName(tenHashTags.get()).stream().collect(Collectors.toMap(HashTagResponse::getTen, giay -> giay));
        Map<String, LotGiayResponse> lotGiayIds = lotGiayRepository.getIdsByName(tenLotGiays).stream().collect(Collectors.toMap(LotGiayResponse::getTen, giay -> giay));
        Map<String, MuiGiayResponse> muiGiayIds = muiGiayRepository.getIdsByName(tenMuiGiays).stream().collect(Collectors.toMap(MuiGiayResponse::getTen, giay -> giay));
        Map<String, ThuongHieuResponse> thuongHieuIds = thuongHieuRepository.getIdsByName(tenThuongHieus).stream().collect(Collectors.toMap(ThuongHieuResponse::getTen, giay -> giay));
        Map<String, MauSacResponse> mauSacIds = mauSacRepository.getIdsByName(tenMauSacs).stream().collect(Collectors.toMap(MauSacResponse::getTen, giay -> giay));
        Map<String, KichThuocResponse> kichThuocIds = kichThuocRepository.getIdsByName(tenKichThuocs).stream().collect(Collectors.toMap(KichThuocResponse::getTen, giay -> giay));

        List<Giay> giays = new ArrayList<>();

        giayExcelRequests.forEach(request -> {
            Giay giay = new Giay();
            if (giayIds.containsKey(request.getTen())) {
                errors.add(new ExcelError(request.getRow(), 0, "Tên đã tồn tại"));
            } else if (request.getTen().isBlank()) {
                errors.add(new ExcelError(request.getRow(), 0, "Tên không được trống"));
            } else if (request.getTen().length() > 120) {
                errors.add(new ExcelError(request.getRow(), 0, "Tên không được quá 120 ký tự"));
            }
            giay.setTen(request.getTen());

            if (lotGiayIds.containsKey(request.getLotGiay())) {
                LotGiay lotGiay = LotGiay.builder().id(lotGiayIds.get(request.getLotGiay()).getId()).build();
                giay.setLotGiay(lotGiay);
            } else {
                errors.add(new ExcelError(request.getRow(), 6, "Lót giày không tồn tại"));
            }

            if (muiGiayIds.containsKey(request.getMuiGiay())) {
                MuiGiay muiGiay = MuiGiay.builder().id(muiGiayIds.get(request.getMuiGiay()).getId()).build();
                giay.setMuiGiay(muiGiay);
            } else {
                errors.add(new ExcelError(request.getRow(), 7, "Mũi giày không tồn tại"));
            }

            if (coGiayIds.containsKey(request.getCoGiay())) {
                CoGiay coGiay = CoGiay.builder().id(coGiayIds.get(request.getCoGiay()).getId()).build();
                giay.setCoGiay(coGiay);
            } else {
                errors.add(new ExcelError(request.getRow(), 8, "Cổ giày không tồn tại"));
            }

            if (thuongHieuIds.containsKey(request.getThuongHieu())) {
                ThuongHieu thuongHieu = ThuongHieu.builder().id(thuongHieuIds.get(request.getThuongHieu()).getId()).build();
                giay.setThuongHieu(thuongHieu);
            } else {
                errors.add(new ExcelError(request.getRow(), 9, "Thương hiệu không tồn tại"));
            }

            if (chatLieuIds.containsKey(request.getChatLieu())) {
                ChatLieu chatLieu = ChatLieu.builder().id(chatLieuIds.get(request.getChatLieu()).getId()).build();
                giay.setChatLieu(chatLieu);
            } else {
                errors.add(new ExcelError(request.getRow(), 10, "Chất liệu không tồn tại"));
            }

            if (dayGiayIds.containsKey(request.getDayGiay())) {
                DayGiay dayGiay = DayGiay.builder().id(dayGiayIds.get(request.getDayGiay()).getId()).build();
                giay.setDayGiay(dayGiay);
            } else {
                errors.add(new ExcelError(request.getRow(), 11, "Dây giày không tồn tại"));
            }

            if (deGiayIds.containsKey(request.getDeGiay())) {
                DeGiay deGiay = DeGiay.builder().id(deGiayIds.get(request.getDeGiay()).getId()).build();
                giay.setDeGiay(deGiay);
            } else {
                errors.add(new ExcelError(request.getRow(), 12, "Đế giày không tồn tại"));
            }

            List<HashTagChiTiet> tags = new ArrayList<>();
            tenHashTags.get().forEach(ht -> {
                if (hashTagIds.containsKey(ht)) {
                    HashTag tag = HashTag.builder().id(hashTagIds.get(ht).getId()).build();
                    tags.add(HashTagChiTiet.builder().hashTag(tag).giay(giay).build());
                } else {
                    errors.add(new ExcelError(request.getRow(), 13, "HashTag không tồn tại"));
                }
            });
            giay.setHashTagChiTiets(tags);

            if (request.getNamSX() == null) {
                errors.add(new ExcelError(request.getRow(), 14, "Năm sản xuất không được để trống"));
            } else if (request.getNamSX() > 9999 || request.getNamSX() < 1000) {
                errors.add(new ExcelError(request.getRow(), 14, "Năm sản xuất không hợp lệ"));
            }
            giay.setNamSX(request.getNamSX());

            if (request.getMoTa().isBlank()) {
                errors.add(new ExcelError(request.getRow(), 15, "Mô tả không được để trống"));
            } else if (request.getMoTa().length() < 3) {
                errors.add(new ExcelError(request.getRow(), 15, "Mô tả không được ít hơn 3 ký tự"));
            } else if (request.getMoTa().length() > 3000) {
                errors.add(new ExcelError(request.getRow(), 15, "Mô tả không được quá 3000 ký tự"));
            }
            giay.setMoTa(request.getMoTa());

            List<BienTheGiay> bienTheGiayList = new ArrayList<>();
            Map<String, String> files = new HashMap<>();
            for (Map.Entry<String, String> mauSacImage : request.getMauSacImages().entrySet()) {
                String file = imageHubService.base64ToFile(mauSacImage.getValue());
                files.put(mauSacImage.getKey(), file);
            }

            request.getBienTheGiays().forEach(bt -> {
                if (bt.getSoLuong() == null) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Không được để trống số lượng"));
                } else if (bt.getSoLuong() < 0) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Số lượng không được âm"));
                }

                if (bt.getBarcode() == null) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Không được để trống barcode"));
                } else if (bt.getBarcode().length() > 20) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Barcode không hợp lệ"));
                }

                if (bt.getGiaBan() == null) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Không được để trống giá bán"));
                } else if (bt.getGiaBan().compareTo(BigDecimal.ZERO) < 0) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Giá bán không được âm"));
                }

                if (kichThuocIds.containsKey(bt.getKichThuoc()) && mauSacIds.containsKey(bt.getMauSac())) {
                    BienTheGiay bienThe = new BienTheGiay();
                    bienThe.setGiay(giay);
                    bienThe.setGiaBan(bt.getGiaBan());
                    bienThe.setBarCode(bt.getBarcode());
                    bienThe.setSoLuong(bt.getSoLuong());
                    bienThe.setTrangThai(bt.getTrangThai());
                    bienThe.setKichThuoc(KichThuoc.builder().id(kichThuocIds.get(bt.getKichThuoc()).getId()).build());
                    bienThe.setMauSac(MauSac.builder().id(mauSacIds.get(bt.getMauSac()).getId()).build());
                    bienThe.setHinhAnh(files.get(bt.getMauSac()));
                    bienTheGiayList.add(bienThe);
                } else if (!kichThuocIds.containsKey(bt.getKichThuoc())) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Kích thước không tồn tại"));
                } else {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Màu sắc không tồn tại"));
                }
            });

            giay.setLstBienTheGiay(bienTheGiayList);

            //set hình ảnh
            List<HinhAnh> hinhAnhs = new ArrayList<>();
            if (request.getImage1() != null) {
                String file = imageHubService.base64ToFile(request.getImage1());
                HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(1).build();
                hinhAnhs.add(hinhAnh);
            }
            if (request.getImage2() != null) {
                String file = imageHubService.base64ToFile(request.getImage2());
                HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(2).build();
                hinhAnhs.add(hinhAnh);
            }
            if (request.getImage3() != null) {
                String file = imageHubService.base64ToFile(request.getImage3());
                HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(3).build();
                hinhAnhs.add(hinhAnh);
            }
            if (request.getImage4() != null) {
                String file = imageHubService.base64ToFile(request.getImage4());
                HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(4).build();
                hinhAnhs.add(hinhAnh);
            }
            if (request.getImage5() != null) {
                String file = imageHubService.base64ToFile(request.getImage1());
                HinhAnh hinhAnh = HinhAnh.builder().giay(giay).link(file).uuTien(5).build();
                hinhAnhs.add(hinhAnh);
            }

            giay.setLstAnh(hinhAnhs);

            giay.setTrangThai(1);
            giays.add(giay);

        });

        if (!errors.isEmpty()) {
            throw new ExcelException(errors);
        } else {
            giayRepository.saveAll(giays);
        }

    }

    @Transactional
    @Override
    public void updateExcel(List<GiayExcelRequest> giayExcelRequests) {
        List<ExcelError> errors = new ArrayList<>();
        Set<String> removeFiles = new HashSet<>();  // rollback nên không cần xóa hình ảnh
        Set<String> tenGiays = new HashSet<>();
        Set<String> tenKichThuocs = new HashSet<>();
        Set<String> tenMauSacs = new HashSet<>();
        Map<String, String> lstBarcodeMap = new HashMap<>();
        Set<String> tenHashTags = new HashSet<>();

        giayExcelRequests.forEach(request -> {
            tenGiays.add(request.getTen());
            tenHashTags.addAll(request.getHashTags());

            request.getBienTheGiays().forEach(bienThe -> {
                tenMauSacs.add(bienThe.getMauSac());
                tenKichThuocs.add(bienThe.getKichThuoc());
                if (lstBarcodeMap.containsKey(bienThe.getBarcode())) {
                    errors.add(new ExcelError(bienThe.getRow(), bienThe.getColumn(), "Barcode không được trùng"));
                } else {
                    lstBarcodeMap.put(bienThe.getBarcode(), bienThe.getBarcode());
                }
            });
        });

        List<BienTheGiayResponse> lstBienTheBarcode = bienTheGiayRepository.getBienTheGiayByListBarCode(new ArrayList<>(lstBarcodeMap.values()));
        Map<String, HashTagResponse> hashTagIds = hashTagRepository.getIdsByName(tenHashTags).stream().collect(Collectors.toMap(HashTagResponse::getTen, giay -> giay));

        if (!lstBienTheBarcode.isEmpty()) {
            lstBienTheBarcode.forEach(bt -> {
                giayExcelRequests.forEach(request -> {
                    request.getBienTheGiays().forEach(bienThe -> {
                        if (lstBarcodeMap.containsKey(bt.getBarCode())
                                && bienThe.getKichThuoc().equals(bt.getKichThuoc().getTen())
                                && bienThe.getMauSac().equals(bt.getMauSac().getTen())
                        ) {
                            errors.add(new ExcelError(bienThe.getRow(), bienThe.getColumn(), "Barcode không được trùng"));
                        }
                    });
                });
            });
        }

        if (!errors.isEmpty()) {
            throw new ExcelException(errors);
        }

        Map<String, MauSacResponse> mauSacIds = mauSacRepository.getIdsByName(tenMauSacs).stream().collect(Collectors.toMap(MauSacResponse::getTen, giay -> giay));
        Map<String, KichThuocResponse> kichThuocIds = kichThuocRepository.getIdsByName(tenKichThuocs).stream().collect(Collectors.toMap(KichThuocResponse::getTen, giay -> giay));

        List<Giay> giays = new ArrayList<>();


        giayExcelRequests.forEach(request -> {
            Optional<Giay> giayOptional = giayRepository.findByTen(request.getTen());
            if (giayOptional.isEmpty()) {
                errors.add(new ExcelError(request.getRow(), 0, "Giày không tồn tại"));
                throw new ExcelException(errors);
            }
            Giay giay = giayOptional.get();

            if (request.getLotGiay() != null && !request.getLotGiay().equals(giay.getLotGiay().getTen())) {
                LotGiay lotGiay = lotGiayRepository.findByTen(request.getLotGiay()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 6, "Lót giày không tồn tại")));
                giay.setLotGiay(lotGiay);
            }

            if (request.getMuiGiay() != null && !request.getMuiGiay().equals(giay.getMuiGiay().getTen())) {
                MuiGiay muiGiay = muiGiayRepository.findByTen(request.getMuiGiay()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 7, "Mũi giày không tồn tại")));
                giay.setMuiGiay(muiGiay);
            }

            if (request.getCoGiay() != null && !request.getCoGiay().equals(giay.getCoGiay().getTen())) {
                CoGiay coGiay = coGiayRepository.findByTen(request.getCoGiay()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 8, "Cổ giày không tồn tại")));
                giay.setCoGiay(coGiay);
            }

            if (request.getThuongHieu() != null && !request.getThuongHieu().equals(giay.getThuongHieu().getTen())) {
                ThuongHieu thuongHieu = thuongHieuRepository.findByTen(request.getThuongHieu()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 9, "Thương hiệu không tồn tại")));
                giay.setThuongHieu(thuongHieu);
            }

            if (request.getChatLieu() != null && !request.getChatLieu().equals(giay.getChatLieu().getTen())) {
                ChatLieu chatLieu = chatLieuRepository.findByTen(request.getChatLieu()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 10, "Chất liệu không tồn tại")));
                giay.setChatLieu(chatLieu);
            }

            if (request.getDayGiay() != null && !request.getDayGiay().equals(giay.getDayGiay().getTen())) {
                DayGiay dayGiay = dayGiayRepository.findByTen(request.getDayGiay()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 11, "Dây giày không tồn tại")));
                giay.setDayGiay(dayGiay);
            }

            if (request.getDeGiay() != null && !request.getDeGiay().equals(giay.getDeGiay().getTen())) {
                DeGiay deGiay = deGiayRepository.findByTen(request.getDeGiay()).orElseThrow(() -> new ExcelException(new ExcelError(request.getRow(), 12, "Đế giày không tồn tại")));
                giay.setDeGiay(deGiay);
            }

            if (!tenHashTags.isEmpty()) {
                List<HashTagChiTiet> tags = new ArrayList<>();
                tenHashTags.forEach(ht -> {
                    if (hashTagIds.containsKey(ht)) {
                        HashTag tag = HashTag.builder().id(hashTagIds.get(ht).getId()).build();
                        tags.add(HashTagChiTiet.builder().hashTag(tag).giay(giay).build());
                    } else {
                        errors.add(new ExcelError(request.getRow(), 13, "HashTag không tồn tại"));
                    }
                });
                giay.setHashTagChiTiets(tags);
            }

            if (request.getNamSX() != null && (request.getNamSX() > 9999 || request.getNamSX() < 1000)) {
                errors.add(new ExcelError(request.getRow(), 14, "Năm sản xuất không hợp lệ"));
            } else if (request.getNamSX() != null) {
                giay.setNamSX(request.getNamSX());
            }

            if (request.getMoTa() != null && request.getMoTa().isBlank()) {
                errors.add(new ExcelError(request.getRow(), 15, "Mô tả không được để trống"));
            } else if (request.getMoTa() != null && request.getMoTa().length() < 3) {
                errors.add(new ExcelError(request.getRow(), 15, "Mô tả không được ít hơn 3 ký tự"));
            } else if (request.getMoTa() != null && request.getMoTa().length() > 3000) {
                errors.add(new ExcelError(request.getRow(), 15, "Mô tả không được quá 3000 ký tự"));
            } else if (request.getMoTa() != null) {
                giay.setMoTa(request.getMoTa());
            }

            List<BienTheGiay> bienTheGiayList = giay.getLstBienTheGiay();
            Map<String, String> files = new HashMap<>();
            for (Map.Entry<String, String> mauSacImage : request.getMauSacImages().entrySet()) {
                String file = imageHubService.base64ToFile(mauSacImage.getValue());
                files.put(mauSacImage.getKey(), file);
            }


            request.getBienTheGiays().forEach(bt -> {
                List<String> newBarcodes = new ArrayList<>();
                if (bt.getSoLuong() != null && bt.getSoLuong() < 0) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Số lượng không được âm"));
                }

                if (bt.getBarcode() != null && bt.getBarcode().length() > 20) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Barcode không hợp lệ"));
                }

                if (bt.getGiaBan() != null && bt.getGiaBan().compareTo(BigDecimal.ZERO) < 0) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Giá bán không được âm"));
                }

                if (kichThuocIds.containsKey(bt.getKichThuoc()) && mauSacIds.containsKey(bt.getMauSac())) {
                    boolean exists = false;
                    for (BienTheGiay bienTheGiay : bienTheGiayList) {
                        if (bt.getKichThuoc().equals(bienTheGiay.getKichThuoc().getTen()) && bt.getMauSac().equals(bienTheGiay.getMauSac().getTen())) {

                            if (bt.getSoLuong() != null) {
                                bienTheGiay.setSoLuong(bt.getSoLuong());
                            }
                            if (bt.getGiaBan() != null) {
                                bienTheGiay.setGiaBan(bt.getGiaBan());
                            }

                            if (bt.getBarcode() != null) {
                                if (bienTheGiayRepository.getBienTheGiayByBarCodeUpdate(bt.getBarcode(), bienTheGiay.getId())) {
                                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Barcode đã tồn tại"));
                                }
                            }
                            exists = true;
                        }
                    }

                    if (!exists) {
                        BienTheGiay bienThe = new BienTheGiay();
                        bienThe.setGiay(giay);
                        bienThe.setGiaBan(bt.getGiaBan());
                        bienThe.setBarCode(bt.getBarcode());
                        bienThe.setSoLuong(bt.getSoLuong());
                        bienThe.setTrangThai(bt.getTrangThai());
                        bienThe.setKichThuoc(KichThuoc.builder().id(kichThuocIds.get(bt.getKichThuoc()).getId()).build());
                        bienThe.setMauSac(MauSac.builder().id(mauSacIds.get(bt.getMauSac()).getId()).build());
                        bienThe.setHinhAnh(files.get(bt.getMauSac()));
                        bienTheGiayList.add(bienThe);
                        newBarcodes.add(bienThe.getBarCode());
                    }
                } else if (!kichThuocIds.containsKey(bt.getKichThuoc())) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Kích thước không tồn tại"));
                } else {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Màu sắc không tồn tại"));
                }

                List<BienTheGiayResponse> lstNewBienTheBarcode = bienTheGiayRepository.getBienTheGiayByListBarCode(newBarcodes);

                for (BienTheGiayResponse bienTheGiayResponse : lstNewBienTheBarcode) {
                    errors.add(new ExcelError(bt.getRow(), bt.getColumn(), "Barcode đã tồn tại"));
                }
                if (!errors.isEmpty()) {
                    throw new ExcelException(errors);
                }

            });

            giay.setLstBienTheGiay(bienTheGiayList);

            //set hình ảnh
            List<HinhAnh> hinhAnhs = giay.getLstAnh();

            if (request.getImage1() != null) {
                boolean finded = false;
                String file = imageHubService.base64ToFile(request.getImage1());
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

            if (request.getImage2() != null) {
                boolean finded = false;
                String file = imageHubService.base64ToFile(request.getImage2());
                for (int i = 0; i < hinhAnhs.size(); i++) {
                    if (hinhAnhs.get(i).getUuTien() == 2) {
                        removeFiles.add(hinhAnhs.get(i).getLink());
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

            if (request.getImage3() != null) {
                boolean finded = false;
                String file = imageHubService.base64ToFile(request.getImage3());
                for (int i = 0; i < hinhAnhs.size(); i++) {
                    if (hinhAnhs.get(i).getUuTien() == 3) {
                        removeFiles.add(hinhAnhs.get(i).getLink());
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
            if (request.getImage4() != null) {
                boolean finded = false;
                String file = imageHubService.base64ToFile(request.getImage4());
                for (int i = 0; i < hinhAnhs.size(); i++) {
                    if (hinhAnhs.get(i).getUuTien() == 4) {
                        removeFiles.add(hinhAnhs.get(i).getLink());
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
            if (request.getImage5() != null) {
                String file = imageHubService.base64ToFile(request.getImage5());
                boolean finded = false;
                for (int i = 0; i < hinhAnhs.size(); i++) {
                    if (hinhAnhs.get(i).getUuTien() == 5) {
                        removeFiles.add(hinhAnhs.get(i).getLink());
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
            giays.add(giay);

        });

        if (!errors.isEmpty()) {
            throw new ExcelException(errors);
        } else {
            giayRepository.saveAll(giays);
            imageHubService.deleteFile(removeFiles);
        }

    }

    @Override
    public Integer getSoLuong(Long id) {
        return bienTheGiayRepository.getSoLuong(id);
    }

    @Override
    public BienTheGiayResponse getBienTheByBarcode(String barCode) {
        return bienTheGiayRepository.getBienTheGiayByBarCode(barCode)
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tồn tại"))));
    }

    @Override
    public List<BienTheGiayResponse> getBienTheGiayByListId(List<Long> ids) {
        return bienTheGiayRepository.findAllByIdIn(ids);
    }

    @Override
    public Page<GiayResponse> findSimpleBySearch(GiaySearch giaySearch) {
        Pageable pageable = PageRequest.of(giaySearch.getCurrentPage() - 1, giaySearch.getPageSize());
        return giayRepository.findPageForSearch(giaySearch, pageable);

    }

    @Override
    public List<GiayResponse> getAllGiayWithoutDiscount(GiaySearch giaySearch) {
        return giayRepository.getAllGiayWithoutDiscount(giaySearch);
    }

    @Override
    public GiayResponse getResponseById(Long id) {
        GiayResponse giayResponse = giayRepository.findResponseById(id);
        if (giayResponse == null) {
            throw new NotFoundException("Không tìm thấy giày này");
        }

        for(int i = 0; i < giayResponse.getLstBienTheGiay().size(); i ++) {
            if(giayResponse.getLstBienTheGiay().get(i).getTrangThai() != 1) {
                giayResponse.getLstBienTheGiay().remove(i);
            }
        }

        Map<Long, String> mauSacImages = new HashMap<>();

        List<Long> idBienThes = new ArrayList<>();
        giayResponse.getLstBienTheGiay().forEach(bienThe -> {
            idBienThes.add(bienThe.getId());
            if (!mauSacImages.containsKey(bienThe.getMauSac().getId())) {
                mauSacImages.put(bienThe.getMauSac().getId(), imageHubService.getBase64FromFile(bienThe.getHinhAnh()));
            }
        });
        giayResponse.setMauSacImages(mauSacImages);

        List<KhuyenMaiChiTietResponse> khuyenMaiChiTiets = khuyenMaiChiTietRepository.getAllByIdBienThe(idBienThes);
        for (KhuyenMaiChiTietResponse kmct : khuyenMaiChiTiets) {
            for (BienTheGiayResponse bienTheGiayResponse : giayResponse.getLstBienTheGiay()) {
                if (bienTheGiayResponse.getId().equals(kmct.getBienTheGiayResponsel().getId())) {
                    bienTheGiayResponse.setKhuyenMai(kmct.getPhanTramGiam());
                }
            }
        }

        return giayResponse;
    }

    private void getGiay(Giay giay, GiayRequest giayRequest) {

        DayGiay dayGiay = dayGiayRepository.findById(giayRequest.getDayGiayId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dayGiay", "Không tồn tại dây giày này"))));
        giay.setDayGiay(dayGiay);

        LotGiay lotGiay = lotGiayRepository.findById(giayRequest.getLotGiayId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("lotGiay", "Không tồn tại lót giày này"))));
        giay.setLotGiay(lotGiay);

        MuiGiay muiGiay = muiGiayRepository.findById(giayRequest.getMuiGiayId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("muiGiay", "Không tồn tại mũi giày này"))));
        giay.setMuiGiay(muiGiay);

        CoGiay coGiay = coGiayRepository.findById(giayRequest.getCoGiayId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("coGiay", "Không tồn tại cổ giày này"))));
        giay.setCoGiay(coGiay);

        ThuongHieu thuongHieu = thuongHieuRepository.findById(giayRequest.getThuongHieuId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("thuongHieu", "Không tồn tại thương hiệu này"))));
        giay.setThuongHieu(thuongHieu);

        ChatLieu chatLieu = chatLieuRepository.findById(giayRequest.getChatLieuId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("chatLieu", "Không tồn tại chất liệu này"))));
        giay.setChatLieu(chatLieu);

        DeGiay deGiay = deGiayRepository.findById(giayRequest.getDeGiayId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("deGiay", "Không tồn tại đế giày này"))));
        giay.setDeGiay(deGiay);

        List<HashTag> hashTags = hashTagRepository.findByIdIn(giayRequest.getHashTagIds());
        List<HashTagChiTiet> hashTagChiTiets = hashTags.stream().map(hashTag -> HashTagChiTiet.builder().giay(giay).hashTag(hashTag).build()).collect(Collectors.toList());

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
            errors.add(bienTheGiayResponse.getMauSac().getId() + ", " + bienTheGiayResponse.getKichThuoc().getId() + ": Barcode đã tồn tại");
        }


        if (!errors.isEmpty()) {
            throw new ConflictException(errors);
        }

    }

    private void checkForUpdate(List<String> lstBarcode, List<String> errors) {

        List<BienTheGiayResponse> lstBienTheBarcode = bienTheGiayRepository.getBienTheGiayByListBarCode(lstBarcode);

        for (BienTheGiayResponse bienTheGiayResponse : lstBienTheBarcode) {
            errors.add(bienTheGiayResponse.getMauSac().getId() + ", " + bienTheGiayResponse.getKichThuoc().getId() + ": Barcode đã tồn tại");
        }

        if (!errors.isEmpty()) {
            throw new ConflictException(errors);
        }
    }


}
