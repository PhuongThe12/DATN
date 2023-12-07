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

    $scope.mapYeuCauChiTiet = new Map(), $scope.mapYeuCauChiTietSaved = new Map();
    $scope.listHoaDonChiTiet = [], $scope.listLyDo = [], $scope.listGiaySearch = [];
    $scope.giayChoosed = {}, $scope.giaySearch = {}, $scope.hoaDon = {};

    $scope.curPage = 1, $scope.maxSize = 5, $scope.giaySearch.pageSize = 12, $scope.giaySearch.trangThai = 1;
    $scope.tongTienHangTra = 0, $scope.tongTienDoiHang = 0, $scope.tongTienHangDoi = 0, $scope.tongTienGiamGia = 0, $scope.tongTienThanhToan = 0, $scope.phiVanChuyen = 0, $scope.soLuongDoi = 0, $scope.feeShippingPerOne = 0;
    $scope.thongTinNhanHang = {}, $scope.thongTinNhanHang.soDienThoaiNhan = '', $scope.thongTinNhanHang.diaChiNhan = '', $scope.thongTinNhanHang.tenNguoiNhan = '', $scope.diaChiNhanHangMoi = {};
    $scope.yeuCau = {}, $scope.soLuongDaTra = 0;


    $scope.$watch('curPage + numPerPage', function () {
        $scope.isLoading = true;
        getListHoaDonChiTiet(idHoaDon);
        console.log()
        searchGiay($scope.giaySearch)
        getListLyDo();
    });



    $scope.$watch('feeShippingPerOne', function (value) {
        tinhTienShip();
    });


    function tinhTongTienThanhToan() {
        $scope.tongTienThanhToan = 0;
        let tongTienThanhToan = $scope.tongTienHangTra - ($scope.tongTienDoiHang + $scope.phiVanChuyen + $scope.phiVanChuyen) ;
        $scope.tongTienThanhToan = tongTienThanhToan > 0 ?  tongTienThanhToan : Math.abs(tongTienThanhToan);
    }

    function tinhTienShip() {
        $scope.phiVanChuyen = 0;
        if ($scope.soLuongDoi <= 0) {
            return;
        }
        let soDonHang;
        if ($scope.soLuong % 30 === 0) {
            soDonHang = $scope.soLuongDoi / 30;
        } else {
            soDonHang = ($scope.soLuongDoi / 30) + 1;
        }
        $scope.phiVanChuyen = Math.round(soDonHang * $scope.feeShippingPerOne);
        tinhTongTienThanhToan();
    }


    function tinhTongtienGiamGia() {
        $scope.tongTienGiamGia = 0;

        $scope.mapYeuCauChiTiet.forEach((item) => {
            if(item.bienTheGiayDoi.id) {
                let giaBan = item.bienTheGiayDoi.giaBan;
                let khuyenMai = item.bienTheGiay.khuyenMai / 100;
                let tienGiam = giaBan * khuyenMai;
                $scope.tongTienGiamGia += tienGiam;
            }
        })
    }


    function tinhTongTienHangDoi() {
        $scope.tongTienHangDoi = 0;
        $scope.soLuongDoi = 0;
        $scope.mapYeuCauChiTiet.forEach((item) => {
            if(item.bienTheGiayDoi.id) {
                let giaBan = item.bienTheGiayDoi.giaBan;
                $scope.tongTienHangDoi += giaBan;
                $scope.soLuongDoi += 1;
            }
        })
        tinhTongtienGiamGia();
        tinhTienShip();
    }


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
            let key = baseId;
            if (!$scope.mapYeuCauChiTiet.has(key)) {
                let valueCopy = angular.copy(hoaDonChiTiet);
                valueCopy.soLuongTra = 1;

                $scope.mapYeuCauChiTiet.set(key, {
                    hoaDonChiTiet: valueCopy, // value1
                    bienTheGiayDoi: null,
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
            tinhTongTienHangDoi();
        } else {
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
        }
    }

    $scope.deleteGiayDoi = function (yeuCauChiTiet) {
        if ($scope.mapYeuCauChiTiet.has(yeuCauChiTiet.key)) {
            let value = $scope.mapYeuCauChiTiet.get(yeuCauChiTiet.key);
            $scope.mapYeuCauChiTiet.set(yeuCauChiTiet.key, {
                hoaDonChiTiet: value.hoaDonChiTiet, // value1
                bienTheGiayDoi: null,               // value2
                lyDo: value.lyDo,                   // value3
                ghiChu: value.ghiChu                // value4
            });
            $scope.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value}));
            tinhTongTienHangDoi();
        } else {
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
        }
    }


    $scope.addLyDoView = function (yeuCauChiTiet) {
        $scope.yeuCauChiTiet = yeuCauChiTiet;
        if($scope.mapYeuCauChiTiet.get(yeuCauChiTiet.key)){
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


    $scope.selectedGiayDoi = function () {
        if($scope.listYeuCauChiTiet.has($scope.yeuCauChiTiet.key)){
            let value = $scope.mapYeuCauChiTiet.get($scope.yeuCauChiTiet.key);
            $scope.mapYeuCauChiTiet.set($scope.yeuCauChiTiet.key, {
                hoaDonChiTiet: value.hoaDonChiTiet, // value1
                bienTheGiayDoi: $scope.giayChoosed,
                lyDo: value.lyDo,              // value2
                ghiChu: value.ghiChu               // value3
            });
            $scope.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTiet, ([key, value]) => ({key, value}));
            $scope.giayChoosed = null;
            tinhTongTienHangDoi();
        }else{
            toastr["error"]("Không tồn tại yêu cầu chi tiết có key: " + $scope.yeuCauChiTiet.key);
        }
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

    $scope.taoYeuCau = function () {
        let coPhanTuKhongHopLe = false;

        $scope.mapYeuCauChiTiet.forEach((yeuCauChiTiet, key) => {
            if (!yeuCauChiTiet.lyDo || !yeuCauChiTiet.ghiChu) {
                toastr["error"]("Bạn chưa điền đủ thông tin lý do hoặc ghi chú cho sản phẩm có mã: " + key);
                coPhanTuKhongHopLe = true;
            }
        });

        // if (!$scope.thongTinNhanHang.tenNguoiNhan || !$scope.thongTinNhanHang.soDienThoaiNhan || !$scope.thongTinNhanHang.diaChiNhan) {
        //     toastr["error"]("Bạn chưa điền đủ thông tin nhận hàng");
        //     coPhanTuKhongHopLe = true;
        // }

        if ($scope.mapYeuCauChiTiet.size === 0) {
            toastr["error"]("Vui lòng thêm sản phẩm trả!");
            coPhanTuKhongHopLe = true;
        }


        // Nếu tất cả các entry đều hợp lệ, thực hiện xử lý tiếp theo
        if (!coPhanTuKhongHopLe) {
            $scope.mapYeuCauChiTiet.forEach((yeuCauChiTiet, key) => {
                let sanPhamDoi = $scope.mapYeuCauChiTiet.has(key) ? $scope.mapYeuCauChiTiet.get(key).bienTheGiayDoi : {
                    bienTheGiayDoi: null
                };

                let sanPhamTra = $scope.mapYeuCauChiTiet.has(key) ? $scope.mapYeuCauChiTiet.get(key).hoaDonChiTiet.bienTheGiay : {
                    bienTheGiay: null
                };

                let tienHoanKhach = yeuCauChiTiet.hoaDonChiTiet.donGia - (yeuCauChiTiet.hoaDonChiTiet.donGia * ($scope.hoaDon.phanTramGiam / 100));
                let tienGiamSanPhamDoi = sanPhamDoi ? sanPhamDoi.giaBan * sanPhamDoi.khuyenMai / 100 : 0;

                $scope.mapYeuCauChiTietSaved.set(key, {
                    hoaDonChiTiet: yeuCauChiTiet.hoaDonChiTiet.id,
                    bienTheGiay: yeuCauChiTiet.bienTheGiay ? yeuCauChiTiet.bienTheGiay.id : null,
                    lyDo: yeuCauChiTiet.lyDo,
                    trangThai: 1,
                    ghiChu: yeuCauChiTiet.ghiChu,
                    loaiYeuCauChiTiet: yeuCauChiTiet.bienTheGiay ? 1 : 2,
                    tinhTrangSanPham: false,
                    tienGiam: yeuCauChiTiet.bienTheGiay ? tienGiamSanPhamDoi : 0,
                    thanhTien: tienHoanKhach ? tienHoanKhach : 0,
                });
            });


            $scope.yeuCau.hoaDon = $scope.hoaDon.id;
            //Người thực hiện
            $scope.yeuCau.trangThai = 1;
            //Ngày tạo
            //Ngày Sửa
            $scope.yeuCau.nguoiTao = $scope.hoaDon.khachHang.id;
            //Ghi chú
            //Id hóa đơn đổi trả
            $scope.yeuCau.thongTinNhanHang = '' + $scope.thongTinNhanHang.tenNguoiNhan + '-' + $scope.thongTinNhanHang.soDienThoaiNhan + '-' + $scope.thongTinNhanHang.diaChiNhan;
            $scope.yeuCau.phiShip = $scope.feeShippingPerOne;
            //Người Tạo
            //Người Sửa
            $scope.yeuCau.listYeuCauChiTiet = Array.from($scope.mapYeuCauChiTietSaved.values());

            $http.post(host + '/user/rest/yeu-cau-khach-hang/add', JSON.stringify($scope.yeuCau))
                .then(function (response) {
                    if (response.status === 200) {
                        toastr["success"]("Thêm thành công");
                    }
                    $location.path("/home");
                })
                .catch(function (error) {
                    toastr["error"]("Thêm thất bại");
                    if (error.status === 400) {
                        $scope.addYeuCauForm.hoaDon.$dirty = false;
                        $scope.errors = error.data;
                    }
                });
        }
    };

    $scope.getOneBienTheGiay = function (giay) {
        $http.get(host + '/rest/admin/giay/' + giay.id)
            .then(function (response) {
                $scope.giaySeletect = response.data;
                console.log($scope.giaySeletect)
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


    function searchGiay(giaySearch) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/giay/get-all-giay';

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

    function getListHoaDonChiTiet(id) {
        $scope.isLoading = true;
        $http.get(host + '/user/rest/yeu-cau-khach-hang/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                console.log($scope.hoaDon)
                $scope.listHoaDonChiTiet = response.data.listHoaDonChiTiet;
                console.log($scope.listHoaDonChiTiet)
                $scope.thongTinNhanHang.soDienThoaiNhan = $scope.hoaDon.khachHang.soDienThoai;
                $scope.thongTinNhanHang.diaChiNhan = $scope.hoaDon.diaChiNhan;
                $scope.thongTinNhanHang.tenNguoiNhan = $scope.hoaDon.khachHang.hoTen;
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




    $scope.saveDiaChi = function () {
        let diaChiNhan = $scope.diaChiNhanHangMoi.addDress + '-' + $scope.diaChiNhanHangMoi.wards.ten + ',' + $scope.diaChiNhanHangMoi.districts.ten + ',' + $scope.diaChiNhanHangMoi.provinces.ten;
        $scope.thongTinNhanHang.diaChiNhan = diaChiNhan;
        toastr["success"]("Lưu địa chỉ nhận thành công!");
        $scope.isLoading = true;

        if ($scope.tongTienDoiHang - $scope.tongTienHangTra > 0 && $scope.tongTienDoiHang - $scope.tongTienHangTra < 80000000) {
            logisticInfo.insurance_value = ($scope.tongTienDoiHang - $scope.tongTienHangTra);
        }

        $http.post('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/preview', logisticInfo)
            .then(response => {
                $scope.feeShippingPerOne = response.data.data.total_fee;
                tinhTienShip();
                $scope.isLoading = false;
            })
            .catch(error => {
                toastr["error"]("Lấy thông tin thất bại");
                $scope.feeShippingPerOne = 50000;
                $scope.isLoading = false;
            })
    }

    $scope.checkAddDress = function () {
        if ($scope.diaChiNhanHangMoi.provinces && $scope.diaChiNhanHangMoi.districts && $scope.diaChiNhanHangMoi.wards && $scope.diaChiNhanHangMoi.addDress) {
            return false;
        }
        return true;
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