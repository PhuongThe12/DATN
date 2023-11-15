var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/banhang/views/home.html',
            controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});

app.directive('customSelect', function ($injector) {
    return {
        restrict: 'E',
        templateUrl: '/pages/admin/giay/views/combobox.html',
        scope: {
            id: '@',
            title: '@',
            items: '=',
            ngModel: '=',
            modalId: '@'
        },
        controller: function ($scope) {
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
                        }
                    }
                }
            }, true);

            $scope.showDetailComboboxModal = function (title, item) {
                event.stopPropagation();
                var DetailEntityService = $injector.get('DetailEntityService');
                let detailEntity = {title: title, ...item};
                DetailEntityService.setDetailEntity(detailEntity);

                setTimeout(function () {
                    document.getElementById('modalDetail').click();
                }, 0);

            }

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

// app.controller("indexController", function ($scope, $http, $location, $cookies) {
//     $http.get(host + '/admin/rest/nhan-vien/check-logged')
//         .then(function (response) {
//             if (response.status == 200) {
//                 $scope.nhanVienLogged = response.data;
//                 if ($scope.nhanVienLogged.chucVu == 2) {
//                     $scope.chuCuaHangLogged = true;
//                 } else if ($scope.nhanVienLogged.chucVu == 1) {
//                     $scope.chuCuaHangLogged = false;
//                 }
//             }
//         }).catch(function (error) {
//         toastr["error"]("Không tìm thấy người dùng , vui lòng đăng nhập lại !");
//     });
// });

app.controller("homeController", function ($scope, $http, $location, $cookies) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 5;
    $scope.hoaDon = {};

    let giaySearch = {};
    $scope.listGiaySelected = []; // List biến thể giày được chọn

    $scope.giayListSearch = [];

    $scope.filterGiay = [];

    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

    $scope.tongTien = 0; // Tổng tiền

    $scope.search = function () {
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }

        if ($scope.status === 0) {
            giaySearch.trangThai = 0;
        } else if ($scope.status === 1) {
            giaySearch.trangThai = 1;
        } else {
            giaySearch.trangThai = null;
        }


        if (!isNaN($scope.giaNhapSearch)) {
            giaySearch.giaNhap = $scope.giaNhapSearch;
        }

        if (!isNaN($scope.giaBanSearch)) {
            giaySearch.giaBan = $scope.giaBanSearch;
        }

        if ($scope.selectedThuongHieu) {
            giaySearch.thuongHieuIds = $scope.selectedThuongHieu.filter(
                thuongHieu => thuongHieu.status === 'active'
            ).map(th => th.id);
        }
        giaySearch.pageSize = 9999999;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                console.log(response);
                $scope.giayListSearch = response.data.content;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    };

    function initGiayList(){
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }

        if ($scope.status === 0) {
            giaySearch.trangThai = 0;
        } else if ($scope.status === 1) {
            giaySearch.trangThai = 1;
        } else {
            giaySearch.trangThai = null;
        }


        if (!isNaN($scope.giaNhapSearch)) {
            giaySearch.giaNhap = $scope.giaNhapSearch;
        }

        if (!isNaN($scope.giaBanSearch)) {
            giaySearch.giaBan = $scope.giaBanSearch;
        }

        if ($scope.selectedThuongHieu) {
            giaySearch.thuongHieuIds = $scope.selectedThuongHieu.filter(
                thuongHieu => thuongHieu.status === 'active'
            ).map(th => th.id);
        }

        $http.post(apiUrl, {
            pageSize : 9999999
        })
            .then(function (response) {
                $scope.giayListSearch = response.data.content;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    initGiayList();

    $scope.search = function() {
        $scope.filterGiay = [];
        angular.forEach($scope.giayListSearch, function(item) {
            if (item.ten.toLowerCase().includes($scope.searchText.toLowerCase())) {
                $scope.filterGiay.push(item);
            }
        });
    };

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }

        if ($scope.status === 0) {
            giaySearch.trangThai = 0;
        } else if ($scope.status === 1) {
            giaySearch.trangThai = 1;
        } else {
            giaySearch.trangThai = null;
        }


        if (!isNaN($scope.giaNhapSearch)) {
            giaySearch.giaNhap = $scope.giaNhapSearch;
        }

        if (!isNaN($scope.giaBanSearch)) {
            giaySearch.giaBan = $scope.giaBanSearch;
        }

        if ($scope.selectedThuongHieu) {
            giaySearch.thuongHieuIds = $scope.selectedThuongHieu.filter(
                thuongHieu => thuongHieu.status === 'active'
            ).map(th => th.id);
        }

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                console.log(response);
                $scope.giays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.isLoading = true;
    getData(1);

    $scope.$watch('curPage + numPerPage', function () {
        $scope.isLoading = true;
        getData($scope.curPage);
    });


    // Thêm giày vào giỏ hàng
    $scope.addOrder = function (id) {

        $scope.checkExits = $scope.listGiaySelected.find(function (giay) {
            return giay.id == id;
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

    $scope.deleteSelected = function (id) {
        for (var i = 0; i < $scope.listGiaySelected.length; i++) {
            if ($scope.listGiaySelected[i].id === id) {

                // Tìm thấy phần tử có id trùng khớp, loại bỏ nó khỏi mảng
                var deletedItem = $scope.listGiaySelected.splice(i, 1)[0];

                // Cập nhật tổng tiền sau khi xóa sản phẩm
                $scope.tongTien -= (deletedItem.gia * deletedItem.soLuong);
                Swal.fire({
                    text: "Xóa thành công",
                    icon: "success"
                });
                break; // Dừng vòng lặp sau khi xóa thành công
            }
        }
    }

    // Hàm thêm biến thể giày sau khi chọn "Thêm vào giỏ haàng"
    $scope.addGiay = function () {
        var exists = $scope.listGiaySelected.some(function (item) {
            // Kiểm tra điều kiện, ví dụ so sánh ID của đối tượng
            return item.id === $scope.giayChoosed.id;
        });

        if (exists) {
            Swal.fire({
                text: "Đã tồn tại giày trong giỏ hàng",
                icon: "error"
            });
        } else {
            Swal.fire({
                text: "Thêm thành công",
                icon: "success"
            });
            $scope.giayChoosed.soLuongMua = 1;
            $scope.listGiaySelected.push($scope.giayChoosed);
            $scope.tongTien += $scope.giayChoosed.giaBan;
        }
    }

    $scope.taoHoaDon = function () {
        $scope.hoaDon.listBienTheGiay = $scope.listGiaySelected;
        $scope.hoaDon.tongTien = $scope.tongTien;
        console.log($scope.hoaDon);
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


});