package luckystore.datn.controller.admin;

import luckystore.datn.service.impl.NhanVienServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/nhan-vien")
public class NhanVienController {

    @Autowired
    NhanVienServiceImpl nhanVienService;

    @GetMapping()
    public String getIndex() {
        return "/admin/nhanvien/index";
    }

    @GetMapping("/detail")
    public String getDetail(){
         return "/admin/nhanvien/demo";
    }
}
