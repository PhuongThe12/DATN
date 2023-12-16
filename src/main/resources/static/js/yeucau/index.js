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

    $scope.yeuCauSearch = {}, $scope.yeuCauSearch.pageSize = 5, $scope.textSearch = '', $scope.yeuCauSearch.pageSize = 6;

    $scope.$watch('curPage + numPerPage', function () {
        $scope.yeuCauSearch.currentPage = $scope.curPage;
        getData($scope.yeuCauSearch);
        getAllLyDo();
    });


    $scope.changeRadioYeuCau = function () {
        $scope.yeuCauSearch.trangThai = $scope.trangThai;
        getData($scope.yeuCauSearch);
    }

    $scope.searchYeuCau = function () {

        if ($scope.typeSearch == 1) {
            if (!isNaN($scope.textSearch)) {
                $scope.yeuCauSearch.idYeuCau = $scope.textSearch;
                $scope.typeSearch = '1';
                getData($scope.yeuCauSearch);
                $scope.resetTextSearch();
            } else {
                toastr["warning"]("Định dạng id không đúng!");
            }
        }

        if ($scope.typeSearch == 2) {
            if (!isNaN($scope.textSearch)) {
                $scope.yeuCauSearch.idNhanVien = $scope.textSearch;
                $scope.typeSearch = '2';
                getData($scope.yeuCauSearch);
                $scope.resetTextSearch();
            } else {
                toastr["warning"]("Cần nhập id nhân viên!");
            }
        }
        if ($scope.typeSearch == 3) {
            $scope.yeuCauSearch.tenKhachHang = $scope.textSearch;
            $scope.typeSearch = '3';
            getData($scope.yeuCauSearch);
            $scope.resetTextSearch();
        }

        if ($scope.typeSearch == 4) {
            var phoneRegex = /^[0-9]{10}$/; // Biểu thức chính quy cho số điện thoại 10 chữ số
            if (phoneRegex.test($scope.textSearch)) {
                $scope.yeuCauSearch.soDienThoaiKhachHang = $scope.textSearch;
                $scope.typeSearch = '4';
                console.log($scope.yeuCauSearch)
                getData($scope.yeuCauSearch);
            } else {
                toastr["warning"]("Định dạng số điện thoại không đúng!");
            }
        }

        if ($scope.typeSearch == 5 && $scope.searchText.trim().length > 0) {
            $scope.yeuCauSearch.email = $scope.textSearch;
            $scope.typeSearch = '5';
            getData($scope.yeuCauSearch);
            $scope.resetTextSearch();
        }

        if ($scope.searchText == '') {
            $scope.resetTextSearch();
            getData($scope.yeuCauSearch);
        }
    };
    // Hàm để định dạng ngày thành chuỗi "yyyy-MM-dd 00:00:00"
    function formatISOToJavaLocalDateTime(isoString) {
        // Chuyển đổi chuỗi ISO 8601 thành một định dạng có thể sử dụng trong Java
        // ví dụ: "2023-11-13T17:00:00.000Z" => "2023-11-13T17:00:00"
        const date = new Date(isoString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
    }

// JavaScript trong Controller của AngularJS
    $scope.searchByDate = function() {
        var ngayHienTai = new Date();
        ngayHienTai.setHours(0, 0, 0, 0); // Đặt thời gian về đầu ngày

        var ngayBatDau = new Date($scope.ngayBatDau);
        var ngayKetThuc = new Date($scope.ngayKetThuc);

        if ( ngayBatDau > ngayHienTai) {
            toastr["error"]("Ngày bắt đầu không được lớn hơn ngày hiện tại!");
            return;
        }

        if ( ngayKetThuc > ngayHienTai) {
            toastr["error"]("Ngày kết thúc không được lớn hơn ngày hiện tại!");
            return;
        }

        if (ngayBatDau > ngayKetThuc && $scope.ngayKetThuc && $scope.ngayBatDau) {
            toastr["error"]("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            return;
        }

        if (ngayKetThuc < ngayBatDau && $scope.ngayBatDau && $scope.ngayKetThuc) {
            toastr["error"]("Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
            return;
        }

        // Nếu không có lỗi, tiếp tục thực hiện tìm kiếm
        $scope.errorMessage = "";
        // Logic tìm kiếm ở đây

        if($scope.ngayBatDau && !$scope.ngayKetThuc){
            $scope.yeuCauSearch.ngayKetThuc = null;
            $scope.yeuCauSearch.ngayBatDau = formatISOToJavaLocalDateTime($scope.ngayBatDau)
            getData($scope.yeuCauSearch)
        } else if(!$scope.ngayBatDau && $scope.ngayKetThuc){
            $scope.yeuCauSearch.ngayBatDau = null;
            $scope.yeuCauSearch.ngayKetThuc = formatISOToJavaLocalDateTime($scope.ngayKetThuc)
            console.log($scope.yeuCauSearch)
            getData($scope.yeuCauSearch)
        }else if($scope.ngayBatDau && $scope.ngayKetThuc){
            $scope.yeuCauSearch.ngayBatDau = formatISOToJavaLocalDateTime($scope.ngayBatDau)
            $scope.yeuCauSearch.ngayKetThuc = formatISOToJavaLocalDateTime($scope.ngayKetThuc)
            getData($scope.yeuCauSearch)
        }else {
            $scope.yeuCauSearch.ngayBatDau = null;
            $scope.yeuCauSearch.ngayKetThuc = null;
            getData($scope.yeuCauSearch)
        }
    };


    $scope.resetTextSearch = function () {
        $scope.yeuCauSearch.idYeuCau = null;
        $scope.yeuCauSearch.idNhanVien = null;
        $scope.yeuCauSearch.tenKhachHang = null;
        $scope.yeuCauSearch.soDienThoaiKhachHang = null;
        $scope.yeuCauSearch.email = null;
    }

    function getData(yeuCauSearch) {
        $http.post(host + '/rest/admin/yeu-cau', JSON.stringify(yeuCauSearch))
            .then(function (response) {
                $scope.listYeuCau = response.data.content;
                $scope.typeSearch = "1";
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/list';
            });
    }

    $scope.lyDo = {};

    $scope.detailLyDo = function (lyDo) {
        $scope.lyDo = lyDo;
    }

    $scope.updateLyDo = function () {
        if ($scope.lyDo.ten === '' || $scope.lyDo.ten == null) {
            toastr["error"]("Bạn chưa điều tên lý do!");
            return;
        } else {
            updateLyDo($scope.lyDo);
            $('#staticBackdrop').modal('show');
            $scope.$apply();
            $scope.lyDo = null;
        }
    }

    $scope.addLyDo = function () {
        if ($scope.lyDo.ten === '' || $scope.lyDo.ten == null) {
            toastr["error"]("Bạn chưa điều tên lý do!");
            return;
        } else {
            $scope.lyDo.trangThai = $scope.lyDo.trangThai == null ? 2 : $scope.lyDo.trangThai;
            addLyDo($scope.lyDo);
            $scope.$apply();
            $scope.lyDo = null;
        }
    }


    $scope.deletelyDo = function () {
        $scope.lyDo.trangThai = 0;
        updateLyDo($scope.lyDo);
        $scope.$apply();
        $('#staticBackdrop').modal('show');
        $scope.lyDo = null;
    }

    function getAllLyDo() {
        $scope.isLoading = true;
        $http.get(host + '/rest/admin/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("" + error.data.ten);
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
            $scope.isLoading = false;
        });
    }

    function updateLyDo(lyDo) {
        $scope.isLoading = true;
        $http.put(host + '/rest/admin/ly-do/update', JSON.stringify(lyDo))
            .then(function (response) {
                $scope.isLoading = false;
                if (response.data == true) {
                    toastr["success"]("Đã cập nhật lý do!");
                }
            }).catch(function (error) {
            toastr["error"]("" + error.data.ten);
            $scope.isLoading = false;
        });
    }

    function addLyDo(lyDo) {
        $scope.isLoading = true;
        $http.post(host + '/rest/admin/ly-do/add', JSON.stringify(lyDo))
            .then(function (response) {
                $scope.isLoading = false;
                if (response.data == true) {
                    toastr["success"]("Đã thêm mới lý do!");
                }
            }).catch(function (error) {
            toastr["error"]("" + error.data.ten);
            $scope.isLoading = false;
        });
    }
});

