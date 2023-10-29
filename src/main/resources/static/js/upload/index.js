var app = angular.module("app", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/index", {
            templateUrl: '/pages/admin/upload/views/index.html',
            controller: 'uploadController'
        })
        .otherwise({redirectTo: '/index'});
});

app.controller("uploadController", function ($scope, $http, $location) {
    $scope.selectedImages = [];
    $scope.imageFiles = [];

    $scope.checkFileCount = function (input) {
        if (input.files.length > 10) {
            alert("Chỉ được phép tải tối đa 10 ảnh cùng một lúc");
            input.value = "";
        }
        if (input.files) {
            $scope.imageFiles = Array.from(input.files);
        }
        $scope.imageFiles.forEach((image) => {
            if (image.size > 5 * 1024 * 1024) {
                alert("Một ảnh không được vượt quá 5MB");
                input.value = "";
            }
        });
    };

    $scope.showSelectedImages = function (e) {
        $scope.selectedImages = [];

        $scope.imageFiles.forEach((imageFile) => {
            const reader = new FileReader();
            reader.onload = function (event) {
                const image = {
                    url: event.target.result,
                    name: imageFile.name,
                    deleted: false
                };
                $scope.$apply(function () {
                    $scope.selectedImages.push(image);
                });
            };
            reader.readAsDataURL(imageFile);
        });
    };

    $scope.removeImage = function (image) {
        var index = $scope.selectedImages.indexOf(image);
        if (index !== -1) {
            $scope.selectedImages.splice(index, 1);
            console.log($scope.imageFiles);
            $scope.imageFiles.splice(index, 1);
        }
    };


    $scope.uploadImages = function () {

        if ($scope.imageFiles.length === 0) {
            toastr["error"]('Chưa chọn ảnh');
            console.log('Uploading images');
            return;
        }
        const formData = new FormData();
        $scope.imageFiles.forEach((imageFile) => {
            formData.append('files', imageFile);
        });

        fetch(host + '/admin/rest/images/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.status === 200) {
                    return response.text();
                } else {
                    toastr["error"]("Lỗi trong quá trình tải lên");
                }
            })
            .then(data => {
                toastr["success"](data + ", có thể sẽ mất vài phút để ảnh hiển thị");
            })
            .catch(error => {
                toastr["error"](error.data);
            });

        $scope.selectedImages = [];
        $scope.imageFiles = [];
        $scope.removeImage();
        setTimeout(getImages, 3000);
    };

    const getImages = function () {
        $http.get(host + '/admin/rest/images')
            .then((response) => {
                $scope.images = [];
                const images = response.data;
                images.forEach((image, index) => {
                    if (!image.startsWith(".")) {
                        $scope.images.push({
                                url: "/upload/images/" + image,
                                name: image
                            }
                        );
                    }
                })
            })
            .catch(function (error) {
                toastr["error"]("Lấy ảnh thất bại. Vui lòng thử lại");
            });

    }
    getImages();

    $scope.removeImageFromList = function (name) {
        // $http.delete(host + '/admin/rest/images/' + name)
        //     .then(function (response) {
        //         toastr["success"]("Xóa thành công");
        //         $scope.images.splice($scope.images.findIndex((find) => {
        //             return find.name === name;
        //         }), 1);
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //         toastr["error"](error.data);
        //     })

        fetch(host + '/admin/rest/images/' + name, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.status === 200) {
                    return response.text();
                } else {
                    toastr["error"]("Lỗi trong quá trình xóa");
                }
            })
            .then(data => {
                toastr["success"]("Xóa thành công");
                $scope.images.splice($scope.images.findIndex((find) => {
                                return find.name === name;
                            }), 1);
            })
            .catch(error => {
                toastr["error"](error.data);
            });
    };

});
