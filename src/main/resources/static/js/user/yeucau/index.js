var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/user/yeucau/views/list.html', controller: 'yeuCauKhachHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/customer/yeucau/views/detail.html', controller: 'detailYeuCauKhachHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/customer/yeucau/views/update.html', controller: 'updateYeuCauKhachHangController'
    }).when("/add/:id", {
        templateUrl: '/pages/user/yeucau/views/add.html', controller: 'addYeuCauKhachHangController'
    }).otherwise({redirectTo: '/list'});
});

app.controller("yeuCauKhachHangListController", function ($scope, $http, $window, $location) {
    $scope.listHoaDonChiTiet = [];
    $scope.hoaDon = {};

});


app.controller("addYeuCauKhachHangController", function ($scope, $http, $routeParams, $window, $location) {
    const idHoaDon = $routeParams.id;
    $scope.mapSanPhamTra = new Map(), $scope.mapSanPhamThayThe = new Map(), $scope.mapYeuCauChiTiet = new Map();
    $scope.listHoaDonChiTiet = [], $scope.listLyDo = [], $scope.listGiaySearch = [];
    $scope.giayChoosed = {}, $scope.giaySearch = {}, $scope.hoaDon = {};

    $scope.curPage = 1, $scope.maxSize = 5, $scope.giaySearch.pageSize = 24, $scope.giaySearch.trangThai = 1;
    $scope.tongTienTraHang = 0, $scope.tongTienDoiHang = 0, $scope.tongTienHangDoi = 0;
    $scope.tongTienGiamGia = 0, $scope.tongTienThanhToan = 0;
    $scope.thongTinNhanHang = {}, $scope.thongTinNhanHang.soDienThoaiNhan = '', $scope.thongTinNhanHang.diaChiNhan = '', $scope.thongTinNhanHang.tenNguoiNhan = '';
    $scope.yeuCau = {};
    $scope.$watch('curPage + numPerPage', function () {
        $scope.isLoading = true;
        getListHoaDonChiTiet(idHoaDon);
        searchGiay($scope.giaySearch)
        getListLyDo();
    });


    $scope.tinhTongTienThanhToan = function () {
        $scope.tongTienThanhToan = 0;
        let tongTienThanhToan = $scope.tongTienDoiHang - $scope.tongTienTraHang;
        $scope.tongTienThanhToan = tongTienThanhToan < 0 ? Math.abs(tongTienThanhToan) : tongTienThanhToan;
    }

    $scope.tinhTongtienGiamGia = function () {
        $scope.tongTienGiamGia = 0;

        $scope.mapSanPhamThayThe.forEach((item) => {
            let giaBan = item.bienTheGiay.giaBan;
            let khuyenMai = item.bienTheGiay.khuyenMai / 100;
            let tienGiam = khuyenMai > 0 ? giaBan * khuyenMai : 0;
            $scope.tongTienGiamGia += tienGiam;
        })
    }


    $scope.tinhTongTienHangDoi = function () {
        $scope.tongTienHangDoi = 0;

        $scope.mapSanPhamThayThe.forEach((item) => {
            let giaBan = item.bienTheGiay.giaBan;
            $scope.tongTienHangDoi += giaBan;
        })
        $scope.tinhTongtienGiamGia();
        $scope.tongTienDoiHang = $scope.tongTienHangDoi - $scope.tongTienGiamGia;
        $scope.tinhTongTienThanhToan();
    }


    $scope.tinhTongTienHangTra = function () {
        $scope.tongTienTraHang = 0;

        $scope.mapSanPhamTra.forEach((item) => {
            let hoaDonChiTiet = item.hoaDonChiTiet;
            let donGia = hoaDonChiTiet.donGia;
            let soLuong = hoaDonChiTiet.soLuongTra;
            let phanTramGiam = $scope.hoaDon.phanTramGiam > 0 ? $scope.hoaDon.phanTramGiam / 100 : 0;
            let tienTraHang = (donGia * soLuong) - (donGia * soLuong * phanTramGiam);
            $scope.tongTienTraHang += tienTraHang;
        });
    };


    $scope.traHang = function (baseId, hoaDonChiTiet) {
        if (hoaDonChiTiet.soLuongTra <= 0) {
            toastr.error('Bạn chưa chọn số lượng muốn trả!');
            return;
        }
        for (let i = 1; i <= hoaDonChiTiet.soLuongTra; i++) {
            let key = baseId + "." + i;
            if (!$scope.mapSanPhamTra.has(key)) {
                let valueCopy = angular.copy(hoaDonChiTiet);
                valueCopy.soLuongTra = 1;

                $scope.mapSanPhamTra.set(key, {
                    hoaDonChiTiet: valueCopy, // value1
                    lyDo: null,              // value2
                    ghiChu: ''               // value3
                });
            }
        }
        $scope.listSanPhamTra = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));

        $scope.tinhTongTienHangTra();
    }

    $scope.deleteGiayTra = function (sanPhamTra) {
        if ($scope.mapSanPhamTra.has(sanPhamTra.key)) {
            $scope.mapSanPhamTra.delete(sanPhamTra.key);
            $scope.listSanPhamTra = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
            if ($scope.mapSanPhamThayThe.has(sanPhamTra.key)) {
                $scope.mapSanPhamThayThe.delete(sanPhamTra.key);
                $scope.listSanPhamDoi = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
                $scope.tinhTongTienHangDoi();
            }
            $scope.tinhTongTienHangTra();
        } else {
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
        }
    }

    $scope.deleteGiayDoi = function (sanPhamDoi) {
        if ($scope.mapSanPhamThayThe.has(sanPhamDoi.key)) {
            $scope.mapSanPhamThayThe.delete(sanPhamDoi.key);
            $scope.listSanPhamDoi = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
            $scope.tinhTongTienHangDoi();
        } else {
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm đổi");
        }
    }


    $scope.updateLyDo = function (key, selectedLyDo, ghiChu) {
        if ($scope.mapSanPhamTra.has(key)) {
            let value = $scope.mapSanPhamTra.get(key);

            value.lyDo = selectedLyDo;
            value.ghiChu = ghiChu;

            $scope.mapSanPhamTra.set(key, value);
            $scope.listSanPhamTra = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
        }
    }

    $scope.resetLyDo = function () {
        $scope.selectedLyDo = null;
        $scope.ghiChu = undefined;
    }

    $scope.addLyDoView = function (sanPhamTra) {
        $scope.sanPhamTra = sanPhamTra;
        $scope.resetLyDo();
        if ($scope.mapSanPhamTra.has($scope.sanPhamTra.key)) {
            $scope.selectedLyDo = $scope.mapSanPhamTra.get($scope.sanPhamTra.key).lyDo;
            $scope.ghiChu = $scope.mapSanPhamTra.get($scope.sanPhamTra.key).ghiChu;
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }


    $scope.addLyDo = function (selectedLyDo, ghiChu) {
        $scope.updateLyDo($scope.sanPhamTra.key, selectedLyDo, ghiChu);
        toastr["success"]("Lưu thành công!");
    }


    $scope.addToMapSanPhamThayThe = function (bienTheGiayDoi) {
        // Cấu trúc dữ liệu mới cho map
        $scope.mapSanPhamThayThe.set($scope.sanPhamTra.key, {
            bienTheGiay: bienTheGiayDoi
        });
        $scope.listSanPhamDoi = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
        $scope.tinhTongTienHangDoi();
    };

    $scope.selectedGiayDoi = function () {
        $scope.addToMapSanPhamThayThe($scope.giayChoosed);
        $scope.giayChoosed = null;
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
                        if (mauSacIdInt === variant.mauSac.id) {
                            const li = document.createElement('li');
                            li.textContent = `ID: ${variant.id}, GiaBan: ${variant.giaBan}`;
                            ul.appendChild(li);

                            // Tạo nút kích thước và xử lý sự kiện click
                            const sizeButton = document.createElement("button");
                            sizeButton.textContent = variant.kichThuoc.ten;
                            sizeButton.className = "btn border btn-size";


                            $scope.$watch('giayDetail', function (newGiayDetail, oldGiayDetail) {
                                $scope.giayDetail = newGiayDetail;
                            });

                            sizeButton.addEventListener("click", () => {
                                const listButton = document.querySelectorAll(".btn-size");
                                listButton.forEach((button) => {
                                    button.classList.remove("click-button");
                                });

                                sizeButton.classList.add("click-button");
                                $scope.giayDetail = variant;
                                $scope.giayChoosed = variant;
                                $scope.giayChoosed.ten = productData.ten;
                                $scope.$apply();
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
                // if (!buttonsContainer.firstClickExecuted) {
                //     outerDiv.click();
                //     buttonsContainer.firstClickExecuted = true;
                // }
                // const firstSizeButton = sizeButtons.querySelector(".btn-size");
                // if (firstSizeButton && !buttonsContainer.firstSizeButtonClickExecuted) {
                //     firstSizeButton.click();
                //     buttonsContainer.firstSizeButtonClickExecuted = true;
                // }
            }
        }
    }

    function displayImages(imageList) {
        const carouselInner = document.querySelector('#carouselExampleControls .carousel-inner');
        const carouselItems = document.querySelectorAll('#carouselExampleControls .carousel-item');
        let imageUrl = 'https://product.hstatic.net/1000361048/product/dz0482_001_a_d718733fb6d344febec840aee172ca23_master.jpg';
        // Xóa tất cả các carousel items hiện tại
        carouselItems.forEach(item => {
            carouselInner.removeChild(item);
        });

        // Tạo các carousel items mới từ danh sách ảnh mới
        if (imageList.length > 0) {
            for (let i = 0; i < imageList.length; i++) {
                imageUrl = imageList[i];
                const div = document.createElement('div');
                div.className = i === 0 ? 'carousel-item active' : 'carousel-item';

                const img = document.createElement('img');
                img.src = imageUrl;
                img.className = 'd-block w-100';

                div.appendChild(img);
                carouselInner.appendChild(div);
            }
        }
    }


    $scope.addYeuCau = function () {
        let coPhanTuKhongHopLe = false;

        $scope.mapSanPhamTra.forEach((sanPhamTra, key) => {
            if (!sanPhamTra.lyDo || !sanPhamTra.ghiChu) {
                toastr["error"]("Bạn chưa điền đủ thông tin lý do hoặc ghi chú cho sản phẩm có mã: " + key);
                coPhanTuKhongHopLe = true;
            }
        });

        if (!$scope.thongTinNhanHang.tenNguoiNhan || !$scope.thongTinNhanHang.soDienThoaiNhan || !$scope.thongTinNhanHang.diaChiNhan) {
            toastr["error"]("Bạn chưa điền đủ thông tin nhận hàng");
            coPhanTuKhongHopLe = true;
        }

        if ($scope.mapSanPhamTra.size === 0) {
            toastr["error"]("Bạn chưa chọn sản phẩm trả");
            coPhanTuKhongHopLe = true;
        }


        // Nếu tất cả các entry đều hợp lệ, thực hiện xử lý tiếp theo
        if (!coPhanTuKhongHopLe) {
            $scope.mapSanPhamTra.forEach((sanPhamTra, key) => {
                let sanPhamDoi = $scope.mapSanPhamThayThe.has(key) ? $scope.mapSanPhamThayThe.get(key) : {
                    bienTheGiay: null
                };

                let tienHoanKhach = sanPhamTra.hoaDonChiTiet.donGia - (sanPhamTra.hoaDonChiTiet.donGia * $scope.hoaDon.phanTramGiam / 100);
                let tienGiamSanPhamDoi = sanPhamDoi.khuyenMai > 0 ? sanPhamDoi.giaBan * sanPhamDoi.khuyenMai / 100 : 0;
                // Cập nhật mapYeuCauChiTiet
                $scope.mapYeuCauChiTiet.set(key, {
                    hoaDonChiTiet: sanPhamTra.hoaDonChiTiet.id,
                    bienTheGiay: sanPhamDoi.bienTheGiay ? sanPhamDoi.bienTheGiay.id : null,
                    lyDo: sanPhamTra.lyDo,
                    thanhTien: sanPhamDoi.bienTheGiay ? tienHoanKhach - (sanPhamDoi.bienTheGiay.giaBan - tienGiamSanPhamDoi) : tienHoanKhach,
                    tienGiam: tienGiamSanPhamDoi,
                    bienTheGiayTra: sanPhamTra.hoaDonChiTiet.bienTheGiay.id,
                    soLuongTra: 1,
                    trangThai: 1,
                    tinhTrangSanPham: false,
                    ghiChu: sanPhamTra.ghiChu,
                    loaiYeuCauChiTiet: sanPhamDoi.bienTheGiay ? 1 : 2
                });
            });
            console.log(Array.from($scope.mapYeuCauChiTiet.values()))

            $scope.yeuCau.trangThai = 1;
            $scope.yeuCau.hoaDon = $scope.hoaDon.id;
            $scope.yeuCau.nguoiTao = $scope.hoaDon.khachHang.id;
            $scope.yeuCau.thongTinNhanHang = '' + $scope.thongTinNhanHang.tenNguoiNhan + '-' + $scope.thongTinNhanHang.soDienThoaiNhan + '-' + $scope.thongTinNhanHang.diaChiNhan;
            $scope.yeuCau.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet.values());

            console.log($scope.yeuCau)
            // console.log($scope.yeuCau)
            $http.post(host + '/user/rest/yeu-cau-khach-hang/add', JSON.stringify($scope.yeuCau))
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

            console.log(JSON.stringify($scope.yeuCau))
        }
    };


    $scope.getOneBienTheGiay = function (giay) {
        $http.get(host + '/admin/rest/giay/' + giay.id)
            .then(function (response) {
                $scope.giaySeletect = response.data;
                let lstBienTheGiay = $scope.giaySeletect.lstBienTheGiay;
                let giaThapNhat = lstBienTheGiay[0].giaBan;
                let giaLonNhat = lstBienTheGiay[0].giaBan;

                let uniqueMauSacSet = new Set();

                let lstMauSac = [];

                for (let bienTheGiay of lstBienTheGiay) {
                    if (bienTheGiay.giaBan < giaThapNhat) {
                        giaThapNhat = bienTheGiay.giaBan;
                    }

                    if (bienTheGiay.giaBan > giaLonNhat) {
                        giaLonNhat = bienTheGiay.giaBan;
                    }
                    if (bienTheGiay.mauSac && bienTheGiay.mauSac.maMau) {
                        let maMau = bienTheGiay.mauSac.maMau;

                        if (!uniqueMauSacSet.has(maMau)) {
                            lstMauSac.push(maMau);
                            uniqueMauSacSet.add(maMau);
                        }
                    }
                }
                $scope.giaySeletect.giaThapNhat = giaThapNhat;
                $scope.giaySeletect.giaLonNhat = giaLonNhat;
                $scope.giaySeletect.lstMauSac = lstMauSac;
                detailGiayChiTiet($scope.giaySeletect);
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu biến thể giày thất bại");
            console.log(error);
            $location.path("/list");
        });
    }


    function getListHoaDonChiTiet(id) {
        $scope.isLoading = true;
        $http.get(host + '/user/rest/yeu-cau-khach-hang/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                $scope.thongTinNhanHang.soDienThoaiNhan = $scope.hoaDon.khachHang.soDienThoai;
                $scope.thongTinNhanHang.diaChiNhan = $scope.hoaDon.diaChiNhan;
                $scope.thongTinNhanHang.tenNguoiNhan = $scope.hoaDon.khachHang.hoTen;
                $scope.listHoaDonChiTiet = response.data.listHoaDonChiTiet;
                $scope.isLoading = false;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Chuyển hóa đơn thất bại. Vui lòng thử lại");
            $scope.isLoading = false;
        });
    }

    function getListLyDo() {
        $http.get(host + '/admin/rest/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function searchGiay(giaySearch) {
        $scope.isLoading = true;
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

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
                for (let bienTheGiayObject of $scope.listGiay) {
                    let lstBienTheGiay = bienTheGiayObject.lstBienTheGiay;
                    let giaThapNhat = lstBienTheGiay[0].giaBan;
                    let giaLonNhat = lstBienTheGiay[0].giaBan;

                    let uniqueMauSacSet = new Set();

                    let lstMauSac = [];

                    for (let bienTheGiay of lstBienTheGiay) {
                        if (bienTheGiay.giaBan < giaThapNhat) {
                            giaThapNhat = bienTheGiay.giaBan;
                        }

                        if (bienTheGiay.giaBan > giaLonNhat) {
                            giaLonNhat = bienTheGiay.giaBan;
                        }
                        if (bienTheGiay.mauSac && bienTheGiay.mauSac.maMau) {
                            let maMau = bienTheGiay.mauSac.maMau;

                            if (!uniqueMauSacSet.has(maMau)) {
                                lstMauSac.push(maMau);
                                uniqueMauSacSet.add(maMau);
                            }
                        }
                    }
                    bienTheGiayObject.giaThapNhat = giaThapNhat;
                    bienTheGiayObject.giaLonNhat = giaLonNhat;
                    bienTheGiayObject.lstMauSac = lstMauSac;
                }
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    //Chọn địa chỉ
    $scope.diaChiNhanHangMoi = {};

    $scope.saveDiaChi = function () {
        let diaChiNhan = $scope.diaChiNhanHangMoi.addDress + '-' + $scope.diaChiNhanHangMoi.wards.ten + ',' + $scope.diaChiNhanHangMoi.districts.ten + ',' + $scope.diaChiNhanHangMoi.provinces.ten;
        $scope.thongTinNhanHang.diaChiNhan = diaChiNhan;
        toastr["success"]("Lưu địa chỉ nhận thành công!");
    }

    $scope.checkAddDress = function () {
        if ($scope.diaChiNhanHangMoi.provinces && $scope.diaChiNhanHangMoi.districts && $scope.diaChiNhanHangMoi.wards && $scope.diaChiNhanHangMoi.addDress) {
            return false;
        }
        return true;
    }

    //lấy dữ liệu huyện theo id tỉnh
    $scope.changeProvince = function () {
        $http.get(host + "/rest/provinces/get-all")
            .then(function (response) {
                $scope.provinces = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu tỉnh thất bại");
            });
    }

    //lấy dữ liệu huyện theo id tỉnh
    $scope.changeDistrict = function () {
        if (!$scope.diaChiNhanHangMoi.provinces.id) {
            toastr["error"]("Bạn chưa chọn tỉnh/thành phố");
        }
        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHangMoi.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                // console.log($scope.diaChiNhanHang.provinces)
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ huyện thất bại");
            });
    }

    // lấy dữ liệu theo xã theo huyện
    $scope.changeWards = function () {
        if (!$scope.diaChiNhanHangMoi.districts.id) {
            toastr["error"]("Bạn chưa chọn quận/huyện");
        } else {
            $http.get(host + "/rest/wards/" + $scope.diaChiNhanHangMoi.districts.id)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ liệu xã thất bại");
                });
        }
    }

    document.getElementById('tenNguoiNhan').addEventListener('input', function (e) {
        let value = e.target.value;
        // Cho phép các chữ cái, số, khoảng trắng và ký tự tiếng Việt
        value = value.replace(/[^a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ _]/g, "");
        e.target.value = value;
    });

    document.getElementById('soDienThoaiNhan').addEventListener('input', function (e) {
        let value = e.target.value;
        // Loại bỏ mọi ký tự không phải là số và kiểm tra định dạng
        value = value.replace(/[^\d]/g, "").replace(/^(0[0-9]{9})?.*$/, '$1');
        e.target.value = value;
    });

    document.getElementById('addDress').addEventListener('input', function (e) {
        let value = e.target.value;
        // Loại bỏ ký tự đặc biệt ngoại trừ dấu phẩy và cho phép tiếng Việt có dấu
        value = value.replace(/[^a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ ,]/g, "");
        e.target.value = value;
    });


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