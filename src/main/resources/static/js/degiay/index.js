var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/degiay/views/list.html',
            controller: 'deGiayListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/degiay/views/detail.html',
        controller: 'detailDeGiayController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/degiay/views/update.html',
        controller: 'updateDeGiayController'
    }).when("/add", {
        templateUrl: '/pages/admin/degiay/views/add.html',
        controller: 'addDeGiayController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addDeGiayController", function ($scope, $http, $location) {
    $scope.deGiay = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addDeGiay = function () {
        if ($scope.deGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/de-giay', $scope.deGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.deGiayForm.ten.$dirty = false;
                    $scope.deGiayForm.moTa.$dirty = false;
                    $scope.deGiayForm.chatLieu.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailDeGiayController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/de-giay/' + id)
        .then(function (response) {
            $scope.deGiay = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("deGiayListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/de-giay?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if($scope.status === 0) {
            apiUrl += '&status=' + 0;
        } else if($scope.status === 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.deGiays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
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

    $scope.detailDeGiay = function (val) {
        const id = val;

        if (!isNaN(id)) {
            $scope.deGiayDetail = $scope.deGiays.find(function(deGiay) {
                return deGiay.id == id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateDeGiayController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/de-giay/' + id)
        .then(function (response) {
            $scope.deGiay = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateDeGiay = function () {
        if ($scope.deGiayForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/de-giay/' + id, $scope.deGiay)
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
                $scope.deGiayForm.ten.$dirty = false;
                $scope.deGiayForm.moTa.$dirty = false;
                $scope.deGiayForm.chatLieu.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
