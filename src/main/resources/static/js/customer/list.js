app.controller('listProductController', function ($scope, $http, $location) {
    $scope.giays = [];
    $scope.curPage = 1, $scope.itemsPerPage = 999, $scope.maxSize = 9999;

    $scope.curPage = 1,
        $scope.itemsPerPage = 12,
        $scope.maxSize = 12;

    let giaySearch = {};

    function getData(currentPage) {
        $scope.isLoading = true;
        let apiUrl = host + '/rest/admin/giay/get-all-giay';

        if ($scope.searchText) {
            giaySearch.ten = ($scope.searchText + "").trim();
        }
        giaySearch.currentPage = currentPage;
        giaySearch.pageSize = $scope.itemsPerPage;
        $http.post(apiUrl, giaySearch)
            .then(function (response) {
                $scope.giays = response.data.content;
                for (let bienTheGiayObject of $scope.giays) {
                    let lstBienTheGiay = bienTheGiayObject.lstBienTheGiay;
                    let giaThapNhat = lstBienTheGiay[0].giaBan;
                    let giaLonNhat = lstBienTheGiay[0].giaBan;

                    let uniqueMauSacSet = new Set();

                    let lstMauSac = [];

                    for (let bienTheGiay of lstBienTheGiay) {
                        if (bienTheGiay.giaBan < giaThapNhat) {
                            giaThapNhat = bienTheGiay.giaBan;
                        }

                        if (bienTheGiay.giaBan > giaLonNhat) {
                            giaLonNhat = bienTheGiay.giaBan;
                        }
                        if (bienTheGiay.mauSac && bienTheGiay.mauSac.maMau) {
                            let maMau = bienTheGiay.mauSac.maMau;

                            if (!uniqueMauSacSet.has(maMau)) {
                                lstMauSac.push(maMau);
                                uniqueMauSacSet.add(maMau);
                            }
                        }
                    }
                    bienTheGiayObject.giaThapNhat = giaThapNhat;
                    bienTheGiayObject.giaLonNhat = giaLonNhat;
                    bienTheGiayObject.lstMauSac = lstMauSac;
                }
                $scope.numOfPages = response.data.totalPages;
                $scope.isLoading = false;
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $scope.isLoading = false;
                // window.location.href = feHost + '/trang-chu';
            });
    }

    getData(1);

    $scope.$watch('curPage + numPerPage', function () {
        getData($scope.curPage);
    });

    $scope.lstSearchChoosed = [];
    $scope.dropdowns = [
        {
            ten: "Màu Sắc",
            listValue: [
                {name: "#FF0000", selected: false},  // Đỏ
                {name: "#00FF00", selected: false},  // Xanh lá cây
                {name: "#0000FF", selected: false},  // Xanh dương
                {name: "#FFFF00", selected: false},  // Vàng
                {name: "#FF00FF", selected: false},  // Hồng
                {name: "#00FFFF", selected: false},  // Lam
                {name: "#000000", selected: false}   // Đen
            ],
            isOpen: true
        },
        {
            ten: "Kích Cỡ", listValue: [
                {
                    name: 35, selected: false
                },
                {
                    name: 36, selected: false
                },
                {
                    name: 37, selected: false
                },
                {
                    name: 38, selected: false
                },
                {
                    name: 39, selected: false
                },
                {
                    name: 40, selected: false
                }, {
                    name: 35, selected: false
                },
                {
                    name: 36, selected: false
                },
                {
                    name: 37, selected: false
                },
                {
                    name: 38, selected: false
                },
                {
                    name: 39, selected: false
                },
                {
                    name: 40, selected: false
                }, {
                    name: 35, selected: false
                },
                {
                    name: 36, selected: false
                },
                {
                    name: 37, selected: false
                },
                {
                    name: 38, selected: false
                },
                {
                    name: 39, selected: false
                },
                {
                    name: 40, selected: false
                }
            ],
            isOpen: true
        },
        {
            ten: "Chất Liệu",
            listValue: [{name: "Leather", selected: false}, {name: "Mesh", selected: false}, {
                name: "Canvas",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Thương Hiệu",
            listValue: [{name: "Nike", selected: false}, {name: "Adidas", selected: false}, {
                name: "Puma",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Cổ Giày",
            listValue: [{name: "Low-top", selected: false}, {name: "High-top", selected: false}],
            isOpen: true
        },
        {
            ten: "Đế Giày",
            listValue: [{name: "Rubber", selected: false}, {name: "EVA", selected: false}, {
                name: "Phylon",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Mũi Giày",
            listValue: [{name: "Round Toe", selected: false}, {name: "Square Toe", selected: false}],
            isOpen: true
        },
        {
            ten: "Lót Giày",
            listValue: [{name: "Gel", selected: false}, {name: "Foam", selected: false}, {
                name: "Ortholite",
                selected: false
            }],
            isOpen: true
        },
        {
            ten: "Dây Giày",
            listValue: [{name: "Flat", selected: false}, {name: "Round", selected: false}, {
                name: "Waxed",
                selected: false
            }],
            isOpen: true
        },

    ];


    $scope.toggleDropdown = function (index) {
        $scope.dropdowns[index].isOpen = !$scope.dropdowns[index].isOpen;
    };

    $scope.changeSearch = function () {
        $scope.lstSearchChoosed = [];
        for (let dropdown of $scope.dropdowns) {
            for (let value of dropdown.listValue) {
                if (value.selected) {
                    $scope.lstSearchChoosed.push({
                        dropdownName: dropdown.ten,
                        selectedValue: value.name,
                    });
                }
            }
        }
    };
});