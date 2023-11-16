var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/donhang/views/list.html',
            controller: 'donHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/donhang/views/detail.html',
        controller: 'detailDonHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/donhang/views/update.html',
        controller: ''
    }).when("/add", {
        templateUrl: '/pages/admin/donhang/views/add.html',
        controller: ''
    })
        .otherwise({redirectTo: '/list'});
});

app.controller("donHangListController", function ($scope, $http, $window, $location) {
    $scope.curPage = 1,
        $scope.itemsPerPage = 5,
        $scope.maxSize = 5;

    let searchText;

    $scope.search = function () {
        if(!$scope.searchText) {
            toastr["error"]("Vui lòng nhập tên muốn tìm kiếm");
            return;
        }
        searchText = $scope.searchText;
        getData(1, searchText);
    };

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {
        let apiUrl = host + '/admin/rest/hoa-don?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if($scope.status == 1) {
            apiUrl += '&status=' + 1;
        } else if($scope.status == 2) {
            apiUrl += '&status=' + 2;
        }else if($scope.status == 3){
            apiUrl += '&status=' + 3;
        }else if($scope.status == 4){
            apiUrl += '&status=' + 4;
        }else if($scope.status == 5){
            apiUrl += '&status=' + 5;
        }else{
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.hoaDons = response.data.content;
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

app.controller("detailDonHangController", function ($scope, $http, $window, $location,$routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/hoa-don/' + id)
        .then(function (response) {
            $scope.hoaDon = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });
});