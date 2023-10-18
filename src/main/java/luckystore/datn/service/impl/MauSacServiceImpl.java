package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.dto.MauSacDto;
import luckystore.datn.entity.MauSac;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.repository.MauSacRepository;
import luckystore.datn.service.MauSacService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MauSacServiceImpl implements MauSacService {

    @Autowired
    private MauSacRepository mauSacRepo;

    @Override
    public List<MauSacDto> getAll() {
        return mauSacRepo.findAll().stream().map(MauSacDto::new).collect(Collectors.toList());
    }

    @Override
    public MauSacDto addMauSac(MauSacDto mauSacDto) {
        checkWhenInsert(mauSacDto);

        MauSac mauSac = getMauSac(new MauSac(), mauSacDto);
        return new MauSacDto(mauSacRepo.save(mauSac));
    }

    @Override
    public MauSacDto updateMauSac(Long id, MauSacDto mauSacDto) {
        MauSac mauSac;
        if(id == null) {
            throw new NullException();
        }else {
            mauSac = mauSacRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(mauSacDto);
        mauSac = getMauSac(mauSac, mauSacDto);
        return new MauSacDto(mauSacRepo.save(mauSac));
    }

    @Override
    public MauSacDto findById(Long id) {
        return new MauSacDto(mauSacRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private MauSac getMauSac(MauSac mauSac, MauSacDto mauSacDto) {
        mauSac.setTen(mauSacDto.getTen());
        mauSac.setMoTa(mauSacDto.getMoTa());
        return mauSac;
    }

    private void checkWhenInsert(MauSacDto mauSacDto) {
        if(mauSacRepo.existsByTen(mauSacDto.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(MauSacDto mauSacDto) {
        if(mauSacRepo.existsByTenAndIdNot(mauSacDto.getTen(), mauSacDto.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}
