var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.controller("loginController", function ($scope, $http, $location, $routeParams) {


    $scope.loginUser = function () {
        $http.get(host + '/tai-khoan/detail', $scope.userLogin)
            .then(function (response) {
                console.log(response.data);
            }).catch(function (error) {
            console.log(error)
            $location.path("/home");
        });
    }

});

