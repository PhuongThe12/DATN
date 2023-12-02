app.controller('updateGiayController', function ($scope, $http, $location, $routeParams, DetailEntityService) {

    const id = $routeParams.id;

    $scope.errors = {};
    $scope.selectedCoGiay = {};

    $scope.selectedMauSac = [];
    $scope.selectedMauSacs = [];
    $scope.selectedKichThuoc = [];
    $scope.selectedHashTag = [];

    $scope.selectedMauSacs = [];
    $scope.selectedKichThuocs = [];

    $scope.giay = {};

    let giayClone;

    $scope.isLoading = true;

    setTimeout(function () {
        $http.get(host + '/admin/rest/giay/' + id)
            .then(function (response) {
                $scope.giay = response.data;
                setData();
                giayClone = angular.copy($scope.giay);
                $scope.isLoading = false;
                setTimeout(() => {
                    document.getElementById('tenGiay').click();
                }, 20);
            })
            .catch(function (error) {
                console.log(error);
                toastr["error"]("Lấy dữ liệu thất bại");
                $location.path("/list");
                // $scope.isLoading = false;
            });
    }, 10);


    $scope.changeSelectedKichThuoc = function () {
        // Sao chép selectedMauSacs
        let selectedMauSacsCopy = angular.copy($scope.selectedMauSacs);
        const length = $scope.selectedMauSacs.length;
        for (let i = 0; i < length; i++) {
            selectedMauSacsCopy[i].hinhAnh = $scope.selectedMauSacs[i].hinhAnh;
        }
        // Kiểm tra và cập nhật selectedMauSacs dựa trên selectedMauSac
        $scope.selectedMauSac.forEach(function (mauSac) {
            let existingMauSac = selectedMauSacsCopy.find(function (ms) {
                return ms.id === mauSac.id || mauSac.status === 'disabled';
            });

            if (!existingMauSac) {
                // Nếu màu sắc chưa tồn tại, thêm vào selectedMauSacs
                mauSac.selectedKichThuocs = [];
                selectedMauSacsCopy.push(angular.copy(mauSac));
            }

        });


        // Xóa các màu không tồn tại trong selectedMauSac
        selectedMauSacsCopy = selectedMauSacsCopy.filter(function (mauSac) {
            return $scope.selectedMauSac.some(function (ms) {
                return ms.id === mauSac.id && ms.status === 'active';
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
                if (mauSac.hinhAnh === 1) {
                    const base64 = $scope.giay.mauSacImages[mauSac.id];
                    if (base64) {
                        loadImageFromBase64(base64, 'selectedColorImage' + mauSac.id);
                        const imageElement = document.getElementById('selectedColorImage' + mauSac.id);
                        imageElement.parentElement.nextElementSibling.style.display = 'none';
                        imageElement.parentElement.parentElement.nextElementSibling.style.display = 'block';
                    }
                } else if (mauSac.hinhAnh !== 1 && mauSac.hinhAnh) {
                    loadImage(mauSac.hinhAnh, 'selectedColorImage' + mauSac.id);
                    const imageElement = document.getElementById('selectedColorImage' + mauSac.id);
                    imageElement.parentElement.nextElementSibling.style.display = 'none';
                    imageElement.parentElement.parentElement.nextElementSibling.style.display = 'block';

                }
                mauSac.selectedKichThuocs.forEach(kichThuoc => {
                    kichThuoc.errors = {};
                });
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
                        if (file.size > 5 * 1024 * 1024) {
                            toastr["error"]("File không được vượt quá 5MB");
                            break;
                        }
                        $scope.image2 = file;
                        loadImage($scope.image2, 'selectedImage2');
                        break;
                    case 3:
                        if (file.size > 5 * 1024 * 1024) {
                            toastr["error"]("File không được vượt quá 5MB");
                            break;
                        }
                        $scope.image3 = file;
                        loadImage($scope.image3, 'selectedImage3');
                        break;
                    case 4:
                        if (file.size > 5 * 1024 * 1024) {
                            toastr["error"]("File không được vượt quá 5MB");
                            break;
                        }
                        $scope.image4 = file;
                        loadImage($scope.image4, 'selectedImage4');
                        break;
                    case 5:
                        if (file.size > 5 * 1024 * 1024) {
                            toastr["error"]("File không được vượt quá 5MB");
                            break;
                        }
                        $scope.image5 = file;
                        loadImage($scope.image5, 'selectedImage5');
                        break;
                    default:
                        if (file.size > 5 * 1024 * 1024) {
                            toastr["error"]("File không được vượt quá 5MB");
                            break;
                        }
                        $scope.image1 = file;
                        $scope.errors.anh = null;
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

    function loadImageFromBase64(base64, imgId) {
        const img = document.getElementById(imgId);
        img.src = base64;
        img.style.display = 'block';
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
        // $scope['image' + image] = null;
        // document.getElementById('selectedImage' + image).src = '';
        // document.getElementById('selectedImage' + image).style.display = 'none';
        for (let i = image; i <= 5; i++) {
            if (i === 5) {
                $scope['image' + i] = null;
                document.getElementById('selectedImage' + i).src = '';
                document.getElementById('selectedImage' + i).style.display = 'none';
                document.getElementById('imageInput' + i).value = '';
            } else if ($scope['image' + (i + 1)]) {
                $scope['image' + i] = $scope['image' + (i + 1)];
                if ($scope['image' + i] === 1) {
                    const img = document.getElementById('selectedImage' + i);
                    img.src = $scope.giay.lstAnh[i];
                    img.style.display = 'block';
                } else {
                    loadImage($scope['image' + i], 'selectedImage' + i);
                }
                document.getElementById('imageInput' + (i + 1)).value = '';
            } else {
                $scope['image' + i] = null;
                document.getElementById('selectedImage' + i).src = '';
                document.getElementById('selectedImage' + i).style.display = 'none';
                document.getElementById('imageInput' + i).value = '';
            }
        }
    };

//Select lot giay
    $scope.lotGiays = [];
    setTimeout(function () {
        $http.get(host + "/admin/rest/lot-giay/get-all")
            .then(function (response) {
                $scope.lotGiays = response.data;
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
        $scope.blurInput(msIndex, ktIndex, 'barcode');
    }

    $scope.comfirmUpdate = function () {
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
                $scope.submitData();
            }

        });
    }

    $scope.submitData = function () {
        $scope.isLoading = true;
        if (!isValid()) {
            $scope.isLoading = false;
            return;
        }

        let giayRequest = {};

        let imagePromises = [];
        if ($scope.image1 !== 1 && $scope.image1) {
            imagePromises.push(getBase64($scope.image1).then(data => giayRequest.image1 = data + ""));
        } else if (!$scope.image1) {
            giayRequest.image1 = null;
        } else {
            giayRequest.image1 = 1;
        }
        if ($scope.image2 !== 1 && $scope.image2) {
            imagePromises.push(getBase64($scope.image2).then(data => giayRequest.image2 = data + ""));
        } else if (!$scope.image2) {
            giayRequest.image2 = null;
        } else {
            giayRequest.image2 = 1;
        }
        if ($scope.image3 !== 1 && $scope.image3) {
            imagePromises.push(getBase64($scope.image3).then(data => giayRequest.image3 = data + ""));
        } else if (!$scope.image3) {
            giayRequest.image3 = null;
        } else {
            giayRequest.image3 = 1;
        }
        if ($scope.image4 !== 1 && $scope.image4) {
            imagePromises.push(getBase64($scope.image4).then(data => giayRequest.image4 = data + ""));
        } else if (!$scope.image4) {
            giayRequest.image4 = null;
        } else {
            giayRequest.image4 = 1;
        }
        if ($scope.image5 !== 1 && $scope.image5) {
            imagePromises.push(getBase64($scope.image5).then(data => giayRequest.image5 = data + ""));
        } else if (!$scope.image5) {
            giayRequest.image5 = null;
        } else {
            giayRequest.image5 = 1;
        }

        giayRequest.id = id;
        giayRequest.ten = $scope.giay.ten;
        giayRequest.namSX = $scope.giay.namSX;
        giayRequest.lotGiayId = $scope.selectedLotGiay.id;
        giayRequest.muiGiayId = $scope.selectedMuiGiay.id;
        giayRequest.coGiayId = $scope.selectedCoGiay.id;
        giayRequest.thuongHieuId = $scope.selectedThuongHieu.id;
        giayRequest.chatLieuId = $scope.selectedChatLieu.id;
        giayRequest.dayGiayId = $scope.selectedDayGiay.id;
        giayRequest.deGiayId = $scope.selectedDeGiay.id;
        giayRequest.trangThai = $scope.giay.trangThai;
        giayRequest.moTa = $scope.giay.moTa;
        giayRequest.hashTagIds = $scope.selectedHashTag.filter(hashTag => hashTag.status === 'active').map(hashTag => hashTag.id);
        giayRequest.mauSacImages = {};

        let bienTheGiays = [];

        async function processMauSac(mauSac) {
            if (mauSac.hinhAnh !== 1 && mauSac.hinhAnh) {
                let mauSacImage = await getBase64(mauSac.hinhAnh);
                giayRequest.mauSacImages[mauSac.id] = mauSacImage + "";
            } else if (!mauSac.hinhAnh) {
                giayRequest.mauSacImages[mauSac.id] = null;
            } else {
                giayRequest.mauSacImages[mauSac.id] = 1;
            }
            mauSac.selectedKichThuocs.forEach(kichThuoc => {
                let bienTheGiay = {
                    mauSacId: mauSac.id,
                    kichThuocId: kichThuoc.id,
                    soLuong: kichThuoc.soLuong,
                    giaBan: kichThuoc.giaBan,
                    trangThai: kichThuoc.trangThai === 0 ? 0 : 1,
                    barcode: kichThuoc.barcode,
                    soLuongLoi: kichThuoc.soLuongLoi? kichThuoc.soLuongLoi : 0,
                }
                bienTheGiays.push(bienTheGiay);
            });
        }


        Promise.all(imagePromises)
            .then(() => {
                Promise.all($scope.selectedMauSacs.map(processMauSac))
                    .then(() => {
                        giayRequest.bienTheGiays = bienTheGiays;
                        $http.put(host + '/admin/rest/giay/update/' + id, JSON.stringify(giayRequest))
                            .then(function (response) {
                                toastr["success"]("Cập nhật thành công");
                                $location.path("/list");
                            })
                            .catch(function (error) {
                                if (error.status === 409) {
                                    toastr["error"]("Cập nhật thất bại. Hãy sửa các thông tin lỗi");
                                    if (error.data) {
                                        error.data.forEach(e => {
                                            let name = e.substring(0, e.indexOf(":"));
                                            let error = e.substring(name.length + 1);
                                            if (name === 'ten') {
                                                $scope.errors.ten = error;
                                            } else {
                                                let msIdKtId = name.split(',').map(Number);
                                                if (msIdKtId[0] && msIdKtId[1]) {
                                                    $scope.selectedMauSacs.forEach(ms => {
                                                        ms.selectedKichThuocs.forEach((kt => {
                                                            if (ms.id === msIdKtId[0] && kt.id === msIdKtId[1]) {
                                                                kt.errors.barcode = error;
                                                            }
                                                        }));
                                                    });
                                                }
                                            }
                                        })
                                    }
                                } else {
                                    toastr["error"]("Cập nhật thất bại. Vui lòng thử lại");
                                }
                                $scope.addGiayForm.ten.$dirty = false;
                                $scope.addGiayForm.moTa.$dirty = false;
                                $scope.addGiayForm.namSX.$dirty = false;
                                $scope.isLoading = false;
                                // window.location.href = feHost + '/trang-chu';
                            });
                    })
                    .catch(error => {
                        console.error("Lỗi xử lý dữ liệu:", error);
                        $scope.isLoading = false;
                    });
            });


    };
    let isValid = function () {
        let valid = true;
        let count = 0;
        if (!$scope.image1) {
            $scope.errors.anh = true;
            valid = false;
            count++;
        }

        if (!$scope.giay.ten || $scope.giay.ten.length === 0) {
            valid = false;
            count++;
        } else if ($scope.giay.ten.length > 120) {
            valid = false;
            count++;
        }

        if (!$scope.giay.moTa || $scope.giay.moTa.length === 0) {
            valid = false;
            count++;
        } else if ($scope.giay.moTa.length > 3000) {
            valid = false;
            count++;
        }

        if (!$scope.giay.namSX || isNaN($scope.giay.namSX)) {
            valid = false;
            count++;
        }

        if (!$scope.selectedMauSacs || $scope.selectedMauSacs.length === 0) {
            toastr["error"]("Hãy chọn ít nhất một màu sắc");
            valid = false;
            count++
        } else if (!$scope.selectedMauSacs[0].selectedKichThuocs || $scope.selectedMauSacs[0].selectedKichThuocs.length === 0) {
            toastr["error"]("Hãy chọn ít nhất một kích thước");
            valid = false;
            count++
        }

        $scope.selectedMauSacs.forEach(mauSac => {
            mauSac.selectedKichThuocs.forEach(kichThuoc => {

                if (isNaN(kichThuoc.giaBan)) {
                    kichThuoc.giaBan = null;
                    kichThuoc.errors.giaBan = 'Không được bỏ trống giá bán';
                    valid = false;
                    count++;
                } else if (kichThuoc.giaBan < 0) {
                    kichThuoc.giaBan = null;
                    kichThuoc.errors.giaBan = 'Giá bán không được âm';
                    valid = false;
                    count++;
                }

                if (isNaN(kichThuoc.soLuong)) {
                    kichThuoc.soLuong = null;
                    kichThuoc.errors.soLuong = 'Không được bỏ trống số lượng';
                    valid = false;
                    count++;
                } else if (kichThuoc.soLuong < 0) {
                    kichThuoc.soLuong = null;
                    kichThuoc.errors.soLuong = 'Số lượng không được âm';
                    valid = false;
                    count++;
                }

                if (isNaN(kichThuoc.soLuongLoi)) {
                    kichThuoc.soLuongLoi = null;
                    kichThuoc.errors.soLuongLoi = 'Không được bỏ trống số lượng';
                    valid = false;
                    count++;
                } else if (kichThuoc.soLuongLoi < 0) {
                    kichThuoc.soLuongLoi = null;
                    kichThuoc.errors.soLuongLoi = 'Số lượng không được âm';
                    valid = false;
                    count++;
                }

                if (!kichThuoc.barcode) {
                    kichThuoc.barcode = '';
                    kichThuoc.errors.barcode = 'Không được bỏ trống barcode';
                    valid = false;
                    count++;
                }
                kichThuoc.barcode = (kichThuoc.barcode + "").trim();

            })
        })

        if (!valid) {
            toastr["error"](count + " trường không hợp lệ!");
            $scope.addGiayForm.ten.$touched = true;
            $scope.addGiayForm.moTa.$touched = true;
            $scope.addGiayForm.namSX.$touched = true;
            $scope.addGiayForm.trangThai.$touched = true;
        }
        return valid;
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

        if (model === 'giaBan' && $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaBan) {
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaBan = null;
        }
        if (model === 'barcode' && $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.barcode) {
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.barcode = null;
        }
        if (model === 'soLuong' && $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuong) {
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuong = null;
        }

        if (model === 'soLuongLoi' && $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuongLoi) {
            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuongLoi = null;
        }

    }

    $scope.blurInput = function (msIndex, ktIndex, model) {

        if (model === 'giaBan') {
            if (isNaN($scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].giaBan)) {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaBan = 'Giá bán không được để trống';
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].giaBan = null;
            } else if ($scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].giaBan < 0) {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaBan = 'Giá bán không được âm';
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].giaBan = null;
            } else {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.giaBan = null;
            }
        }
        if (model === 'barcode') {
            if (!$scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode) {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.barcode = 'Barcode không được để trống';
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = '';
            } else {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.barcode = null;
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = ($scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode + "").trim();
            }

            const currentBarcode = $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode;
            for (let mIndex = 0; mIndex < $scope.selectedMauSacs.length; mIndex++) {
                for (let kIndex = 0; kIndex < $scope.selectedMauSacs[mIndex].selectedKichThuocs.length; kIndex++) {
                    if (msIndex !== mIndex || ktIndex !== kIndex) {
                        const otherBarcode = $scope.selectedMauSacs[mIndex].selectedKichThuocs[kIndex].barcode;
                        if (currentBarcode === otherBarcode) {
                            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.barcode = 'Barcode không được trùng';
                            $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = '';
                            return;
                        }
                    }
                }
            }


        }
        if (model === 'soLuong') {
            if (isNaN($scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].soLuong)) {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuong = 'Số lượng không được để trống';
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].soLuong = null;
            } else {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuong = null;
            }
        }

        if (model === 'soLuongLoi') {
            if (isNaN($scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].soLuongLoi)) {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuongLoi = 'Không được để trống';
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].soLuongLoi = null;
            } else {
                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].errors.soLuongLoi = null;
            }
        }
    }

    $scope.updateForAll = function () {

        if ($scope.giaBanAll && !isNaN($scope.giaBanAll)) {
            $scope.selectedMauSacs.forEach(mauSac => {
                mauSac.selectedKichThuocs.forEach(kichThuoc => {
                    if (!isNaN(kichThuoc.giaBan)) {
                        kichThuoc.giaBan = $scope.giaBanAll;
                    }
                })
            })
        }

        if ($scope.soLuongAll && !isNaN($scope.soLuongAll)) {
            $scope.selectedMauSacs.forEach(mauSac => {
                mauSac.selectedKichThuocs.forEach(kichThuoc => {
                    kichThuoc.soLuong = $scope.soLuongAll;
                })
            })
        }

        $scope.selectedMauSacs.forEach((ms, msIndex) => {
            ms.selectedKichThuocs.forEach((kt, ktIndex) => {
                $scope.blurInput(msIndex, ktIndex, 'barcode');
                $scope.blurInput(msIndex, ktIndex, 'giaBan');
                $scope.blurInput(msIndex, ktIndex, 'soLuong');
                $scope.blurInput(msIndex, ktIndex, 'soLuongLoi');
            })
        })
        $scope.resetFormForAll();

    }

    $scope.resetFormForAll = function () {
        $scope.soLuongAll = '';
        $scope.giaBanAll = '';
    }

    function setData() {
        $scope.selectedLotGiay = $scope.lotGiays.find(lotGiay => lotGiay.id === $scope.giay.lotGiay.id);
        if (!$scope.selectedLotGiay) {
            $scope.selectedLotGiay = $scope.lotGiays[0];
        }

        $scope.selectedDeGiay = $scope.deGiays.find(deGiay => deGiay.id === $scope.giay.deGiay.id);
        if (!$scope.selectedDeGiay) {
            $scope.selectedDeGiay = $scope.deGiays[0];
        }

        $scope.selectedMuiGiay = $scope.muiGiays.find(muiGiay => muiGiay.id === $scope.giay.muiGiay.id);
        if (!$scope.selectedMuiGiay) {
            $scope.selectedMuiGiay = $scope.muiGiays[0];
        }

        $scope.selectedCoGiay = $scope.coGiays.find(coGiay => coGiay.id === $scope.giay.coGiay.id);
        if (!$scope.selectedCoGiay) {
            $scope.selectedCoGiay = $scope.coGiays[0];
        }

        $scope.selectedThuongHieu = $scope.thuongHieus.find(thuongHieu => thuongHieu.id === $scope.giay.thuongHieu.id);
        if (!$scope.selectedThuongHieu) {
            $scope.selectedThuongHieu = $scope.thuongHieus[0];
        }

        $scope.selectedChatLieu = $scope.chatLieus.find(chatLieu => chatLieu.id === $scope.giay.chatLieu.id);
        if (!$scope.selectedChatLieu) {
            $scope.selectedChatLieu = $scope.chatLieus[0];
        }

        $scope.selectedDayGiay = $scope.dayGiays.find(dayGiay => dayGiay.id === $scope.giay.dayGiay.id);
        if (!$scope.selectedDayGiay) {
            $scope.selectedDayGiay = $scope.dayGiays[0];
        }

        $scope.giay.lstHashTagChiTiet.forEach(htct => {
            let selectedHashTag = $scope.hashTags.find(ht => ht.id === htct.hashTag.id);
            if (selectedHashTag) {
                selectedHashTag.status = 'active';
                $scope.selectedHashTag.push(selectedHashTag);
            }
        });

        $scope.giay.lstBienTheGiay.forEach(bienThe => {
            let mauSacSelected = $scope.mauSacs.find(ms => ms.id === bienThe.mauSac.id);
            if (!$scope.selectedMauSac.find(mauSac => mauSac.id === bienThe.mauSac.id) && mauSacSelected) {
                mauSacSelected.canRemove = false;
                $scope.selectedMauSac.push(mauSacSelected);
            }

            let kichThuocSelected = $scope.kichThuocs.find(kt => kt.id === bienThe.kichThuoc.id);
            if (!$scope.selectedKichThuoc.find(kichThuoc => kichThuoc.id === bienThe.kichThuoc.id) && mauSacSelected) {
                // kichThuocSelected.status = $scope.giay.lstBienTheGiay.find(item =>
                //     item.trangThai === 1 && item.kichThuoc.id === bienThe.kichThuoc.id) ? 'active' : 'disabled';
                kichThuocSelected.canRemove = false;
                $scope.selectedKichThuoc.push(kichThuocSelected);
            }
        });

        async function fetchDataAndProcess() {
            await $scope.changeSelectedKichThuoc();
            setTimeout(function () {
                $scope.selectedMauSacs.forEach((ms, msIndex) => {
                    ms.selectedKichThuocs.forEach((kt, ktIndex) => {
                        const bt = $scope.giay.lstBienTheGiay.find(bienThe => bienThe.mauSac.id === ms.id && bienThe.kichThuoc.id === kt.id);
                        if (bt) {
                            kt.giaBan = bt.giaBan;
                            kt.trangThai = bt.trangThai;
                            kt.barcode = bt.barCode;
                            kt.soLuong = bt.soLuong;
                            kt.soLuongLoi = bt.soLuongLoi ? bt.soLuongLoi : 0;
                        } else {
                            kt.giaBan = 0;
                            kt.trangThai = 0;
                            kt.barcode = new Date().getTime();
                            kt.soLuong = 0;
                            kt.soLuongLoi = bt.soLuongLoi ? bt.soLuongLoi : 0;
                        }
                    });
                });

                $scope.selectedMauSacs.forEach(ms => {
                    if ($scope.giay.mauSacImages[ms.id]) {
                        ms.hinhAnh = 1;
                    }
                })


            }, 0);


        }

        fetchDataAndProcess();

        const lstAnh = $scope.giay.lstAnh;
        if (lstAnh && lstAnh[0]) {
            $scope.image1 = 1;
            loadImageFromBase64(lstAnh[0], 'selectedImage1');
        }

        if (lstAnh && lstAnh[1]) {
            $scope.image2 = 1;
            loadImageFromBase64(lstAnh[1], 'selectedImage2');
        }
        if (lstAnh && lstAnh[2]) {
            $scope.image3 = 1;
            loadImageFromBase64(lstAnh[2], 'selectedImage3');
        }

        if (lstAnh && lstAnh[3]) {
            $scope.image4 = 1;
            loadImageFromBase64(lstAnh[3], 'selectedImage4');
        }

        if (lstAnh && lstAnh[4]) {
            $scope.image5 = 1;
            loadImageFromBase64(lstAnh[4], 'selectedImage5');
        }
    }


