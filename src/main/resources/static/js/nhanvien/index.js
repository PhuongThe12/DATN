var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/nhanvien/views/list.html',
            controller: 'nhanVienListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/nhanvien/views/detail.html',
        controller: 'detailNhanVienController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/nhanvien/views/update.html',
        controller: 'updateNhanVienController'
    }).when("/add", {
        templateUrl: '/pages/admin/nhanvien/views/add.html',
        controller: 'addNhanVienController'
    })
        .otherwise({redirectTo: '/danhsach'});
});


app.controller("addNhanVienController", function ($scope, $http, $location) {
    // $scope.nhanVien = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.addNhanVien = function () {
        if ($scope.nhanVienForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/nhan-vien', $scope.nhanVien)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/danhsach");
            })
            .catch(function (error) {
                // toastr["error"]("Thêm thất bại");
                console.log(error)
                if (error.status === 400) {
                    $scope.nhanVienForm.hoTen = false;
                    // $scope.nhanVienForm.ghiChu.$dirty = false;
                    // $scope.nhanVienForm.soDienThoai.$dirty = false;
                    $scope.nhanVienForm.email.$dirty = false;
                    // $scope.nhanVienForm.tenDangNhap = error.data.tenDangNhap;
                    $scope.errors = error.data;
                }
            });
    }

});


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
        setTimeout(function() {
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
        if(!isNaN(id)){
            $scope.nhanVienDetail = $scope.nhanViens.find(function (nhanVien){
                return nhanVien.id == id;
            });
        }else{
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateNhanVienController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $http.get(host + '/admin/rest/nhan-vien/' + id)
        .then(function (response) {
            $scope.nhanVien = response.data;
            console.log(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/danhsach");
    });

    $scope.updateNhanVien = function () {
        if ($scope.nhanVienForm.$invalid) {
            return;
        }
        const nhanVienUpdate = {
            id: $scope.nhanVien.id,
            hoTen: $scope.nhanVien.id,
            gioiTinh : $scope.nhanVien.gioiTinh,
            idTaiKhoan : $scope.nhanVien.taiKhoan.id,
            tenDangNhap : $scope.nhanVien.taiKhoan.tenDangNhap,
            matKhau : $scope.nhanVien.taiKhoan.matKhau,
            soDienThoai : $scope.nhanVien.soDienThoai,
            email : $scope.nhanVien.email,
            trangThai : $scope.nhanVien.trangThai,
            chucVu : $scope.nhanVien.chucVu,
            ngaySinh : $scope.nhanVien.ngaySinh,
            xa : $scope.nhanVien.xa,
            huyen : $scope.nhanVien.huyen,
            tinh : $scope.nhanVien.tinh,
            ghiChu: $scope.nhanVien.ghiChu,
        };
        console.log($scope.nhanVien);
        $http.put(host + '/admin/rest/nhan-vien/' + id, nhanVienUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/danhsach");
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {

                $scope.nhanVienForm.hoTen = false;
                // $scope.nhanVienForm.ghiChu.$dirty = false;
                // $scope.nhanVienForm.soDienThoai.$dirty = false;
                $scope.nhanVienForm.email.$dirty = false;
                // $scope.nhanVienForm.tenDangNhap = error.data.tenDangNhap;
                $scope.errors = error.data;
            }
        })
    };
});

