var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/cogiay/views/list.html',
            controller: 'coGiayListController'
        })
        .when("/update/:id", {
            templateUrl: '/pages/admin/cogiay/views/update.html',
            controller: 'updateCoGiayController'
        })
        .when("/add", {
            templateUrl: '/pages/admin/cogiay/views/add.html',
            controller: 'addCoGiayController'
        })
        .otherwise({redirectTo: '/list'});
});

app.controller("addCoGiayController", function ($scope, $http, $location) {
    $scope.coGiay = {};
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
                $scope.addCoGiay();
            }

        });
    }

    $scope.addCoGiay = function () {
        $scope.isLoading = true;
        if ($scope.coGiayForm.$invalid) {
            $scope.isLoading = false;
            return;
        }

        $http.post(host + '/rest/admin/co-giay', $scope.coGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.coGiayForm.ten.$dirty = false;
                    $scope.coGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                    $scope.isLoading = false;
                }
            });
    }

});

app.controller("coGiayListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/rest/admin/co-giay?page=' + currentPage;
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
                $scope.coGiays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.resetSearch = function () {
        searchText = null;
        $scope.searchText = '';
        $scope.status = -1;
        getData(1);
    }

    $scope.detailCoGiay = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.coGiayDetail = $scope.coGiays.find(function (coGiay) {
                return coGiay.id === id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateCoGiayController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.isLoading = true;
    $http.get(host + '/rest/admin/co-giay/' + id)
        .then(function (response) {
            $scope.coGiay = response.data;
            $scope.isLoading = false;
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
                $scope.updateCoGiay();
            }
        });
    }
    $scope.updateCoGiay = function () {
        $scope.isLoading = true;
        if ($scope.coGiayForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.put(host + '/rest/admin/co-giay/' + id, $scope.coGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.coGiayForm.ten.$dirty = false;
                $scope.coGiayForm.moTa.$dirty = false;
                $scope.errors = error.data;
                $scope.isLoading = false;
            }
        })
    };
});
