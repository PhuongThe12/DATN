var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/yeucau/views/list.html', controller: 'yeuCauListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/yeucau/views/detail.html', controller: 'detailYeuCauController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/yeucau/views/update.html', controller: 'updateYeuCauController'
    }).when("/add/:id", {
        templateUrl: '/pages/admin/yeucau/views/add.html', controller: 'addYeuCauController'
    }).when("/hoa-don", {
        templateUrl: '/pages/admin/yeucau/views/selectHoaDon.html', controller: 'selectedHoaDonController'
    })
        .otherwise({redirectTo: '/list'});
});


app.controller("yeuCauListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1, $scope.itemsPerPage = 5, $scope.maxSize = 5;
    let searchText;
    // Lấy giá trị ngày bắt đầu
    let startDate;
    // Lấy giá trị ngày kết thúc
    let endDate;


    // Datepicker ngày bắt đầu
    const fpStart = flatpickr('#ngayBatDau', {
        dateFormat: 'd/m/Y', maxDate: new Date(), allowInput: true, // Cho phép nhập giá trị thay vì chỉ chọn từ calendar
        clickOpens: true, // Cho phép click vào input để mở calendar
        onClose: function (selectedDates) {
            // Nếu chọn ngày bắt đầu sau ngày kết thúc
            if (selectedDates[0] > fpEnd.selectedDates[0]) {
                // Cập nhật lại ngày kết thúc = ngày bắt đầu
                fpEnd.setDate(selectedDates[0]);
            }
            // Giới hạn ngày kết thúc không thể trước ngày bắt đầu
            fpEnd.set('minDate', selectedDates[0]);
        }, onChange: function (selectedDates, dateStr, instance) {
            // Gọi hàm searchByDate khi ngày bắt đầu thay đổi
            $scope.searchByDate();
        }, onReady: function () {
            this.input.addEventListener('blur', function () {
                // Gọi hàm searchByDate khi trường nhập liệu mất focus
                $scope.searchByDate();
            });
        }
    });

// Datepicker ngày kết thúc
    const fpEnd = flatpickr('#ngayKetThuc', {
        dateFormat: 'd/m/Y', maxDate: new Date(), allowInput: true, // Cho phép nhập giá trị thay vì chỉ chọn từ calendar
        clickOpens: true, // Cho phép click vào input để mở calendar
        onChange: function (selectedDates, dateStr, instance) {
            // Gọi hàm searchByDate khi ngày kết thúc thay đổi
            $scope.searchByDate();
        }, onReady: function () {
            this.input.addEventListener('blur', function () {
                // Gọi hàm searchByDate khi trường nhập liệu mất focus
                $scope.searchByDate();
            });
        }
    });


// Hàm để định dạng ngày thành chuỗi "yyyy-MM-dd 00:00:00"
    function formatDateToISO(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

// Hàm để định dạng ngày thành chuỗi "dd-MM-yyyy hh:mm:ss"
    $scope.formatDateView = function (isoDateString) {
        const inputDate = new Date(isoDateString);

        const options = {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit',
        };

        const formattedDate = inputDate.toLocaleDateString('en-GB', options);

        // Loại bỏ dấu phẩy
        return formattedDate.replace(',', '');
    };


    $scope.searchByDate = function () {
        startDate = fpStart.selectedDates[0];
        endDate = fpEnd.selectedDates[0];
        console.log(startDate)
        console.log(endDate)
        if (startDate === null && endDate === null) {
            toastr["error"]("Vui lòng chọn ngày bắt đầu hoặc ngày kết thúc");
            return;
        }
        getData(1, startDate, endDate);
        console.log(startDate)
        console.log(endDate)
    };

    $scope.changeRadio = function (loaiYeuCau) {
        $scope.loaiYeuCau = loaiYeuCau;
        getData(1);
    }

    $scope.search = function () {
        if (!$scope.searchText) {
            toastr["error"]("Vui lòng nhập tên muốn tìm kiếm");
            return;
        }
        searchText = $scope.searchText;
        getData(1, searchText);
    };

    function getData(currentPage, startDate, endDate, searchText) {
        let apiUrl = host + '/admin/rest/yeu-cau?page=' + currentPage;
        if (startDate != null) {
            console.log("1")
            apiUrl += '&ngayBatDau=' + formatDateToISO(startDate);
        }
        if (endDate != null) {
            apiUrl += '&ngayKetThuc=' + formatDateToISO(endDate);
        }
        if (searchText) {
            apiUrl += '&searchText=' + searchText;
        }
        if ($scope.loaiYeuCau == 1) {
            apiUrl += '&loaiYeuCau=' + 1;
        } else if ($scope.loaiYeuCau == 2) {
            apiUrl += '&loaiYeuCau=' + 2;
        }

        console.log(apiUrl);

        $http.get(apiUrl)
            .then(function (response) {
                $scope.listYeuCau = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});


app.controller("detailYeuCauController", function ($scope, $http, $location, $routeParams) {

    $scope.formatDateView = function (isoDateString) {
        const inputDate = new Date(isoDateString);

        const options = {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit',
        };

        const formattedDate = inputDate.toLocaleDateString('en-GB', options);

        // Loại bỏ dấu phẩy
        return formattedDate.replace(',', '');
    };

    const id = $routeParams.id;
    $http.get(host + '/admin/rest/yeu-cau/' + id)
        .then(function (response) {
            $scope.yeuCau = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("updateYeuCauController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.danhSachNhanVien = [{id: 1, ten: "Quân"}, {id: 2, ten: "Tuấn"}, {id: 3, ten: "Chiến"}, {
        id: 4, ten: "Phương"
    }, {id: 5, ten: "Hoàng"}, {id: 6, ten: "Cường"}];

    $scope.yeuCau = {}; // Khởi tạo đối tượng yeuCau trước

    $http.get(host + '/admin/rest/yeu-cau/' + id)
        .then(function (response) {
            $scope.yeuCau = response.data;
            console.log($scope.yeuCau)
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });

    $scope.updateYeuCau = function () {
        if ($scope.updateYeuCauForm.$invalid) {
            return;
        }

        $http.put(host + '/admin/rest/yeu-cau/' + id, $scope.yeuCau)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công");
                } else {
                    toastr["error"]("Cập nhật thất bại. Lỗi bất định");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Cập nhật thất bại");
                if (error.status === 400) {
                    $scope.updateYeuCauForm.hoaDon.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    };
});

app.controller("addYeuCauController", function ($scope, $http, $location,$routeParams) {


    const formSanPhamDaMua = document.getElementById("formSanPhamDaMua");
    const formSanPhamThayThe = document.getElementById("formSanPhamThayThe");
    const formChonSanPhamThayThe = document.getElementById("formChonSanPhamThayThe");
    const formYeuCau = document.getElementById("formYeuCau");
    const formBienTheGiay = document.getElementById("formBienTheGiay");
    const idHoaDon = $routeParams.id;

    $scope.listHoaDonChiTiet = [];
    $scope.listGiay = [];

    $scope.mapYeuCauChiTiet = new Map();
    $scope.arrayForRepeat = [];

    $scope.mapSanPhamThayThe = new Map();
    $scope.arrayForRepeat1 = [];

    $scope.listHoaDonChiTietDoi = [];


    $scope.yeuCau = {
        nguoiThucHien: "1", loaiYeuCau: "1", trangThai: "1",
    };
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.danhSachNhanVien = [{"id": 1, "ten": "Quân"}, {"id": 2, "ten": "Tuấn"}, {"id": 3, "ten": "Chiến"}, {
        "id": 4, "ten": "Phương"
    }, {"id": 5, "ten": "Hoàng"}, {"id": 6, "ten": "Cường"}];

    $scope.listHoaDon = [{
        "id": 3,
        "khachHang": 1,
        "nhanVien": 4,
        "kenhBan": 1,
        "loaiHoaDon": 1,
        "tongTien": 200000,
        "trangThai": 1,
        "ngayNhan": "15/04/2023"
    }, {
        "id": 4,
        "khachHang": 2,
        "nhanVien": 5,
        "kenhBan": 1,
        "loaiHoaDon": 2,
        "tongTien": 300000,
        "trangThai": 1,
        "ngayNhan": "15/05/2023"
    }, {
        "id": 5,
        "khachHang": 3,
        "nhanVien": 6,
        "kenhBan": 1,
        "loaiHoaDon": 1,
        "tongTien": 400000,
        "trangThai": 1,
        "ngayNhan": "15/06/2023"

    },];


    const getAllChiTietHoaDon = [
        {
            "id": 1,
            "hoaDon": 3,
            "bienTheGiay": [{
                "id": 1,
                "giay": [{
                    "id": 1, "ten": "Giày Vans"
                }],
                "soLuong": 5,
                "soLuongLoi": 5,
                "kichThuoc": 28,
                "mauSac": "đỏ",
                "giaBan": 100000,
            }],
            "donGia": 200000,
            "soLuong": 1,
            "soLuongTra": 0,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 2,
            "hoaDon": 3,
            "bienTheGiay": [{
                "id": 2,
                "giay": [{
                    "id": 2, "ten": "Giày Nike"
                }],
                "soLuong": 5,
                "soLuongLoi": 5,
                "kichThuoc": 28,
                "mauSac": "đỏ",
                "giaBan": 100000,
            }],
            "donGia": 300000,
            "soLuong": 2,
            "soLuongTra": 0,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 3,
            "hoaDon": 4,
            "bienTheGiay": [{
                "id": 3,
                "giay": [{
                    "id": 3, "ten": "Giày Vans"
                }],
                "soLuong": 5,
                "soLuongLoi": 5,
                "kichThuoc": 28,
                "mauSac": "Ghi",
                "giaBan": 300000,
            }],
            "donGia": 300000,
            "soLuong": 1,
            "soLuongTra": 0,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 4,
            "hoaDon": 5,
            "bienTheGiay": [{
                "id": 4,
                "giay": [{
                    "id": 4, "ten": "Giày Aidaphat"
                }],
                "soLuong": 5,
                "soLuongLoi": 5,
                "kichThuoc": 28,
                "mauSac": "Hồng",
                "giaBan": 200000,
            }],
            "donGia": 300000,
            "soLuong": 2,
            "soLuongTra": 0,
            "trangThai": 1,
            "moTag": "hihi",
        },
    ];

    function getAllListGiay(){
        $http.get(host + '/admin/rest/giay/get-all-active')
            .then(function (response) {
                $scope.listGiay = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });
    }


    function filterChiTietByHoaDonId(idHoaDon) {
        return getAllChiTietHoaDon.filter(function (chiTiet) {
            //Tìm những Hóa Đơn Chi tiết có ID = với ID của hóa đơn được chọn
            return chiTiet.hoaDon === idHoaDon;
        });
    };

    $scope.listHoaDonChiTiet = filterChiTietByHoaDonId(parseInt(idHoaDon));

    $scope.addToMapForMultipleItems = function (baseId, hoaDonChiTiet, soLuongTra) {
        // Lặp qua số lượng và thêm vào map với khóa và giá trị phù hợp
        for (var i = 1; i <= soLuongTra; i++) {
            var key = baseId + "." + i;
            var valueCopy = angular.copy(hoaDonChiTiet);
            valueCopy.soLuongTra = 1; // Set số lượng trả mỗi bản ghi là 1

            // Thêm vào map
            $scope.mapYeuCauChiTiet.set(key, valueCopy);
        }
        // Chuyển đổi map thành mảng sau khi thêm tất cả các phần tử vào map
        $scope.arrayForRepeat = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value: [value]}));
    };


    $scope.doiSanPham = function (hoaDonChiTiet) {
        if (hoaDonChiTiet.soLuongTra > 0) {
            // Lấy ID duy nhất của hoá đơn chi tiết
            var baseId = hoaDonChiTiet.id;

            // Thêm hoặc cập nhật hoá đơn chi tiết vào Map
            $scope.addToMapForMultipleItems(baseId, hoaDonChiTiet, hoaDonChiTiet.soLuongTra);

            // Hiển thị form sản phẩm thay thế
            formSanPhamThayThe.style.display = "block";
        } else {
            toastr["error"]("Bạn phải chọn số lượng trả lớn hơn 0.");
        }
    };


    $scope.chonSanPham = function (key) {
        $scope.keyGiayTra = key;
        formSanPhamThayThe.style.display = "none";
        formYeuCau.style.display = "none";
        formChonSanPhamThayThe.style.display = "block";
        getAllListGiay();
    };

    $scope.chonGiayDoi = function (giay) {
        $scope.giayDoi = giay;
        $scope.updateFilteredResults();
        var listMauSac = [];
        var listKichThuoc = [];

        giay.lstBienTheGiay.forEach(function(bienTheGiay) {
            // Kiểm tra màu sắc
            if (bienTheGiay.mauSac) {
                // Tìm xem màu sắc đã tồn tại trong mảng hay chưa
                var existingMauSac = listMauSac.find(function(m) {
                    return m.id === bienTheGiay.mauSac.id;
                });
                // Nếu chưa tồn tại, thêm vào mảng
                if (!existingMauSac) {
                    listMauSac.push(bienTheGiay.mauSac);
                }
            }

            // Kiểm tra kích thước
            if (bienTheGiay.kichthuoc) {
                // Tìm xem kích thước đã tồn tại trong mảng hay chưa
                var existingKichThuoc = listKichThuoc.find(function(k) {
                    return k.id === bienTheGiay.kichthuoc.id;
                });
                // Nếu chưa tồn tại, thêm vào mảng
                if (!existingKichThuoc) {
                    listKichThuoc.push(bienTheGiay.kichthuoc);
                }
            }
        });


        $scope.listMauSac = listMauSac;
        $scope.listKichThuoc = listKichThuoc;

        console.log(listMauSac);
        console.log(listKichThuoc);
        // // Ẩn và hiển thị các form phù hợp
        formChonSanPhamThayThe.style.display = "none";
        formBienTheGiay.style.display = "block";

    };


    $scope.updateFilteredResults = function() {
        // Khởi tạo danh sách lọc với tất cả các biến thể
        var filteredList = $scope.giayDoi.lstBienTheGiay;

        // Nếu người dùng đã chọn màu sắc, lọc theo màu sắc
        if ($scope.selectedMauSac) {
            filteredList = filteredList.filter(function(bienTheGiay) {
                return bienTheGiay.mauSac.id === $scope.selectedMauSac.id;
            });
        }

        // Nếu người dùng đã chọn kích thước, lọc theo kích thước
        if ($scope.selectedKichThuoc) {
            filteredList = filteredList.filter(function(bienTheGiay) {
                return bienTheGiay.kichthuoc.id === $scope.selectedKichThuoc.id;
            });
        }

        // Cập nhật danh sách biến thể giày được lọc
        $scope.filteredBienTheGiay = filteredList;
    };

    $scope.getMauSac = function() {
        // Cập nhật danh sách lọc
        $scope.updateFilteredResults();
    };


    $scope.getKichThuoc = function() {
        // Cập nhật danh sách lọc
        $scope.updateFilteredResults();
    };


    $scope.chonBienTheGiayDoi = function (bienTheGiayDoi) {
        // // Ẩn và hiển thị các form phù hợp
        formBienTheGiay.style.display = "none";
        formSanPhamThayThe.style.display = "block";
        formYeuCau.style.display = "block";
        console.log(bienTheGiayDoi)
        // Thêm sản phẩm thay thế vào map với id của sản phẩm đang đổi
        // và cập nhật array cho ng-repeat
        $scope.mapSanPhamThayThe.set($scope.keyGiayTra, bienTheGiayDoi);
        $scope.arrayForRepeat1 = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
    };



    $scope.checkSoLuongTra = function () {
        var tongSoLuongDoiMoi = $scope.tongSoLuongDoi();

        angular.forEach($scope.arrayForRepeat, function (parentItem) {
            var soLuongTraHienTai = parentItem.value[0].soLuongTra;

            if (tongSoLuongDoiMoi > soLuongTraHienTai) {
                toastr.error("Tổng số lượng đổi không được lớn hơn số lượng trả cho sản phẩm có mã " + parentItem.key);
                // Tìm và giảm số lượng đổi cho item cuối cùng đã đổi để đưa tổng số lượng về mức cho phép
                for (let i = $scope.arrayForRepeat1.length - 1; i >= 0; i--) {
                    if ($scope.arrayForRepeat1[i].key === parentItem.key && $scope.arrayForRepeat1[i].soLuongDoi > 0) {
                        $scope.arrayForRepeat1[i].soLuongDoi--;
                        break;
                    }
                }
            } else if (tongSoLuongDoiMoi === soLuongTraHienTai) {
                toastr.warning("Tổng số lượng đổi đã đạt mức số lượng trả, không thể tăng thêm cho sản phẩm có mã " + parentItem.key);
            }
        });
    };

    $scope.tongSoLuongDoi = function () {
        var tongSoLuongDoiMoi = 0;
        angular.forEach($scope.arrayForRepeat1, function (item1) {
            if (!isNaN(item1.soLuongDoi)) {
                tongSoLuongDoiMoi += Number(item1.soLuongDoi);
            }
        });
        return tongSoLuongDoiMoi;
    };

    $scope.capNhatSoLuong = function (index) {
        var item1 = $scope.arrayForRepeat1[index];
        if (!item1.soLuongDoiCu) {
            item1.soLuongDoiCu = item1.soLuongDoi; // khởi tạo nếu chưa có
        }

        var tongSoLuongDoiMoi = $scope.tongSoLuongDoi();
        var soLuongTraCuaSanPham = $scope.arrayForRepeat.find(item => item.key === item1.key).value[0].soLuongTra;

        if (tongSoLuongDoiMoi > soLuongTraCuaSanPham) {
            toastr.error("Số lượng đổi không được lớn hơn số lượng trả");
            item1.soLuongDoi = item1.soLuongDoiCu; // phục hồi lại giá trị cũ nếu vượt quá
        } else {
            item1.soLuongDoiCu = item1.soLuongDoi; // cập nhật lại giá trị cũ
        }
    };

    $scope.tongSoLuongDoi = function () {
        var tongSoLuongDoiMoi = 0;
        angular.forEach($scope.arrayForRepeat1, function (item1) {
            if (!isNaN(item1.soLuongDoi)) {
                tongSoLuongDoiMoi += Number(item1.soLuongDoi);
            }
        });
        return tongSoLuongDoiMoi;
    };


    $scope.combineMaps = function () {
        // Duyệt qua tất cả các entries trong mapYeuCauChiTiet
        $scope.mapYeuCauChiTiet.forEach((value, key) => {
            // Kiểm tra xem có sản phẩm thay thế nào có key tương ứng không
            if ($scope.mapSanPhamThayThe.has(key)) {
                // Nếu có, lấy thông tin sản phẩm thay thế
                var sanPhamThayThe = $scope.mapSanPhamThayThe.get(key);
                // Cập nhật value trong mapYeuCauChiTiet bằng cách thêm sản phẩm thay thế vào
                if (Array.isArray(value)) {
                    // Nếu value là một mảng, chúng ta có thể định giá trị value2
                    value.push(sanPhamThayThe);
                } else {
                    // Nếu value không phải là một mảng, chúng ta cần phải tạo một mảng mới
                    $scope.mapYeuCauChiTiet.set(key, [value, sanPhamThayThe]);
                }
            }
        });

        // Cập nhật arrayForRepeat để phản ánh sự thay đổi trên giao diện
        $scope.arrayForRepeat = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value}));
        console.log($scope.arrayForRepeat)
    };


    $scope.yeuCauCho = {};

    function getYeuCauByStatus() {
        $http.get(host + '/admin/rest/yeu-cau/get-one/by-status')
            .then(function (response) {
                $scope.yeuCauCho = response.data;
                console.log(response.data)
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        })
    };


    // $scope.addYeuCau = function () {
    //
    //     if ($scope.addYeuCauForm.$invalid) {
    //         return;
    //     }
    //     $scope.yeuCau.hoaDon = $scope.hoaDonSelected;
    //     console.log($scope.yeuCau.hoaDon);
    //     // Gửi yêu cầu POST đến máy chủ Spring Boot
    //     $http.post(host + '/admin/rest/yeu-cau/add', $scope.yeuCau)
    //         .then(function (response) {
    //             if (response.status === 200) {
    //                 toastr["success"]("Thêm thành công");
    //             }
    //             $location.path("/list");
    //         })
    //         .catch(function (error) {
    //             toastr["error"]("Thêm thất bại");
    //             if (error.status === 400) {
    //                 $scope.addYeuCauForm.hoaDon.$dirty = false;
    //                 $scope.errors = error.data;
    //             }
    //         });
    //
    // }

});

