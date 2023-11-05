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
    }).when("/add", {
        templateUrl: '/pages/admin/yeucau/views/add.html', controller: 'addYeuCauController'
    })
        .otherwise({redirectTo: '/list'});
});

app.controller("addYeuCauController", function ($scope, $http, $location) {

    const formListHoaDon = document.getElementById("formListHoaDon");
    const formSanPhamDaMua = document.getElementById("formSanPhamDaMua");
    const formSanPhamThayThe = document.getElementById("formSanPhamThayThe");
    const formChonSanPhamThayThe = document.getElementById("formChonSanPhamThayThe");
    const formYeuCau = document.getElementById("formYeuCau");

    $scope.soLuongTra = 0;
    $scope.soLuongMua = 1;
    $scope.soLuongDoi = 1;

    $scope.listBienTheGiayDoi = [];

    $scope.yeuCau = {
        nguoiThucHien: "1",
        loaiYeuCau: "1",
        trangThai: "1",
    };
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.danhSachNhanVien = [
        {"id": 1, "ten": "Quân"},
        {"id": 2, "ten": "Tuấn"},
        {"id": 3, "ten": "Chiến"},
        {"id": 4, "ten": "Phương"},
        {"id": 5, "ten": "Hoàng"},
        {"id": 6, "ten": "Cường"}
    ];

    $scope.listHoaDon = [
        {
            "id": 1,
            "khachHang": "Tuấn",
            "nhanVien":"Quân",
            "kenhBan":1,
            "loaiHoaDon":1,
            "tongTien":1,
            "trangThai":1,
        },
        {
            "id": 2,
            "khachHang": "Tuấn",
            "nhanVien":"Phương",
            "kenhBan":1,
            "loaiHoaDon":1,
            "tongTien":1,
            "trangThai":1,
        },
        {
            "id": 3,
            "khachHang": "Tuấn",
            "nhanVien":"Cường",
            "kenhBan":1,
            "loaiHoaDon":1,
            "tongTien":1,
            "trangThai":1,
        },
    ];



    const getAllChiTietHoaDon = [
        {
            "id": 1,
            "idHoaDon": 1,
            "idBienTheGiay": [
                {
                    "id": 1,
                    "ten": "Giày vans triệu like",
                }
            ],
            "donGia": 200000,
            "soLuong": 1,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 2,
            "idHoaDon": 1,
            "idBienTheGiay": [
                {
                    "id": 2,
                    "ten": "Giày vans màu đỏ",
                }
            ],
            "donGia": 300000,
            "soLuong": 2,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 3,
            "idHoaDon": 2,
            "idBienTheGiay": [
                {
                    "id": 3,
                    "ten": "Giày vans hường",
                }
            ],
            "donGia": 200000,
            "soLuong": 1,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 4,
            "idHoaDon": 2,
            "idBienTheGiay": [
                {
                    "id": 2,
                    "ten": "Giày vans màu đỏ",
                }
            ],
            "donGia": 300000,
            "soLuong": 2,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 5,
            "idHoaDon": 3,
            "idBienTheGiay": [
                {
                    "id": 4,
                    "ten": "Giày like màu Bê Đê",
                }
            ],
            "donGia": 200000,
            "soLuong": 1,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 6,
            "idHoaDon": 3,
            "idBienTheGiay": [
                {
                    "id": 2,
                    "ten": "Giày vans màu đỏ",
                }
            ],
            "donGia": 300000,
            "soLuong": 2,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 7,
            "idHoaDon": 4,
            "idBienTheGiay": [
                {
                    "id": 4,
                    "ten": "Giày like màu Bê Đê",
                }
            ],
            "donGia": 200000,
            "soLuong": 1,
            "trangThai": 1,
            "moTag": "hihi",
        },
        {
            "id": 8,
            "idHoaDon": 4,
            "idBienTheGiay": [
                {
                    "id": 1,
                    "ten": "Giày vans triệu like",
                }
            ],
            "donGia": 300000,
            "soLuong": 2,
            "trangThai": 1,
            "moTag": "hihi",
        },
    ];


    $scope.listBienTheGiays = [
        {
            "id": 1,
            "ten": "Giày 1 điểm",
            "soLuong": 5,
            "giaBan": 100000,
        },        {
            "id": 2,
            "ten": "Giày 2 điểm",
            "soLuong": 5,
            "giaBan": 200000,
        },        {
            "id": 3,
            "ten": "Giày 3 điểm",
            "soLuong": 5,
            "giaBan": 300000,
        },        {
            "id": 4,
            "ten": "Giày 4 điểm",
            "soLuong": 5,
            "giaBan": 400000,
        },        {
            "id": 5,
            "ten": "Giày 5 điểm",
            "soLuong": 5,
            "giaBan": 500000,
        },
    ];

    $scope.chonHoaDon = function (hoaDon) {
        // Lưu đối tượng hóa đơn được chọn vào biến hoaDonSelected
        $scope.hoaDonSelected = hoaDon;
    };


    function filterChiTietByHoaDonId(hoaDonSelected) {
        return getAllChiTietHoaDon.filter(function (chiTiet) {
            return chiTiet.idHoaDon === hoaDonSelected.id;
        });
    }


///////////////////////////////////

    $scope.taoYeuCau = function () {
        // Kiểm tra xem đã chọn hóa đơn nào chưa
        if ($scope.hoaDonSelected) {
            formListHoaDon.style.display = "none";
            formSanPhamDaMua.style.display = "block";
            formSanPhamThayThe.style.display = "block";
            formYeuCau.style.display = "block";
            //truyền listHoaDonChiTiet vào form
            $scope.listHoaDonChiTiet = filterChiTietByHoaDonId($scope.hoaDonSelected);
        }else{
            toastr["error"]("Vui lòng chọn một hóa đơn");
        };
    };

    $scope.chonSanPhamDoi = function(){
        formSanPhamThayThe.style.display = "none";
        formYeuCau.style.display = "none";
        formChonSanPhamThayThe.style.display = "block";
    }

    $scope.chonGiayDoi = function(bienTheGiay){
        formChonSanPhamThayThe.style.display = "none";
        formSanPhamThayThe.style.display = "block";
        formYeuCau.style.display = "block";
        $scope.listBienTheGiayDoi.push(bienTheGiay);
    }

    $scope.addYeuCau = function () {
        if ($scope.addYeuCauForm.$invalid) {
            return;
        }
        // Gửi yêu cầu POST đến máy chủ Spring Boot
        $http.post(host + '/admin/rest/yeu-cau/add', $scope.yeuCau)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.addYeuCauForm.hoaDon.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

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

app.controller("yeuCauListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 5,
        $scope.maxSize = 5;
    let searchText;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Datepicker ngày bắt đầu
    const fpStart = flatpickr('#ngayBatDau', {
        dateFormat: 'd/m/Y',
        maxDate: new Date(),
        allowInput: true, // Cho phép nhập giá trị thay vì chỉ chọn từ calendar
        clickOpens: true, // Cho phép click vào input để mở calendar
        onClose: function (selectedDates) {
            // Nếu chọn ngày bắt đầu sau ngày kết thúc
            if (selectedDates[0] > fpEnd.selectedDates[0]) {
                // Cập nhật lại ngày kết thúc = ngày bắt đầu
                fpEnd.setDate(selectedDates[0]);
            }
            // Giới hạn ngày kết thúc không thể trước ngày bắt đầu
            fpEnd.set('minDate', selectedDates[0]);
        },
        onChange: function (selectedDates, dateStr, instance) {
            // Gọi hàm searchByDate khi ngày bắt đầu thay đổi
            $scope.searchByDate();
        },
        onReady: function () {
            this.input.addEventListener('blur', function () {
                // Gọi hàm searchByDate khi trường nhập liệu mất focus
                $scope.searchByDate();
            });
        }
    });

// Datepicker ngày kết thúc
    const fpEnd = flatpickr('#ngayKetThuc', {
        dateFormat: 'd/m/Y',
        maxDate: new Date(),
        allowInput: true, // Cho phép nhập giá trị thay vì chỉ chọn từ calendar
        clickOpens: true, // Cho phép click vào input để mở calendar
        onChange: function (selectedDates, dateStr, instance) {
            // Gọi hàm searchByDate khi ngày kết thúc thay đổi
            $scope.searchByDate();
        },
        onReady: function () {
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

// Lấy giá trị ngày bắt đầu
    let startDate;
// Lấy giá trị ngày kết thúc
    let endDate;
/////////////////////////////////////////////////////////////////////////////////////////////////////////


    $scope.searchByDate = function () {
        startDate = fpStart.selectedDates[0];
        endDate = fpEnd.selectedDates[0];
        console.log(startDate)
        console.log(endDate)
        if(startDate===null && endDate===null){
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

    function getData(currentPage,startDate,endDate,searchText) {
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

app.controller("updateYeuCauController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.danhSachNhanVien = [{id: 1, ten: "Quân"}, {id: 2, ten: "Tuấn"}, {id: 3, ten: "Chiến"}, {
        id: 4,
        ten: "Phương"
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

