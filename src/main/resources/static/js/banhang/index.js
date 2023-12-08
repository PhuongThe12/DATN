var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/banhang/views/home.html', controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});


app.controller("homeController", function ($scope, $http, $location, $cookies, $rootScope) {

    document.title = 'Bán hàng';
    $scope.curPage = 1, $scope.itemsPerPage = 12, $scope.maxSize = 5;
    $scope.hoaDon = {};

    $scope.selectedHoaDon = {};

    $scope.feeShippingPerOne = 0;
    $scope.soLuong = 0;

    let giaySearch = {};
    $scope.listGiaySelected = []; // List biến thể giày được chọn

    $scope.giayListSearch = [];

    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

    $scope.oldValue = {};
    $scope.dotGiamGias = [];

    $scope.khachHangs = [];
    $scope.selectedKhachHang = {};

    $scope.tongTien = 0; // Tổng tiền
    $scope.tongTienGiam = 0; // Tổng tiền giảm
    $scope.tongTienPhaiTra = 0; // Tổng tiền phải trả
    $scope.chuyenKhoanTaiQuay = 0; //Tiền chuyển khoản

    $scope.dotGiamGiaSelect = {phanTramGiam: 0};
    $scope.uuDai = {uuDai: 0};

    $scope.phuongThucTaiQuays = [{id: 1, value: 'Tiền mặt'}, {id: 2, value: 'Chuyển khoản'}, {id: 3, value: 'Kết hợp'}];
    $scope.phuongThucDatHangs = [{id: 1, value: 'Trả sau'}, {id: 2, value: 'Chuyển khoản'}];
    $scope.phuongThucTaiQuay = $scope.phuongThucTaiQuays.find(item => item.id === 1);
    $scope.phuongThucDatHang = $scope.phuongThucDatHangs.find(item => item.id === 1);

    $scope.sdtNhanHang = '';
    $scope.tenNguoiNhan = '';
    $scope.diaChiNhan = '';
    $scope.diaChi = {};
    $scope.diaChi.tinh = {};
    $scope.diaChi.huyen = {};
    $scope.diaChi.xa = {};

    //Lay dia chi tinh
    $http.get(host + "/rest/provinces/get-all")
        .then(response => {
            $scope.tinhs = response.data;
        })
        .catch(err => {
            toastr["error"]("Lấy thông tin địa chỉ thất bại");
        });

    $scope.selecteHoaDon = function (id) { // chọn hóa đơn
        if ($scope.selectedHoaDon && $scope.selectedHoaDon.id === id) {
            return;
        }

        $http.get(host + '/rest/admin/hoa-don/get-full-response/' + id)
            .then(function (response) {
                if (response.data.trangThai === 0) {
                    const select = response.data;
                    $scope.selectedHoaDon = select;
                    $scope.listGiaySelected = [];
                    $scope.tongTien = 0;
                    $scope.selectedKhachHang = select.khachHangRestponse;
                    $scope.phuongThucTaiQuay = $scope.phuongThucTaiQuays[0];
                    select.hoaDonChiTiets.forEach(hdct => {
                        $scope.listGiaySelected.push({
                            kichThuoc: hdct.bienTheGiay.kichThuoc,
                            mauSac: hdct.bienTheGiay.mauSac,
                            ten: hdct.bienTheGiay.giayResponse.ten,
                            khuyenMai: hdct.bienTheGiay.khuyenMai,
                            giaBan: hdct.bienTheGiay.giaBan - (hdct.bienTheGiay.giaBan * hdct.bienTheGiay.khuyenMai / 100),
                            soLuongMua: hdct.soLuong,
                            idBienThe: hdct.bienTheGiay.id,
                            id: hdct.id
                        });
                        $scope.oldValue[hdct.id] = hdct.soLuong;
                        $scope.tongTien += ((hdct.bienTheGiay.giaBan - (hdct.bienTheGiay.giaBan * hdct.bienTheGiay.khuyenMai / 100)) * hdct.soLuong);
                        resetThongTinNhanHang();
                    });
                    $scope.listGiaySelected.sort((a, b) => (a.id - b.id));
                    resetTien();
                } else {
                    toastr["error"]("Lỗi. Hóa đơn đã được xử lý");
                    const foundIndex = $scope.hoaDons.findIndex(hd => hd.id === id);
                    if (foundIndex !== -1) {
                        $scope.hoaDons.splice(foundIndex, 1);
                    }

                }
            })
            .catch(function (error) {
                toastr["error"]("Chuyển hóa đơn thất bại. Vui lòng thử lại");
            })

    }

    let container = {};
    location.search.split('&').toString().substr(1).split(",").forEach(item => {
        container[item.split("=")[0]] = decodeURIComponent(item.split("=")[1]) ? item.split("=")[1] : "No query strings available";
    });

    if (Object.keys(container).length === 2 && container["status"] === "00") {
        printOrder(container["hd"], 2);
    } else if (Object.keys(container).length === 2 && container["status"] === "02" && !isNaN(container["hd"])) {
        $scope.selecteHoaDon(container["hd"]);
    }

    if (Object.keys(container).length > 2) {
        console.log(container, container["vnp_OrderInfo"], container["vnp_OrderInfo"].split("x"));
        const info = container["vnp_OrderInfo"].split("x");
        if (info.length < 2 && (info[1] !== '2' || info[1] !== '3')) {
            window.location.href = window.location.origin + window.location.pathname + "?status=02&hd=" + info[0] + "#home";
            return;
        } else if (info.length === 3 && (info[1] !== '2' || info[1] !== '3') && info[2] !== '1') {
            window.location.href = window.location.origin + window.location.pathname + "?status=02&hd=" + info[0] + "#home";
            return;
        }
        if (container["vnp_TransactionStatus"] === "00") {
            let request = {
                idHoaDon: info[0],
                phuongThuc: info[1],
                tienChuyenKhoan: container["vnp_Amount"] / 100,
                maGiaoDich: container["vnp_TxnRef"]
            }
            if (info.length === 2) {
                $http.post(host + "/rest/admin/hoa-don/thanh-toan-tai-quay-banking", request)
                    .then(response => {
                        window.location.href = window.location.origin + window.location.pathname + "?status=00&hd=" + info[0] + "#home";
                    })
                    .catch(err => {
                        window.location.href = window.location.origin + window.location.pathname + "?status=02#home";
                    })
            } else if (info.length === 3) {
                $http.post(host + "/rest/admin/hoa-don/dat-hang-tai-quay-banking", request)
                    .then(response => {
                        window.location.href = window.location.origin + window.location.pathname + "?status=00&hd=" + info[0] + "#home";
                    })
                    .catch(err => {
                        window.location.href = window.location.origin + window.location.pathname + "?status=02#home";
                    })
            }

        } else {
            $http.get(host + "/vnpay/cancel-banking/" + info[0])
                .then(response => {
                    window.location.href = window.location.origin + window.location.pathname + "?status=02#home";
                })
                .catch(err => {
                    window.location.href = window.location.origin + window.location.pathname + "?status=02#home";
                })
        }
    }

    $scope.getAllDotGiamGia = function () {
        $http.get(host + "/rest/admin/dot-giam-gia/get-all-active")
            .then(function (response) {
                $scope.dotGiamGias = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy thông tin đợt giảm giá thất bại");
            })
    }

    $scope.getAllDotGiamGia();

    $scope.searchKhachHang = function () {
        if (!$scope.khachHangSearch || $scope.khachHangSearch.length === 0) {
            toastr["warning"]("Không được để trống khi tìm kiếm");
            return;
        } else if ($scope.khachHangSearch.length < 3) {
            toastr["warning"]("Nhập ít nhất 3 ký tự");
            return;
        }

        $http.post(host + "/rest/admin/khach-hang/search-by-name", $scope.khachHangSearch)
            .then(function (response) {
                $scope.khachHangs = response.data;
                if ($scope.khachHangs.length === 0) {
                    toastr["warning"]("Không tìm thấy khách hàng nào");
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy thông tin thất bại");
            })

    }

    $scope.$watchCollection('listGiaySelected', function () {
        changeSP();
    });

    function changeSP() {
        if (!isNaN($scope.tongTien)) {
            let max = 0;
            $scope.dotGiamGias.forEach(dgg => {
                dgg.dieuKienResponses.forEach(dk => {
                    if (dk.tongHoaDon <= $scope.tongTien && max < dk.phanTramGiam) {
                        max = dk.phanTramGiam;
                        $scope.dotGiamGiaSelect = dk;
                    }
                })
            });

            if (max === 0) {
                $scope.dotGiamGiaSelect = null;
            }
        }
        setTongTienPhaiTra();
        $scope.changePhuongThucTaiQuay();

        $scope.soLuong = 0;
        $scope.listGiaySelected.forEach(item => {
            $scope.soLuong += item.soLuongMua;
        });

        tinhTienShip();
    }

    $scope.$watch('tongTien', function () {
        changeSP();
    });


    $scope.selectKhachHang = function (khachHang) {
        if (!$scope.selectedHoaDon.id) {
            toastr["error"]("Bạn chưa chọn hóa đơn");
            document.getElementById('closeModalKhachHang').click();
            return;
        }
        const reqest = {
            idHoaDon: $scope.selectedHoaDon.id, idGiay: khachHang.id
        }

        $http.post(host + "/rest/admin/hoa-don/add-khach-hang", reqest)
            .then(response => {
                $scope.selectedKhachHang = response.data;
            })
            .catch(err => {
                toastr["error"]("Có lỗi. Vui lòng thử lại");
            });

        changeSP();
        if (document.getElementById('closeModalKhachHang')) {
            setTimeout(() => {
                document.getElementById('closeModalKhachHang').click();
            }, 0);
        }
    }

    $scope.$watch('selectedKhachHang', function () {
        if ($scope.selectedKhachHang) {
            if ($scope.selectedKhachHang.id) {
                $scope.uuDai = $scope.selectedKhachHang.hangKhachHang;
            } else {
                $scope.uuDai = {uuDai: 0};
            }
            changeSP()
        }
    });

    function setTongTienPhaiTra() {
        let tongPhanTramGiam = 0;
        if (!isNaN($scope.uuDai.uuDai)) {
            tongPhanTramGiam += $scope.uuDai.uuDai;
        }
        if ($scope.dotGiamGiaSelect && !isNaN($scope.dotGiamGiaSelect.phanTramGiam)) {
            tongPhanTramGiam += $scope.dotGiamGiaSelect.phanTramGiam;
        }

        $scope.tongTienGiam = ($scope.tongTien * tongPhanTramGiam / 100)
        $scope.tongTienPhaiTra = $scope.tongTien - $scope.tongTienGiam;
    }

    function setTienThuaTaiQuay() {
        let tienChuyenKhoan, tienMat;
        if (isNaN($scope.chuyenKhoanTaiQuay) || !$scope.chuyenKhoanTaiQuay) {
            tienChuyenKhoan = 0;
        } else {
            tienChuyenKhoan = $scope.chuyenKhoanTaiQuay;
        }

        if (isNaN($scope.tienMatTaiQuay) || !$scope.tienMatTaiQuay) {
            tienMat = 0;
        } else {
            tienMat = $scope.tienMatTaiQuay;
        }

        if ($scope.phuongThucTaiQuay.id === 1 && $scope.tienMatTaiQuay) {
            $scope.tienThuaTaiQuay = tienMat - $scope.tongTienPhaiTra;
        } else if ($scope.phuongThucTaiQuay.id === 3) {
            $scope.tienThuaTaiQuay = tienMat + tienChuyenKhoan - $scope.tongTienPhaiTra;
        } else {
            $scope.tienThuaTaiQuay = 0;
        }

    }

    function setTienThuaDatHang() {

    }

    $scope.changePhuongThucTaiQuay = function () {
        if ($scope.phuongThucTaiQuay.id === 2) {
            $scope.disabledTienMatTaiQuay = true;
            $scope.tienMatTaiQuay = null;
            $scope.chuyenKhoanTaiQuay = $scope.tongTienPhaiTra;
            $scope.disabledChuyenKhoanTaiQuay = true;
        } else if ($scope.phuongThucTaiQuay.id === 1) {
            $scope.disabledTienMatTaiQuay = false;
            $scope.disabledChuyenKhoanTaiQuay = true;
            $scope.chuyenKhoanTaiQuay = null;
            $scope.disabledChuyenKhoanTaiQuay = true;
        } else {
            $scope.disabledTienMatTaiQuay = false;
            $scope.disabledChuyenKhoanTaiQuay = false;
            $scope.chuyenKhoanTaiQuay = null;
            $scope.tienMatTaiQuay = null;
        }
        setTienThuaTaiQuay();
    }

    $scope.changePhuongThucTaiQuay();

    $scope.changeTienMatTaiQuay = function () {
        if (!$scope.selectedHoaDon.id) {
            $scope.tienMatTaiQuay = null;
            toastr["error"]("Bạn chưa chọn hóa đơn");
            return;
        }

        if (isNaN($scope.tienMatTaiQuay)) {
            $scope.tienMatTaiQuay = '';
            toastr["error"]("Không hợp lệ");
        } else if ($scope.tienMatTaiQuay < 0) {
            $scope.tienMatTaiQuay = null;
            toastr["error"]("Tiền không được âm");
        }
        setTienThuaTaiQuay();
    }


    $scope.changeChuyenKhoanTaiQuay = function () {
        if (!$scope.selectedHoaDon.id) {
            $scope.chuyenKhoanTaiQuay = null;
            toastr["error"]("Bạn chưa chọn hóa đơn");
            return;
        }

        if (isNaN($scope.chuyenKhoanTaiQuay)) {
            $scope.chuyenKhoanTaiQuay = null;
            toastr["error"]("Không hợp lệ");
        } else if ($scope.chuyenKhoanTaiQuay < 0) {
            $scope.chuyenKhoanTaiQuay = null;
            toastr["error"]("Tiền không được âm");
        } else if ($scope.chuyenKhoanTaiQuay > $scope.tongTienPhaiTra) {
            $scope.chuyenKhoanTaiQuay = $scope.tongTienPhaiTra;
            toastr["error"]("Tiền chuyển khoản không được lớn hơn tổng giá trị hóa đơn");
        }
        setTienThuaTaiQuay();
    }


    function resetTien() {
        $scope.tienMatTaiQuay = null;
        $scope.chuyenKhoanTaiQuay = null;
        $scope.tienThuaTaiQuay = null;
        $scope.ghiChuTaiQuay = null;
        $scope.ghiChuChuyenKhoan = null;

    }

    $scope.blurSoLuong = function (giay) {

        let soLuong;

        if (isNaN(giay.soLuongMua) || giay.soLuongMua <= 0) {
            giay.soLuongMua = $scope.oldValue[giay.id];
            toastr["error"]("Số lượng không được âm");
            return;
        }

        if (giay.soLuongMua === $scope.oldValue[giay.id]) {
            return;
        }

        $http.get(host + '/rest/admin/giay/' + giay.idBienThe + "/so-luong")
            .then(function (response) {
                soLuong = response.data;
                if (soLuong < giay.soLuongMua - $scope.oldValue[giay.id]) {
                    toastr["error"]("Số lượng không đủ. Còn lại: " + (soLuong + $scope.oldValue[giay.id]));
                    giay.soLuongMua = $scope.oldValue[giay.id];
                } else {

                    const requestData = {
                        idHoaDon: giay.id, idGiay: giay.idBienThe, soLuong: giay.soLuongMua
                    }
                    $scope.isLoading = true;
                    $http.post(host + '/rest/admin/hoa-don/add-product', requestData)
                        .then(function (response) {
                            const result = response.data;
                            $scope.tongTien = 0;
                            $scope.listGiaySelected.forEach(item => {

                                if (item.idBienThe === result.bienTheGiay.id) {
                                    item.kichThuoc = result.bienTheGiay.kichThuoc;
                                    item.mauSac = result.bienTheGiay.mauSac;
                                    item.ten = result.bienTheGiay.giayResponse.ten;
                                    item.khuyenMai = result.bienTheGiay.khuyenMai;
                                    item.giaBan = result.bienTheGiay.giaBan - (result.bienTheGiay.giaBan * result.bienTheGiay.khuyenMai / 100);
                                    item.soLuongMua = result.soLuong;
                                    item.idBienThe = result.bienTheGiay.id;
                                    item.id = result.id;
                                    $scope.oldValue[result.id] = result.soLuong;
                                }
                                $scope.tongTien += (item.giaBan * item.soLuongMua);
                            });
                            $scope.listGiaySelected.sort((a, b) => (a.id - b.id));
                            toastr["success"]("Cập nhật thành công");

                        })
                        .catch(function (error) {
                            if (error.status === 409) {
                                $location.path("#home");
                                toastr["error"](error.data.data);
                            }
                            giay.soLuongMua = $scope.oldValue[giay.id];
                        });
                    $scope.isLoading = false;

                }

            })
            .catch(function (error) {
                toastr["error"](error.data.data);
                giay.soLuongMua = $scope.oldValue[giay.id];
            });
    }

    $scope.deleteHoaDon = function (id) {
        event.stopPropagation();
        Swal.fire({
            text: "Xác nhận hủy hóa đơn HD" + id + "?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.isLoading = true;
                $http.delete(host + "/rest/admin/hoa-don/" + id)
                    .then(function (response) {
                        let foundIndex = $scope.hoaDons.findIndex(hd => hd.id === id);
                        if (foundIndex !== -1) {
                            $scope.hoaDons.splice(foundIndex, 1);
                            toastr["success"]("Hủy thành công");
                        }
                    })
                    .catch(function (error) {
                        if (error.status === 409) {
                            $location.path("#home");
                            toastr["error"](error.data.data);
                        } else {
                            toastr["error"]("Lấy dữ liệu thất bại");
                        }
                    });
                $scope.isLoading = false;
            }
        });
    }

    function getHoaDonChuaThanhToan() {
        $scope.isLoading = true;
        $http.get(host + "/rest/admin/hoa-don/chua-thanh-toan-ban-hang")
            .then(function (response) {
                $scope.hoaDons = response.data;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    getHoaDonChuaThanhToan();

    $scope.createNewHoaDon = function () {
        $http.post(host + '/rest/admin/hoa-don/new-hoa-don')
            .then(function (response) {
                $scope.hoaDons.push(response.data);
                $scope.selecteHoaDon(response.data.id)
                toastr["success"]("Tạo mới thành công");
            })
            .catch(function (error) {
                toastr["error"]("Tạo mới thất bại. Vui lòng thử lại");
            })
    }

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/giay/get-all-giay';

        if ($scope.searchText && $scope.searchText.length > 0) {
            giaySearch.ten = ($scope.searchText + "").trim();
        } else {
            giaySearch.ten = null;
        }

        giaySearch.trangThai = 1;

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    getData(1);

    $scope.searching = false;
    $scope.search = function () {
        if (!$scope.searchText) {
            toastr["warning"]("Bạn chưa nhập thông tin tìm kiếm");
            return;
        }

        $scope.searching = true;
        getData(1);
    }

    $scope.reset = function () {
        if ($scope.searching) {
            $scope.searchText = "";
            getData(1);
        } else {
            toastr["warning"]("Bạn đang không tìm kiếm");
        }
        $scope.searching = false;

    }

    $scope.$watch('curPage + numPerPage', function () {
        $scope.isLoading = true;
        getData($scope.curPage);
    });


    // Thêm giày vào giỏ hàng
    $scope.addOrder = function (id) {
        if (!$scope.selectedHoaDon.id) {
            toastr["warning"]("Bạn chưa chọn hóa đơn");
            return;
        }

        $scope.checkExits = $scope.listGiaySelected.find(function (giay) {
            return giay.id === id;
        });

        if ($scope.checkExits === undefined) {

            $http.get(host + '/rest/admin/giay/' + id)
                .then(function (response) {
                    $scope.giaySeletect = response.data;
                    detailGiayChiTiet(response.data);
                    if (document.getElementById('buttonModalSanPham')) {
                        setTimeout(() => {
                            document.getElementById('buttonModalSanPham').click();
                        }, 0);
                    }
                }).catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                console.log(error);
                $location.path("/home");
            });
        } else {
            $scope.checkExits.soLuong++;
            $scope.totalPrice += $scope.checkExits.gia;
        }
        changeSP();

    }

    $scope.thanhToanTaiQuay = function () {

        if ($scope.listGiaySelected.length === 0) {
            toastr["error"]("Chưa có sản phẩm trong giỏ hàng");
            return;
        }

        if ($scope.phuongThucTaiQuay.id === 1 && ($scope.tienMatTaiQuay === null || $scope.tienThuaTaiQuay === null || $scope.tienMatTaiQuay < 0)) {
            toastr["error"]("Tiền khách trả chưa đủ");
            return;
        } else if ($scope.tienThuaTaiQuay < 0) {
            toastr["error"]("Tiền khách trả chưa đủ");
            return;
        }

        if (isNaN($scope.chuyenKhoanTaiQuay)) {
            toastr["warning"]("Tiền chuyển khoản không hợp lệ");
            return;
        }

        if ($scope.phuongThucTaiQuay.id !== 1 && $scope.chuyenKhoanTaiQuay < 10001) {
            toastr["warning"]("Tiền chuyển khoản không được nhỏ hơn 10,001 vnđ");
            return;
        }

        if ($scope.phuongThucTaiQuay.id !== 1 && $scope.chuyenKhoanTaiQuay > 999999999) {
            toastr["warning"]("Tiền chuyển khoản không được lớn hơn 999,999,999 vnđ");
            return;
        }

        let soLuong = 0;
        $scope.listGiaySelected.forEach(item => {
            soLuong += item.soLuongMua;
        });

        if (soLuong === 0) {
            toastr["warning"]("Hóa đơn chưa có sản phẩm nào");
            return;
        }

        Swal.fire({
            text: "Xác nhận xóa thanh toán ?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {

                const request = {
                    idHoaDon: $scope.selectedHoaDon.id,
                    idDieuKien: $scope.dotGiamGiaSelect ? $scope.dotGiamGiaSelect.id : null,
                    tienGiam: $scope.tongTienGiam,
                    ghiChu: $scope.ghiChuTaiQuay
                }

                if ($scope.phuongThucTaiQuay.id === 1) {
                    request.phuongThuc = 1;
                    request.tienMat = $scope.tongTienPhaiTra;
                    $scope.isLoading = true;
                    $http.post(host + "/rest/admin/hoa-don/thanh-toan-tai-quay", request)
                        .then(response => {
                            const index = $scope.hoaDons.findIndex(item => item.id === response.data);
                            if (index !== -1) {
                                $scope.hoaDons.splice(index, 1);
                                printOrder(response.data, 1);
                                resetHoaDon();
                            }
                            $scope.isLoading = false;
                        })
                        .catch(err => {
                            if (err.status === 409) {
                                toastr["error"](err.data.data);
                                $location.path("#home");
                            } else {
                                toastr["error"]("Có lỗi vui lòng thử lại");
                            }
                            $scope.isLoading = false;
                        });
                    return;

                } else if ($scope.phuongThucTaiQuay.id === 2) {
                    request.phuongThuc = 2;
                    request.tienChuyenKhoan = $scope.chuyenKhoanTaiQuay;
                    request.idHoaDon = $scope.selectedHoaDon.id;
                } else {
                    request.phuongThuc = 3;
                    request.tienMat = $scope.tongTienPhaiTra - $scope.chuyenKhoanTaiQuay;
                    if (request.tienMat === 0) {
                        request.phuongThuc = 2;
                    }
                    request.tienChuyenKhoan = $scope.chuyenKhoanTaiQuay;
                }

                $http.post(host + "/rest/admin/hoa-don/thanh-toan-tai-quay", request)
                    .then(response => {
                        // const index = $scope.hoaDons.findIndex(item => item.id === response.data);
                        request.idHoaDon = response.data + "x" + request.phuongThuc;
                        $http.post(host + "/vnpay/create-vnpay-order-tai-quay", request)
                            .then(response => {
                                $scope.loading = true;
                                window.location.href = response.data;
                            })
                            .catch(err => {
                                console.log(err);
                            })
                    })
                    .catch(err => {
                        if (err.status === 409) {
                            toastr["error"](err.data.data);
                            $location.path("#home");
                        } else {
                            console.log(err);
                            toastr["error"]("Có lỗi vui lòng thử lại");
                        }
                        $scope.isLoading = false;
                    });
            }
        });
    }

    function printOrder(idHd, level) {
        Swal.fire({
            text: "Thanh toán thành công. Bạn có muốn in hóa đơn không?",
            icon: "success",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $http.get(host + "/rest/admin/hoa-don/get-print/" + idHd)
                    .then((response) => {
                        $scope.hoaDonPrint = {};
                        const data = response.data;
                        $scope.hoaDonPrint.ma = data.id;
                        $scope.hoaDonPrint.tenKhachHang = data.khachHang ? data.khachHang.hoTen : "";
                        $scope.hoaDonPrint.tenNhanVien = data.nhanVien ? data.nhanVien.hoTen : "";
                        $scope.hoaDonPrint.ngayThanhToan = data.ngayThanhToan;
                        $scope.sanPhams = data.hoaDonChiTietResponses;
                        $scope.hoaDonPrint.conLai = 0;

                        $scope.hoaDonPrint.thongTinThanhToan = {};
                        data.chiTietThanhToans.forEach(item => {
                            $scope.hoaDonPrint.conLai += item.tienThanhToan;
                            $scope.hoaDonPrint.thongTinThanhToan.show = true;
                            if (item.hinhThucThanhToan === 1) {
                                $scope.hoaDonPrint.thongTinThanhToan.tienMat = item.tienThanhToan;
                            }
                            if (item.hinhThucThanhToan === 2) {
                                $scope.hoaDonPrint.thongTinThanhToan.chuyenKhoan = item.tienThanhToan;
                                $scope.hoaDonPrint.thongTinThanhToan.maGiaoDich = item.maGiaoDich;
                            }
                        });

                        $scope.hoaDonPrint.tongTru = data.tienGiam ? data.tienGiam : 0;
                        $scope.hoaDonPrint.tienShip = data.tienShip ? data.tienShip : 0;

                        $scope.hoaDonPrint.tongCong = $scope.hoaDonPrint.conLai + $scope.hoaDonPrint.tongTru + $scope.hoaDonPrint.tienShip;

                        $scope.hoaDonPrint.trangThai = data.trangThai;

                        document.title = 'HD' + data.id;
                        setTimeout(function () {
                            printJS({
                                printable: 'invoiceContent',
                                type: 'html',
                                documentTitle: 'HD' + data.id,
                                css: '/css/banhang/print.css',
                                onPrintDialogClose: () => {
                                    window.location.href = window.location.origin + window.location.pathname + "#home";
                                }
                            })
                        }, 0);

                    })
                    .catch((error) => {
                        console.log(error);
                        toastr["error"]("Không tìm thấy hóa đơn vui lòng thử lại");
                    })
            } else {
                if (level === 2) {
                    window.location.href = window.location.origin + window.location.pathname + "#home";
                }
            }
        });

    }

    function resetHoaDon() {
        $scope.listGiaySelected = [];
        $scope.dotGiamGias = [];

        $scope.selectedHoaDon = null;
        $scope.khachHangs = [];
        $scope.selectedKhachHang = {};

        $scope.tongTien = 0; // Tổng tiền
        $scope.tongTienGiam = 0; // Tổng tiền giảm
        $scope.tongTienPhaiTra = 0; // Tổng tiền phải trả
        $scope.chuyenKhoanTaiQuay = 0; //Tiền chuyển khoản

        $scope.dotGiamGiaSelect = {phanTramGiam: 0};
        $scope.uuDai = {uuDai: 0};
        $scope.tienMatTaiQuay = null;
        $scope.tienThuaTaiQuay = null;

        $scope.tenNguoiNhan = '';
        $scope.diaChiNhan = '';
        $scope.sdtNhan = '';
    }

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
                        if (mauSacIdInt === variant.mauSac.id && variant.trangThai === 1) {
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

    $scope.deleteSelected = function (id) {

        Swal.fire({
            text: "Xác nhận xóa sản phẩm ?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                for (let i = 0; i < $scope.listGiaySelected.length; i++) {
                    if ($scope.listGiaySelected[i].id === id) {
                        $scope.isLoading = true;
                        $http.delete(host + '/rest/admin/hoa-don/delete-hdct/' + id)
                            .then(function (response) {
                                const deletedItem = $scope.listGiaySelected.splice(i, 1)[0];
                                $scope.tongTien -= (deletedItem.giaBan * deletedItem.soLuongMua);
                                toastr["success"]("Loại bỏ sản phẩm thành công");
                            })
                            .catch(function (error) {
                                toastr["error"]("Thất bại. Vui lòng thử lại");
                            })
                        $scope.isLoading = false;
                        break; // Dừng vòng lặp sau khi xóa thành công
                    }
                }
            }
        });

    }

    $scope.deleteAllFromCart = function () {
        if ($scope.selectedHoaDon.id) {
            Swal.fire({
                text: "Xác nhận xóa toàn bộ sản phẩm khỏi giỏ?",
                icon: "info",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy"
            }).then((result) => {
                if (result.isConfirmed) {
                    $scope.isLoading = true;
                    $http.delete(host + '/rest/admin/hoa-don/delete-all-hdct/' + $scope.selectedHoaDon.id)
                        .then(function (response) {
                            $scope.listGiaySelected = [];
                            $scope.tongTien = 0;
                            toastr["success"]("Loại bỏ sản phẩm thành công");
                        })
                        .catch(function (error) {
                            toastr["error"]("Thất bại. Vui lòng thử lại");
                        })
                    $scope.isLoading = false;

                }
            });
        }
    }

    // Hàm thêm biến thể giày sau khi chọn "Thêm vào giỏ haàng"
    $scope.addGiay = function () {
        if (!$scope.giayChoosed.id) {
            toastr["warning"]("Bạn chưa chọn sản phẩm");
            return;
        }

        if (!$scope.selectedHoaDon.id) {
            toastr["warning"]("Bạn chưa chọn hóa đơn");
            return;
        }
        const exists = $scope.listGiaySelected.find(function (item) {
            return item.idBienThe === $scope.giayChoosed.id;
        });

        if ($scope.giayChoosed.soLuong <= 0) {
            toastr["warning"]("Sản phẩm này đã hết hàng");
            return;
        }

        let requestData;
        if (exists) {
            requestData = {
                idHoaDon: exists.id, idGiay: $scope.giayChoosed.id, soLuong: (1 + $scope.oldValue[exists.id])
            }
            $scope.addToOrder(requestData);
        } else {
            requestData = {
                idHoaDon: $scope.selectedHoaDon.id, idGiay: $scope.giayChoosed.id, soLuong: 1
            }
            $scope.addNewHDCT(requestData);
        }

        changeSP();
        if (document.getElementById('modalSP')) {
            setTimeout(() => {
                document.getElementById('closeModalSanPham').click();
            }, 0);
        }
    }

    $scope.addNewHDCT = function (data) {
        $scope.isLoading = true;
        $http.post(host + '/rest/admin/hoa-don/add-new-hdct', data)
            .then(function (response) {
                const result = response.data;
                $scope.listGiaySelected.push({
                    kichThuoc: result.bienTheGiay.kichThuoc,
                    mauSac: result.bienTheGiay.mauSac,
                    ten: result.bienTheGiay.giayResponse.ten,
                    khuyenMai: result.bienTheGiay.khuyenMai,
                    giaBan: result.bienTheGiay.giaBan - (result.bienTheGiay.giaBan * result.bienTheGiay.khuyenMai / 100),
                    soLuongMua: result.soLuong,
                    idBienThe: result.bienTheGiay.id,
                    id: result.id
                });
                $scope.oldValue[result.id] = result.soLuong;
                $scope.tongTien += ((result.bienTheGiay.giaBan - (result.bienTheGiay.giaBan * result.bienTheGiay.khuyenMai / 100)) * result.soLuong);
                toastr["success"]("Thêm thành công");
            })
            .catch(function (error) {
                if (error.status === 409) {
                    $location.path("#home");
                    toastr["error"]("Thêm thất bại. " + error.data.data);
                } else {
                    toastr["error"]("Thêm thất bại. ");
                }
            });
        $scope.listGiaySelected.sort((a, b) => (a.id - b.id));
        $scope.isLoading = false;
    }


    $scope.addToOrder = function (data) {
        $scope.isLoading = true;
        $http.post(host + '/rest/admin/hoa-don/add-product', data)
            .then(function (response) {
                const result = response.data;
                $scope.tongTien = 0;
                $scope.listGiaySelected.forEach(item => {

                    if (item.idBienThe === result.bienTheGiay.id) {
                        item.kichThuoc = result.bienTheGiay.kichThuoc;
                        item.mauSac = result.bienTheGiay.mauSac;
                        item.ten = result.bienTheGiay.giayResponse.ten;
                        item.khuyenMai = result.bienTheGiay.khuyenMai;
                        item.giaBan = result.bienTheGiay.giaBan - (result.bienTheGiay.giaBan * result.bienTheGiay.khuyenMai / 100);
                        item.soLuongMua = result.soLuong;
                        item.idBienThe = result.bienTheGiay.id;
                        item.id = result.id;
                        $scope.oldValue[result.id] = result.soLuong;
                    }

                    $scope.tongTien += (item.giaBan * item.soLuongMua);
                });
                $scope.listGiaySelected.sort((a, b) => (a.id - b.id));
                toastr["success"]("Cập nhật thành công");

            })
            .catch(function (error) {
                if (error.status === 409) {
                    $location.path("#home");
                    toastr["error"](error.data.data);
                }
                toastr["error"]("Thêm thất bại");
            });
        $scope.isLoading = false;
    }

    $scope.taoHoaDon = function () {
        $scope.hoaDon.listBienTheGiay = $scope.listGiaySelected;
        $scope.hoaDon.tongTien = $scope.tongTien;
    }

    $scope.deleteSelectedKhachHang = function () {
        event.stopPropagation();
        $scope.selectedKhachHang = {};
    }


    var addToCartListener = $scope.$on('addToCartEvent', function (event, data) {
        // Thực hiện xử lý khi sự kiện được phát ra từ directive
        $scope.listGiaySelected.push(data.item);
        alert(2);
    });

