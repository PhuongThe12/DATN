app.run(function($rootScope) {
    $rootScope.$on('$viewContentLoaded', function() {
        // feather.replace();
        function _createForOfIteratorHelper(o, allowArrayLike) {
            let it;
            if (typeof Symbol === "undefined" || o[Symbol.iterator] == null) { if (Array.isArray(o) || (it = _unsupportedIterableToArray(o)) || allowArrayLike && o && typeof o.length === "number") { if (it) o = it;
                let i = 0;
                const F = function F() {
                };
                return { s: F, n: function n() { if (i >= o.length) return { done: true }; return { done: false, value: o[i++] }; }, e: function e(_e) { throw _e; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); }
            let normalCompletion = true,
                didErr = false, err;
            return { s: function s() { it = o[Symbol.iterator](); }, n: function n() { var step = it.next(); normalCompletion = step.done; return step; }, e: function e(_e2) { didErr = true; err = _e2; }, f: function f() { try { if (!normalCompletion && it["return"] != null) it["return"](); } finally { if (didErr) throw err; } } }; }

        function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen);
            let n = Object.prototype.toString.call(o).slice(8, -1);
            if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

        function _arrayLikeToArray(arr, len) { let arr2 = new Array(len);
            let i = 0;
            if (len == null || len > arr.length) len = arr.length; for (; i < len; i++) { arr2[i] = arr[i]; } return arr2; }


        const sidebar = document.querySelector('.sidebar'),
            catSubMenu = document.querySelector('.cat-sub-menu'),
            sidebarBtns = document.querySelectorAll('.sidebar-toggle');

        let _iterator = _createForOfIteratorHelper(sidebarBtns),
            _step;

        try {
            for (_iterator.s(); !(_step = _iterator.n()).done;) {
                const sidebarBtn = _step.value;

                if (sidebarBtn && catSubMenu && sidebarBtn) {
                    sidebarBtn.addEventListener('click', function () {

                        let _iterator2 = _createForOfIteratorHelper(sidebarBtns),
                            _step2;

                        try {
                            for (_iterator2.s(); !(_step2 = _iterator2.n()).done;) {
                                let sdbrBtn = _step2.value;
                                sdbrBtn.classList.toggle('rotated');
                            }
                        } catch (err) {
                            _iterator2.e(err);
                        } finally {
                            _iterator2.f();
                        }

                        sidebar.classList.toggle('hidden');
                        catSubMenu.classList.remove('visible');
                    });
                }
            }
        } catch (err) {
            _iterator.e(err);
        } finally {
            _iterator.f();
        }

        const showCatBtns = document.querySelectorAll('.show-cat-btn');

        if (showCatBtns) {
            showCatBtns.forEach(function (showCatBtn) {
                showCatBtn.addEventListener('click', function (e) {
                    e.preventDefault();
                    const catSubMenu = this.nextElementSibling;
                    catSubMenu.classList.toggle('visible');
                    const catBtnToRotate = this.querySelector('.category__btn');
                    catBtnToRotate.classList.toggle('rotated');
                });
            });
        }

        const showMenu = document.querySelector('.lang-switcher');
        const langMenu = document.querySelector('.lang-menu');
        let layer = document.querySelector('.layer');

        if (showMenu) {
            showMenu.addEventListener('click', function () {
                langMenu.classList.add('active');
                layer.classList.add('active');
            });

            if (layer) {
                layer.addEventListener('click', function (e) {
                    if (langMenu.classList.contains('active')) {
                        langMenu.classList.remove('active');
                        layer.classList.remove('active');
                    }
                });
            }
        }
        const userDdBtnList = document.querySelectorAll('.dropdown-btn');
        const userDdList = document.querySelectorAll('.users-item-dropdown');
        layer = document.querySelector('.layer');

        if (userDdList && userDdBtnList) {
            let _iterator3 = _createForOfIteratorHelper(userDdBtnList),
                _step3;

            try {
                for (_iterator3.s(); !(_step3 = _iterator3.n()).done;) {
                    const userDdBtn = _step3.value;
                    userDdBtn.addEventListener('click', function (e) {
                        layer.classList.add('active');

                        let _iterator4 = _createForOfIteratorHelper(userDdList),
                            _step4;

                        try {
                            for (_iterator4.s(); !(_step4 = _iterator4.n()).done;) {
                                const userDd = _step4.value;

                                if (e.currentTarget.nextElementSibling === userDd) {
                                    if (userDd.classList.contains('active')) {
                                        userDd.classList.remove('active');
                                    } else {
                                        userDd.classList.add('active');
                                    }
                                } else {
                                    userDd.classList.remove('active');
                                }
                            }
                        } catch (err) {
                            _iterator4.e(err);
                        } finally {
                            _iterator4.f();
                        }
                    });
                }
            } catch (err) {
                _iterator3.e(err);
            } finally {
                _iterator3.f();
            }
        }

        if (layer) {
            layer.addEventListener('click', function (e) {
                let _iterator5 = _createForOfIteratorHelper(userDdList),
                    _step5;

                try {
                    for (_iterator5.s(); !(_step5 = _iterator5.n()).done;) {
                        const userDd = _step5.value;

                        if (userDd.classList.contains('active')) {
                            userDd.classList.remove('active');
                            layer.classList.remove('active');
                        }
                    }
                } catch (err) {
                    _iterator5.e(err);
                } finally {
                    _iterator5.f();
                }
            });
        }

    });
});