(function() {

    var onlineImage,
        backupStyle = editor.queryCommandValue('background');

    window.onload = function() {
        initColorSelector();
    };

    /* 初始化颜色设置 */
    function initColorSelector() {
        var obj = editor.queryCommandValue('background');
        if (obj) {
            var color = obj['background-color'],
                repeat = obj['background-repeat'] || 'repeat',
                image = obj['background-image'] || '',
                position = obj['background-position'] || 'center center',
                pos = position.split(' '),
                x = parseInt(pos[0]) || 0,
                y = parseInt(pos[1]) || 0;

            if (repeat == 'no-repeat' && (x || y)) repeat = 'self';

            image = image.match(/url[\s]*\(([^\)]*)\)/);
            image = image ? image[1] : '';
            updateFormState('colored', color, image, repeat, x, y);
        } else {
            updateFormState();
        }

        var updateHandler = function() {
            updateFormState();
            updateBackground();
        }

        domUtils.on($G('url'), 'keyup', function() {
            if ($G('url').value && $G('alignment').style.display == "none") {
                utils.each($G('repeatType').children, function(item) {
                    item.selected = ('repeat' == item.getAttribute('value') ? 'selected' : false);
                });
            }
            updateHandler();
        });
        domUtils.on($G('repeatType'), 'change', updateHandler);
        domUtils.on($G('x'), 'keyup', updateBackground);
        domUtils.on($G('y'), 'keyup', updateBackground);

        initColorPicker();
    }

    /* 初始化颜色选择器 */
    function initColorPicker() {
        var me = editor,
            cp = $G("colorPicker");

        /* 生成颜色选择器ui对象 */
        var popup = new UE.ui.Popup({
            content: new UE.ui.ColorPicker({
                noColorText: me.getLang("clearColor"),
                editor: me,
                onpickcolor: function(t, color) {
                    updateFormState('colored', color);
                    updateBackground();
                    UE.ui.Popup.postHide();
                },
                onpicknocolor: function(t, color) {
                    updateFormState('colored', 'transparent');
                    updateBackground();
                    UE.ui.Popup.postHide();
                }
            }),
            editor: me,
            onhide: function() {}
        });

        /* 设置颜色选择器 */
        domUtils.on(cp, "click", function() {
            popup.showAnchor(this);
        });
        domUtils.on(document, 'mousedown', function(evt) {
            var el = evt.target || evt.srcElement;
            UE.ui.Popup.postHide(el);
        });
        domUtils.on(window, 'scroll', function() {
            UE.ui.Popup.postHide();
        });
    }

    /* 更新背景色设置面板 */
    function updateFormState(radio, color, url, align, x, y) {       
        if (color) {
            domUtils.setStyle($G("colorPicker"), "background-color", color);
        }

        if (url && /^\//.test(url)) {
            var a = document.createElement('a');
            a.href = url;
            browser.ie && (a.href = a.href);
            url = browser.ie ? a.href : (a.protocol + '//' + a.host + a.pathname + a.search + a.hash);
        }

        if (url || url === '') {
            $G('url').value = url;
        }
        if (align) {
            utils.each($G('repeatType').children, function(item) {
                item.selected = (align == item.getAttribute('value') ? 'selected' : false);
            });
        }
        if (x || y) {
            $G('x').value = parseInt(x) || 0;
            $G('y').value = parseInt(y) || 0;
        }

    }

    /* 更新背景颜色 */
    function updateBackground() {

        var color = domUtils.getStyle($G("colorPicker"), "background-color"),
            bgimg = $G("url").value,
            align = $G("repeatType").value,
            backgroundObj = {
                "background-repeat": "no-repeat",
                "background-position": "center center"
            };

        if (color) backgroundObj["background-color"] = color;
        if (bgimg) backgroundObj["background-image"] = 'url(' + bgimg + ')';
        if (align == 'self') {
            backgroundObj["background-position"] = $G("x").value + "px " + $G("y").value + "px";
        } else if (align == 'repeat-x' || align == 'repeat-y' || align == 'repeat') {
            backgroundObj["background-repeat"] = align;
        }

        editor.execCommand('background', backgroundObj);
    }

    dialog.onok = function() {
        updateBackground();
        editor.fireEvent('saveScene');
    };
    dialog.oncancel = function() {
        editor.execCommand('background', backupStyle);
    };

    window.uploadBgFile = function() {

        $.ajaxFileUpload({
            url: '/common/upload/file', //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'fileInput', //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json
            success: function(data, status) //服务器成功响应处理函数
            {
                if (data.status == 1) {
                    $("#url").val("/file/" + data.result.pelativePath);
                } else {
                    $.errorMessage("上传文件失败");
                }
            },
            error: function(data, status, e) //服务器响应失败处理函数
            {
                $.errorMessage("服务器异常");
            }
        });
    }
})();