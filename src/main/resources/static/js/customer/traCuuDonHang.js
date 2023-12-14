app.controller('traCuuDonHangController', function ($scope, $http, $location) {

    $scope.errors = {};

    // "http://localhost:8080/home#/tra-cuu-don-hang?maHD=" + idHoaDon + "&sdt=" + 4soCuoiSDTNhan

    let container = [];
    window.location.hash.split('&').toString().substr(1).split(",").forEach(item => {
        container[item.split("=")[0]] = decodeURIComponent(item.split("=")[1]) ? item.split("=")[1] : "No query strings available";
    });

    if(Object.keys(container).length === 2) {
        $scope.maHD = container['/tra-cuu-don-hang?maHD'];
        $scope.sdt = container['sdt'];
        timKiemHoaDon();
    }

    $scope.changeInput = function (input) {
        if (input === 'maHD') {
            if ($scope.maHD && Number($scope.maHD)) {
                $scope.errors.maHD = null;
            } else {
                $scope.errors.maHD = 'Mã hóa đơn không hợp lệ';
            }
        } else {
            if ($scope.sdt && Number($scope.sdt) && $scope.sdt.length === 4) {
                $scope.errors.sdt = null;
            } else {
                $scope.errors.sdt = '4 số cuối sdt không hợp lệ';
            }
        }
    }

    $scope.timKiemHoaDon = function () {
        timKiemHoaDon();
    }

    function timKiemHoaDon() {
        if ($scope.maHD && $scope.sdt && Number($scope.sdt) && Number($scope.maHD) && $scope.sdt.length === 4) {

            $scope.hoaDonDetail = {};
            $http.get(host + "/rest/admin/hoa-don/get-tra-cuu-don?maHD=" + $scope.maHD + "&sdt=" + $scope.sdt)
                .then(response => {
                    if(!response.data) {
                        toastr["error"]("Không tìm thấy hóa đơn nào");
                        $scope.hoaDonDetail = null;
                        return;
                    }
                    $scope.hoaDonDetail = response.data;
                    $scope.hoaDonDetail.conLai = 0;

                    $scope.hoaDonDetail.thongTinThanhToan = {
                        show: false
                    };
                    $scope.hoaDonDetail.chiTietThanhToans.forEach(item => {
                        $scope.hoaDonDetail.conLai += item.tienThanhToan;
                        $scope.hoaDonDetail.thongTinThanhToan.show = true;
                        if (item.hinhThucThanhToan === 1) {
                            $scope.hoaDonDetail.thongTinThanhToan.tienMat = item.tienThanhToan;
                        }
                        if (item.hinhThucThanhToan === 2) {
                            $scope.hoaDonDetail.thongTinThanhToan.chuyenKhoan = item.tienThanhToan;
                            $scope.hoaDonDetail.thongTinThanhToan.maGiaoDich = item.maGiaoDich;
                        }
                    });
                    $scope.hoaDonDetail.tongTru = $scope.hoaDonDetail.tienGiam ? $scope.hoaDonDetail.tienGiam : 0;
                    $scope.hoaDonDetail.tienShip = $scope.hoaDonDetail.tienShip ? $scope.hoaDonDetail.tienShip : 0;

                    $scope.hoaDonDetail.tongCong = $scope.hoaDonDetail.conLai + $scope.hoaDonDetail.tongTru + $scope.hoaDonDetail.tienShip;

                })
                .catch(err => {
                    console.log(err);
                    toastr["error"]("Lỗi khi tìm kiếm. Vui lòng thử lại");
                })

        } else {
            toastr["error"]("Bạn hãy nhập đủ thông tin trước khi tìm kiếm");
            $scope.hoaDonDetail = null;
        }
    }

});