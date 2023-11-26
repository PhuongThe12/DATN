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

    $scope.addTest = function () {
        $http.post(host + '/admin/rest/nhan-vien', $scope.nhanVien)
            .then(function (response) {
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    // $scope.nhanVien = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.addNhanVien = function () {
        if ($scope.nhanVienForm.$invalid) {
            return;
        }
        console.log($scope.nhanVien);
        $http.post(host + '/admin/rest/nhan-vien', $scope.nhanVien)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                console.log(error)
                if (error.status === 400) {
                    $scope.errors = error.data;
                }
            });
    }

    $scope.backToNhanVienList = function () {
        $window.location.href = '/admin/nhan-vien#/list';
    }

});
// app.controller("indexController", function ($scope, $http, $location, $cookies, $window) {
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
//         $window.location.href = '/admin/tong-quan#/home';
//     });
// });


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

    $http.get(host + '/admin/rest/nhan-vien/check-logged')
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
            updateAccount : updateAccount
        };
        console.log(nhanVienUpdate);
        $http.put(host + '/admin/rest/nhan-vien/' + nhanVienUpdate.id, nhanVienUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    $http.get(host + '/admin/rest/nhan-vien/check-logged')
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
        $scope.itemsPerPage = 3,
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
        let apiUrl = host + '/admin/rest/nhan-vien?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        } else if ($scope.status == 2) {
            apiUrl += '&chucVu=' + 1;
        } else if ($scope.status == 3) {
            apiUrl += '&chucVu=' + 2;
        }

        console.log(apiUrl);
        $scope.isLoading = true;
        setTimeout(function () {
            $http.get(apiUrl)
                .then(function (response) {
                    $scope.nhanViens = response.data.content;
                    $scope.numOfPages = response.data.totalPages;
                    $scope.isLoading = false;
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ liệu thất bại");
                    // window.location.href = feHost + '/trang-chu';
                });
        }, 400);
    }

    $scope.detailNhanVien = function (val) {
        var id = val;
        console.log(id);
        if (!isNaN(id)) {
            $scope.nhanVienDetail = $scope.nhanViens.find(function (nhanVien) {
                return nhanVien.id == id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

    $scope.inBaoCao = function () {
        $http.get(host + '/admin/rest/nhan-vien/send-report-daily').then(function (response) {
            if (response.status == 200) {
                toastr["success"]("In báo cáo thành công , vui lòng kiểm tra email");
            }
        }).catch(function (error) {
            toastr["error"]("In không thành công , vui lòng liên hệ 0396189965");
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

    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $http.get(host + '/admin/rest/nhan-vien/' + id)
        .then(function (response) {
            var ngaySinh = new Date(response.data.ngaySinh);
            var nhanVienBefore = convertObjectToString(response.data);
            nhanVienBefore.ngaySinh = ngaySinh;
            $scope.nhanVien = nhanVienBefore;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateNhanVien = function () {
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
            role: 1
        };
        console.log(nhanVienUpdate);
        $http.put(host + '/admin/rest/nhan-vien/' + id, nhanVienUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Tài khoản đang được sử dụng");
        })
    };
    $scope.backToNhanVienList = function () {
        $window.location.href = '/admin/nhan-vien#/list';
    }
});

