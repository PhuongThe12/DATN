var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/banhang/views/home.html',
            controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});

app.controller("indexController", function ($scope, $http, $location, $cookies) {
    $http.get(host + '/admin/rest/nhan-vien/check-logged')
        .then(function (response) {
            if (response.status == 200) {
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

app.controller("homeController", function ($scope, $http, $location, $cookies) {
    $http.get(host + '/admin/rest/nhan-vien/logger')
        .then(function (response) {
            $scope.listHanhDong = response.data;
            console.log(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/danhsach");
    });
})