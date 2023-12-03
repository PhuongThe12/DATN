app.controller('hoaDonHoanController', function ($scope, $http, $location) {

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
        let apiUrl = host + '/rest/admin/hoa-don/get-all';

        if ($scope.searchText > 0) {
            hoaDonSearch.id = ($scope.searchText + "").trim();
        } else {
            hoaDonSearch.id = null;
        }

        if ($scope.status === 5) {
            hoaDonSearch.trangThai = 5;
        } else if ($scope.status === 6) {
            hoaDonSearch.trangThai = 6;
        } else {
            $scope.status = 5;
            hoaDonSearch.trangThai = 5;
        }


        hoaDonSearch.tuNgay = $scope.tu;

        hoaDonSearch.denNgay = $scope.den;

        hoaDonSearch.currentPage = currentPage;
        hoaDonSearch.pageSize = $scope.itemsPerPage;

        $http.post(apiUrl, hoaDonSearch)
            .then(function (response) {
                $scope.hoaDons = response.data.content;
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
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
        $scope.sanPhamLois = [];
        const hoaDon = $scope.hoaDons.find(item => item.id === id);
        console.log(hoaDon);
        if (hoaDon) {
            $scope.hoaDonDetail = hoaDon;
            $scope.hoaDonDetail.tienTraLai = 0;

            hoaDon.chiTietThanhToans.forEach(item => {
                $scope.hoaDonDetail.tienTraLai -= item.tienThanhToan;
            });

            $scope.hoaDonDetail.trangThai = hoaDon.trangThai === 5 ? -1 : 1;

        } else {
            toastr["error"]("Lấy dữ liệu thất bại. Vui lòng thử lại");
            document.getElementById('closeModal').click();
        }
    }

    $scope.submitLoi = function (sanPham, soLuongLoi) {
        if (!soLuongLoi) {
            toastr["error"]("Số lượng trả không được trống hoặc lớn hơn số lượng ban đầu(" + sanPham.soLuong + ")");
            return;
        }


        let sanPhamTraExist = $scope.sanPhamLois.find(item => item.id === sanPham.id);
        if (sanPhamTraExist) {
            sanPhamTraExist.soLuongLoi = soLuongLoi;
        } else {
            $scope.sanPhamLois.push(angular.copy(sanPham))
        }

    }

    $scope.xacNhanHoanDon = function () {
        Swal.fire({
            text: "Xác nhận đã hoàn đơn?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {

                let request = {
                    idHoaDon: $scope.hoaDonDetail.id,
                    chiTietHoaDons: []
                }

                $scope.sanPhamLois.forEach(item => {
                    request.chiTietHoaDons.push({
                        id: item.id,
                        bienTheGiay: {
                            id: item.bienTheGiay.id
                        },
                        soLuong: item.soLuongLoi,
                        trangThai: 0
                    });
                });

                $scope.hoaDonDetail.hoaDonChiTietResponses.forEach(item => {
                    const hoaDonLoi = request.chiTietHoaDons.find(ct => ct.id === item.id && ct.bienTheGiay.id === item.bienTheGiay.id);
                    if (hoaDonLoi) {
                        if (item.soLuong - hoaDonLoi.soLuong > 0) {
                            request.chiTietHoaDons.push({
                                id: item.id,
                                bienTheGiay: {
                                    id: item.bienTheGiay.id
                                },
                                soLuong: item.soLuong - hoaDonLoi.soLuong,
                                trangThai: 1
                            });
                        }
                    } else {
                        request.chiTietHoaDons.push({
                            idHoaDon: item.id,
                            bienTheGiay: {
                                id: item.bienTheGiay.id
                            },
                            soLuong: item.soLuong,
                            trangThai: 1
                        });
                    }

                });

                console.log(request);
                $http.post(host + "/rest/admin/hoa-don/xac-nhan-hoan", request)
                    .then((response) => {
                        toastr["success"]("Đã hoàn thành hóa đơn");
                        $scope.changeRadio(6);
                    })
                    .catch((error) => {
                        if (error.status === 409) {
                            toastr["error"](error.data.data);
                        } else {
                            toastr["error"]("Có lỗi xảy ra vui lòng thử lại");
                        }
                        $scope.changeRadio(5);
                    })
            }
        });
    }

});