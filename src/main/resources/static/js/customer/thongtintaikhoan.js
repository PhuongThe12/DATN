var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/thong-tin-tai-khoan", {
            templateUrl: '/pages/user/views/thong-tin-tai-khoan.html',
            controller: 'thongTinTaiKhoanController'
        })
        .otherwise({redirectTo: '/thong-tin-tai-khoan'});
});

app.controller("thongTinTaiKhoanController", function ($scope, $http, $window, $location) {
    console.log("aaaaaaa")
    $scope.curPage = 1,
        $scope.itemsPerPage = 4,
        $scope.maxSize = 4;
    let searchText;
    $scope.change = function (input) {
        input.$dirty = true;
    }


    $scope.changeRadio = function (status) {
        $scope.status = status;
        getData(1);
    }

    function getData(currentPage) {

        let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang?page=' + currentPage;

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data.content;
                console.log($scope.diaChiNhanHangs);
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.updateTrangThai = function (dieuKien) {
        console.log(dieuKien)
        $scope.trangThai = {
            "id": dieuKien,
            "trangThai": 1
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
        $location.path("/thong-tin-tai-khoan");
    };



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
        $location.path("/thong-tin-tai-khoan");
    };


//    thông tin khách hàng
    const id = 5;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    console.log("thông tin phương đây")
    $http.get(host + '/rest/admin/khach-hang/' + id)
        .then(function (response) {
            $scope.khachHang = response.data;
            var ngaySinh = $scope.khachHang.ngaySinh;
            var object = new Date(ngaySinh);
            $scope.khachHang.ngaySinh = object;
            console.log(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/thong-tin-tai-khoan");
    });

    //hiển thị thông tin khách hàng lên modal
    $scope.detailKhachHang = function (val) {
        var id = val;
        console.log("vào detail khách hàng"+"id khách hàng" +val)
        $http.get(host + '/rest/admin/khach-hang/' + id)
            .then(function (response) {
                $scope.khachHang = response.data;
                var ngaySinh = $scope.khachHang.ngaySinh;
                var object = new Date(ngaySinh);
                $scope.khachHang.ngaySinh = object;
                console.log("$scope.khachHangDetail ")
                console.log($scope.khachHang )
                const button = document.querySelector('[data-bs-target="#modalUpdateKhachHang"]');
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    // cập nhật thông tin khách hàng
    $scope.updateKhachHang = function () {
        if ($scope.khachHangForm.$invalid) {
            return;
        }
        const khachHangUpdate = {
            id: $scope.khachHang.id,
            hoTen: $scope.khachHang.hoTen,
            gioiTinh: $scope.khachHang.gioiTinh,
            ngaySinh: $scope.khachHang.ngaySinh,
            soDienThoai: $scope.khachHang.soDienThoai,
            email: $scope.khachHang.email,
            // diemTichLuy: $scope.khachHang.diemTichLuy,
            // trangThai: $scope.khachHang.trangThai,

        };
        console.log($scope.khachHang);
        $http.put(host + '/rest/admin/khach-hang/' + id, khachHangUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                    $('#modalUpdateKhachHang').modal('hide');
                    getData(1)
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.khachHangForm.hoTen.$dirty = false;
                $scope.khachHangForm.gioiTinh.$dirty = false;
                $scope.khachHangForm.ngaySinh.$dirty = false;
                $scope.khachHangForm.soDienThoai.$dirty = false;
                $scope.khachHangForm.email.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
    //

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
    // hiển thị  địa chỉ nhận lên modal

    $scope.updateDiaChiNhan = function (id) {
        console.log("vào update")
        console.log(id)

        $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + id)

            .then(function (response) {
                $scope.diaChiNhanHang = response.data;
                console.log("show dia chi:", $scope.diaChiNhanHang)
                setData();
                $('#modalDiaChi').modal('show');
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại1");
            });

        // UPDATE địa chỉ nhận
        $scope.updateDiaChiNhanHang = function () {
            console.log("vào update địa chỉ rùi")
            if ($scope.diaChiNhanHangForm.$invalid) {
                return;
            }
            const diaChiNhanHangUpdate = {
                diaChiNhan: $scope.diaChiNhanHang.diaChiNhan,
                soDienThoaiNhan: $scope.diaChiNhanHang.soDienThoaiNhan,
                hoTen: $scope.diaChiNhanHang.hoTen,
                provinces: $scope.diaChiNhanHang.provinces.ten,
                districts: $scope.diaChiNhanHang.districts.ten,
                wards: $scope.diaChiNhanHang.wards.ten


            };
            // $scope.diaChiNhanHang.districts = $scope.diaChiNhanHang.districts.ten;
            // $scope.diaChiNhanHang.provinces = $scope.diaChiNhanHang.provinces.ten;
            // $scope.diaChiNhanHang.wards = $scope.diaChiNhanHang.wards.ten;

            $http.put(host + '/rest/khach-hang/dia-chi-nhan-hang/' + id,diaChiNhanHangUpdate)

                .then(function (response) {
                    if (response.status == 200) {
                        toastr["success"]("Cập nhật thành công")
                        $('#modalDiaChi').modal('hide');
                        console.log("thông tin sau cập nhập")
                        console.log(diaChiNhanHangUpdate)
                        getData(1)
                    } else {
                        toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                    }
                    $location.path("/list");
                }).catch(function (error) {
                toastr["error"]("Cập nhật thất bại");
                if (error.status === 400) {
                    $scope.diaChiNhanHangForm.hoTen.$dirty = false;
                    $scope.diaChiNhanHangForm.soDienThoaiNhan.$dirty = false;
                    $scope.errors = error.data;
                }
            })
        };

    }

    // thêm địa chỉ nhận
    $scope.ThemDiaChiNhan = function () {
        console.log("vào modal add rùi")
        $('#modalThemDiaChi').modal('show');
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
                        $('#modalThemDiaChi').modal('hide');
                        getData(1)
                        console.log("thêm thành công 111")
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

    }

    //
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

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });
});
