const app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/hoa-don", {
            templateUrl: '/pages/admin/hoadon/views/hoa-don.html',
            controller: 'hoaDonController'
        })
        .when("/hoa-don-cho", {
            templateUrl: '/pages/admin/hoadon/views/hoa-don-cho.html',
            controller: 'hoaDonChoController'
        })
        .when("/hoa-don-hoan", {
            templateUrl: '/pages/admin/hoadon/views/hoa-don-hoan.html',
            controller: 'hoaDonHoanController'
        })
        .otherwise({redirectTo: '/hoa-don'});
});

app.controller('hoaDonController', function ($scope, $http, $location) {
    $scope.curPage = 1,
        $scope.itemsPerPage = 10,
        $scope.maxSize = 5;

    let hoaDonSearch = {};
    $scope.searching = true;

    $scope.search = function () {
        getData(1);
        $scope.searching = true;
    };

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/hoa-don/get-all-order-ngay-thanh-toan';

        if ($scope.searchText > 0) {
            hoaDonSearch.id = ($scope.searchText + "").trim();
        } else {
            hoaDonSearch.id = null;
        }

        if ($scope.status === -1) {
            hoaDonSearch.trangThai = -1;
        } else if ($scope.status === 1) {
            hoaDonSearch.trangThai = 1;
        } else {
            // toastr["error"]("Lấy dữ liệu thất bại. Vui lòng thử lại");
            $scope.status = 1;
            hoaDonSearch.trangThai = 1;
        }


        hoaDonSearch.tuNgay = $scope.tu;
        if($scope.tu && hoaDonSearch.tuNgay.getHours() === 0) {
            hoaDonSearch.tuNgay.setHours(hoaDonSearch.tuNgay.getHours() + 7);
        }

        hoaDonSearch.denNgay = $scope.den;
        if($scope.den && hoaDonSearch.denNgay.getHours() === 0) {
            hoaDonSearch.denNgay.setHours(hoaDonSearch.denNgay.getHours() + 7);
        }

        hoaDonSearch.currentPage = currentPage;
        hoaDonSearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, hoaDonSearch)
            .then(function (response) {
                $scope.hoaDons = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    $scope.resetSearch = function () {
        if ($scope.searching) {
            $scope.searchText = '';
            $scope.tu = null;
            $scope.den = null;
            getData(1);
            $scope.searching = false;
        } else {
            toastr["warning"]("Bạn đang không tìm kiếm");
        }
    }

    $scope.resetSearch();

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

    $scope.detailHoaDon = function (id) {
        const hoaDon = $scope.hoaDons.find(item => item.id === id);
        if (hoaDon) {
            $scope.hoaDonDetail = hoaDon;
            $scope.hoaDonDetail.conLai = 0;

            $scope.hoaDonDetail.thongTinThanhToan = {
                show: false
            };
            hoaDon.chiTietThanhToans.forEach(item => {
                $scope.hoaDonDetail.conLai += item.tienThanhToan;
                $scope.hoaDonDetail.thongTinThanhToan.show = true;
                if (item.hinhThucThanhToan === 1) {
                    $scope.hoaDonDetail.thongTinThanhToan.tienMat = item.tienThanhToan;
                }
                if (item.hinhThucThanhToan === 2) {
                    $scope.hoaDonDetail.thongTinThanhToan.chuyenKhoan = item.tienThanhToan;
                    $scope.hoaDonDetail.thongTinThanhToan.maGiaoDich = item.maGiaoDich;
                }
            });
            $scope.hoaDonDetail.tongTru = hoaDon.tienGiam ? hoaDon.tienGiam : 0;
            $scope.hoaDonDetail.tienShip = hoaDon.phiShip ? hoaDon.phiShip : 0;

            $scope.hoaDonDetail.tongCong = $scope.hoaDonDetail.conLai + $scope.hoaDonDetail.tongTru + $scope.hoaDonDetail.tienShip;

            $scope.hoaDonDetail.trangThai = hoaDon.trangThai;
            getDetailDoiTra($scope.hoaDonDetail.id);

        } else {
            toastr["error"]("Lấy dữ liệu thất bại. Vui lòng thử lại");
            document.getElementById('closeModal').click();
        }
    }

    function getDetailDoiTra(id) {
        $scope.isLoading = true;
        $scope.hoaDonDoi = {};
        $scope.hoaDonTra = {};

        $http.get(host + "/rest/admin/hoa-don/get-don-doi-tra/" + id)
            .then(function (response) {
                if (response.data) {
                    response.data.forEach(item => {
                        if (item.loaiHoaDon === 1) {
                            $scope.hoaDonDoi = item;
                        } else {
                            $scope.hoaDonTra = item;
                        }
                    })
                }

                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

});