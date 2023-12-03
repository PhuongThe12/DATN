var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.controller("loginController", function ($scope, $http, $location, $window, $cookies) {

    $scope.loginUser = function () {
        alert($scope.userLogin);
        console.log($scope.userLogin);
        $http.post(host + '/api/authentication/login-basic' + $scope.userLogin)
            .then(function (response) {
                if (response.data.role == 1) {
                    $http.get(host + '/rest/admin/nhan-vien/find-tai-khoan/' + response.data.id)
                        .then(function (response) {
                            $window.location.href = '/admin/tong-quan#/home';
                        }).catch(function (error) {
                        toastr["error"]("Đăng nhập thất bại");
                    });

                } else {
                    $window.location.href = '/home';
                }
            }).catch(function (error) {
            console.log(error)
        });
    }

});

