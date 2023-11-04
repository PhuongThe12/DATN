var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/chatlieu/views/list.html',
            controller: 'chatLieuListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/chatlieu/views/detail.html',
        controller: 'detailChatLieuController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/chatlieu/views/update.html',
        controller: 'updateChatLieuController'
    }).when("/add", {
        templateUrl: '/pages/admin/chatlieu/views/add.html',
        controller: 'addChatLieuController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addChatLieuController", function ($scope, $http, $location) {
    $scope.chatLieu = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.addChatLieu = function () {
        if ($scope.chatLieuForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/chat-lieu', $scope.chatLieu)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.chatLieuForm.ten.$dirty = false;
                    $scope.chatLieuForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("detailChatLieuController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/chat-lieu/' + id)
        .then(function (response) {
            $scope.chatLieu = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("chatLieuListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/chat-lieu?page=' + currentPage;
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
                $scope.chatLieus = response.data.content;
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

    $scope.detailChatLieu = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.chatLieuDetail = $scope.chatLieus.find(function(chatLieu) {
                return chatLieu.id === id;
            });
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateChatLieuController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/admin/rest/chat-lieu/' + id)
        .then(function (response) {
            $scope.chatLieu = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateChatLieu = function () {
        if ($scope.chatLieuForm.$invalid) {
            return;
        }
        $http.put(host + '/admin/rest/chat-lieu/' + id, $scope.chatLieu)
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
                $scope.chatLieuForm.ten.$dirty = false;
                $scope.chatLieuForm.moTa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
