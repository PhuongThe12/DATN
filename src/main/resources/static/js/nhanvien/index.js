var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/nhanvien/views/list.html',
            controller: 'nhanVienListController'
        }).when("/detail", {
        templateUrl: '/pages/admin/nhanvien/views/detail.html',
        controller: 'detailNhanVienController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/nhanvien/views/update.html',
        controller: 'updateNhanVienController'
    }).when("/add", {
        templateUrl: '/pages/admin/nhanvien/views/add.html',
        controller: 'addNhanVienController'
    })
        .otherwise({redirectTo: '/list'});
});
// app.controller("indexController",function ($scope, $http, $location){
//     $scope.quanLy = ($location.search().cv === 'true');
//     // alert($scope.quanLy);
// })

app.controller("addNhanVienController", function ($scope, $http, $location, $window) {

    $scope.nhanVien = {
        gioiTinh: 1
    };

    $scope.chucVus = [
        {id: 1, name: "Nhân viên"},
        {id: 2, name: "Quản lý"}
    ]

    $scope.nhanVien.chucVu = $scope.chucVus.find(item => item.id === 1);

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addNhanVien = function () {
        if ($scope.nhanVienForm.$invalid) {
            return;
        }

        checkDiaChi();
        if ($scope.error.diaChi) {
            return;
        }

        $scope.nhanVien.chucVu = $scope.nhanVien.chucVu.id;
        $scope.nhanVien.xa = $scope.nhanVien.xa.ten;
        $scope.nhanVien.huyen = $scope.nhanVien.huyen.ten;
        $scope.nhanVien.tinh = $scope.nhanVien.tinh.ten;
        console.log($scope.nhanVien);
        $http.post(host + '/rest/admin/nhan-vien', $scope.nhanVien)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại: " + error.data.data);
                console.log(error)
                $scope.nhanVien.chucVu = $scope.chucVus.find(item => item.id === $scope.nhanVien.chucVu);
                $scope.nhanVien.xa = $scope.xas.find(item => item.ten === $scope.nhanVien.xa);
                $scope.nhanVien.huyen = $scope.huyens.find(item => item.ten === $scope.nhanVien.huyen);
                $scope.nhanVien.tinh = $scope.tinhs.find(item => item.ten === $scope.nhanVien.tinh);
            });
    }

    $scope.backToNhanVienList = function () {
        $window.location.href = '/admin/nhan-vien#/list';
    }

    $scope.changeTinh = function () {
        if ($scope.nhanVien.tinh && $scope.nhanVien.tinh.id) {
            $http.get(host + "/rest/districts/" + $scope.nhanVien.tinh.id)
                .then(response => {
                    $scope.huyens = response.data;
                    $scope.nhanVien.huyen = {};
                    $scope.nhanVien.xa = {};
                })
                .catch(err => {
                    toastr["error"]("Lấy thông tin địa chỉ thất bại");
                });
        }

    }

    $scope.changeHuyen = function () {
        if ($scope.nhanVien.huyen && $scope.nhanVien.huyen.id) {
            $http.get(host + "/rest/wards/" + $scope.nhanVien.huyen.id)
                .then(response => {
                    $scope.xas = response.data;
                    $scope.nhanVien.xa = {};
                })
                .catch(err => {
                    toastr["error"]("Lấy thông tin địa chỉ thất bại");
                });
        }
    }

    $scope.changeXa = function () {
        $scope.error = {};
    }

    $http.get(host + "/rest/provinces/get-all")
        .then(response => {
            $scope.tinhs = response.data;
        })
        .catch(err => {
            toastr["error"]("Lấy thông tin địa chỉ thất bại");
        });

    $scope.error = {};

    function checkDiaChi() {
        $scope.error = {};
        if (!$scope.nhanVien.tinh.id || !$scope.nhanVien.huyen.id || !$scope.nhanVien.xa.id) {
            $scope.error.diaChi = "Địa chỉ phải đầy đủ xã, huyện tỉnh";
        } else {
            $scope.error.diaChi = null;
        }
    }

});


