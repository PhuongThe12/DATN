var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/banhang/views/home.html', controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});


app.controller("homeController", function ($scope, $http, $location, $cookies, $rootScope) {

    $scope.curPage = 1, $scope.itemsPerPage = 12, $scope.maxSize = 5;
    $scope.hoaDon = {};

    $scope.selectedHoaDon = {};

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
    $scope.phuongThucTaiQuay = $scope.phuongThucTaiQuays.find(item => item.id === 1);

    $scope.getAllDotGiamGia = function () {
        $http.get(host + "/admin/rest/dot-giam-gia/get-all-active")
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

        $http.post(host + "/admin/rest/khach-hang/search-by-name", $scope.khachHangSearch)
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

    $scope.$watch('tongTien', function () {
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
        }
        setTongTienPhaiTra();

    });


    $scope.selectKhachHang = function (khachHang) {
        if (!$scope.selectedHoaDon.id) {
            toastr["error"]("Bạn chưa chọn hóa đơn");
            document.getElementById('closeModalKhachHang').click();
            return;
        }
        const reqest = {
            idHoaDon: $scope.selectedHoaDon.id,
            idGiay: khachHang.id
        }

        $http.post(host + "/admin/rest/hoa-don/add-khach-hang", reqest)
            .then(response => {
                $scope.selectedKhachHang = response.data;
            })
            .catch(err => {
                toastr["error"]("Có lỗi. Vui lòng thử lại");
            });

        setTongTienPhaiTra();
        document.getElementById('closeModalKhachHang').click();
    }

    $scope.$watch('selectedKhachHang', function () {
        if ($scope.selectedKhachHang) {
            if ($scope.selectedKhachHang.id) {
                $scope.uuDai = $scope.selectedKhachHang.hangKhachHang;
            } else {
                $scope.uuDai = {uuDai: 0};
            }
            setTongTienPhaiTra();
        }
    });

    function setTongTienPhaiTra() {
        let tongPhanTramGiam = 0;
        if (!isNaN($scope.uuDai.uuDai)) {
            tongPhanTramGiam += $scope.uuDai.uuDai;
        }
        if (!isNaN($scope.dotGiamGiaSelect.phanTramGiam)) {
            tongPhanTramGiam += $scope.dotGiamGiaSelect.phanTramGiam;
        }

        $scope.tongTienGiam = ($scope.tongTien * tongPhanTramGiam / 100)
        $scope.tongTienPhaiTra = $scope.tongTien - $scope.tongTienGiam;
    }

    setTienThuaTaiQuay = function () {
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
        }
        setTienThuaTaiQuay();
    }

    $scope.selecteHoaDon = function (id) { // chọn hóa đơn
        if ($scope.selectedHoaDon.id === id) {
            return;
        }

        $http.get(host + '/admin/rest/hoa-don/get-full-response/' + id)
            .then(function (response) {
                const select = response.data;
                $scope.selectedHoaDon = select;
                $scope.listGiaySelected = [];
                $scope.tongTien = 0;
                $scope.selectedKhachHang = select.khachHangRestponse;
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
                });
                toastr["success"]("Chuyển hóa đơn thành công");
                $scope.listGiaySelected.sort((a, b) => (a.id - b.id));
                resetTien();

            })
            .catch(function (error) {
                toastr["error"]("Chuyển hóa đơn. Vui lòng thử lại");
            })

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

        if (isNaN(giay.soLuongMua) || giay.soLuongMua < 0) {
            giay.soLuongMua = $scope.oldValue[giay.id];
            toastr["error"]("Số lượng không được âm");
            return;
        }

        if (giay.soLuongMua === $scope.oldValue[giay.id]) {
            return;
        }

        $http.get(host + '/admin/rest/giay/' + giay.idBienThe + "/so-luong")
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
                    $http.post(host + '/admin/rest/hoa-don/add-product', requestData)
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
                            toastr["error"](error.data.data);
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
                $http.delete(host + "/admin/rest/hoa-don/" + id)
                    .then(function (response) {
                        let foundIndex = $scope.hoaDons.findIndex(hd => hd.id === id);
                        if (foundIndex) {
                            $scope.hoaDons.splice(foundIndex, 1);
                            toastr["success"]("Hủy thành công");
                        }
                    })
                    .catch(function (error) {
                        console.log(error);
                        toastr["error"]("Lấy dữ liệu thất bại");
                    });
                $scope.isLoading = false;
            }
        });
    }

    function getHoaDonChuaThanhToan() {
        $scope.isLoading = true;
        $http.get(host + "/admin/rest/hoa-don/chua-thanh-toan")
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
        $http.post(host + '/admin/rest/hoa-don/new-hoa-don')
            .then(function (response) {
                $scope.hoaDons.push(response.data);
                $scope.selectedHoaDon = response.data;
                toastr["success"]("Tạo mới thành công");
            })
            .catch(function (error) {
                toastr["error"]("Tạo mới thất bại. Vui lòng thử lại");
            })
    }

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

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

        document.getElementById('buttonModalSanPham').click();

        $scope.checkExits = $scope.listGiaySelected.find(function (giay) {
            return giay.id === id;
        });

        if ($scope.checkExits === undefined) {

            $http.get(host + '/admin/rest/giay/' + id)
                .then(function (response) {
                    $scope.giaySeletect = response.data;
                    detailGiayChiTiet(response.data);
                }).catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                console.log(error);
                $location.path("/home");
            });
        } else {
            $scope.checkExits.soLuong++;
            $scope.totalPrice += $scope.checkExits.gia;
        }

    }

    $scope.thanhToanTaiQuay = function () {
        if (!$scope.tienThuaTaiQuay || $scope.tienMatTaiQuay < 0) {
            toastr["error"]("Tiền khách trả chưa đủ");
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
                    idDieuKien: $scope.dotGiamGiaSelect.id,
                    tienGiam: $scope.tongTienGiam,
                    ghiChu: $scope.ghiChuTaiQuay
                }

                if ($scope.phuongThucTaiQuay === 1) {
                    request.phuongThuc = 1;
                    request.tienMat = $scope.tienMatTaiQuay;
                } else if ($scope.phuongThucTaiQuay === 2) {
                    request.phuongThuc = 2;
                    request.tienChuyenKhoan = $scope.chuyenKhoanTaiQuay;
                } else {
                    request.phuongThuc = 3;
                    request.tienMat = $scope.tienMatTaiQuay;
                    request.tienChuyenKhoan = $scope.chuyenKhoanTaiQuay;
                }

                $scope.isLoading = true;
                $http.post(host + "/admin/rest/hoa-don/thanh-toan", request)
                    .then(response => {
                        const index = $scope.hoaDons.findIndex(item => item.id === response.data);
                        $scope.hoaDons.splice(index, 1);
                        toastr["success"]("Thanh toán thành công");
                        resetHoaDon();
                        $scope.isLoading = false;
                    })
                    .catch(err => {
                        console.log(err);
                        toastr["error"]("Có lỗi vui lòng thử lại");
                        $scope.isLoading = false;
                    });
            }
        });
    }

    function resetHoaDon() {
        $scope.listGiaySelected = [];
        $scope.dotGiamGias = [];

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
                            sizeButton.className = "btn border";

                            $scope.$watch('giayDetail', function (newGiayDetail, oldGiayDetail) {
                                $scope.giayDetail = newGiayDetail;
                            });

                            sizeButton.addEventListener("click", () => {
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
                        $http.delete(host + '/admin/rest/hoa-don/delete-hdct/' + id)
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
                    $http.delete(host + '/admin/rest/hoa-don/delete-all-hdct/' + $scope.selectedHoaDon.id)
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

        document.getElementById('closeModalSanPham').click();
    }

    $scope.addNewHDCT = function (data) {
        $scope.isLoading = true;
        $http.post(host + '/admin/rest/hoa-don/add-new-hdct', data)
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
                toastr["error"]("Thêm thất bại. " + error.data.data);
            });
        $scope.listGiaySelected.sort((a, b) => (a.id - b.id));
        $scope.isLoading = false;
    }


    $scope.addToOrder = function (data) {
        $scope.isLoading = true;
        $http.post(host + '/admin/rest/hoa-don/add-product', data)
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

    $scope.startScanning = function () {
        if (!$scope.selectedHoaDon || !$scope.selectedHoaDon.id) {
            toastr["warning"]("Bạn chưa chọn hóa đơn");
            return;
        }

        document.getElementById('staticBackDropButton').click();
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
                            let code = jsQR(imageData.data, imageData.width, imageData.height);

                            if (code) {
                                // Thực hiện các hành động với mã QR tại đây
                                // document.getElementById('closeModalCamera').click();
                                // clearInterval(interval);
                                scanning = false;
                                $http.get(host + '/admin/rest/giay/bien-the/' + code.data)
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
                                }, 1500);

                            } else {
                                Quagga.decodeSingle({
                                    src: convertImageDataToBase64(imageData), numOfWorkers: 0, decoder: {
                                        readers: ['ean_reader', 'code_128_reader', 'code_39_reader']
                                    },
                                }, function (result) {
                                    if (result && result.codeResult) {
                                        $http.get(host + '/admin/rest/giay/bien-the/' + result.codeResult.code)
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
                                        }, 1500);
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
        if (video.srcObject) {
            video.srcObject.getTracks().forEach(track => track.stop());
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
        $http.post(host + '/admin/rest/khach-hang', $scope.khachHang)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
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

});

function nextInput(e) {
    if (e.which === 13) {
        e.preventDefault();
        e.target.blur();
    }
}
