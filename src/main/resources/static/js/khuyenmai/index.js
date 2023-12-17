var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/khuyenmai/views/list.html',
            controller: 'khuyenMaiListController'
        })
        .when("/update/:id", {
            templateUrl: '/pages/admin/khuyenmai/views/update.html',
            controller: 'updateKhuyenMaiController'
        })
        .when("/add", {
            templateUrl: '/pages/admin/khuyenmai/views/add.html',
            controller: 'addKhuyenMaiController'
        })
        .otherwise({redirectTo: '/list'});
});

app.controller("addKhuyenMaiController", function ($scope, $http, $location) {

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

    $http.get(host + '/rest/admin/thuong-hieu/get-all')
        .then(function (response) {
            $scope.thuongHieus = response.data;
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        });

    $scope.formatDate = function (date) {
        return $filter('date')(date, 'HH:mm dd/MM/yyyy');
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
            kmSearch.ngayBatDau.setHours($scope.khuyenMai.ngayBatDau.getHours() + 7);
            kmSearch.ngayKetThuc = new Date($scope.khuyenMai.ngayKetThuc);
            kmSearch.ngayKetThuc.setHours($scope.khuyenMai.ngayKetThuc.getHours() + 7);
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
                $scope.giays = response.data;
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

    $scope.selectedGiay = [];
    $scope.selectedGiayTableData = [];

    $scope.blurInput = function (bienTheGiay) {
        if (bienTheGiay.phanTramGiam) {
            bienTheGiay.errors = null;
        } else {
            bienTheGiay.errors = 'Phần trăm giảm phải là số nguyên từ 1 - 50';
        }
    }

    $scope.xacNhan = function () {
        $scope.isLoading = true;

        const newTableData = $scope.allGiay.filter(item =>
            item.selected && !item.disabled
        );

        newTableData.forEach(item => {
            $scope.selectedGiay.push(item.id);
        });

        $scope.selectedGiayTableData = [...$scope.selectedGiayTableData, ...newTableData];
        $scope.isLoading = false;
    };

    $scope.addKhuyenMai = function () {

        $scope.isLoading = true;
        if ($scope.khuyenMaiForm.$invalid) {
            $scope.isLoading = false;
            return;
        }

        const valid = $scope.validateNgay();
        if (!valid) {
            toastr["error"]("Vui lòng kiểm tra lại ngày bắt đầu, ngày kết thúc");
            $scope.isLoading = false;
            return;
        }

        if (!Array.isArray($scope.selectedGiayTableData)) {
            toastr["error"]("Có lỗi xảy ra. Vui lòng thử lại");
            $scope.isLoading = false;
            return;
        }

        if ($scope.selectedGiayTableData.length === 0) {
            toastr["error"]("Bạn chưa chọn giày. Vui lòng thử lại");
            $scope.isLoading = false;
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
                        bienTheGiay.errors = "Phần trăm giảm phải là số nguyên từ 1 - 50";
                    }
                    $scope.khuyenMai.khuyenMaiChiTietRequests.push(khuyenMaiChiTietRequest);
                }
            });
        })

        if ($scope.khuyenMai.ngayBatDau.getHours() === 0) {
            $scope.khuyenMai.ngayBatDau.setHours($scope.khuyenMai.ngayBatDau.getHours() + 7);
        }
        if ($scope.khuyenMai.ngayKetThuc.getHours() === 0) {
            $scope.khuyenMai.ngayKetThuc.setHours($scope.khuyenMai.ngayKetThuc.getHours() + 7);
        }

        if (invalidCount !== 0) {
            toastr["warning"]("Vui lòng kiểm tra lại các trường không hợp lệ");
            $scope.isLoading = false;
            return;
        }

        if ($scope.khuyenMai.khuyenMaiChiTietRequests.length === 0) {
            toastr["error"]("Bạn chưa set khuyến mại cho sản phẩm nào. Vui lòng thử lại");
            $scope.isLoading = false;
            return;
        }

        $scope.isLoading = false;
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
                $http.post(host + '/rest/admin/khuyen-mai', $scope.khuyenMai)
                    .then(function (response) {
                        if (response.status === 200) {
                            toastr["success"]("Thêm thành công");
                        }
                        $location.path("/list");
                    })
                    .catch(function (error) {
                        if (error.status === 400) {
                            toastr["error"]("Thêm thất bại. Vui lòng kiểm tra lại");
                            $scope.khuyenMaiForm.ten.$dirty = false;
                            $scope.khuyenMaiForm.ghiChu.$dirty = false;
                            $scope.errors = error.data;
                        }
                        if (error.status === 409) {

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
                                    $scope.selectedGiay.filter(item => item.id !== id);
                                }

                            })
                        }
                        console.log(error);
                        $scope.isLoading = false;
                    });
            }
        });


    }

    $scope.changeCheckAll = function () {
        $scope.selectAll = !$scope.selectAll;
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
                    bienTheGiay.errors = null;
                }
            });

        })
    }
    $scope.xoaDong = function (giay) {
        const indexGiay = $scope.selectedGiay.findIndex(function (item) {
            return item === giay.id;
        });

        $scope.selectedGiay.splice(indexGiay, 1);

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

});

