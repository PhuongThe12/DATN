var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/danhsach", {
            templateUrl: '/pages/admin/mausac/views/list.html',
            controller: 'mauSacListController'
        }).when("/detail/:id", {
            templateUrl: '/pages/admin/mausac/views/detail.html',
            controller: 'detailMauSacController'
        }).when("/update/:id", {
            templateUrl: '/pages/admin/mausac/views/update.html',
            controller: 'updateMauSacController'
        }).when("/add", {
            templateUrl: '/pages/admin/mausac/views/add.html',
            controller: 'addMauSacController'
        })
        .otherwise({ redirectTo: '/danhsach' });
});

app.controller("addMauSacController", function ($scope, $http, $location) {
    $scope.mauSac = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addMauSac = function () {
        if ($scope.mauSacForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/mau-sac', $scope.mauSac)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/danhsach");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    console.log($scope.mauSacForm);
                    $scope.mauSacForm.ten.$dirty = false;
                    $scope.mauSacForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailMauSacController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/mau-sac/' + id)
        .then(function (response) {
            $scope.mauSac = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/danhsach");
    });

});

app.controller("mauSacListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 1,
        $scope.maxSize = 5;

    function getData(currentPage) {
        $http.get(host + '/admin/rest/mau-sac?page=' + currentPage)
            .then(function (response) {
                $scope.mauSacs = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateMauSacController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/mau-sac/' + id)
        .then(function (response) {
            $scope.mauSac = response.data;
        }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/danhsach");
        });

    $scope.updateMauSac = function () {
        if ($scope.mauSacForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/mau-sac/' + id, $scope.mauSac)
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
                    $scope.mauSacForm.ten.$dirty = false;
                    $scope.mauSacForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            })
    };
});
