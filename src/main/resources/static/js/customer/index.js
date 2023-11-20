const app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/user/views/list.html',
            controller: 'listProductController'
        }).when("/detail/:id", {
        templateUrl: '/pages/user/views/detail-product.html',
        controller: 'detailProductController'
    })
        .otherwise({redirectTo: '/list'});
});

app.controller('abc', function ($scope, $http, $location, $cookies) {

    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;
    $scope.loadLocalStorage = function () {
        var gioHangFromCookies = localStorage.getItem('gioHang') || '[]';
        $scope.gioHang = JSON.parse(gioHangFromCookies);
        $scope.gioHang.sort(function (a, b) {
            return a.idBienTheGiay - b.idBienTheGiay;
        });
        console.log($scope.gioHang);
        var idList = $scope.gioHang.map(function (item) {
            return item.idBienTheGiay || item.bienTheGiay;
        });
        var resultJson = {"ids": idList};
        $http.post("http://localhost:8080/admin/rest/giay/bien-the/get-all-by-list-id", resultJson)
            .then(function (response) {
                $scope.listBienTheGiayLocalStorage = response.data;
                $scope.gioHang.forEach(function (item1) {
                    var correspondingObject = $scope.listBienTheGiayLocalStorage.find(function (item2) {
                        return item2.id === item1.idBienTheGiay;
                    });
                    if (correspondingObject) {
                        correspondingObject.soLuongMua = item1.soLuong;
                    }
                });
                $scope.tongTien = 0;
                $scope.listBienTheGiayLocalStorage.forEach(function (item) {
                    $scope.tongTien += item.soLuongMua * item.giaBan;
                })
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    $scope.loadLocalStorage();


})
app.controller('listProductController', function ($scope, $http, $location) {
    $scope.giays = [];
    $scope.curPage = 1, $scope.itemsPerPage = 12, $scope.maxSize = 5;

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 5;

    let giaySearch = {};

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }
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
                $scope.isLoading = false;
                // window.location.href = feHost + '/trang-chu';
            });
    }

    getData(1);

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });


});

app.controller('detailProductController', function ($scope, $http, $location, $cookies, $routeParams) {
    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

    const id = $routeParams.id;

    $http.get(host + '/admin/rest/giay/' + id)
        .then(function (response) {
            $scope.giaySeletect = response.data;
            detailGiayChiTiet(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        console.log(error);
        $location.path("/home");
    });

    $scope.themVaoGioHang = function () {
        console.log($scope.giayChoosed.id);
        if ($scope.giayChoosed.id === undefined) {
            toastr["warning"]("Vui lòng chọn sản phẩm");
        } else {
            var giaTriCanThem = {idBienTheGiay: $scope.giayChoosed.id, soLuong: 1};

            var tonTai = kiemTraTonTai($scope.gioHang, giaTriCanThem.idBienTheGiay);

            if (!tonTai) {
                $scope.gioHang.push(giaTriCanThem);
                $scope.listBienTheGiayLocalStorage.push($scope.giayChoosed);
                toastr["success"]("Thêm vào giỏ hàng thành công");
            } else {
                var index = timViTri($scope.gioHang, giaTriCanThem.idBienTheGiay);
                $scope.gioHang[index].soLuong++;
            }

            localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
            $scope.loadLocalStorage();
        }
    }


    function kiemTraTonTai(arr, id) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].idBienTheGiay === id) {
                return true; // id đã tồn tại
            }
        }
        return false; // id không tồn tại
    }

    function timViTri(arr, id) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].idBienTheGiay === id) {
                return i;
            }
        }
        return -1;
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


})
