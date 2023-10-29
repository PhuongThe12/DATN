var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.controller("loginController", function ($scope, $http, $location, $window) {


    $scope.loginUser = function () {
        console.log($scope.userLogin);
        $http.post(host + '/tai-khoan/detail', $scope.userLogin)
            .then(function (response) {
                if(response.data.role == 1){
                    console.log(response.data);
                    $http.get(host + '/admin/rest/nhan-vien/find-tai-khoan/' + response.data.id)
                        .then(function (response) {
                            console.log(response.data);
                            alert(response.data.chucVu);
                            $window.location.href = '/admin/nhan-vien#/list';
                            $scope.nhanVienLogged = response.data;

                        }).catch(function (error) {
                        toastr["error"]("Lấy dữ liệu thất bại");
                    });

                }else{
                    $window.location.href = '/home';
                }

            }).catch(function (error) {
            console.log(error)
        });
    }

});

