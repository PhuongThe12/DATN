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

    $scope.khuyenMai = {};
    $scope.khuyenMai.khuyenMaiChiTietRequests = [];

    $scope.formatDate = function (date) {
        return $filter('date')(date, 'HH:mm dd/MM/yyyy');
    };

    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.changeGhiChu = function (input) {
        input.$dirty = true;
    }


    $scope.curPage = 1,
        $scope.itemsPerPage = 500,
        $scope.maxSize = 100;

    let giaySearch = {};

    $scope.loadGiay = function (currentPage) {
        $scope.isLoading = true;
        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;

        $http.post('http://localhost:8080/rest/admin/giay/get-all-giay', giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                console.log("Load giày: ", $scope.giays);
                $scope.isLoading = false;
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

    $scope.selectedGiay = [];
    $scope.selectedGiayTableData = [];

    $scope.xacNhan = function () {
        $scope.giays.forEach(function (giay) {
            if (giay.selected === true) {
                $scope.selectedGiay.push(giay);
            }
        });

        $scope.selectedGiay = $scope.selectedGiay.filter(function (giay) {
            return giay.selected;
        }).map(function (giay) {
            return giay.id;
        });
        $http.post('http://localhost:8080/rest/admin/giay/get-giay-contains', $scope.selectedGiay)
            .then(function (response) {
                // Xử lý dữ liệu từ API ở đây
                $scope.selectedGiayTableData = response.data;
                console.log("Load biến thể: ", $scope.selectedGiayTableData);
            })
            .catch(function (error) {
                // Xử lý lỗi ở đây
                console.error('Error fetching data:', error);
            });
        console.log("Id giày ", $scope.selectedGiay);
    };

    $scope.addKhuyenMai = function () {

        if ($scope.khuyenMaiForm.$invalid) {
            return;
        }

        angular.forEach($scope.selectedGiayTableData, function (giay) {
            angular.forEach(giay.lstBienTheGiay, function (bienTheGiay) {
                if (giay.selected) {
                    var khuyenMaiChiTietRequest = {
                        bienTheGiayId: bienTheGiay.id,
                        phanTramGiam: bienTheGiay.phanTramGiam
                    };
                    $scope.khuyenMai.khuyenMaiChiTietRequests.push(khuyenMaiChiTietRequest);
                }
            });
        })

        console.log("Khuyến mại chi tiết: ", $scope.khuyenMai.khuyenMaiChiTietRequests)
        console.log("Khuyến mại: ", $scope.khuyenMai)

        $http.post(host + '/rest/admin/khuyen-mai', $scope.khuyenMai)
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

app.controller("updateKhuyenMaiController", function ($scope, $http, $location, $routeParams) {
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
        var filteredArrayUpdate  = $scope.data.map(function(item) {
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
        var filteredArrayInsert  = $scope.data.map(function(item) {
            return {
                id: item.idKhuyenMaiChiTiet,
                bienTheGiayId: item.id,
                phanTramGiam: item.phanTramGiam
            };
        });
        var dataUpdate = filteredArrayUpdate.concat(filteredArrayInsert);
        console.log("up:",filteredArrayUpdate);
        console.log("in:",filteredArrayInsert);

        $scope.khuyenMai.khuyenMaiChiTietRequests = dataUpdate;
        console.log("after:",dataUpdate);
        console.log("chitiet:",$scope.khuyenMai.khuyenMaiChiTietRequests);

        console.log("post:",$scope.khuyenMai);


        $http.put(host + '/rest/admin/khuyen-mai/'+id, $scope.khuyenMai)
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
        var filteredArrayUpdate  = $scope.data.map(function(item) {
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
        var filteredArrayInsert  = $scope.data.map(function(item) {
            return {
                id: item.idKhuyenMaiChiTiet,
                bienTheGiayId: item.id,
                phanTramGiam: item.phanTramGiam
            };
        });
        var dataUpdate = filteredArrayUpdate.concat(filteredArrayInsert);
        console.log("up:",filteredArrayUpdate);
        console.log("in:",filteredArrayInsert);

        $scope.khuyenMai.khuyenMaiChiTietRequests = dataUpdate;
        console.log("after:",dataUpdate);
        console.log("chitiet:",$scope.khuyenMai.khuyenMaiChiTietRequests);

        console.log("post:",$scope.khuyenMai);


        $http.put(host + '/rest/admin/khuyen-mai/'+id, $scope.khuyenMai)
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