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
        .otherwise({redirectTo: '/list'});
});

app.controller("addDotGiamGiaController", function ($scope, $http, $location) {
    $scope.dotGiamGia = {};
    $scope.dotGiamGia.dieuKienRequests = [{
        errors: {},
        tongHoaDon: "",
        phanTramGiam: ""
    }];

    $scope.checkNgayBatDau = function () {
        var currentDate = new Date();
        currentDate.setHours(0, 0, 0, 0); // Đặt giờ về 0 để so sánh ngày mà không tính giờ, phút, giây
        var ngayBatDau = new Date($scope.dotGiamGia.ngayBatDau);

        return ngayBatDau >= currentDate;
    };

    $scope.change = function (input, model) {
        if (model === 'tongHoaDon') {
            if (input.tongHoaDon) {
                input.errors.tongHoaDon = null;
                $scope.dotGiamGia.dieuKienRequests.forEach(item => {
                    if (item.tongHoaDon === input.tongHoaDon && item !== input) {
                        input.errors.tongHoaDon = 'Tổng giá trị hóa đơn không được trùng với điều kiện khác';
                    }
                });
            } else {
                input.errors.tongHoaDon = 'Tổng giá trị hóa đơn phải là số >= 0';
            }

        } else if (model === 'phanTramGiam') {
            if (input.phanTramGiam) {
                input.errors.phanTramGiam = null;
                if (input.phanTramGiam % 1 !== 0) {
                    input.phanTramGiam = parseInt(input.phanTramGiam);
                }
            } else {
                input.errors.phanTramGiam = 'Phần trăm giảm phải là số nguyên từ 1 - 50';
            }
        }
    }

    $scope.addDieuKien = function () {

        let invalid = false;
        $scope.dotGiamGia.dieuKienRequests.forEach(item => {
            if (!item.tongHoaDon) {
                item.errors.tongHoaDon = 'Tổng giá trị hóa đơn phải là số >= 0';
                invalid = true;
            }
            if (!item.phanTramGiam) {
                item.errors.phanTramGiam = 'Phần trăm giảm phải là số nguyên từ 1 - 50';
                invalid = true;
            }

            if (item.errors.tongHoaDon) {
                invalid = true;
            }
        });

        if (invalid) {
            toastr["error"]("Nhập đúng các điều kiện trước đó để thêm điều kiện mới");
            return;
        }

        $scope.dotGiamGia.dieuKienRequests.push({
            errors: {},
            tongHoaDon: 0,
            phanTramGiam: 0
        });
    }

    $scope.removeDieuKien = function (index) {
        $scope.dotGiamGia.dieuKienRequests.splice(index, 1);
    }

    $scope.addDotGiamGia = function () {

        if ($scope.dotGiamGiaForm.$invalid) {
            return;
        }

        let invalid = false;

        $scope.dotGiamGia.dieuKienRequests.forEach(item => {
            if (!item.tongHoaDon) {
                item.errors.tongHoaDon = 'Tổng giá trị hóa đơn phải là số >= 0';
                invalid = true;
            }
            if (!item.phanTramGiam) {
                item.errors.phanTramGiam = 'Phần trăm giảm phải là số nguyên từ 1 - 50';
                invalid = true;
            }

            if (item.errors.tongHoaDon) {
                invalid = true;
            }
        });

        if (invalid) {
            toastr["error"]("Nhập đúng các điều kiện để thêm");
            return;
        }

        if ($scope.dotGiamGia.dieuKienRequests.length === 0) {
            toastr["error"]("Hãy thêm điều kiện trước khi thêm đợt giảm giá");
            return;
        }

        if ($scope.dotGiamGia.ngayBatDau.getHours() === 0) {
            $scope.dotGiamGia.ngayBatDau.setHours($scope.dotGiamGia.ngayBatDau.getHours() + 7);
        }

        if ($scope.dotGiamGia.ngayKetThuc.getHours() === 0) {
            $scope.dotGiamGia.ngayKetThuc.setHours($scope.dotGiamGia.ngayKetThuc.getHours() + 7);
        }

        Swal.fire({
            text: "Xác nhận thêm?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.isLoading = true;
                $http.post(host + '/rest/admin/dot-giam-gia', $scope.dotGiamGia)
                    .then(function (response) {
                        if (response.status === 200) {
                            toastr["success"]("Thêm thành công");
                            $scope.isLoading = false;
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
                        $scope.isLoading = false;
                    });
            }
        });
    }

});


