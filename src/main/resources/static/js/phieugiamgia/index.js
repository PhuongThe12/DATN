var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/phieugiamgia/views/list.html',
            controller: 'phieuGiamGiaListController'
        }).when("/update/:id", {
        templateUrl: '/pages/admin/phieugiamgia/views/update.html',
        controller: 'updatePhieuGiamGiaController'
    }).when("/add", {
        templateUrl: '/pages/admin/phieugiamgia/views/add.html',
        controller: 'addPhieuGiamGiaController'
    })
        .otherwise({redirectTo: '/list'});
});

app.filter('longToDate', function () {
    return function (input) {
        if (!input) return "";

        var date = new Date(input);
        return date.toLocaleDateString();
    };
});

app.controller('listHangKhachHangController', function ($scope, $http) {
    $scope.options = [];
    $http.get(host + '/rest/admin/hang-khach-hang/get-all')
        .then(function (response) {
            $scope.options = response.data;
        })
        .catch(function (error) {
            console.error('Error fetching data:', error);
        });
});

app.controller("addPhieuGiamGiaController", function ($scope, $http, $location) {
    // $scope.phieuGiamGia = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.options = [];
    $scope.getOptions = function() {
        $http.get(host + '/rest/admin/hang-khach-hang/get-all')
            .then(function (response) {
                $scope.options = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching data:', error);
            });
    };
    $scope.getOptions();

    $scope.comfirmAdd = function () {
        Swal.fire({
            text: "Xác nhận thêm?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.addPhieuGiamGia();
            }

        });
    }

    $scope.addPhieuGiamGia = function () {
        $scope.isLoading = true;
        if ($scope.phieuGiamGiaForm.$invalid) {
            $scope.isLoading = false;
            return;
        }

        let user = JSON.parse(localStorage.getItem("currentUser"));
        var ngayBaDauDate = new Date($scope.phieuGiamGia.ngayBatDau);
        var ngayKetThucDate = new Date($scope.phieuGiamGia.ngayKetThuc);

        $scope.phieuGiamGia.ngayBatDau = ngayBaDauDate.getTime();
        $scope.phieuGiamGia.ngayKetThuc = ngayKetThucDate.getTime();
        $scope.phieuGiamGia.nhanVienId = user.idKhachHang;
        $http.post(host + '/rest/admin/phieu-giam-gia/add', $scope.phieuGiamGia)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                $scope.isLoading = false;
                if (error.status === 400) {
                    $scope.phieuGiamGiaForm.ten.$dirty = false;
                    $scope.phieuGiamGiaForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }
});


app.controller("phieuGiamGiaListController", function ($scope, $http) {

    $scope.curPage = 1,
    $scope.itemsPerPage = 3,
    $scope.maxSize = 5;

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    $scope.search = function () {
        getData(1, $scope.findPhieu);
    };

    function getData(currentPage) {
        $scope.isLoading = true;
        $scope.findPhieu = {
            maGiamGia: $scope.maGiamGia,
            hangKhachHang : $scope.hangKhachHang,
            trangThai: $scope.status,
            ngayBatDau: $scope.ngayBatDau == null ? null : $scope.ngayBatDau.getTime(),
            ngayKetThuc: $scope.ngayKetThuc == null ? null : $scope.ngayKetThuc.getTime(),
            currentPage: currentPage
        }

        let apiUrl = host + '/rest/admin/phieu-giam-gia/search';
        console.log($scope.findPhieu.hangKhachHang)
        if ($scope.status == 0) {
            $scope.findPhieu.trangThai = 0;
        } else if ($scope.status == 1) {
            $scope.findPhieu.trangThai = 1;
        } else {
            $scope.findPhieu.trangThai = "";
        }

        $http.post(apiUrl, $scope.findPhieu)
            .then(function (response) {
                $scope.lstPhieuGiamGia = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.resetInputSearch = function () {
        maGiamGia = null;
        $scope.maGiamGia = '';
        $scope.ngayBatDau = null;
        $scope.ngayKetThuc = null;
        $scope.status = -1;
        getData(1);
    }

    $scope.detailPhieuGiamGia = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.pggDetail = $scope.lstPhieuGiamGia.find(function (pgg) {
                return pgg.id === id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.options = [];
    $scope.getOptions = function() {
        $http.get(host + '/rest/admin/hang-khach-hang/get-all')
            .then(function (response) {
                $scope.options = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching data:', error);
            });
    };
    $scope.getOptions();
    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updatePhieuGiamGiaController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    let user = JSON.parse(localStorage.getItem("currentUser"));

    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/rest/admin/phieu-giam-gia/' + id)
        .then(function (response) {
            $scope.phieuGiamGia = response.data;
            $scope.phieuGiamGia.hangKhachHang = $scope.phieuGiamGia.hangKhachHang;
            $scope.phieuGiamGia.ngayBatDau = new Date($scope.phieuGiamGia.ngayBatDau);
            $scope.phieuGiamGia.ngayKetThuc = new Date($scope.phieuGiamGia.ngayKetThuc)
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.options = [];
    $scope.getOptions = function() {
        $http.get(host + '/rest/admin/hang-khach-hang/get-all')
            .then(function (response) {
                $scope.options = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching data:', error);
            });
    };
    $scope.getOptions();

    $scope.comfirmUpdate = function () {
        Swal.fire({
            text: "Xác nhận cập nhật?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.updatePhieuGiamGia();
            }

        });
    }

    $scope.updatePhieuGiamGia = function () {
        $scope.isLoading = true;
        if ($scope.phieuGiamGiaForm.$invalid) {
            $scope.isLoading = false;
            return;
        }

        var ngayBaDauDate = new Date($scope.phieuGiamGia.ngayBatDau);
        var ngayKetThucDate = new Date($scope.phieuGiamGia.ngayKetThuc);

        $scope.phieuGiamGia.ngayBatDau = ngayBaDauDate.getTime();
        $scope.phieuGiamGia.ngayKetThuc = ngayKetThucDate.getTime();
        $scope.phieuGiamGia.nhanVienId = user.idKhachHang;

        $http.put(host + '/rest/admin/phieu-giam-gia/' + id, $scope.phieuGiamGia)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            toastr["error"]("Cập nhật thất bại");
            $scope.isLoading = false;
            if (error.status === 400) {
                $scope.phieuGiamGiaForm.ten.$dirty = false;
                $scope.phieuGiamGiaForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});

