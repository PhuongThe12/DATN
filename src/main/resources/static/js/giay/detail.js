app.controller('detailGiayController', function ($scope, $http, $location, $routeParams) {
    const id = $routeParams.id;
    $http.get(host + '/admin/rest/giay/' + id)
        .then(function (response) {
            show(response.data);
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/list");
    });

    function show(productData) {
        displayImages(productData.lstAnh);

        const mauSacImages = productData.mauSacImages;
        const lstBienTheGiay = productData.lstBienTheGiay;

        const buttonsContainer = document.getElementById('buttons-container');
        const productInfoContainer = document.getElementById('product-info');
        const sizeButtons = document.getElementById("sizeButtons");
        const quantityDisplay = document.getElementById('quantity');
        const priceDisplay = document.getElementById('price-product');

// Tạo các button màu sắc và xử lý sự kiện click
        for (const mauSacId in mauSacImages) {
            if (mauSacImages.hasOwnProperty(mauSacId)) {
                const mauSacIdInt = parseInt(mauSacId, 10);

                // Tìm tên màu sắc từ lstBienTheGiay dựa trên mauSacId
                const mauSacInfo = lstBienTheGiay.find(variant => variant.mauSac.id === mauSacIdInt)?.mauSac || {
                    ten: `Màu ${mauSacId}`,
                    maMau: '#FFFFFF'
                };


                const outerDiv = document.createElement('div');
                const insideDiv = document.createElement('div');
                insideDiv.classList.add('insideDiv');
                const input = document.createElement("input");
                input.type = "radio"; // Để sử dụng input như một lựa chọn màu sắc
                input.name = "color" // Đặt cùng một tên cho tất cả các input của màu sắc
                input.hidden = true;

                insideDiv.textContent = "";
                insideDiv.style.backgroundColor = mauSacInfo.maMau; // Đặt màu nền của nút
                insideDiv.appendChild(input);
                outerDiv.appendChild(insideDiv);

                outerDiv.addEventListener('click', () => {
                    input.checked = true;
                    productInfoContainer.innerHTML = '';
                    sizeButtons.innerHTML = '';
                    // Tạo danh sách ul để chứa thông tin sản phẩm
                    const ul = document.createElement('ul');
                    lstBienTheGiay.forEach(variant => {
                        if (mauSacIdInt === variant.mauSac.id) {
                            const li = document.createElement('li');
                            li.textContent = `ID: ${variant.id}, GiaBan: ${variant.giaBan}`;
                            ul.appendChild(li);

                            // Tạo nút kích thước và xử lý sự kiện click
                            const sizeButton = document.createElement("button");
                            sizeButton.textContent = variant.kichThuoc.ten;
                            sizeButton.className = "btn btn-dark";
                            sizeButton.addEventListener("click", () => {
                                quantityDisplay.textContent = variant.soLuong;
                                priceDisplay.textContent = variant.giaBan;
                            });
                            sizeButtons.appendChild(sizeButton);
                        }

                    });

                    const allColorContainers = document.querySelectorAll('.button_color');
                    allColorContainers.forEach(container => {
                        container.classList.remove('button_checked');
                    });
                    outerDiv.classList.add('button_checked');


                    const linkAnh = mauSacImages[mauSacId];
                    const imageList = [linkAnh];
                    displayImages(imageList);
                });
                outerDiv.className = "button_color";
                buttonsContainer.appendChild(outerDiv);
            }
        }

        function displayImages(imageList) {
            const carouselInner = document.querySelector('#carouselExampleControls .carousel-inner');
            const carouselItems = document.querySelectorAll('#carouselExampleControls .carousel-item');

            // Xóa tất cả các carousel items hiện tại
            carouselItems.forEach(item => {
                carouselInner.removeChild(item);
            });

            // Tạo các carousel items mới từ danh sách ảnh mới
            for (let i = 0; i < imageList.length; i++) {
                const imageUrl = imageList[i];
                const div = document.createElement('div');
                div.className = i === 0 ? 'carousel-item active' : 'carousel-item';

                const img = document.createElement('img');
                img.src = imageUrl;
                img.className = 'd-block w-100';
                img.style.width = '400px !important';
                img.style.height = '400px';
                img.style.objectFit = 'contain';

                div.appendChild(img);
                carouselInner.appendChild(div);
            }
        }


        const infoContainer = document.querySelector('.info');
        const isExpandedMap = {};

// Duyệt qua các trường trong productData
        for (const key in productData) {
            if (key !== 'id' && key !== 'ten' && key !== 'namSX' && key !== 'lstAnh' && key !== 'trangThai' && key !== 'lstBienTheGiay' && key !== 'mauSacImages') {
                const keyData = productData[key];

                const itemDiv = document.createElement('div');
                itemDiv.classList.add('item');

                if (key === 'moTa') {
                    // Tạo một lớp CSS để giới hạn chiều cao văn bản cho key 'moTa'
                    itemDiv.classList.add('text-limit');
                    itemDiv.innerHTML = `<p>${keyData}: ${keyData}</p>`;
                } else if (keyData && keyData.id !== null) {
                    itemDiv.classList.add('text-limit');
                    const itemH5 = document.createElement('h5');
                    const itemP = document.createElement('p');
                    itemH5.textContent = keyData.ten;
                    itemP.textContent = keyData.moTa;
                    itemDiv.appendChild(itemH5);

                    // Tạo một lớp CSS để giới hạn chiều cao văn bản cho các khóa khác
                    itemP.classList.add('text-limit');
                    itemDiv.appendChild(itemP);
                    console.log(itemDiv.textContent);
                }

                infoContainer.appendChild(itemDiv);

                if (keyData && keyData.id !== null) {

                    // Tạo nút Xem thêm và Thu gọn cho từng key
                    if (itemDiv.scrollHeight > 80) {
                        const toggleButton = document.createElement('button');
                        toggleButton.classList.add('toggle-button');
                        toggleButton.textContent = "Xem Thêm";
                        infoContainer.appendChild(toggleButton);


                        // Mặc định, cho rằng key này là đóng
                        isExpandedMap[key] = false;

                        // Bắt sự kiện click cho nút Xem thêm/Thu gọn
                        toggleButton.addEventListener('click', function () {
                            if (isExpandedMap[key]) {
                                // Đóng nội dung
                                itemDiv.style.maxHeight = null;
                                toggleButton.textContent = "Xem Thêm";
                            } else {
                                // Mở nội dung
                                itemDiv.style.maxHeight = itemDiv.scrollHeight + "px";
                                toggleButton.textContent = "Thu Gọn";
                            }

                            // Đảo ngược trạng thái
                            isExpandedMap[key] = !isExpandedMap[key];
                        });
                    }

                }
            }
        }
        const xemThemBtn = document.getElementById('xemThemBtn');
        const infoDiv = document.getElementById('info');
        let isExpanded = false; // Biến để theo dõi trạng thái

        xemThemBtn.addEventListener('click', function () {
            if (isExpanded) {
                infoDiv.style.maxHeight = null; // Đóng nội dung
                xemThemBtn.textContent = "Xem Thêm Toàn Bộ"; // Thay đổi nội dung nút
            } else {
                infoDiv.style.maxHeight = infoDiv.scrollHeight + "px"; // Mở nội dung
                xemThemBtn.textContent = "Thu Gọn Toàn Bộ"; // Thay đổi nội dung nút
            }

            isExpanded = !isExpanded; // Đảo ngược trạng thái
        });

    }
});