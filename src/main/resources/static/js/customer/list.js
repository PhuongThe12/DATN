app.controller('listProductController', function ($scope, $http, $location) {
    $scope.giays = [];

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 12;


    function getData(currentPage) {
        $scope.isLoading = true;
        $scope.isLoading = true;
        let giaySearch = {};

        // if ($scope.tenSearch.length > 0) {
        //     giaySearch.ten = ($scope.tenSearch + "").trim();
        // } else {
        //     giaySearch.ten = null;
        // }

        let apiUrl;

        if ($scope.tenSearch) {
            giaySearch.ten = ($scope.tenSearch + "").trim();
        }

        if ($scope.status === 1) { // giày bán chạy
            apiUrl = host + '/rest/admin/giay/get-simple-by-search';
        } else if ($scope.status === 2) { // giày yêu thích
            apiUrl = host + '/rest/admin/giay/get-simple-by-search';
        } else {
            apiUrl = host + '/rest/admin/giay/find-all-by-search';
            $scope.status = 0;
        }

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        if ($scope.tenSearch) {
            giaySearch.ten = ($scope.tenSearch + "").trim();
        }
        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                console.log($scope.giays);
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
        getData($scope.curPage);
    });


    $scope.searchGiay = function () {
        getData(1);
        $scope.searching = true;
        console.log(!!$scope.tenSearch)
        if($scope.tenSearch) {
        console.log($scope.tenSearch.length);
        }
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
            getData(1);
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