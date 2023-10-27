var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/daygiay/views/list.html',
            controller: 'dayGiayListController'
        }).when("/detail/:id", {
            templateUrl: '/pages/admin/daygiay/views/detail.html',
            controller: 'detailDayGiayController'
        }).when("/update/:id", {
            templateUrl: '/pages/admin/daygiay/views/update.html',
            controller: 'updateDayGiayController'
        }).when("/add", {
            templateUrl: '/pages/admin/daygiay/views/add.html',
            controller: 'addDayGiayController'
        })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addDayGiayController", function ($scope, $http, $location) {
    $scope.dayGiay = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addDayGiay = function () {
        if ($scope.dayGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/day-giay', $scope.dayGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.dayGiayForm.ten.$dirty = false;
                    $scope.dayGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailDayGiayController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/day-giay/' + id)
        .then(function (response) {
            $scope.dayGiay = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("dayGiayListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/day-giay?page=' + currentPage;
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
                $scope.dayGiays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.detailDayGiay = function (val) {
        const id = val;

        if (!isNaN(id)) {
            $scope.dayGiayDetail = $scope.dayGiays.find(function(dayGiay) {
                return dayGiay.id == id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateDayGiayController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/day-giay/' + id)
        .then(function (response) {
            $scope.dayGiay = response.data;
        }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });

    $scope.updateDayGiay = function () {
        if ($scope.dayGiayForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/day-giay/' + id, $scope.dayGiay)
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
                    $scope.dayGiayForm.ten.$dirty = false;
                    $scope.dayGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            })
    };
});