app.controller("updateYeuCauController", function ($scope, $http, $routeParams, $location) {
    const idYeuCau = $routeParams.id;
    $scope.listYeuCauChiTiet = [];
    $scope.isLoading = true;
    $scope.tongTienHangTra = 0, $scope.tongTienDoiHang = 0, $scope.tongTienHangDoi = 0, $scope.tongTienGiamGia = 0, $scope.tongTienThanhToan = 0, $scope.phiVanChuyen = 0, $scope.soLuongDoi = 0;

    $http.get(host + '/rest/admin/yeu-cau-chi-tiet/list/' + idYeuCau)
        .then(function (response) {
            $scope.listYeuCauChiTietResponse = response.data;
            $scope.yeuCau = response.data[0].yeuCau;
            $scope.hoaDon = $scope.yeuCau.hoaDon;
            getAllLyDo();
            tinhTongTienHangTra();
            tinhTongTienHangDoi();
            getNhanVien($scope.yeuCau);
            console.log($scope.yeuCau)
            $scope.isLoading = false;
        })
        .catch(function (error) {
            console.log(error)
            toastr["error"]("Lấy dữ liệu thất bại 1");
            $scope.isLoading = false;
        });


    function tinhTongTienThanhToan() {
        $scope.tongTienThanhToan = 0;
        let tongTienThanhToan = $scope.tongTienHangTra - (($scope.tongTienHangDoi - $scope.tongTienGiamGia) + $scope.phiVanChuyen);
        $scope.tongTienThanhToan = tongTienThanhToan > 0 ? tongTienThanhToan : Math.abs(tongTienThanhToan);
    }

    function tinhTienShip() {
        $scope.phiVanChuyen = 0;
        if ($scope.soLuongDoi == 0) {
            $scope.phiVanChuyen = 0;
            tinhTongTienThanhToan();
            return;
        }
        let soDonHang;
        if ($scope.soLuongDoi % 30 === 0) {
            soDonHang = $scope.soLuongDoi / 30;
        } else {
            soDonHang = ($scope.soLuongDoi / 30) + 1;
        }
        $scope.phiVanChuyen = Math.round(soDonHang * $scope.yeuCau.phiShip);
        tinhTongTienThanhToan();
    }


    function tinhTongTienGiam() {
        $scope.tongTienGiamGia = 0;
        $scope.listYeuCauChiTietResponse.forEach((item) => {
            if (item.trangThai == 0) {
                let tienGiam = item.tienGiam;
                $scope.tongTienGiamGia += tienGiam;
            }
        });
        tinhTongTienThanhToan();
    }

    function tinhTongTienHangDoi() {
        $scope.tongTienHangDoi = 0;
        $scope.soLuongDoi = 0;
        $scope.listYeuCauChiTietResponse.forEach((item) => {
            if (item.bienTheGiay.id && item.trangThai == 0) {
                let giaBan = item.bienTheGiay.giaBan;
                let tienGiam = item.tienGiam;
                $scope.tongTienGiamGia += tienGiam;
                $scope.tongTienHangDoi += giaBan;
                $scope.soLuongDoi += 1;
            }
        });
        tinhTongTienGiam();
        tinhTienShip();
    }


    function tinhTongTienHangTra() {
        $scope.tongTienHangTra = 0;
        $scope.listYeuCauChiTietResponse.forEach((item) => {
            if (item.trangThai != 1) {
                let tienTraHang = item.thanhTien;
                $scope.tongTienHangTra += tienTraHang;
            }
        });
        tinhTongTienThanhToan();
    };

    $scope.confirmDeleteGiayTra = function (item) {
        if (confirm('Bạn có chắc chắn muốn hủy trả giày này không bạn không thể hoàn tác thao tác này?')) {
            let index = $scope.listYeuCauChiTietResponse.indexOf(item);
            if (index !== -1) {
                $scope.listYeuCauChiTietResponse[index].loaiYeuCauChiTiet = 3;
                $scope.listYeuCauChiTietResponse[index].trangThai = 1;
                tinhTongTienHangDoi();
                tinhTongTienHangTra();
            }
        }
    };


    $scope.confirmDeleteGiayDoi = function (item) {
        if (confirm('Bạn có chắc chắn muốn hủy đổi giày này không? Bạn không thể hoàn tác thao tác này.')) {
            let index = $scope.listYeuCauChiTietResponse.indexOf(item);
            if (index !== -1) {
                $scope.listYeuCauChiTietResponse[index].loaiYeuCauChiTiet = 2;
                $scope.listYeuCauChiTietResponse[index].trangThai = 2;
                tinhTongTienHangDoi();
            }
        }
    };


    $scope.addLyDoView = function (index, yeuCauChiTiet) {
        $scope.yeuCauChiTiet = yeuCauChiTiet;
        $scope.selectedIndex = index;
    };

    $scope.updateLyDo = function () {
        if ($scope.selectedIndex !== null) {
            $scope.listYeuCauChiTiet[$scope.selectedIndex] = $scope.yeuCauChiTiet;
        }
    }

    $scope.checkComfirm = function () {
        $scope.mapSanPhamDoi = new Map();
        $scope.listYeuCauChiTietResponse.forEach(item => {
            if (item.trangThai == 0) {
                if (item.bienTheGiay.id) {
                    // Kiểm tra xem ID đã tồn tại trong map chưa
                    if (!$scope.mapSanPhamDoi.has(item.bienTheGiay.id)) {
                        // Nếu chưa, thêm mới với soLuongDoi là 1
                        $scope.mapSanPhamDoi.set(item.bienTheGiay.id, {
                            bienTheGiay: item.bienTheGiay, soLuongDoi: 1
                        });
                    } else {
                        // Nếu đã tồn tại, tăng soLuongDoi lên 1 đơn vị
                        let currentValue = $scope.mapSanPhamDoi.get(item.bienTheGiay.id);
                        currentValue.soLuongDoi++;
                        $scope.mapSanPhamDoi.set(item.bienTheGiay.id, currentValue);
                    }
                }
            }
        });

        $scope.listSanPhamDoi = Array.from($scope.mapSanPhamDoi, ([key, value]) => ({key, value}));


        if ($scope.listSanPhamDoi.length > 0) {
            $scope.checkDoi = true; // Khởi tạo giá trị mặc định

            for (let item of $scope.listSanPhamDoi) {
                let sanPhamDoi = item.value; // Truy cập trực tiếp vào value của mỗi item
                if (sanPhamDoi.bienTheGiay.soLuong < sanPhamDoi.soLuongDoi) {
                    $scope.checkDoi = false;
                    break; // Thoát khỏi vòng lặp nếu điều kiện không thỏa mãn
                }
            }
        } else {
            $scope.checkDoi = true; // Nếu không có sản phẩm nào, checkDoi nên là false
        }

    };


    $scope.xacNhanYeuCau = function () {
        let listYeuCauChiTiet = $scope.listYeuCauChiTietResponse.map(item => {
            const yeuCauChiTietRequest = {
                id: item.id,
                hoaDonChiTiet: item.hoaDonChiTiet?.id, // Sử dụng optional chaining nếu có thể
                bienTheGiay: item.bienTheGiay?.id, // Sử dụng optional chaining
                bienTheGiayTra: item.hoaDonChiTiet?.bienTheGiay?.id, // Sử dụng optional chaining
                lyDo: item.lyDo?.id, // Sử dụng optional chaining
                trangThai: item.trangThai,
                ghiChu: item.ghiChu,
                loaiYeuCauChiTiet: item.loaiYeuCauChiTiet,
                tinhTrangSanPham: !!item.tinhTrangSanPham, // Chuyển đổi sang boolean
                tienGiam: item.tienGiam,//tiền giảm giá của sản phẩm đổi lúc đổi
                thanhTien: item.thanhTien // tiền hoàn của sản phẩm trả
            };
            return yeuCauChiTietRequest;
        });

        $scope.yeuCau.trangThai = 2;//trạng thái đã xác nhận
        $scope.yeuCau.hoaDon = $scope.hoaDon.id;
        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTiet;

        $http.put(host + '/rest/admin/yeu-cau/confirm', JSON.stringify($scope.yeuCau))
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Đã xác nhận yêu cầu!");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Xác nhận yêu cầu thất bại!");
                if (error.status === 400) {
                    $scope.addYeuCauForm.hoaDon.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }


    $scope.luuYeuCau = function () {
        let listYeuCauChiTietUpdate = $scope.listYeuCauChiTietResponse.map(item => {
            let yeuCauChiTietRequest = {
                id: item.id,
                ghiChu: item.ghiChu,
                lyDo: item.lyDo.id,
                tinhTrangSanPham: item.tinhTrangSanPham == null ? false : item.tinhTrangSanPham,
                bienTheGiayTra: item.hoaDonChiTiet.bienTheGiay.id,
            };
            return yeuCauChiTietRequest;
        });
        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTietUpdate;



        // Gửi yêu cầu POST đến máy chủ Spring Boot
        $http.put(host + '/rest/admin/yeu-cau/update', JSON.stringify($scope.yeuCau))
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Đã lưu yêu cầu!");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Xác nhận yêu cầu thất bại! 3");
                if (error.status === 400) {
                    $scope.addYeuCauForm.hoaDon.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }


    $scope.tuChoiNhanYeuCau = function () {
        let listYeuCauChiTietUncomfim = $scope.listYeuCauChiTietResponse.map(item => {
            let yeuCauChiTietRequest = {
                id: item.id,
                hoaDonChiTiet: item.hoaDonChiTiet?.id,
                ghiChu: item.ghiChu,
                lyDo: item.lyDo.id,
                loaiYeuCauChiTiet: 3,
                tinhTrangSanPham: item.tinhTrangSanPham
            };
            return yeuCauChiTietRequest;
        });

        $scope.yeuCau.trangThai = 0;//trạng thái bbị hủy
        $scope.yeuCau.hoaDon = $scope.hoaDon.id;

        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTietUncomfim;

        // Gửi yêu cầu POST đến máy chủ Spring Boot
        $http.put(host + '/rest/admin/yeu-cau/unconfirm', JSON.stringify($scope.yeuCau))
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Đã từ chối yêu cầu!");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Từ chối yêu cầu thất bại!");
                if (error.status === 400) {
                    $scope.errors = error.data;
                }
            });
    }


    function getAllLyDo() {
        $scope.isLoading = true;
        $http.get(host + '/rest/admin/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Không lấy được danh sách lý do!");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
            $scope.isLoading = false;
        });
    }

    function getNhanVien(yeuCau) {

        if (yeuCau.nguoiThucHien) {
            $http.get(host + '/rest/admin/nhan-vien/' + yeuCau.nguoiThucHien)
                .then(function (response) {
                    $scope.nguoiThucHien = response.data;
                    $scope.isLoading = false;
                })
                .catch(function (error) {
                    console.log(error)
                    toastr["error"]("Không tìm thấy người thực hiện");
                });
        }

        if (yeuCau.nguoiSua) {
            $http.get(host + '/rest/admin/nhan-vien/' + yeuCau.nguoiSua)
                .then(function (response) {
                    $scope.nguoiSua = response.data;
                    $scope.isLoading = false;
                })
                .catch(function (error) {
                    console.log(error)
                    toastr["error"]("Không tìm thấy người sửa!");
                });
        }

        if (yeuCau.nguoiTao) {
            $http.get(host + '/rest/admin/nhan-vien/' + yeuCau.nguoiTao)
                .then(function (response) {
                    $scope.nguoiTao = response.data;
                    $scope.isLoading = false;
                })
                .catch(function (error) {
                    console.log(error)
                    toastr["error"]("Không tìm thấy người sửa!");
                });
        }
    }

    function getSoLuongTon(id) {
        return $http.get(host + '/rest/admin/giay/' + id + '/so-luong')
            .then(function (response) {
                return response.data; // Đảm bảo trả về dữ liệu từ phản hồi
            })
            .catch(function (error) {
                toastr["error"]("Không thể lấy số lượng tồn!");
                return Promise.reject(error); // Trả về một Promise bị từ chối
            });
    }

});


