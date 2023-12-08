var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/khuyenmai/views/list.html',
            controller: 'khuyenMaiListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/khuyenmai/views/detail.html',
        controller: 'detailKhuyenMaiController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/khuyenmai/views/update.html',
        controller: 'updateKhuyenMaiController'
    }).when("/add", {
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
            bienTheGiay.errors = 'Phần trăm giảm phải là số nguyên từ 1 - 100';
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
        })
        $scope.khuyenMai.ngayBatDau.setHours($scope.khuyenMai.ngayBatDau.getHours() + 7);
        $scope.khuyenMai.ngayKetThuc.setHours($scope.khuyenMai.ngayKetThuc.getHours() + 7);

        if(invalidCount !== 0) {
            toastr["warning"]("Vui lòng kiểm tra lại các trường không hợp lệ");
            return;
        }

        if ($scope.khuyenMai.khuyenMaiChiTietRequests.length === 0) {
            toastr["error"]("Bạn chưa set khuyến mại cho sản phẩm nào. Vui lòng thử lại");
            return;
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
                                }

                            })
                        }
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

    function getData(currentPage) {
        let apiUrl = host + '/rest/admin/khuyen-mai?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        }

        $http.get(apiUrl)
            .then(function (response) {
                $scope.khuyenMais = response.data.content;
                console.log("List khuyến mại: ", $scope.khuyenMais)
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.detailKhuyenMai = function (val) {
        var id = val;
        $http.get(host + '/rest/admin/khuyen-mai/' + id)
            .then(function (response) {
                $scope.khuyenMaiDetail = response.data;
                const button = document.querySelector('[data-bs-target="#showKhuyenMai"]');
                if (button) {
                    button.click();
                }
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

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
