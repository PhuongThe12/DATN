var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/lotgiay/views/list.html',
            controller: 'lotGiayListController'
        }).when("/update/:id", {
        templateUrl: '/pages/admin/lotgiay/views/update.html',
        controller: 'updateLotGiayController'
    }).when("/add", {
        templateUrl: '/pages/admin/lotgiay/views/add.html',
        controller: 'addLotGiayController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addLotGiayController", function ($scope, $http, $location) {
    $scope.lotGiay = {};
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
                $scope.addLotGiay();
            }

        });
    }

    $scope.addLotGiay = function () {
        $scope.isLoading = true;
        if ($scope.lotGiayForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.post(host + '/rest/admin/lot-giay', $scope.lotGiay)
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
                    $scope.lotGiayForm.ten.$dirty = false;
                    $scope.lotGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("lotGiayListController", function ($scope, $http, $window, $location) {

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
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/lot-giay?page=' + currentPage;
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
                $scope.lotGiays = response.data.content;
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

    $scope.detailLotGiay = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.lotGiayDetail = $scope.lotGiays.find(function(lotGiay) {
                return lotGiay.id === id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateLotGiayController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/rest/admin/lot-giay/' + id)
        .then(function (response) {
            $scope.lotGiay = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.comfirmAdd = function () {
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
                $scope.updateLotGiay();
            }

        });
    }

    $scope.updateLotGiay = function () {
        $scope.isLoading = true;
        if ($scope.lotGiayForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.put(host + '/rest/admin/lot-giay/' + id, $scope.lotGiay)
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
                $scope.lotGiayForm.ten.$dirty = false;
                $scope.lotGiayForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
