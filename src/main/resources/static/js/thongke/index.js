var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/sanpham", {
            templateUrl: '/pages/admin/thongke/views/thongkesanpham.html',
            controller: 'thongKeSanPhamController'
        }).when("/doanhthu", {
        templateUrl: '/pages/admin/thuonghieu/views/update.html',
        controller: 'updateThuongHieuController'
    })
        .otherwise({redirectTo: '/sanpham'});
});

app.controller("thongKeSanPhamController", function ($scope, $http, $location) {

    $scope.curPage = 1, $scope.itemsPerPage = 5, $scope.maxSize = 5;
    $scope.thongKeTopGiayBanChay = {}, $scope.topGiayBanChay = "1", $scope.thoiGian1 = "3", $scope.thongKeTopGiayBanChay.currentPage = $scope.curPage;
    $scope.thongKeTopGiayYeuThich = {}, $scope.topGiayYeuThich = "1", $scope.thongKeTopGiayYeuThich.currentPage = $scope.curPage;
    $scope.thongKeTopBienTheGiayBanChay = {}, $scope.topBienTheGiayBanChay = "1", $scope.thoiGian2 = "3", $scope.thongKeTopBienTheGiayBanChay.currentPage = $scope.curPage;
    $scope.thongKeTopBienTheGiayQuanTam = {}, $scope.topBienTheGiayQuanTam = "1", $scope.thongKeTopBienTheGiayQuanTam.currentPage = $scope.curPage;
    $scope.thongKeTopBienTheGiayTra = {}, $scope.topBienTheGiayTra = "1", $scope.thongKeTopBienTheGiayTra.currentPage = $scope.curPage;
    $scope.thongKeLyDo = {}, $scope.topLyDo = "1", $scope.thongKeLyDo.currentPage = $scope.curPage;


    findTopSellingShoesInLastDays($scope.thongKeTopGiayBanChay);
    findTopFavoritedShoes($scope.thongKeTopGiayYeuThich);
    findTopSellingShoeVariantInLastDays($scope.thongKeTopBienTheGiayBanChay);
    findTopCartVariants($scope.thongKeTopBienTheGiayQuanTam);
    findVariantReturnRates($scope.thongKeTopBienTheGiayTra);
    findReasonsForReturn($scope.thongKeLyDo)

    $scope.topSellingShoesInLastDays = function () {
        if ($scope.topGiayBanChay == "1") {
            $scope.thongKeTopGiayBanChay.pageSize = 5;
        } else if ($scope.topGiayBanChay == "2") {
            $scope.thongKeTopGiayBanChay.pageSize = 10;
        } else if ($scope.topGiayBanChay == "3") {
            $scope.thongKeTopGiayBanChay.pageSize = 20;
        } else if ($scope.topGiayBanChay == "4") {
            $scope.thongKeTopGiayBanChay.pageSize = 30;
        } else if ($scope.topGiayBanChay == "5") {
            $scope.thongKeTopGiayBanChay.pageSize = 50;
        } else if ($scope.topGiayBanChay == "6") {
            $scope.thongKeTopGiayBanChay.pageSize = 100;
        }

        if ($scope.thoiGian1 == "1") {
            $scope.thongKeTopGiayBanChay.lastDate = 1;
        } else if ($scope.thoiGian1 == "2") {
            $scope.thongKeTopGiayBanChay.lastDate = 7;
        } else if ($scope.thoiGian1 == "3") {
            $scope.thongKeTopGiayBanChay.lastDate = 30;
        } else if ($scope.thoiGian1 == "4") {
            $scope.thongKeTopGiayBanChay.lastDate = 90;
        } else if ($scope.thoiGian1 == "5") {
            $scope.thongKeTopGiayBanChay.lastDate = 180;
        } else if ($scope.thoiGian1 == "6") {
            $scope.thongKeTopGiayBanChay.lastDate = 365;
        }
        findTopSellingShoesInLastDays($scope.thongKeTopGiayBanChay)
    }


    $scope.topFavoritedShoes = function () {
        if ($scope.topGiayYeuThich == "1") {
            $scope.thongKeTopGiayYeuThich.pageSize = 5;
        } else if ($scope.topGiayYeuThich == "2") {
            $scope.thongKeTopGiayYeuThich.pageSize = 10;
        } else if ($scope.topGiayYeuThich == "3") {
            $scope.thongKeTopGiayYeuThich.pageSize = 20;
        } else if ($scope.topGiayYeuThich == "4") {
            $scope.thongKeTopGiayYeuThich.pageSize = 30;
        } else if ($scope.topGiayYeuThich == "5") {
            $scope.thongKeTopGiayYeuThich.pageSize = 50;
        } else if ($scope.topGiayYeuThich == "6") {
            $scope.thongKeTopGiayYeuThich.pageSize = 100;
        }
        findTopFavoritedShoes($scope.thongKeTopGiayYeuThich);
    }

    $scope.topSellingShoeVariantInLastDays = function () {
        if ($scope.topBienTheGiayBanChay == "1") {
            $scope.thongKeTopBienTheGiayBanChay.pageSize = 5;
        } else if ($scope.topBienTheGiayBanChay == "2") {
            $scope.thongKeTopBienTheGiayBanChay.pageSize = 10;
        } else if ($scope.topBienTheGiayBanChay == "3") {
            $scope.thongKeTopBienTheGiayBanChay.pageSize = 20;
        } else if ($scope.topBienTheGiayBanChay == "4") {
            $scope.thongKeTopBienTheGiayBanChay.pageSize = 30;
        } else if ($scope.topBienTheGiayBanChay == "5") {
            $scope.thongKeTopBienTheGiayBanChay.pageSize = 50;
        } else if ($scope.topBienTheGiayBanChay == "6") {
            $scope.thongKeTopBienTheGiayBanChay.pageSize = 100;
        }

        if ($scope.thoiGian2 == "1") {
            $scope.thongKeTopBienTheGiayBanChay.lastDate = 1;
        } else if ($scope.thoiGian2 == "2") {
            $scope.thongKeTopBienTheGiayBanChay.lastDate = 7;
        } else if ($scope.thoiGian2 == "3") {
            $scope.thongKeTopBienTheGiayBanChay.lastDate = 30;
        } else if ($scope.thoiGian2 == "4") {
            $scope.thongKeTopBienTheGiayBanChay.lastDate = 90;
        } else if ($scope.thoiGian2 == "5") {
            $scope.thongKeTopBienTheGiayBanChay.lastDate = 180;
        } else if ($scope.thoiGian2 == "6") {
            $scope.thongKeTopBienTheGiayBanChay.lastDate = 365;
        }
        findTopSellingShoeVariantInLastDays($scope.thongKeTopBienTheGiayBanChay)
    }

    $scope.topCartVariants = function () {
        if ($scope.topBienTheGiayQuanTam == "1") {
            $scope.thongKeTopBienTheGiayQuanTam.pageSize = 5;
        } else if ($scope.topBienTheGiayQuanTam == "2") {
            $scope.thongKeTopBienTheGiayQuanTam.pageSize = 10;
        } else if ($scope.topBienTheGiayQuanTam == "3") {
            $scope.thongKeTopBienTheGiayQuanTam.pageSize = 20;
        } else if ($scope.topBienTheGiayQuanTam == "4") {
            $scope.thongKeTopBienTheGiayQuanTam.pageSize = 30;
        } else if ($scope.topBienTheGiayQuanTam == "5") {
            $scope.thongKeTopBienTheGiayQuanTam.pageSize = 50;
        } else if ($scope.topBienTheGiayQuanTam == "6") {
            $scope.thongKeTopBienTheGiayQuanTam.pageSize = 100;
        }
        findTopCartVariants($scope.thongKeTopGiayYeuThich);
    }

    $scope.topVariantReturnRates = function () {
        if ($scope.topBienTheGiayTra == "1") {
            $scope.thongKeTopBienTheGiayTra.pageSize = 5;
        } else if ($scope.topBienTheGiayTra == "2") {
            $scope.thongKeTopBienTheGiayTra.pageSize = 10;
        } else if ($scope.topBienTheGiayTra == "3") {
            $scope.thongKeTopBienTheGiayTra.pageSize = 20;
        } else if ($scope.topBienTheGiayTra == "4") {
            $scope.thongKeTopBienTheGiayTra.pageSize = 30;
        } else if ($scope.topBienTheGiayTra == "5") {
            $scope.thongKeTopBienTheGiayTra.pageSize = 50;
        } else if ($scope.topBienTheGiayTra == "6") {
            $scope.thongKeTopBienTheGiayTra.pageSize = 100;
        }
        findVariantReturnRates($scope.thongKeTopBienTheGiayTra);
    }

    $scope.topReasonsForReturn = function () {
        if ($scope.topLyDo == "1") {
            $scope.thongKeLyDo.pageSize = 5;
        } else if ($scope.topLyDo == "2") {
            $scope.thongKeLyDo.pageSize = 10;
        } else if ($scope.topLyDo == "3") {
            $scope.thongKeLyDo.pageSize = 20;
        }
        findReasonsForReturn($scope.thongKeLyDo);
    }


    $scope.exportTableToExcel1 = function () {
        // Thêm cột "Thứ Tự"
        let data = $scope.listTopSellingShoesInLastDays.map((item, index) => ({
            "STT": index + 1,
            "Mã Sản Phẩm": item.id,
            "Tên Sản Phẩm": item.ten,
            "Thương Hiệu": item.thuongHieu.ten,
            "Số Lượng Bán": item.soLuongThongKe
        }));

        // Tạo một workbook mới
        let workbook = XLSX.utils.book_new();

        // Tạo một worksheet mới
        let worksheet = XLSX.utils.json_to_sheet(data, {skipHeader: true, origin: "A3"});

        // Gộp ô từ A1 đến E1 cho tiêu đề
        worksheet['!merges'] = [{s: {r: 0, c: 0}, e: {r: 0, c: 4}}]; // Gộp ô từ A1 đến E1

        // Thêm và định dạng tiêu đề lớn
        let top = data.length;
        let title = "TOP " + top + " GIÀY BÁN CHẠY NHẤT";
        worksheet['A1'] = {v: title, t: 's', s: {font: {sz: 16, bold: true}, alignment: {horizontal: "center"}}};

        // Thêm tiêu đề cột vào dòng thứ 2
        let headers = ["STT", "Mã Sản Phẩm", "Tên", "Thương Hiệu", "Số Lượng Bán"];
        XLSX.utils.sheet_add_aoa(worksheet, [headers], {origin: "A2"});
        // Định dạng tiêu đề cột
        const headerRange = XLSX.utils.decode_range(worksheet['!ref']);
        for (let C = headerRange.s.c; C <= headerRange.e.c; ++C) {
            const address = XLSX.utils.encode_col(C) + "2";
            if (!worksheet[address]) continue;
            worksheet[address].s = {
                font: {bold: true},
                fill: {bgColor: {rgb: "FFFFAA00"}},
                alignment: {horizontal: "center", vertical: "center"}
            };
        }

        worksheet['!cols'] = [
            {wch: 5}, // Cột đầu tiên có chiều rộng 10
            {wch: 10}, // Cột thứ hai có chiều rộng 20
            {wch: 30}, // Cột thứ ba có chiều rộng 15
            {wch: 15}, // Cột thứ ba có chiều rộng 15
            {wch: 15}, // Cột thứ ba có chiều rộng 15
        ];


        XLSX.utils.book_append_sheet(workbook, worksheet, "Top_Selling_Shoes");
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "TopSellingShoes.xlsx");
    };

    $scope.exportTableToExcel2 = function () {
        // Thêm cột "Thứ Tự
        let data = $scope.listTopFavoritedShoes.map((item, index) => ({
            "STT": index + 1,
            "Mã Sản Phẩm": item.id,
            "Tên Sản Phẩm": item.ten,
            "Thương Hiệu": item.thuongHieu.ten,
            "Số Lượng yêu Thích": item.soLuongThongKe
        }));

        // Tạo một workbook mới
        let workbook = XLSX.utils.book_new();

        // Tạo một worksheet mới
        let worksheet = XLSX.utils.json_to_sheet(data, {skipHeader: true, origin: "A3"});

        // Gộp ô từ A1 đến E1 cho tiêu đề
        worksheet['!merges'] = [{s: {r: 0, c: 0}, e: {r: 0, c: 4}}]; // Gộp ô từ A1 đến E1

        // Thêm và định dạng tiêu đề lớn
        let top = data.length;
        let title = "TOP " + top + " GIÀY ĐƯỢC YÊU THÍCH";
        worksheet['A1'] = {v: title, t: 's', s: {font: {sz: 16, bold: true}, alignment: {horizontal: "center"}}};

        // Thêm tiêu đề cột vào dòng thứ 2
        let headers = ["STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Thương Hiệu", "Số Lượng Yêu Thích"];
        XLSX.utils.sheet_add_aoa(worksheet, [headers], {origin: "A2"});
        // Định dạng tiêu đề cột
        const headerRange = XLSX.utils.decode_range(worksheet['!ref']);
        for (let C = headerRange.s.c; C <= headerRange.e.c; ++C) {
            const address = XLSX.utils.encode_col(C) + "2";
            if (!worksheet[address]) continue;
            worksheet[address].s = {
                font: {bold: true},
                fill: {bgColor: {rgb: "FFFFAA00"}},
                alignment: {horizontal: "center", vertical: "center"}
            };
        }

        worksheet['!cols'] = [
            {wch: 5}, // Cột đầu tiên có chiều rộng 10
            {wch: 10}, // Cột thứ hai có chiều rộng 20
            {wch: 30}, // Cột thứ ba có chiều rộng 15
            {wch: 15}, // Cột thứ ba có chiều rộng 15
            {wch: 15}, // Cột thứ ba có chiều rộng 15
        ];


        XLSX.utils.book_append_sheet(workbook, worksheet, "Top_Favorited_Shoes");
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "TopFavoritedShoes.xlsx");
    };

    $scope.exportTableToExcel3 = function () {
        // Thêm cột "Thứ Tự
        let data = $scope.listTopSellingShoeVariantInLastDays.map((item, index) => ({
            "STT": index + 1,
            "Mã Sản Phẩm": item.id,
            "Tên Sản Phẩm": item.giayResponse.ten,
            "Màu Sắc": item.mauSac.ten,
            "Kích Thước": item.kichThuoc.ten,
            "Thương Hiệu": item.giayResponse.thuongHieu.ten,
            "Số Lượng yêu Thích": item.soLuongThongKe
        }));

        // Tạo một workbook mới
        let workbook = XLSX.utils.book_new();

        // Tạo một worksheet mới
        let worksheet = XLSX.utils.json_to_sheet(data, {skipHeader: true, origin: "A3"});

        // Gộp ô từ A1 đến E1 cho tiêu đề
        worksheet['!merges'] = [{s: {r: 0, c: 0}, e: {r: 0, c: 4}}]; // Gộp ô từ A1 đến E1

        // Thêm và định dạng tiêu đề lớn
        let top = data.length;
        let title = "TOP " + top + " BIẾN THỂ GIÀY BÁN CHẠY";
        worksheet['A1'] = {v: title, t: 's', s: {font: {sz: 16, bold: true}, alignment: {horizontal: "center"}}};

        // Thêm tiêu đề cột vào dòng thứ 2
        let headers = ["STT", "Mã Sản Phẩm", "Tên Sản Phẩm","Màu Sắc","Kích Thước", "Thương Hiệu", "Số Lượng Bán"];
        XLSX.utils.sheet_add_aoa(worksheet, [headers], {origin: "A2"});
        // Định dạng tiêu đề cột
        const headerRange = XLSX.utils.decode_range(worksheet['!ref']);
        for (let C = headerRange.s.c; C <= headerRange.e.c; ++C) {
            const address = XLSX.utils.encode_col(C) + "2";
            if (!worksheet[address]) continue;
            worksheet[address].s = {
                font: {bold: true},
                fill: {bgColor: {rgb: "FFFFAA00"}},
                alignment: {horizontal: "center", vertical: "center"}
            };
        }

        worksheet['!cols'] = [
            {wch: 5},
            {wch: 10},
            {wch: 30},
            {wch: 15},
            {wch: 15},
            {wch: 15},
            {wch: 15},
        ];


        XLSX.utils.book_append_sheet(workbook, worksheet, "Top_Selling_Shoe_Variant");
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "TopSellingShoeVariant.xlsx");
    };

    $scope.exportTableToExcel4 = function () {
        // Thêm cột "Thứ Tự
        let data = $scope.listTopCartVariants.map((item, index) => ({
            "STT": index + 1,
            "Mã Sản Phẩm": item.id,
            "Tên Sản Phẩm": item.giayResponse.ten,
            "Màu Sắc": item.mauSac.ten,
            "Kích Thước": item.kichThuoc.ten,
            "Thương Hiệu": item.giayResponse.thuongHieu.ten,
            "Số Lượng Quan Tâm": item.soLuongThongKe
        }));

        // Tạo một workbook mới
        let workbook = XLSX.utils.book_new();

        // Tạo một worksheet mới
        let worksheet = XLSX.utils.json_to_sheet(data, {skipHeader: true, origin: "A3"});

        // Gộp ô từ A1 đến E1 cho tiêu đề
        worksheet['!merges'] = [{s: {r: 0, c: 0}, e: {r: 0, c: 4}}]; // Gộp ô từ A1 đến E1

        // Thêm và định dạng tiêu đề lớn
        let top = data.length;
        let title = "TOP " + top + " BIẾN THỂ GIÀY ĐƯỢC QUAN TÂM";
        worksheet['A1'] = {v: title, t: 's', s: {font: {sz: 16, bold: true}, alignment: {horizontal: "center"}}};

        // Thêm tiêu đề cột vào dòng thứ 2
        let headers = ["STT", "Mã Sản Phẩm", "Tên Sản Phẩm","Màu Sắc","Kích Thước", "Thương Hiệu", "Số Quan Tâm"];
        XLSX.utils.sheet_add_aoa(worksheet, [headers], {origin: "A2"});
        // Định dạng tiêu đề cột
        const headerRange = XLSX.utils.decode_range(worksheet['!ref']);
        for (let C = headerRange.s.c; C <= headerRange.e.c; ++C) {
            const address = XLSX.utils.encode_col(C) + "2";
            if (!worksheet[address]) continue;
            worksheet[address].s = {
                font: {bold: true},
                fill: {bgColor: {rgb: "FFFFAA00"}},
                alignment: {horizontal: "center", vertical: "center"}
            };
        }

        worksheet['!cols'] = [
            {wch: 5},
            {wch: 10},
            {wch: 30},
            {wch: 15},
            {wch: 15},
            {wch: 15},
            {wch: 15},
        ];


        XLSX.utils.book_append_sheet(workbook, worksheet, "Top_Cart_Variants");
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "TopCartVariants.xlsx");
    };


    $scope.exportTableToExcel5 = function () {
        // Thêm cột "Thứ Tự
        let data = $scope.listTopVariantReturnRates.map((item, index) => ({
            "STT": index + 1,
            "Mã Sản Phẩm": item.id,
            "Tên Sản Phẩm": item.giayResponse.ten,
            "Màu Sắc": item.mauSac.ten,
            "Kích Thước": item.kichThuoc.ten,
            "Thương Hiệu": item.giayResponse.thuongHieu.ten,
            "Số Lượng Mua": item.soLuongMua,
            "Số Lượng Trả": item.soLuongTra,
            "Tỷ Lệ Trả": item.tyLeTra+"%"
        }));

        // Tạo một workbook mới
        let workbook = XLSX.utils.book_new();

        // Tạo một worksheet mới
        let worksheet = XLSX.utils.json_to_sheet(data, {skipHeader: true, origin: "A3"});

        // Gộp ô từ A1 đến E1 cho tiêu đề
        worksheet['!merges'] = [{s: {r: 0, c: 0}, e: {r: 0, c: 4}}]; // Gộp ô từ A1 đến E1

        // Thêm và định dạng tiêu đề lớn
        let top = data.length;
        let title = "TOP " + top + " BIẾN THỂ GIÀY KÁCH TRẢ";
        worksheet['A1'] = {v: title, t: 's', s: {font: {sz: 16, bold: true}, alignment: {horizontal: "center"}}};

        // Thêm tiêu đề cột vào dòng thứ 2
        let headers = ["STT", "Mã Sản Phẩm", "Tên Sản Phẩm","Màu Sắc","Kích Thước", "Thương Hiệu", "Số Lượng Mua","Số Lượng Trả","Tỷ Lệ Trả(%)"];
        XLSX.utils.sheet_add_aoa(worksheet, [headers], {origin: "A2"});
        // Định dạng tiêu đề cột
        const headerRange = XLSX.utils.decode_range(worksheet['!ref']);
        for (let C = headerRange.s.c; C <= headerRange.e.c; ++C) {
            const address = XLSX.utils.encode_col(C) + "2";
            if (!worksheet[address]) continue;
            worksheet[address].s = {
                font: {bold: true},
                fill: {bgColor: {rgb: "FFFFAA00"}},
                alignment: {horizontal: "center", vertical: "center"}
            };
        }

        worksheet['!cols'] = [
            {wch: 5},
            {wch: 10},
            {wch: 30},
            {wch: 15},
            {wch: 15},
            {wch: 15},
            {wch: 10},
            {wch: 10},
            {wch: 10},
        ];


        XLSX.utils.book_append_sheet(workbook, worksheet, "Top_Variant_Return_Rates");
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "TopVariantReturnRates.xlsx");
    };

    $scope.exportTableToExcel6 = function () {
        // Thêm cột "Thứ Tự
        let data = $scope.listTopReasonsForReturn.map((item, index) => ({
            "STT": index + 1,
            "Mã Lý Do": item.id,
            "Tên Lý Do": item.ten,
            "Số Lượng" : item.soLuongThongKe +"/"+item.soLuongYeuCauTra,
            "Tỷ Lệ Trả" : item.tyLe+"%"
        }));

        // Tạo một workbook mới
        let workbook = XLSX.utils.book_new();

        // Tạo một worksheet mới
        let worksheet = XLSX.utils.json_to_sheet(data, {skipHeader: true, origin: "A3"});

        // Gộp ô từ A1 đến E1 cho tiêu đề
        worksheet['!merges'] = [{s: {r: 0, c: 0}, e: {r: 0, c: 4}}]; // Gộp ô từ A1 đến E1

        // Thêm và định dạng tiêu đề lớn
        let top = data.length;
        let title = "TOP " + top + " Lý Do Trả Hàng";
        worksheet['A1'] = {v: title, t: 's', s: {font: {sz: 16, bold: true}, alignment: {horizontal: "center"}}};

        // Thêm tiêu đề cột vào dòng thứ 2
        let headers = ["STT", "Mã Lý Do", "Tên Lý Do", "Số Lượng","Tỷ Lệ(%)"];
        XLSX.utils.sheet_add_aoa(worksheet, [headers], {origin: "A2"});
        // Định dạng tiêu đề cột
        const headerRange = XLSX.utils.decode_range(worksheet['!ref']);
        for (let C = headerRange.s.c; C <= headerRange.e.c; ++C) {
            const address = XLSX.utils.encode_col(C) + "2";
            if (!worksheet[address]) continue;
            worksheet[address].s = {
                font: {bold: true},
                fill: {bgColor: {rgb: "FFFFAA00"}},
                alignment: {horizontal: "center", vertical: "center"}
            };
        }

        worksheet['!cols'] = [
            {wch: 5}, // Cột đầu tiên có chiều rộng 10
            {wch: 10}, // Cột thứ hai có chiều rộng 20
            {wch: 30}, // Cột thứ ba có chiều rộng 15
            {wch: 15}, // Cột thứ ba có chiều rộng 15
            {wch: 15}, // Cột thứ ba có chiều rộng 15
        ];


        XLSX.utils.book_append_sheet(workbook, worksheet, "Top_Reasons_ForReturn");
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "TopReasonsForReturn.xlsx");
    };

    function findTopSellingShoesInLastDays(thongKeRequest) {
        $http.post(host + '/rest/admin/thong-ke/top-giay-ban-chay', JSON.stringify(thongKeRequest))
            .then(function (response) {
                $scope.listTopSellingShoesInLastDays = response.data.content;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy top giày bán chạy thất bại!");
        });
    }

    function findTopSellingShoeVariantInLastDays(thongKeRequest) {
        $http.post(host + '/rest/admin/thong-ke/top-bien-the-ban-chay', JSON.stringify(thongKeRequest))
            .then(function (response) {
                $scope.listTopSellingShoeVariantInLastDays = response.data.content;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy top biến thể giày bán chạy thất bại!");
        });
    }

    function findTopFavoritedShoes(thongKeRequest) {
        $http.post(host + '/rest/admin/thong-ke/top-giay-yeu-thich', JSON.stringify(thongKeRequest))
            .then(function (response) {
                $scope.listTopFavoritedShoes = response.data.content;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy top giày được yêu thích thất bại!");
        });
    }

    function findTopCartVariants(thongKeRequest) {
        $http.post(host + '/rest/admin/thong-ke/top-giay-gio-hang', JSON.stringify(thongKeRequest))
            .then(function (response) {
                $scope.listTopCartVariants = response.data.content;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy top biến thể giày được quan tâm thất bại!");
        });
    }

    function findReasonsForReturn(thongKeRequest) {
        $http.post(host + '/rest/admin/thong-ke/top-ly-do', JSON.stringify(thongKeRequest))
            .then(function (response) {
                $scope.listTopReasonsForReturn = response.data.content;
                console.log($scope.listTopReasonsForReturn)
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy top biến thể giày được quan tâm thất bại!");
        });
    }

    function findVariantReturnRates(thongKeRequest) {
        $http.post(host + '/rest/admin/thong-ke/top-bien-the-ty-le-tra', JSON.stringify(thongKeRequest))
            .then(function (response) {
                $scope.listTopVariantReturnRates = response.data.content;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy top biến thể giày được quan tâm thất bại!");
        });
    }

});