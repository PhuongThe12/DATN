app.controller("updateKhuyenMaiController", function ($scope, $http, $location, $routeParams) {
    let getCurrentDay = function () {
        const current = new Date();
        current.setHours(0);
        current.setMinutes(0);
        current.setMilliseconds(0);
        current.setSeconds(0);
        return current;
    }

    $scope.thuongHieus = [];
    $scope.selectedThuongHieu = [];
    $scope.items = [];
    $scope.allGiay = [];
    $scope.giays = [];
    $scope.khuyenMai = {};
    $scope.khuyenMai.khuyenMaiChiTietRequests = [];
    $scope.khuyenMai.ngayBatDau = getCurrentDay();
    $scope.errors = {};

    const id = $routeParams.id;

    $http.get(host + '/rest/admin/thuong-hieu/get-all')
        .then(function (response) {
            $scope.thuongHieus = response.data;
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        });

    $scope.selectedGiay = [];
    $scope.selectedGiayTableData = [];

    $scope.giayCu = [];

    $scope.init = function () {
        $http.get(host + '/rest/admin/khuyen-mai/giay/' + id)
            .then(function (response) {
                if (response.status === 200) {
                    $scope.khuyenMaiDetail = response.data;

                    const ngayBatDau = $scope.khuyenMaiDetail.ngayBatDau;
                    const ngayBatDauDate = new Date(ngayBatDau);
                    $scope.khuyenMaiDetail.ngayBatDau = ngayBatDauDate;

                    const ngayKetThuc = $scope.khuyenMaiDetail.ngayKetThuc;
                    const ngayKetThucDate = new Date(ngayKetThuc);
                    $scope.khuyenMaiDetail.ngayKetThuc = ngayKetThucDate;


                    if ($scope.khuyenMaiDetail.ngayBatDau <= getCurrentDay() && $scope.khuyenMaiDetail.ngayKetThuc >= getCurrentDay()) {
                        toastr["warning"]("Khuyến mại đang diễn ra không được cập nhật");
                        $location.path("/list");
                    }

                    if ($scope.khuyenMaiDetail.ngayKetThuc <= getCurrentDay()) {
                        toastr["warning"]("Khuyến mại đã kết thúc không được cập nhật");
                        $location.path("/list");
                    }

                    $scope.khuyenMai = {
                        id: $scope.khuyenMaiDetail.id,
                        ten: $scope.khuyenMaiDetail.ten,
                        ngayBatDau: $scope.khuyenMaiDetail.ngayBatDau,
                        ngayKetThuc: $scope.khuyenMaiDetail.ngayKetThuc,
                        ghiChu: $scope.khuyenMaiDetail.ghiChu,
                        trangThai: $scope.khuyenMaiDetail.trangThai,
                    }

                    angular.forEach($scope.khuyenMaiDetail.khuyenMaiChiTietResponses.giays, function (giay) {
                        $scope.selectedGiay.push(giay.id);
                        giay.selected = true;
                        $scope.selectedGiayTableData.push(giay);
                        $scope.giayCu.push(giay);
                    });
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    };

    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.changeGhiChu = function (input) {
        input.$dirty = true;
    }

    let kmSearch = {};

    $scope.validateNgay = function () {

        if ($scope.khuyenMai.ngayBatDau) {
            if ($scope.khuyenMai.ngayBatDau < getCurrentDay()) {
                $scope.errors.ngayBatDau = 'Ngày bắt đầu không được là ngày trong quá khứ';
                return false;
            }
        } else {
            $scope.errors.ngayBatDau = 'Ngày bắt đầu không được để trống';
            return false;
        }

        if (!$scope.khuyenMai.ngayKetThuc) {
            $scope.errors.ngayKetThuc = 'Ngày kết thúc không được để trống';
            return false;
        }

        if ($scope.khuyenMai.ngayBatDau >= $scope.khuyenMai.ngayKetThuc) {
            $scope.errors.ngayKetThuc = 'Ngày kết thúc phải lớn ngày bắt đầu';
            return false;
        }

        if ($scope.khuyenMai.ngayBatDau && $scope.khuyenMai.ngayKetThuc) {
            $scope.errors.ngayKetThuc = null;
            $scope.errors.ngayBatDau = null;
            return true
        }
    }

    $scope.loadGiay = function () {

        const valid = $scope.validateNgay();

        if (!valid) {
            toastr["warning"]("Kiểm tra lại ngày bắt đầu và ngày kết thúc");
            return;
        }
        if ($scope.khuyenMai.ngayBatDau && $scope.khuyenMai.ngayKetThuc) {
            kmSearch.ngayBatDau = new Date($scope.khuyenMai.ngayBatDau);
            if (kmSearch.ngayBatDau.getHours() === 0) {
                kmSearch.ngayBatDau.setHours($scope.khuyenMai.ngayBatDau.getHours() + 7);
            }
            kmSearch.ngayKetThuc = new Date($scope.khuyenMai.ngayKetThuc);
            if (kmSearch.ngayKetThuc.getHours() === 0) {
                kmSearch.ngayKetThuc.setHours($scope.khuyenMai.ngayKetThuc.getHours() + 7);
            }
        }

        if (document.getElementById('modalSP')) {
            setTimeout(() => {
                document.getElementById('modalSP').click();
            }, 0);
        }
        $scope.isLoading = true;
        $http.post('http://localhost:8080/rest/admin/giay/get-all-without-discount', kmSearch)
            .then(function (response) {
                $scope.allGiay = response.data;
                if (Array.isArray($scope.giayCu)) {
                    $scope.giayCu.forEach(inserted => {
                        if ($scope.allGiay.findIndex(item => item.id === inserted.id) === -1) {
                            $scope.allGiay.push(inserted);
                        }
                    });
                }

                $scope.giays = $scope.allGiay;
                $scope.isLoading = false;
                angular.forEach($scope.giays, function (giay) {
                    giay.selected = $scope.selectedGiay.includes(giay.id);
                    giay.disabled = giay.selected;
                });

            })
            .catch(function (error) {
                console.error('Error fetching data: ' + error);
                $scope.isLoading = false;
            });
    };

    $scope.search = function () {

        if ($scope.selectedThuongHieu.length === 0 && !$scope.searchText) {
            toastr["warning"]("Chưa có nội dung tìm kiếm");
            return;
        }

        $scope.giays = [];

        $scope.allGiay.forEach(item => {
            if (item.ten.includes($scope.searchText) || !$scope.searchText) {
                if ($scope.selectedThuongHieu.length === 0) {
                    $scope.giays.push(item);
                } else {
                    for (const thuongHieu in $scope.selectedThuongHieu) {
                        if (item.thuongHieu.ten.includes($scope.selectedThuongHieu[thuongHieu].ten)) {
                            $scope.giays.push(item);
                            break;
                        }
                    }
                }
            }
        });
        $scope.searching = true;
    }

    $scope.resetSearch = function () {
        $scope.searchText = '';
        if ($scope.selectedThuongHieu) {
            $scope.selectedThuongHieu.forEach((thuongHieu, index) => {
                $scope.selectedThuongHieu.splice(index, 1);
                $scope.thuongHieus.push(thuongHieu)
            });
        }
        if ($scope.searching) {
            $scope.giays = $scope.allGiay;
            $scope.searching = false;
        }
    }

    $scope.selectAllGiay = function () {
        // Kiểm tra xem tất cả các checkbox đã được chọn và disabled chưa
        var allSelectedAndDisabled = $scope.giays.every(function (giay) {
            return giay.selected && giay.disabled;
        });
        // Nếu tất cả đã được chọn và disabled, không thay đổi trạng thái
        if (!allSelectedAndDisabled) {
            angular.forEach($scope.giays, function (giay) {
                giay.selected = $scope.selectAll;
            });
        }

    };

    $scope.$watch('giays', function (newGiays, oldGiays) {
        // Kiểm tra xem tất cả các checkbox trong tbody đã được chọn và không bị disabled chưa
        var allSelectedAndNotDisabled = newGiays.every(function (giay) {
            return giay.selected && !giay.disabled;
        });

        // Nếu tất cả đã được chọn và không bị disabled, cập nhật trạng thái cho checkbox ở thead
        if (allSelectedAndNotDisabled) {
            $scope.selectAll = true;
        } else {
            $scope.selectAll = false;
        }
    }, true);

    $scope.xacNhan = function () {
        $scope.isLoading = true;

        const newTableData = $scope.allGiay.filter(item =>
            item.selected && !item.disabled
        );

        newTableData.forEach(item => {
            $scope.selectedGiay.push(item.id);
            if (Array.isArray(item.lstBienTheGiay)) {
                item.lstBienTheGiay.forEach(bt => {
                    bt.phanTramGiam = 0;
                })
            }
        });

        $scope.selectedGiayTableData = [...$scope.selectedGiayTableData, ...newTableData];
        $scope.isLoading = false;
    };

    $scope.blurInput = function (bienTheGiay) {
        if (bienTheGiay.phanTramGiam) {
            bienTheGiay.errors = null;
        } else {
            bienTheGiay.errors = 'Phần trăm giảm phải là số nguyên từ 1 - 50';
        }
    }


    $scope.updateKhuyenMai = function () {

        if ($scope.khuyenMaiForm.$invalid) {
            return;
        }

        const valid = $scope.validateNgay();
        if (!valid) {
            toastr["error"]("Vui lòng kiểm tra lại ngày bắt đầu, ngày kết thúc");
            return;
        }

        if (!Array.isArray($scope.selectedGiayTableData)) {
            toastr["error"]("Có lỗi xảy ra. Vui lòng thử lại");
            return;
        }

        if ($scope.selectedGiayTableData.length === 0) {
            toastr["error"]("Bạn chưa chọn giày. Vui lòng thử lại");
            return;
        }
        let invalidCount = 0;
        $scope.khuyenMai.khuyenMaiChiTietRequests = [];
        angular.forEach($scope.selectedGiayTableData, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                if (giay.selected && bienTheGiay.trangThai) {
                    const khuyenMaiChiTietRequest = {
                        bienTheGiayId: bienTheGiay.id,
                        phanTramGiam: bienTheGiay.phanTramGiam
                    };
                    if (!khuyenMaiChiTietRequest.phanTramGiam) {
                        invalidCount++;
                        bienTheGiay.errors = "Phần trăm giảm phải là số nguyên từ 1 - 100";
                    }
                    $scope.khuyenMai.khuyenMaiChiTietRequests.push(khuyenMaiChiTietRequest);
                }
            });
        });

        if (invalidCount !== 0) {
            toastr["warning"]("Vui lòng kiểm tra lại các trường không hợp lệ");
            return;
        }

        if ($scope.khuyenMai.khuyenMaiChiTietRequests.length === 0) {
            toastr["error"]("Bạn chưa set khuyến mại cho sản phẩm nào. Vui lòng thử lại");
            return;
        }
        if ($scope.khuyenMai.ngayBatDau.getHours() === 0) {
            $scope.khuyenMai.ngayBatDau.setHours($scope.khuyenMai.ngayBatDau.getHours() + 7);
        }
        if ($scope.khuyenMai.ngayKetThuc.getHours() === 0) {
            $scope.khuyenMai.ngayKetThuc.setHours($scope.khuyenMai.ngayKetThuc.getHours() + 7);
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
                $http.put(host + '/rest/admin/khuyen-mai/' + $scope.khuyenMai.id, $scope.khuyenMai)
                    .then(function (response) {
                        if (response.status === 200) {
                            toastr["success"]("Thêm thành công");
                        }
                        $location.path("/list");
                        $scope.isLoading = false;
                    })
                    .catch(function (error) {
                        if (error.status === 406) {
                            toastr["error"](error.data.error);
                            $location.path("/list");
                        } else if (error.status === 400) {
                            toastr["error"]("Thêm thất bại. Vui lòng kiểm tra lại");
                            $scope.khuyenMaiForm.ten.$dirty = false;
                            $scope.khuyenMaiForm.ghiChu.$dirty = false;
                            $scope.errors = error.data;
                        } else if (error.status === 409) {

                            Swal.fire({
                                text: error.data.length + " sản phẩm đang tham gia khuyến mãi khác trong thời gian bạn đã chọn. Bạn phải loại bỏ các sản phẩm này?",
                                icon: "warning",
                                confirmButtonColor: "#3085d6",
                                cancelButtonColor: "#d33",
                                confirmButtonText: "Đồng ý"
                            })
                            error.data.forEach(id => {
                                const index = $scope.selectedGiayTableData.findIndex(item => item.id === id);
                                if (index !== -1) {
                                    $scope.selectedGiayTableData.splice(index, 1);
                                }

                            })
                        }
                        $scope.isLoading = false;
                    });
            }
        });

    }

    $scope.changeCheckAll = function () {
        $scope.selectAll = !$scope.selectAll;
        console.log("All: ", $scope.selectAll);
        if ($scope.selectedGiayTableData) {
            $scope.selectedGiayTableData.forEach(item => {
                item.selected = $scope.selectAll;
            })
        }
    }

    $scope.thietLapHangLoat = function () {
        if ($scope.allForm.$invalid) {
            return;
        }

        const giamPhanTram = parseInt($scope.giamPhanTramAll); // Chuyển giá trị thành số
        angular.forEach($scope.selectedGiayTableData, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                if (giay.selected) {
                    bienTheGiay.phanTramGiam = giamPhanTram
                }
            });
        })
    }
    $scope.xoaDong = function (giay) {
        var indexGiay = $scope.selectedGiay.findIndex(function (item) {
            return item === giay.id;
        });

        $scope.selectedGiay.splice(indexGiay, 1);
        console.log(giay, $scope.selectedGiay);

        $scope.selectedGiayTableData = $scope.selectedGiayTableData.filter(function (item) {
            return item.id !== giay.id;
        });
    };
    $scope.hasErrorInItems = false;
    $scope.checkErrorsInItems = function () {
        $scope.hasErrorInItems = false;
        for (var i = 0; i < $scope.items.length; i++) {
            if ($scope.items[i].itemForm.$invalid) {
                $scope.hasErrorInItems = true;
                break;
            }
        }
    };
    $scope.$watch('items', function (newItems, oldItems) {
        $scope.checkErrorsInItems();
    }, true);
    $scope.init();

});

