package luckystore.datn.util;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtil {
    public static void main(String[] args) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            // Tạo danh sách lựa chọn
            String[] options = {"Option 1", "Option 2", "Option 3", "Option 1", "Option 2", "Option 3", "Option 1", "Option 2", "Option 3"};

            // Tạo CellRangeAddressList để áp dụng Data Validation
            CellRangeAddressList addressList = new CellRangeAddressList(0, 9999, 0, 0);

            // Tạo Data Validation Constraint
            DataValidationHelper validationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint validationConstraint = validationHelper.createExplicitListConstraint(options);

            // Tạo Data Validation
            DataValidation dataValidation = validationHelper.createValidation(validationConstraint, addressList);

            // Áp dụng Data Validation cho Cell
            sheet.addValidationData(dataValidation);

            // Ghi workbook ra file
            try (FileOutputStream fileOut = new FileOutputStream("workbook_with_dropdown.xlsx")) {
                workbook.write(fileOut);
            }

            // Đóng workbook
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
