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
    }).when("/cart", {
        templateUrl: '/pages/user/views/cart.html',
        controller: 'cartProductController'
    }).when("/don-hang", {
        templateUrl: '/pages/user/views/donhang.html',
        controller: 'donHangListController'
    }).when("/don-hang/detail/:id", {
        templateUrl: '/pages/user/views/don-hang-detail.html',
        controller: 'detailDonHangController'
    }).when("/thanh-toan", {
        templateUrl: '/pages/user/views/thanh-toan.html',
        controller: 'thanhToanController'
    }).when("/thong-tin-tai-khoan", {
        templateUrl: '/pages/user/views/thong-tin-tai-khoan.html',
        controller: 'thongTinTaiKhoanController'
    }).when("/thanh-toan-status", {
        template: "<h1>Đang tiến hành thanh toán</h1>",
        controller: 'thanhToanStatusController'
    })
        .when("/tra-cuu-don-hang", {
            templateUrl: '/pages/user/views/tra-cuu-don-hang.html',
            controller: 'traCuuDonHangController'
        })
        .otherwise({redirectTo: '/list'});
});

app.controller('thanhToanStatusController', function ($scope, $http, $location) {
    let container = {};
    let currentUser;
    $http.get(host + "/session/get-customer")
        .then(response => {
            currentUser = response.data;
        })
    window.location.hash.split('&').toString().substr(1).split(",").forEach(item => {
        container[item.split("=")[0]] = decodeURIComponent(item.split("=")[1]) ? item.split("=")[1] : "No query strings available";
    });

    if (Object.keys(container).length > 3) {
        if (container["vnp_TransactionStatus"] === "00") {
            let request = {
                idHoaDon: container["vnp_OrderInfo"]
            }
            $http.post(host + "/rest/user/hoa-don/hoan-tat-banking", request)
                .then(response => {
                    toastr["success"]("Thanh toán thành công");
                    if (currentUser) {
                        $location.path("/don-hang");
                    } else {
                        $location.path("/list");
                    }
                })
                .catch(err => {
                    if (err.status === 409) {
                        toastr["error"](err.data.data);
                    } else {
                        toastr["error"]("Thanh toán thất bại");
                    }
                    $location.path("/list");
                })

        } else {
            $http.get(host + "/vnpay/cancel-banking-order/" + info[0])
                .then(response => {
                    toastr["warning"]("Bạn chưa hoàn tất thanh toán");
                    if (!UserDetailService.getCurrentUser()) {
                        $location.path("/don-hang");
                    } else {
                        $location.path("/cart");
                    }
                })
                .catch(err => {
                    $location.path("/list");
                })
        }
    }
});

app.controller('navbarController', function ($rootScope, $scope, $http, $location, $cookies, $window) {

    $scope.khachHang = {};
    var token = $cookies.get('token');
    if (token) {
        $scope.userLogged = true;
        $http.get(host + "/session/get-customer")
            .then(response => {
                if (response.data !== null) {
                    $scope.khachHang = response.data;
                    $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id)
                        .then(function (response) {
                            $scope.khachHang.idGioHang = response.data.id;
                        }).catch(function (error) {

                    })
                }
            })
    }else{
        $scope.userLogged = false;
    }


    if (angular.equals({}, $scope.khachHang)) {

        $scope.currentUser = {
            idKhachHang: "",
            username: "",
            role: "",
            token: ""
        };
        $scope.userLogin = {};

        $scope.loginUser = function () {
            $http.post(host + '/api/authentication/singin', $scope.userLogin)
                .then(function (response) {
                    if (response.status == 200) {
                        setTokenCookie(response.data.token, 1)
                        $scope.currentUser.username = response.data.userName
                        $scope.currentUser.token = response.data.token
                        $scope.currentUser.idKhachHang = response.data.id
                        $scope.currentUser.role = response.data.role
                        $rootScope.currentUser = $scope.currentUser;
                        if ($scope.currentUser.role === 'ROLE_USER') {
                            $window.location.href = '/home';
                        } else if ($scope.currentUser.role === 'ROLE_STAFF') {
                            $window.location.href = '/admin/ban-hang';
                        } else {
                            $window.location.href = '/admin/tong-quan';
                        }
                    }
                })
                .catch(function (error) {
                    toastr["error"]("Đăng nhập thất bại sai thông tin tài khoản mật khẩu");
                });
        }

        function setTokenCookie(token, expiryDays) {
            const d = new Date();
            d.setTime(d.getTime() + (expiryDays * 24 * 60 * 60 * 1000));
            const expires = "expires=" + d.toUTCString();
            document.cookie = `token=${token}; ${expires}; path=/`;
        }
    }

    $scope.logoutUser = function () {
        $cookies.put('token', null, {expires: 'Thu, 01 Jan 1970 00:00:00 GMT'});
        $window.localStorage.clear();
        $window.location.href = '/home';
    }


    $scope.listBienTheGiayLocalStorage = [];
    $scope.gioHang = [];
    $scope.currentUser = {};
    $scope.gioHangChiTietList = [];
    // var storedUserData = $window.localStorage.getItem('currentUser');
    if (!angular.equals({}, $scope.khachHang)) {

        $scope.loadCartByIdKhachHang = function () {
            $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id).then(function (response) {
                console.log(response.data);
                var bienTheGiayList = [];
                angular.forEach(response.data.gioHangChiTietResponses, function (gioHangChiTiet) {
                    var bienTheGiay = gioHangChiTiet.bienTheGiay;
                    bienTheGiay.soLuongMua = gioHangChiTiet.soLuong;
                    bienTheGiay.idGioHang = gioHangChiTiet.id;
                    bienTheGiayList.push(bienTheGiay);
                });
                $scope.listBienTheGiayLocalStorage = bienTheGiayList;
                $scope.tongTien = 0;
                $scope.tongKhuyenMaiSanPham = 0;
                $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                    var giaSauKhuyenMai = item.giaBan;
                    if (item.khuyenMai) {
                        giaSauKhuyenMai = (giaSauKhuyenMai * item.khuyenMai) / 100;
                        $scope.tongKhuyenMaiSanPham += giaSauKhuyenMai;
                    }
                    $scope.tongTien += item.soLuongMua * item.giaBan;
                });

                $scope.tongThanhToan = $scope.tongTien - $scope.tongKhuyenMaiSanPham;

            }).catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });

        }

        $scope.loadCartByIdKhachHang();
        // $scope.loadLocalStorage();
    } else {

        $scope.loadLocalStorage = function () {
            var gioHangFromCookies = localStorage.getItem('gioHang') || '[]';
            $scope.gioHang = JSON.parse(gioHangFromCookies);
            $scope.gioHang.sort(function (a, b) {
                return a.idBienTheGiay - b.idBienTheGiay;
            });

            var idList = $scope.gioHang.map(function (item) {
                return item.idBienTheGiay || item.bienTheGiay;
            });
            var resultJson = {"ids": idList};
            $http.post("http://localhost:8080/rest/admin/giay/bien-the/get-all-by-list-id", resultJson)
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

                    $scope.gioHangChiTietList = $scope.listBienTheGiayLocalStorage.map(item => ({
                        bienTheGiay: item.id,
                        soLuong: item.soLuongMua,
                        giaBan: item.giaBan
                    }));

                })
                .catch(function (error) {
                    console.log(error);
                    toastr["error"]("Lấy dữ liệu thất bại");
                    $scope.isLoading = false;
                });
        }

        $scope.loadLocalStorage();
    }


    $scope.isCartVisible = false;

    $scope.hideCart = function () {
        $scope.isCartVisible = false;
    }

    $scope.khachHangR = {};

    $scope.khachHangR.showPassR = false;
    $scope.khachHangR.gioiTinh = true;
    $scope.userLogin.showPass = false;
    $scope.khachHangRegister = {};

    $scope.toggleShowPassR = function () {
        if ($scope.khachHangR.showPassR) {
            document.getElementById('khachHangRPassword').type = 'text';
        } else {
            document.getElementById('khachHangRPassword').type = 'password';
        }
    }

    $scope.toggleShowPass = function () {
        if ($scope.userLogin.showPass) {
            document.getElementById('password').type = 'text';
        } else {
            document.getElementById('password').type = 'password';
        }
    }

    $scope.errors = {};

    $scope.signUp = function () {

        if (!$scope.khachHangR.email || !$scope.khachHangR.password || !$scope.khachHangR.ngaySinh || !$scope.khachHangR.hoTen || !$scope.khachHangR.soDienThoai) {
            return;
        }
        if ($scope.khachHangR.ngaySinh > new Date()) {
            toastr["warning"]('Ngày sinh phải là ngày trong quá khứ');
            return;
        }

        $http.post(host + '/api/authentication/signup', $scope.khachHangR)
            .then(function (response) {
                toastr["success"]("Đăng ký thành công vui lòng kiểm tra email để kích hoạt tài khoản");
            })
            .catch(function (error) {
                toastr["error"](error.data.email);
            });


        $http.get(host + "/rest/account/confirm?email=" + $scope.khachHangR.email)
        document.getElementById('closeModalR').click();
    }

    $scope.resetTaiKhoan = {};
    $scope.resetPassword = function () {
        if (!$scope.resetTaiKhoan.email || !$scope.resetTaiKhoan.soDienThoai) {
            toastr["error"]("Vui lòng điển đầy đủ thông tin");
            return;
        }

        $http.get(host + '/rest/account/khach-hang?email=' + $scope.resetTaiKhoan.email + '&sdt=' + $scope.resetTaiKhoan.soDienThoai)
            .then(res => {
                $http.get(host + "/rest/account/forgot-password?email=" + res.data)
                document.getElementById('closeModalForget').click();
                toastr["success"]("Đặt lại mật khẩu thành công vui lòng kiểm tra email để hoàn thành");
                $scope.resetTaiKhoan = {};
            })
            .catch(err => {
                toastr["error"](err.data.data);
            })
    }


})


