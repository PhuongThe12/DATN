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

    $scope.curPage = 1, $scope.itemsPerPage = 5, $scope.maxSize = 5, $scope.searchTextYeuCau = '';
    let startDate, endDate;


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
        if (startDate === null && endDate === null) {
            toastr["error"]("Vui lòng chọn ngày bắt đầu hoặc ngày kết thúc");
            return;
        }
        getData(1, startDate, endDate);
    };

    $scope.changeRadioYeuCau = function (trangThai) {
        getData(1, startDate, endDate, null, trangThai);
    }
    $scope.searchYeuCau = function () {
        if (!$scope.searchTextYeuCau) {
            getData(1, null, null, null, null);
        }
        getData(1, startDate, endDate, $scope.searchTextYeuCau);
    };


    function getData(currentPage, startDate, endDate, searchText, trangThai) {

        let apiUrl = host + '/admin/rest/yeu-cau?page=' + currentPage;
        if (startDate != null) {
            apiUrl += '&ngayBatDau=' + formatDateToISO(startDate);
        }
        if (endDate != null) {
            apiUrl += '&ngayKetThuc=' + formatDateToISO(endDate);
        }
        if (searchText) {
            apiUrl += '&searchText=' + searchText;
        }
        if (trangThai) {
            apiUrl += '&trangThai=' + trangThai;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.listYeuCau = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                window.location.href = feHost + '/list';
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });
});


app.controller("detailYeuCauController", function ($scope, $http, $location, $routeParams) {


});


