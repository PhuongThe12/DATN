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

    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.changeGhiChu = function (input) {
        input.$dirty = true;
    }

    $scope.khuyenMai = {};
    $scope.khuyenMai.khuyenMaiChiTietRequests = [];

    $scope.curPage = 1,
        $scope.itemsPerPage = 5,
        $scope.maxSize = 5;

    let giaySearch = {};

    $scope.loadGiay = function (currentPage) {

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;


        $http.post('http://localhost:8080/admin/rest/giay/get-all-giay',giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                console.log("Load giày: ", $scope.giays);
                $$scope.giays.forEach(function (giay) {
                    giay.selected = $scope.selectedGiays.some(function (selectedGiay) {
                        return selectedGiay.id === giay.id;
                    });
                });
                // Show the modal
                $('#myModal').modal('show');
            })
            .catch(function (error) {
                console.error('Error fetching data: ' + error);
            });
    };

    $scope.$watch('curPage + numPerPage', function () {
        $scope.loadGiay($scope.curPage);
    });

    //Hàm chọn select tất cả.
    $scope.toggleSelectAllGiay = function () {
        angular.forEach($scope.giays, function (giay) {
            giay.selected = $scope.selectAll;
        });
    };

    $scope.selectedGiays = [];
    $scope.giays = [];

    //Hàm xác nhận
    $scope.isXacNhanDisabled = function () {
        $scope.selectedGiays = $scope.giays.filter(function (giay) {
            return giay.selected;
        });
        return $scope.selectedGiays.length === 0;
    };

    $scope.isTableVisible = false;
    $scope.xacNhan = function () {
        $scope.selectedGiays = $scope.giays.filter(function (giay) {
            return giay.selected;
        });

        $scope.isTableVisible = true;
        console.log($scope.selectedGiays)
    };


    ///////////////////////
    $scope.thietLapHangLoat = function () {
        var giamPhanTram = parseFloat($scope.giamPhanTram); // Chuyển giá trị thành số
        angular.forEach($scope.selectedGiays, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                if (bienTheGiay.selected) {
                    bienTheGiay.phanTramGiam = giamPhanTram;
                }
            });
        });
    };
    $scope.toggleAll = function (giay) {
        angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
            bienTheGiay.selected = giay.selectAll;
        });
    };
    ///////
    $scope.addKhuyenMai = function () {

        if ($scope.khuyenMaiForm.$invalid) {
            return;
        }

        // $scope.handleCheckboxChange = function (bienTheGiay) {
        //     if (bienTheGiay.selected) {
        //         var khuyenMaiChiTiet = {
        //             bienTheGiayId: bienTheGiay.id,
        //             phanTramGiam: bienTheGiay.phanTramGiam
        //         };
        //         $scope.khuyenMai.khuyenMaiChiTietRequests.push(khuyenMaiChiTiet);
        //     }
        // };
        angular.forEach($scope.selectedGiays, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                var khuyenMaiChiTietRequest = {
                    bienTheGiayId: bienTheGiay.id,
                    phanTramGiam: bienTheGiay.phanTramGiam
                };

                $scope.khuyenMai.khuyenMaiChiTietRequests.push(khuyenMaiChiTietRequest);
            });
        });

        console.log("Khuyến mại chi tiết: ", $scope.khuyenMai.khuyenMaiChiTietRequests)
        $http.post(host + '/admin/rest/khuyen-mai', $scope.khuyenMai)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
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
        let apiUrl = host + '/admin/rest/khuyen-mai?page=' + currentPage;
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
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.detailKhuyenMai = function (val) {
        var id = val;
        $http.get(host + '/admin/rest/khuyen-mai/' + id)
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

app.controller("updateKhuyenMaiController", function ($scope, $http, $location, $routeParams) {

    $scope.khuyenMai = {};
    $scope.khuyenMai.id = $routeParams.id;
    $scope.khuyenMai.khuyenMaiChiTietRequests = [];

    //Hàm chọn select tất cả.
    $scope.toggleSelectAllGiay = function () {
        angular.forEach($scope.giays, function (giay) {
            giay.selected = $scope.selectAll;
        });
    };
    $scope.selectedGiays = [];

    //Hàm xác nhận
    $scope.isXacNhanDisabled = function () {
        $scope.selectedGiays = $scope.giays.filter(function (giay) {
            return giay.selected;
        });
        return $scope.selectedGiays.length === 0;
    };
    console.log($scope.giays);


    $scope.isTableVisible = false;
    $scope.xacNhan = function () {
        $scope.isTableVisible = true;
        console.log($scope.selectedGiays)
    };

    ///////////////////////
    $scope.thietLapHangLoat = function () {
        var giamPhanTram = parseFloat($scope.giamPhanTram); // Chuyển giá trị thành số
        angular.forEach($scope.selectedGiays, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                if (bienTheGiay.selected) {
                    bienTheGiay.phanTramGiam = giamPhanTram;
                }
            });
        });
    };
    $scope.toggleAll = function (giay) {
        angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
            bienTheGiay.selected = giay.selectAll;
        });
    };
    $scope.init = function () {
        $http.get(host + '/admin/rest/khuyen-mai/' + $scope.khuyenMai.id)
            .then(function (response) {
                if (response.status === 200) {
                    $scope.khuyenMai = response.data;
                    console.log("Detail khuyến mại: ", $scope.khuyenMai)
                    console.log("Detail khuyến mại chi tiết: ", $scope.khuyenMai.khuyenMaiChiTietResponses)
                    $scope.loadGiay();
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
        $scope.itemsPerPage = 5,
        $scope.maxSize = 5;

    let giaySearch = {};

    $scope.loadGiay = function (currentPage) {

        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;


        $http.post('http://localhost:8080/admin/rest/giay/get-all-giay',giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                console.log("Load giày: ", $scope.giays);
                $scope.xacNhan();
                $scope.khuyenMai.khuyenMaiChiTietResponses.forEach(function (item) {
                    var giayResponseId = item.bienTheGiayResponsel.giayResponse.id;
                    console.log("abc", giayResponseId);

                    var selectedGiay = $scope.giays.find(function (giay) {
                        return giay.id === giayResponseId;
                    });

                    if (selectedGiay) {
                        selectedGiay.selected = true;
                    }
                });
                // $('#myModal').modal('show');
            })
            .catch(function (error) {
                console.error('Error fetching data: ' + error);
            });
    };

    $scope.$watch('curPage + numPerPage', function () {
        $scope.loadGiay($scope.curPage);
    });

    $scope.updateDotGiamGia = function () {

        if ($scope.dotGiamGiaForm.$invalid) {
            return;
        }
        console.log($scope.dotGiamGia);
        console.log($scope.dotGiamGia.dieuKienRequests);
        // Gửi danh sách các điều kiện lên backend
        $scope.dotGiamGia.dieuKienRequests = $scope.dotGiamGia.dieuKienResponses.map(function (dieuKien) {
            return {
                id: dieuKien.id,
                tongHoaDon: dieuKien.tongHoaDon,
                phanTramGiam: dieuKien.phanTramGiam
            };
        });

        console.log($scope.dotGiamGia);

        $http.put(host + '/admin/rest/dot-giam-gia/' + $scope.dotGiamGia.id, $scope.dotGiamGia)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Cập nhật thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                toastr["error"]("Cập nhật thất bại");
                if (error.status === 400) {
                    $scope.dotGiamGiaForm.ten.$dirty = false;
                    $scope.dotGiamGiaForm.ghiChu.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

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