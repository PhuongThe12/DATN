package luckystore.datn.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import luckystore.datn.repository.ChatLieuRepository;
import luckystore.datn.repository.CoGiayRepository;
import luckystore.datn.repository.DayGiayRepository;
import luckystore.datn.repository.DeGiayRepository;
import luckystore.datn.repository.HashTagRepository;
import luckystore.datn.repository.KichThuocRepository;
import luckystore.datn.repository.LotGiayRepository;
import luckystore.datn.repository.MauSacRepository;
import luckystore.datn.repository.MuiGiayRepository;
import luckystore.datn.repository.ThuongHieuRepository;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ExcelUtil {


    private final LotGiayRepository lotGiayRepository;
    private final MuiGiayRepository muiGiayRepository;
    private final CoGiayRepository coGiayRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final DayGiayRepository dayGiayRepository;
    private final DeGiayRepository deGiayRepository;
    private final MauSacRepository mauSacRepository;
    private final KichThuocRepository kichThuocRepository;

    @SneakyThrows
    public byte[] writeExcel(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            setDataColumn(sheet, lotGiayRepository.getAllTen(), 6);
            setDataColumn(sheet, muiGiayRepository.getAllTen(), 7);
            setDataColumn(sheet, coGiayRepository.getAllTen(), 8);
            setDataColumn(sheet, thuongHieuRepository.getAllTen(), 9);
            setDataColumn(sheet, chatLieuRepository.getAllTen(), 10);
            setDataColumn(sheet, dayGiayRepository.getAllTen(), 11);
            setDataColumn(sheet, deGiayRepository.getAllTen(), 12);
            setDataColumn(sheet, mauSacRepository.getAllTen(), 16);
            setDataColumn(sheet, kichThuocRepository.getAllTen(), 17);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            workbook.write(outputStream);
            workbook.close();

            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDataColumn(Sheet sheet, String[] data, int column) {

        CellRangeAddressList addressList = new CellRangeAddressList(5, 9999, column, column);

        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint validationConstraint = validationHelper.createExplicitListConstraint(data);
        DataValidation dataValidation = validationHelper.createValidation(validationConstraint, addressList);

        sheet.addValidationData(dataValidation);
    }
}
