var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/daygiay/views/list.html',
            controller: 'dayGiayListController'
        }).when("/update/:id", {
        templateUrl: '/pages/admin/daygiay/views/update.html',
        controller: 'updateDayGiayController'
    }).when("/add", {
        templateUrl: '/pages/admin/daygiay/views/add.html',
        controller: 'addDayGiayController'
    })
        .otherwise({redirectTo: '/list'});
});

app.controller("addDayGiayController", function ($scope, $http, $location) {
    $scope.dayGiay = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

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
                $scope.addDayGiay();
            }

        });
    }

    $scope.addDayGiay = function () {
        $scope.isLoading = true;
        if ($scope.dayGiayForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.post(host + '/rest/admin/day-giay', $scope.dayGiay)
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
                    $scope.dayGiayForm.ten.$dirty = false;
                    $scope.dayGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});

app.controller("dayGiayListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/rest/admin/day-giay?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.dayGiays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.resetSearch = function () {
        searchText = null;
        $scope.searchText = '';
        $scope.status = -1;
        getData(1);
    }

    $scope.detailDayGiay = function (val) {
        const id = val;

        if (!isNaN(id)) {
            $scope.dayGiayDetail = $scope.dayGiays.find(function (dayGiay) {
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
    $http.get(host + '/rest/admin/day-giay/' + id)
        .then(function (response) {
            $scope.dayGiay = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

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
                $scope.updateDayGiay();
            }

        });
    }

    $scope.updateDayGiay = function () {
        $scope.isLoading = true;
        if ($scope.dayGiayForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.put(host + '/rest/admin/day-giay/' + id, $scope.dayGiay)
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
                $scope.dayGiayForm.ten.$dirty = false;
                $scope.dayGiayForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
