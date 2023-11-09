var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/diachinhanhang/views/list.html',
            controller: 'diaChiNhanHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/diachinhanhang/views/detail.html',
        controller: 'detailDiaChiNhanHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/diachinhanhang/views/update.html',
        controller: 'updateDiaChiNhanHangController'
    }).when("/add", {
        templateUrl: '/pages/admin/diachinhanhang/views/add.html',
        controller: 'addDiaChiNhanHangController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addDiaChiNhanHangController", function ($scope, $http, $location) {
    $scope.diaChiNhanHang = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addDiaChiNhanHang = function () {
        if ($scope.diaChiNhanHangForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/dia-chi-nhan-hang', $scope.diaChiNhanHang)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    console.log($scope.diaChiNhanHangForm);
                    $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                    $scope.diaChiNhanHangForm.diaChiNhan.$dirty = false;
                    $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});




app.controller("diaChiNhanHangListController", function ($scope, $http, $window, $location) {

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

    function getData(ahhoang) {
        let apiUrl = host + '/admin/rest/dia-chi-nhan-hang?page=' + ahhoang;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        console.log(apiUrl);

        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data.content;
                $scope.diaChiNhanHang = response.data;
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

app.controller("updateDiaChiNhanHangController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/dia-chi-nhan-hang/' + id)
        .then(function (response) {
            $scope.diaChiNhanHang = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateDiaChiNhanHang = function () {
        if ($scope.diaChiNhanHangForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/dia-chi-nhan-hang/' + id, $scope.diaChiNhanHang)
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
                $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                $scope.diaChiNhanHangForm.diaChiNhan.$dirty = false;
                $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