//////////////for modal
    $scope.addChatLieu = function () {
        if ($scope.chatLieuForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/chat-lieu', $scope.chatLieu)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.chatLieus.unshift(response.data);
                $scope.selectedChatLieu = response.data;
                setTimeout(function () {
                    document.getElementById("modalChatLieuLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.chatLieuForm.ten.$dirty = false;
                    $scope.chatLieuForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal lot giay
    $scope.addLotGiay = function () {
        if ($scope.lotGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/lot-giay', $scope.lotGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.lotGiays.unshift(response.data);
                $scope.selectedLotGiay = response.data;
                setTimeout(function () {
                    document.getElementById("modalLotGiayLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.lotGiayForm.ten.$dirty = false;
                    $scope.lotGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal mui giay
    $scope.addMuiGiay = function () {
        if ($scope.muiGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/mui-giay', $scope.muiGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.muiGiays.unshift(response.data);
                $scope.selectedMuiGiay = response.data;
                setTimeout(function () {
                    document.getElementById("modalMuiGiayLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.muiGiayForm.ten.$dirty = false;
                    $scope.muiGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal co giay
    $scope.addCoGiay = function () {
        if ($scope.coGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/co-giay', $scope.coGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.coGiays.unshift(response.data);
                $scope.selectedCoGiay = response.data;
                setTimeout(function () {
                    document.getElementById("modalCoGiayLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.coGiayForm.ten.$dirty = false;
                    $scope.coGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal thuong hieu
    $scope.addThuongHieu = function () {
        if ($scope.thuongHieuForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/thuong-hieu', $scope.thuongHieu)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.thuongHieus.unshift(response.data);
                $scope.selectedThuongHieu = response.data;
                setTimeout(function () {
                    document.getElementById("modalThuongHieuLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.thuongHieuForm.ten.$dirty = false;
                    $scope.thuongHieuForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }


//modal day giay
    $scope.addDayGiay = function () {
        if ($scope.dayGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/day-giay', $scope.dayGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.dayGiays.unshift(response.data);
                $scope.selectedDayGiay = response.data;
                setTimeout(function () {
                    document.getElementById("modalDayGiayLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.dayGiayForm.ten.$dirty = false;
                    $scope.dayGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal de giay
    $scope.addDeGiay = function () {
        if ($scope.deGiayForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/de-giay', $scope.deGiay)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.deGiays.unshift(response.data);
                $scope.selectedDeGiay = response.data;
                setTimeout(function () {
                    document.getElementById("modalDeGiayLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.deGiayForm.ten.$dirty = false;
                    $scope.deGiayForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal hash tag
    $scope.addHashTag = function () {
        if ($scope.hashTagForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/de-giay', $scope.hashTag)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.hashTags.unshift(response.data);
                $scope.selectedHashTag = response.data;
                setTimeout(function () {
                    document.getElementById("modalHashTagLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.hashTagForm.ten.$dirty = false;
                    $scope.hashTagForm.moTa.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }


//modal kichThuoc
    $scope.addKichThuoc = function () {

        if ($scope.kichThuocForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/kich-thuoc', $scope.kichThuoc)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.kichThuocs.unshift(response.data);
                setTimeout(function () {
                    document.getElementById("modalKichThuocLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.kichThuocForm.ten.$dirty = false;
                    $scope.kichThuocForm.moTa.$dirty = false;
                    $scope.kichThuocForm.chieuDai.$dirty = false;
                    $scope.kichThuocForm.chieuRong.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

//modal mau sac
    $scope.addMauSac = function () {

        if ($scope.mauSacForm.$invalid) {
            return;
        }
        $http.post(host + '/admin/rest/mau-sac', $scope.mauSac)
            .then(function (response) {
                if (response.status === 200) {
                    toastr["success"]("Thêm thành công");
                }
                $scope.mauSacs.unshift(response.data);
                setTimeout(function () {
                    document.getElementById("modalMauSacLabel").nextElementSibling.click()
                }, 0);
            })
            .catch(function (error) {
                toastr["error"]("Thêm thất bại");
                if (error.status === 400) {
                    $scope.mauSacForm.ten.$dirty = false;
                    $scope.mauSacForm.moTa.$dirty = false;
                    $scope.mauSacForm.maMau.$dirty = false;
                    $scope.errors = error.data;
                }
            });
    }

})
;


//input chung
app.directive('customInputUpdate', function () {
    return {
        restrict: 'E',
        templateUrl: '/pages/admin/giay/views/input-update.html',
        scope: {
            id: '@',
            title: '@',
            items: '=',
            ngModel: '=',
            modalId: "@",
            selectType: '@'
        }, controller: function ($scope) {
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
                        if (item.canRemove !== false) {
                            item.canRemove = true;
                        }
                        // $scope.ngModel = $scope.selectedTags;
                        $scope.ngModel = $scope.selectedTags;
                    } else {
                        item.status = 'active';
                        if (item.canRemove !== false) {
                            item.canRemove = true;
                        }
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
                }
            };
            $scope.$watchCollection('ngModel', function (newNgModel, oldNgModel) {
                if (newNgModel !== oldNgModel) {
                    // Thực hiện các tác vụ tương ứng với sự thay đổi trong ngModel (danh sách)
                    newNgModel.forEach((element) => {
                        if (element) {
                            var selectedItem = $scope.items.find((item) => item.id === element.id && item.status === 'active' || item.canRemove === false);
                            $scope.selectItem(selectedItem);
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

    $scope.setBarcode = function (msIndex, ktIndex) {
        $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = new Date().getTime();
        $scope.blurInput(msIndex, ktIndex, 'barcode');
    }

    $scope.scanQR = function (e, element) {

        const msIndex = parseInt(element.getAttribute("ms-index"));
        const ktIndex = parseInt(element.getAttribute("kt-index"));
        if (isNaN(msIndex) || isNaN(ktIndex)) {
            toastr["error"]("Lỗi bất định!");
            return;
        }

        if (e.target.files && e.target.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
                const image = new Image();
                image.src = e.target.result;

                image.onload = function () {
                    const canvas = document.createElement('canvas');
                    canvas.width = image.width;
                    canvas.height = image.height;
                    const context = canvas.getContext('2d');
                    context.drawImage(image, 0, 0, image.width, image.height);

                    const imageData = context.getImageData(0, 0, image.width, image.height);
                    const code = jsQR(imageData.data, image.width, image.height);

                    if (code) {
                        $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = code.data;
                        $scope.blurInput(msIndex, ktIndex, 'barcode');
                        document.getElementById('barcode' + msIndex + ktIndex).value = code.data;
                    } else {
                        Quagga.decodeSingle({
                            src: e.target.result,
                            numOfWorkers: 0,
                            decoder: {
                                readers: ['ean_reader', 'code_128_reader', 'code_39_reader']
                            },
                        }, function (result) {
                            if (result && result.codeResult) {
                                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = result.codeResult.code;
                                $scope.blurInput(msIndex, ktIndex, 'barcode');
                                document.getElementById('barcode' + msIndex + ktIndex).value = result.codeResult.code;
                            } else {
                                toastr["error"]("Không tìm thấy mã trong hình ảnh");
                            }
                        });
                    }
                };
            };

            reader.readAsDataURL(e.target.files[0]);
        } else {
            toastr["error"]("Không tìm thấy hình ảnh");
        }
    }

    const video = document.getElementById('video');

    let scanning = false;

    $scope.startScanning = function (msIndex, ktIndex) {
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
                                $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = code.data;
                                $scope.blurInput(msIndex, ktIndex, 'barcode');
                                document.getElementById('barcode' + msIndex + ktIndex).value = code.data;
                                document.getElementById('closeModalCamera').click();
                                clearInterval(interval);
                            } else {
                                Quagga.decodeSingle({
                                    src: convertImageDataToBase64(imageData),
                                    numOfWorkers: 0,
                                    decoder: {
                                        readers: ['ean_reader', 'code_128_reader', 'code_39_reader']
                                    },
                                }, function (result) {
                                    if (result && result.codeResult) {
                                        $scope.selectedMauSacs[msIndex].selectedKichThuocs[ktIndex].barcode = result.codeResult.code;
                                        $scope.blurInput(msIndex, ktIndex, 'barcode');
                                        document.getElementById('barcode' + msIndex + ktIndex).value = result.codeResult.code;
                                        document.getElementById('closeModalCamera').click();
                                        clearInterval(interval);
                                    }
                                });

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
            video: {deviceId: selectedCamera, width: 1920, height: 1080}
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