app.controller('detailProductController', function ($scope, $http, $location, $cookies, $routeParams, $window) {
    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size
    $scope.khachHang = {};
    var token = $cookies.get('token');
    if (token) {
        $http.get(host + "/session/get-customer")
            .then(response => {
                if (response.data !== null) {
                    $scope.khachHang = response.data;
                    $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id)
                        .then(function (response) {
                            $scope.khachHang.idGioHang = response.data.id;
                        }).catch(function (error) {

                    })
                }
            })
    }


    const id = $routeParams.id;

    $scope.soLuongMua = 1;
    $scope.soLuongGioHangChiTiet;

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

        if (angular.equals({}, $scope.giayChoosed)) {
            toastr["warning"]("Vui lòng chọn sản phẩm");
        } else if ($scope.soLuongMua < 1) {
            toastr["warning"]("Hãy nhập số lượng lớn hơn 0");
        } else {
            if (token) {
                $http.get("http://localhost:8080/rest/admin/giay/" + $scope.giayDetail.id + "/so-luong")
                    .then(function (response) {
                        var soLuongTrongKho = response.data;
                        // $http.get("http://localhost:8080/admin/rest/giay/" + $scope.giayDetail.id + "/so-luong")
                        $http.get("http://localhost:8080/rest/user/gio-hang/" + $scope.khachHang.idGioHang + "/so-luong/" + $scope.giayDetail.id)
                            .then(function (response) {
                                $scope.soLuongGioHangChiTiet = response.data;
                                console.log(response.data);

                                if ($scope.soLuongMua + response.data > soLuongTrongKho) {
                                    toastr["warning"]("Số lượng vượt quá trong kho");
                                } else {
                                    $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id).then(function (response) {
                                        $scope.gioHangChiTiet = {};
                                        $scope.gioHangChiTiet.gioHang = response.data.id;
                                        $scope.gioHangChiTiet.bienTheGiay = $scope.giayChoosed.id;
                                        $scope.gioHangChiTiet.soLuong = $scope.soLuongMua;
                                        console.log($scope.gioHangChiTiet);
                                        $http.post("http://localhost:8080/rest/user/gio-hang", $scope.gioHangChiTiet)
                                            .then(function (response) {
                                                $scope.$parent.isCartVisible = true;
                                                $scope.loadCartByIdKhachHang();

                                            }).catch(function (error) {

                                        })
                                    }).catch(function (error) {
                                        console.log(error);
                                        toastr["error"]("Lấy dữ liệu thất bại");
                                        $scope.isLoading = false;
                                    });
                                }
                            }).catch(function (error) {

                        })
                    }).catch(function (error) {
                    console.log(error);
                });
            } else {
                var gioHangFromCookies = localStorage.getItem('gioHang') || '[]';
                $scope.gioHang = JSON.parse(gioHangFromCookies);

                var giaTriCanThem = {idBienTheGiay: $scope.giayChoosed.id, soLuong: $scope.soLuongMua};

                var tonTai = kiemTraTonTai($scope.gioHang, giaTriCanThem.idBienTheGiay);
                if (!tonTai) {
                    $scope.gioHang.push(giaTriCanThem);
                    $scope.listBienTheGiayLocalStorage.push($scope.giayChoosed);
                    localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
                    toastr["success"]("Thêm vào giỏ hàng thành công");
                    $scope.$parent.loadLocalStorage();
                } else {
                    var index = timViTri($scope.gioHang, giaTriCanThem.idBienTheGiay);
                    $http.get("http://localhost:8080/rest/admin/giay/" + $scope.giayDetail.id + "/so-luong")
                        .then(function (response) {
                            if (($scope.gioHang[index].soLuong + $scope.soLuongMua) <= response.data) {
                                $scope.gioHang[index].soLuong += $scope.soLuongMua;
                                localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
                                $scope.$parent.loadLocalStorage();
                            } else {
                                toastr["error"]("Số lượng trong kho không đủ");
                            }
                        }).catch(function (error) {
                    })
                }
            }
        }
    }

    $scope.getSoLuongInGhct = function (idBienTheGiay) {
        $http.get("http://localhost:8080/rest/user/gio-hang/16/so-luong/" + idBienTheGiay)
            .then(function (response) {
                $scope.soLuongGioHangChiTiet = response.data;
                console.log(response.data);
            }).catch(function (error) {

        })
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
                            if (variant.soLuong == 0) {
                                sizeButton.classList.add("soldout", "disable");
                            }


                            $scope.$watch('giayDetail', function (newGiayDetail, oldGiayDetail) {
                                $scope.giayDetail = newGiayDetail;
                            });
                            if (variant.soLuong != 0) {
                                sizeButton.addEventListener("click", () => {
                                    const listButton = document.querySelectorAll(".btn-size");
                                    listButton.forEach((button) => {
                                        button.classList.remove("click-button");
                                    });

                                    sizeButton.classList.add("click-button");
                                    $scope.giayDetail = variant;
                                    $scope.giayChoosed = variant;
                                    $scope.giayChoosed.ten = productData.ten;
                                    $scope.soLuongMua = 1;
                                    $scope.$apply();
                                });
                            }
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
                    itemP.textContent = keyData.moTa;
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


    $scope.themSoLuong = function (bienTheGiay) {
        $http.get("http://localhost:8080/rest/admin/giay/" + bienTheGiay.id + "/so-luong")
            .then(function (response) {
                if ($scope.soLuongMua > response.data - 1) {
                    toastr["warning"]("Số lượng vượt quá trong kho");
                } else {
                    $scope.soLuongMua += 1;
                }

            }).catch(function (error) {
            console.log(error);
        })
    }

    $scope.giamSoLuong = function () {
        if ($scope.soLuongMua > 1) {
            $scope.soLuongMua -= 1;
        } else {
            toastr["warning"]("Số lượng phải lớn hơn 0");
        }

    }
// cường làm thêm yêu thích vào đây
    $scope.sanPhamYeuThich = function () {
        console.log("vào trong rùi")
        // Dữ liệu bạn muốn gửi đến API
        var requestData = {
            "khachHangId": 5,
            "giayId": $scope.giayDetail.id
        };
        // Địa chỉ URL của API
        var apiUrl = 'http://localhost:8080/rest/khach-hang/san-pham-yeu-thich';

        // Thiết lập các tùy chọn cho cuộc gọi Fetch
        var requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // Kiểu dữ liệu bạn đang gửi đi
                // Các header khác nếu cần
            },
            body: JSON.stringify(requestData), // Chuyển đổi đối tượng thành chuỗi JSON
        };
        // Thực hiện cuộc gọi Fetch
        fetch(apiUrl, requestOptions)
            .then(function (response) {
                // Kiểm tra trạng thái của phản hồi từ API
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json(); // Chuyển đổi dữ liệu JSON từ phản hồi
            })
            .then(function successCallback(response) {
                // Xử lý dữ liệu từ phản hồi thành công
                console.log('Đã thêm thành công danh sách yêu thích', response);
                console.log(response);
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                console.error('Error:', error);
            });

    }

