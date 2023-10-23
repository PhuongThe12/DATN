var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/thuonghieu/views/list.html',
            controller: 'thuongHieuListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/thuonghieu/views/detail.html',
        controller: 'detailThuongHieuController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/thuonghieu/views/update.html',
        controller: 'updateThuongHieuController'
    }).when("/add", {
        templateUrl: '/pages/admin/thuonghieu/views/add.html',
        controller: 'addThuongHieuController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addThuongHieuController", function ($scope, $http, $location) {
    $scope.thuongHieu = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addThuongHieu = function () {
        if ($scope.thuongHieuForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/thuong-hieu', $scope.thuongHieu)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.thuongHieuForm.ten.$dirty = false;
                    $scope.thuongHieuForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailThuongHieuController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/thuong-hieu/' + id)
        .then(function (response) {
            $scope.thuongHieu = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("thuongHieuListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 5,
        $scope.maxSize = 5;

    let searchText;

    $scope.search = function () {
        if(!$scope.searchText) {
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
        let apiUrl = host + '/admin/rest/thuong-hieu?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.thuongHieus = response.data.content;
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

app.controller("updateThuongHieuController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/thuong-hieu/' + id)
        .then(function (response) {
            $scope.thuongHieu = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateThuongHieu = function () {
        if ($scope.thuongHieuForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/thuong-hieu/' + id, $scope.thuongHieu)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.thuongHieuForm.ten.$dirty = false;
                $scope.thuongHieuForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
