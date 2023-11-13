var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/banhang/views/home.html',
            controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});

// app.controller("indexController", function ($scope, $http, $location, $cookies) {
//     $http.get(host + '/admin/rest/nhan-vien/check-logged')
//         .then(function (response) {
//             if (response.status == 200) {
//                 $scope.nhanVienLogged = response.data;
//                 if ($scope.nhanVienLogged.chucVu == 2) {
//                     $scope.chuCuaHangLogged = true;
//                 } else if ($scope.nhanVienLogged.chucVu == 1) {
//                     $scope.chuCuaHangLogged = false;
//                 }
//             }
//         }).catch(function (error) {
//         toastr["error"]("Không tìm thấy người dùng , vui lòng đăng nhập lại !");
//     });
// });

app.controller("homeController", function ($scope, $http, $location, $cookies) {
    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 5;

    let giaySearch = {};
    $scope.listGiaySelected = [];

    $scope.search = function () {
        getData(1);
    };

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {
        let apiUrl = host + '/admin/rest/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }

        if ($scope.status === 0) {
            giaySearch.trangThai = 0;
        } else if ($scope.status === 1) {
            giaySearch.trangThai = 1;
        } else {
            giaySearch.trangThai = null;
        }


        if (!isNaN($scope.giaNhapSearch)) {
            giaySearch.giaNhap = $scope.giaNhapSearch;
        }

        if (!isNaN($scope.giaBanSearch)) {
            giaySearch.giaBan = $scope.giaBanSearch;
        }

        if ($scope.selectedThuongHieu) {
            giaySearch.thuongHieuIds = $scope.selectedThuongHieu.filter(
                thuongHieu => thuongHieu.status === 'active'
            ).map(th => th.id);
        }

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    getData(1);

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });


    // Thêm giày vào giỏ hàng


    $scope.addOrder = function (id) {

        $scope.checkExits = $scope.listGiaySelected.find(function (giay) {
            return giay.id == id;
        });

        if ($scope.checkExits === undefined) {
            $scope.giaySeletect = $scope.giays.find(function (giay) {
                return giay.id == id;
            });
            $scope.giaySeletect.soLuong = 1;
            console.log($scope.giaySeletect);
            $scope.listGiaySelected.push($scope.giaySeletect);

            $scope.totalPrice += $scope.giaySeletect.gia;
        } else {
            $scope.checkExits.soLuong++;
            $scope.totalPrice += $scope.checkExits.gia;
        }

    }

});