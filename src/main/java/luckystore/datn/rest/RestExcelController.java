package luckystore.datn.rest;

import luckystore.datn.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/download")
public class RestExcelController {

    @Autowired
    private ExcelUtil excelUtil;

    @GetMapping("/insert-template")
    public ResponseEntity<byte[]> downloadInsertFile() throws IOException {

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "InsertTemplate.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelUtil.writeExcel("InsertTemplate.xlsx"));
    }

    @GetMapping("/update-template")
    public ResponseEntity<byte[]> downloadUpdateFile() throws IOException {

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "UpdateTemplate.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelUtil.writeExcel("UpdateTemplate.xlsx"));
    }

}