app.controller("detailKhuyenMaiController", function ($scope, $http, $location, $routeParams) {
    $scope.khuyenMai = {};
    $scope.khuyenMai.khuyenMaiChiTietRequests = [];
    const id = $routeParams.id;

    $scope.selectedGiay = [];
    $scope.selectedGiayTableData = [];

    $scope.selectedGiayInsert = [];
    $scope.selectedGiayTableDataInsert = [];

    $scope.selectedGiayUpdate = [];
    $scope.selectedGiayTableDataUpdate = [];

    $scope.init = function () {
        $http.get(host + '/rest/admin/khuyen-mai/giay/' + id)
            .then(function (response) {
                if (response.status === 200) {
                    $scope.khuyenMaiDetail = response.data;
                    console.log("Detail khuyến mại: ", $scope.khuyenMaiDetail.ten)

                    var ngayBatDau = $scope.khuyenMaiDetail.ngayBatDau;
                    var object = new Date(ngayBatDau);
                    $scope.khuyenMaiDetail.ngayBatDau = object;

                    var ngayKetThuc = $scope.khuyenMaiDetail.ngayKetThuc;
                    var object = new Date(ngayKetThuc);
                    $scope.khuyenMaiDetail.ngayKetThuc = object;
                    console.log("Ngày bắt đầu", $scope.khuyenMaiDetail.ngayBatDau)
                    console.log("Ngày kết thúc", $scope.khuyenMaiDetail.ngayKetThuc)
                    $scope.khuyenMai = {
                        id: $scope.khuyenMaiDetail.id,
                        ten: $scope.khuyenMaiDetail.ten,
                        ngayBatDau: $scope.khuyenMaiDetail.ngayBatDau,
                        ngayKetThuc: $scope.khuyenMaiDetail.ngayKetThuc,
                        ghiChu: $scope.khuyenMaiDetail.ghiChu,
                        trangThai: $scope.khuyenMaiDetail.trangThai,
                    }
                    console.log("Tên:", $scope.khuyenMai)
                    console.log("Detail khuyến mại chi tiết: ", $scope.khuyenMaiDetail.khuyenMaiChiTietResponses)
                    console.log("Giày", $scope.khuyenMaiDetail.khuyenMaiChiTietResponses.giays)
                    angular.forEach($scope.khuyenMaiDetail.khuyenMaiChiTietResponses.giays, function (giay) {
                        console.log("Giày id:", giay.id)
                        console.log("Biến thể", giay.lstBienTheGiay)
                    });

                    angular.forEach($scope.khuyenMaiDetail.khuyenMaiChiTietResponses.giays, function (giay) {
                        $scope.selectedGiayUpdate.push(giay.id);
                        $scope.selectedGiayTableDataUpdate.push(giay);
                        console.log("Id detail:", $scope.selectedGiayUpdate)
                        console.log("Biến thể detail", $scope.selectedGiayTableDataUpdate)
                    });

                    $scope.selectedGiay = $scope.selectedGiayUpdate.concat($scope.selectedGiay);
                    $scope.selectedGiayTableData = $scope.selectedGiayTableDataUpdate.concat($scope.selectedGiayTableData);

                    console.log("Id show: ", $scope.selectedGiay)
                    console.log("Biến thể show: ", $scope.selectedGiayTableData)
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    };

    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.changeGhiChu = function (input) {
        input.$dirty = true;
    }


    $scope.curPage = 1,
        $scope.itemsPerPage = 500,
        $scope.maxSize = 5;

    let giaySearch = {};

    $scope.loadGiay = function (currentPage) {

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post('http://localhost:8080/rest/admin/giay/get-all-giay', giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                console.log("Load giày: ", $scope.giays);

                angular.forEach($scope.giays, function (giay) {
                    giay.selected = $scope.selectedGiay.includes(giay.id);
                    giay.disabled = giay.selected;
                });
                // Show the modal
                $('#myModal').modal('show');
            })
            .catch(function (error) {
                console.error('Error fetching data: ' + error);
            });
    };

    $scope.selectAllGiay = function () {
        // Kiểm tra xem tất cả các checkbox đã được chọn và disabled chưa
        var allSelectedAndDisabled = $scope.giays.every(function (giay) {
            return giay.selected && giay.disabled;
        });
        // Nếu tất cả đã được chọn và disabled, không thay đổi trạng thái
        if (!allSelectedAndDisabled) {
            angular.forEach($scope.giays, function (giay) {
                giay.selected = $scope.selectAll;
            });
        }

    };

    $scope.$watch('giays', function (newGiays, oldGiays) {
        // Kiểm tra xem tất cả các checkbox trong tbody đã được chọn và không bị disabled chưa
        var allSelectedAndNotDisabled = newGiays.every(function (giay) {
            return giay.selected && !giay.disabled;
        });

        // Nếu tất cả đã được chọn và không bị disabled, cập nhật trạng thái cho checkbox ở thead
        if (allSelectedAndNotDisabled) {
            $scope.selectAll = true;
        } else {
            $scope.selectAll = false;
        }
    }, true);

    $scope.xacNhan = function () {

        $scope.giays.forEach(function (giay) {
            if (giay.selected === true) {
                $scope.selectedGiayInsert.push(giay);
            }
        });

        $scope.selectedGiayInsert = $scope.selectedGiayInsert.filter(function (giay) {
            return giay.selected;
        }).map(function (giay) {
            return giay.id;
        });

        $scope.selectedGiayInsert = $scope.selectedGiayInsert.filter(function (item) {
            return $scope.selectedGiay.indexOf(item) === -1;
        });

        console.log("Id giày insert: ", $scope.selectedGiayInsert);

        $http.post('http://localhost:8080/rest/admin/giay/get-giay-contains', $scope.selectedGiayInsert)
            .then(function (response) {
                $scope.selectedGiayTableDataInsert = response.data;

                $scope.selectedGiay = $scope.selectedGiayInsert.concat($scope.selectedGiay);
                $scope.selectedGiayTableData = $scope.selectedGiayTableDataInsert.concat($scope.selectedGiayTableData);

                console.log("Id show: ", $scope.selectedGiay)
                console.log("Biến thể show: ", $scope.selectedGiayTableData)

            })
            .catch(function (error) {
                // Xử lý lỗi ở đây
                console.error('Error fetching data:', error);
            });

    };

    $scope.updateKhuyenMai = function () {

        if ($scope.khuyenMaiForm.$invalid) {
            return;
        }
        angular.forEach($scope.selectedGiayTableData, function (giay) {
            console.log("data", giay.lstBienTheGiay)
            $scope.data = giay.lstBienTheGiay
        })
        var filteredArrayUpdate = $scope.data.map(function (item) {
            return {
                id: item.idKhuyenMaiChiTiet,
                bienTheGiayId: item.id,
                phanTramGiam: item.phanTramGiam
            };
        });
        angular.forEach($scope.selectedGiayTableDataInsert, function (giay) {
            console.log("data", giay.lstBienTheGiay)
            $scope.data = giay.lstBienTheGiay
        })
        var filteredArrayInsert = $scope.data.map(function (item) {
            return {
                id: item.idKhuyenMaiChiTiet,
                bienTheGiayId: item.id,
                phanTramGiam: item.phanTramGiam
            };
        });
        var dataUpdate = filteredArrayUpdate.concat(filteredArrayInsert);
        console.log("up:", filteredArrayUpdate);
        console.log("in:", filteredArrayInsert);

        $scope.khuyenMai.khuyenMaiChiTietRequests = dataUpdate;
        console.log("after:", dataUpdate);
        console.log("chitiet:", $scope.khuyenMai.khuyenMaiChiTietRequests);

        console.log("post:", $scope.khuyenMai);


        $http.put(host + '/rest/admin/khuyen-mai/' + id, $scope.khuyenMai)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Cập nhật thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                console.log(error)
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.khuyenMaiForm.ten.$dirty = false;
                    $scope.khuyenMaiForm.ghiChu.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

    $scope.thietLapHangLoat = function () {
        var giamPhanTram = parseFloat($scope.giamPhanTram); // Chuyển giá trị thành số
        angular.forEach($scope.selectedGiayTableData, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                if (giay.selected) {
                    bienTheGiay.phanTramGiam = giamPhanTram
                }
            });

        })
        console.log("Khuyến mại chi tiết: ", $scope.khuyenMai.khuyenMaiChiTietRequests)
    }
    $scope.xoaDong = function (giay) {
        var indexGiay = $scope.selectedGiay.findIndex(function (item) {
            return item === giay.id;
        });

        $scope.selectedGiay.splice(indexGiay, 1);
        console.log(giay, $scope.selectedGiay);

        $scope.selectedGiayTableData = $scope.selectedGiayTableData.filter(function (item) {
            return item.id !== giay.id;
        });
    };
    $scope.hasErrorInItems = false;
    $scope.checkErrorsInItems = function () {
        $scope.hasErrorInItems = false;
        for (var i = 0; i < $scope.items.length; i++) {
            if ($scope.items[i].itemForm.$invalid) {
                $scope.hasErrorInItems = true;
                break;
            }
        }
    };
    $scope.$watch('items', function (newItems, oldItems) {
        $scope.checkErrorsInItems();
    }, true);
    $scope.init();

});