app.controller("detailMauSacController", function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/rest/admin/mau-sac/' + id)
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
        let apiUrl = host + '/rest/admin/dot-giam-gia/search';
        let kmSearch = {};

        if ($scope.searchText.length > 0) {
            kmSearch.ten = ($scope.searchText + "").trim();
        } else {
            kmSearch.ten = null;
        }

        if ($scope.status === 1) {
            kmSearch.status = 1;
        } else if ($scope.status === 2) {
            kmSearch.status = 2;
        } else if ($scope.status === 3) {
            kmSearch.status = 3;
        } else if ($scope.status === 0) {
            kmSearch.status = 0;
        } else {
            kmSearch.status = 1;
        }


        kmSearch.ngayBatDau = $scope.tu;
        if ($scope.tu && kmSearch.ngayBatDau.getHours() === 0) {
            kmSearch.ngayBatDau.setHours(kmSearch.ngayBatDau.getHours() + 7);
        }
        kmSearch.ngayKetThuc = $scope.den;
        if ($scope.den && kmSearch.ngayKetThuc.getHours() === 0) {
            kmSearch.ngayKetThuc.setHours(kmSearch.ngayKetThuc.getHours() + 7);
        }

        kmSearch.currentPage = currentPage;
        kmSearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, kmSearch)
            .then(function (response) {
                if (response.data) {
                    $scope.dotGiamGias = response.data.content;
                    $scope.numOfPages = response.data.totalPages;
                    if ($scope.status === 0) {
                        $scope.dotGiamGias.forEach(item => {
                            const nbd = new Date(item.ngayBatDau);
                            const nkt = new Date(item.ngayKetThuc);
                            if (nbd <= new Date() <= nkt) {
                                item.hienThi = true;
                            }
                        })
                    }
                    $scope.isLoading = false;
                } else {
                    $scope.dotGiamGias = [];
                    toastr["error"]("Không có đợt giảm giá nào");
                }
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
            $scope.status = 1;
        } else {
            toastr["warning"]("Bạn đang không tìm kiếm");
        }
    }

    $scope.resetSearch();

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

    $scope.hienThiDotGiamGia = function (dotGiamGia) {
        const nbd = new Date(dotGiamGia.ngayBatDau);
        const nkt = new Date(dotGiamGia.ngayKetThuc);
        if (nbd <= new Date() <= nkt) {
            Swal.fire({
                text: "Xác nhận hiển thị đợt giảm giá?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy"
            }).then((result) => {
                $http.put(host + '/rest/admin/dot-giam-gia/hien-thi/' + dotGiamGia.id)
                    .then(function (response) {
                        toastr["success"]("Hiển thị thành công");
                        $scope.changeRadio(1);
                    })
                    .catch(function (error) {
                        if (error.status === 406) {
                            toastr["error"](error.data.data);
                        } else {
                            console.log(error);
                            toastr["error"]("Lấy dữ liệu thất bại");
                        }
                        $scope.isLoading = false;
                    });

            });
        } else {
            toastr["error"]("Đợt giảm giá đã kết thúc không được phép chỉnh sửa");
        }
    }

    $scope.anDotGiamGia = function (dotGiamGia) {
        const nbd = new Date(dotGiamGia.ngayBatDau);
        const nkt = new Date(dotGiamGia.ngayKetThuc);
        if (nbd <= new Date() <= nkt) {
            Swal.fire({
                text: "Xác nhận ẩn đợt giảm giá?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy"
            }).then((result) => {
                $http.put(host + '/rest/admin/dot-giam-gia/an/' + dotGiamGia.id)
                    .then(function (response) {
                        toastr["success"]("Ẩn thành công");
                        $scope.changeRadio(0);
                    })
                    .catch(function (error) {
                        if (error.status === 406) {
                            toastr["error"](error.data.data);
                        } else {
                            console.log(error);
                            toastr["error"]("Lấy dữ liệu thất bại");
                        }
                        $scope.isLoading = false;
                    });

            });
        } else {
            toastr["error"]("Đợt giảm giá đã kết thúc không được phép chỉnh sửa");
        }
    }

    $scope.detailDotGiamGia = function (val) {
        $scope.dotGiamGiaDetail = val;
    }

});

