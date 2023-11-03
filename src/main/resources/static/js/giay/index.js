var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/admin/giay/views/list.html',
            controller: 'giayListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/admin/giay/views/detail.html',
        controller: 'detailGiayController'
    }).when("/update/:id", {
        templateUrl: '/pages/admin/giay/views/update.html',
        controller: 'updateGiayController'
    }).when("/add", {
        templateUrl: '/pages/admin/giay/views/add.html',
        controller: 'addGiayController'
    })
        .otherwise({redirectTo: '/list'});
});
app.controller('addGiayController', function ($scope, $http, $location, DetailEntityService) {

    $scope.selectedCoGiay = {};

    $scope.selectedMauSac = [];
    $scope.selectedKichThuoc = [];
    $scope.selectedHashTag = [];

    $scope.selectedMauSacs = [];
    $scope.selectedKichThuocs = [];

    $scope.changeSelectedKichThuoc = function () {
        // Sao chép selectedMauSacs
        let selectedMauSacsCopy = angular.copy($scope.selectedMauSacs);
        const length = $scope.selectedMauSacs.length;
        for (let i = 0; i < length; i++) {
            selectedMauSacsCopy[i].hinhAnh = $scope.selectedMauSacs[i].hinhAnh;
        }

        // Kiểm tra và cập nhật selectedMauSacs dựa trên selectedMauSac
        $scope.selectedMauSac.forEach(function (mauSac) {
            var existingMauSac = selectedMauSacsCopy.find(function (ms) {
                return ms.id === mauSac.id;
            });

            if (!existingMauSac) {
                // Nếu màu sắc chưa tồn tại, thêm vào selectedMauSacs
                mauSac.selectedKichThuocs = [];
                selectedMauSacsCopy.push(angular.copy(mauSac));
            }

            //update cần
            // var existingMauSac = selectedMauSacsCopy.find(function (ms) {
            //     return ms.id === mauSac.id && mauSac.status == 'disabled';
            // });

            // if (existingMauSac) {
            //     existingMauSac.status = 'disabled';
            // }

        });

        // Xóa các màu không tồn tại trong selectedMauSac
        selectedMauSacsCopy = selectedMauSacsCopy.filter(function (mauSac) {
            return $scope.selectedMauSac.some(function (ms) {
                return ms.id === mauSac.id && ms.status === 'active'; // update thì bỏ && ...
            });
        });

        // Kiểm tra và cập nhật selectedKichThuocs dựa trên selectedKichThuoc
        $scope.selectedKichThuoc.forEach(function (kichThuoc) {
            selectedMauSacsCopy.forEach(function (mauSac) {
                var existingKichThuoc = mauSac.selectedKichThuocs.find(function (kt) {
                    return kt.id === kichThuoc.id;
                });

                if (!existingKichThuoc) {
                    mauSac.selectedKichThuocs.push(angular.copy(kichThuoc));
                }
            });
        });

        // Xóa các kích thước không tồn tại trong selectedKichThuoc
        selectedMauSacsCopy.forEach(function (mauSac) {
            mauSac.selectedKichThuocs = mauSac.selectedKichThuocs.filter(function (kt) {
                return $scope.selectedKichThuoc.some(function (kth) {
                    return kth.id === kt.id && kth.status === 'active';
                });
            });
        });


        // Cập nhật $scope.selectedMauSacs
        $scope.selectedMauSacs = selectedMauSacsCopy;

        setTimeout(function () {
            $scope.selectedMauSacs.forEach(mauSac => {
                if (mauSac.hinhAnh) {
                    loadImage(mauSac.hinhAnh, 'selectedColorImage' + mauSac.id);
                    const imageElement = document.getElementById('selectedColorImage' + mauSac.id);
                    imageElement.parentElement.nextElementSibling.style.display = 'none';
                    imageElement.parentElement.parentElement.nextElementSibling.style.display = 'block';
                }
            })
        }, 0);


    }

    $scope.getDetailEntity = function () {
        event.stopPropagation();
        $scope.detailEntity = DetailEntityService.getDetailEntity();
    }

    $scope.showSelectedColorImage = function (e, input) {
        $scope.$apply(function () {
            if (e.target.files && e.target.files[0]) {
                const file = e.target.files[0];
                if (!isImage(file.name)) {
                    toastr["error"]("Chỉ cho phép định dạng jpg/png/jpeg");
                    return;
                }
                loadImage(file, input.id.replace('imageColorInput', 'selectedColorImage'));
                document.getElementById('imageTextContainer' + input.id.replace('imageColorInput', '')).style.display = 'none';
                $scope.selectedMauSacs[input.getAttribute("ms-index")].hinhAnh = file;
                input.parentElement.nextElementSibling.style.display = 'block';
            }
        });
    }

    //show selected image
    $scope.showSelectedImage = function (e, image) {
        $scope.$apply(function () {
            if (e.target.files && e.target.files[0]) {
                const file = event.target.files[0];
                if (!isImage(file.name)) {
                    toastr["error"]("Chỉ cho phép định dạng jpg/png/jpeg");
                    return;
                }
                switch (image) {
                    case 2:
                        $scope.image2 = file;
                        loadImage($scope.image2, 'selectedImage2');
                        break;
                    case 3:
                        $scope.image3 = file;
                        loadImage($scope.image3, 'selectedImage3');
                        break;
                    case 4:
                        $scope.image4 = file;
                        loadImage($scope.image4, 'selectedImage4');
                        break;
                    case 5:
                        $scope.image5 = file;
                        loadImage($scope.image5, 'selectedImage5');
                        break;
                    default:
                        $scope.image1 = file;
                        loadImage($scope.image1, 'selectedImage1');
                        break;
                }
            }

        });

    }

    function isImage(fileName) {
        const allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;
        return allowedExtensions.test(fileName);
    }

    //Load selected image function
    function loadImage(file, imgId) {
        var reader = new FileReader();
        reader.onload = function (e) {
            const img = document.getElementById(imgId);
            img.src = e.target.result;
            img.style.display = 'block';
        };
        reader.readAsDataURL(file);
    }

    $scope.removeImageColor = function (e, id, msIndex, ktIndex) {
        document.getElementById('imageTextContainer' + id).style.display = 'flex';
        document.getElementById('selectedColorImage' + id).src = '';
        document.getElementById('selectedColorImage' + id).style.display = 'none';
        document.getElementById('imageColorInput' + id).value = '';
        $scope.selectedMauSacs[msIndex].hinhAnh = null;

        if (e.target.tagName === 'I') {
            e.target.parentElement.style.display = 'none';
        } else {
            e.target.style.display = 'none';
        }
    }

    //remove selected image
    $scope.removeImage = function (image) {
        for (let i = image; i <= 5; i++) {
            if (i === 5) {
                $scope['image' + i] = null;
                document.getElementById('selectedImage' + i).src = '';
                document.getElementById('selectedImage' + i).style.display = 'none';
            } else if ($scope['image' + (i + 1)]) {
                $scope['image' + i] = $scope['image' + (i + 1)];
                loadImage($scope['image' + i], 'selectedImage' + i);
            } else {
                $scope['image' + i] = null;
                document.getElementById('selectedImage' + i).src = '';
                document.getElementById('selectedImage' + i).style.display = 'none';
            }
        }
    };

    //Select lot giay
    $scope.lotGiays = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/lot-giay/get-all")
            .then(function (response) {
                $scope.lotGiays = response.data;
                $scope.selectedLotGiay = $scope.lotGiays[0] ? $scope.lotGiays[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu lót giày thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select de giay
    $scope.deGiays = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/de-giay/get-all")
            .then(function (response) {
                $scope.deGiays = response.data;
                $scope.selectedDeGiay = $scope.deGiays[0] ? $scope.deGiays[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu đế giày thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);
    //Select mui giay
    $scope.muiGiays = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/mui-giay/get-all")
            .then(function (response) {
                $scope.muiGiays = response.data;
                $scope.selectedMuiGiay = $scope.muiGiays[0] ? $scope.muiGiays[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu mũi giày thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select co giay
    $scope.coGiays = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/co-giay/get-all")
            .then(function (response) {
                $scope.coGiays = response.data;
                $scope.selectedCoGiay = $scope.coGiays[0] ? $scope.coGiays[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu cổ giày thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select thuong hieu
    $scope.thuongHieus = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/thuong-hieu/get-all")
            .then(function (response) {
                $scope.thuongHieus = response.data;
                $scope.selectedThuongHieu = $scope.thuongHieus[0] ? $scope.thuongHieus[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thương hiệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select chat lieu
    $scope.chatLieus = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/chat-lieu/get-all")
            .then(function (response) {
                $scope.chatLieus = response.data;
                $scope.selectedChatLieu = $scope.chatLieus[0] ? $scope.chatLieus[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu chất liệu thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select day giay
    $scope.dayGiays = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/day-giay/get-all")
            .then(function (response) {
                $scope.dayGiays = response.data;
                $scope.selectedDayGiay = $scope.dayGiays[0] ? $scope.dayGiays[0] : {};
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu dây giày thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select mau sac
    $scope.mauSacs = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/mau-sac/get-all")
            .then(function (response) {
                $scope.mauSacs = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu màu sắc thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select size
    $scope.kichThuocs = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/kich-thuoc/get-all")
            .then(function (response) {
                $scope.kichThuocs = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu kích thước thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    //Select hashtag
    $scope.hashTags = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/hash-tag/get-all")
            .then(function (response) {
                $scope.hashTags = response.data;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu hash tag thất bại");
                // window.location.href = feHost + '/trang-chu';
            });
    }, 0);

    $scope.setBarcode = function (msIndex, ktIndex) {
        $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = new Date().getTime();
    }

    $scope.submitData = function () {

        if (!isValid()) {
            return;
        }

        let giayRequest = {};

        let imagePromises = [];
        if ($scope.image1) {
            imagePromises.push(getBase64($scope.image1).then(data => giayRequest.image1 = data + ""));
        }
        if ($scope.image2) {
            imagePromises.push(getBase64($scope.image2).then(data => giayRequest.image2 = data + ""));
        }
        if ($scope.image3) {
            imagePromises.push(getBase64($scope.image3).then(data => giayRequest.image3 = data + ""));
        }
        if ($scope.image4) {
            imagePromises.push(getBase64($scope.image4).then(data => giayRequest.image4 = data + ""));
        }
        if ($scope.image5) {
            imagePromises.push(getBase64($scope.image5).then(data => giayRequest.image5 = data + ""));
        }

        giayRequest.ten = $scope.ten;
        giayRequest.namSX = $scope.namSX;
        giayRequest.lotGiayId = $scope.selectedLotGiay.id;
        giayRequest.muiGiayId = $scope.selectedMuiGiay.id;
        giayRequest.coGiayId = $scope.selectedCoGiay.id;
        giayRequest.thuongHieuId = $scope.selectedThuongHieu.id;
        giayRequest.chatLieuId = $scope.selectedChatLieu.id;
        giayRequest.dayGiayId = $scope.selectedDayGiay.id;
        giayRequest.deGiayId = $scope.selectedDeGiay.id;
        giayRequest.trangThai = $scope.trangThai;
        giayRequest.moTa = $scope.moTa;
        giayRequest.hashTagIds = $scope.selectedHashTag.map(hashTag => hashTag.id);
        giayRequest.mauSacImages = {};

        let bienTheGiays = [];

        async function processMauSac(mauSac) {
            if (mauSac.hinhAnh) {
                let mauSacImage = await getBase64(mauSac.hinhAnh);
                giayRequest.mauSacImages[mauSac.id] = mauSacImage + "";
            }
            mauSac.selectedKichThuocs.forEach(kichThuoc => {
                let bienTheGiay = {
                    mauSacId: mauSac.id,
                    kichThuocId: kichThuoc.id,
                    giaNhap: kichThuoc.giaNhap,
                    soLuong: kichThuoc.soLuong,
                    giaBan: kichThuoc.giaBan,
                    trangThai: kichThuoc.trangThai,
                    barcode: kichThuoc.barcode
                }
                bienTheGiays.push(bienTheGiay);
            });
        }

        Promise.all(imagePromises)
            .then(() => {
                Promise.all($scope.selectedMauSacs.map(processMauSac))
                    .then(() => {
                        giayRequest.bienTheGiays = bienTheGiays;

                        $http.post(host + '/admin/rest/giay/add', JSON.stringify(giayRequest))
                            .then(function (response) {
                                toastr["success"]("Thêm thành công");
                                $location.path("/list");
                            })
                            .catch(function (error) {
                                console.log(error);
                                $scope.addGiayForm.ten.$dirty = false;
                                $scope.addGiayForm.moTa.$dirty = false;
                                $scope.addGiayForm.namSX.$dirty = false;
                                $scope.errors = error.data;
                                toastr["error"]("Thêm thất bại");
                                // window.location.href = feHost + '/trang-chu';
                            });
                    })
                    .catch(error => {
                        console.error("Lỗi xử lý dữ liệu:", error);
                    });
            });


    };
    let isValid = function () {
        $scope.selectedMauSacs[0].selectedKichThuocs[0].errors = {};
        if (!$scope.image1) {

        }
        $scope.selectedMauSacs[0].selectedKichThuocs[0].errors.giaNhap = 'Giá nhập không được để trống';
        return false;
    }

    function getBase64(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = error => reject(error);
        });
    }

    $scope.changeInput = function (msIndex, ktIndex, model) {
        if (model === 'giaNhap' && $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors) {
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaNhap = null;
        }
    }

    $scope.blurInput = function (msIndex, ktIndex, model) {
        $scope.selectedMauSacs[0].selectedKichThuocs[0].errors = {};
        if (model === 'giaNhap' && isNaN($scope.selectedMauSacs[0].selectedKichThuocs[0].giaNhap)) {
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaNhap = 'Giá nhập không được để trống';
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].giaNhap = '';
        }
    }

});


app.controller('updateGiayController', function ($scope, DetailEntityService) {

});

app.controller('detailGiayController', function ($scope) {

});

//combobox chung
app.directive('customSelect', function ($injector) {
    return {
        restrict: 'E',
        templateUrl: '/pages/admin/giay/views/combobox.html',
        scope: {
            id: '@',
            title: '@',
            items: '=',
            ngModel: '=',
            modalId: '@'
        },
        controller: function ($scope) {
            $scope.isActive = false;

            $scope.toggleDropdown = function () {
                $scope.isActive = !$scope.isActive;
            };

            $scope.selectItem = function (item) {
                $scope.ngModel = item;
                $scope.selectedItem = item;
                $scope.isActive = false;
            };

            $scope.$watch('ngModel', function (newNgModel, oldNgModel) {
                if (!oldNgModel && newNgModel || newNgModel && newNgModel.id !== oldNgModel.id) {
                    if ($scope.items) {
                        var selectedItem = $scope.items.find((item) => item.id === newNgModel.id);
                        if (selectedItem) {
                            $scope.selectItem(selectedItem);
                        }
                    }
                }
            }, true);

            $scope.showDetailComboboxModal = function (title, item) {
                event.stopPropagation();
                var DetailEntityService = $injector.get('DetailEntityService');
                let detailEntity = {title: title, ...item};
                DetailEntityService.setDetailEntity(detailEntity);

                setTimeout(function () {
                    document.getElementById('modalDetail').click();
                }, 0);

            }

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

});

//input chung
app.directive('customInput', function () {
    return {
        restrict: 'E',
        templateUrl: '/pages/admin/giay/views/input.html',
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

                if ($scope.selectType === "mausac") {
                    setTimeout(function () {
                        document.getElementById('inputChangeMauSac').click();
                    }, 0);
                } else if ($scope.selectType === "kichthuoc") {
                    setTimeout(function () {
                        document.getElementById('inputChangeKichThuoc').click();
                    }, 0);
                }

            };

            if ($scope.ngModel && $scope.ngModel.length > 0) {
                $scope.ngModel.forEach((element) => {
                    var selectedItem = $scope.items.find((item) => item.id === element.id);
                    $scope.selectItem(selectedItem);
                });
            }

            $scope.removeTag = function (tag) {
                const index = $scope.selectedTags.indexOf(tag);
                if (index !== -1) {
                    // $scope.selectedTags.splice(index, 1);
                    tag.status = 'disabled';
                    $scope.items.push(tag);
                }
                if ($scope.selectType === "mausac") {
                    setTimeout(function () {
                        document.getElementById('inputChangeMauSac').click();
                    }, 0);
                } else if ($scope.selectType === "kichthuoc") {
                    setTimeout(function () {
                        document.getElementById('inputChangeKichThuoc').click();
                    }, 0);
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
});

app.service('DetailEntityService', function () {
    var detailEntity = {};

    return {
        getDetailEntity: function () {
            return detailEntity;
        },
        setDetailEntity: function (entity) {
            detailEntity = entity;
        }
    };
});

function nextInput (e) {
    if (e.which === 13) {
        e.target.focus = false;
    }
}
