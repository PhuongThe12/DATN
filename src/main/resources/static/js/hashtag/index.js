var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/hashtag/views/list.html',
            controller: 'hashTagListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/hashtag/views/detail.html',
        controller: 'detailHashTagController'
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

    $scope.addHashTag = function () {
        if ($scope.hashTagForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/hash-tag', $scope.hashTag)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.hashTagForm.ten.$dirty = false;
                    $scope.hashTagForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailHashTagController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/hash-tag/' + id)
        .then(function (response) {
            $scope.hashTag = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

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
        let apiUrl = host + '/admin/rest/hash-tag?page=' + currentPage;
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
    $http.get(host + '/admin/rest/hash-tag/' + id)
        .then(function (response) {
            $scope.hashTag = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateHashTag = function () {
        if ($scope.hashTagForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/hash-tag/' + id, $scope.hashTag)
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
                $scope.hashTagForm.ten.$dirty = false;
                $scope.hashTagForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
