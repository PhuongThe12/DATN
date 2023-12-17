var app = angular.module("app", ["ngRoute", "ui.bootstrap","ngCookies"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/diachinhanhang/views/list.html',
            controller: 'diaChiNhanHangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/diachinhanhang/views/detail.html',
        controller: 'detailDiaChiNhanHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/diachinhanhang/views/update.html',
        controller: 'updateDiaChiNhanHangController'
    }).when("/add", {
        templateUrl: '/pages/admin/diachinhanhang/views/add.html',
        controller: 'addDiaChiNhanHangController'
    })
        .otherwise({redirectTo: '/list'});
});

app.controller("addDiaChiNhanHangController", function ($scope, $http, $location) {
    $scope.diaChiNhanHang = {};
    $scope.change = function (input) {
        input.$dirty = true;
    }
    //select provinces
    $http.get(host + "/rest/provinces/get-all")
        .then(function (response) {
            $scope.provinces = response.data;
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu tỉnh thất bại");
        });


//    lấy dữ liệu huyện theo id tỉnh
    $scope.changeProvince = function () {
        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                // console.log($scope.diaChiNhanHang.provinces)
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ huyện thất bại");
            });
    }
    // lấy dữ liệu theo xã theo huyện
    $scope.changeDistrict = function () {
        if ($scope.diaChiNhanHang.districts.id == 'undefined') {
            alert(" mời bạn chọn tỉnh")
        } else {
            $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ liệu xã thất bại");
                });
        }

    }

    $scope.addDiaChiNhanHang = function () {
        if ($scope.diaChiNhanHangForm.$invalid) {
            return;
        }

        $scope.diaChiNhanHang.districts = $scope.diaChiNhanHang.districts.ten;
        $scope.diaChiNhanHang.provinces = $scope.diaChiNhanHang.provinces.ten;
        $scope.diaChiNhanHang.wards = $scope.diaChiNhanHang.wards.ten;

        console.log($scope.diaChiNhanHang);
        $http.post(host + '/rest/khach-hang/dia-chi-nhan-hang', $scope.diaChiNhanHang)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    console.log($scope.diaChiNhanHangForm);
                    $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                    $scope.diaChiNhanHangForm.diaChiNhan.$dirty = false;
                    $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

});


app.controller("diaChiNhanHangListController", function ($scope, $http, $window, $location) {

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

    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(ahhoang) {
        let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang?page=' + ahhoang;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        console.log(apiUrl);

        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data.content;
                console.log($scope.diaChiNhanHangs);
                // $scope.diaChiNhanHang = response.data;
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

    $scope.removeDieuKien = function (dieuKien) {
        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/rest/khach-hang/dia-chi-nhan-hang/delete/' + dieuKien
        }).then(function successCallback(response) {
            // Xử lý khi API DELETE thành công
            console.log('Xóa điều kiện giảm giá thành công', response);
            getData(1);
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi xóa điều kiện giảm giá', response);
        });
        console.log("Điều kiện: ", dieuKien)
        $location.path("/list");
    };

    $scope.updateTrangThai = function (dieuKien) {
        console.log(dieuKien)
        $scope.trangThai = {
            "id": dieuKien,
            // "trangThai": 0
        }

        $http({
            method: 'PUT',
            url: 'http://localhost:8080/rest/khach-hang/dia-chi-nhan-hang/update-trang-thai/' + dieuKien ,
            data:$scope.trangThai
        }).then(function successCallback(response) {
            // Xử lý khi API UPDATE thành công
            console.log('UPDATE điều kiện giảm giá thành công', response);
            getData(1);
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi', response);
        });
        console.log("Điều kiện: ", dieuKien)
        $location.path("/list");
    };


});

app.controller("updateDiaChiNhanHangController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    //select provinces
    $http.get(host + "/rest/provinces/get-all")
        .then(function (response) {
            $scope.provinces = response.data;
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu tỉnh thất bại");
        });


//    lấy dữ liệu huyện theo id tỉnh
    $scope.changeProvince = function () {
        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                // console.log($scope.diaChiNhanHang.provinces)
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ huyện thất bại");
            });
    }
    // lấy dữ liệu theo xã theo huyện
    $scope.changeDistrict = function () {
        if ($scope.diaChiNhanHang.districts.id == 'undefined') {
            alert(" mời bạn chọn tỉnh")
        } else {
            $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    toastr["error"]("Lấy dữ liệu xã thất bại");
                });
        }

    }
    $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + id)
        .then(function (response) {
            $scope.diaChiNhanHang = response.data;
            setData();
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        // $location.path("/list");
    });

    function setData ()  {
        for(let i = 0; i < $scope.provinces.length; i++) {
            if($scope.provinces[i].ten === $scope.diaChiNhanHang.provinces) {
                $scope.diaChiNhanHang.provinces = $scope.provinces[i];
                break;
            }
        }

        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                for(let j = 0; j < $scope.districts.length; j++) {
                    if($scope.districts[j].ten === $scope.diaChiNhanHang.districts) {
                        $scope.diaChiNhanHang.districts = $scope.districts[j];
                        $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                            .then(function (response) {
                                $scope.wards = response.data;
                                for(let k = 0; k < $scope.wards.length; k++) {
                                    if($scope.wards[k].ten === $scope.diaChiNhanHang.wards) {
                                        $scope.diaChiNhanHang.wards = $scope.wards[k];
                                        break;
                                    }
                                }
                            })
                            .catch(function (error) {
                                toastr["error"]("Lấy dữ liệu xã thất bại");
                            });
                        break;
                    }
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ huyện thất bại");
            });


    }

    $scope.updateDiaChiNhanHang = function () {
        if ($scope.diaChiNhanHangForm.$invalid) {
            return;
        }
        $scope.diaChiNhanHang.districts = $scope.diaChiNhanHang.districts.ten;
        $scope.diaChiNhanHang.provinces = $scope.diaChiNhanHang.provinces.ten;
        $scope.diaChiNhanHang.wards = $scope.diaChiNhanHang.wards.ten;

        $http.put(host + '/rest/khach-hang/dia-chi-nhan-hang/' + id, $scope.diaChiNhanHang)
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
                $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});
