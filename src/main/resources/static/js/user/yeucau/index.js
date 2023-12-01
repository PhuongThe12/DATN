var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/customer/yeucau/views/list.html', controller: 'yeuCauKhachHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/customer/yeucau/views/detail.html', controller: 'detailYeuCauKhachHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/customer/yeucau/views/update.html', controller: 'updateYeuCauKhachHangController'
    }).when("/add/:id", {
        templateUrl: '/pages/customer/yeucau/views/add.html', controller: 'addYeuCauKhachHangController'
    }).otherwise({redirectTo: '/list'});
});

app.controller("addYeuCauKhachHangController", function ($scope, $http, $window, $location) {
    $scope.listHoaDonChiTiet = [];
    $scope.hoaDon = {};

});


app.controller("addYeuCauKhachHangController", function ($scope, $http, $routeParams, $window, $location) {
    const idHoaDon = $routeParams.id;
    $scope.mapSanPhamTra = new Map(), $scope.mapSanPhamThayThe = new Map();
    $scope.listHoaDonChiTiet = [], $scope.listLyDo = [], $scope.listGiaySearch = [];
    $scope.giayChoosed = {}, $scope.giaySearch = {}, $scope.hoaDon = {};
    $scope.curPage = 1, $scope.maxSize = 5, $scope.giaySearch.pageSize = 24, $scope.giaySearch.trangThai = 1;
    $scope.tongTienTraHang = 0,$scope.tongTienDoiHang = 0;
    let searchText = '';

    $scope.$watch('curPage + numPerPage', function () {
        $scope.isLoading = true;
        getListHoaDonChiTiet(idHoaDon);
        searchGiay($scope.giaySearch)
        getListLyDo();
    });



    $scope.tinhTienTraHang = function() {
        $scope.tongTienTraHang = 0;

        $scope.mapSanPhamTra.forEach((item) => {
            let hoaDonChiTiet = item.hoaDonChiTiet;
            let donGia = hoaDonChiTiet.donGia;
            let soLuong = hoaDonChiTiet.soLuongTra;
            let phanTramGiam = $scope.hoaDon.phanTramGiam / 100;

            let tienTraHang = (donGia * soLuong) - (donGia * soLuong * phanTramGiam);
            console.log("tiền trả hàng: "+tienTraHang)
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

        $scope.tinhTienTraHang();
    }

    $scope.deleteGiayTra = function (sanPhamTra) {
        // Swal.fire({
        //     text: "Xác nhận xóa sản phẩm trả ?",
        //     icon: "info",
        //     showCancelButton: true,
        //     confirmButtonColor: "#3085d6",
        //     cancelButtonColor: "#d33",
        //     confirmButtonText: "Đồng ý",
        //     cancelButtonText: "Hủy"
        // }).then((result) => {
        //     if (result.isConfirmed) {
        //         if ($scope.mapSanPhamTra.has(sanPhamTra.key)) {
        //             $scope.mapSanPhamTra.delete(sanPhamTra.key);
        //             $scope.listSanPhamTra = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
        //         } else {
        //             toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
        //         }
        //     }
        // });
        if ($scope.mapSanPhamTra.has(sanPhamTra.key)) {
            $scope.mapSanPhamTra.delete(sanPhamTra.key);
            $scope.listSanPhamTra = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
            $scope.tinhTienTraHang();
        } else {
            toastr["error"]("Không tồn tại giày này trong danh sách sản phẩm trả");
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
        $scope.resetLyDo();
        toastr["success"]("Lưu thành công!");
    }


    $scope.selectBienTheGiayDoiView = function (giay) {
        getOneBienTheGiayDoi(giay.id);
    }

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


    function getListHoaDonChiTiet(id) {
        $scope.isLoading = true;
        $http.get(host + '/user/rest/yeu-cau-khach-hang/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                console.log( $scope.hoaDon)
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
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    function getOneBienTheGiayDoi(id) {
        $http.get(host + '/admin/rest/giay/' + id)
            .then(function (response) {
                $scope.giaySeletect = response.data;
                detailGiayChiTiet(response.data);
                // document.getElementById('buttonModalSanPham').click();
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });
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