app.controller("selectedHoaDonController", function ($scope, $http, $location, $routeParams) {

    $scope.listHoaDon = [{
        "id": 3,
        "khachHang": 1,
        "nhanVien": 4,
        "kenhBan": 1,
        "loaiHoaDon": 1,
        "tongTien": 200000,
        "trangThai": 1,
        "ngayNhan": "15/04/2023"
    }, {
        "id": 4,
        "khachHang": 2,
        "nhanVien": 5,
        "kenhBan": 1,
        "loaiHoaDon": 2,
        "tongTien": 300000,
        "trangThai": 1,
        "ngayNhan": "15/05/2023"
    }, {
        "id": 5,
        "khachHang": 3,
        "nhanVien": 6,
        "kenhBan": 1,
        "loaiHoaDon": 1,
        "tongTien": 400000,
        "trangThai": 1,
        "ngayNhan": "15/06/2023"

    },];

    $scope.chonHoaDon = function (hoaDon){
        $scope.hoaDonSelected = hoaDon;
    }

    $scope.taoYeuCau = function() {
        if ($scope.hoaDonSelected && $scope.hoaDonSelected.id) {
            $location.path('/add/' + $scope.hoaDonSelected.id);
        } else {
            // Thông báo cho người dùng chọn một hóa đơn
            toastr["error"]("Bạn phải chọn một hóa đơn để tạo yêu cầu");
        }
    };

});