var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/dotgiamgia/views/list.html',
            controller: 'dotGiamGiaListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/mausac/views/detail.html',
        controller: 'detailMauSacController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/dotgiamgia/views/update.html',
        controller: 'updateDotGiamGiaController'
    }).when("/add", {
        templateUrl: '/pages/admin/dotgiamgia/views/add.html',
        controller: 'addDotGiamGiaController'
    })
        .otherwise({ redirectTo: '/list' });
});

app.controller("addDotGiamGiaController", function ($scope, $http, $location) {
    $scope.dotGiamGia = {};
    $scope.dotGiamGia.dieuKienRequests = [{
        tongHoaDon: "",
        phanTramGiam: ""
    }];

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.removeDieuKien = function (index) {
        $scope.dotGiamGia.dieuKienRequests.splice(index, 1);
    }

    $scope.addDotGiamGia = function () {

        if ($scope.dotGiamGiaForm.$invalid) {
            return;
        }

        // Gửi danh sách các điều kiện lên backend
        $scope.dotGiamGia.dieuKienRequests = $scope.dotGiamGia.dieuKienRequests.map(function (dieuKien) {
            return {
                tongHoaDon: dieuKien.tongHoaDon,
                phanTramGiam: dieuKien.phanTramGiam
            };
        });

        $http.post(host + '/admin/rest/dot-giam-gia', $scope.dotGiamGia)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.dotGiamGiaForm.ten.$dirty = false;
                    $scope.dotGiamGiaForm.ghiChu.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

    $scope.addDieuKien = function () {
        $scope.dotGiamGia.dieuKienRequests.push({
            tongHoaDon: "",
            phanTramGiam: ""
        });
    }
    $scope.hasErrorInItems = false;
    $scope.checkErrorsInItems = function() {
        $scope.hasErrorInItems = false;
        for (var i = 0; i < $scope.items.length; i++) {
            if ($scope.items[i].itemForm.$invalid) {
                $scope.hasErrorInItems = true;
                break;
            }
        }
    };
    $scope.$watch('items', function(newItems, oldItems) {
        $scope.checkErrorsInItems();
    }, true);

});


app.controller("detailMauSacController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/mau-sac/' + id)
        .then(function (response) {
            $scope.mauSac = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

});

app.controller("dotGiamGiaListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/admin/rest/dot-giam-gia?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.dotGiamGias = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.detailDotGiamGia = function (val) {
        var id = val;
        $http.get(host + '/admin/rest/dot-giam-gia/' + id)
            .then(function (response) {
                $scope.dotGiamGiaDetail = response.data;
                const button = document.querySelector('[data-bs-target="#showDotGiamGia"]');
                if (button) {
                    button.click();
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });


});

app.controller("updateDotGiamGiaController", function ($scope, $http, $location, $routeParams) {
    $scope.dotGiamGia = {};
    $scope.dotGiamGia.id = $routeParams.id;
    $scope.dotGiamGia.dieuKienRequests = [];

    $scope.init = function () {
        $http.get(host + '/admin/rest/dot-giam-gia/' + $scope.dotGiamGia.id)
            .then(function (response) {
                if (response.status === 200) {
                    $scope.dotGiamGia = response.data;
                    console.log($scope.dotGiamGia)
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    };

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.removeDieuKien = function (index) {
        $scope.dotGiamGia.dieuKienRequests.splice(index, 1);
    }

    $scope.updateDotGiamGia = function () {

        if ($scope.dotGiamGiaForm.$invalid) {
            return;
        }
        console.log($scope.dotGiamGia);
        console.log($scope.dotGiamGia.dieuKienRequests);
        // Gửi danh sách các điều kiện lên backend
        $scope.dotGiamGia.dieuKienRequests = $scope.dotGiamGia.dieuKienResponses.map(function (dieuKien) {
            return {
                id: dieuKien.id,
                tongHoaDon: dieuKien.tongHoaDon,
                phanTramGiam: dieuKien.phanTramGiam
            };
        });

        console.log($scope.dotGiamGia);

        $http.put(host + '/admin/rest/dot-giam-gia/' + $scope.dotGiamGia.id, $scope.dotGiamGia)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Cập nhật thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Cập nhật thất bại");
                if (error.status === 400) {
                    $scope.dotGiamGiaForm.ten.$dirty = false;
                    $scope.dotGiamGiaForm.ghiChu.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

    $scope.addDieuKien = function () {
        $scope.dotGiamGia.dieuKienRequests.push({
            tongHoaDon: "",
            phanTramGiam: ""
        });
    }
    $scope.hasErrorInItems = false;
    $scope.checkErrorsInItems = function() {
        $scope.hasErrorInItems = false;
        for (var i = 0; i < $scope.items.length; i++) {
            if ($scope.items[i].itemForm.$invalid) {
                $scope.hasErrorInItems = true;
                break;
            }
        }
    };
    $scope.$watch('items', function(newItems, oldItems) {
        $scope.checkErrorsInItems();
    }, true);

    $scope.init();
});

