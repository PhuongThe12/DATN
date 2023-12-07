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
    })
        .otherwise({redirectTo: '/list'});
});

app.controller('navbarController', function ($rootScope, $scope, $http, $location, $cookies, $window) {
    var storedUserData = $window.localStorage.getItem('currentUser');
    if (!storedUserData) {
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
                        $window.localStorage.setItem('currentUser', JSON.stringify($scope.currentUser));
                        if ($scope.currentUser.role === 'ROLE_USER') {
                            $window.location.href = '/home';
                        } else if ($scope.currentUser.role === 'ROLE_STAFF') {
                            $window.location.href = '/admin/ban-hang';
                        } else {
                            $window.location.href = '/admin/tong-quan';
                        }
                    }
                }).catch(function (error) {
                console.log(error)
            });
        }

        function setTokenCookie(token, expiryDays) {
            const d = new Date();
            d.setTime(d.getTime() + (expiryDays * 24 * 60 * 60 * 1000));
            const expires = "expires=" + d.toUTCString();
            document.cookie = `token=${token}; ${expires}; path=/`;
        }
    }

    $scope.logoutUser = function (response) {
        var storedUserData = $window.localStorage.getItem('currentUser');
        if (storedUserData) {
            $window.localStorage.clear();
            $window.location.href = '/home';
        }
    }


    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;
    $scope.currentUser = {};

    var storedUserData = $window.localStorage.getItem('currentUser');

    if (storedUserData) {
        $scope.currentUser = JSON.parse(storedUserData);
        $http.get("http://localhost:8080/rest/admin/khach-hang/" + $scope.currentUser.idKhachHang)
            .then(function (response) {
                $scope.currentUser.hoTen = response.data.hoTen;
            }).catch(function (error) {

        })

        $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.currentUser.idKhachHang)
            .then(function (response) {
                $scope.currentUser.idGioHang = response.data.id;
                console.log($scope.currentUser);
                $window.localStorage.setItem('currentUser', JSON.stringify($scope.currentUser));
            }).catch(function (error) {

        })
    }
    $scope.isCartVisible = false;

    $scope.hideCart = function () {
        $scope.isCartVisible = false;
    }


})
app.controller('listProductController', function ($scope, $http, $location) {
    $scope.giays = [];
    $scope.curPage = 1, $scope.itemsPerPage = 999, $scope.maxSize = 9999;

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 12;

    let giaySearch = {};

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }
        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;
        console.log(giaySearch);
        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                console.log($scope.giays);
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

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

    $scope.lstSearchChoosed = [];
    $scope.dropdowns = [
        {
            ten: "Màu Sắc",
            listValue: [
                {name: "#FF0000", selected: false},  // Đỏ
                {name: "#00FF00", selected: false},  // Xanh lá cây
                {name: "#0000FF", selected: false},  // Xanh dương
                {name: "#FFFF00", selected: false},  // Vàng
                {name: "#FF00FF", selected: false},  // Hồng
                {name: "#00FFFF", selected: false},  // Lam
                {name: "#000000", selected: false}   // Đen
            ],
            isOpen: true
        },
        {
            ten: "Kích Cỡ", listValue: [
                {
                    name: 35, selected: false
                },
                {
                    name: 36, selected: false
                },
                {
                    name: 37, selected: false
                },
                {
                    name: 38, selected: false
                },
                {
                    name: 39, selected: false
                },
                {
                    name: 40, selected: false
                }, {
                    name: 35, selected: false
                },
                {
                    name: 36, selected: false
                },
                {
                    name: 37, selected: false
                },
                {
                    name: 38, selected: false
                },
                {
                    name: 39, selected: false
                },
                {
                    name: 40, selected: false
                }, {
                    name: 35, selected: false
                },
                {
                    name: 36, selected: false
                },
                {
                    name: 37, selected: false
                },
                {
                    name: 38, selected: false
                },
                {
                    name: 39, selected: false
                },
                {
                    name: 40, selected: false
                }
            ],
            isOpen: true
        },
        {
            ten: "Chất Liệu",
            listValue: [{name: "Leather", selected: false}, {name: "Mesh", selected: false}, {
                name: "Canvas",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Thương Hiệu",
            listValue: [{name: "Nike", selected: false}, {name: "Adidas", selected: false}, {
                name: "Puma",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Cổ Giày",
            listValue: [{name: "Low-top", selected: false}, {name: "High-top", selected: false}],
            isOpen: true
        },
        {
            ten: "Đế Giày",
            listValue: [{name: "Rubber", selected: false}, {name: "EVA", selected: false}, {
                name: "Phylon",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Mũi Giày",
            listValue: [{name: "Round Toe", selected: false}, {name: "Square Toe", selected: false}],
            isOpen: true
        },
        {
            ten: "Lót Giày",
            listValue: [{name: "Gel", selected: false}, {name: "Foam", selected: false}, {
                name: "Ortholite",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Dây Giày",
            listValue: [{name: "Flat", selected: false}, {name: "Round", selected: false}, {
                name: "Waxed",
                selected: false
            }],
            isOpen: true
        },

    ];


    $scope.toggleDropdown = function (index) {
        $scope.dropdowns[index].isOpen = !$scope.dropdowns[index].isOpen;
    };

    $scope.changeSearch = function () {
        $scope.lstSearchChoosed = [];
        for (let dropdown of $scope.dropdowns) {
            for (let value of dropdown.listValue) {
                if (value.selected) {
                    $scope.lstSearchChoosed.push({
                        dropdownName: dropdown.ten,
                        selectedValue: value.name,
                    });
                }
            }
        }
    };
});

app.controller('detailProductController', function ($scope, $http, $location, $cookies, $routeParams, $window) {
    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

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
            var storedUserData = $window.localStorage.getItem('currentUser');
            if (storedUserData) {
                $scope.currentUser = JSON.parse(storedUserData);
                $http.get("http://localhost:8080/rest/admin/giay/" + $scope.giayDetail.id + "/so-luong")
                    .then(function (response) {
                        var soLuongTrongKho = response.data;
                        // $http.get("http://localhost:8080/admin/rest/giay/" + $scope.giayDetail.id + "/so-luong")
                        $http.get("http://localhost:8080/rest/user/gio-hang/" + $scope.currentUser.idGioHang + "/so-luong/" + $scope.giayDetail.id)
                            .then(function (response) {
                                $scope.soLuongGioHangChiTiet = response.data;
                                console.log(response.data);

                                if ($scope.soLuongMua + response.data > soLuongTrongKho) {
                                    toastr["warning"]("Số lượng vượt quá trong kho");
                                } else {
                                    $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.currentUser.idKhachHang).then(function (response) {
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
                } else {
                    var index = timViTri($scope.gioHang, giaTriCanThem.idBienTheGiay);
                    $http.get("http://localhost:8080/rest/admin/giay/" + $scope.giayDetail.id + "/so-luong")
                        .then(function (response) {
                            if (($scope.gioHang[index].soLuong + $scope.soLuongMua) <= response.data) {
                                $scope.gioHang[index].soLuong += $scope.soLuongMua;
                                localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
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
                console.log($scope.gioHang);
                $scope.tongTien = 0;
                $scope.listBienTheGiayLocalStorage.forEach(function (item) {
                    $scope.tongTien += item.soLuongMua * item.giaBan;
                });

                console.log($scope.listBienTheGiayLocalStorage);

            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    var storedUserData = $window.localStorage.getItem('currentUser');

    if (storedUserData) {
        $scope.currentUser = JSON.parse(storedUserData);
        // Khách đã Login
        $scope.loadCartByIdKhachHang = function () {
            $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.currentUser.idKhachHang).then(function (response) {
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
                $scope.listBienTheGiayLocalStorage.forEach(function (item) {
                    $scope.tongTien += item.soLuongMua * item.giaBan;
                });
                console.log($scope.listBienTheGiayLocalStorage);

            }).catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });

        }

        $scope.loadCartByIdKhachHang();
        // $scope.loadLocalStorage();
    } else {
        // Khách chưa Login
        $scope.loadLocalStorage();
    }
    $scope.subtraction = function (bienTheGiay) {
        if (bienTheGiay.soLuongMua < 2) {
            toastr["warning"]("Số lượng phải lớn hơn 0");
        } else {
            if (!angular.equals({}, $scope.currentUser)) {
                var gioHangChiTietUpdate = {};
                gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
                gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua - 1;
                $http.put("http://localhost:8080/rest/user/gio-hang/update/so-luong", gioHangChiTietUpdate)
                    .then(function (response) {
                        $scope.loadCartByIdKhachHang();
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
        if (!angular.equals({}, $scope.currentUser)) {
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
        if (!angular.equals({}, $scope.currentUser)) {
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
                })
                .catch(function (error) {
                    console.error('Error:', error);
                });
        } else {
            var index = timViTri($scope.gioHang, bienTheGiay.id);
            $scope.gioHang.splice(index, 1);
            localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));

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
    var storedUserData = $window.localStorage.getItem('currentUser');
    if(!storedUserData){
        $window.location.href = '/home';
    }

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
        let apiUrl = host + '/rest/admin/hoa-don/khach-hang/1?page=' + currentPage;
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

app.controller("thanhToanController", function ($scope, $http, $window, $location, $routeParams, $timeout) {
    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;
    $scope.idGioHang;

    $scope.diaChiNhanHang = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }
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

//fix_
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

    var storedUserData = $window.localStorage.getItem('currentUser');

    if (storedUserData) {
        $http.get(host + '/rest/admin/dia-chi-nhan-hang/2')
            .then(function (response) {
                $scope.diaChiNhanHang = response.data;
                setData();
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            // $location.path("/list");
        });
        $scope.currentUser = JSON.parse(storedUserData);
        $http.get("http://localhost:8080/rest/admin/khach-hang/" + $scope.currentUser.idKhachHang).then(function (response) {
            $scope.khachHang = response.data;
        }).catch(function (error) {
            console.log(error)
        })

        $scope.loadCartByIdKhachHang = function () {
            $http.get("http://localhost:8080/rest/user/gio-hang/khach-hang/" + $scope.currentUser.idKhachHang).then(function (response) {
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
                $scope.tongTien = 0;
                $scope.listBienTheGiayLocalStorage.forEach(function (item) {
                    $scope.tongTien += item.soLuongMua * item.giaBan;
                });
                console.log($scope.listBienTheGiayLocalStorage);

            }).catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });

        }
        // $scope.loadLocalStorage();
        $scope.loadCartByIdKhachHang();
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
                    $scope.tongTien = 0;
                    $scope.listBienTheGiayLocalStorage.forEach(function (item) {
                        $scope.tongTien += item.soLuongMua * item.giaBan;
                    });

                    console.log($scope.listBienTheGiayLocalStorage);

                })
                .catch(function (error) {
                    console.log(error);
                    toastr["error"]("Lấy dữ liệu thất bại");
                    $scope.isLoading = false;
                });
        }

        $scope.loadLocalStorage();
    }

    $scope.getErrorId = function (error) {
        return error.split(':')[0].trim();
    };

    $scope.productsLessQuantity = [];
    $scope.productsSoldOutQuantity = [];
    // Thanh toán
    $scope.thanhToan = function () {
        if ($scope.listBienTheGiayLocalStorage.length != 0) {

            $http.post("/rest/user/gio-hang/check-so-luong", $scope.listBienTheGiayLocalStorage)
                .then(function (response) {
                    $scope.hoaDonThanhToan = {};
                    if(storedUserData){
                        $scope.hoaDonThanhToan.khachHang = $scope.khachHang;
                        $scope.hoaDonThanhToan.email = $scope.khachHang.email;
                    }else{
                        $scope.hoaDonThanhToan.email = $scope.khachHang.email;
                    }

                    $scope.hoaDonThanhToan.phiShip = 1000;
                    $scope.hoaDonThanhToan.soDienThoaiNhan = $scope.diaChiNhanHang.soDienThoaiNhan;
                    $scope.hoaDonThanhToan.diaChiNhan = $scope.diaChiNhanHang.diaChiNhan + ', '+$scope.diaChiNhanHang.wards.fullName + ', ' + $scope.diaChiNhanHang.districts.fullName + ', ' + $scope.diaChiNhanHang.provinces.fullName;
                    $scope.hoaDonThanhToan.trangThai = 4;
                    $scope.hoaDonThanhToan.bienTheGiayRequests = $scope.listBienTheGiayLocalStorage;
                    $scope.hoaDonThanhToan.id = $scope.idGioHang;
                    $http.post("http://localhost:8080/rest/user/hoa-don", $scope.hoaDonThanhToan)
                        .then(function (response) {
                            if(storedUserData){
                                $location.path("/don-hang");
                            }else{
                                $window.localStorage.removeItem('gioHang');
                                $location.path("/home");
                            }
                        }).catch(function (error) {
                        console.log(error);
                        if (error.status == 400) {
                            toastr["warning"]("Giỏ hàng đã được thanh toán , vui lòng load lại trang để kiểm tra lại !");
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
            })


        } else {
            toastr["warning"]("Giỏ hàng trống , vui lòng thêm sản phẩm !");
        }
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
})
