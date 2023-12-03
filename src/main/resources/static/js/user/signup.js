var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);

app.controller("signUpController", function ($rootScope, $scope, $http, $location, $window) {

    $scope.signUp = function () {
        $http.post(host + '/api/authentication/signup',$scope.userSignup)
            .then(function (response) {
                if (response.status == 200) {
                    console.log($scope.userSignup)
                    $window.location.href = '/login';
                }
            }).catch(function (error) {
            console.log(error)
        });
    }
});