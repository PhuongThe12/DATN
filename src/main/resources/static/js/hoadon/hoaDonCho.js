app.controller('hoaDonChoController', function ($scope, $http, $location) {
    $scope.hoaDons = [];
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

        if ($scope.status === 2) {
            hoaDonSearch.trangThai = 2;
        } else if ($scope.status === 3) {
            hoaDonSearch.trangThai = 3;
        } else if ($scope.status === 4) {
            hoaDonSearch.trangThai = 4;
        } else {
            $scope.status = 4;
            hoaDonSearch.trangThai = 4;
        }

        if ($scope.status === 3) {
            apiUrl = host + '/rest/admin/hoa-don/get-all-order-ngay-ship';
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
                    $scope.hoaDonDetail.trangThai = item.trangThai;
                }
            });
            $scope.hoaDonDetail.tongTru = hoaDon.tienGiam ? hoaDon.tienGiam : 0;
            $scope.hoaDonDetail.tienShip = hoaDon.phiShip ? hoaDon.phiShip : 0;

            $scope.hoaDonDetail.tongCong = $scope.hoaDonDetail.conLai + $scope.hoaDonDetail.tienShip + $scope.hoaDonDetail.tongTru;
            console.log($scope.hoaDons, $scope.hoaDonDetail.phiShip, $scope.hoaDonDetail.tongCong);

        } else {
            toastr["error"]("Lấy dữ liệu thất bại. Vui lòng thử lại");
            document.getElementById('closeModal').click();
        }
    }

    $scope.$watch('all', function () {
        $scope.hoaDons.forEach(item => {
            item.selected = $scope.all;
        });
    })

    $scope.selectHoaDonCho = function (id) {
        const hoaDon = $scope.hoaDons.find(item => item.id === id);
        if (hoaDon) {
            hoaDon.selected = true;
            document.getElementById('closeModal').click();
        } else {
            toastr["error"]("Lấy dữ liệu thất bại");
        }
    }

    $scope.xacNhan = function () {
        const hoaDonSelecteds = $scope.hoaDons.filter(item => item.selected === true).map(item => item.id);

        if (hoaDonSelecteds.length === 0) {
            toastr["warning"]("Bạn chưa chọn hóa đơn nào");
            return;
        }

        Swal.fire({
            text: "Xác nhận " + hoaDonSelecteds.length + " hóa đơn?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                console.log(hoaDonSelecteds);
                $http.post(host + "/rest/admin/hoa-don/xac-nhan-don-hang", hoaDonSelecteds)
                    .then((response) => {
                        if (Number.isInteger(response.data)) {
                            if (response.data === hoaDonSelecteds.length) {
                                toastr["success"]("Đã xác nhận " + hoaDonSelecteds.length + " hóa đơn");
                            } else {
                                toastr["success"]("Đã xác nhận " + hoaDonSelecteds.length + " hóa đơn. Các hóa đơn khác đã được xử lý trước đó");
                            }
                            $scope.changeRadio(2);
                        } else {
                            toastr["error"]("Có lỗi. Vui lòng thử lại");
                        }
                    })
                    .catch(err => {
                        toastr["error"](err.data.data);
                    })
            }

        });
    }

    $scope.detailTraMotPhan = function (hoaDon) {
        $scope.hoaDonTra = hoaDon;
        $scope.sanPhamTras = [];
    }

    $scope.sanPhamTras = [];
    $scope.submitTra = function (sanPham, soLuongTra) {
        if (!soLuongTra) {
            toastr["error"]("Số lượng trả không được trống hoặc lớn hơn số lượng ban đầu(" + sanPham.soLuong + ")");
            return;
        }


        let sanPhamTraExist = $scope.sanPhamTras.find(item => item.id === sanPham.id);
        if (sanPhamTraExist) {
            sanPhamTraExist.soLuongTra = soLuongTra;
        } else {
            $scope.sanPhamTras.push(angular.copy(sanPham))
        }

    }

    $scope.deleteSanPhamTra = function (id) {

        Swal.fire({
            text: "Xác nhận xóa sản phẩm khỏi danh sách sản phẩm trả?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                const index = $scope.sanPhamTras.findIndex(item => item.id === id);
                if (index !== -1) {
                    $scope.sanPhamTras.splice(index, 1);
                    setTimeout(() => {
                        document.getElementById('noneButton').click();
                    }, 0);
                } else {
                    toastr["error"]("Có lỗi xảy ra. Vui lòng thử lại");
                }
            }
        });
    }

    $scope.xacNhanTraMotPhan = function () {

        if ($scope.sanPhamTras.length === 0) {
            toastr["error"]("Chưa có sản phẩm trả. Không thể xác nhận");
            return;
        }

        let soLuongSanPhamGoc = 0;
        $scope.hoaDonTra.hoaDonChiTietResponses.forEach(item => {
            soLuongSanPhamGoc += item.soLuong;
        })

        let soLuongTra = 0;
        $scope.sanPhamTras.forEach(item => {
            soLuongTra += item.soLuongTra;
        })

        if (soLuongTra >= soLuongSanPhamGoc) {
            toastr["error"]("Tổng số lượng trả phải nhỏ hơn tổng số lượng sản phẩm");
            return;
        }

        Swal.fire({
            text: "Xác nhận. Đơn hàng sẽ được hoàn trả một phẩn?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {

                let request = {
                    idHoaDon: $scope.hoaDonTra.id,
                    chiTietHoaDons: []
                }

                $scope.sanPhamTras.forEach(item => {
                    request.chiTietHoaDons.push({
                        id: item.id,
                        bienTheGiay: {
                            id: item.bienTheGiay.id
                        },
                        soLuongTra: item.soLuongTra,
                        ghiChu: item.ghiChu
                    });
                });

                $http.post(host + "/rest/admin/hoa-don/tra-mot-phan", request)
                    .then((response) => {
                        if (Number.isInteger(response.data)) {
                            toastr["success"]("Đã hoàn thành hóa đơn");
                            if($scope.hoaDons.length > 1) {
                                $scope.changeRadio(3);
                            } else {
                                $scope.hoaDons = [];
                            }
                            document.getElementById('closeModalTraMotPhan').click();
                        }

                    })
                    .catch((error) => {
                        if (error.status === 409) {
                            toastr["error"](error.data.data);
                        } else {
                            toastr["error"]("Có lỗi xảy ra vui lòng thử lại");
                        }
                    })
            }
        });
    }

    $scope.giaoHang = function () {
        const hoaDonSelecteds = $scope.hoaDons.filter(item => item.selected === true).map(item => item.id);

        if (hoaDonSelecteds.length === 0) {
            toastr["warning"]("Bạn chưa chọn hóa đơn nào");
            return;
        }

        Swal.fire({
            text: "Xác nhận giao " + hoaDonSelecteds.length + " hóa đơn?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                console.log(hoaDonSelecteds);
                $http.post(host + "/rest/admin/hoa-don/xac-nhan-giao-hang", hoaDonSelecteds)
                    .then((response) => {
                        if (Number.isInteger(response.data)) {
                            if (response.data === hoaDonSelecteds.length) {
                                toastr["success"]("Đã xác nhận giao " + hoaDonSelecteds.length + " hóa đơn");
                            } else {
                                toastr["success"]("Đã xác nhận giao " + hoaDonSelecteds.length + " hóa đơn. Các hóa đơn khác đã được xử lý trước đó");
                            }
                            $scope.changeRadio(3);
                        } else {
                            toastr["error"]("Có lỗi. Vui lòng thử lại");
                        }
                    })
                    .catch(err => {
                        toastr["error"](err.data.data);
                    })
            }

        });
    }

    $scope.hoanThanh = function () {
        const hoaDonSelecteds = $scope.hoaDons.filter(item => item.selected === true).map(item => item.id);

        if (hoaDonSelecteds.length === 0) {
            toastr["warning"]("Bạn chưa chọn hóa đơn nào");
            return;
        }

        Swal.fire({
            text: "Xác nhận hoàn thành " + hoaDonSelecteds.length + " hóa đơn?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                console.log(hoaDonSelecteds);
                $http.post(host + "/rest/admin/hoa-don/hoan-thanh-don-hang", hoaDonSelecteds)
                    .then((response) => {
                        if (Number.isInteger(response.data)) {
                            if (response.data === hoaDonSelecteds.length) {
                                toastr["success"]("Đã hoàn thành " + hoaDonSelecteds.length + " hóa đơn");
                            } else {
                                toastr["success"]("Đã hoàn thành giao " + hoaDonSelecteds.length + " hóa đơn. Các hóa đơn khác đã được xử lý trước đó");
                            }
                            $scope.changeRadio(3);
                        } else {
                            toastr["error"]("Có lỗi. Vui lòng thử lại");
                        }
                    })
                    .catch(err => {
                        toastr["error"](err.data.data);
                    })
            }
        });
    }

    $scope.selectOptions = [
        {value: 1, label: 'Khách từ chối nhận hàng'},
        {value: 2, label: 'Khách yêu cầu hủy đơn hàng'},
        {value: 3, label: 'Cửa hàng đã hết hàng'},
        {value: 4, label: 'Khác'}
    ];

    $scope.changeLyDo = function (hoaDon) {
        const option = $scope.selectOptions.find(item => item.value === hoaDon.selectForm);
        if (option.value === 4) {
            hoaDon.ghiChu = '';
        } else {
            hoaDon.ghiChu = option.label;
        }
    }

    $scope.huy = function () {
        const filterHoaDons = $scope.hoaDons.filter(item => item.selected === true);
        const hoaDonSelecteds = filterHoaDons.map(item => item.id);

        if (hoaDonSelecteds.length === 0) {
            toastr["warning"]("Bạn chưa chọn hóa đơn nào");
            return;
        }

        filterHoaDons.forEach(item => {
            item.selectForm = 1;
            $scope.changeLyDo(item);
        })

        document.getElementById('modalHuyButton').click();
    }

    $scope.xacNhanHuy = function () {
        const filterHoaDons = $scope.hoaDons.filter(item => item.selected === true);
        const hoaDonSelecteds = filterHoaDons.map(item => item.id);

        if (hoaDonSelecteds.length === 0) {
            toastr["warning"]("Bạn chưa chọn hóa đơn nào");
            return;
        }

        let error = false;
        let request = [];
        filterHoaDons.forEach(item => {
            if (item.selected === true && item.ghiChu && item.ghiChu.length > 0) {
                request.push({
                    id: item.id,
                    ghiChu: item.ghiChu
                });
            } else {
                error = true;
            }
        });

        if (error) {
            toastr["warning"]("Hãy điền đầy đủ lý do hủy đơn");
            return;
        }
        Swal.fire({
            text: "Xác nhận hủy " + hoaDonSelecteds.length + " hóa đơn?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                console.log(hoaDonSelecteds);
                $http.post(host + "/rest/admin/hoa-don/huy-don-hang", request)
                    .then((response) => {
                        if (Number.isInteger(response.data)) {
                            if (response.data === hoaDonSelecteds.length) {
                                toastr["success"]("Đã hủy " + hoaDonSelecteds.length + " hóa đơn");
                            } else {
                                toastr["success"]("Đã hủy " + hoaDonSelecteds.length + " hóa đơn. Các hóa đơn khác đã được xử lý trước đó");
                            }
                            $scope.changeRadio($scope.status);
                            document.getElementById('closeModalHuy').click();
                        } else {
                            toastr["error"]("Có lỗi. Vui lòng thử lại");
                        }
                    })
                    .catch(err => {
                        toastr["error"](err.data.data);
                    })
            }
        });

    }


});