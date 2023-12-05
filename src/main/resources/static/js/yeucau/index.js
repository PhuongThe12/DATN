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
    $scope.startDate, $scope.endDate, $scope.trangThai, $scope.searchTextYeuCau = '';

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage, $scope.startDate == null ? null : $scope.startDate, $scope.endDate == null ? null : $scope.endDate, $scope.searchTextYeuCau, $scope.trangThai);
        getAllLyDo()
    });

    document.addEventListener('DOMContentLoaded', function () {
        var ngayBatDau = document.getElementById('ngayBatDau');
        var ngayKetThuc = document.getElementById('ngayKetThuc');

        // Cài đặt ngày tối đa là ngày hiện tại
        var today = new Date().toISOString().split('T')[0];
        ngayBatDau.setAttribute('max', today);
        ngayKetThuc.setAttribute('max', today);

        ngayBatDau.addEventListener('change', function () {
            if (ngayBatDau.value > ngayKetThuc.value) {
                alert('Ngày bắt đầu không thể lớn hơn ngày kết thúc. Vui lòng chọn lại.');
                ngayBatDau.value = '';
            }
            sendDateToBackend(ngayBatDau.value, 'start');
        });

        ngayKetThuc.addEventListener('change', function () {
            if (ngayKetThuc.value < ngayBatDau.value) {
                alert('Ngày kết thúc không thể nhỏ hơn ngày bắt đầu. Vui lòng chọn lại.');
                ngayKetThuc.value = '';
            }
            sendDateToBackend(ngayKetThuc.value, 'end');
        });
    });

    function sendDateToBackend(dateValue, type) {
        if (dateValue === '') return; // Không gửi nếu ngày không hợp lệ
        var dateForBackend = new Date(dateValue).toISOString();
        console.log(`Sending ${type} date to backend: ${dateForBackend}`);
        // Gửi dữ liệu tới back-end ở đây
    }



    $scope.reset = function () {
        $scope.searchTextYeuCau = '',
            $scope.startDate = null,
            $scope.endDate = null,
            $scope.trangThai = null
    }

    $scope.searchByDate = function () {
        $scope.startDate = fpStart.selectedDates[0];
        $scope.endDate = fpEnd.selectedDates[0];
        if ($scope.startDate === null && $scope.endDate === null) {
            toastr["error"]("Vui lòng chọn ngày bắt đầu hoặc ngày kết thúc");
            return;
        }
        getData(1, $scope.startDate, $scope.endDate);
    };

    $scope.changeRadioYeuCau = function (trangThai) {
        if (trangThai == null) {
            $scope.reset();
            fpStart.setDate(null);
            fpEnd.setDate(null);
            getData(1);
        }
        $scope.trangThai = trangThai;
        getData(1, $scope.startDate, $scope.endDate, null, $scope.trangThai);
    }

    $scope.searchYeuCau = function () {
        if (!$scope.searchTextYeuCau) {
            getData(1);
        }
        getData(1, null, null, $scope.searchTextYeuCau, null);
    };


    function getData(currentPage, startDate, endDate, searchText, trangThai) {

        let apiUrl = host + '/rest/admin/yeu-cau?page=' + currentPage;
        if (startDate != null) {
            apiUrl += '&ngayBatDau=' + encodeURIComponent(formatISOToJavaLocalDateTime(startDate));
        }
        if (endDate != null) {
            apiUrl += '&ngayKetThuc=' + encodeURIComponent(formatISOToJavaLocalDateTime(endDate));
        }
        if (searchText) {
            apiUrl += '&searchText=' + encodeURIComponent(searchText);
        }
        if (trangThai) {
            apiUrl += '&trangThai=' + encodeURIComponent(trangThai);
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.listYeuCau = response.data.content;
                console.log($scope.listYeuCau)
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/list';
            });
    }

    $scope.lyDo = {};
    $scope.detailLyDo = function (lyDo){
        $scope.lyDo = lyDo;
    }

     $scope.updateLyDo = function () {
         if($scope.lyDo.ten === '' || $scope.lyDo.ten == null){
             toastr["error"]("Bạn chưa điều tên lý do!");
             return;
         }else {
             updateLyDo($scope.lyDo);
             toastr["success"]("Đã cập nhật lý do!");
             $('#staticBackdrop').modal('show');
             $scope.lyDo =null;
         }
     }

    $scope.addLyDo = function () {
        if($scope.lyDo.ten === '' || $scope.lyDo.ten == null){
            toastr["error"]("Bạn chưa điều tên lý do!");
            return;
        }else {
            $scope.lyDo.trangThai = $scope.lyDo.trangThai == null ? 2 : $scope.lyDo.trangThai;
            addLyDo($scope.lyDo);
            toastr["success"]("Đã thêm mới lý do!");
            $('#staticBackdrop').modal('show');
            $scope.lyDo =null;
        }
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

    function updateLyDo(lyDo) {
        $scope.isLoading = true;
        $http.put(host + '/rest/admin/ly-do/update',JSON.stringify(lyDo))
            .then(function (response) {
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"](""+error.data);
            $scope.isLoading = false;
        });
    }

    function addLyDo(lyDo) {
        $scope.isLoading = true;
        $http.post(host + '/rest/admin/ly-do/update',JSON.stringify(lyDo))
            .then(function (response) {
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"](""+error.data);
            $scope.isLoading = false;
        });
    }
});


