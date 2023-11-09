app.controller('giayListController', function ($scope, $http, $location) {

    $scope.giays = [];

    $http.post(host + '/admin/rest/giay/get-all-giay', {})
        .then(function (response) {
            $scope.giays = response.data.content;
            $scope.numOfPages = response.data.totalPages;
        })
        .catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            // window.location.href = feHost + '/trang-chu';
        });

    $scope.showVarients = {};
    $scope.toggleVarients = function (index) {
        $scope.showVarients[index] = !$scope.showVarients[index];
    }

    $scope.focusInputModal = function (modal, giayIndex, bienTheIndex) {
        if (modal === 'soLuong') {
            $scope.giayModal = angular.copy($scope.giays[giayIndex]);
            $scope.giayModal.lstBienTheGiay.forEach(detailModal => {
                detailModal.errors = {};
            });
            // $scope.giayModal.index = giayIndex;
            setTimeout(function () {
                document.getElementById('detailModalSoLuong' + bienTheIndex).focus();
            }, 524);
        } else if (modal === 'giaNhap') {
            $scope.giaGiayModal = angular.copy($scope.giays[giayIndex]);
            $scope.giaGiayModal.lstBienTheGiay.forEach(giaGiayModal => {
                giaGiayModal.errors = {};
            });
            setTimeout(function () {
                document.getElementById('detailModalGiaNhap' + bienTheIndex).focus();
            }, 524);
        } else if (modal === 'giaBan') {
            $scope.giaGiayModal = angular.copy($scope.giays[giayIndex]);
            $scope.giaGiayModal.lstBienTheGiay.forEach(giaGiayModal => {
                giaGiayModal.errors = {};
            });
            setTimeout(function () {
                document.getElementById('detailModalGiaBan' + bienTheIndex).focus();
            }, 524);
        }


    }

    $scope.changeInputModal = function (modal, bienTheIndex) {
        if (modal === 'soLuong') {
            if (isNaN($scope.giayModal.lstBienTheGiay[bienTheIndex].soLuong) ||
                ($scope.giayModal.lstBienTheGiay[bienTheIndex].soLuong + "").trim().length === 0 ||
                !$scope.giayModal.lstBienTheGiay[bienTheIndex].soLuong) {
                $scope.giayModal.lstBienTheGiay[bienTheIndex].soLuong = null;
                $scope.giayModal.lstBienTheGiay[bienTheIndex].errors.soLuong = 'Không được để trống số lượng';
            } else if ($scope.giayModal.lstBienTheGiay[bienTheIndex].soLuong < 0) {
                $scope.giayModal.lstBienTheGiay[bienTheIndex].soLuong = null;
                $scope.giayModal.lstBienTheGiay[bienTheIndex].errors.soLuong = 'Số lượng không được âm';
            } else {
                $scope.giayModal.lstBienTheGiay[bienTheIndex].errors.soLuong = null;
            }
        } else if (modal === 'giaNhap') {
            if (isNaN($scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaNhap) ||
                ($scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaNhap + "").trim().length === 0 ||
                !$scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaNhap) {
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaNhap = null;
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].errors.giaNhap = 'Không được để trống số lượng';
            } else if ($scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaNhap < 0) {
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaNhap = null;
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].errors.giaNhap = 'Số lượng không được âm';
            } else {
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].errors.giaNhap = null;
            }
        } else if (modal === 'giaBan') {
            if (isNaN($scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaBan) ||
                ($scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaBan + "").trim().length === 0 ||
                !$scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaBan) {
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaBan = null;
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].errors.giaBan = 'Không được để trống số lượng';
            } else if ($scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaBan < 0) {
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].giaBan = null;
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].errors.giaBan = 'Số lượng không được âm';
            } else {
                $scope.giaGiayModal.lstBienTheGiay[bienTheIndex].errors.giaBan = null;
            }

        }
    }

    $scope.setAllForUpdateSoLuong = function () {
        if (!$scope.soLuongUpdateAll && !$scope.giayModal) {
            return;
        }
        $scope.giayModal.lstBienTheGiay.forEach(bienThe => {
            bienThe.soLuong = $scope.soLuongUpdateAll;
        });

    }

    $scope.changeInputSetAllForUpdate = function () {
        if (isNaN($scope.soLuongUpdateAll) ||
            ($scope.soLuongUpdateAll + "").trim().length === 0 ||
            !$scope.soLuongUpdateAll) {
            $scope.soLuongUpdateAll = null;
            $scope.soLuongUpdateAllErrors = 'Không được để trống số lượng';
        } else if ($scope.soLuongUpdateAll < 0) {
            $scope.soLuongUpdateAll = null;
            $scope.soLuongUpdateAllErrors = 'Số lượng không được âm';
        } else {
            $scope.soLuongUpdateAllErrors = null;
        }
    }

    $scope.changeInputSetAllForUpdateGia = function (modal) {
        if (modal === 'giaBan') {
            if (isNaN($scope.giaBanUpdateAll) ||
                ($scope.giaBanUpdateAll + "").trim().length === 0 ||
                !$scope.giaBanUpdateAll) {
                $scope.giaBanUpdateAll = null;
                $scope.giaBanUpdateAllErrors = 'Không được để trống số lượng';
            } else if ($scope.giaBanUpdateAll < 0) {
                $scope.giaBanUpdateAll = null;
                $scope.giaBanUpdateAllErrors = 'Số lượng không được âm';
            } else {
                $scope.giaBanUpdateAllErrors = null;
            }
        } else if (modal === 'giaNhap') {
            if (isNaN($scope.giaNhapUpdateAll) ||
                ($scope.giaNhapUpdateAll + "").trim().length === 0 ||
                !$scope.giaNhapUpdateAll) {
                $scope.giaNhapUpdateAll = null;
                $scope.giaNhapUpdateAllErrors = 'Không được để trống số lượng';
            } else if ($scope.giaNhapUpdateAll < 0) {
                $scope.giaNhapUpdateAll = null;
                $scope.giaNhapUpdateAllErrors = 'Số lượng không được âm';
            } else {
                $scope.giaNhapUpdateAllErrors = null;
            }
        }
    }

    $scope.blurGiaInput = function (index, modal) {
        if(modal === 'giaBan') {
            if($scope.giaGiayModal.lstBienTheGiay[index].giaBan && $scope.giaGiayModal.lstBienTheGiay[index].giaNhap &&
                $scope.giaGiayModal.lstBienTheGiay[index].giaBan < $scope.giaGiayModal.lstBienTheGiay[index].giaNhap) {
                $scope.giaGiayModal.lstBienTheGiay[index].giaBan = null;
                $scope.giaGiayModal.lstBienTheGiay[index].errors.giaBan = 'Giá bán không được nhỏ hơn giá nhập';
            }
        } else if(modal === 'giaNhap') {
            if($scope.giaGiayModal.lstBienTheGiay[index].giaBan && $scope.giaGiayModal.lstBienTheGiay[index].giaNhap &&
                $scope.giaGiayModal.lstBienTheGiay[index].giaBan < $scope.giaGiayModal.lstBienTheGiay[index].giaNhap) {
                $scope.giaGiayModal.lstBienTheGiay[index].giaNhap = null;
                $scope.giaGiayModal.lstBienTheGiay[index].errors.giaNhap = 'Giá bán không được nhỏ hơn giá nhập';
            }
        }
    }

    $scope.setAllForUpdateGia = function () {
        if (!$scope.giaNhapUpdateAll || $scope.giaBanUpdateAll) {
            toastr["error"]("Lỗi, hãy điền đầy đủ thông tin bắt buộc");
            return;
        }

        if ($scope.giaBanUpdateAll < $scope.giaNhapUpdateAll) {
            toastr["error"]("Lỗi, giá nhập không được lớn hơn giá bán");
            return;
        }
        $scope.giaGiayModal.lstBienTheGiay.forEach(bienThe => {
            bienThe.giaBan = $scope.giaBanUpdateAll;
            bienThe.giaNhap = $scope.giaNhapUpdateAll;
        });

    }

    $scope.updateSoLuong = function () {
        let giayChange = {
            id: $scope.giayModal.id,
            bienTheGiays: $scope.giayModal.lstBienTheGiay.map(bienThe => {
                return {
                    id: bienThe.id,
                    soLuong: bienThe.soLuong
                };
            })
        };

        if (!giayChange.id || giayChange.bienTheGiays.some(bienThe => !bienThe.id || !bienThe.soLuong || isNaN(bienThe.soLuong))) {
            toastr["error"]("Lỗi, hãy điền đầy đủ thông tin bắt buộc");
            return;
        }

        $http.put(host + '/admin/rest/giay/update-so-luong', JSON.stringify(giayChange))
            .then(function (response) {
                toastr["success"]("Cập nhật thành công");
                document.getElementById('closeModalSoLuong').click();
                const g = $scope.giays.find(giay => giay.id === giayChange.id)
                if (g) {
                    g.lstBienTheGiay.forEach(bienThe => {
                        giayChange.bienTheGiays.forEach(bienTheChange => {
                            if (bienTheChange.id === bienThe.id) {
                                bienThe.soLuong = bienTheChange.soLuong;
                            }
                        })
                    })
                }

            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Cập nhật thất bại! Lỗi đọc dữ liệu");
            });
    }

    $scope.updateGia = function () {
        let giayChange = {
            id: $scope.giaGiayModal.id,
            bienTheGiays: $scope.giaGiayModal.lstBienTheGiay.map(bienThe => {
                return {
                    id: bienThe.id,
                    giaNhap: bienThe.giaNhap,
                    giaBan: bienThe.giaBan
                };
            })
        };

        if (!giayChange.id || giayChange.bienTheGiays.some(bienThe => !bienThe.id || !bienThe.giaNhap || isNaN(bienThe.giaNhap))
            || giayChange.bienTheGiays.some(bienThe => !bienThe.id || !bienThe.giaBan || isNaN(bienThe.giaBan))) {
            toastr["error"]("Lỗi, hãy điền đầy đủ thông tin bắt buộc");
            return;
        }

        $http.put(host + '/admin/rest/giay/update-gia', JSON.stringify(giayChange))
            .then(function (response) {
                toastr["success"]("Cập nhật thành công");
                document.getElementById('closeModalGia').click();
                const g = $scope.giays.find(giay => giay.id === giayChange.id)
                if (g) {
                    g.lstBienTheGiay.forEach(bienThe => {
                        giayChange.bienTheGiays.forEach(bienTheChange => {
                            if (bienTheChange.id === bienThe.id) {
                                bienThe.giaNhap = bienTheChange.giaNhap;
                                bienThe.giaBan = bienTheChange.giaBan;
                            }
                        })
                    })
                }

            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Cập nhật thất bại! Lỗi đọc dữ liệu");
            });
    }

});
