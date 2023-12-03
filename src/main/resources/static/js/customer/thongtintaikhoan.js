var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/thong-tin-tai-khoan", {
            templateUrl: '/pages/user/views/thong-tin-tai-khoan.html',
            controller: 'thongTinTaiKhoanController'
        }).when("/detail/:id", {
        templateUrl: '/pages/user/views/yeu-thich.html',
        controller: 'detailProductController'
    })
        .otherwise({redirectTo: '/thong-tin-tai-khoan'});
});

app.controller("thongTinTaiKhoanController", function ($scope, $http, $window, $location) {
    console.log("aaaaaaa")
    $scope.curPage = 1,
        $scope.itemsPerPage = 4,
        $scope.maxSize = 4;
    let searchText;
    $scope.change = function (input) {
        input.$dirty = true;
    }


    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {
        let apiUrl = host + '/rest/admin/dia-chi-nhan-hang?page=' + currentPage;

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data.content;
                console.log($scope.diaChiNhanHangs);
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.updateTrangThai = function (dieuKien) {
        console.log(dieuKien)
        $scope.trangThai = {
            "id": dieuKien,
            "trangThai": 1
        }

        $http({
            method: 'PUT',
            url: 'http://localhost:8080/rest/admin/dia-chi-nhan-hang/update-trang-thai/' + dieuKien ,
            data:$scope.trangThai
        }).then(function successCallback(response) {
            // Xử lý khi API UPDATE thành công
            console.log('UPDATE điều kiện giảm giá thành công', response);
            getData(1);
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi', response);
        });
        console.log("Điều kiện: ", dieuKien)
        $location.path("/thong-tin-tai-khoan");
    };



    $scope.removeDieuKien = function (dieuKien) {
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/rest/admin/dia-chi-nhan-hang/delete/' + dieuKien
        }).then(function successCallback(response) {
            // Xử lý khi API DELETE thành công
            console.log('Xóa điều kiện giảm giá thành công', response);
            getData(1);
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi xóa điều kiện giảm giá', response);
        });
        console.log("Điều kiện: ", dieuKien)
        $location.path("/thong-tin-tai-khoan");
    };


//    thông tin khách hàng
    const id = 5;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    console.log("thông tin phương đây")
    $http.get(host + '/rest/admin/khach-hang/' + id)
        .then(function (response) {
            $scope.khachHang = response.data;
            var ngaySinh = $scope.khachHang.ngaySinh;
            var object = new Date(ngaySinh);
            $scope.khachHang.ngaySinh = object;
            console.log(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/thong-tin-tai-khoan");
    });




    //select provinces
    $http.get(host + "/rest/provinces/get-all")
        .then(function (response) {
            $scope.provinces = response.data;
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu tỉnh thất bại");
        });


//    lấy dữ liệu huyện theo id tỉnh
    $scope.changeProvince = function () {
        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                // console.log($scope.diaChiNhanHang.provinces)
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ huyện thất bại");
            });
    }
    // lấy dữ liệu theo xã theo huyện
    $scope.changeDistrict = function () {
        if ($scope.diaChiNhanHang.districts.id == 'undefined') {
            alert(" mời bạn chọn tỉnh")
        } else {
            $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ liệu xã thất bại");
                });
        }

    }
    // hiển thị  địa chỉ nhận lên modal
    $scope.updateDiaChiNhan = function (id) {

        console.log("vào đây rùi")
        console.log(id)
        $http.get(host + '/rest/admin/dia-chi-nhan-hang/' + id)
            .then(function (response) {
                $scope.diaChiNhanHang = response.data;
                console.log("show dia chi:", $scope.diaChiNhanHang)
                setData();
                $('#modalDiaChi').modal('show');
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại1");
            });

        // UPDATE địa chỉ nhận
        $scope.updateDiaChiNhanHang = function () {
            console.log("vào update địa chỉ rùi")
            if ($scope.diaChiNhanHangForm.$invalid) {
                return;
            }
            $scope.diaChiNhanHang.districts = $scope.diaChiNhanHang.districts.ten;
            $scope.diaChiNhanHang.provinces = $scope.diaChiNhanHang.provinces.ten;
            $scope.diaChiNhanHang.wards = $scope.diaChiNhanHang.wards.ten;

            $http.put(host + '/rest/admin/dia-chi-nhan-hang/' + id, $scope.diaChiNhanHang)
                .then(function (response) {
                    if (response.status == 200) {
                        toastr["success"]("Cập nhật thành công")
                        $('#modalDiaChi').modal('hide');
                        getData(1)
                    } else {
                        toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                    }
                    $location.path("/danhsach");
                }).catch(function (error) {
                toastr["error"]("Cập nhật thất bại");
                if (error.status === 400) {
                    $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                    $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                    $scope.errors = error.data;
                }
            })
        };

    }

    function setData ()  {
        for(let i = 0; i < $scope.provinces.length; i++) {
            if($scope.provinces[i].ten === $scope.diaChiNhanHang.provinces) {
                $scope.diaChiNhanHang.provinces = $scope.provinces[i];
                break;
            }
        }

        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                for(let j = 0; j < $scope.districts.length; j++) {
                    if($scope.districts[j].ten === $scope.diaChiNhanHang.districts) {
                        $scope.diaChiNhanHang.districts = $scope.districts[j];
                        $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                            .then(function (response) {
                                $scope.wards = response.data;
                                for(let k = 0; k < $scope.wards.length; k++) {
                                    if($scope.wards[k].ten === $scope.diaChiNhanHang.wards) {
                                        $scope.diaChiNhanHang.wards = $scope.wards[k];
                                        break;
                                    }
                                }
                            })
                            .catch(function (error) {
                                toastr["error"]("Lấy dữ liệu xã thất bại");
                            });
                        break;
                    }
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ huyện thất bại");
            });


    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });
});


