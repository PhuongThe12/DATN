var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.controller("loginController", function ($scope, $http, $location, $window, $cookies) {

    $scope.loginUser = function () {
        console.log($scope.userLogin);
        $http.post(host + '/api/authentication/singin',$scope.userLogin)
            .then(function (response) {
                if (response.status == 200) {
                    setTokenCookie(response.data.token, 1)
                    $window.location.href = '/admin/ban-hang';
                }
            }).catch(function (error) {
            console.log(error)
        });
    }

    function setTokenCookie(token, expiryDays) {
        const d = new Date();
        d.setTime(d.getTime() + (expiryDays * 24 * 60 * 60 * 1000));
        const expires = "expires=" + d.toUTCString();
        document.cookie = `token=${token}; ${expires}; path=/`;
    }
});