app.controller("detailNhanVienController", function ($scope, $http, $window, $location, $cookies) {
    function convertObjectToString(obj) {
        var result = {};

        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                if (typeof obj[key] === 'object') {
                    result[key] = convertObjectToString(obj[key]);
                } else {
                    result[key] = String(obj[key]);
                }
            }
        }

        return result;
    }

    $http.get(host + '/rest/admin/nhan-vien/check-logged')
        .then(function (response) {
            if (response.status == 200) {
                var nhanVienAsString = convertObjectToString(response.data);
                var ngaySinh = new Date(nhanVienAsString.ngaySinh);
                nhanVienAsString.ngaySinh = ngaySinh;
                $scope.nhanVien = nhanVienAsString;
            }
        }).catch(function (error) {
        console.log(error);
        toastr["error"]("Không tìm thấy người dùng , vui lòng đăng nhập lại !");

    });
    $scope.updateNhanVien = function (updateAccount) {
        if ($scope.nhanVienForm.$invalid) {
            return;
        }
        console.log($scope.nhanVien);
        const nhanVienUpdate = {
            id: $scope.nhanVien.id,
            hoTen: $scope.nhanVien.hoTen,
            gioiTinh: $scope.nhanVien.gioiTinh,
            idTaiKhoan: $scope.nhanVien.taiKhoan.id,
            tenDangNhap: $scope.nhanVien.taiKhoan.tenDangNhap,
            matKhau: $scope.nhanVien.taiKhoan.matKhau,
            soDienThoai: $scope.nhanVien.soDienThoai,
            email: $scope.nhanVien.email,
            trangThai: $scope.nhanVien.trangThai,
            chucVu: $scope.nhanVien.chucVu,
            ngaySinh: $scope.nhanVien.ngaySinh,
            xa: $scope.nhanVien.xa,
            huyen: $scope.nhanVien.huyen,
            tinh: $scope.nhanVien.tinh,
            ghiChu: $scope.nhanVien.ghiChu,
            role: 1,
            updateAccount: updateAccount
        };
        console.log(nhanVienUpdate);
        $http.put(host + '/rest/admin/nhan-vien/' + nhanVienUpdate.id, nhanVienUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    $http.get(host + '/rest/admin/nhan-vien/check-logged')
                        .then(function (response) {
                            if (response.status == 200) {
                                if ($scope.nhanVienLogged.chucVu == 2) {
                                    $window.location.href = '/admin/nhan-vien#/list';
                                } else if ($scope.nhanVienLogged.chucVu == 1) {
                                    $window.location.href = '/admin/tong-quan#/home';
                                }
                            }
                        })
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.errors = error.data;
            }
        })
    };
})