app.controller('detailProductController', function ($scope, $http, $location, $cookies, $routeParams) {
    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

    const id = $routeParams.id;

    $http.get(host + '/rest/admin/giay/' + id)
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

    // List Recommend

    $scope.curPage = 1,
        $scope.itemsPerPage = 6,
        $scope.maxSize = 6;

    let giaySearch = {};

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }
        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                for (let bienTheGiayObject of $scope.giays) {
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
                console.log($scope.giays);
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
        const infoContainer = document.querySelector('.info');
        const isExpandedMap = {};

        for (const key in productData) {
            if (key !== 'id' && key !== 'ten' && key !== 'namSX' && key !== 'lstAnh' && key !== 'trangThai' && key !== 'lstBienTheGiay' && key !== 'mauSacImages' && key !== 'giaLonNhat' && key !== 'giaThapNhat' && key !== 'lstMauSac' && key !== 'lstHashTagChiTiet') {
                const keyData = productData[key];

                const itemDiv = document.createElement('div');
                itemDiv.classList.add('item');
                const itemChild = document.createElement('div');
                const itemH5 = document.createElement('h5');
                itemChild.appendChild(itemH5);
                const itemP = document.createElement('p');
                const itemSpan = document.createElement('span');
                if (key === 'moTa') {
                    itemDiv.innerHTML = `<p>${keyData}: ${keyData}</p>`;
                } else if (keyData && keyData.id !== null) {
                    itemDiv.appendChild(itemChild);
                    itemH5.textContent = genNameKey(key);
                    itemSpan.textContent = keyData.ten;
                    itemP.textContent =keyData.moTa;
                    // itemDiv.appendChild(itemH5);

                    itemP.classList.add('text-limit');
                    itemDiv.appendChild(itemSpan);
                    itemDiv.appendChild(itemP);

                }

                infoContainer.appendChild(itemDiv);

                if (keyData && keyData.id !== null) {

                    if (itemDiv.scrollHeight > 0) {
                        const iButton = document.createElement('i');
                        const iDropdown = document.createElement('i');

                        iButton.classList.add("fas", "fa-ellipsis-h", "i-button");
                        iDropdown.classList.add("fas", "fa-caret-down");
                        itemChild.classList.add("d-flex");
                        itemChild.appendChild(iButton);
                        itemChild.appendChild(iDropdown);

                        isExpandedMap[key] = false;

                        iButton.addEventListener('click', function () {
                            if (isExpandedMap[key]) {
                                itemDiv.classList.remove("show-text");
                            } else {
                                // Mở nội dung
                                itemDiv.classList.add("show-text");
                            }

                            isExpandedMap[key] = !isExpandedMap[key];
                        });
                    }

                }
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

    function genNameKey(key) {
        if (key === 'chatLieu') {
            return "Chất Liệu";
        } else if (key == 'coGiay') {
            return "Cổ Giày";
        } else if (key == 'dayGiay') {
            return "Dây Giày";
        } else if (key == 'deGiay') {
            return "Đế Giày";
        } else if (key == 'lotGiay') {
            return "Lót Giày"
        } else if (key == 'muiGiay') {
            return "Mũi Giày";
        } else if (key == 'thuongHieu') {
            return "Thương Hiệu";
        } else {
            return "null";
        }
    }


// Duyệt qua các trường trong productData


})