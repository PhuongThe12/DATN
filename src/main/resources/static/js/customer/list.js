app.controller('listProductController', function ($scope, $http, $location, $window) {

    $http.get(host + "/session/get-customer")
        .then(response => {
            $scope.currentKhachHang = response.data;
            console.log($scope.currentKhachHang);
        })

    $scope.giays = [];

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 12;


    function getData(currentPage, giaySearch) {
        $scope.isLoading = true;
        let apiUrl;

        if ($scope.status === 1) {
            giaySearch.trangThai = 1;
        } else if ($scope.status === 2) { // giày yêu thích
            if ($scope.currentKhachHang) {
                giaySearch.trangThai = 2;
                giaySearch.idKhachHang = $scope.currentKhachHang.id;
            } else {
                // $scope.status = 0;
                console.log($scope.status);
                toastr["error"]("Bạn chưa đăng nhập");
                $scope.isLoading = false;
                return;
            }
            // giaySearch.idKhachHang = ;
        } else {
            $scope.status = 0;
            giaySearch.trangThai = 0;
        }
        apiUrl = host + '/rest/admin/giay/find-all-by-search';

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage, {});
    });

    $scope.changeRadio = function (status) {
        $scope.status = status;
        $scope.resetSearchGiay();
        if (!$scope.searching) {
            getData(1, {});
        }
    }

    $scope.errorsSearch = {};

    $scope.searchGiay = function () {
        let giaySearch = {};

        if ($scope.tenSearch) {
            giaySearch.ten = ($scope.tenSearch + "").trim();
        }

        giaySearch.thuongHieuIds = $scope.thuongHieus.filter(item => item.selected).map(item => item.id);
        if (giaySearch.thuongHieuIds.length === 0) {
            giaySearch.thuongHieuIds = null;
        }
        giaySearch.mauSacIds = $scope.mauSacs.filter(item => item.selected).map(item => item.id);
        if (giaySearch.mauSacIds.length === 0) {
            giaySearch.mauSacIds = null;
        }
        giaySearch.kichThuocIds = $scope.kichThuocs.filter(item => item.selected).map(item => item.id);
        if (giaySearch.kichThuocIds.length === 0) {
            giaySearch.kichThuocIds = null;
        }
        giaySearch.hashTagIds = $scope.hashtags.filter(item => item.selected).map(item => item.id);
        if (giaySearch.hashTagIds.length === 0) {
            giaySearch.hashTagIds = null;
        }

        if ($scope.giaTu) {
            giaySearch.giaTu = $scope.giaTu;
        }

        if ($scope.giaDen) {
            giaySearch.giaDen = $scope.giaDen;
        }

        if ($scope.giaTu && $scope.giaDen && $scope.giaTu < $scope.giaDen) {
            $scope.errorsSearch = 'Giá đến phải lớn hơn giá từ'
        }

        if (!giaySearch.ten && !giaySearch.giaTu && !giaySearch.giaDen && !giaySearch.thuongHieuIds && !giaySearch.hashTagIds && !giaySearch.kichThuocIds && !giaySearch.mauSacIds) {
            toastr["error"]("Không có đủ dữ liệu để tìm kiếm");
            return;
        }

        getData(1, giaySearch);
        $scope.searching = true;
    }

    $scope.resetSearchGiay = function () {
        $scope.giaTu = '';
        $scope.giaDen = '';
        $scope.tenSearch = '';
        $scope.thuongHieus.forEach(item => {
            item.selected = false;
        });
        $scope.mauSacs.forEach(item => {
            item.selected = false;
        });
        $scope.kichThuocs.forEach(item => {
            item.selected = false;
        });
        $scope.hashtags.forEach(item => {
            item.selected = false;
        });
        if ($scope.searching) {
            getData(1, {});
            $scope.searching = false;
        }
    }

    $scope.toggleCollapse = function (idIcon) {
        document.getElementById(idIcon).classList.toggle('rotate');
    }

    $scope.thuongHieus = [];
    setTimeout(function () {
        $http.get(host + "/rest/admin/thuong-hieu/get-all")
            .then(function (response) {
                $scope.thuongHieus = response.data;
                $scope.selectedThuongHieu = $scope.thuongHieus[0] ? $scope.thuongHieus[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thương hiệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select mau sac
    $scope.mauSacs = [];
    setTimeout(function () {
        $http.get(host + "/rest/admin/mau-sac/get-all")
            .then(function (response) {
                $scope.mauSacs = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu màu sắc thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select size
    $scope.kichThuocs = [];
    setTimeout(function () {
        $http.get(host + "/rest/admin/kich-thuoc/get-all")
            .then(function (response) {
                $scope.kichThuocs = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu kích thước thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select hashtag
    $scope.hashtags = [];
    setTimeout(function () {
        $http.get(host + "/rest/admin/hash-tag/get-all")
            .then(function (response) {
                $scope.hashtags = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu hash tag thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);
});