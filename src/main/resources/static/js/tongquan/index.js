var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/tongquan/views/home.html',
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
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/home");
    });

    const ctx = document.getElementById('myChart');

    const data = {
        labels: ['Ngày 1', 'Ngày 2', 'Ngày 3', 'Ngày 4', 'Ngày 5', 'Ngày 6', 'Ngày 1', 'Ngày 2', 'Ngày 3', 'Ngày 4', 'Ngày 5', 'Ngày 6'],
        datasets: [{
            label: 'Số đơn hàng',
            data: [10, 20, 15, 30, 25, 40, 10, 20, 15, 30, 25, 40],
            backgroundColor: '#4669fa'
        }]
    };

    new Chart(ctx, {
        type: 'bar',
        data: data
    });

})