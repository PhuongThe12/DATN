var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/danhgia/views/list.html',
            controller: 'danhGiaListController'
        }).when("/update/:id", {
        templateUrl: '/pages/admin/mausac/views/update.html',
        controller: 'updateMauSacController'
    }).when("/add", {
        templateUrl: '/pages/admin/danhgia/views/add.html',
        controller: 'addDanhGiaController'
    })
        .otherwise({redirectTo: '/list'});
});

app.controller("addDanhGiaController", function ($scope, $http, $location) {
    $scope.rating = 5;
    $scope.stars = [];
    $scope.danhGia = {
        "binhLuan": $scope.binhLuan,
        "saoDanhGia": 5,
        "idGiay": $scope.idGiay,
        "idKhachHang": $scope.idKhachHang
    };
    $scope.change = function (input) {
        input.$dirty = true;
    }
    for (var i = 1; i <= 5; i++) {
        $scope.stars.push(i);
    }

    // Đặt class cho từng sao tùy thuộc vào trạng thái
    $scope.starClass = function (star) {
        return {
            'filled': star <= $scope.rating,
            'hover': star <= $scope.hoverRating
        };
    };

    $scope.toggleRating = function (star) {
        $scope.rating = star;
        $scope.danhGia.saoDanhGia = star;
    };

    $scope.hoverRating = 0;
    $scope.hover = function (star) {
        $scope.hoverRating = star;
    };

    $scope.resetHover = function () {
        $scope.hoverRating = 0;
    };

    $scope.comfirmAdd = function () {
        Swal.fire({
            text: "Xác nhận đánh giá?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.addDanhGia();
            }

        });
    }

    $scope.addDanhGia = function () {
        $scope.isLoading = true;
        // if ($scope.mauSacForm.$invalid) {
        //     $scope.isLoading = false;
        //     return;
        // }
        $http.post(host + '/rest/admin/danh-gia', $scope.danhGia)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Bình luận thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                $scope.isLoading = false;
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.errors = error.data;
                }
            });
    }


});


app.controller("danhGiaListController", function ($scope, $http, $window, $location) {

    $scope.stars = [];

    for (var i = 1; i <= 5; i++) {
        $scope.stars.push(i);
    }
    $scope.getRatingText = function (rating) {
        switch (rating) {
            case 1:
                return 'Thất Vọng';
            case 2:
                return 'Chưa tốt';
            case 3:
                return 'Chấp nhận';
            case 4:
                return 'Tốt';
            case 5:
                return 'Tuyệt vời';
            default:
                return '';
        }
    };


    $scope.curPage = 1,
        $scope.itemsPerPage = 5,
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

    $scope.changeRadio = function (star) {
        $scope.star = star;
        getData(1);
    }

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/danh-gia?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.star == 5) {
            apiUrl += '&star=' + 5;
        } else if ($scope.star == 4) {
            apiUrl += '&star=' + 4;
        } else if ($scope.star == 3) {
            apiUrl += '&star=' + 3;
        } else if ($scope.star == 2) {
            apiUrl += '&star=' + 2;
        } else if ($scope.star == 1) {
            apiUrl += '&star=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.danhGias = response.data.content;
                console.log("List đánh giá: ", $scope.danhGias)
                angular.forEach($scope.danhGias, function (dg) {
                    console.log("dg id:", dg.giayResponse.ten)
                });

                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
            });
    }

    $scope.resetSearch = function () {
        searchText = null;
        $scope.searchText = '';
        $scope.status = -1;
        getData(1);
    }

    $scope.detailDanhGia = function (val) {
        const id = val;
        if (!isNaN(id)) {
            $scope.danhGiaDetail = $scope.danhGias.find(function (danhGia) {
                return danhGia.id === id;
            });
            console.log("Detail đánh giá:", $scope.danhGiaDetail)

        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.removeDanhGia = function (danhGia) {
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/rest/admin/danh-gia/delete-danh-gia/' + danhGia
        }).then(function successCallback(response) {
            console.log('Xóa đánh giá thành công', response);
            getData(1);
        }, function errorCallback(response) {
            console.error('Lỗi xóa đánh giá giá', response);
        });
    };
    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

});

app.controller("updateMauSacController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $http.get(host + '/rest/admin/mau-sac/' + id)
        .then(function (response) {
            $scope.mauSac = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.comfirmAdd = function () {
        Swal.fire({
            text: "Xác nhận cập nhật?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.updateMauSac();
            }

        });
    }

    $scope.updateMauSac = function () {
        $scope.isLoading = true;
        if ($scope.mauSacForm.$invalid) {
            $scope.isLoading = false;
            return;
        }
        $http.put(host + '/rest/admin/mau-sac/' + id, $scope.mauSac)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            toastr["error"]("Cập nhật thất bại");
            $scope.isLoading = false;
            if (error.status === 400) {
                $scope.mauSacForm.ten.$dirty = false;
                $scope.mauSacForm.moTa.$dirty = false;
                $scope.mauSacForm.maMau.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
