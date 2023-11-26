var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.controller("loginController", function ($scope, $http, $location, $window, $cookies) {

    $scope.loginUser = function () {
        console.log($scope.userLogin);
        $http.post(host + '/api/authentication/singin',$scope.userLogin)
            .then(function (response) {
                console.log("respopnse" +response);
                if (response.status === 200) {
                    alert("Success!")
                    $window.location.href = '/admin/de-giay';
                }
                console.log("Error!")
            }).catch(function (error) {
            console.log(error)
        });
    }
});

