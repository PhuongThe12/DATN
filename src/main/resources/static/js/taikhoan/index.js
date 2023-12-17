var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/taikhoan/views/list.html',
            controller: 'taiKhoanListController'
        }).when("/add", {
        templateUrl: '/pages/admin/taikhoan/views/add.html',
        controller: 'addTaiKhoanController'
    })
        .otherwise({redirectTo: '/list'});
});


app.controller("addTaiKhoanController", function ($scope, $http, $location) {
    $scope.taiKhoan = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.addTaiKhoan = function () {
        if ($scope.taiKhoanForm.$invalid) {
            return;
        }
        $http.post(host + '/rest/admin/tai-khoan', $scope.taiKhoan)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Đăng kí thành công");
                }
                $location.path("/login");
            })
            .catch(function (error) {
                // toastr["error"]("Thêm thất bại");
                console.log(error)
                if (error.status === 400) {

                    $scope.taiKhoanForm.tenDangNhap.$dirty = false;
                    $scope.taiKhoanForm.matKhau.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("taiKhoanListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        // $scope.itemsPerPage = 3,
        // $scope.maxSize = 5;

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }
    function getData(currentPage) {
        let apiUrl = host + '/rest/admin/tai-khoan?page=' + currentPage;

        console.log(apiUrl);

        $http.get(apiUrl)
            .then(function (response) {
                $scope.taiKhoans = response.data.content;
                $scope.khachHangs = response.data.content;
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

//    login
    $scope.userLogin = {
        tenDangNhap: '',
        matKhau: ''
    };
    $scope.loginUser = function() {
        // Make sure to replace the URL with the correct endpoint
        var apiUrl = 'http://localhost:8080/rest/admin/tai-khoan/login';
        $http.post(apiUrl, $scope.userLogin)
            .then(function(response) {
                // Successful login
                console.log(response.data);
                $window.location.href = '/khach-hang/dia-chi-nhan-hang';
            })
            .catch(function(error) {
                // Error handling
                if (error.status === 404) {
                    console.log('User not found');
                    // Handle the case where the user is not found
                } else {
                    console.error('An error occurred:', error);
                    // Handle other errors
                }
            });
    };


});




