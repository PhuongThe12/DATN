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
        .otherwise({ redirectTo: '/list' });
});
app.controller('addGiayController', function ($scope, DetailEntityService) {

    // $scope.modalCountry = "modalCountry";
    // $scope.modalProvince = "modalProvince";

    $scope.selectedCoGiay = {};

    $scope.selectedLotGiay = {};


    $scope.selectedMauSac = [];
    $scope.selectedKichThuoc = [];


    $scope.selectedMauSacs = [];

    $scope.changeSelectedKichThuoc = function () {
        // Sao chép selectedMauSacs
        var selectedMauSacsCopy = angular.copy($scope.selectedMauSacs);

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
                return ms.id === mauSac.id && ms.status == 'active'; // update thì bỏ && ...
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
                    return kth.id === kt.id && kth.status == 'active';
                });
            });
        });

        // Cập nhật $scope.selectedMauSacs
        $scope.selectedMauSacs = selectedMauSacsCopy;

    }

    $scope.getDetailEntity = function () {
        event.stopPropagation();
        $scope.detailEntity = DetailEntityService.getDetailEntity();
    }

    $scope.showSelectedColorImage = function (e, input) {
        $scope.$apply(function () {
            if (e.target.files && e.target.files[0]) {
                loadImage(e.target.files[0], input.id.replace('imageColorInput', 'selectedColorImage'));
                document.getElementById('imageTextContainer' + input.id.replace('imageColorInput', '')).style.display = 'none';
                input.parentElement.nextElementSibling.style.display = 'block';
            }
        });
    }

    //show selected image
    $scope.showSelectedImage = function (e, image) {
        $scope.$apply(function () {
            if (e.target.files && e.target.files[0]) {
                switch (image) {
                    case 2:
                        $scope.image2 = event.target.files[0];
                        loadImage($scope.image2, 'selectedImage2');
                        break;
                    case 3:
                        $scope.image3 = event.target.files[0];
                        loadImage($scope.image3, 'selectedImage3');
                        break;
                    case 4:
                        $scope.image4 = event.target.files[0];
                        loadImage($scope.image4, 'selectedImage4');
                        break;
                    case 5:
                        $scope.image5 = event.target.files[0];
                        loadImage($scope.image5, 'selectedImage5');
                        break;
                    default:
                        $scope.image1 = event.target.files[0];
                        loadImage($scope.image1, 'selectedImage1');
                        break;
                }
            }

        });

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
        $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].hinhAnh = null;

        if (e.target.tagName == 'I') {
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
    $scope.lotGiays = [
        {
            "id": 1,
            "ten": "BOOST",
            "moTa": "Ra m?t l?n d?u tiên vào nam 2013, công ngh? BOOST c?a Adidas dã gây lên “con s?t” trên toàn c?u. Boost là ch?t li?u d?m giày có kh? nang hoàn tr? nang lu?ng t?i uu nh?t so v?i nh?ng ch?t li?u khác.",
            "trangThai": 1
        },
        {
            "id": 3,
            "ten": "CLOUDFOAM",
            "moTa": "B?n có th? c?m nh?n dôi giày này nhu chính cái tên c?a nó Cloudfoam – b?t mây ngay t? khi d?t chân vào dôi giày. Nh? nhàng",
            "trangThai": 1
        }
    ];

    //Select de giay
    $scope.deGiays = [
        {
            "id": 1,
            "ten": "BOOST",
            "moTa": "Ra m?t l?n d?u tiên vào nam 2013, công ngh? BOOST c?a Adidas dã gây lên “con s?t” trên toàn c?u. Boost là ch?t li?u d?m giày có kh? nang hoàn tr? nang lu?ng t?i uu nh?t so v?i nh?ng ch?t li?u khác.",
            "trangThai": 1
        },
        {
            "id": 3,
            "ten": "CLOUDFOAM",
            "moTa": "B?n có th? c?m nh?n dôi giày này nhu chính cái tên c?a nó Cloudfoam – b?t mây ngay t? khi d?t chân vào dôi giày. Nh? nhàng",
            "trangThai": 1
        }
    ];

    //Select mui giay
    $scope.muiGiays = [
        {
            "id": 1,
            "ten": "Mui giay 1",
            "moTa": "Mui giay",
            "trangThai": 1
        },
        {
            "id": 2,
            "ten": "Round Toe",
            "moTa": "Mui giày d?u tròn (Round Toe) là m?t lo?i mui giày ph? bi?n trong h?u h?t các lo?i giày th? thao. Ð?c di?m chính c?a mui giày d?u tròn là hình d?ng tròn",
            "trangThai": 1
        },
        {
            "id": 3,
            "ten": "Pointed Toe",
            "moTa": "Mui giày d?u nh?n (Pointed Toe) là m?t lo?i mui giày có hình d?ng hoi nh?n ? ph?n d?u. Thu?ng du?c s? d?ng trong các lo?i giày th? thao th?i trang và mang l?i m?t di?n m?o sang tr?ng và n?i b?t. ",
            "trangThai": 1
        },
        {
            "id": 4,
            "ten": "Square Toe",
            "moTa": "Mui giày vuông (Square Toe) là m?t lo?i mui giày có hình d?ng vuông góc ? ph?n d?u. Ðây là m?t ki?u mui giày ph? bi?n trong các lo?i giày th? thao nhu giày bóng r?",
            "trangThai": 1
        }
    ];

    //Select co giay

    //Select thuong hieu

    //Select chat lieu

    //Select day giay

    //Select mau sac
    $scope.mauSacs = [
        {
            "id": 1,
            "maMau": "#e6e6fa",
            "ten": "Tím nh?t",
            "moTa": "123456",
            "trangThai": 1
        },
        {
            "id": 2,
            "maMau": "#0000ff",
            "ten": "xanh duong",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 3,
            "maMau": null,
            "ten": "I",
            "moTa": null,
            "trangThai": 0
        },
        {
            "id": 10002,
            "maMau": null,
            "ten": "Cat",
            "moTa": null,
            "trangThai": 0
        },
        {
            "id": 10003,
            "maMau": null,
            "ten": "g",
            "moTa": null,
            "trangThai": 0
        },
        {
            "id": 10004,
            "maMau": null,
            "ten": "A",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10005,
            "maMau": null,
            "ten": "phuong2",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10006,
            "maMau": null,
            "ten": "phuongthe3",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10007,
            "maMau": null,
            "ten": "phuongthe4",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10008,
            "maMau": null,
            "ten": "phuongthe5",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10009,
            "maMau": null,
            "ten": "phuongthe6",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10010,
            "maMau": null,
            "ten": "phuongthe7",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10011,
            "maMau": null,
            "ten": "phuongthe8",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10012,
            "maMau": null,
            "ten": "Tr?ng",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10013,
            "maMau": null,
            "ten": "Ðen",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 10014,
            "maMau": null,
            "ten": "Xanh",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 20004,
            "maMau": "#0000ff",
            "ten": "xanh duong",
            "moTa": null,
            "trangThai": 1
        }
    ];

    //Select size
    $scope.kichThuocs = [
        {
            "id": 1,
            "ten": "36",
            "chieuDai": 23.0,
            "chieuRong": 9.0,
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 2,
            "ten": "37",
            "chieuDai": 23.5,
            "chieuRong": 10.0,
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 3,
            "ten": "38",
            "chieuDai": 24.0,
            "chieuRong": 9.5,
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 4,
            "ten": "39",
            "chieuDai": 24.5,
            "chieuRong": 9.5,
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 5,
            "ten": "40",
            "chieuDai": 25.0,
            "chieuRong": 10.0,
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 6,
            "ten": "42",
            "chieuDai": 26.0,
            "chieuRong": 10.5,
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 7,
            "ten": "41",
            "chieuDai": 25.5,
            "chieuRong": 10.0,
            "moTa": null,
            "trangThai": 1
        }
    ]

    //Select hashtag
    $scope.hashTags = [
        {
            "id": 1,
            "ten": "giaydep",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 2,
            "ten": "giaysinhvien",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 3,
            "ten": "giaynam",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 4,
            "ten": "giaynike",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 1,
            "ten": "giaydep",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 2,
            "ten": "giaysinhvien",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 3,
            "ten": "giaynam",
            "moTa": null,
            "trangThai": 1
        },
        {
            "id": 4,
            "ten": "giaynike",
            "moTa": null,
            "trangThai": 1
        }
    ]

    $scope.send = function () {
        $scope.selectedMauSacs.forEach(function (varient) {
            varient.hinhAnh = document.getElementById('imageColorInput' + varient.id).files[0];
        });
    }

});

app.controller('updateGiayController', function ($scope, DetailEntityService) {

});

app.controller('giayListController', function ($scope) {

});

app.controller ('detailGiayController', function ($scope) {

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

            if ($scope.ngModel && $scope.items) {
                var selectedItem = $scope.items.find((item) => item.id == $scope.ngModel.id);
                if (selectedItem) { $scope.selectItem(selectedItem); }
            }

            $scope.showDetailComboboxModal = function (title, item) {
                event.stopPropagation();
                var DetailEntityService = $injector.get('DetailEntityService');
                let detailEntity = { title: title, ...item };
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

                if ($scope.selectType == "mausac") {
                    setTimeout(function () {
                        document.getElementById('inputChangeMauSac').click();
                    }, 0);
                } else if ($scope.selectType == "kichthuoc") {
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
                if ($scope.selectType == "mausac") {
                    setTimeout(function () {
                        document.getElementById('inputChangeMauSac').click();
                    }, 0);
                } else if ($scope.selectType == "kichthuoc") {
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

