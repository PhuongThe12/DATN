// var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
// app.config(function ($routeProvider, $locationProvider) {
//     $locationProvider.hashPrefix('');
//     $routeProvider
//         .when("/thong-tin-tai-khoan", {
//             templateUrl: '/pages/user/views/thong-tin-tai-khoan.html',
//             controller: 'thongTinTaiKhoanController'
//         })
//         .otherwise({redirectTo: '/thong-tin-tai-khoan'});
// });

app.controller("thongTinTaiKhoanController", function ($scope, $http, $window, $location, $cookies) {

    $scope.khachHang = {};
    $scope.khachHangUpdatePassword = {};

    var token = $cookies.get('token');
    if (token) {
        $http.get(host + "/session/get-customer")
            .then(response => {
                if (response.data !== null) {
                    $scope.khachHang = response.data;
                    $scope.khachHang.ngaySinh = new Date($scope.khachHang.ngaySinh);

                    let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang/khach-hang';

                    apiUrl += '?idKhachHang=' + $scope.khachHang.id;
                    console.log(apiUrl);
                    $http.get(apiUrl)
                        .then(function (response) {
                            $scope.diaChiNhanHangs = response.data;
                            console.log($scope.diaChiNhanHangs);
                        })
                        .catch(function (error) {
                            toastr["error"]("Lấy dữ liệu thất bại");
                            // window.location.href = feHost + '/trang-chu';
                        });
                }
            })
    } else {
        $window.location.href = '/home';
    }

    $scope.toggleShowPassUpdate = function (idDom) {
        if (idDom === 'khachHangOldPassword') {
            document.getElementById('khachHangOldPassword').type = $scope.khachHangUpdatePassword.showPassOld ? 'text' : 'password';
        }
        if (idDom === 'khachHangNewPassword') {
            document.getElementById('khachHangNewPassword').type = $scope.khachHangUpdatePassword.showPassNew ? 'text' : 'password';
        }
        if (idDom === 'khachHangReNewPassword') {
            document.getElementById('khachHangReNewPassword').type = $scope.khachHangUpdatePassword.showPassReNew ? 'text' : 'password';
        }
    }

    $scope.errors = {};

    $scope.changeOldPassword = function () {
        if ($scope.khachHangUpdatePassword.oldPass === $scope.khachHangUpdatePassword.newPass && $scope.khachHangUpdatePassword.oldPass && $scope.khachHangUpdatePassword.newPass) {
            $scope.errors.newPassword = 'Mật khẩu mới không được giống mật khẩu cũ';
        } else {
            $scope.errors.newPassword = null;
        }
    }

    $scope.changeNewPassword = function () {
        if ($scope.khachHangUpdatePassword.oldPass === $scope.khachHangUpdatePassword.newPass && $scope.khachHangUpdatePassword.oldPass && $scope.khachHangUpdatePassword.newPass) {
            $scope.errors.newPassword = 'Mật khẩu mới không được giống mật khẩu cũ';
        } else {
            $scope.errors.newPassword = null;
        }
        if ($scope.khachHangUpdatePassword.renewPass !== $scope.khachHangUpdatePassword.newPass || !$scope.khachHangUpdatePassword.renewPass) {
            $scope.errors.renewPassword = 'Mật khẩu không trùng khớp';
        } else {
            $scope.errors.renewPassword = null;
            $scope.errors.newPassword = null;
        }
    }

    $scope.changeRePassword = function () {
        if ($scope.khachHangUpdatePassword.renewPass !== $scope.khachHangUpdatePassword.newPass || !$scope.khachHangUpdatePassword.renewPass) {
            $scope.errors.renewPassword = 'Mật khẩu không trùng khớp';
        } else {
            $scope.errors.renewPassword = null;
        }

    }

    $scope.updatePassword = function () {
        if (!$scope.khachHangUpdatePassword.oldPass || !$scope.khachHangUpdatePassword.newPass || !$scope.khachHangUpdatePassword.renewPass) {
            return;
        }

        if ($scope.khachHangUpdatePassword.oldPass === $scope.khachHangUpdatePassword.newPass) {
            return;
        }

        if ($scope.khachHang.taiKhoan) {
            $scope.isLoading = true;
            $http.get(host + "/tai-khoan/thay-doi-mat-khau/" + $scope.khachHang.taiKhoan.id + "?mkCu=" + $scope.khachHangUpdatePassword.oldPass + "&mkMoi=" + $scope.khachHangUpdatePassword.newPass)
                .then(response => {
                    toastr["success"]("Thay đổi mật khẩu thành công");
                    document.getElementById('closeModalUpdateMK').click();
                    $scope.khachHangUpdatePassword = {};
                    $scope.isLoading = false;
                })
                .catch(error => {
                    toastr["error"]("Thay đổi mật khẩu thất bại vui lòng kiểm tra lại");
                    $scope.isLoading = false;
                })
        } else {
            toastr["warning"]("Vui lòng đăng nhập để thực hiện chứ năng này");
        }

    }


    // function getData() {
    //
    //     let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang/khach-hang';
    //
    //     apiUrl+= '&idKhachHang='+$scope.khachHang.id;
    //     $http.get(apiUrl)
    //         .then(function (response) {
    //             $scope.diaChiNhanHangs = response.data.content;
    //             console.log($scope.diaChiNhanHangs);
    //             $scope.numOfPages = response.data.totalPages;
    //         })
    //         .catch(function (error) {
    //             toastr["error"]("Lấy dữ liệu thất bại");
    //             // window.location.href = feHost + '/trang-chu';
    //         });
    // }

    $scope.updateTrangThai = function (dieuKien) {
        console.log(dieuKien)
        $scope.trangThai = {
            "id": dieuKien,
            "trangThai": 1
        }

        $http({
            method: 'PUT',
            url: 'http://localhost:8080/rest/khach-hang/dia-chi-nhan-hang/update-trang-thai/' + dieuKien,

            data: $scope.trangThai
        }).then(function successCallback(response) {
            // Xử lý khi API UPDATE thành công
            console.log('UPDATE điều kiện giảm giá thành công', response);
            loadDiaChi();
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
            loadDiaChi();
        }, function errorCallback(response) {
            // Xử lý khi có lỗi xảy ra trong quá trình gọi API DELETE
            console.error('Lỗi xóa điều kiện giảm giá', response);
        });
        console.log("Điều kiện: ", dieuKien)
    };


    function loadDiaChi() {
        let apiUrl = host + '/rest/khach-hang/dia-chi-nhan-hang/khach-hang';

        apiUrl += '?idKhachHang=' + $scope.khachHang.id;
        console.log(apiUrl);
        $http.get(apiUrl)
            .then(function (response) {
                $scope.diaChiNhanHangs = response.data;
                console.log($scope.diaChiNhanHangs);
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }


    //hiển thị thông tin khách hàng lên modal
    $scope.detailKhachHang = function (khachHang) {
        if (khachHang.id) {
            console.log(khachHang);
            $scope.khachHangUpdate = {};
            $scope.khachHangUpdate.id = khachHang.id;
            $scope.khachHangUpdate.gioiTinh = khachHang.gioiTinh;
            $scope.khachHangUpdate.hoTen = khachHang.hoTen;
            $scope.khachHangUpdate.ngaySinh = khachHang.ngaySinh;
            $scope.khachHangUpdate.soDienThoai = khachHang.soDienThoai;
        } else {
            toastr["warning"]("Vui lòng đăng nhập để thực hiện chứ năng này");
        }
    }

    // cập nhật thông tin khách hàng
    $scope.updateKhachHang = function () {
        if ($scope.khachHangUpdateForm.$invalid) {
            return;
        }

        const khachHangUpdate = {
            id: $scope.khachHangUpdate.id,
            hoTen: $scope.khachHangUpdate.hoTen,
            gioiTinh: $scope.khachHangUpdate.gioiTinh,
            ngaySinh: $scope.khachHangUpdate.ngaySinh,
            soDienThoai: $scope.khachHangUpdate.soDienThoai

        };

        $scope.isLoading = true;
        $http.put(host + '/rest/admin/khach-hang/cap-nhat-mot-phan/' + khachHangUpdate.id, khachHangUpdate)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Cập nhật thành công")
                    $scope.khachHang.hoTen = response.data.hoTen;
                    $scope.khachHang.gioTinh = response.data.gioiTinh;
                    $scope.khachHang.ngaySinh = new Date(response.data.ngaySinh);
                    $scope.khachHang.soDienThoai = response.data.soDienThoai;
                    document.getElementById('closeModalUpdateKhachhang').click();
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $scope.isLoading = false;
            })
            .catch(function (error) {
                toastr["error"]("Cập nhật thất bại vui lòng thử lại");
                $scope.isLoading = false;
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

        $http.get(host + '/rest/khach-hang/dia-chi-nhan-hang/' + $scope.khachHang.id)

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

            $http.put(host + '/rest/khach-hang/dia-chi-nhan-hang/' + $scope.khachHang.id, diaChiNhanHangUpdate)

                .then(function (response) {
                    if (response.status == 200) {
                        toastr["success"]("Cập nhật thành công")
                        $('#modalDiaChi').modal('hide');
                        console.log("thông tin sau cập nhập")
                        console.log(diaChiNhanHangUpdate)
                        loadDiaChi();
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
            $scope.diaChiNhanHang.idKhachHang = $scope.khachHang.id;

            console.log($scope.diaChiNhanHang);
            $http.post(host + '/rest/khach-hang/dia-chi-nhan-hang', $scope.diaChiNhanHang)
                .then(function (response) {
                    if (response.status === 200) {
                        toastr["success"]("Thêm thành công");
                        $('#modalThemDiaChi').modal('hide');
                        loadDiaChi();
                    }
                })
                .catch(function (error) {
                    toastr["error"]("Thêm thất bại");
                    console.log(error);
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
    function setData() {
        for (let i = 0; i < $scope.provinces.length; i++) {
            if ($scope.provinces[i].ten === $scope.diaChiNhanHang.provinces) {
                $scope.diaChiNhanHang.provinces = $scope.provinces[i];
                break;
            }
        }

        $http.get(host + "/rest/districts/" + $scope.diaChiNhanHang.provinces.id)
            .then(function (response) {
                $scope.districts = response.data;
                for (let j = 0; j < $scope.districts.length; j++) {
                    if ($scope.districts[j].ten === $scope.diaChiNhanHang.districts) {
                        $scope.diaChiNhanHang.districts = $scope.districts[j];
                        $http.get(host + "/rest/wards/" + $scope.diaChiNhanHang.districts.id)
                            .then(function (response) {
                                $scope.wards = response.data;
                                for (let k = 0; k < $scope.wards.length; k++) {
                                    if ($scope.wards[k].ten === $scope.diaChiNhanHang.wards) {
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


});
