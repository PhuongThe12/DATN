
var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);

app.controller("loginController", function ($rootScope, $scope, $http, $location, $window, $cookies) {

    $scope.currentUser ={
        idKhachHang:"",
        username :"",
        role : "",
        token :""
    };

    $scope.loginUser = function () {
        $http.post(host + '/api/authentication/singin',$scope.userLogin)
            .then(function (response) {
                if (response.status == 200) {
                    setTokenCookie(response.data.token, 1)
                    $scope.currentUser.username = response.data.userName
                    $scope.currentUser.token =response.data.token
                    $scope.currentUser.idKhachHang =response.data.id
                    $scope.currentUser.role =response.data.role
                    $rootScope.currentUser =$scope.currentUser;
                    $window.localStorage.setItem('currentUser', JSON.stringify($scope.currentUser));
                    if ($scope.currentUser.role === 'ROLE_USER') {
                        $window.location.href = '/home';
                    } else if ($scope.currentUser.role === 'ROLE_STAFF') {
                        $window.location.href = '/admin/ban-hang';
                    } else {
                        $window.location.href = '/admin/tong-quan';
                    }
                }
            }).catch(function (error) {
            console.log(error)
                toastr["error"]("Đăng nhập thất bại!");
                if (error.status === 400) {
                    $scope.loginForm.tenDangNhap.$dirty = false;
                    $scope.loginForm.matKhau.$dirty = false;
                    $scope.errors = error.data;
                    $scope.isLoading = false;
                }
        });
    }

    function setTokenCookie(token, expiryDays) {
        const d = new Date();
        d.setTime(d.getTime() + (expiryDays * 24 * 60 * 60 * 1000));
        const expires = "expires=" + d.toUTCString();
        document.cookie = `token=${token}; ${expires}; path=/`;
    }
});