app.controller("khuyenMaiListController", function ($scope, $http, $window, $location) {

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
        let apiUrl = host + '/rest/admin/khuyen-mai/search';
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
                    $scope.khuyenMais = response.data.content;
                    $scope.numOfPages = response.data.totalPages;
                    if ($scope.status === 0) {
                        $scope.khuyenMais.forEach(item => {
                            const nbd = new Date(item.ngayBatDau);
                            const nkt = new Date(item.ngayKetThuc);
                            if (nbd <= new Date() <= nkt) {
                                item.hienThi = true;
                            }
                        })
                    }
                    $scope.isLoading = false;
                } else {
                    $scope.khuyenMais = [];
                    toastr["error"]("Không có khuyến mại nào");
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

    $scope.hienThiKhuyenMai = function (khuyenMai) {
        const nbd = new Date(khuyenMai.ngayBatDau);
        const nkt = new Date(khuyenMai.ngayKetThuc);
        if (nbd <= new Date() <= nkt) {
            Swal.fire({
                text: "Xác nhận hiển thị khuyến mại?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy"
            }).then((result) => {
                $http.put(host + '/rest/admin/khuyen-mai/hien-thi/' + khuyenMai.id)
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
            toastr["error"]("Khuyến mại đã kết thúc không được phép chỉnh sửa");
        }
    }

    $scope.anKhuyenMai = function (khuyenMai) {
        const nbd = new Date(khuyenMai.ngayBatDau);
        const nkt = new Date(khuyenMai.ngayKetThuc);
        if (nbd <= new Date() <= nkt) {
            Swal.fire({
                text: "Xác nhận ẩn khuyến mại?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy"
            }).then((result) => {
                $http.put(host + '/rest/admin/khuyen-mai/an/' + khuyenMai.id)
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
            toastr["error"]("Khuyến mại đã kết thúc không được phép chỉnh sửa");
        }
    }

    $scope.detailKhuyenMai = function (val) {
        $scope.khuyenMaiDetail = val;
        const giayMap = {};
        val.khuyenMaiChiTietResponses.forEach(item => {
            if (giayMap.hasOwnProperty(item.bienTheGiayResponsel.giayResponse.id)) {
                giayMap[item.bienTheGiayResponsel.giayResponse.id].lstBienTheGiay.push({
                    kichThuoc: item.bienTheGiayResponsel.kichThuoc,
                    mauSac: item.bienTheGiayResponsel.mauSac,
                    giaBan: item.bienTheGiayResponsel.giaBan,
                    phanTramGiam: item.phanTramGiam
                });
            } else {
                giayMap[item.bienTheGiayResponsel.giayResponse.id] = {
                    id: item.bienTheGiayResponsel.giayResponse.id,
                    ten: item.bienTheGiayResponsel.giayResponse.ten,
                    lstBienTheGiay: [
                        {
                            kichThuoc: item.bienTheGiayResponsel.kichThuoc,
                            mauSac: item.bienTheGiayResponsel.mauSac,
                            giaBan: item.bienTheGiayResponsel.giaBan,
                            phanTramGiam: item.phanTramGiam
                        }
                    ]
                }
            }
        })

        $scope.khuyenMaiDetail.giays = Object.values(giayMap);
    }

});


app.directive('customInput', function () {
        return {
            restrict: 'E',
            templateUrl: '/pages/admin/khuyenmai/views/input.html',
            scope: {
                id: '@',
                title: '@',
                items: '=',
                ngModel: '=',
                modalId: "@",
                selectType: '@'
            },
            controller: function ($scope) {
                $scope.selectedTags = [];
                $scope.selectedItem = "";
                $scope.items.forEach(function (item) {
                    item.status = 'disabled';
                });

                $scope.isItemSelected = function (item) {
                    return $scope.selectedTags.includes(item);
                };

                $scope.selectItem = function (item) {
                    if (item) {
                        if (!$scope.isItemSelected(item)) {
                            $scope.selectedTags.push(item);
                            item.status = 'active';
                            // $scope.ngModel = $scope.selectedTags;
                            $scope.ngModel = $scope.selectedTags;
                        } else {
                            item.status = 'active';
                            // $scope.selectedTags.push(item);
                        }

                        $scope.selectedItem = "";
                        const index = $scope.items.indexOf(item);
                        if (index !== -1) {
                            $scope.items.splice(index, 1);
                        }

                    }

                };
                $scope.$watchCollection('ngModel', function (newNgModel, oldNgModel) {
                    if (newNgModel !== oldNgModel) {
                        // Thực hiện các tác vụ tương ứng với sự thay đổi trong ngModel (danh sách)
                        newNgModel.forEach((element) => {
                            if (element) {
                                const selectedItem = $scope.items.find((item) => item.id === element.id && item.status === 'active');
                                if (selectedItem) {
                                    $scope.selectItem(selectedItem);
                                }
                            }
                        });
                    }
                });

                $scope.removeTag = function (tag) {
                    const index = $scope.selectedTags.indexOf(tag);
                    if (index !== -1) {
                        // $scope.selectedTags.splice(index, 1);
                        tag.status = 'disabled';
                        $scope.items.push(tag);
                    }
                };

                $scope.toggleDropdown = function () {
                    $scope.isActive = !$scope.isActive;
                };

                angular.element(document).on('click', function (event) {
                    var container = angular.element(document.querySelector('#' + $scope.id));
                    if (container.length > 0) {
                        if (!container[0].contains(event.target)) {
                            $scope.$apply(function () {
                                $scope.isActive = false;
                            });
                        }
                    }

                });


            }
        };
    }
);