app.controller("addYeuCauController", function ($scope, $http, $location, $routeParams) {
    const idHoaDon = $routeParams.id;

    $scope.mapYeuCauChiTiet = new Map(), $scope.mapYeuCauChiTietSaved = new Map();
    $scope.listHoaDonChiTiet = [], $scope.listLyDo = [];
    $scope.hoaDon = {};
    $scope.tongTienHangTra = 0;
    $scope.yeuCau = {};

    getListHoaDonChiTiet(idHoaDon);
    getListLyDo();

    function tinhTongTienHangTra() {
        $scope.tongTienHangTra = 0;

        $scope.mapYeuCauChiTiet.forEach((item) => {
            let hoaDonChiTiet = item.hoaDonChiTiet;
            let donGia = hoaDonChiTiet.donGia;
            let soLuong = 1;
            let phanTramGiam = $scope.hoaDon.phanTramGiam / 100;
            let tienTraHang = (donGia * soLuong) - (donGia * soLuong * phanTramGiam);
            $scope.tongTienHangTra += tienTraHang;
        });
    };


    $scope.traHang = function (baseId, hoaDonChiTiet) {

        if (hoaDonChiTiet.soLuongTra <= 0) {
            toastr.error('Bạn chưa chọn số lượng muốn trả!');
            return;
        }
        for (let i = 1; i <= hoaDonChiTiet.soLuongTra; i++) {
            let key = baseId+'.'+i;
            if (!$scope.mapYeuCauChiTiet.has(key)) {
                let valueCopy = angular.copy(hoaDonChiTiet);
                valueCopy.soLuongTra = 1;

                $scope.mapYeuCauChiTiet.set(key, {
                    hoaDonChiTiet: valueCopy, // value1
                    lyDo: null,              // value2
                    ghiChu: ''               // value3
                });
            }
        }
        $scope.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value}));
        tinhTongTienHangTra();
    }


    $scope.subtraction = function (hoaDonChiTiet) {
        if (hoaDonChiTiet.soLuongTra > 0) {
            hoaDonChiTiet.soLuongTra = hoaDonChiTiet.soLuongTra - 1;
        }
    }
    $scope.summation = function (hoaDonChiTiet) {
        if (hoaDonChiTiet.soLuongTra < hoaDonChiTiet.soLuongDuocTra) {
            hoaDonChiTiet.soLuongTra = hoaDonChiTiet.soLuongTra + 1;
            $scope.tongSoLuongTra += 1;
        }
    }

    $scope.deleteGiayTra = function (yeuCauChiTiet) {
        if ($scope.mapYeuCauChiTiet.has(yeuCauChiTiet.key)) {
            $scope.mapYeuCauChiTiet.delete(yeuCauChiTiet.key);
            $scope.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value}));
            tinhTongTienHangTra();
        } else {
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
        }
    }


    $scope.addLyDoView = function (yeuCauChiTiet) {
        $scope.yeuCauChiTiet = yeuCauChiTiet;
        if ($scope.mapYeuCauChiTiet.get(yeuCauChiTiet.key)) {
            $scope.yeuCauChiTiet.lyDo = $scope.mapYeuCauChiTiet.get(yeuCauChiTiet.key).lyDo;
            $scope.yeuCauChiTiet.ghiChu = $scope.mapYeuCauChiTiet.get(yeuCauChiTiet.key).ghiChu;
        }
    }


    $scope.updateLyDo = function () {
        if ($scope.mapYeuCauChiTiet.has($scope.yeuCauChiTiet.key)) {
            let yeuCauChiTiet = $scope.mapYeuCauChiTiet.get($scope.yeuCauChiTiet.key);

            yeuCauChiTiet.lyDo = $scope.yeuCauChiTiet.lyDo;
            yeuCauChiTiet.ghiChu = $scope.yeuCauChiTiet.ghiChu;

            $scope.mapYeuCauChiTiet.set($scope.yeuCauChiTiet.key, yeuCauChiTiet);
            $scope.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value}));
            toastr["success"]("Lưu lý do thành công!");
        } else {
            toastr["error"]("Không tìm thấy dữ liệu!");
        }

    }

    $scope.taoYeuCau = function () {
        let coPhanTuKhongHopLe = false;

        $scope.mapYeuCauChiTiet.forEach((yeuCauChiTiet, key) => {
            if (!yeuCauChiTiet.lyDo || !yeuCauChiTiet.ghiChu) {
                toastr["error"]("Bạn chưa điền đủ thông tin lý do hoặc ghi chú cho sản phẩm có mã: " + key);
                coPhanTuKhongHopLe = true;
            }
        });

        if ($scope.mapYeuCauChiTiet.size === 0) {
            toastr["error"]("Vui lòng thêm sản phẩm trả!");
            coPhanTuKhongHopLe = true;
        }

        // Nếu tất cả các entry đều hợp lệ, thực hiện xử lý tiếp theo
        if (!coPhanTuKhongHopLe) {
            $scope.mapYeuCauChiTiet.forEach((yeuCauChiTiet, key) => {

                let tienHoanKhach = yeuCauChiTiet.hoaDonChiTiet.donGia - (yeuCauChiTiet.hoaDonChiTiet.donGia * ($scope.hoaDon.phanTramGiam / 100));

                $scope.mapYeuCauChiTietSaved.set(key, {
                    hoaDonChiTiet: yeuCauChiTiet.hoaDonChiTiet.id,
                    bienTheGiayTra: yeuCauChiTiet.hoaDonChiTiet.bienTheGiay.id,
                    lyDo: yeuCauChiTiet.lyDo,
                    trangThai: 0,
                    ghiChu: yeuCauChiTiet.ghiChu,
                    loaiYeuCauChiTiet: 2,
                    tinhTrangSanPham: false,
                    tienKhachTra: 0,
                    thanhTien: tienHoanKhach ? tienHoanKhach : 0,
                });
            });


            $scope.yeuCau.hoaDon = $scope.hoaDon.id;
            $scope.yeuCau.trangThai = 1;
            $scope.yeuCau.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTietSaved.values());
            console.log(JSON.stringify($scope.yeuCau))
            $http.post(host + '/rest/admin/yeu-cau/tra-hang-nhanh', JSON.stringify($scope.yeuCau))
                .then(function (response) {
                    if (response.status === 200) {
                        toastr["success"]("Đã gửi yêu cầu đổi/trả thành công!");
                    }
                    $location.path("/home");
                })
                .catch(function (error) {
                    toastr["error"]("Gửi yêu cầu đổi/trả thất bại!");
                    if (error.status === 400) {
                        $scope.addYeuCauForm.hoaDon.$dirty = false;
                        $scope.errors = error.data;
                    }
                });
        }
    };


    function getListHoaDonChiTiet(id) {
        $scope.isLoading = true;
        $http.get(host + '/rest/admin/yeu-cau/hoa-don/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                console.log($scope.hoaDon)
                $scope.listHoaDonChiTiet = response.data.listHoaDonChiTiet;
                $scope.nguoiTao = getNhanVien();
                $scope.isLoading = false;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Chuyển hóa đơn thất bại. Vui lòng thử lại");
            $scope.isLoading = false;
        });
    }

    function getListLyDo() {
        $http.get(host + '/rest/admin/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function getNhanVien(idNhanVien) {
        $http.get(host + '/rest/admin/nhan-vien/', idNhanVien)
            .then(function (response) {
                return response.data;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }


});


app.controller("selectedHoaDonController", function ($scope, $http, $location, $routeParams) {
    $scope.isLoading = true, $scope.searchText;
    $scope.hoaDonSearch = {},$scope.tongTienThanhToan = "0";
    $scope.hoaDonSearch.currentPage = 1, $scope.hoaDonSearch.itemsPerPage = 5, $scope.hoaDonSearch.pageSize = 6, $scope.maxSize = 5, $scope.curPage = 1;
    $scope.typeSearch = "1", $scope.textSearch = '';

    $scope.$watch('curPage + numPerPage', function () {
        $scope.hoaDonSearch.currentPage = $scope.curPage;
        getData($scope.hoaDonSearch);
        console.log($scope.hoaDonSearch)

    });

    $scope.searchHoaDon = function () {
        if ($scope.typeSearch == 1) {
            if (!isNaN($scope.textSearch)) {
                $scope.hoaDonSearch.idHoaDon = $scope.textSearch;
                $scope.typeSearch = '1';
                getData($scope.hoaDonSearch);
            } else {
                toastr["warning"]("Định dạng id hóa đơn không đúng!");
            }
        }

        if ($scope.typeSearch == 2) {
            if (!isNaN($scope.textSearch)) {
                $scope.hoaDonSearch.idNhanVien = $scope.textSearch;
                $scope.typeSearch = '2';
                getData($scope.hoaDonSearch);
            } else {
                toastr["warning"]("Cần nhập id nhân viên!");
            }
        }
        if ($scope.typeSearch == 3) {
            $scope.hoaDonSearch.tenKhachHang = $scope.textSearch;
            $scope.typeSearch = '3';
            getData($scope.hoaDonSearch);
        }

        if ($scope.typeSearch == 4) {
            var phoneRegex = /^[0-9]{10}$/; // Biểu thức chính quy cho số điện thoại 10 chữ số
            if (phoneRegex.test($scope.textSearch)) {
                $scope.hoaDonSearch.soDienThoaiKhachHang = $scope.textSearch;
                $scope.typeSearch = '4';
                getData($scope.hoaDonSearch);
            } else {
                toastr["warning"]("Định dạng số điện thoại không đúng!");
            }
        }

        if ($scope.typeSearch == 5 && $scope.searchText.trim().length > 0) {
            $scope.hoaDonSearch.email = $scope.textSearch;
            $scope.typeSearch = '5';
            getData($scope.hoaDonSearch);
        }

        if ($scope.searchText == '') {
            $scope.resetTextSearch();
            getData($scope.hoaDonSearch);
        }
    }

    // Hàm để định dạng ngày thành chuỗi "yyyy-MM-dd 00:00:00"
    function formatISOToJavaLocalDateTime(isoString) {
        // Chuyển đổi chuỗi ISO 8601 thành một định dạng có thể sử dụng trong Java
        // ví dụ: "2023-11-13T17:00:00.000Z" => "2023-11-13T17:00:00"
        const date = new Date(isoString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
    }

// JavaScript trong Controller của AngularJS
    $scope.searchByDate = function() {
        var ngayHienTai = new Date();
        ngayHienTai.setHours(0, 0, 0, 0); // Đặt thời gian về đầu ngày

        var ngayBatDau = new Date($scope.ngayBatDau);
        var ngayKetThuc = new Date($scope.ngayKetThuc);

        if ( ngayBatDau > ngayHienTai) {
            toastr["error"]("Ngày bắt đầu không được lớn hơn ngày hiện tại!");
            return;
        }

        if ( ngayKetThuc > ngayHienTai) {
            toastr["error"]("Ngày kết thúc không được lớn hơn ngày hiện tại!");
            return;
        }

        if (ngayBatDau > ngayKetThuc && $scope.ngayKetThuc && $scope.ngayBatDau) {
            toastr["error"]("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            return;
        }

        if (ngayKetThuc < ngayBatDau && $scope.ngayBatDau && $scope.ngayKetThuc) {
            toastr["error"]("Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
            return;
        }

        // Nếu không có lỗi, tiếp tục thực hiện tìm kiếm
        $scope.errorMessage = "";
        // Logic tìm kiếm ở đây

        if($scope.ngayBatDau && !$scope.ngayKetThuc){
            $scope.hoaDonSearch.ngayBatDau = formatISOToJavaLocalDateTime($scope.ngayBatDau)
            $scope.hoaDonSearch.ngayKetThuc = null;
            getData($scope.hoaDonSearch)
        } else if(!$scope.ngayBatDau && $scope.ngayKetThuc){
            $scope.hoaDonSearch.ngayBatDau = null;
            $scope.hoaDonSearch.ngayKetThuc = formatISOToJavaLocalDateTime($scope.ngayKetThuc)
            getData($scope.hoaDonSearch)
        }else if($scope.ngayBatDau && $scope.ngayKetThuc){
            $scope.hoaDonSearch.ngayBatDau = formatISOToJavaLocalDateTime($scope.ngayBatDau)
            $scope.hoaDonSearch.ngayKetThuc = formatISOToJavaLocalDateTime($scope.ngayKetThuc)
            console.log( $scope.hoaDonSearch.ngayBatDau)
            console.log( $scope.hoaDonSearch.ngayKetThuc)
            getData($scope.hoaDonSearch)
        }else {
            $scope.hoaDonSearch.ngayBatDau = null;
            $scope.hoaDonSearch.ngayKetThuc = null;
            getData($scope.hoaDonSearch)
        }
    };


    $scope.resetTextSearch = function () {
        $scope.hoaDonSearch.idHoaDon = null;
        $scope.hoaDonSearch.idNhanVien = null;
        $scope.hoaDonSearch.tenKhachHang = null;
        $scope.hoaDonSearch.soDienThoaiKhachHang = null;
        $scope.hoaDonSearch.email = null;
        getData($scope.hoaDonSearch)
    }

    $scope.changeRadioLoaiHoaDon = function (loaiHoaDon) {
        $scope.hoaDonSearch.loaiHoaDon = loaiHoaDon;
        getData($scope.hoaDonSearch);
    }

    $scope.reset = function () {
        $scope.hoaDonSearch.idHoaDon = null, $scope.hoaDonSearch.khachHang = null, $scope.hoaDonSearch.nhanVien = null, $scope.hoaDonSearch.soDienThoaiKhacHang = null, $scope.hoaDonSearch.email = null, $scope.hoaDonSearch.loaiHoaDon = null, $scope.hoaDonSearch.ngayBatDau = null, $scope.hoaDonSearch.ngayKetThuc = null, $scope.hoaDonSearch.giaTu = null, $scope.hoaDonSearch.giaDen = null
        getData($scope.hoaDonSearch);
        $scope.textSearch = '';
    }

    $scope.searchByTotalMoney = function () {
        if($scope.tongTienThanhToan == 0){
            $scope.hoaDonSearch.tongThanhToanMin = null;
            $scope.hoaDonSearch.tongThanhToanMax = null;
        } else if ($scope.tongTienThanhToan == 1) {
            $scope.hoaDonSearch.tongThanhToanMax = 500000;
        } else if ($scope.tongTienThanhToan == 2) {
            $scope.hoaDonSearch.tongThanhToanMin = 500001;
            $scope.hoaDonSearch.tongThanhToanMax = 1000000;
        } else if ($scope.tongTienThanhToan == 3) {
            $scope.hoaDonSearch.tongThanhToanMin = 1000001;
            $scope.hoaDonSearch.tongThanhToanMax = 1500000;
        } else if ($scope.tongTienThanhToan == 4) {
            $scope.hoaDonSearch.tongThanhToanMin = 1500001;
            $scope.hoaDonSearch.tongThanhToanMax = 2000000;
        } else if ($scope.tongTienThanhToan == 5) {
            $scope.hoaDonSearch.tongThanhToanMin = 2000001;
            $scope.hoaDonSearch.tongThanhToanMax = 250000;
        } else if ($scope.tongTienThanhToan == 6) {
            $scope.hoaDonSearch.tongThanhToanMin = 250001;
            $scope.hoaDonSearch.tongThanhToanMax = 3000000;
        } else if ($scope.tongTienThanhToan == 7) {
            $scope.hoaDonSearch.tongThanhToanMin = 3000001;
        }
        getData($scope.hoaDonSearch);
    }


    document.getElementById('flexSwitchCheckDefault').addEventListener('change', function () {
        var label = document.getElementById('switchLabel');
        if (this.checked) {
            label.textContent = 'Onlline';
            $scope.hoaDonSearch.kenhBan = 1;
            getData($scope.hoaDonSearch);
        } else {
            label.textContent = 'Tại Quầy';
            $scope.hoaDonSearch.kenhBan = 2;
            getData($scope.hoaDonSearch);
        }
    });


    function getData(hoaDonSearch) {
        console.log(JSON.stringify(hoaDonSearch))
        $scope.isLoading = true;
        $http.post(host + '/rest/user/hoa-don/yeu-cau', JSON.stringify(hoaDonSearch))
            .then(function (response) {
                $scope.listHoaDon = response.data.content;
                console.log( $scope.listHoaDon)
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
                $scope.isLoading = false;
            });
    }



    $scope.selectedHoaDon = function (hoaDon) {
        $scope.hoaDonSelected = hoaDon;
    }

    $scope.taoYeuCau = function () {
        if ($scope.hoaDonSelected && $scope.hoaDonSelected.id) {
            $location.path('/add/' + $scope.hoaDonSelected.id);
        } else {
            // Thông báo cho người dùng chọn một hóa đơn
            toastr["error"]("Bạn phải chọn một hóa đơn để tạo yêu cầu");
        }
    };

});


app.filter('formatToVND', function () {
    return function (number) {
        if (number !== undefined && number !== null) {
            // Sử dụng toLocaleString để định dạng số, nhưng thêm " ₫" vào cuối
            return number.toLocaleString('vi-VN') + ' ₫';
        } else {
            return '0 ₫'; // Hoặc giá trị mặc định khác
        }
    };
});

app.filter('formatDate', function () {
    return function (isoDateString) {
        if (isoDateString) {
            const inputDate = new Date(isoDateString);
            const options = {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
            };
            const formattedDate = inputDate.toLocaleDateString('en-GB', options);
            return formattedDate.replace(',', '');
        } else {
            return '-'; // Hoặc giá trị mặc định khác nếu không có ngày
        }
    };
});
