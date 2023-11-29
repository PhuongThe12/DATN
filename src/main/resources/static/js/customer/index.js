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

app.controller('navbarController', function ($scope, $http, $location, $cookies) {

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
    $scope.curPage = 1, $scope.itemsPerPage = 999, $scope.maxSize = 9999;

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 12;

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
        console.log($scope.lstSearchChoosed);
    };
});

app.controller('detailProductController', function ($scope, $http, $location, $cookies, $routeParams) {
    $scope.giayChoosed = {}; // Biến thể giày được chọn khi chọn màu + size

    const id = $routeParams.id;

    $http.get(host + '/admin/rest/giay/' + id)
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
        //Khách chưa Login

        // if ($scope.giayChoosed.id === undefined) {
        //     toastr["warning"]("Vui lòng chọn sản phẩm");
        // } else {
        //     var giaTriCanThem = {idBienTheGiay: $scope.giayChoosed.id, soLuong: 1};
        //
        //     var tonTai = kiemTraTonTai($scope.gioHang, giaTriCanThem.idBienTheGiay);
        //     if (!tonTai) {
        //         $scope.gioHang.push(giaTriCanThem);
        //         $scope.listBienTheGiayLocalStorage.push($scope.giayChoosed);
        //         toastr["success"]("Thêm vào giỏ hàng thành công");
        //     } else {
        //         var index = timViTri($scope.gioHang, giaTriCanThem.idBienTheGiay);
        //         $scope.gioHang[index].soLuong++;
        //     }
        //
        //     localStorage.setItem('gioHang', JSON.stringify($scope.gioHang));
        //     $scope.loadLocalStorage();
        // }

        //Khách đã Login
        $http.get("http://localhost:8080/user/rest/gio-hang/1").then(function (response) {
            $scope.gioHangChiTiet = {};
            $scope.gioHangChiTiet.id = response.data.id;
            $scope.gioHangChiTiet.bienTheGiay = $scope.giayChoosed.id;
            console.log($scope.gioHangChiTiet);
            $http.post("http://localhost:8080/user/rest/gio-hang", $scope.gioHangChiTiet)
        }).catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            $scope.isLoading = false;
        });


    }

    // List Recommend

    $scope.curPage = 1,
        $scope.itemsPerPage = 6,
        $scope.maxSize = 6;

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


// Duyệt qua các trường trong productData


})


app.controller('cartProductController', function ($scope, $http, $location, $cookies) {
    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;
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

    $scope.loadCartByIdKhachHang = function () {
        $http.get("http://localhost:8080/user/rest/gio-hang/1").then(function (response) {
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

    $scope.subtraction = function (bienTheGiay) {
        var gioHangChiTietUpdate = {};
        gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
        gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua - 1;
        $http.put("http://localhost:8080/user/rest/gio-hang/update/so-luong", gioHangChiTietUpdate)
            .then(function (response) {
                $scope.loadCartByIdKhachHang();
            }).catch(function (error) {

        })

    }

    $scope.summation = function (bienTheGiay) {
        var gioHangChiTietUpdate = {};
        gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
        gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua + 1;
        console.log(gioHangChiTietUpdate);
        $http.put("http://localhost:8080/user/rest/gio-hang/update/so-luong", gioHangChiTietUpdate)
            .then(function (response) {
                $scope.loadCartByIdKhachHang();
            }).catch(function (error) {

        })

    }

    $scope.deleteBienTheGiay = function (bienTheGiay) {
        var gioHangChiTietUpdate = {};
        gioHangChiTietUpdate.id = bienTheGiay.idGioHang;
        gioHangChiTietUpdate.soLuong = bienTheGiay.soLuongMua
        console.log(gioHangChiTietUpdate);
        $http.delete("http://localhost:8080/user/rest/gio-hang/delete", {
            data: JSON.stringify(gioHangChiTietUpdate),
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(function (response) {
                $scope.loadCartByIdKhachHang();
            })
            .catch(function (error) {
                console.error('Error:', error);
            });
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
        let apiUrl = host + '/admin/rest/hoa-don/khach-hang/1?page=' + currentPage;
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
            $http.get(host + '/admin/rest/hoa-don-chi-tiet/find-by-id-hoa-don/' + selectedRow.id)
                .then(function (response) {
                    selectedRow.listHoaDonChiTiet = response.data;
                }).catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                $location.path("/list");
            });

            selectedRow.trangThai == updateStatus;
        });

        $http.post(host + '/admin/rest/hoa-don/update-list-hdct', selectedRows)
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
        $http.get(host + '/admin/rest/hoa-don/' + id)
            .then(function (response) {
                $scope.hoaDon = response.data;
            }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });

        $http.get(host + '/admin/rest/hoa-don-chi-tiet/find-by-id-hoa-don/' + id)
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
    $http.get("http://localhost:8080/admin/rest/hoa-don-chi-tiet/find-by-id-hoa-don/" + id)
        .then(function (response) {
            $scope.lstHoaDonChiTiet = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });
});

app.controller("thanhToanController", function ($scope, $http, $window, $location, $routeParams) {
    $scope.listBienTheGiayLocalStorage = [];
    $scope.tongTien = 0;
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
app.controller("thongTinTaiKhoanController", function ($scope, $http, $window, $location, $routeParams) {


});
