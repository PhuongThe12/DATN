package luckystore.datn.service;

import luckystore.datn.dto.MauSacDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MauSacService {

    List<MauSacDto> getAll();
    MauSacDto addMauSac(MauSacDto mauSacDto);
    MauSacDto updateMauSac(Long id, MauSacDto mauSacDto);
    MauSacDto findById(Long id);

}
