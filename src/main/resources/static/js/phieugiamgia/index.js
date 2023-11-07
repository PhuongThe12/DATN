var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/danhsach", {
            templateUrl: '/pages/admin/phieugiamgia.views/list.html',
            controller: 'phieuGiamGiaListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/phieugiamgia/views/detail.html',
        controller: 'detailPhieuGiamGiaController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/phieugiamgia.views/update.html',
        controller: 'updatePhieuGiamGiaController'
    }).when("/add", {
        templateUrl: '/pages/admin/phieugiamgia.views/add.html',
        controller: 'addPhieuGiamGiaController'
    })
        .otherwise({redirectTo: '/danhsach'});
});


app.controller("addPhieuGiamGiaController", function ($scope, $http, $location) {
    $scope.addPhieuGiamGia = function (){
        console.log($scope.phieuGiamGia);

        $http.post(host + '/admin/rest/phieu-giam-gia/add', $scope.phieuGiamGia)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/danhsach");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                console.log(error)

            });
    }
});


app.controller("phieuGiamGiaListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/phieu-giam-gia?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.phieugiamgias = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.detailPhieuGiamGia = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.phieuGiamGiaDetail = $scope.phieugiamgias.find(function (phieuGiamGia) {
                return phieuGiamGia.id = id;
            });
        } else {
            alert("aaaaa")
            toastr["error"]("Lấy dữ liệu thất bại");
        }

    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("detailPhieuGiamGiaController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/phieu-giam-gia/' + id)
        .then(function (response) {
            $scope.mauSac = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        alert("bbbb")
        $location.path("/danhsach");
    });

});

app.controller("updatePhieuGiamGiaController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $http.get(host + '/admin/rest/phieu-giam-gia/' + id)
        .then(function (response) {
            $scope.phieuGiamGia = response.data;
            console.log(response.data);
            var ngayBatDauObject = new Date($scope.phieuGiamGia.ngayBatDau);
            var ngayKetThucObject = new Date($scope.phieuGiamGia.ngayKetThuc);
            $scope.phieuGiamGia.ngayBatDau = ngayBatDauObject;
            $scope.phieuGiamGia.ngayKetThuc = ngayKetThucObject;
            $scope.phieuGiamGia.idHangKhachHang = ""+$scope.phieuGiamGia.idHangKhachHang;
            $scope.phieuGiamGia.trangThai = ""+$scope.phieuGiamGia.trangThai;
            console.log($scope.phieuGiamGia.ngayBatDau);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/danhsach");
    });

    $scope.updatePhieuGiamGia = function () {
        if ($scope.phieuGiamGiaForm.$invalid) {
            return;
        }
        console.log($scope.phieuGiamGia);
        $http.put(host + '/admin/rest/phieu-giam-gia/update/' + id, $scope.phieuGiamGia)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/danhsach");
            }).catch(function (error) {
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.phieuGiamGiaForm.maPhieu.$dirty = false;
                $scope.phieuGiamGiaForm.phanTramGiam.$dirty = false;
                $scope.phieuGiamGiaForm.soLuongPhieu.$dirty = false;
                $scope.phieuGiamGiaForm.giaTriDonToiThieu.$dirty = false;
                $scope.phieuGiamGiaForm.giaTriGiamToiDa.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});

