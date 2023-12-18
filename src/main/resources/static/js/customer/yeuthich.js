var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/yeu-thich", {
            templateUrl: '/pages/user/views/yeu-thich.html',
            controller: 'danhSachYeuThichController'
})
        .otherwise({redirectTo: '/yeu-thich'});
});


app.controller("danhSachYeuThichController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 3,
        $scope.maxSize = 5;
    let searchText;

    $scope.search = function () {
        if (!$scope.searchText) {
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
        let apiUrl = host + '/rest/khach-hang/san-pham-yeu-thich?page=' + currentPage +"&idKhachHang="+$scope.khachHang.id;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        } else if ($scope.status == 2) {
            apiUrl += '&chucVu=' + 1;
        } else if ($scope.status == 3) {
            apiUrl += '&chucVu=' + 2;
        }

        console.log(apiUrl);
        $http.get(apiUrl)
            .then(function (response) {
                $scope.sanPhamYeuThichs = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                console.log($scope.sanPhamYeuThichs)
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }
    //xoá sản phẩm yêu thick
    $scope.deleteSanPhamYeuThich = function (dieuKien) {
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/rest/khach-hang/san-pham-yeu-thich/delete/' + dieuKien

        }).then(function successCallback(response) {
            // Xử lý khi API DELETE thành công
            console.log('Xóa sản phẩm yêu thích thành công ', response);
            getData(1);
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi xóa điều kiện giảm giá', response);
        });
        console.log("Điều kiện: ", dieuKien)
        $location.path("/yeu-thich");
    };


    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});