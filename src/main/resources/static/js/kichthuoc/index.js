var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/kichthuoc/views/list.html',
            controller: 'kichThuocListController'
        }).when("/detail/:id", {
            templateUrl: '/pages/admin/kichthuoc/views/detail.html',
            controller: 'detailKichThuocController'
        }).when("/update/:id", {
            templateUrl: '/pages/admin/kichthuoc/views/update.html',
            controller: 'updateKichThuocController'
        }).when("/add", {
            templateUrl: '/pages/admin/kichthuoc/views/add.html',
            controller: 'addKichThuocController'
        })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addKichThuocController", function ($scope, $http, $location) {
    $scope.kichThuoc = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addKichThuoc = function () {
        if ($scope.kichThuocForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/kich-thuoc', $scope.kichThuoc)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.kichThuocForm.ten.$dirty = false;
                    $scope.kichThuocForm.moTa.$dirty = false;
                    $scope.kichThuocForm.chieuDai.$dirty = false;
                    $scope.kichThuocForm.chieuRong.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailKichThuocController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/kich-thuoc/' + id)
        .then(function (response) {
            $scope.kichThuoc = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("kichThuocListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/kich-thuoc?page=' + currentPage;
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
                $scope.kichThuocs = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.detailKichThuoc = function (val) {
        const id = val;

        if (!isNaN(id)) {
            $scope.kichThuocDetail = $scope.kichThuocs.find(function(kichThuoc) {
                return kichThuoc.id == id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateKichThuocController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/kich-thuoc/' + id)
        .then(function (response) {
            $scope.kichThuoc = response.data;
        }).catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
            $location.path("/list");
        });

    $scope.updateKichThuoc = function () {
        if ($scope.kichThuocForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/kich-thuoc/' + id, $scope.kichThuoc)
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
                    $scope.kichThuocForm.ten.$dirty = false;
                    $scope.kichThuocForm.moTa.$dirty = false;
                    $scope.kichThuocForm.chieuDai.$dirty = false;
                    $scope.kichThuocForm.chieuRong.$dirty = false;
                    $scope.errors = error.data;
                }
            })
    };
});