app.controller("nhanVienListController", function ($scope, $http, $window, $location) {


    $scope.curPage = 1,
        $scope.itemsPerPage = 5,
        $scope.maxSize = 5;

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
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/nhan-vien?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status === 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status === 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.nhanViens = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.resetSearch = function () {
        searchText = null;
        $scope.searchText = '';
        $scope.status = -1;
        getData(1);
    }

    $scope.detailNhanVien = function (val) {
        var id = val;
        console.log(id);
        if (!isNaN(id)) {
            $scope.nhanVienDetail = $scope.nhanViens.find(function (nhanVien) {
                return nhanVien.id === id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });


    $scope.resetPassword = function (id) {
        Swal.fire({
            text: "Xác nhận reset mật khẩu? Mật khẩu mặc định sẽ là 123456",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $http.put(host + "/rest/admin/nhan-vien/reset-password/" + id)
                    .then((response) => {
                        toastr["success"]("Reset mật khẩu thành công");
                    })
                    .catch (err => {
                        toastr["error"]("Reset mật khẩu thất bại. Lỗi bất định");
                    })

            }
        });

    }

});

app.controller("updateNhanVienController", function ($scope, $http, $routeParams, $location, $window) {

    function convertObjectToString(obj) {
        var result = {};

        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                if (typeof obj[key] === 'object') {
                    result[key] = convertObjectToString(obj[key]);
                } else {
                    result[key] = String(obj[key]);
                }
            }
        }

        return result;
    }

    $scope.chucVus = [
        {id: 1, name: "Nhân viên"},
        {id: 2, name: "Quản lý"}
    ]

    $scope.changeTinh = function () {
        if ($scope.nhanVien.tinh && $scope.nhanVien.tinh.id) {
            $http.get(host + "/rest/districts/" + $scope.nhanVien.tinh.id)
                .then(response => {
                    $scope.huyens = response.data;
                    $scope.nhanVien.huyen = {};
                    $scope.nhanVien.xa = {};
                })
                .catch(err => {
                    toastr["error"]("Lấy thông tin địa chỉ thất bại");
                });
        }

    }

    $scope.changeHuyen = function () {
        if ($scope.nhanVien.huyen && $scope.nhanVien.huyen.id) {
            $http.get(host + "/rest/wards/" + $scope.nhanVien.huyen.id)
                .then(response => {
                    $scope.xas = response.data;
                    $scope.nhanVien.xa = {};
                })
                .catch(err => {
                    toastr["error"]("Lấy thông tin địa chỉ thất bại");
                });
        }
    }

    $scope.changeXa = function () {
        $scope.error = {};
    }

    $http.get(host + "/rest/provinces/get-all")
        .then(response => {
            $scope.tinhs = response.data;
        })
        .catch(err => {
            toastr["error"]("Lấy thông tin địa chỉ thất bại");
        });

    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.isLoading = true;
    $http.get(host + '/rest/admin/nhan-vien/' + id)
        .then(function (response) {
            var ngaySinh = new Date(response.data.ngaySinh);
            var nhanVienBefore = convertObjectToString(response.data);
            nhanVienBefore.ngaySinh = ngaySinh;
            $scope.nhanVien = nhanVienBefore;
            $scope.nhanVien.chucVu = $scope.chucVus.find(item => item.id == $scope.nhanVien.chucVu);
            $scope.nhanVien.trangThai = parseInt($scope.nhanVien.trangThai);

            $http.get(host + "/rest/provinces/get-all")
                .then(response => {
                    $scope.tinhs = response.data;
                    $scope.nhanVien.tinh = $scope.tinhs.find(item => item.ten === $scope.nhanVien.tinh);
                    $http.get(host + "/rest/districts/" + $scope.nhanVien.tinh.id)
                        .then(response => {
                            $scope.huyens = response.data;
                            $scope.nhanVien.huyen = $scope.huyens.find(item => item.ten === $scope.nhanVien.huyen);
                            $http.get(host + "/rest/wards/" + $scope.nhanVien.huyen.id)
                                .then(response => {
                                    $scope.xas = response.data;
                                    $scope.nhanVien.xa = $scope.xas.find(item => item.ten === $scope.nhanVien.xa);
                                    $scope.isLoading = false;
                                })
                        })
                })
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
            $scope.isLoading = false;
        });

    $scope.updateNhanVien = function () {
        if ($scope.nhanVienForm.$invalid) {
            return;
        }
        const nhanVienUpdate = {
            id: $scope.nhanVien.id,
            hoTen: $scope.nhanVien.hoTen,
            gioiTinh: $scope.nhanVien.gioiTinh,
            soDienThoai: $scope.nhanVien.soDienThoai,
            trangThai: $scope.nhanVien.trangThai,
            chucVu: $scope.nhanVien.chucVu.id,
            ngaySinh: $scope.nhanVien.ngaySinh,
            ghiChu: $scope.nhanVien.ghiChu,
            xa: $scope.nhanVien.xa.ten,
            huyen: $scope.nhanVien.huyen.ten,
            tinh: $scope.nhanVien.tinh.ten,
        };
        console.log(nhanVienUpdate);

        $http.put(host + '/rest/admin/nhan-vien/' + id, nhanVienUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            console.log(error);
            toastr["error"](error.data.data);
        })
    };
    $scope.backToNhanVienList = function () {
        $window.location.href = '/admin/nhan-vien#/list';
    }

});


app.directive('customSelect', function ($injector) {
    return {
        restrict: 'E', templateUrl: '/pages/admin/banhang/views/combobox.html', scope: {
            id: '@', title: '@', items: '=', ngModel: '=', onChange: '&'
        }, controller: function ($scope) {
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
                            $scope.onChange();
                        }
                        if (!newNgModel.id) {
                            $scope.selectedItem = {};
                        }
                    }
                }
            }, true);

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