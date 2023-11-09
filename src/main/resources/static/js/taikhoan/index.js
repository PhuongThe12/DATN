var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
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
        $http.post(host + '/admin/rest/tai-khoan', $scope.taiKhoan)
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
        let apiUrl = host + '/admin/rest/tai-khoan?page=' + currentPage;

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

});



