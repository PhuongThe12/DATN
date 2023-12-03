var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/user/donmua/views/donmua.html',
            controller: 'DonMuaKhachHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/user/donmua/views/detail.html',
        controller: 'detailDonMuaController'
    })
        .otherwise({redirectTo: '/list'});
});

    app.controller("DonMuaKhachHangListController", function ($scope, $http, $window, $location) {
        $scope.curPage = 1,
            $scope.itemsPerPage = 5,
            $scope.maxSize = 5;

        $scope.changeRadio = function (status) {
            $scope.status = status;
            getData(1);
        }

        function getData(currentPage) {
            let apiUrl = host + '/rest/admin/hoa-don-chi-tiet/get-all?page='+ currentPage;

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
            }
            $http.get(apiUrl)
                .then(function (response) {
                    $scope.donMuas = response.data.content;
                    $scope.numOfPages = response.data.totalPages;
                    console.log($scope.donMuas)
                    console.log("AAAAAAAAAA")
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ liệu thất bại");
                });
        }

        $scope.$watch('curPage + numPerPage', function () {
            getData($scope.curPage);
        });
    });

app.controller("detailDonMuaController", function ($scope, $http, $window, $location,$routeParams) {
    const id = $routeParams.id;
    console.log("aaaaaaaaaaaaaaaa")
    $http.get(host + '/rest/admin/hoa-don/' + id)
        .then(function (response) {
            $scope.hoaDon = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});



