var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/khachhang/views/list.html',
            controller: 'khachhangListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/khachhang/views/detail.html',
        controller: 'detailKhachHangController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/khachhang/views/update.html',
        controller: 'updateKhachHangController'
    }).when("/add", {
        templateUrl: '/pages/admin/khachhang/views/add.html',
        controller: 'addKhachHangController'
    }).when("/donmua", {
        templateUrl: '/pages/admin/khachhang/views/donmua.html',
        controller: 'donMuaController'
    })
        .otherwise({redirectTo: '/list'});
});


app.controller("addKhachHangController", function ($scope, $http, $location) {
    $scope.khachHang = {};
    $scope.khachHang.gioiTinh = true;
    $scope.change = function (input) {
        input.$dirty = true;
    }
    $scope.addKhachHang = function () {
        if ($scope.khachHangForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/khach-hang', $scope.khachHang)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $location.path("/list");
            })
            .catch(function (error) {
                // toastr["error"]("Thêm thất bại");
                console.log(error)
                if (error.status === 400) {
                    $scope.khachHangForm.hoTen.$dirty = false;
                    $scope.khachHangForm.gioiTinh.$dirty = false;
                    $scope.khachHangForm.ngaySinh.$dirty = false;
                    $scope.khachHangForm.soDienThoai.$dirty = false;
                    $scope.khachHangForm.email.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

    const video = document.getElementById('video');
    let scanning = false;
    $scope.scanQR = function () {
        scanning = true;
        if (scanning) {
            navigator.mediaDevices.getUserMedia({video: true})
                .then((stream) => {
                    video.srcObject = stream;
                    video.play();

                    const canvas = document.createElement('canvas');
                    const context = canvas.getContext('2d');

                    const interval = setInterval(() => {
                        if (scanning) {
                            context.drawImage(video, 0, 0, canvas.width, canvas.height);
                            const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
                            const code = jsQR(imageData.data, imageData.width, imageData.height);

                            if (code) {
                                // Thực hiện các hành động với mã QR tại đây
                                if (typeof code.data === 'string') {
                                    console.log(code.data.split('|'));
                                    const data = code.data.split('|');
                                    if (data.length === 7) {
                                        $scope.khachHang.hoTen = data[2];
                                        $scope.khachHang.gioiTinh = data[4] === 'Nam';
                                        console.log($scope.khachHang.gioiTinh);
                                    } else {
                                        toastr["error"]('Không hợp lệ. Vui lòng thử lại');
                                    }
                                } else {
                                    toastr["error"]('Không hợp lệ. Vui lòng thử lại');
                                }
                                document.getElementById('closeModalCamera').click();
                                clearInterval(interval);
                            }
                        }
                    }, 500);
                })
                .catch((error) => {
                    toastr["error"]('Không thể truy cập camera:');
                });
        } else {
            $scope.stopScanning();
        }
    }

    function convertImageDataToBase64(imageData) {
        const canvas = document.createElement('canvas');
        const context = canvas.getContext('2d');
        canvas.width = imageData.width;
        canvas.height = imageData.height;
        context.putImageData(imageData, 0, 0);

        return canvas.toDataURL('image');
    }


    function updateVideoStream() {
        navigator.mediaDevices.getUserMedia({
            video: {deviceId: selectedCamera}
        })
            .then((stream) => {
                video.srcObject = stream;
                video.play();
            })
            .catch((error) => {
                console.error('Không thể cập nhật stream camera:', error);
            });
    }


    $scope.stopScanning = function () {
        video.pause();
        video.srcObject.getTracks().forEach(track => track.stop());
    }

    $scope.closeModalCamera = function () {
        scanning = false;
        $scope.stopScanning();
    }

});


app.controller("khachhangListController", function ($scope, $http, $window, $location) {

    $scope.curPage = 1,
        $scope.itemsPerPage = 3,
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
        let apiUrl = host + '/admin/rest/khach-hang?page=' + currentPage;
        if (searchText) {
            apiUrl += '&search=' + searchText;
        }

        if ($scope.status == 0) {
            apiUrl += '&status=' + 0;
        } else if ($scope.status == 1) {
            apiUrl += '&status=' + 1;
        } else if ($scope.status == 2) {
            apiUrl += '&chucVu=' + 1;
        } else if ($scope.status == 3) {
            apiUrl += '&chucVu=' + 2;
        }

        console.log(apiUrl);

        $http.get(apiUrl)
            .then(function (response) {
                $scope.khachHangs = response.data.content;
                $scope.hangKhachHangs = response.data.content;
                $scope.numOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }

    $scope.detailKhachHang = function (val) {
        var id = val;
        $http.get(host + '/admin/rest/khach-hang/' + id)
            .then(function (response) {
                $scope.khachHangDetail = response.data;
                const button = document.querySelector('[data-bs-target="#showKhachHang"]');
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

app.controller("updateKhachHangController", function ($scope, $http, $routeParams, $location) {
    const id = $routeParams.id;
    $scope.change = function (input) {
        input.$dirty = true;
    }

    $http.get(host + '/admin/rest/khach-hang/' + id)
        .then(function (response) {
            $scope.khachHang = response.data;
            var ngaySinh = $scope.khachHang.ngaySinh;
            var object = new Date(ngaySinh);
            $scope.khachHang.ngaySinh = object;
            console.log(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    $scope.updateKhachHang = function () {
        if ($scope.khachHangForm.$invalid) {
            return;
        }
        const khachHangUpdate = {
            id: $scope.khachHang.id,
            hoTen: $scope.khachHang.hoTen,
            gioiTinh: $scope.khachHang.gioiTinh,
            ngaySinh: $scope.khachHang.ngaySinh,
            soDienThoai: $scope.khachHang.soDienThoai,
            email: $scope.khachHang.email,
            diemTichLuy: $scope.khachHang.diemTichLuy,
            trangThai: $scope.khachHang.trangThai,

        };
        console.log($scope.khachHang);
        $http.put(host + '/admin/rest/khach-hang/' + id, khachHangUpdate)
            .then(function (response) {
                if (response.status == 200) {
                    toastr["success"]("Cập nhật thành công")
                } else {
                    toastr["error"]("Cập nhât thất bại. Lỗi bất định")
                }
                $location.path("/list");
            }).catch(function (error) {
            console.log(error);
            toastr["error"]("Cập nhật thất bại");
            if (error.status === 400) {
                $scope.khachHangForm.hoTen.$dirty = false;
                $scope.khachHangForm.gioiTinh.$dirty = false;
                $scope.khachHangForm.ngaySinh.$dirty = false;
                $scope.khachHangForm.soDienThoai.$dirty = false;
                $scope.khachHangForm.email.$dirty = false;
                $scope.errors = error.data;
            }
        })
    };
});