//

})


app.controller('cartProductController', function ($scope, $http, $location, $cookies, $timeout, $window) {
    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;

    $scope.gioHang = [];

    $scope.loadLocalStorage = function () {
        var gioHangFromCookies = localStorage.getItem('gioHang') || '[]';
        $scope.gioHang = JSON.parse(gioHangFromCookies);
        $scope.gioHang.sort(function (a, b) {
            return a.idBienTheGiay - b.idBienTheGiay;
        });

        var idList = $scope.gioHang.map(function (item) {
            return item.idBienTheGiay || item.bienTheGiay;
        });
        var resultJson = {"ids": idList};
        $http.post("http://localhost:8080/rest/admin/giay/bien-the/get-all-by-list-id", resultJson)
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
                $scope.tongKhuyenMaiSanPham = 0;
                $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                    var giaSauKhuyenMai = item.giaBan;
                    if (item.khuyenMai) {
                        giaSauKhuyenMai = (giaSauKhuyenMai * item.khuyenMai) / 100;
                        $scope.tongKhuyenMaiSanPham += giaSauKhuyenMai;
                    }
                    $scope.tongTien += item.soLuongMua * item.giaBan;
                });

                $scope.tongThanhToan = $scope.tongTien - $scope.tongKhuyenMaiSanPham;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    $scope.khachHang = {};
    var token = $cookies.get('token');
    if (token) {
        $http.get(host + "/session/get-customer")
            .then(response => {
                if (response.data && response.data !== null && response.data !== undefined) {
                    $scope.khachHang = response.data;
                    $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id)
                        .then(function (response) {
                            $scope.khachHang.idGioHang = response.data.id;
                            $scope.loadCartByIdKhachHang = function () {
                                console.log($scope.khachHang.id);
                                $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id).then(function (response) {
                                    console.log(response.data);
                                    var bienTheGiayList = [];
                                    angular.forEach(response.data.gioHangChiTietResponses, function (gioHangChiTiet) {
                                        var bienTheGiay = gioHangChiTiet.bienTheGiay;
                                        bienTheGiay.soLuongMua = gioHangChiTiet.soLuong;
                                        bienTheGiay.idGioHang = gioHangChiTiet.id;
                                        bienTheGiayList.push(bienTheGiay);
                                    });
                                    $scope.listBienTheGiayLocalStorage = bienTheGiayList;
                                    $scope.tongTien = 0;
                                    $scope.tongKhuyenMaiSanPham = 0;
                                    $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                                        var giaSauKhuyenMai = item.giaBan;
                                        if (item.khuyenMai) {
                                            giaSauKhuyenMai = (giaSauKhuyenMai * item.khuyenMai) / 100;
                                            $scope.tongKhuyenMaiSanPham += giaSauKhuyenMai;
                                        }
                                        $scope.tongTien += item.soLuongMua * item.giaBan;
                                    });

                                    $scope.tongThanhToan = $scope.tongTien - $scope.tongKhuyenMaiSanPham;

                                }).catch(function (error) {
                                    console.log(error);
                                    toastr["error"]("Lấy dữ liệu thất bại");
                                    $scope.isLoading = false;
                                });

                            }

                            $scope.loadCartByIdKhachHang();
                        }).catch(function (error) {
                        console.log(error)
                    })
                }
            })
    } else {
        $scope.loadLocalStorage();
    }
    $scope.subtraction = function (bienTheGiay) {
        if (bienTheGiay.soLuongMua < 2) {
            toastr["warning"]("Số lượng phải lớn hơn 0");
        } else {
            if (!angular.equals({}, $scope.khachHang)) {
                var gioHangChiTietUpdate = {};
                gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
                gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua - 1;
                $http.put("http://localhost:8080/rest/user/gio-hang/update/so-luong", gioHangChiTietUpdate)
                    .then(function (response) {
                        $scope.loadCartByIdKhachHang();
                        $scope.$parent.loadCartByIdKhachHang();
                    }).catch(function (error) {

                })
            } else {
                var index = timViTri($scope.gioHang, bienTheGiay.id);
                if ($scope.gioHang[index].soLuong >= 2) {
                    $scope.gioHang[index].soLuong--;
                }
                localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
                $scope.loadLocalStorage();
            }


        }
    }

    $scope.summation = function (bienTheGiay) {
        if (!angular.equals({}, $scope.khachHang)) {
            $http.get("http://localhost:8080/rest/admin/giay/" + bienTheGiay.id + "/so-luong")
                .then(function (response) {
                    if (bienTheGiay.soLuongMua > response.data - 1) {
                        toastr["warning"]("Số lượng vượt quá trong kho");
                    } else {
                        var gioHangChiTietUpdate = {};
                        gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
                        gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua + 1;
                        $http.put("http://localhost:8080/rest/user/gio-hang/update/so-luong", gioHangChiTietUpdate)
                            .then(function (response) {
                                $scope.loadCartByIdKhachHang();
                                $scope.$parent.loadCartByIdKhachHang();
                            }).catch(function (error) {
                        })
                    }
                }).catch(function (error) {
                console.log(error);
            })
        } else {
            var index = timViTri($scope.gioHang, bienTheGiay.id);
            $http.get("http://localhost:8080/rest/admin/giay/" + bienTheGiay.id + "/so-luong")
                .then(function (response) {
                    if ($scope.gioHang[index].soLuong + 1 > response.data) {
                        toastr["warning"]("Số lượng vượt quá trong kho");
                    } else {
                        $scope.gioHang[index].soLuong++;
                        localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
                        $scope.loadLocalStorage();
                    }
                }).catch(function (error) {
            })
        }
    }

    $scope.deleteBienTheGiay = function (bienTheGiay) {
        if (!angular.equals({}, $scope.khachHang)) {
            var gioHangChiTietUpdate = {};
            gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
            gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua
            console.log(gioHangChiTietUpdate);
            $http.delete("http://localhost:8080/rest/user/gio-hang/delete", {
                data: JSON.stringify(gioHangChiTietUpdate),
                headers: {'Content-Type': 'application/json;charset=utf-8'}
            })
                .then(function (response) {
                    $scope.loadCartByIdKhachHang();
                    $scope.$parent.loadCartByIdKhachHang();
                })
                .catch(function (error) {
                    console.error('Error:', error);
                });
        } else {
            var index = timViTri($scope.gioHang, bienTheGiay.id);
            $scope.gioHang.splice(index, 1);
            localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
            $scope.$parent.loadLocalStorage();

            $scope.loadLocalStorage();
        }

    }

    function timViTri(arr, id) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].idBienTheGiay === id) {
                return i;
            }
        }
        return -1;
    }

    $scope.productsLessQuantity = [];
    $scope.productsSoldOutQuantity = [];

    $scope.thanhToanLocation = function () {
        $scope.productsLessQuantity = [];
        $scope.productsSoldOutQuantity = [];
        if ($scope.listBienTheGiayLocalStorage.length != 0) {
            $http.post("/rest/user/gio-hang/check-so-luong", $scope.listBienTheGiayLocalStorage)
                .then(function (response) {
                    $location.path("/thanh-toan");
                }).catch(function (error) {
                if (error.status == 409) {
                    $scope.showErrorMessages(error.data);

                }
            });
            $scope.showErrorMessages = function (errorData) {


                for (let i = 0; i < errorData.length; i++) {
                    let errorParts = errorData[i].split(':');
                    let errorId = errorParts[0].trim();
                    let status = errorParts[1].trim();

                    // Tìm sản phẩm có ID trùng với errorId trong danh sách và hiển thị message
                    let productWithError = $scope.listBienTheGiayLocalStorage.find(function (product) {
                        return product.id === parseInt(errorId);
                    });

                    if (productWithError && parseInt(status) === 2) {
                        $scope.productsLessQuantity.push(productWithError);
                    } else {
                        $scope.productsSoldOutQuantity.push(productWithError);
                    }
                }
                if ($scope.productsLessQuantity.length > 0 || $scope.productsSoldOutQuantity.length > 0) {
                    var soldoutElement = document.getElementById('soldout');
                    if (soldoutElement) {
                        $timeout(function () {
                            soldoutElement.click();
                        });
                    }
                }
            };
        } else {
            toastr["warning"]("Vui lòng thêm sản phẩm để tiếp tục !");
        }

    }

    $scope.deleteSoldOut = function () {
        console.log($scope.productsSoldOutQuantity);
    }

    $scope.giamSoLuong = function () {
        if ($scope.soLuongMua > 1) {
            $scope.soLuongMua -= 1;
        } else {
            toastr["warning"]("Số lượng phải lớn hơn 0");
        }

    }

});