// Hủy đăng ký lắng nghe sự kiện khi controller bị hủy
    $scope.$on('$destroy', function () {
        addToCartListener(); // Hủy đăng ký lắng nghe sự kiện
    });

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

    const video = document.getElementById('video');

    let scanning = false;
    let inteval;

    $scope.startScanning = function () {
        if (!$scope.selectedHoaDon || !$scope.selectedHoaDon.id) {
            toastr["warning"]("Bạn chưa chọn hóa đơn");
            return;
        }

        if (document.getElementById('staticBackDropButton')) {
            setTimeout(() => {
                document.getElementById('staticBackDropButton').click();

            }, 0);
        }
        scanning = true;
        if (scanning) {
            navigator.mediaDevices.getUserMedia({video: true})
                .then((stream) => {
                    video.srcObject = stream;
                    video.play();

                    const canvas = document.createElement('canvas');
                    const context = canvas.getContext('2d');

                    inteval = setInterval(() => {
                        if (scanning) {
                            context.drawImage(video, 0, 0, canvas.width, canvas.height);
                            const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
                            let code = jsQR(imageData.data, imageData.width, imageData.height);

                            if (code) {
                                // Thực hiện các hành động với mã QR tại đây
                                // document.getElementById('closeModalCamera').click();
                                // clearInterval(interval);
                                scanning = false;
                                $http.get(host + '/rest/admin/giay/bien-the/' + code.data)
                                    .then(function (response) {

                                        if (response.data.soLuong < 1) {
                                            toastr["error"]("Sản phẩm này đã hết hàng");
                                        } else {
                                            const exists = $scope.listGiaySelected.find(function (item) {
                                                return item.idBienThe === response.data.id;
                                            });
                                            const result = {
                                                idGiay: response.data.id,
                                            }

                                            if (exists) {
                                                result.idHoaDon = exists.id;
                                                result.soLuong = (1 + $scope.oldValue[exists.id]);
                                                $scope.addToOrder(result);
                                            } else {
                                                result.idHoaDon = $scope.selectedHoaDon.id;
                                                result.soLuong = 1;
                                                $scope.addNewHDCT(result);
                                            }

                                        }
                                    })
                                    .catch(function (error) {
                                        toastr["error"]("Không tìm thấy sản phẩm");
                                    })

                                setTimeout(function () {
                                    scanning = true;
                                    code = null;
                                }, 1000);

                            } else {
                                Quagga.decodeSingle({
                                    src: convertImageDataToBase64(imageData), numOfWorkers: 0, decoder: {
                                        readers: ['ean_reader', 'code_128_reader', 'code_39_reader']
                                    },
                                }, function (result) {
                                    if (result && result.codeResult) {
                                        $http.get(host + '/rest/admin/giay/bien-the/' + result.codeResult.code)
                                            .then(function (response) {
                                                if (response.data.soLuong < 1) {
                                                    toastr["error"]("Sản phẩm này đã hết hàng");
                                                } else {
                                                    const exists = $scope.listGiaySelected.find(function (item) {
                                                        return item.idBienThe === response.data.id;
                                                    });
                                                    const result = {
                                                        idGiay: response.data.id,
                                                    }

                                                    if (exists) {
                                                        result.idHoaDon = exists.id;
                                                        result.soLuong = (1 + $scope.oldValue[exists.id]);
                                                        $scope.addToOrder(result);
                                                    } else {
                                                        result.idHoaDon = $scope.selectedHoaDon.id;
                                                        result.soLuong = 1;
                                                        $scope.addNewHDCT(result);
                                                    }

                                                }
                                            })
                                            .catch(function (error) {
                                                toastr["error"]("Không tìm thấy sản phẩm");
                                            })

                                        setTimeout(function () {
                                            scanning = true;
                                            result = null;
                                        }, 1000);
                                        // document.getElementById('closeModalCamera').click();
                                        // clearInterval(interval);
                                    }
                                });

                            }

                        }
                    }, 500);
                })
                .catch((error) => {
                    toastr["error"]('Không thể truy cập camera:');
                });
        } else {
            $scope.stopScanning();
        }
    }

    function convertImageDataToBase64(imageData) {
        const canvas = document.createElement('canvas');
        const context = canvas.getContext('2d');
        canvas.width = imageData.width;
        canvas.height = imageData.height;
        context.putImageData(imageData, 0, 0);

        return canvas.toDataURL('image');
    }


    function updateVideoStream() {
        navigator.mediaDevices.getUserMedia({
            video: {deviceId: selectedCamera, width: 1920, height: 1080}
        })
            .then((stream) => {
                video.srcObject = stream;
                video.play();
            })
            .catch((error) => {
                console.error('Không thể cập nhật stream camera:', error);
            });
    }


    $scope.stopScanning = function () {
        video.pause();
        video.srcObject.getTracks().forEach(track => track.stop());
        if (inteval) {
            clearInterval(inteval);
        }
    }

    $scope.closeModalCamera = function () {
        scanning = false;
        $scope.stopScanning();
    }


    ///Add khách hàng
    $scope.khachHang = {gioiTinh: true};
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.addKhachHang = function () {
        if ($scope.khachHangForm.$invalid) {
            return;
        }
        console.log($scope.khachHang);
        $http.post(host + '/rest/admin/khach-hang', $scope.khachHang)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.selectedKhachHang = response;
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.khachHangForm.hoTen.$dirty = false;
                    $scope.khachHangForm.gioiTinh.$dirty = false;
                    $scope.khachHangForm.ngaySinh.$dirty = false;
                    $scope.khachHangForm.soDienThoai.$dirty = false;
                    $scope.khachHangForm.email.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

    $scope.scanQRKhachHang = function () {
        scanning = true;
        if (scanning) {
            navigator.mediaDevices.getUserMedia({video: true})
                .then((stream) => {
                    video.srcObject = stream;
                    video.play();

                    const canvas = document.createElement('canvas');
                    const context = canvas.getContext('2d');

                    const interval = setInterval(() => {
                        if (scanning) {
                            context.drawImage(video, 0, 0, canvas.width, canvas.height);
                            const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
                            const code = jsQR(imageData.data, imageData.width, imageData.height);

                            if (code) {
                                // Thực hiện các hành động với mã QR tại đây
                                if (typeof code.data === 'string') {
                                    console.log(code.data.split('|'));
                                    const data = code.data.split('|');
                                    if (data.length === 7) {
                                        $scope.khachHang.hoTen = data[2];
                                        $scope.khachHang.gioiTinh = data[4] === 'Nam';
                                        console.log($scope.khachHang.gioiTinh);
                                    } else {
                                        toastr["error"]('Không hợp lệ. Vui lòng thử lại');
                                    }
                                } else {
                                    toastr["error"]('Không hợp lệ. Vui lòng thử lại');
                                }
                                if (document.getElementById('closeModalCamera')) {
                                    setTimeout(() => {
                                        document.getElementById('closeModalCamera').click();

                                    }, 0);
                                }
                                clearInterval(interval);
                            }
                        }
                    }, 500);
                })
                .catch((error) => {
                    toastr["error"]('Không thể truy cập camera:');
                });
        } else {
            $scope.stopScanning();
        }
    }

    $scope.changeTinh = function () {
        if ($scope.diaChi.tinh && $scope.diaChi.tinh.id) {
            $http.get(host + "/rest/districts/" + $scope.diaChi.tinh.id)
                .then(response => {
                    $scope.huyens = response.data;
                    $scope.diaChi.huyen = {};
                    $scope.diaChi.xa = {};
                })
                .catch(err => {
                    toastr["error"]("Lấy thông tin địa chỉ thất bại");
                });
        }

    }

    $scope.changeHuyen = function () {
        if ($scope.diaChi.huyen && $scope.diaChi.huyen.id) {
            $http.get(host + "/rest/wards/" + $scope.diaChi.huyen.id)
                .then(response => {
                    $scope.xas = response.data;
                    $scope.diaChi.xa = {};
                })
                .catch(err => {
                    toastr["error"]("Lấy thông tin địa chỉ thất bại");
                });
        }
    }

    $scope.changeXa = function () {
        $scope.error = {};
    }

    function checkDiaChi() {
        $scope.error = {};
        if (!$scope.diaChi.tinh.id || !$scope.diaChi.huyen.id || !$scope.diaChi.xa.id) {
            $scope.error.diaChi = "Địa chỉ phải đầy đủ xã, huyện tỉnh";
        } else {
            $scope.error.diaChi = null;
        }
    }

    $scope.submitDiaChi = function () {

        checkDiaChi();
        if (!$scope.diaChi.xa.id || !$scope.diaChi.huyen.id || !$scope.diaChi.tinh.id || !$scope.diaChi.tenNguoiNhan || !$scope.diaChi.sdtNguoiNhan || !$scope.diaChi.emailNhan) {
            toastr["error"]("Lấy thông tin địa chỉ thất bại");
            return;
        }

        $scope.isLoading = true;
        $scope.tenNguoiNhan = $scope.diaChi.tenNguoiNhan;
        $scope.sdtNhan = $scope.diaChi.sdtNguoiNhan;
        $scope.diaChiNhan = $scope.diaChi.detailAdress + ", " + $scope.diaChi.xa.ten + ", " + $scope.diaChi.huyen.ten + ", " + $scope.diaChi.tinh.ten;
        if (document.getElementById('closeModalThongTinNhanHang')) {
            setTimeout(() => {
                document.getElementById('closeModalThongTinNhanHang').click();
            }, 0);
        }

        logisticInfo.to_address = $scope.diaChi.detailAdress;
        logisticInfo.to_ward_name = $scope.diaChi.xa.ten;
        logisticInfo.to_district_name = $scope.diaChi.huyen.ten;
        logisticInfo.to_province_name = $scope.diaChi.tinh.ten;
        if ($scope.tongTienPhaiTra < 80000000) {
            logisticInfo.insurance_value = parseInt($scope.tongTienPhaiTra);
        }

        $http.post('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/preview', logisticInfo)
            .then(response => {
                $scope.feeShippingPerOne = response.data.data.total_fee;
                $scope.isLoading = false;
            })
            .catch(error => {
                toastr["error"]("Lấy thông tin thất bại");
                $scope.feeShippingPerOne = 50000;
                $scope.isLoading = false;
            })
    }

    $scope.$watch('feeShippingPerOne', function (value) {
        tinhTienShip();
    });


    $scope.chagePhiVanChuyen = function () {
        if (isNaN($scope.phiVanChuyen)) {
            toastr["error"]("Không hợp lệ");
            tinhTienShip();
            return;
        }

        if (!$scope.selectedHoaDon.id) {
            toastr["error"]("Bạn chưa chọn hóa đơn");
            return;
        }
        $scope.tongTienPhaiTraDatHang = $scope.tongTien - $scope.tongTienGiam + $scope.phiVanChuyen;
    }

    function tinhTienShip() {
        if ($scope.soLuong <= 0) {
            return;
        }

        let soDonHang = 0;
        if ($scope.soLuong % 30 === 0) {
            soDonHang = parseInt($scope.soLuong / 30);
        } else {
            soDonHang = parseInt($scope.soLuong / 30) + 1;
        }

        $scope.phiVanChuyen = Math.round(soDonHang * $scope.feeShippingPerOne);
        $scope.tongTienPhaiTraDatHang = $scope.tongTienPhaiTra + $scope.phiVanChuyen;

    }

    resetThongTinNhanHang = function () {
        $scope.tenNguoiNhan = '';
        $scope.sdtNhan = '';
        $scope.diaChiNhan = '';
        $scope.phiVanChuyen = null;
        $scope.feeShippingPerOne = 0;
        $scope.diaChi = {};
        $scope.diaChi.tinh = {};
        $scope.diaChi.huyen = {};
        $scope.diaChi.xa = {};
    }

    let logisticInfo = {
        "payment_type_id": 2,
        "note": "Tintest 123",
        "required_note": "KHONGCHOXEMHANG",
        "from_name": "TinTest124",
        "from_phone": "0987654321",
        "from_address": "Phường Mỹ Đình 2, Quận Nam Từ Liêm, Hà Nội",
        "from_ward_name": "Phường Mỹ Đình 2",
        "from_district_name": "Quận Nam Từ Liêm",
        "from_province_name": "Hà Nội",
        "to_name": "TinTest124",
        "to_phone": "0987654321",
        "to_address": "Xuân Lôi, Lập Thạch, Vĩnh Phúc, Việt Nam",
        "to_ward_name": "Xuân Lôi",
        "to_district_name": "Lập Thạch",
        "to_province_name": "Vĩnh Phúc",
        "cod_amount": 0,
        "content": "Theo New York Times",
        "weight": 150,
        "length": 150,
        "width": 19,
        "height": 10,
        "cod_failed_amount": 2000000,
        "insurance_value": 0,
        "service_id": 0,
        "service_type_id": 2
    }

    $scope.thayDoiDiaChi = function () {
        if ($scope.selectedKhachHang.id && (!$scope.diaChi.tenNguoiNhan || !$scope.diaChi.sdtNguoiNhan || !$scope.diaChi.emailNhan)) {
            $scope.diaChi.tenNguoiNhan = $scope.selectedKhachHang.hoTen;
            $scope.diaChi.sdtNguoiNhan = $scope.selectedKhachHang.soDienThoai;
            $scope.diaChi.emailNhan = $scope.selectedKhachHang.email;
        }
    }

    $scope.datHang = function () {

        if ($scope.listGiaySelected.length === 0) {
            toastr["error"]("Chưa có sản phẩm trong giỏ hàng");
            return;
        }

        if (!$scope.selectedHoaDon.id) {
            toastr["error"]("Bạn chưa chọn hóa đơn");
            return;
        }

        if (!$scope.diaChi || !$scope.diaChi.xa || !$scope.diaChi.huyen || !$scope.diaChi.tinh || !$scope.diaChi.xa || !$scope.diaChi.sdtNguoiNhan || !$scope.diaChi.tenNguoiNhan || !$scope.diaChi.detailAdress) {
            toastr["error"]("Chưa có địa chỉ giao hàng hoặc địa chỉ không hợp lệ");
            return;
        }

        if ($scope.tongTienPhaiTra < 10001) {
            toastr["error"]("Tiền đơn hàng không được nhỏ hơn 10,001 vnđ");
            return;
        }

        if ($scope.tongTienPhaiTra > 999999999) {
            toastr["error"]("Tiền đơn hàng không được nhỏ hơn 999,999,999 vnđ");
            return;
        }

        const request = {
            id: $scope.selectedHoaDon.id,
            phuongThuc: $scope.phuongThucDatHang.id,
            tienGiam: $scope.tongTienGiam,
            tienShip: $scope.phiVanChuyen,
            tongTien: $scope.tongTienPhaiTra,
            diaChiNhan: $scope.diaChiNhan,
            sdtNhan: $scope.sdtNhan,
            nguoiNhan: $scope.tenNguoiNhan,
            ghiChu: $scope.ghiChuDatHang
        }

        Swal.fire({
            text: "Xác nhận xóa thanh toán ?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {

                $http.post(host + "/rest/admin/hoa-don/dat-hang-tai-quay", request)
                    .then(response => {
                        if (request.phuongThuc === 1) {
                            const index = $scope.hoaDons.findIndex(item => item.id === response.data);
                            if (index !== -1) {
                                $scope.hoaDons.splice(index, 1);
                                toastr["success"]("Thanh toán thành công");
                                resetHoaDon();
                            }
                        } else {
                            request.idHoaDon = response.data + "x" + request.phuongThuc + "x1";
                            request.tienChuyenKhoan = request.tongTien;
                            $http.post(host + "/vnpay/create-vnpay-order-tai-quay", request)
                                .then(response => {
                                    window.location.href = response.data;
                                })
                                .catch(err => {
                                    console.log(err);
                                })
                        }

                        $scope.isLoading = false;
                    })
                    .catch(err => {
                        if (err.status === 409) {
                            toastr["error"](err.data.data);
                            $location.path("#home");
                        } else {
                            toastr["error"]("Có lỗi vui lòng thử lại");
                        }
                        $scope.isLoading = false;
                    });
            }
        });

    }

});