app.controller("updateYeuCauController", function ($scope, $http, $routeParams, $location) {
    const idYeuCau = $routeParams.id;
    $scope.listYeuCauChiTiet = [];
    $scope.isLoading = true;
    $scope.tongTienHangTra = 0, $scope.tongTienGiamGia = 0, $scope.tongTienHangDoi = 0, $scope.tongTienThanhToan = 0,$scope.phiVanChuyen = 0;
    $scope.soLuong = 0;

    $http.get(host + '/rest/admin/yeu-cau-chi-tiet/list/' + idYeuCau)
        .then(function (response) {
            $scope.listYeuCauChiTietResponse = response.data;
            $scope.yeuCau = response.data[0].yeuCau;
            console.log($scope.yeuCau)
            $scope.hoaDon = $scope.yeuCau.hoaDon;
            $scope.tinhTongTienHangTra();
            tinhTienShip();
            if ($scope.yeuCau.nguoiThucHien != null) {
                getNhanVien($scope.yeuCau.nguoiThucHien);
            }
            $scope.isLoading = false;
        })
        .catch(function (error) {
            console.log(error)
            toastr["error"]("Lấy dữ liệu thất bại 1");
            $scope.isLoading = false;
        });

    $scope.$watch('tongTienHangDoi', function (value) {
        tinhTienShip();
    });

    function tinhTienShip() {
        $scope.phiVanChuyen = 0;
        if ($scope.soLuong <= 0) {
            return;
        }
        let soDonHang = 0;
        if ($scope.soLuong % 30 === 0) {
            soDonHang = $scope.soLuong / 30;
        } else {
            soDonHang = ($scope.soLuong / 30) + 1;
        }
        $scope.phiVanChuyen = Math.round(soDonHang * $scope.yeuCau.phiShip);
    }

    $scope.tinhTongTienThanhToan = function () {
        let tienThanhToan = $scope.tongTienHangTra - $scope.tongTienHangDoi;
        $scope.tongTienThanhToan = tienThanhToan > 0 ? tienThanhToan : Math.abs(tienThanhToan);
    }

    $scope.tinhTongTienHangDoi = function () {
        $scope.tongTienHangDoi = 0;
        $scope.tongTienGiamGia = 0;
        $scope.soLuong = 0;
        $scope.listYeuCauChiTietResponse.forEach((item) => {
            if(item.bienTheGiay.id != null){
                $scope.soLuong +=1;
            }
            if (item.trangThai === 1 || item.trangThai === 4) {
                if (item.bienTheGiay.id != null) {
                    let donGia = item.bienTheGiay.giaBan;
                    let tienHang = donGia - item.tienGiam;
                    $scope.tongTienHangDoi += tienHang;
                    $scope.tongTienGiamGia += item.tienGiam;
                }
            }
        })
        $scope.tinhTongTienThanhToan();
    }

    $scope.tinhTongTienHangTra = function () {
        $scope.tongTienHangTra = 0;
        $scope.listYeuCauChiTietResponse.forEach((item) => {
            let donGia = item.hoaDonChiTiet.donGia;
            let phanTramGiam = $scope.hoaDon.phanTramGiam / 100;
            let tienHang = donGia - (donGia * phanTramGiam);
            $scope.tongTienHangTra += tienHang;
        })
        $scope.tinhTongTienHangDoi();

    }

    $scope.confirmDeleteGiayTra = function (item) {
        if (confirm('Bạn có chắc chắn muốn hủy trả giày này không bạn không thể hoàn tác thao tác này?')) {
            let index = $scope.listYeuCauChiTietResponse.indexOf(item);
            if (index !== -1) {
                $scope.listYeuCauChiTietResponse[index].loaiYeuCauChiTiet = 3;
                $scope.listYeuCauChiTietResponse[index].trangThai = 5;
                $scope.tinhTongTienHangTra();
                $scope.soLuong = $scope.soLuong - 1;
            }
        }
    };


    $scope.confirmDeleteGiayDoi = function (item) {
        if (confirm('Bạn có chắc chắn muốn hủy đổi giày này không? Bạn không thể hoàn tác thao tác này.')) {
            let index = $scope.listYeuCauChiTietResponse.indexOf(item);
            if (index !== -1) {
                $scope.listYeuCauChiTietResponse[index].loaiYeuCauChiTiet = 2;
                $scope.listYeuCauChiTietResponse[index].trangThai = 2;
                $scope.listYeuCauChiTietResponse[index].isDeleted = true; // Thêm thuộc tính isDeleted
                $scope.tinhTongTienHangDoi();
                $scope.soLuong = $scope.soLuong - 1;
            }
        }
    };


    $scope.lyDoChiTiet = function (item) {
        getAllLyDo();
        $scope.yeuCauChiTiet = item;
        $scope.lyDo = "" + item.lyDo.id;
        $scope.ghiChu = item.ghiChu;
        $scope.tinhTrangSanPham = item.tinhTrangSanPham == null ? false : item.tinhTrangSanPham;
    }


    $scope.updateLyDo = function () {
        let foundLyDo = $scope.listLyDo.find(lyDo => lyDo.id == $scope.lyDo);
        let foundIndex = $scope.listYeuCauChiTietResponse.findIndex(yeuCau => yeuCau.id === $scope.yeuCauChiTiet.id);

        if (foundIndex !== -1) {
            $scope.listYeuCauChiTietResponse[foundIndex].lyDo = foundLyDo;
            $scope.listYeuCauChiTietResponse[foundIndex].ghiChu = $scope.ghiChu;
            $scope.listYeuCauChiTietResponse[foundIndex].tinhTrangSanPham = $scope.tinhTrangSanPham;
        }
    }

    $scope.checkComfirm = function () {

        $scope.mapSanPhamDoi = new Map();

        $scope.listYeuCauChiTietResponse.forEach(item => {
            let sanPhamDoi = item.bienTheGiay;
            if (sanPhamDoi.id) {
                // Kiểm tra xem ID đã tồn tại trong map chưa
                if (!$scope.mapSanPhamDoi.has(sanPhamDoi.id)) {
                    // Nếu chưa, thêm mới với soLuongDoi là 1
                    $scope.mapSanPhamDoi.set(sanPhamDoi.id, {
                        soLuongDoi: 1
                    });
                } else {
                    // Nếu đã tồn tại, tăng soLuongDoi lên 1 đơn vị
                    let currentValue = $scope.mapSanPhamDoi.get(sanPhamDoi.id);
                    currentValue.soLuongDoi++;
                    $scope.mapSanPhamDoi.set(sanPhamDoi.id, currentValue);
                }
            }
        });

        let promises = [];
        let check = false;
        // Tạo một mảng của các Promise
        $scope.mapSanPhamDoi.forEach((value, key) => {
            let promise = getSoLuongTon(key).then(soLuongTon => {
                if (value.soLuongDoi > soLuongTon) {
                    // Thông báo hoặc xử lý ở đây
                    check = true;
                    return {id: key, status: "not enough", required: value.soLuongDoi, available: soLuongTon};
                } else {
                    return {id: key, status: "sufficient", required: value.soLuongDoi, available: soLuongTon};
                }
            });
            promises.push(promise);
        });

        // Xử lý khi tất cả Promise hoàn thành
        Promise.all(promises).then(results => {

            if (!check) {
                if (confirm('Xác nhận yêu cầu và tạo hóa đơn Đổi/Trả')) {
                    xacNhanYeuCau();
                }
            }

            results.forEach(result => {
                if (result.status === "not enough") {
                    // alert(`Số lượng tồn của bienTheGiay với ID ${result.id} không đủ. Yêu cầu: ${result.required}, Có sẵn: ${result.available}`);
                    $('#modalCheckSoLuongTon').modal('show');
                    toastr["error"]("Số lượng tồn kho " + result.id + " không đủ!");
                }
            });
        }).catch(error => {
            console.error("Có lỗi khi kiểm tra số lượng tồn:", error);
        });
    };


    function xacNhanYeuCau() {
        let listYeuCauChiTiet = $scope.listYeuCauChiTietResponse.map(item => {
            let yeuCauChiTietRequest = {
                id: item.id,
                hoaDonChiTiet: item.hoaDonChiTiet.id,
                bienTheGiay: item.bienTheGiay ? item.bienTheGiay.id : null,
                bienTheGiayTra: item.hoaDonChiTiet.bienTheGiay.id,
                loaiYeuCauChiTiet: item.loaiYeuCauChiTiet,
                lyDo: item.lyDo.id,
                trangThai: item.trangThai,
                tinhTrangSanPham: item.tinhTrangSanPham == null ? false : item.tinhTrangSanPham,
                ghiChu: item.ghiChu,
            };
            return yeuCauChiTietRequest;
        });
        $scope.yeuCau.trangThai = 2;
        $scope.yeuCau.hoaDon = $scope.hoaDon.id;
        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTiet;

        console.log($scope.yeuCau)
        // Gửi yêu cầu POST đến máy chủ Spring Boot
        $http.put(host + '/rest/admin/yeu-cau/confirm', JSON.stringify($scope.yeuCau))
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Đã xác nhận yêu cầu!");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Xác nhận yêu cầu thất bại! 2");
                if (error.status === 400) {
                    $scope.addYeuCauForm.hoaDon.$dirty = false;
                    $scope.errors = error.data;
                }
            });
        console.log(JSON.stringify($scope.yeuCau))
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
        $scope.yeuCau.nguoiThucHien = 4;
        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTietUpdate;

        // Gửi yêu cầu POST đến máy chủ Spring Boot
        $http.put(host + '/rest/admin/yeu-cau/change', JSON.stringify($scope.yeuCau))
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
                bienTheGiay: item.bienTheGiay ? item.bienTheGiay.id : null,
                loaiYeuCauChiTiet: 3,
                trangThai: 5,
                tinhTrangSanPham: item.tinhTrangSanPham
            };
            return yeuCauChiTietRequest;
        });
        $scope.yeuCau.hoaDon = $scope.hoaDon.id;
        $scope.yeuCau.nguoiThucHien = 4;
        $scope.yeuCau.trangThai = 3;
        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTietUncomfim;

        console.log($scope.yeuCau)
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

    $scope.quayVe = function () {
        window.location.href = feHost + '/list';
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

    function getNhanVien(id) {
        $scope.isLoading = true;
        $http.get(host + '/rest/admin/nhan-vien/' + id)
            .then(function (response) {
                $scope.nhanVien = response.data;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Không tìm thấy nhân viên!");
                $scope.isLoading = false;
            });
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

//các list
    $scope.listYeuCauChiTiet = [], $scope.listGiay = [], $scope.arrayForRepeat = [], $scope.arrayForRepeat1 = [];
//các map
    $scope.mapSanPhamTra = new Map(), $scope.mapSanPhamThayThe = new Map(), $scope.mapYeuCauChiTiet = new Map();
//tạo search
    $scope.giaySearch = {}, $scope.curPage = 1, $scope.itemsPerPage = 5, $scope.maxSize = 5, $scope.giaySearch.pageSize = 24, $scope.giaySearch.trangThai = 1;

    $scope.yeuCau = {};

    getHoaDon(idHoaDon);
    getAllNhanVien();


    //add sản phẩm vào map trả
    $scope.addToMapSanPhamTra = function (baseId, hoaDonChiTiet) {
        if (hoaDonChiTiet.soLuongTra <= 0) {
            toastr.error('Bạn phải chọn số lượng trả lớn hơn 0.');
            return;
        }
        for (let i = 1; i <= hoaDonChiTiet.soLuongTra; i++) {
            let key = baseId + "." + i;
            if (!$scope.mapSanPhamTra.has(key)) {
                let valueCopy = angular.copy(hoaDonChiTiet);
                valueCopy.soLuongTra = 1; // Set số lượng trả mỗi bản ghi là 1

                // Cập nhật cấu trúc dữ liệu khi thêm vào map
                $scope.mapSanPhamTra.set(key, {
                    hoaDonChiTiet: valueCopy, // value1
                    lyDo: null,              // value2
                    ghiChu: ''               // value3
                });
            }
        }
        $scope.arrayForRepeat = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
    };


    $scope.chonLaiGiay = function (item) {
        $scope.isLoading = true;
        $scope.giayTra = item;
        $scope.getAllGiay();
        $('#exampleModalToggle').modal('show');
    }


    //hiển thị modal để điền ghi chú + lý do
    $scope.ghiChu_LyDo = function (item) {
        getAllLyDo();
        if ($scope.mapSanPhamTra.has(item.key)) {
            $scope.lyDo = $scope.mapSanPhamTra.get(item.key).lyDo
            $scope.ghiChu = $scope.mapSanPhamTra.get(item.key).ghiChu
        } else {
            $scope.resetLyDo_ghiChu();
        }
        $('#exampleModal').modal('show');
        $scope.giayTra = item;
    }


    $scope.resetLyDo_ghiChu = function () {
        $scope.lyDo = 0;
        $scope.ghiChu = '';
    }


    //update lý do
    $scope.updateLyDoVaGhiChu = function (key, lyDoMoi, ghiChuMoi) {
        // Kiểm tra xem entry với key cụ thể có tồn tại trong map không
        if ($scope.mapSanPhamTra.has(key)) {
            let value = $scope.mapSanPhamTra.get(key);

            // Cập nhật lyDo và ghiChu cho entry này
            value.lyDo = lyDoMoi;
            value.ghiChu = ghiChuMoi;

            // Cập nhật lại entry trong map
            $scope.mapSanPhamTra.set(key, value);

            // Cập nhật array dùng cho ng-repeat
            $scope.arrayForRepeat = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
        }
    };


    //check xem đã điền ghi chú + lý do chưa
    $scope.checkFormMoTaLyDo = function (lyDo, ghiChu) {

        if (!lyDo || !ghiChu || ghiChu.trim() === '') {
            toastr.error('Bạn chưa cung cấp đủ thông tin.');
            return;
        }

        $scope.updateLyDoVaGhiChu($scope.giayTra.key, lyDo, ghiChu);
        $('#exampleModal').modal('hide');
        $scope.getAllGiay();
        $('#exampleModalToggle').modal('show');
        $scope.resetLyDo_ghiChu();
    };


    //Xóa nếu người dùng không muốn trả nữa
    $scope.deleteGiayTra = function (item) {
        // Kiểm tra và xóa bản ghi trong map nếu key tồn tại
        if ($scope.mapSanPhamTra.has(item.key)) {
            $scope.mapSanPhamTra.delete(item.key);
            $scope.mapSanPhamThayThe.delete(item.key);
            // Cập nhật lại array cho ng-repeat
            $scope.arrayForRepeat = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
            $scope.arrayForRepeat1 = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
            // Tính tổng số tiền hàng đổi
            $scope.tongTienHangTra = $scope.tongTienHangTra - item.value.hoaDonChiTiet.donGia
            $scope.tinhTongTienHangDoi();
        } else {
            // Xử lý trường hợp key không tồn tại trong map
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
        }
    }


    //Xóa nếu người dùng không muốn đổi
    $scope.deleteGiayDoi = function (item1) {
        // Kiểm tra và xóa bản ghi trong map nếu key tồn tại
        if ($scope.mapSanPhamThayThe.has(item1.key)) {
            $scope.mapSanPhamThayThe.delete(item1.key);

            // Cập nhật lại array cho ng-repeat
            $scope.arrayForRepeat1 = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
            // Tính tổng số tiền hàng đổi
            $scope.tinhTongTienHangDoi();
        } else {
            // Xử lý trường hợp key không tồn tại trong map
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
            // Bạn có thể thêm các xử lý khác tại đây nếu cần
        }
    };


///////////////////////Bắt đầu đổi
    $scope.getAllGiay = function () {
        $scope.isLoading = true;
        $scope.giaySearch.currentPage = $scope.curPage;
        searchGiay($scope.giaySearch)
    }


    //fill biến thể giày cho người dùng chọn
    $scope.chonGiayDoi = function (giay) {
        $scope.isLoading = true;
        $http.get(host + '/rest/admin/giay/' + giay.id)
            .then(function (response) {
                detailGiayChiTiet(response.data); // Trả về dữ liệu khi sẵn sàng
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
            $scope.isLoading = false;
        });

        $('#exampleModalToggle2').modal('show');
        $('#exampleModalToggle').modal('hide');

    };

    //Sau khi chọn được biến thể giày
    $scope.giaySelected = function () {
        $scope.addToMapSanPhamThayThe($scope.giayChoosed);
    };

    //Add biến thể giày đã chọn vào map đổi
    $scope.addToMapSanPhamThayThe = function (bienTheGiayDoi) {
        // Cấu trúc dữ liệu mới cho map
        $scope.mapSanPhamThayThe.set($scope.giayTra.key, {
            bienTheGiay: bienTheGiayDoi, // Sản phẩm thay thế
            soLuongDoi: 1              // Giá trị sẽ cập nhật sau
        });

        // Cập nhật array cho ng-repeat
        $scope.arrayForRepeat1 = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));

        // Tính tổng số tiền hàng đổi
        $scope.tinhTongTienHangDoi();
    };


    //tính tiền khi đổi trả
    $scope.tongTienTraKhach = 0, $scope.tongTienKhachPhaiTra = 0, $scope.tongTienHangTra = 0, $scope.tongTienHangDoi = 0;


    $scope.updateTongTienTra = function () {
        // Thiết lập lại tổng tiền về 0 trước khi bắt đầu tính toán
        $scope.tongTienHangTra = 0;

        angular.forEach($scope.hoaDon.listHoaDonChiTiet, function (hoaDonChiTiet) {
            if (hoaDonChiTiet.soLuongTra >= 0 && hoaDonChiTiet.donGia >= 0) {
                $scope.tongTienHangTra += hoaDonChiTiet.soLuongTra * hoaDonChiTiet.donGia;
            } else {
                toastr["error"]("Số lượng trả hoặc đơn giá không hợp lệ");
            }
        });
        $scope.tinhTongTienHangDoi();
    };


    $scope.updateTongTienTraKhach = function () {
        $scope.tongTienTraKhach = 0;
        $scope.tongTienKhachPhaiTra = 0;
        let tienTraKhach = $scope.tongTienHangTra - $scope.tongTienHangDoi;

        if (tienTraKhach <= 0) {
            $scope.tongTienKhachPhaiTra = -tienTraKhach;
        } else {
            $scope.tongTienTraKhach = tienTraKhach;
        }
    }


    function tinhTongTienHangMua() {
        $scope.tongTienHangMua = 0;
        angular.forEach($scope.hoaDon.listHoaDonChiTiet, function (hoaDonChiTiet) {
            $scope.tongTienHangMua += hoaDonChiTiet.soLuong * hoaDonChiTiet.donGia;
        });
    }


    $scope.tinhTongTienHangDoi = function () {
        $scope.tongTienHangDoi = 0;
        angular.forEach($scope.arrayForRepeat1, function (item) {
            let soLuongDoi = item.value.soLuongDoi;
            let giaBan = item.value.bienTheGiay.giaBan;
            $scope.tongTienHangDoi += soLuongDoi * giaBan;
        });
        $scope.updateTongTienTraKhach();
    };


    //Hợp nhất 2 map mapSanPhamThayThe và mapSanPhamTra
    $scope.kiemTraSanPhamTra = function () {
        let coPhanTuKhongHopLe = false;

        $scope.mapSanPhamTra.forEach((sanPhamTra, key) => {
            if (!sanPhamTra.lyDo || !sanPhamTra.ghiChu) {
                toastr["error"]("Bạn chưa điền đủ thông tin lý do hoặc ghi chú cho sản phẩm có mã: " + key);
                coPhanTuKhongHopLe = true;
            }
        });

        if (!$scope.yeuCau.nguoiThucHien) {
            toastr["error"]("Bạn chưa điền đủ thông tin người thực hiện");
            coPhanTuKhongHopLe = true;
        }

        // Nếu tất cả các entry đều hợp lệ, thực hiện xử lý tiếp theo
        if (!coPhanTuKhongHopLe) {
            $scope.mapSanPhamTra.forEach((sanPhamTra, key) => {
                let entry = $scope.mapSanPhamThayThe.has(key) ? $scope.mapSanPhamThayThe.get(key) : {
                    bienTheGiay: null,
                    soLuongDoi: 0,
                };

                // Cập nhật mapYeuCauChiTiet
                $scope.mapYeuCauChiTiet.set(key, {
                    hoaDonChiTiet: sanPhamTra.hoaDonChiTiet.id,
                    soLuongTra: 1,
                    bienTheGiay: entry.bienTheGiay ? entry.bienTheGiay.id : null,
                    trangThai: 0,
                    lyDo: sanPhamTra.lyDo,
                    soLuong: 1,
                    tinhTrangSanPham: sanPhamTra.tinhTrangSanPham,
                    ghiChu: sanPhamTra.ghiChu,
                    loaiYeuCauChiTiet: entry.bienTheGiay ? 1 : 2
                });
            });


            if ($scope.mapYeuCauChiTiet.size === 0) {
                toastr["error"]("Bạn chưa chọn sản phẩm trả");
            } else {
                $scope.yeuCau.trangThai = 1;
                $scope.yeuCau.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet.values());

                console.log($scope.yeuCau)

                // Gửi yêu cầu POST đến máy chủ Spring Boot
                $http.post(host + '/rest/admin/yeu-cau/add', JSON.stringify($scope.yeuCau))
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
        }
    };


    //Hiển thị modal chi tiết biến thể giày
    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

    function detailGiayChiTiet(productData) {
        $scope.giayDetail = productData;
        const mauSacImages = productData.mauSacImages;
        for (const key in mauSacImages) {
            if (mauSacImages[key]) {
                productData.lstAnh.push(mauSacImages[key]);
            }
        }
        displayImages(productData.lstAnh);
        const lstBienTheGiay = productData.lstBienTheGiay;

        const buttonsContainer = document.getElementById('buttons-container');
        buttonsContainer.innerHTML = '';
        const productInfoContainer = document.getElementById('product-info');
        const sizeButtons = document.getElementById("sizeButtons");
        while (sizeButtons.firstChild) {
            sizeButtons.removeChild(sizeButtons.firstChild);
        }
        $scope.giayChoosed = {};

        if (mauSacImages.length === 0) {
            toastr["warning"]("Sản phẩm này chưa có biến thể nào");
        }

// Tạo các button màu sắc và xử lý sự kiện click
        for (const mauSacId in mauSacImages) {
            if (mauSacImages.hasOwnProperty(mauSacId)) {
                const mauSacIdInt = parseInt(mauSacId, 10);

                // Tìm tên màu sắc từ lstBienTheGiay dựa trên mauSacId
                const mauSacInfo = lstBienTheGiay.find(variant => variant.mauSac.id === mauSacIdInt)?.mauSac || {
                    ten: `Màu ${mauSacId}`, maMau: '#FFFFFF'
                };


                const outerDiv = document.createElement('div');
                const insideDiv = document.createElement('div');
                insideDiv.classList.add('insideDiv');
                const input = document.createElement("input");
                input.type = "radio"; // Để sử dụng input như một lựa chọn màu sắc
                input.name = "color" // Đặt cùng một tên cho tất cả các input của màu sắc
                input.hidden = true;

                insideDiv.textContent = "";
                insideDiv.style.backgroundColor = mauSacInfo.maMau; // Đặt màu nền của nút
                insideDiv.appendChild(input);
                outerDiv.appendChild(insideDiv);

                outerDiv.addEventListener('click', () => {
                    $scope.giayChoosed = {};
                    $scope.giayDetail = {};
                    input.checked = true;
                    productInfoContainer.innerHTML = '';
                    sizeButtons.innerHTML = '';
                    // Tạo danh sách ul để chứa thông tin sản phẩm
                    const ul = document.createElement('ul');
                    lstBienTheGiay.forEach(variant => {
                        if (mauSacIdInt === variant.mauSac.id) {
                            const li = document.createElement('li');
                            li.textContent = `ID: ${variant.id}, GiaBan: ${variant.giaBan}`;
                            ul.appendChild(li);

                            // Tạo nút kích thước và xử lý sự kiện click
                            const sizeButton = document.createElement("button");
                            sizeButton.textContent = variant.kichThuoc.ten;
                            sizeButton.className = "btn border";

                            $scope.$watch('giayDetail', function (newGiayDetail, oldGiayDetail) {
                                $scope.giayDetail = newGiayDetail;
                            });

                            sizeButton.addEventListener("click", () => {
                                $scope.giayDetail = variant;
                                $scope.giayChoosed = variant;
                                $scope.giayChoosed.ten = productData.ten;
                                if ($scope.giayDetail) {
                                    $scope.$apply();
                                }
                            });
                            sizeButtons.appendChild(sizeButton);
                        }

                    });

                    const allColorContainers = document.querySelectorAll('.button_color');
                    allColorContainers.forEach(container => {
                        container.classList.remove('button_checked');
                    });
                    outerDiv.classList.add('button_checked');

                    const linkAnh = mauSacImages[mauSacId];
                    if (linkAnh) {
                        const imageList = [linkAnh];
                        displayImages(imageList);
                    } else {
                        displayImages(productData.lstAnh);
                    }

                });
                outerDiv.className = "button_color";
                buttonsContainer.appendChild(outerDiv);
            }
        }
    }


    function displayImages(imageList) {
        const carouselInner = document.querySelector('#carouselExampleControls .carousel-inner');
        const carouselItems = document.querySelectorAll('#carouselExampleControls .carousel-item');

        // Xóa tất cả các carousel items hiện tại
        carouselItems.forEach(item => {
            carouselInner.removeChild(item);
        });

        // Tạo các carousel items mới từ danh sách ảnh mới
        for (let i = 0; i < imageList.length; i++) {
            const imageUrl = imageList[i];
            const div = document.createElement('div');
            div.className = i === 0 ? 'carousel-item active' : 'carousel-item';

            const img = document.createElement('img');
            img.src = imageUrl;
            img.className = 'd-block w-100';

            div.appendChild(img);
            carouselInner.appendChild(div);
        }
    }


    //các hàm get/insert
    function getHoaDon(id) {
        $http.get(host + '/rest/admin/hoa-don/yeu-cau/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                console.log($scope.hoaDon)
                $scope.khachHang = response.data.khachHang;
                $scope.yeuCau.hoaDon = response.data.id;
                $scope.numOfPages = response.data.totalPages;
                $scope.hoaDon.listHoaDonChiTiet.forEach(function (hoaDonChiTiet) {
                    hoaDonChiTiet.soLuongTraMax = hoaDonChiTiet.soLuong - hoaDonChiTiet.soLuongTra;
                });
                $scope.isLoading = false;
                // Gọi hàm tính tổng tiền mua hàng
                tinhTongTienHangMua();
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại 4");
            // $location.path("/list");
            $scope.isLoading = false;
        });
    }

    $scope.searching = false;

    $scope.search = function () {
        if (!$scope.searchText) {
            toastr["warning"]("Bạn chưa nhập thông tin tìm kiếm");
            return;
        }

        $scope.searching = true;
        searchGiay($scope.giaySearch);
    }

    $scope.reset = function () {
        if ($scope.searching) {
            $scope.searchText = "";
            searchGiay($scope.giaySearch);
        } else {
            toastr["warning"]("Bạn đang không tìm kiếm");
        }
        $scope.searching = false;

    }

    $scope.$watch('curPage + numPerPage', function () {
        $scope.isLoading = true;
        searchGiay($scope.giaySearch);
    });

    function searchGiay(giaySearch) {
        $scope.isLoading = true;

        let apiUrl = host + '/rest/admin/giay/find-all-by-search';

        if ($scope.searchText && $scope.searchText.length > 0) {
            $scope.giaySearch.ten = ($scope.searchText + "").trim();
        } else {
            $scope.giaySearch.ten = null;
        }

        giaySearch.currentPage = $scope.curPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.listGiay = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    function getAllLyDo() {
        $http.get(host + '/rest/admin/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại 2 ");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function getAllNhanVien() {
        $http.get(host + '/rest/admin/nhan-vien/get-all')
            .then(function (response) {
                $scope.listNhanVien = response.data;
                $scope.nguoiThucHien = response.data[0].id;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại 3");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function insertOrUpdateLyDo(lyDo) {
        $http.post(host + '/rest/admin/ly-do/list', lyDo)
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    $scope.troVe = function () {
        $location.path("/hoa-don");
    }
});


app.controller("selectedHoaDonController", function ($scope, $http, $location, $routeParams) {
    $scope.isLoading = true, $scope.searchText;
    $scope.hoaDonSearch = {};
    $scope.hoaDonSearch.currentPage = 1, $scope.hoaDonSearch.itemsPerPage = 5, $scope.hoaDonSearch.pageSize = 6, $scope.maxSize = 5, $scope.curPage = 1;
    $scope.typeSearch = "1", $scope.textSearch = '', $scope.tongTien = {};
    let startDate, endDate;

    getData($scope.hoaDonSearch);

    $scope.search = function () {
        if ($scope.typeSearch == 1) {
            $scope.hoaDonSearch.idHoaDon = $scope.textSearch;
        } else if ($scope.typeSearch == 2 || $scope.typeSearch == 3) {
            // Kiểm tra nếu có ký tự đặc biệt không mong muốn
            if (/[^a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ _]/.test($scope.textSearch)) {
                toastr["error"]("Vui lòng không nhập ký tự đặc biệt.");
            } else {
                if ($scope.typeSearch == 2) {
                    $scope.hoaDonSearch.khachHang = $scope.textSearch;
                } else {
                    $scope.hoaDonSearch.nhanVien = $scope.textSearch;
                }
            }
        } else if ($scope.typeSearch == 4) {
            if (!validatePhoneNumber($scope.textSearch)) {
                toastr["error"]("Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại có 10 chữ số.");
            } else {
                $scope.hoaDonSearch.soDienThoaiKhacHang = $scope.textSearch;
            }
        } else if ($scope.typeSearch == 5) {
            if (!validateEmail($scope.textSearch)) {
                toastr["error"]("Địa chỉ email không hợp lệ.");
            } else {
                $scope.hoaDonSearch.email = $scope.textSearch;
            }
        }
        getData($scope.hoaDonSearch);
        $scope.textSearch = '';
    }

    $scope.changeRadioLoaiHoaDon = function (loaiHoaDon) {
        $scope.hoaDonSearch.loaiHoaDon = loaiHoaDon;
        getData($scope.hoaDonSearch);
    }

    $scope.reset = function () {
        $scope.hoaDonSearch.idHoaDon = null,
            $scope.hoaDonSearch.khachHang = null,
            $scope.hoaDonSearch.nhanVien = null,
            $scope.hoaDonSearch.soDienThoaiKhacHang = null,
            $scope.hoaDonSearch.email = null,
            $scope.hoaDonSearch.loaiHoaDon = null,
            $scope.hoaDonSearch.ngayBatDau = null,
            $scope.hoaDonSearch.ngayKetThuc = null,
            $scope.hoaDonSearch.giaTu = null,
            $scope.hoaDonSearch.giaDen = null
        getData($scope.hoaDonSearch);
        $scope.textSearch = '';
    }

    $scope.searchByTotalMoney = function () {
        if ($scope.tongTien == 1) {
            $scope.hoaDonSearch.giaDen = 500000;
        } else if ($scope.tongTien == 2) {
            $scope.hoaDonSearch.giaTu = 500001;
            $scope.hoaDonSearch.giaDen = 1000000;
        } else if ($scope.tongTien == 3) {
            $scope.hoaDonSearch.giaTu = 1000001;
            $scope.hoaDonSearch.giaDen = 1500000;
        } else if ($scope.tongTien == 4) {
            $scope.hoaDonSearch.giaTu = 1500001;
            $scope.hoaDonSearch.giaDen = 2000000;
        } else if ($scope.tongTien == 5) {
            $scope.hoaDonSearch.giaTu = 2000001;
            $scope.hoaDonSearch.giaDen = 250000;
        } else if ($scope.tongTien == 6) {
            $scope.hoaDonSearch.giaTu = 250001;
            $scope.hoaDonSearch.giaDen = 3000000;
        } else if ($scope.tongTien == 6) {
            $scope.hoaDonSearch.giaTu = 3000001;
        }
    }

//     // Datepicker ngày bắt đầu
//     const fpStart = flatpickr('#ngayBatDau', {
//         dateFormat: 'd/m/Y', maxDate: new Date(), allowInput: true, // Cho phép nhập giá trị thay vì chỉ chọn từ calendar
//         clickOpens: true, // Cho phép click vào input để mở calendar
//         onClose: function (selectedDates) {
//             // Nếu chọn ngày bắt đầu sau ngày kết thúc
//             if (selectedDates[0] > fpEnd.selectedDates[0]) {
//                 // Cập nhật lại ngày kết thúc = ngày bắt đầu
//                 fpEnd.setDate(selectedDates[0]);
//             }
//             // Giới hạn ngày kết thúc không thể trước ngày bắt đầu
//             fpEnd.set('minDate', selectedDates[0]);
//         }, onChange: function (selectedDates, dateStr, instance) {
//             // Gọi hàm searchByDate khi ngày bắt đầu thay đổi
//             $scope.searchByDate();
//         }, onReady: function () {
//             this.input.addEventListener('keydown', function (e) {
//                 if (e.key === 'Enter') {
//                     $scope.searchByDate();
//                     $scope.$apply(); // Cần gọi $scope.$apply để cập nhật Angular scope
//                 }
//             });
//         }
//     });
//
// // Datepicker ngày kết thúc
//     const fpEnd = flatpickr('#ngayKetThuc', {
//         dateFormat: 'd/m/Y', maxDate: new Date(), allowInput: true, // Cho phép nhập giá trị thay vì chỉ chọn từ calendar
//         clickOpens: true, // Cho phép click vào input để mở calendar
//         onChange: function (selectedDates, dateStr, instance) {
//             // Gọi hàm searchByDate khi ngày kết thúc thay đổi
//             $scope.searchByDate();
//         }, onReady: function () {
//             this.input.addEventListener('keydown', function (e) {
//                 if (e.key === 'Enter') {
//                     $scope.searchByDate();
//                     $scope.$apply(); // Cần gọi $scope.$apply để cập nhật Angular scope
//                 }
//             });
//         }
//     });


    $scope.searchByDate = function () {
        startDate = fpStart.selectedDates[0];
        endDate = fpEnd.selectedDates[0];
        if (startDate === null && endDate === null) {
            toastr["error"]("Vui lòng chọn ngày bắt đầu hoặc ngày kết thúc");
            return;
        }

        $scope.hoaDonSearch.ngayBatDau = startDate == null ? null : formatISOToJavaLocalDateTime(startDate);
        $scope.hoaDonSearch.ngayKetThuc = endDate == null ? null : formatISOToJavaLocalDateTime(endDate);

        getData($scope.hoaDonSearch);
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

    function validateEmail(email) {
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function validatePhoneNumber(phone) {
        var phoneRegex = /^0[0-9]{9}$/;
        return phoneRegex.test(phone);
    }

    function getData(hoaDonSearch) {
        $scope.isLoading = true;
        $http.post(host + '/rest/admin/hoa-don/yeu-cau', hoaDonSearch)
            .then(function (response) {
                $scope.listHoaDon = response.data.content;
                console.log($scope.listHoaDon)
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
                $scope.isLoading = false;
            });
    }


    $scope.$watch('curPage + numPerPage', function () {
        $scope.hoaDonSearch.currentPage = $scope.curPage;
        getData($scope.hoaDonSearch);
    });

    $scope.chonHoaDon = function (hoaDon) {
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
    $scope.troVe = function () {
        $location.path("/list");
    }

});


app.filter('formatToVND', function () {
    return function (number) {
        if (number !== undefined && number !== null) {
            return number.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
        } else {
            return '0 VND'; // Hoặc giá trị mặc định khác
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