app.controller("donHangListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 9999,
        $scope.maxSize = 9999;
    $scope.statusCurrently = null;

    let searchText;

    $scope.search = function () {
        if (!$scope.searchText) {
            toastr["error"]("Vui lòng nhập tên muốn tìm kiếm");
            return;
        }
        searchText = $scope.searchText;
        getData(1, searchText);
    };

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {
        $scope.hoaDon = {
            hoaDonChiTiets: []
        }
        let apiUrl = host + "/rest/admin/hoa-don/khach-hang/" + $scope.khachHang.id + "?page=" + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        } else if ($scope.status == 2) {
            apiUrl += '&status=' + 2;
        } else if ($scope.status == 3) {
            apiUrl += '&status=' + 3;
        } else if ($scope.status == 4) {
            apiUrl += '&status=' + 4;
        } else if ($scope.status == 5) {
            apiUrl += '&status=' + 5;
        } else {
            apiUrl += '&status=' + 1;
        }


        $http.get(apiUrl)
            .then(function (response) {
                $scope.hoaDons = response.data.content;
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

    $scope.updateSelected = function () {
        console.log($scope.status);
        var updateStatus = $scope.status;
        if (updateStatus == 4) {
            updateStatus = 2;
        } else if (updateStatus == 2) {
            updateStatus == 3
        } else if (updateStatus == 3) {
            updateStatus == 1;
        }
        console.log(updateStatus);
        var selectedRows = $scope.hoaDons.filter(function (hoaDon) {
            return hoaDon.isSelected;
        });

        function convertStringToDateTime(dateTimeString) {
            // Chuyển đổi định dạng của chuỗi ngày
            var formattedDateTime = dateTimeString.replace(' ', 'T');
            return formattedDateTime;
        }

        selectedRows.forEach(function (hoaDon) {
            hoaDon.ngayTao = convertStringToDateTime(hoaDon.ngayTao);
            hoaDon.ngayShip = convertStringToDateTime(hoaDon.ngayShip);
            hoaDon.ngayNhan = convertStringToDateTime(hoaDon.ngayNhan);
            hoaDon.ngayThanhToan = convertStringToDateTime(hoaDon.ngayThanhToan);
        })


        selectedRows.forEach(function (selectedRow) {
            $http.get(host + '/rest/admin/hoa-don-chi-tiet/find-by-id-hoa-don/' + selectedRow.id)
                .then(function (response) {
                    selectedRow.listHoaDonChiTiet = response.data;
                }).catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                $location.path("/list");
            });

            selectedRow.trangThai == updateStatus;
        });

        $http.post(host + '/rest/admin/hoa-don/update-list-hdct', selectedRows)
            .then(function (response) {
                console.log(response);
                toastr["success"]("Cập nhật thành công");
                $location.path("/list");
            }).catch(function (error) {
            console.log(error);
        });

        console.log(selectedRows);
    };


    $scope.detailHoaDon = function (id) {
        $http.get(host + '/rest/admin/hoa-don/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });

        $http.get(host + '/rest/admin/hoa-don-chi-tiet/find-by-id-hoa-don/' + id)
            .then(function (response) {
                $scope.hoaDonChiTiets = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });
    }
});

app.controller("detailDonHangController", function ($scope, $http, $window, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get("http://localhost:8080/rest/admin/hoa-don-chi-tiet/find-by-id-hoa-don/" + id)
        .then(function (response) {
            $scope.lstHoaDonChiTiet = response.data;
            console.log($scope.lstHoaDonChiTiet);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });
});

