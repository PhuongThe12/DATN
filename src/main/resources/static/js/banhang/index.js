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

    $scope.tongTien = 0; // Tổng tiền

    $scope.selecteHoaDon = function (id) { // chọn hóa đơn
        $scope.isLoading = true;
        if ($scope.selectedHoaDon.id !== id) {
            toastr["success"]("Chuyển hóa đơn thành công");
        }
        const select = $scope.hoaDons.find(hd => hd.id === id);
        if (select) {
            $scope.selectedHoaDon = select;
            $scope.listGiaySelected = [];
            $scope.tongTien = 0;
            select.hoaDonChiTiets.forEach(hdct => {
                $scope.listGiaySelected.push({
                    kichThuoc: hdct.bienTheGiay.kichThuoc,
                    mauSac: hdct.bienTheGiay.mauSac,
                    ten: hdct.bienTheGiay.giayResponse.ten,
                    giaBan: hdct.bienTheGiay.giaBan,
                    soLuongMua: hdct.soLuong,
                    idBienThe: hdct.bienTheGiay.id,
                    id: hdct.id
                });
                $scope.oldValue[hdct.id] = hdct.soLuong;
                $scope.tongTien += (hdct.bienTheGiay.giaBan * hdct.soLuong);
            });

            $scope.listGiaySelected.sort((a, b) => (a.id - b.id));

        }
        $scope.isLoading = false;
    }

    $scope.blurSoLuong = function (giay) {

        let soLuong;

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
                            toastr["success"]("Cập nhật số lượng thành công");
                            let foundIndex = $scope.hoaDons.findIndex(hd => hd.id === $scope.selectedHoaDon.id);
                            if (foundIndex !== -1) {
                                $scope.hoaDons[foundIndex] = response.data;
                            }
                            $scope.selecteHoaDon($scope.selectedHoaDon.id);

                        })
                        .catch(function (error) {
                            toastr["error"](error.data);
                        });
                    $scope.isLoading = false;

                }

            })
            .catch(function (error) {
                toastr["error"](error.data);
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
                        console.log(foundIndex, ": found index");
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
        $http.get(host + "/admin/rest/hoa-don/chua-thanh-toan")
            .then(function (response) {
                $scope.hoaDons = response.data;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
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

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
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
        if(!$scope.searchText) {
            toastr["warning"]("Bạn chưa nhập thông tin tìm kiếm");
            return;
        }

        $scope.searching = true;
        getData(1);
    }

    $scope.reset = function () {
        if($scope.searching) {
            getData(1);
            $scope.searchText = "";
        } else {
            toastr["warning"]("Bạn đang không tìm kiếm");
        }
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

    function detailGiayChiTiet(productData) {


        $scope.giayDetail = productData;
        displayImages(productData.lstAnh);

        const mauSacImages = productData.mauSacImages;
        const lstBienTheGiay = productData.lstBienTheGiay;

        const buttonsContainer = document.getElementById('buttons-container');
        buttonsContainer.innerHTML = '';
        const productInfoContainer = document.getElementById('product-info');
        const sizeButtons = document.getElementById("sizeButtons");
        while (sizeButtons.firstChild) {
            sizeButtons.removeChild(sizeButtons.firstChild);
        }
        $scope.giayChoosed = {};
        const quantityDisplay = document.getElementById('quantity');
        const priceDisplay = document.getElementById('price-product');
        quantityDisplay.textContent = '';
        priceDisplay.textContent = '';

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
                    quantityDisplay.textContent = '';
                    priceDisplay.textContent = '';
                    $scope.giayChoosed = {};
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

                    if (mauSacImages[mauSacId]) {
                        const linkAnh = mauSacImages[mauSacId];
                        const imageList = [linkAnh];
                        displayImages(imageList);
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

        if($scope.giayChoosed.soLuong <= 0) {
            toastr["warning"]("Sản phẩm này đã hết hàng");
            return;
        }

        let requestData;
        if (exists) {
            requestData = {
                idHoaDon: $scope.selectedHoaDon.id,
                idGiay: $scope.giayChoosed.id,
                soLuong: (1 + $scope.oldValue[exists.id])
            }
        } else {
            requestData = {
                idHoaDon: $scope.selectedHoaDon.id, idGiay: $scope.giayChoosed.id, soLuong: 1
            }
        }
        $scope.isLoading = true;
        $http.post(host + '/admin/rest/hoa-don/add-product', requestData)
            .then(function (response) {
                toastr["success"]("Thêm thành công");
                let foundIndex = $scope.hoaDons.findIndex(hd => hd.id === $scope.selectedHoaDon.id);
                if (foundIndex !== -1) {
                    $scope.hoaDons[foundIndex] = response.data;
                }
                $scope.selecteHoaDon($scope.selectedHoaDon.id);

            })
            .catch(function (error) {
                toastr["error"](error.data);
            });
        $scope.isLoading = false;

        document.getElementById('closeModalSanPham').click();
    }

    $scope.taoHoaDon = function () {
        $scope.hoaDon.listBienTheGiay = $scope.listGiaySelected;
        $scope.hoaDon.tongTien = $scope.tongTien;
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

    $scope.startScanning =  function(msIndex, ktIndex) {
        scanning = true;
        if (scanning) {
            navigator.mediaDevices.getUserMedia({ video: true })
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

                                document.getElementById('closeModalCamera').click();
                                clearInterval(interval);
                            } else {
                                Quagga.decodeSingle({
                                    src: convertImageDataToBase64(imageData),
                                    numOfWorkers: 0,
                                    decoder: {
                                        readers: ['ean_reader', 'code_128_reader', 'code_39_reader']
                                    },
                                }, function (result) {
                                    if (result && result.codeResult) {

                                        document.getElementById('closeModalCamera').click();
                                        clearInterval(interval);
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
            video: { deviceId: selectedCamera, width: 1920, height: 1080 }
        })
            .then((stream) => {
                video.srcObject = stream;
                video.play();
            })
            .catch((error) => {
                console.error('Không thể cập nhật stream camera:', error);
            });
    }


    $scope.stopScanning = function() {
        video.pause();
        video.srcObject.getTracks().forEach(track => track.stop());
    }

    $scope.closeModalCamera = function () {
        scanning = false;
        $scope.stopScanning();
    }

});

function nextInput(e) {
    if (e.which === 13) {
        e.preventDefault();
        e.target.blur();
    }
}