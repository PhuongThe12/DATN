var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/hashtag/views/list.html',
            controller: 'hashTagListController'
        }).when("/update/:id", {
        templateUrl: '/pages/admin/hashtag/views/update.html',
        controller: 'updateHashTagController'
    }).when("/add", {
        templateUrl: '/pages/admin/hashtag/views/add.html',
        controller: 'addHashTagController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addHashTagController", function ($scope, $http, $location) {
    $scope.hashTag = {};
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
                $scope.addHashTag();
            }

        });
    }

    $scope.addHashTag = function () {
        $scope.isLoading = true;
        if ($scope.hashTagForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.post(host + '/rest/admin/hash-tag', $scope.hashTag)
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
                    $scope.hashTagForm.ten.$dirty = false;
                    $scope.hashTagForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("hashTagListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/rest/admin/hash-tag?page=' + currentPage;
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
                $scope.hashTags = response.data.content;
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

    $scope.detailHashTag = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.hashTagDetail = $scope.hashTags.find(function(hashTag) {
                return hashTag.id === id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateHashTagController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/rest/admin/hash-tag/' + id)
        .then(function (response) {
            $scope.hashTag = response.data;
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
                $scope.updateHashTag();
            }

        });
    }
    $scope.updateHashTag = function () {
        $scope.isLoading = true;
        if ($scope.hashTagForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.put(host + '/rest/admin/hash-tag/' + id, $scope.hashTag)
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
                $scope.hashTagForm.ten.$dirty = false;
                $scope.hashTagForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