app.controller("thanhToanController", function ($scope, $http, $window, $location, $routeParams, $timeout, $cookies) {
    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;
    $scope.tongTienHangKhachHang = 0;
    $scope.tongTienVoucher = 0;
    $scope.tongTienChuongTrinhGiamGia = 0;
    $scope.tongTienSanPham = 0;
    $scope.tongTienSauKhuyenMai = 0;
    $scope.idGioHang;
    $scope.tongSoLuongMua = 0;
    $scope.phuongThucThanhToan = 2;

    $scope.feeShippingPerOne = 0;

    $scope.diaChiNhanHang = {};
    $scope.khachHang = {};

    $scope.userLogged = false;

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

    function tinhTienShip() {
        if ($scope.tongSoLuongMua <= 0) {
            return;
        }

        let soDonHang = 0;
        if ($scope.tongSoLuongMua % 30 === 0) {
            soDonHang = parseInt($scope.tongSoLuongMua / 30);
        } else {
            soDonHang = parseInt($scope.tongSoLuongMua / 30) + 1;
        }

        $scope.phiVanChuyen = Math.round(soDonHang * $scope.feeShippingPerOne);
        $scope.tongTienPhaiTraDatHang = $scope.tongTienPhaiTra + $scope.phiVanChuyen;

    }

    $scope.submitDiaChi = function () {
        if (!$scope.diaChiNhanHang.wards.id || !$scope.diaChiNhanHang.districts.id || !$scope.diaChiNhanHang.provinces.id) {

            toastr["error"]("Lấy thông tin địa chỉ thất bại");
            return;
        }

        $scope.isLoading = true;
        $scope.tenNguoiNhan = $scope.diaChiNhanHang.tenNguoiNhan;
        $scope.sdtNhan = $scope.diaChiNhanHang.sdtNguoiNhan;
        $scope.diaChiNhan = $scope.diaChiNhanHang.diaChiNhan + ", " + $scope.diaChiNhanHang.wards.ten + ", " + $scope.diaChiNhanHang.districts.ten + ", " + $scope.diaChiNhanHang.provinces.ten;
        // if (document.getElementById('closeModalThongTinNhanHang')) {
        //     setTimeout(() => {
        //         document.getElementById('closeModalThongTinNhanHang').click();
        //     }, 0);
        // }

        logisticInfo.to_address = $scope.diaChiNhanHang.diaChiNhan;
        logisticInfo.to_ward_name = $scope.diaChiNhanHang.wards.ten;
        logisticInfo.to_district_name = $scope.diaChiNhanHang.districts.ten;
        logisticInfo.to_province_name = $scope.diaChiNhanHang.provinces.ten;
        if ($scope.tongTien < 80000000) {
            logisticInfo.insurance_value = parseInt($scope.tongTien);
        }
        console.log(logisticInfo);

        var header = {
            'ShopId': 189641,
            'Token': 'ecf11c4a-5d20-11ee-b1d4-92b443b7a897'
        }

        $http({
            method: 'POST',
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/preview',
            headers: {
                'ShopId': 189641,
                'Token': 'ecf11c4a-5d20-11ee-b1d4-92b443b7a897'
            },
            data: logisticInfo
        }).then(response => {
            console.log(response);
            $scope.tongThanhToan -= $scope.feeShippingPerOne;
            $scope.feeShippingPerOne = response.data.data.total_fee;
            $scope.tongThanhToan += $scope.feeShippingPerOne;
            console.log(response.data);
            $scope.isLoading = false;
        })
            .catch(error => {
                console.log(error);
                // toastr["error"]("Lấy thông tin thất bại");
                $scope.tongThanhToan -= $scope.feeShippingPerOne;
                $scope.feeShippingPerOne = 50000;
                $scope.tongThanhToan += $scope.feeShippingPerOne;
                $scope.isLoading = false;
            })
    }

    $scope.thayDoiDiaChiNhanHang = function () {
        let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang/khach-hang';

        apiUrl += '?idKhachHang=' + $scope.khachHang.id;
        console.log(apiUrl);
        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.updateTrangThai = function (dieuKien) {
        console.log(dieuKien)
        $scope.trangThai = {
            "id": dieuKien,
            "trangThai": 1,
            "idKhachHang": $scope.khachHang.id
        }

        $http({
            method: 'PUT',
            url: 'http://localhost:8080/rest/khach-hang/dia-chi-nhan-hang/update-trang-thai/' + dieuKien,

            data: $scope.trangThai
        }).then(function successCallback(response) {
            // Xử lý khi API UPDATE thành công
            console.log('UPDATE điều kiện giảm giá thành công', response);
            loadDiaChi();
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi', response);
        });
        console.log("Điều kiện: ", dieuKien)

    };

    function loadDiaChi() {
        let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang/khach-hang';

        apiUrl += '?idKhachHang=' + $scope.khachHang.id;
        console.log(apiUrl);
        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data;
                console.log($scope.diaChiNhanHangs);
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.updateDiaChiNhan = function (id) {
        $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + $scope.khachHang.id)

            .then(function (response) {
                $scope.diaChiNhanHang = response.data;
                setData();
                $('#modalDiaChi').modal('show');
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại1");
            });

        // UPDATE địa chỉ nhận
        $scope.updateDiaChiNhanHang = function () {
            if ($scope.diaChiNhanHangForm.$invalid) {
                return;
            }
            const diaChiNhanHangUpdate = {
                diaChiNhan: $scope.diaChiNhanHang.diaChiNhan,
                soDienThoaiNhan: $scope.diaChiNhanHang.soDienThoaiNhan,
                hoTen: $scope.diaChiNhanHang.hoTen,
                provinces: $scope.diaChiNhanHang.provinces.ten,
                districts: $scope.diaChiNhanHang.districts.ten,
                wards: $scope.diaChiNhanHang.wards.ten


            };
            // $scope.diaChiNhanHang.districts = $scope.diaChiNhanHang.districts.ten;
            // $scope.diaChiNhanHang.provinces = $scope.diaChiNhanHang.provinces.ten;
            // $scope.diaChiNhanHang.wards = $scope.diaChiNhanHang.wards.ten;

            $http.put(host + '/rest/khach-hang/dia-chi-nhan-hang/' + $scope.khachHang.id, diaChiNhanHangUpdate)

                .then(function (response) {
                    if (response.status == 200) {
                        toastr["success"]("Cập nhật thành công")
                        $('#modalDiaChi').modal('hide');
                        console.log("thông tin sau cập nhập")
                        console.log(diaChiNhanHangUpdate)
                        loadDiaChi();
                    } else {
                        toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                    }
                    $location.path("/list");
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

    $scope.selectedDiaChiNhanHang = function (diaChiNhanHang) {

        $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + diaChiNhanHang.id)
            .then(function (response) {
                console.log(response);
                $scope.diaChiNhanHang = response.data;
                angular.forEach($scope.diaChiNhanHangs, function (item) {
                    item.selected = item.id === diaChiNhanHang.id;
                });
                setData();

            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            // $location.path("/list");
        });
    }


    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.provinces = [];
    $scope.districts = []
    $scope.wards = [];

    $http.get(host + "/rest/provinces/get-all")
        .then(function (response) {
            $scope.provinces = response.data;
        })
        .catch(function (error) {
            console.log(error)
            toastr["error"]("Lấy dữ liệu tỉnh thất bại");
        });


//    lấy dữ liệu huyện theo id tỉnh
    $scope.changeProvince = function () {
        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHangAdd.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                $scope.wards = [];

                // console.log($scope.diaChiNhanHang.provinces)
            })
            .catch(function (error) {
                console.log(error)
                toastr["error"]("Lấy dữ huyện thất bại");
            });
    }
    // lấy dữ liệu theo xã theo huyện
    $scope.changeDistrict = function () {
        if ($scope.diaChiNhanHangAdd.districts.id == 'undefined') {
            alert(" mời bạn chọn tỉnh")
        } else {
            $http.get(host + "/rest/wards/" + $scope.diaChiNhanHangAdd.districts.id)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    console.log(error)
                    toastr["error"]("Lấy dữ liệu xã thất bại");
                });
        }

    }

    function setData() {
            for (let i = 0; i < $scope.provinces.length; i++) {
                if ($scope.provinces[i].ten === $scope.diaChiNhanHang.provinces) {
                    $scope.diaChiNhanHang.provinces = $scope.provinces[i];
                    break;
                }
            }

            $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
                .then(function (response) {
                    $scope.districts = response.data;
                    for (let j = 0; j < $scope.districts.length; j++) {
                        if ($scope.districts[j].ten === $scope.diaChiNhanHang.districts) {
                            $scope.diaChiNhanHang.districts = $scope.districts[j];
                            $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                                .then(function (response) {
                                    $scope.wards = response.data;
                                    for (let k = 0; k < $scope.wards.length; k++) {
                                        if ($scope.wards[k].ten === $scope.diaChiNhanHang.wards) {
                                            $scope.diaChiNhanHang.wards = $scope.wards[k];
                                            console.log($scope.diaChiNhanHang);
                                            $scope.submitDiaChi();
                                            break;
                                        }
                                    }
                                })
                                .catch(function (error) {
                                    console.log(error)
                                    toastr["error"]("Lấy dữ liệu xã thất bại");
                                });
                            break;
                        }
                    }
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ huyện thất bại");
                    console.log(error);
                });
    }


    $scope.tongKhuyenMaiTheoDot = 0;
    $scope.dieuKienChoosed = {};

    $scope.khachHang = {};
    $scope.diaChiNull = false;

    var token = $cookies.get('token');
    if (token) {
        $scope.userLogged = true;
        $http.get(host + "/session/get-customer")
            .then(response => {
                if (response.data !== null) {
                    $scope.khachHang = response.data;
                    $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id)
                        .then(function (response) {
                            $scope.khachHang.idGioHang = response.data.id;
                            $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + $scope.khachHang.id + "/khach-hang/get-one")
                                .then(function (response) {
                                    if(response.data === ""){
                                        $scope.errosDiaChiNhan = "Vui lòng thêm địa chỉ để tiếp tục thanh toán !"
                                    }else{
                                        $scope.diaChiNull = true;
                                        $scope.diaChiNhanHang = response.data;
                                        console.log(response.data);
                                        setData();
                                    }

                                }).catch(function (error) {
                                console.log(error);
                                toastr["error"]("Lấy dữ liệu thất bại");
                                // $location.path("/list");
                            });
                            $http.get("http://localhost:8080/rest/admin/khach-hang/" + $scope.khachHang.id).then(function (response) {
                                $scope.khachHang = response.data;
                                console.log($scope.khachHang);

                            }).catch(function (error) {
                                console.log(error)
                            })

                            $scope.loadCartByIdKhachHang();
                        }).catch(function (error) {

                    })
                }
            })
    } else {
        var gioHangFromCookies = localStorage.getItem('gioHang') || '[]';
        console.log(gioHangFromCookies);
        $scope.gioHang = JSON.parse(gioHangFromCookies);
        $scope.gioHang.sort(function (a, b) {
            return a.idBienTheGiay - b.idBienTheGiay;
        });

        var idList = $scope.gioHang.map(function (item) {
            return item.idBienTheGiay || item.bienTheGiay;
        });
        var resultJson = {"ids": idList};
        $http.post("http://localhost:8080/rest/admin/giay/bien-the/get-all-by-list-id", resultJson)
            .then(function (response) {
                $scope.listBienTheGiayLocalStorage = response.data;
                if ($scope.listBienTheGiayLocalStorage.length === 0) {
                    $location.path("/cart");
                }
                $scope.gioHang.forEach(function (item1) {
                    var correspondingObject = $scope.listBienTheGiayLocalStorage.find(function (item2) {
                        return item2.id === item1.idBienTheGiay;
                    });
                    if (correspondingObject) {
                        correspondingObject.soLuongMua = item1.soLuong;
                    }
                });

                $scope.tongKhuyenMaiSanPham = 0;
                $scope.tongKhuyenMaiHoaDon = 0;

                $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                    var giaSauKhuyenMai = item.giaBan;
                    if (item.khuyenMai) {
                        giaSauKhuyenMai = (giaSauKhuyenMai * item.khuyenMai) / 100;
                        item.giaBanSauKhuyenMai = item.giaBan - giaSauKhuyenMai;
                        $scope.tongKhuyenMaiSanPham += giaSauKhuyenMai;
                        $scope.tongTienSanPham += item.soLuongMua * item.giaBanSauKhuyenMai;
                    } else {
                        $scope.tongTienSanPham += item.soLuongMua * item.giaBan;
                    }

                    $scope.tongTien = $scope.tongTienSanPham;
                    $scope.tongSoLuongMua += item.soLuongMua;
                });
                $http.get(host + '/rest/admin/dot-giam-gia/get-all-active')
                    .then(function (response) {
                        $scope.khuyenMaiDot = 0;

                        angular.forEach(response.data, function (item) {
                            var maxDiscount = 0;

                            angular.forEach(item.dieuKienResponses, function (condition) {
                                if (condition.tongHoaDon <= $scope.tongTien && condition.phanTramGiam > $scope.khuyenMaiDot) {
                                    $scope.khuyenMaiDot = condition.phanTramGiam;
                                    $scope.dieuKienChoosed = condition;
                                }
                            });

                        });
                        $scope.tongKhuyenMaiTheoDot = ($scope.tongTien * $scope.khuyenMaiDot) / 100;
                        $scope.tongTienChuongTrinhGiamGia = $scope.tongTien - ($scope.tongTien * $scope.khuyenMaiDot) / 100;

                        $scope.tongTienSauKhuyenMai = $scope.tongTien  - $scope.tongKhuyenMaiTheoDot;
                        console.log($scope.tongTienSauKhuyenMai);
                        $scope.tongThanhToan = $scope.tongTienSauKhuyenMai;


                    }).catch(function (error) {
                    console.log(error);
                })


                // $scope.tongThanhToan = $scope.tongTien - $scope.tongKhuyenMaiSanPham;

                console.log($scope.listBienTheGiayLocalStorage);

            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }


    $scope.testThanhToan = function () {
        console.log($scope.listBienTheGiayLocalStorage);
    }


    $scope.getErrorId = function (error) {
        return error.split(':')[0].trim();
    };

    $scope.productsLessQuantity = [];
    $scope.productsSoldOutQuantity = [];
    $scope.phieuGiamGiaList = [];
    // Thanh toán

    $scope.getPhieuGiamGia = function () {
        $http.get("http://localhost:8080/rest/admin/khach-hang/" + $scope.khachHang.id).then(function (response) {
            $scope.khachHang = response.data;
            $http.get("/rest/admin/phieu-giam-gia/get-all-by-hang-khach-hang?hangKhachHang=" + $scope.khachHang.hangKhachHang.tenHang)
                .then(function (response) {
                    $scope.phieuGiamGiaList = response.data;
                }).catch(function (error) {
                console.log(error)
            })
        }).catch(function (error) {
            console.log(error)
        })

    }

    $scope.addPhieuGiamGia = function (phieuGiamGia) {
        $http.get("/rest/admin/phieu-giam-gia/" + phieuGiamGia.id)
            .then(function (response) {
                if ($scope.tongTien >= response.data.giaTriDonToiThieu && response.data.trangThai === 1 && response.data.soLuongPhieu > 0) {
                    var tongTienPhieuGiamGia = ($scope.tongTien * response.data.phanTramGiam) / 100;
                    $scope.tongTienSauKhuyenMai = $scope.tongTien - $scope.tongKhuyenMaiHoaDon - $scope.tongKhuyenMaiTheoDot - tongTienPhieuGiamGia;
                    $scope.tongThanhToan = $scope.tongTienSauKhuyenMai + $scope.feeShippingPerOne;
                    if ($scope.tongTienSauKhuyenMai < 0) {
                        $scope.tongTienSauKhuyenMai = 0;
                    }
                    $scope.phieuGiamGiaChoosed = response.data;
                    $scope.getPhieuGiamGia();
                } else if ($scope.tongTien <= response.data.giaTriDonToiThieu) {
                    toastr["info"]("Không khả dụng , Tổng tiền đơn hàng chưa đủ điều kiện !");
                    $scope.getPhieuGiamGia();
                } else if (response.data.soLuongPhieu <= 0 || response.data.trangThai != 1) {
                    toastr["info"]("Phiếu giảm giá đã hết !");
                    $scope.getPhieuGiamGia();
                } else if (response.data.trangThai) {
                    toastr["warning"]("Có lỗi , vui lòng tải lại trang !");
                }
            }).catch(function (error) {
        })
    }


    $scope.thanhToan = function () {
        if(angular.equals({},$scope.diaChiNhanHang)){
            toastr["warning"]('Vui lòng thêm địa chỉ nhận hàng');
        } else if ($scope.listBienTheGiayLocalStorage.length != 0) {
            $http.post("/rest/user/gio-hang/check-so-luong", $scope.listBienTheGiayLocalStorage)
                .then(function (response) {
                    $scope.hoaDonThanhToan = {};

                    if ($scope.khachHang && $scope.khachHang.id !== undefined && $scope.khachHang.id !== null) {
                        $scope.hoaDonThanhToan.khachHang = $scope.khachHang;
                        $scope.hoaDonThanhToan.email = $scope.khachHang.email;
                        $scope.hoaDonThanhToan.id = $scope.idGioHang;
                    } else {
                        $scope.hoaDonThanhToan.email = $scope.khachHang.email;
                    }
                    if (!angular.equals({}, $scope.phieuGiamGiaChoosed)) {
                        $scope.hoaDonThanhToan.phieuGiamGia = $scope.phieuGiamGiaChoosed;
                    }

                    if (!angular.equals({}, $scope.dieuKienChoosed)) {
                        $scope.hoaDonThanhToan.dieuKien = $scope.dieuKienChoosed;
                    } else {
                        $scope.hoaDonThanhToan.dieuKien = null;
                    }


                    $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                        var giaSauKhuyenMai = item.giaBan;
                        if (item.khuyenMai) {
                            giaSauKhuyenMai = giaSauKhuyenMai - (giaSauKhuyenMai * item.khuyenMai) / 100;
                            item.giaBanSauKhuyenMai = giaSauKhuyenMai;
                        }
                    });


                    $scope.hoaDonThanhToan.phiShip = $scope.feeShippingPerOne;
                    $scope.hoaDonThanhToan.soDienThoaiNhan = $scope.diaChiNhanHang.soDienThoai;
                    $scope.hoaDonThanhToan.diaChiNhan = $scope.diaChiNhanHang.diaChiNhan + ', ' + $scope.diaChiNhanHang.wards.fullName + ', ' + $scope.diaChiNhanHang.districts.fullName + ', ' + $scope.diaChiNhanHang.provinces.fullName;
                    $scope.hoaDonThanhToan.trangThai = 4;
                    $scope.hoaDonThanhToan.bienTheGiayRequests = $scope.listBienTheGiayLocalStorage;
                    $scope.hoaDonThanhToan.tongTien = $scope.tongTien;
                    $scope.hoaDonThanhToan.tongTienSanPham = $scope.tongTien + $scope.feeShippingPerOne;
                    $scope.hoaDonThanhToan.tongTienHangKhachHang = $scope.tongTienHangKhachHang;
                    $scope.hoaDonThanhToan.tongTienChuongTrinhGiamGia = $scope.tongTienChuongTrinhGiamGia;
                    $scope.hoaDonThanhToan.tongTienThanhToan = $scope.tongThanhToan;
                    $scope.hoaDonThanhToan.tongTienSanPham = $scope.tongTienSanPham;
                    $scope.hoaDonThanhToan.tongTienSauKhuyenMai = $scope.tongTienSauKhuyenMai;
                    $scope.hoaDonThanhToan.phuongThuc = $scope.phuongThucThanhToan;
                    console.log($scope.hoaDonThanhToan);
                    if ($scope.hoaDonThanhToan.tongTienThanhToan < 10001) {
                        toastr["error"]('Tiền thanh toán không được nhỏ hơn 10.000vnđ');
                        return;
                    }

                    if ($scope.hoaDonThanhToan.tongTienThanhToan > 999999999) {
                        toastr["error"]('Tiền thanh toán không được lớn hơn 999.999.999vnđ');
                        return;
                    }
                    console.log($scope.hoaDonThanhToan);
                    $http.post("http://localhost:8080/rest/user/hoa-don", $scope.hoaDonThanhToan)
                        .then(function (response) {
                            if ($scope.hoaDonThanhToan.phuongThuc === 2) {
                                let request = {};
                                request.idHoaDon = response.data.id;
                                request.tienChuyenKhoan = $scope.hoaDonThanhToan.tongTienThanhToan;
                                $http.post(host + "/vnpay/create-vnpay-order", request)
                                    .then(response => {
                                        $scope.loading = true;
                                        window.location.href = response.data;
                                    })
                                    .catch(err => {
                                        console.log(err);
                                    })
                            } else {
                                toastr["success"]('Đặt hàng thành công');
                                if ($scope.khachHang && $scope.khachHang.id !== undefined && $scope.khachHang.id !== null) {
                                    $location.path("/don-hang");
                                } else {
                                    $window.localStorage.removeItem('gioHang');
                                    $location.path("/home");
                                }
                            }
                        }).catch(function (error) {
                        console.log(error);
                        if (error.data.dieuKienError) {
                            toastr["warning"](error.data.dieuKienError);
                        } else if (error.status == 400) {
                            toastr["warning"]("Có lỗi vui lòng kiểm tra lại !");
                        } else if (error.data.khuyenMaiError) {
                            toastr["warning"](error.data.khuyenMaiError);
                        } else if (error.data.khuyenMaiHangKhachHangError) {
                            toastr["warning"](error.data.khuyenMaiHangKhachHangError);
                        } else if (error.data.phieuGiamGiaError) {
                            toastr["warning"](error.data.phieuGiamGiaError);
                        } else if (error.status) {

                        }
                    })

                    $scope.showErrorMessages = function (errorData) {


                        for (let i = 0; i < errorData.length; i++) {
                            let errorParts = errorData[i].split(':');
                            let errorId = errorParts[0].trim();
                            let status = errorParts[1].trim();

                            // Tìm sản phẩm có ID trùng với errorId trong danh sách và hiển thị message
                            let productWithError = $scope.listBienTheGiayLocalStorage.find(function (product) {
                                return product.id === parseInt(errorId);
                            });

                            if (productWithError && parseInt(status) === 2) {
                                $scope.productsLessQuantity.push(productWithError);
                            } else {
                                $scope.productsSoldOutQuantity.push(productWithError);
                            }
                        }
                        if ($scope.productsLessQuantity.length > 0 || $scope.productsSoldOutQuantity.length > 0) {
                            var soldoutElement = document.getElementById('soldout');
                            if (soldoutElement) {
                                $timeout(function () {
                                    soldoutElement.click();
                                });
                            }
                        }
                    };
                }).catch(function (error) {
                console.log(error);
                if (error.status == 409) {
                    $scope.showErrorMessages(error.data);
                }
            })


        } else {
            toastr["warning"]("Giỏ hàng trống , vui lòng thêm sản phẩm !");
        }
        $scope.showErrorMessages = function (errorData) {
            $scope.productsLessQuantity = [];
            $scope.productsSoldOutQuantity = [];

            for (let i = 0; i < errorData.length; i++) {
                let errorParts = errorData[i].split(':');
                let errorId = errorParts[0].trim();
                let status = errorParts[1].trim();

                // Tìm sản phẩm có ID trùng với errorId trong danh sách và hiển thị message
                let productWithError = $scope.listBienTheGiayLocalStorage.find(function (product) {
                    return product.id === parseInt(errorId);
                });

                if (productWithError && parseInt(status) === 2) {
                    $scope.productsLessQuantity.push(productWithError);
                } else {
                    $scope.productsSoldOutQuantity.push(productWithError);
                }
            }
            if ($scope.productsLessQuantity.length > 0 || $scope.productsSoldOutQuantity.length > 0) {
                var soldoutElement = document.getElementById('soldout');
                if (soldoutElement) {
                    $timeout(function () {
                        soldoutElement.click();
                    });
                }
            }
        };
    }


    $scope.isExpanded = false;
    var targetElement = document.getElementById('cart');
    var iconDropDown = document.getElementById('icon-drop-down');
    targetElement.style.height = '0';
    targetElement.style.overflow = 'hidden';

    $scope.toggleHeight = function () {
        var targetElement = document.getElementById('cart');

        if ($scope.isExpanded) {
            targetElement.style.height = '0';
            targetElement.style.overflow = 'hidden';
            iconDropDown.classList.remove("rotate-icon");
        } else {
            var cartHeight = targetElement.scrollHeight;
            targetElement.style.height = cartHeight + 'px';
            targetElement.style.overflow = 'visible';
            iconDropDown.classList.add("rotate-icon");

        }

        $scope.isExpanded = !$scope.isExpanded;
    };


    $scope.loadCartByIdKhachHang = function () {
        $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.khachHang.id).then(function (response) {
            console.log(response.data);
            var bienTheGiayList = [];
            $scope.idGioHang = response.data.id;
            angular.forEach(response.data.gioHangChiTietResponses, function (gioHangChiTiet) {
                var bienTheGiay = gioHangChiTiet.bienTheGiay;

                bienTheGiay.soLuongMua = gioHangChiTiet.soLuong;
                bienTheGiay.idGioHang = gioHangChiTiet.id;
                bienTheGiayList.push(bienTheGiay);
            });
            $scope.listBienTheGiayLocalStorage = bienTheGiayList;
            $scope.tongKhuyenMaiSanPham = 0;
            $scope.tongKhuyenMaiHoaDon = 0;
            $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                var giaSauKhuyenMai = item.giaBan;
                if (item.khuyenMai) {
                    giaSauKhuyenMai = (giaSauKhuyenMai * item.khuyenMai) / 100;
                    item.giaBanSauKhuyenMai = item.giaBan - giaSauKhuyenMai;
                    $scope.tongKhuyenMaiSanPham += giaSauKhuyenMai;
                    $scope.tongTienSanPham += item.soLuongMua * item.giaBanSauKhuyenMai;
                } else {
                    $scope.tongTienSanPham += item.soLuongMua * item.giaBan;
                }

                $scope.tongTien = $scope.tongTienSanPham;
                $scope.tongSoLuongMua += item.soLuongMua;
            });
            if ($scope.listBienTheGiayLocalStorage.length === 0) {
                $location.path("/cart");
            }
            $http.get(host + '/rest/admin/dot-giam-gia/get-all-active')
                .then(function (response) {
                    console.log(response);
                    $scope.khuyenMaiDot = 0;

                    angular.forEach(response.data, function (item) {
                        var maxDiscount = 0;

                        angular.forEach(item.dieuKienResponses, function (condition) {
                            if (condition.tongHoaDon <= $scope.tongTien && condition.phanTramGiam > $scope.khuyenMaiDot) {
                                $scope.khuyenMaiDot = condition.phanTramGiam;
                                $scope.dieuKienChoosed = condition;
                            }
                        });

                    });
                    $scope.tongKhuyenMaiTheoDot = ($scope.tongTien * $scope.khuyenMaiDot) / 100;
                    $scope.tongTienChuongTrinhGiamGia = $scope.tongTien - ($scope.tongTien * $scope.khuyenMaiDot) / 100;
                    console.log($scope.khachHang);
                    $scope.tongTienHangKhachHang = $scope.tongTien - ($scope.tongTien * $scope.khachHang.hangKhachHang.uuDai) / 100; // non login
                    $scope.tongKhuyenMaiHoaDon = ($scope.tongTien * $scope.khachHang.hangKhachHang.uuDai) / 100; // non login

                    $scope.tongTienSauKhuyenMai = $scope.tongTien - $scope.tongKhuyenMaiHoaDon - $scope.tongKhuyenMaiTheoDot;
                    $scope.tongThanhToan = $scope.tongTienSauKhuyenMai;

                }).catch(function (error) {
                console.log(error)
            })


        }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            $scope.isLoading = false;
        });

    }

    $scope.loadLocalStorage = function () {
        var gioHangFromCookies = localStorage.getItem('gioHang') || '[]';
        console.log(gioHangFromCookies);
        $scope.gioHang = JSON.parse(gioHangFromCookies);
        $scope.gioHang.sort(function (a, b) {
            return a.idBienTheGiay - b.idBienTheGiay;
        });

        var idList = $scope.gioHang.map(function (item) {
            return item.idBienTheGiay || item.bienTheGiay;
        });
        var resultJson = {"ids": idList};
        $http.post("http://localhost:8080/rest/admin/giay/bien-the/get-all-by-list-id", resultJson)
            .then(function (response) {
                $scope.listBienTheGiayLocalStorage = response.data;
                if ($scope.listBienTheGiayLocalStorage.length === 0) {
                    $location.path("/cart");
                }
                $scope.gioHang.forEach(function (item1) {
                    var correspondingObject = $scope.listBienTheGiayLocalStorage.find(function (item2) {
                        return item2.id === item1.idBienTheGiay;
                    });
                    if (correspondingObject) {
                        correspondingObject.soLuongMua = item1.soLuong;
                    }
                });

                $scope.tongKhuyenMaiSanPham = 0;
                $scope.tongKhuyenMaiHoaDon = 0;

                $scope.listBienTheGiayLocalStorage.forEach(function (item) {

                    var giaSauKhuyenMai = item.giaBan;
                    if (item.khuyenMai) {
                        giaSauKhuyenMai = (giaSauKhuyenMai * item.khuyenMai) / 100;
                        item.giaBanSauKhuyenMai = item.giaBan - giaSauKhuyenMai;
                        $scope.tongKhuyenMaiSanPham += giaSauKhuyenMai;
                        $scope.tongTienSanPham += item.soLuongMua * item.giaBanSauKhuyenMai;
                    } else {
                        $scope.tongTienSanPham += item.soLuongMua * item.giaBan;
                    }

                    $scope.tongTien = $scope.tongTienSanPham;
                    $scope.tongSoLuongMua += item.soLuongMua;
                });
                $http.get(host + '/rest/admin/dot-giam-gia/get-all-active')
                    .then(function (response) {
                        $scope.khuyenMaiDot = 0;

                        angular.forEach(response.data, function (item) {
                            var maxDiscount = 0;

                            angular.forEach(item.dieuKienResponses, function (condition) {
                                if (condition.tongHoaDon <= $scope.tongTien && condition.phanTramGiam > $scope.khuyenMaiDot) {
                                    $scope.khuyenMaiDot = condition.phanTramGiam;
                                    $scope.dieuKienChoosed = condition;
                                }
                            });

                        });
                        $scope.tongKhuyenMaiTheoDot = ($scope.tongTien * $scope.khuyenMaiDot) / 100;
                        $scope.tongTienChuongTrinhGiamGia = $scope.tongTien - ($scope.tongTien * $scope.khuyenMaiDot) / 100;

                        $scope.tongTienSauKhuyenMai = $scope.tongTien  - $scope.tongKhuyenMaiTheoDot;
                        console.log($scope.tongTienSauKhuyenMai);
                        $scope.tongThanhToan = $scope.tongTienSauKhuyenMai;


                    }).catch(function (error) {
                    console.log(error);
                })


                // $scope.tongThanhToan = $scope.tongTien - $scope.tongKhuyenMaiSanPham;

                console.log($scope.listBienTheGiayLocalStorage);

            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    $scope.diaChiNhanHangAdd = {};
    $scope.addDiaChiNhanHang = function () {
        // alert()
        // if ($scope.diaChiNhanHangForm.$invalid) {
        //     return;
        // }

        $scope.diaChiNhanHangAdd.districts = $scope.diaChiNhanHangAdd.districts.ten;
        $scope.diaChiNhanHangAdd.provinces = $scope.diaChiNhanHangAdd.provinces.ten;
        $scope.diaChiNhanHangAdd.wards = $scope.diaChiNhanHangAdd.wards.ten;
        if ($scope.diaChiNhanHangAdd.macDinh) {
            $scope.diaChiNhanHangAdd.macDinh = $scope.diaChiNhanHangAdd.macDinh;
        } else {
            $scope.diaChiNhanHangAdd.macDinh = null;
        }
        $scope.diaChiNhanHangAdd.idKhachHang = $scope.khachHang.id;


        console.log("abc"+$scope.diaChiNhanHangAdd);
        $http.post(host + '/rest/khach-hang/dia-chi-nhan-hang', $scope.diaChiNhanHangAdd)
            .then(function (response) {
                if (response.status === 200) {
                    $scope.diaChiNhanHangAdd = {};
                    toastr["success"]("Thêm thành công");
                    $('#modalThemDiaChiNhanHang').modal('hide');
                    loadDiaChi();
                    $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + $scope.khachHang.id + "/khach-hang/get-one")
                        .then(function (response) {
                            if(response.data === ""){
                                $scope.errosDiaChiNhan = "Vui lòng thêm địa chỉ để tiếp tục thanh toán !"
                            }else{
                                $scope.errosDiaChiNhan = null;
                                $scope.diaChiNull = true;
                                $scope.diaChiNhanHang = response.data;
                                console.log(response.data);
                                setData();
                            }

                        }).catch(function (error) {
                        console.log(error);
                        toastr["error"]("Lấy dữ liệu thất bại");
                        // $location.path("/list");
                    });
                }
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                console.log(error);
                if (error.status === 400) {
                    console.log($scope.diaChiNhanHangForm);
                    $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                    $scope.diaChiNhanHangForm.diaChiNhan.$dirty = false;
                    $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }


    $scope.chonDiaChiNhanHang = function(){
        if ($scope.diaChiNhanHangForm.$invalid) {
            return;
        }

        $scope.diaChiNhanHang.districts = $scope.diaChiNhanHangAdd.districts.ten;
        $scope.diaChiNhanHang.provinces = $scope.diaChiNhanHangAdd.provinces.ten;
        $scope.diaChiNhanHang.wards = $scope.diaChiNhanHangAdd.wards.ten;
        $scope.diaChiNhanHang.hoTen = $scope.diaChiNhanHangAdd.hoTen;
        $scope.diaChiNhanHang.soDienThoaiNhan = $scope.diaChiNhanHangAdd.soDienThoaiNhan;
        $scope.diaChiNhanHang.email = $scope.diaChiNhanHangAdd.email;
        $scope.diaChiNhanHang = $scope.diaChiNhanHangAdd;
        $scope.khachHang.email = $scope.diaChiNhanHang.email;
        $scope.diaChiNull = true;
        console.log($scope.diaChiNhanHang);
        $scope.submitDiaChi();
        $scope.diaChiNhanHangAdd = {};
    }
})