app.controller("updateDotGiamGiaController", function ($scope, $http, $location, $routeParams) {
    $scope.dotGiamGia = {};
    $scope.dotGiamGia.id = $routeParams.id;
    $scope.dotGiamGia.dieuKienRequests = [];

    let getCurrentDay = function () {
        const current = new Date();
        current.setHours(0);
        current.setMinutes(0);
        current.setMilliseconds(0);
        current.setSeconds(0);
        return current;
    }

    $http.get(host + '/rest/admin/dot-giam-gia/' + $scope.dotGiamGia.id)
        .then(function (response) {
            if (response.status === 200) {
                $scope.dotGiamGia = response.data;

                var ngayBatDau = $scope.dotGiamGia.ngayBatDau;
                var object = new Date(ngayBatDau);
                $scope.dotGiamGia.ngayBatDau = object;

                var ngayKetThuc = $scope.dotGiamGia.ngayKetThuc;
                var object = new Date(ngayKetThuc);
                $scope.dotGiamGia.ngayKetThuc = object;

                if ($scope.dotGiamGia.ngayBatDau <= getCurrentDay() && $scope.dotGiamGia.ngayKetThuc >= getCurrentDay()) {
                    toastr["warning"]("Đợt giảm giá đang diễn ra không được cập nhật");
                    $location.path("/list");
                }

                if ($scope.dotGiamGia.ngayKetThuc <= getCurrentDay()) {
                    toastr["warning"]("Đợt giảm giá đã kết thúc không được cập nhật");
                    $location.path("/list");
                }

                $scope.dotGiamGia.dieuKienRequests = $scope.dotGiamGia.dieuKienResponses;
                $scope.dotGiamGia.dieuKienRequests.forEach(item => {
                    item.errors = {};
                })

            }
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        });

    $scope.change = function (input) {
        input.$dirty = true;
    }

    $scope.checkNgayBatDau = function () {
        var currentDate = new Date();
        currentDate.setHours(0, 0, 0, 0); // Đặt giờ về 0 để so sánh ngày mà không tính giờ, phút, giây
        var ngayBatDau = new Date($scope.dotGiamGia.ngayBatDau);

        return ngayBatDau >= currentDate;
    };


    $scope.removeDieuKien = function (index) {
        $scope.dotGiamGia.dieuKienRequests.splice(index, 1);
    }


    $scope.updateDotGiamGia = function () {

        if ($scope.dotGiamGiaForm.$invalid) {
            return;
        }

        let invalid = false;

        $scope.dotGiamGia.dieuKienRequests.forEach(item => {
            if (!item.tongHoaDon) {
                item.errors.tongHoaDon = 'Tổng giá trị hóa đơn phải là số >= 0';
                invalid = true;
            }
            if (!item.phanTramGiam) {
                item.errors.phanTramGiam = 'Phần trăm giảm phải là số nguyên từ 1 - 50';
                invalid = true;
            }

            if (item.errors.tongHoaDon) {
                invalid = true;
            }
        });

        if (invalid) {
            toastr["error"]("Nhập đúng các điều kiện để thêm");
            return;
        }

        if ($scope.dotGiamGia.dieuKienRequests.length === 0) {
            toastr["error"]("Hãy thêm điều kiện trước khi thêm đợt giảm giá");
            return;
        }

        if ($scope.dotGiamGia.ngayBatDau.getHours() === 0) {
            $scope.dotGiamGia.ngayBatDau.setHours($scope.dotGiamGia.ngayBatDau.getHours() + 7);
        }

        if ($scope.dotGiamGia.ngayKetThuc.getHours() === 0) {
            $scope.dotGiamGia.ngayKetThuc.setHours($scope.dotGiamGia.ngayKetThuc.getHours() + 7);
        }

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
                $scope.isLoading = true;
                $http.put(host + '/rest/admin/dot-giam-gia/' + $scope.dotGiamGia.id, $scope.dotGiamGia)
                    .then(function (response) {
                        if (response.status === 200) {
                            toastr["success"]("Cập nhật thành công");
                        }
                        $location.path("/list");
                        $scope.isLoading = false;
                    })
                    .catch(function (error) {
                        if (error.status === 406) {
                            toastr["error"](error.data.data);
                            $location.path("/list");
                        }
                        if (error.status === 400) {
                            $scope.dotGiamGiaForm.ten.$dirty = false;
                            $scope.dotGiamGiaForm.ghiChu.$dirty = false;
                            $scope.errors = error.data;
                            toastr["error"]("Cập nhật thất bại");
                        }
                    });
                $scope.isLoading = false;
            }
        });
    }

    $scope.addDieuKien = function () {
        $scope.dotGiamGia.dieuKienResponses.push({
            errors: {},
            tongHoaDon: "",
            phanTramGiam: ""
        });
    }
});

