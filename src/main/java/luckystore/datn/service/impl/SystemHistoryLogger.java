package luckystore.datn.service.impl;

import luckystore.datn.entity.SystemHistoryEntry;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemHistoryLogger {

    private static List<String> sheetName = new ArrayList<>();
    private static List<Integer> sumAction = new ArrayList<>();

    public static void writeSystemHistoryEntry(SystemHistoryEntry entry) {
        List<SystemHistoryEntry> existingEntries = readSystemHistoryEntries();
        boolean duplicateFound = false;
        for (SystemHistoryEntry existingEntry : existingEntries) {
            if (existingEntry.getTimestamp().equals(entry.getTimestamp())) {
                duplicateFound = true;
                break;
            }
        }
        if (!duplicateFound) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("SystemHistory.txt", true))) {
                writer.println("Performer: " + entry.getPerformerName());
                writer.println("Action: " + entry.getActionDescription());
                writer.println("Timestamp: " + entry.getTimestamp());
                writer.println();
                System.out.println("Ghi lịch sử hệ thống thành công.");
            } catch (IOException e) {
                System.err.println("Lỗi khi ghi lịch sử hệ thống: " + e.getMessage());
            }
        }
    }


    public static List<SystemHistoryEntry> readSystemHistoryEntries() {
        List<SystemHistoryEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("SystemHistory.txt"))) {
            String line;
            SystemHistoryEntry entry = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Performer: ")) {
                    String performerName = line.substring(11);
                    String actionDescription = reader.readLine().substring(7);
                    String timestamp = reader.readLine().substring(11);

                    entry = new SystemHistoryEntry(performerName, actionDescription, timestamp);
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc lịch sử hệ thống: " + e.getMessage());
        }
        return entries;
    }

    public static void sendDaiLyReport() {
        Map<String, List<SystemHistoryEntry>> performerMap = new HashMap<>();

        for (SystemHistoryEntry log : readSystemHistoryEntries()) {
            String performer = log.getPerformerName();
            if (!performerMap.containsKey(performer)) {
                performerMap.put(performer, new ArrayList<>());
            }
            performerMap.get(performer).add(log);
        }

        List<List<SystemHistoryEntry>> dataList = new ArrayList<>();
        sheetName = new ArrayList<>();

        for (String performer : performerMap.keySet()) {
            List<SystemHistoryEntry> performerLogs = performerMap.get(performer);
            dataList.add(performerLogs);
            sheetName.add(performer);
        }

        writeDataToExcel(dataList, sheetName);
    }

    public static void main(String[] args) {
        sendDaiLyReport();
    }

    public static void writeDataToExcel(List<List<SystemHistoryEntry>> dataLists, List<String> sheetNames) {
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle = createHeaderStyle(workbook); // Tạo CellStyle cho ô tiêu đề



            for (int i = 0; i < dataLists.size(); i++) {
                Sheet sheet = workbook.createSheet(sheetNames.get(i));
                List<?> dataList = dataLists.get(i);
                sumAction.add(dataList.size());
                int rowNum = 0;

                // Tạo dòng tiêu đề
                Row headerRow = sheet.createRow(rowNum++);
                if (dataList.get(0) instanceof String) {
                    // Đây là danh sách kiểu String
                    headerRow.createCell(0).setCellValue("Dữ liệu kiểu String");
                } else if (dataList.get(0) instanceof SystemHistoryEntry) {
                    // Đây là danh sách kiểu SystemHistoryEntry
                    headerRow.createCell(0).setCellValue("Người Thực Hiện");
                    headerRow.createCell(1).setCellValue("Hành Động");
                    headerRow.createCell(2).setCellValue("Thời Gian");
                }
                // Đặt CellStyle cho ô tiêu đề
                for (Cell cell : headerRow) {
                    cell.setCellStyle(headerStyle);
                }

                // Lặp qua danh sách dữ liệu và in vào tệp Excel
                for (Object data : dataList) {
                    Row row = sheet.createRow(rowNum++);
                    if (data instanceof String) {
                        // Xử lý dữ liệu kiểu String
                        row.createCell(0).setCellValue((String) data);
                    } else if (data instanceof SystemHistoryEntry) {
                        // Xử lý dữ liệu kiểu SystemHistoryEntry
                        SystemHistoryEntry entry = (SystemHistoryEntry) data;
                        row.createCell(0).setCellValue(entry.getPerformerName());
                        row.createCell(1).setCellValue(entry.getActionDescription());
                        row.createCell(2).setCellValue(entry.getTimestamp());
                    }
                }
                // Thêm biên (border) cho các ô
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        setBorderStyle(cell);
                    }
                }
                // Tự động căn chỉnh kích thước cột
                for (int column = 0; column < headerRow.getLastCellNum(); column++) {
                    sheet.autoSizeColumn(column);
                }
            }

            // Tạo một trang riêng để ghi các cột tùy chỉnh
            Sheet customSheet = workbook.createSheet("Tổng Quan");
            Row customHeaderRow = customSheet.createRow(0);
            // Tạo các ô cho cột tùy chỉnh
            customHeaderRow.createCell(0).setCellValue("Nhân Viên");
            customHeaderRow.createCell(1).setCellValue("Số Đơn Hàng Bán Trong Ngày");
            customHeaderRow.createCell(2).setCellValue("Phần Trăm");
            customHeaderRow.createCell(3).setCellValue("Số Giờ Trực Tuyến");
            customHeaderRow.createCell(4).setCellValue("Số Thao Tác");
            for (Cell cell : customHeaderRow) {
                cell.setCellStyle(headerStyle);
            }
            for (Row rowCss : customSheet) {
                for (Cell cell : rowCss) {
                    setBorderStyle(cell);
                }
            }
            // Lặp qua danh sách dữ liệu và in vào trang tùy chỉnh
            for (int i = 1; i <= sheetName.size(); i++) {
                Row row = customSheet.createRow(i);
                row.createCell(0).setCellValue(sheetName.get(i-1));
                row.createCell(1).setCellValue("43");
                row.createCell(2).setCellValue("23%");
                row.createCell(3).setCellValue("8 Tiếng");
                row.createCell(4).setCellValue(sumAction.get(i-1));

                // Tự động căn chỉnh kích thước cột
                for (int column = 0; column < row.getLastCellNum(); column++) {
                    customSheet.autoSizeColumn(column);
                }
            }


            // Lưu tệp Excel
            try (FileOutputStream outputStream = new FileOutputStream("file.xlsx")) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tạo CellStyle cho ô tiêu đề với màu nền xanh lá và màu chữ trắng
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        return headerStyle;
    }

    // Đặt biên (border) cho ô
    private static void setBorderStyle(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
    }

    public static String getDateNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd / HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }

}
