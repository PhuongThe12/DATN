app.controller('excelUpdateController', function ($scope, $http, $location) {

    let request = [];

    $scope.isLoading = false;

    $scope.updateExcel = function () {
        if ($scope.errors.length > 0) {
            return;
        }

        $scope.isLoading = true;
        $http.post("http://localhost:8080/rest/admin/giay/update-excel", JSON.stringify($scope.requestData))
            .then(function (response) {
                toastr["success"]("Cập nhật thành công");
                $location.path("/list");
            })
            .catch(function (error) {
                console.log(error);
                if (Array.isArray(error.data)) {
                    error.data.forEach(e => {
                        $scope.errors.push(
                            "Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: e.row,
                                c: e.column
                            }) + ": " + e.message
                        )
                    });
                    document.getElementById("btnShowErrors").click();
                }
                $scope.isLoading = false;
            });
    }

    $scope.requestData = {};
    $scope.errors = [];

    $scope.processExcel = async function () {
        const fileInput = document.getElementById('fileInput');
        const file = fileInput.files[0];

        if (!file) {
            Swal.fire({
                title: "Lỗi!",
                text: "Bạn chưa chọn file excel nào!",
                icon: "error"
            });
            return;
        }

        const reader = new FileReader();

        reader.onload = function (e) {
            const data = new Uint8Array(e.target.result);
            try {
                const workbook = XLSX.read(data, {type: 'array'});

                // Assume the first sheet is the one you want to process
                const sheetName = workbook.SheetNames[0];
                const sheet = workbook.Sheets[sheetName];

                // Kiểm tra số dòng trong sheet
                const range = XLSX.utils.decode_range(sheet['!ref']);
                let rowCount = range.e.r - range.s.r + 1;

                // Convert sheet data to JSON format
                const jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});

                // Process the JSON data as needed

                let giayData = {};

                let lastKey;
                // Duyệt qua từng dòng bắt đầu từ dòng thứ 6
                for (let row = 5; row <= 999 && row <= range.e.r; row++) {

                    //Lưu dữ liệu của một dòng
                    const giay = {};
                    let colIndex = 0;

                    let rowData = [];

                    giay.ten = getCellData(row, colIndex++);
                    if (!giay.ten && row === 5) {
                        Swal.fire({
                            title: "Lỗi!",
                            text: "Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: row,
                                c: 0
                            }) + ": Tên không được để trống ở dòng đầu tiên",
                            icon: "error"
                        });
                        document.getElementById('fileInput').value = null;
                        return;
                    } else if (!giay.ten && lastKey) {
                        giay.ten = giayData[lastKey].ten;
                        rowData.push(giay.ten ? '1' : null);
                    } else {

                        rowData.push(giay.ten ? '1' : null);
                        rowData.push(giay.ten ? '1' : null);
                    }
                    giay.image1 = getCellData(row, colIndex++);
                    rowData.push(giay.image1 ? '1' : null);

                    giay.image2 = getCellData(row, colIndex++);
                    rowData.push(giay.image2 ? '1' : null);

                    giay.image3 = getCellData(row, colIndex++);
                    rowData.push(giay.image3 ? '1' : null);

                    giay.image4 = getCellData(row, colIndex++);
                    rowData.push(giay.image4 ? '1' : null);

                    giay.image5 = getCellData(row, colIndex++);
                    rowData.push(giay.image5 ? '1' : null);

                    giay.lotGiay = getCellData(row, colIndex++);
                    rowData.push(giay.lotGiay ? '1' : null);

                    giay.muiGiay = getCellData(row, colIndex++);
                    rowData.push(giay.muiGiay ? '1' : null);

                    giay.coGiay = getCellData(row, colIndex++);
                    rowData.push(giay.coGiay ? '1' : null);

                    giay.thuongHieu = getCellData(row, colIndex++);
                    rowData.push(giay.thuongHieu ? '1' : null);

                    giay.chatLieu = getCellData(row, colIndex++);
                    rowData.push(giay.chatLieu ? '1' : null);

                    giay.dayGiay = getCellData(row, colIndex++);
                    rowData.push(giay.dayGiay ? '1' : null);

                    giay.deGiay = getCellData(row, colIndex++);
                    rowData.push(giay.deGiay ? '1' : null);

                    giay.hashTags = getCellData(row, colIndex++);
                    rowData.push(giay.hashTags ? '1' : null);

                    giay.namSX = getCellData(row, colIndex++);
                    rowData.push(giay.namSX ? '1' : null);

                    giay.moTa = getCellData(row, colIndex++);
                    rowData.push(giay.moTa ? '1' : null);

                    giay.row = row;
                    // Duyệt qua từng ô trong dòng
                    giay.bienTheGiay = {};
                    for (let col = 0; col <= range.e.c - colIndex; col += 6) {
                        if (!getCellData(row, col + colIndex)) {
                            break;
                        }
                        let ms = null, kt = null, sl = null, barcode = null, hinhAnh = null, giaBan = null;
                        for (let i = 0; i < 6; i++) {
                            if (i === 0) {
                                ms = getCellData(row, col + i + colIndex);
                                rowData.push(ms ? '1' : null);
                            } else if (i === 1) {
                                kt = getCellData(row, col + i + colIndex);
                                rowData.push(kt ? '1' : null);
                            } else if (i === 2) {
                                sl = getCellData(row, col + i + colIndex);
                            } else if (i === 3) {
                                giaBan = getCellData(row, col + i + colIndex);
                            } else if (i === 4) {
                                barcode = getCellData(row, col + i + colIndex) + "";
                            } else {
                                hinhAnh = getCellData(row, col + i + colIndex) + "";
                            }
                        }
                        if (!giay.bienTheGiay.hasOwnProperty(ms + ', ' + kt)) {
                            giay.bienTheGiay[ms + ', ' + kt] = {};
                            giay.bienTheGiay[ms + ', ' + kt].mauSac = ms;
                            giay.bienTheGiay[ms + ', ' + kt].kichThuoc = kt;
                        }
                        giay.bienTheGiay[ms + ', ' + kt].hinhAnh = hinhAnh;
                        giay.bienTheGiay[ms + ', ' + kt].barcode = barcode;
                        giay.bienTheGiay[ms + ', ' + kt].soLuong = parseInt(sl);
                        giay.bienTheGiay[ms + ', ' + kt].giaBan = parseFloat(giaBan);

                        giay.bienTheGiay[ms + ', ' + kt].column = col + colIndex;
                        giay.bienTheGiay[ms + ', ' + kt].row = row;

                    }

                    if (giayData.hasOwnProperty(giay.ten)) {
                        //
                        // giayData[giay.ten].ten = giay.ten;
                        // if (giay.image1) {
                        //     giayData[giay.ten].image1 = giay.image1;
                        // }
                        //
                        // if (giay.image2) {
                        //     giayData[giay.ten].image2 = giay.image2;
                        // }
                        // if (giay.image3) {
                        //     giayData[giay.ten].image3 = giay.image3;
                        // }
                        // if (giay.image4) {
                        //     giayData[giay.ten].image4 = giay.image4;
                        // }
                        // if (giay.image5) {
                        //     giayData[giay.ten].image5 = giay.image5;
                        // }
                        // if (giay.lotGiay) {
                        //     giayData[giay.ten].lotGiay = giay.lotGiay;
                        // }
                        // if (giay.muiGiay) {
                        //     giayData[giay.ten].muiGiay = giay.muiGiay;
                        // }
                        // if (giay.coGiay) {
                        //     giayData[giay.ten].coGiay = giay.coGiay;
                        // }
                        // if (giay.thuongHieu) {
                        //     giayData[giay.ten].thuongHieu = giay.thuongHieu;
                        // }
                        // if (giay.chatLieu) {
                        //     giayData[giay.ten].chatLieu = giay.chatLieu;
                        // }
                        // if (giay.dayGiay) {
                        //     giayData[giay.ten].dayGiay = giay.dayGiay;
                        // }
                        // if (giay.deGiay) {
                        //     giayData[giay.ten].deGiay = giay.deGiay;
                        // }
                        // if (giay.hashTags) {
                        //     giayData[giay.ten].hashTags = (giay.hashTags + "").split(',').map(item => item.trim());
                        // }
                        // if (giay.namSX) {
                        //     giayData[giay.ten].namSX = giay.namSX;
                        // }
                        // if (giay.moTa) {
                        //     giayData[giay.ten].moTa = giay.moTa;
                        // }

                        for (const k in giay.bienTheGiay) {
                            if (giayData[giay.ten].bienTheGiay.hasOwnProperty(k)) {
                                giayData[giay.ten].bienTheGiay[k].soLuong = giay.bienTheGiay[k].soLuong;
                                giayData[giay.ten].bienTheGiay[k].barcode = giay.bienTheGiay[k].barcode;
                                giayData[giay.ten].bienTheGiay[k].hinhAnh = giay.bienTheGiay[k].hinhAnh;
                                giayData[giay.ten].bienTheGiay[k].giaBan = giay.bienTheGiay[k].giaBan;
                                giayData[giay.ten].bienTheGiay[k].column = giay.bienTheGiay[k].column;
                                giayData[giay.ten].bienTheGiay[k].row = giay.bienTheGiay[k].row;
                            } else {
                                giayData[giay.ten].bienTheGiay[k] = giay.bienTheGiay[k];
                            }
                        }

                    } else {
                        lastKey = giay.ten;
                        giayData[giay.ten] = giay;
                        giayData[giay.ten].bienTheGiay = giay.bienTheGiay;
                        giayData[giay.ten].hashTags = (giayData[giay.ten].hashTags + "").split(',').map(item => item.trim());
                    }

                    if (!rowData.some(value => value !== null && value !== '')) {
                        rowCount = row;
                        break;
                    }


                }

                function getCellData(row, col) {
                    const cellRef = XLSX.utils.encode_cell({r: row, c: col});
                    const cell = sheet[cellRef];

                    let cellValue = cell ? cell.v : null;
                    if (typeof cellValue === 'string') {
                        cellValue = cellValue.trim();
                    }
                    return cellValue;
                }

                const requestGiays = [];
                const errors = [];
                const promises = [];
                for (const k in giayData) {

                    if (giayData[k].namSX && !Number.isInteger(giayData[k].namSX)) {
                        errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                            r: giayData[k].row,
                            c: 14
                        }) + ": Năm sản xuất phải là số nguyên");
                    } else if (giayData[k].namSX && giayData[k].namSX > 9999 || giayData[k].namSX < 1000) {
                        errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                            r: giayData[k].row,
                            c: 14
                        }) + ": Năm sản xuất phải nằm trong khoảng 1000 - 9999");
                    }

                    if ((giayData[k].moTa + "") && (giayData[k].moTa + "").length > 3000) {
                        errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                            r: giayData[k].row,
                            c: 15
                        }) + ": Mô tả không được quá 3000 ký tự");
                    } else if ((giayData[k].moTa + "") && (giayData[k].moTa + "").length < 3) {
                        errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                            r: giayData[k].row,
                            c: 15
                        }) + ": Mô tả không được ít hơn 3 ký tự");
                    }

                    let numImages = [giayData[k].image1, giayData[k].image2, giayData[k].image3, giayData[k].image4, giayData[k].image5];

                    for (let i = 0; i < numImages.length; i++) {
                        const columnIndex = i + 1;
                        if (numImages[i]) {
                            promises.push(
                                readImageFromURL(numImages[i])
                                    .then(result => {
                                        giayData[k][`image${i + 1}`] = result;
                                    })
                                    .catch(error => {
                                        errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                            r: giayData[k].row,
                                            c: columnIndex
                                        }) + ': ' + error.message);
                                    })
                            );
                        }
                    }
                    if(giayData[k].hashTags.length === 1 && giayData[k].hashTags[0] === 'null') {
                        giayData[k].hashTags = [];
                    }

                    giayData[k].mauSacImages = {};
                    giayData[k].bienTheGiays = [];
                    for (const x in giayData[k].bienTheGiay) {

                        if (!giayData[k].bienTheGiay[x].mauSac) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Không được để trống màu sắc");
                        }

                        if (!giayData[k].bienTheGiay[x].kichThuoc) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Không được để trống kích thước");
                        }

                        if (giayData[k].bienTheGiay[x].barcode && (giayData[k].bienTheGiay[x].barcode + "").length > 20) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Barcode không hợp lệ, barcode quá 20 ký tự");
                        }

                        if (giayData[k].bienTheGiay[x].soLuong && !Number.isInteger(giayData[k].bienTheGiay[x].soLuong)) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Số lượng không đúng định dạng. Số lượng phải là số nguyên");
                        } else if (giayData[k].bienTheGiay[x].soLuong && giayData[k].bienTheGiay[x].soLuong < 0) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Số lượng không được âm");
                        }

                        if (giayData[k].bienTheGiay[x].giaBan && isNaN(giayData[k].bienTheGiay[x].giaBan)) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Giá bán phải là số.");
                        } else if (giayData[k].bienTheGiay[x].giaBan && giayData[k].bienTheGiay[x].giaBan < 0) {
                            errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                r: giayData[k].bienTheGiay[x].row,
                                c: giayData[k].bienTheGiay[x].column
                            }) + ": Giá bán không được âm");
                        }


                        if (giayData[k].bienTheGiay[x].hinhAnh && giayData[k].bienTheGiay[x].hinhAnh > 0) {
                            promises.push(readImageFromURL(giayData[k].bienTheGiay[x].hinhAnh)
                                .then(result => {
                                    giayData[k].mauSacImages[giayData[k].bienTheGiay[x].mauSac] = result;
                                })
                                .catch((error) => {
                                    errors.push("Lỗi ở ô " + XLSX.utils.encode_cell({
                                        r: giayData[k].row,
                                        c: giayData[k].bienTheGiay[x].column + 4
                                    }) + ': ' + error.message)
                                }));
                        }

                        giayData[k].bienTheGiays.push(giayData[k].bienTheGiay[x]);
                    }

                    giayData[k].bienTheGiay = null;
                    requestGiays.push(giayData[k]);
                }

                Promise.all(promises)
                    .then(() => {
                        if (!requestGiays || requestGiays.length === 0) {
                            Swal.fire({
                                title: "Lỗi!",
                                text: "Không thể đọc dữ liệu hoặc dữ liệu không đúng!",
                                icon: "error"
                            });
                            document.getElementById('fileInput').value = null;
                            return;

                        }

                        if (errors.length > 0) {
                            $scope.errors = errors;
                            $scope.requestData = {};
                            document.getElementById("addExcelBtn").click();
                            document.getElementById("btnShowErrors").click();
                            return;
                        } else {
                            $scope.errors = [];
                        }

                        $scope.$apply(function () {
                            $scope.requestData = requestGiays;
                            $scope.requestData.active = true;
                        });

                    })


                function readImageFromURL(url) {

                    if (!url) {
                        return;
                    }

                    if (url.startsWith("https://drive.google.com")) {
                        url = url.replace("https://drive.google.com/file/d/", "");
                        const id = url.split("/")[0];
                        url = 'https://lh3.googleusercontent.com/d/' + id;
                    }

                    return new Promise((resolve, reject) => {
                        fetch(url)
                            .then(response => response.blob())
                            .then(blob => {
                                const imageUrlReader = new FileReader();
                                imageUrlReader.onload = function () {
                                    const imageDataURL = imageUrlReader.result;
                                    if (imageDataURL.startsWith('data:image/')) {
                                        resolve(imageDataURL + "");
                                    } else {
                                        reject(new Error('Đường dẫn không phải là hình ảnh'));
                                    }
                                };
                                imageUrlReader.onerror = function (error) {
                                    reject(new Error('Không có quyền truy cập vào đường dẫn'));
                                };
                                imageUrlReader.readAsDataURL(blob);
                            })
                            .catch(error => {
                                reject(new Error('Đường dẫn không hợp lệ'));
                            });
                    });
                }
            } catch (e) {
                Swal.fire({
                    title: "Lỗi!",
                    text: "Không thể xử lý file này. Vui lòng thử lại",
                    icon: "error"
                });
                document.getElementById('fileInput').value = null;
            }

        };

        reader.readAsArrayBuffer(file);
    }

});