app.directive('customSelect', function ($injector) {
    return {
        restrict: 'E', templateUrl: '/pages/admin/banhang/views/combobox.html', scope: {
            id: '@', title: '@', items: '=', ngModel: '=', onChange: '&'
        }, controller: function ($scope) {
            $scope.isActive = false;

            $scope.toggleDropdown = function () {
                $scope.isActive = !$scope.isActive;
            };

            $scope.selectItem = function (item) {
                $scope.ngModel = item;
                $scope.selectedItem = item;
                $scope.isActive = false;
            };

            $scope.$watch('ngModel', function (newNgModel, oldNgModel) {
                if (!oldNgModel && newNgModel || newNgModel && newNgModel.id !== oldNgModel.id) {
                    if ($scope.items) {
                        var selectedItem = $scope.items.find((item) => item.id === newNgModel.id);
                        if (selectedItem) {
                            $scope.selectItem(selectedItem);
                            $scope.onChange();
                        }
                        if (!newNgModel.id) {
                            $scope.selectedItem = {};
                        }
                    }
                }
            }, true);

            angular.element(document).on('click', function (event) {
                var container = angular.element(document.querySelector('#' + $scope.id));
                if (container.length > 0) {
                    if (!container[0].contains(event.target)) {
                        $scope.$apply(function () {
                            $scope.isActive = false;
                        });
                    }
                }
            });


        }
    };

});

function nextInput(e) {
    if (e.which === 13) {
        e.preventDefault();
        e.target.blur();
    }
}