app.controller("updateYeuCauController", function ($scope, $http, $routeParams, $location) {
    const idYeuCau = $routeParams.id;
    $scope.isLoading = true;
    $scope.listYeuCauChiTiet = [];

    getListYeuCauChiTiet(idYeuCau, function () {
        $scope.soLuongTra = tinhSoLuongTra(), $scope.soLuongDoi = tinhSoLuongDoi();
    });

    $scope.confirmDeleteGiayTra = function (item) {
        if (confirm('Bạn có chắc chắn muốn hủy trả giày này không bạn không thể hoàn tác thao tác này?')) {
            let index = $scope.listYeuCauChiTietResponse.indexOf(item);
            if (index !== -1) {
                $scope.listYeuCauChiTietResponse[index].loaiYeuCauChiTiet = 3;
                $scope.listYeuCauChiTietResponse[index].trangThai = 2;
                $scope.soLuongTra = $scope.soLuongTra - 1;
            }
        }
    };


    $scope.confirmDeleteGiayDoi = function (item) {
        if (confirm('Bạn có chắc chắn muốn hủy đổi giày này không bạn không thể hoàn tác thao tác này?')) {
            let index = $scope.listYeuCauChiTietResponse.indexOf(item);
            if (index !== -1) {
                $scope.listYeuCauChiTietResponse[index].loaiYeuCauChiTiet = 2;
                $scope.listYeuCauChiTietResponse[index].trangThai = 3;
                $scope.listYeuCauChiTietResponse[index].isBienTheGiayHidden = true; // Thêm thuộc tính để ẩn bienTheGiay
                $scope.soLuongDoi = $scope.soLuongDoi - 1;
            }
        }
    };


    $scope.lyDoChiTiet = function (item) {
        getAllLyDo();
        $scope.yeuCauChiTiet = item;
        $scope.lyDo = "" + item.lyDo.id;
        $scope.ghiChu = item.ghiChu;
        $scope.sanPhamLoi = item.sanPhamLoi == 1 ? true : false;
    }


    $scope.updateLyDo = function () {
        let foundLyDo = $scope.listLyDo.find(lyDo => lyDo.id == $scope.lyDo);
        let foundIndex = $scope.listYeuCauChiTietResponse.findIndex(yeuCau => yeuCau.id === $scope.yeuCauChiTiet.id);

        if (foundIndex !== -1) {
            $scope.listYeuCauChiTietResponse[foundIndex].lyDo = foundLyDo;
            $scope.listYeuCauChiTietResponse[foundIndex].ghiChu = $scope.ghiChu;
            $scope.listYeuCauChiTietResponse[foundIndex].sanPhamLoi = $scope.sanPhamLoi ? 1:0;
        }
    }


    function tinhSoLuongTra() {
        return $scope.listYeuCauChiTietResponse.length;
    };


    function tinhSoLuongDoi() {
        let soLuongDoi = 0;
        for (let i = 0; i < $scope.listYeuCauChiTietResponse.length; i++) {
            if ($scope.listYeuCauChiTietResponse[i].loaiYeuCauChiTiet == 1 && $scope.listYeuCauChiTietResponse[i].bienTheGiay && Object.keys($scope.listYeuCauChiTietResponse[i].bienTheGiay).length > 0) {
                soLuongDoi++;
            }
        }
        return soLuongDoi;
    };


    $scope.formatDateView = function (isoDateString) {
        const inputDate = new Date(isoDateString);

        const options = {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit',
        };

        const formattedDate = inputDate.toLocaleDateString('en-GB', options);

        // Loại bỏ dấu phẩy
        return formattedDate.replace(',', '');
    };


    $scope.xacNhanYeuCau = function () {
        let listYeuCauChiTiet = $scope.listYeuCauChiTietResponse.map(item => {
            let yeuCauChiTietRequest = {
                id: item.id,
                bienTheGiay: item.bienTheGiay ? item.bienTheGiay.id : null,
                ghiChu: item.ghiChu,
                hoaDonChiTiet: item.hoaDonChiTiet.id,
                loaiYeuCauChiTiet: item.loaiYeuCauChiTiet,
                lyDo: item.lyDo.id,
                soLuong: item.soLuong,
                sanPhamLoi: item.sanPhamLoi,
                bienTheGiayTra: item.hoaDonChiTiet.bienTheGiay.id
            };
            return yeuCauChiTietRequest;
        });

        $scope.yeuCau.listYeuCauChiTiet = listYeuCauChiTiet;

        console.log($scope.yeuCau)

        // Gửi yêu cầu POST đến máy chủ Spring Boot
        $http.put(host + '/admin/rest/yeu-cau/update', JSON.stringify($scope.yeuCau))
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


    $scope.quayVe = function () {
        window.location.href = feHost + '/list';
    }

    function getAllLyDo() {
        $scope.isLoading = true;
        $http.get(host + '/admin/rest/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
            $scope.isLoading = false;
        });
    }

    function getListYeuCauChiTiet(id, callback) {
        $scope.isLoading = true;
        $http.get(host + '/admin/rest/yeu-cau-chi-tiet/list/' + id)
            .then(function (response) {
                $scope.listYeuCauChiTietResponse = response.data;
                $scope.yeuCau = response.data[0].yeuCau;
                getNhanVien($scope.yeuCau.nguoiThucHien)
                getHoaDon();
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
                callback(); // Gọi hàm callback sau khi dữ liệu đã tải
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    function getNhanVien(id) {
        $scope.isLoading = true;
        $http.get(host + '/admin/rest/nhan-vien/' + id)
            .then(function (response) {
                $scope.nhanVien = response.data;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    function getHoaDon() {
        $scope.hoaDonSearch = {}, $scope.hoaDonSearch.currentPage = 1, $scope.hoaDonSearch.pageSize = 1, $scope.hoaDonSearch.idHoaDon = 8;
        $scope.isLoading = true;
        $http.post(host + '/admin/rest/hoa-don/yeu-cau', $scope.hoaDonSearch)
            .then(function (response) {
                $scope.hoaDon = response.data.content[0];
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
                $scope.isLoading = false;
            });
    }
});














app.controller("addYeuCauController", function ($scope, $http, $location, $routeParams) {

    const idHoaDon = $routeParams.id;

//checkForm
    $scope.focusLyDo = false, $scope.focusGhiChu = false;
//các list
    $scope.listYeuCauChiTiet = [], $scope.listGiay = [], $scope.arrayForRepeat = [], $scope.arrayForRepeat1 = [];
//các map
    $scope.mapSanPhamTra = new Map(), $scope.mapSanPhamThayThe = new Map(), $scope.mapYeuCauChiTiet = new Map();
//tạo search
    $scope.giaySearch = {}, $scope.giaySearch.curPage = 1, $scope.giaySearch.itemsPerPage = 5, $scope.giaySearch.maxSize = 5, $scope.giaySearch.pageSize = 6;

    $scope.yeuCau = {};

    $scope.change = function (input) {
        input.$dirty = true;
    }

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
        searchGiay($scope.giaySearch)
    }


    //fill biến thể giày cho người dùng chọn
    $scope.chonGiayDoi = function (giay) {
        $scope.isLoading = true;
        $http.get(host + '/admin/rest/giay/' + giay.id)
            .then(function (response) {
                detailGiayChiTiet(response.data); // Trả về dữ liệu khi sẵn sàng
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
            $scope.isLoading = false;
        });
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
                    sanPhamLoi: 0,
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
                $http.post(host + '/admin/rest/yeu-cau/add', JSON.stringify($scope.yeuCau))
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

        displayImages(productData.lstAnh);

        const mauSacImages = productData.mauSacImages;
        const lstBienTheGiay = productData.lstBienTheGiay;

        const buttonsContainer = document.getElementById('buttons-container');
        buttonsContainer.innerHTML = '';
        const productInfoContainer = document.getElementById('product-info');
        const sizeButtons = document.getElementById("sizeButtons");
        const quantityDisplay = document.getElementById('quantity');
        const priceDisplay = document.getElementById('price-product');

// Tạo các button màu sắc và xử lý sự kiện click
        for (const mauSacId in mauSacImages) {
            if (mauSacImages.hasOwnProperty(mauSacId)) {
                const mauSacIdInt = parseInt(mauSacId, 10);

                // Tìm tên màu sắc từ lstBienTheGiay dựa trên mauSacId
                const mauSacInfo = lstBienTheGiay.find(variant => variant.mauSac.id === mauSacIdInt)?.mauSac || {
                    ten: `Màu ${mauSacId}`,
                    maMau: '#FFFFFF'
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
                            sizeButton.className = "btn btn-dark";
                            sizeButton.addEventListener("click", () => {
                                quantityDisplay.textContent = variant.soLuong;
                                priceDisplay.textContent = variant.giaBan;
                                $scope.giayChoosed = variant;
                                $scope.giayChoosed.ten = productData.ten;
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
                    const imageList = [linkAnh];
                    displayImages(imageList);
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
        $http.get(host + '/admin/rest/hoa-don/yeu-cau/'+ id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                $scope.khachHang = response.data.khachHang;
                $scope.yeuCau.hoaDon = response.data.id;
                $scope.numOfPages = response.data.totalPages;
                $scope.hoaDon.listHoaDonChiTiet.forEach(function(hoaDonChiTiet) {
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

    function searchGiay(giaySearch) {
        $http.post(host + '/admin/rest/giay/find-all-by-search', giaySearch)
            .then(function (response) {
                $scope.listGiay = response.data;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại 1");
            // $location.path("/list");
            $scope.isLoading = false;
        });
    }

    function getAllLyDo() {
        $http.get(host + '/admin/rest/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại 2 ");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function getAllNhanVien() {
        $http.get(host + '/admin/rest/nhan-vien/get-all')
            .then(function (response) {
                $scope.listNhanVien = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại 3");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function insertOrUpdateLyDo(lyDo) {
        $http.post(host + '/admin/rest/ly-do/list', lyDo)
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

});


app.controller("selectedHoaDonController", function ($scope, $http, $location, $routeParams) {
    $scope.isLoading = true, $scope.searchText;

    $scope.hoaDonSearch = {};
    $scope.hoaDonSearch.currentPage = 1, $scope.hoaDonSearch.itemsPerPage = 5, $scope.hoaDonSearch.pageSize = 6, $scope.hoaDonSearch.kenhBan = 2;
    getData($scope.hoaDonSearch);

    $scope.changeRadioLoaiHoaDon = function (loaiHoaDon) {
        $scope.hoaDonSearch.loaiHoaDon = loaiHoaDon;
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

    $scope.search = function () {

    }

    function getData(hoaDonSearch) {
        $scope.isLoading = true;
        $http.post(host + '/admin/rest/hoa-don/yeu-cau', hoaDonSearch)
            .then(function (response) {
                $scope.listHoaDon = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
                $scope.isLoading = false;
            });
    }

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