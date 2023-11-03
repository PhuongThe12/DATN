var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/donhang/views/list.html',
            controller: 'donHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/donhang/views/detail.html',
        controller: 'detailDonHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/donhang/views/update.html',
        controller: ''
    }).when("/add", {
        templateUrl: '/pages/admin/donhang/views/add.html',
        controller: ''
    })
        .otherwise({redirectTo: '/list'});
});

app.controller("indexController", function ($scope, $http, $location, $cookies) {
    $http.get(host + '/admin/rest/nhan-vien/check-logged')
        .then(function (response) {
            if(response.status == 200){
                $scope.nhanVienLogged = response.data;
                if ($scope.nhanVienLogged.chucVu == 2) {
                    $scope.chuCuaHangLogged = true;
                } else if ($scope.nhanVienLogged.chucVu == 1) {
                    $scope.chuCuaHangLogged = false;
                }
            }
        }).catch(function (error) {
        toastr["error"]("Không tìm thấy người dùng , vui lòng đăng nhập lại !");
    });
});
app.controller("donHangListController", function ($scope, $http, $window, $location) {
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
        let apiUrl = host + '/admin/rest/hoa-don?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if($scope.status == 1) {
            apiUrl += '&status=' + 1;
        } else if($scope.status == 2) {
            apiUrl += '&status=' + 2;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.hoaDons = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });
});

app.controller("detailDonHangController", function ($scope, $http, $window, $location,$routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/hoa-don/' + id)
        .then(function (response) {
            $scope.donHang = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });
});