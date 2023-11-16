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

    $scope.curPage = 1, $scope.itemsPerPage = 5, $scope.maxSize = 5;
    let searchText, startDate, endDate;


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

    $scope.changeRadio = function (loaiYeuCau) {
        $scope.loaiYeuCau = loaiYeuCau;
        getData(1);
    }

    $scope.search = function () {
        if (!$scope.searchText) {
            toastr["error"]("Vui lòng nhập tên muốn tìm kiếm");
            return;
        }
        searchText = $scope.searchText;
        getData(1, searchText);
    };

    function getData(currentPage, startDate, endDate, searchText) {
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
        if ($scope.loaiYeuCau == 1) {
            apiUrl += '&loaiYeuCau=' + 1;
        } else if ($scope.loaiYeuCau == 2) {
            apiUrl += '&loaiYeuCau=' + 2;
        }


        $http.get(apiUrl)
            .then(function (response) {
                $scope.listYeuCau = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});


app.controller("detailYeuCauController", function ($scope, $http, $location, $routeParams) {

    $scope.formatDateView = function (isoDateString) {
        const inputDate = new Date(isoDateString);

        const options = {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit',
        };

        const formattedDate = inputDate.toLocaleDateString('en-GB', options);

        // Loại bỏ dấu phẩy
        return formattedDate.replace(',', '');
    };

    const id = $routeParams.id;
    $http.get(host + '/admin/rest/yeu-cau/' + id)
        .then(function (response) {
            $scope.yeuCau = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("updateYeuCauController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.danhSachNhanVien = [{id: 1, ten: "Quân"}, {id: 2, ten: "Tuấn"}, {id: 3, ten: "Chiến"}, {
        id: 4, ten: "Phương"
    }, {id: 5, ten: "Hoàng"}, {id: 6, ten: "Cường"}];

    $scope.yeuCau = {}; // Khởi tạo đối tượng yeuCau trước

    $http.get(host + '/admin/rest/yeu-cau/' + id)
        .then(function (response) {
            $scope.yeuCau = response.data;
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });

    $scope.updateYeuCau = function () {
        if ($scope.updateYeuCauForm.$invalid) {
            return;
        }

        $http.put(host + '/admin/rest/yeu-cau/' + id, $scope.yeuCau)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công");
                } else {
                    toastr["error"]("Cập nhật thất bại. Lỗi bất định");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Cập nhật thất bại");
                if (error.status === 400) {
                    $scope.updateYeuCauForm.hoaDon.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    };
});


app.controller("addYeuCauController", function ($scope, $http, $location, $routeParams) {

    const idHoaDon = $routeParams.id;

//checkForm
    $scope.focusLyDo = false, $scope.focusGhiChu = false;
//các list
    $scope.listHoaDonChiTiet = [], $scope.listGiay = [], $scope.arrayForRepeat = [], $scope.arrayForRepeat1 = [];
//các map
    $scope.mapSanPhamTra = new Map(), $scope.mapSanPhamThayThe = new Map(), $scope.mapYeuCauChiTiet = new Map();
//tạo search
    $scope.giaySearch = {}, $scope.giaySearch.curPage = 1, $scope.giaySearch.itemsPerPage = 5, $scope.giaySearch.maxSize = 5, $scope.giaySearch.pageSize = 6;

    $scope.yeuCau = {
        nguoiThucHien: "1", loaiYeuCau: "1", trangThai: "1",
    };

    $scope.change = function (input) {
        input.$dirty = true;
    }

    getHoaDon(idHoaDon);


    $scope.formatToVND = function (number) {
        return number.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
    }


    $scope.doiSanPham = function (hoaDonChiTiet, index) {
        if (hoaDonChiTiet.soLuongTra > 0) {
            // Lấy ID duy nhất của hoá đơn chi tiết
            var baseId = index;

            // Thêm hoặc cập nhật hoá đơn chi tiết vào Map
            $scope.addToMapForMultipleItems(baseId, hoaDonChiTiet, hoaDonChiTiet.soLuongTra);

        } else {
            toastr["error"]("Bạn phải chọn số lượng trả lớn hơn 0.");
        }
    };


    $scope.addToMapForMultipleItems = function (baseId, hoaDonChiTiet, soLuongTra) {
        for (var i = 1; i <= soLuongTra; i++) {
            var key = baseId + "." + i;
            var valueCopy = angular.copy(hoaDonChiTiet);
            valueCopy.soLuongTra = 1; // Set số lượng trả mỗi bản ghi là 1

            // Cập nhật cấu trúc dữ liệu khi thêm vào map
            $scope.mapSanPhamTra.set(key, {
                hoaDonChiTiet: valueCopy, // value1
                lyDo: null,              // value2
                ghiChu: ''               // value3
            });
        }
        $scope.arrayForRepeat = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
        console.log("List Giày Trả:")
        console.log($scope.arrayForRepeat)
    };


    $scope.ghiChu_LyDo = function (item) {
        getAllLyDo();
        $scope.giayTra = item;
    }


    $scope.updateMapSanPhamTra = function (key, lyDo, ghiChu) {
        console.log(key);
        console.log(ghiChu);
        console.log(lyDo);
        if ($scope.mapSanPhamTra.has(key)) {
            var item = $scope.mapSanPhamTra.get(key);
            item.lyDo = lyDo;
            item.ghiChu = ghiChu;
            $scope.mapSanPhamTra.set(key, item);

            // Cập nhật arrayForRepeat nếu cần
            $scope.arrayForRepeat = Array.from($scope.mapSanPhamTra, ([key, value]) => ({key, value}));
            console.log("Cập nhật lý do + ghichu:")
            console.log($scope.arrayForRepeat)
        } else {
            toastr["error"]("Lỗi không lấy được giữ liệu");
        }
    }

    $scope.isFormValid = function () {
        return !$scope.lyDo && !$scope.ghiChu;
    };


    $scope.kiemTraVaMoModal = function (lyDo, ghiChu) {
        if ($scope.isFormValid()) {
            $scope.chonSanPham(lyDo, ghiChu);
            $('#exampleModal').modal('show');
        } else {
            // Hiển thị thông báo lỗi hoặc thông báo nhắc nhở
            toastr["error"]("Bạn chưa điền chọn và mô tả lý do!");
            return;
        }
    };


    $scope.chonSanPham = function (lyDo, ghiChu) {
        $scope.updateMapSanPhamTra($scope.giayTra.key, lyDo, ghiChu)
        $http.post(host + '/admin/rest/giay/find-all-by-search', $scope.giaySearch)
            .then(function (response) {
                $scope.listGiay = response.data;
                $scope.numOfPages = response.data.totalPages;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            // $location.path("/list");
        });

    };

    $scope.chonGiayDoi = function (giay) {
        $http.get(host + '/admin/rest/giay/' + giay.id)
            .then(function (response) {
                console.log(response.data);
                detailGiay(response.data);
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    };


    $scope.chonBienTheGiayDoi = function (bienTheGiayDoi) {

        // Cấu trúc dữ liệu mới cho map
        $scope.mapSanPhamThayThe.set($scope.keyGiayTra, {
            bienTheGiay: bienTheGiayDoi, // Sản phẩm thay thế
            soLuongDoi: 0              // Giá trị sẽ cập nhật sau
        });

        // Cập nhật array cho ng-repeat
        $scope.arrayForRepeat1 = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
    };

    $scope.capNhatThongTinThem = function (key, soLuongDoi) {
        if ($scope.mapSanPhamThayThe.has(key)) {
            var item = $scope.mapSanPhamThayThe.get(key);
            item.soLuongDoi = soLuongDoi;
            $scope.mapSanPhamThayThe.set(key, item);

            // Cập nhật lại arrayForRepeat1 để phản ánh thay đổi
            $scope.arrayForRepeat1 = Array.from($scope.mapSanPhamThayThe, ([key, value]) => ({key, value}));
        } else {
            toastr.error("Không thấy dữ liệu");
        }
    };


    $scope.combineMaps = function () {
        // Duyệt qua tất cả các entries trong mapSanPhamTra
        $scope.mapSanPhamTra.forEach((hoaDonValue, key) => {
            // Kiểm tra xem có sản phẩm thay thế nào có key tương ứng không
            if ($scope.mapSanPhamThayThe.has(key)) {
                var thayTheValue = $scope.mapSanPhamThayThe.get(key);

                // Cập nhật mapYeuCauChiTiet với dữ liệu từ cả hai map
                $scope.mapYeuCauChiTiet.set(key, {
                    hoaDonChiTiet: hoaDonValue.hoaDonChiTiet.id,
                    soLuongTra: hoaDonValue.hoaDonChiTiet.soLuongTra,
                    bienTheGiay: thayTheValue.bienTheGiay.id,
                    lyDo: hoaDonValue.lyDo,
                    soLuong: thayTheValue.soLuongDoi,
                    ghiChu: hoaDonValue.ghiChu,
                });
            }
        });
        var yeuCauChiTietList = Array.from($scope.mapYeuCauChiTiet.values());
        $scope.yeuCau.listYeuCauChiTiet = yeuCauChiTietList;


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
                    console.log(error)
                }
            });

    };


    function detailGiay(productData) {
        console.log(productData);
        displayImages(productData.lstAnh);

        const mauSacImages = productData.mauSacImages;
        const lstBienTheGiay = productData.lstBienTheGiay;

        const buttonsContainer = document.getElementById('buttons-container');
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

    function getHoaDon(id) {
        $http.get(host + '/admin/rest/hoa-don/yeu-cau/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            // $location.path("/list");
            $scope.isLoading = false;
        });
    }

    function getAllListGiay(giaySearch) {
        $http.post(host + '/admin/rest/giay/find-all-by-search', giaySearch)
            .then(function (response) {
                $scope.listGiay = response.data;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            // $location.path("/list");
            $scope.isLoading = false;
        });
    }

    function getOneGiayById(id) {
        $http.get(host + '/admin/rest/giay/' + id)
            .then(function (response) {
                console.log(response.data);
                return response.data; // Trả về dữ liệu khi sẵn sàng
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            throw error; // Đẩy lỗi để xử lý ở nơi gọi hàm
        });
    }

    function getAllLyDo() {
        $http.get(host + '/admin/rest/ly-do/list')
            .then(function (response) {
                $scope.listLyDo = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
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
        console.log($scope.hoaDonSearch)
        $scope.isLoading = true;
        $http.post(host + '/admin/rest/hoa-don/yeu-cau', hoaDonSearch)
            .then(function (response) {
                $scope.listHoaDon = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                console.log($scope.listHoaDon)
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
                $scope.isLoading = false;
            });
    }


    $scope.formatToVND = function (number) {
        return number.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
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