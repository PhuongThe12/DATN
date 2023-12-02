var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/danhsach", {
            templateUrl: '/pages/admin/hangkhachhang/views/list.html',
            controller: 'hangKhachHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/hangkhachhang/views/detail.html',
        controller: 'detailHangKhachHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/hangkhachhang/views/update.html',
        controller: 'updateHangKhachHangController'
    }).when("/add", {
        templateUrl: '/pages/admin/hangkhachhang/views/add.html',
        controller: 'addHangKhachHangController'
    })
        .otherwise({ redirectTo: '/danhsach' });
});

app.controller("addHangKhachHangController", function ($scope, $http, $location) {
    $scope.hangKhachHang = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addHangKhachHang = function () {
        if ($scope.hangKhachHangForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/hang-khach-hang', $scope.hangKhachHang)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/danhsach");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    console.log($scope.hangKhachHangForm);
                    $scope.hangKhachHangForm.tenHang.$dirty = false;
                    $scope.hangKhachHangForm.dieuKien.$dirty = false;
                    $scope.hangKhachHangForm.uuDai.$dirty = false;
                    $scope.hangKhachHangForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});




app.controller("hangKhachHangListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/hang-khach-hang?page=' + currentPage;
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

        $http.get(apiUrl)
            .then(function (response) {
                $scope.hangKhachHangs = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    //modal

    $scope.detailHangKhachHang = function (val) {
        var id = val;
        $http.get(host + '/admin/rest/hang-khach-hang/' + id)
            .then(function (response) {
                $scope.HangKhachHangDetail = response.data;
                const button = document.querySelector('[data-bs-target="#showHangKhachHang"]');
                if (button) {
                    button.click();
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateHangKhachHangController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/hang-khach-hang/' + id)
        .then(function (response) {
            $scope.hangKhachHang = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/danhsach");
    });

    $scope.updateHangKhachHang = function () {
        if ($scope.hangKhachHangForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/hang-khach-hang/' + id, $scope.hangKhachHang)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/danhsach");
            }).catch(function (error) {
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.hangKhachHangForm.tenHang.$dirty = false;
                $scope.hangKhachHangForm.dieuKien.$dirty = false;
                $scope.hangKhachHangForm.uuDai.$dirty = false;
                $scope.hangKhachHangForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
