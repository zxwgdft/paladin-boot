//------------------------------------------
//
// 自动化编辑查看代码
//
// 基于不复杂化配置，让用户产生疑惑的原则，针对简单类型的表单进行约定。
// 处理了表单生成、表单验证、表单自动提交、表单列依赖、表单控件封装等功能，
// 从而提高后台开发人员开发前端难道和时间，并且可以做一些统一处理
//
// 控件封装后可更方便统一替换和解决bug
// -----------------------------------------
function generateTagAttribute(obj) {
    if (!obj) return '';
    var s = [];
    for (var o in obj) {
        var v = obj[o];
        if (v !== undefined && v !== null) {
            s.push(o + '="' + v + '"');
        }
    }
    return s.length > 0 ? s.join(' ') : '';
}

function generateTag(tag, obj, text) {
    var s = [];
    if (obj) {
        for (var o in obj) {
            var v = obj[o];
            if (v !== undefined && v !== null) {
                s.push(o + '="' + v + '"');
            }
        }
    }
    s = s.length > 0 ? s.join(" ") : "";
    return "<" + tag + " " + s + ">" + (text ? text : '') + "</" + tag + ">";
}

// 创建box部分html code
function generateBox(options, content) {
    var id = options.id,
        headerTitle = options.name || '',
        headerClass = options.headerClass || 'with-border',
        headerIcon = options.headerIcon || 'fa fa-th-list',
        headerStyle = options.headerStyle || '',
        boxClass = options.boxClass || 'box-widget',
        boxStyle = options.boxStyle || ''

    // 创建box
    var html = '<div id="' + id + '_container" class="box ' + boxClass + '" style="' + boxStyle + '">\n';

    if (options.boxHeader !== false) {
        // 创建头部html代码
        if (typeof options.generateHeader === 'function') {
            // 由用户自己生成box header
            html += options.generateHeader();
        } else {
            html += '<div class="box-header ' + headerClass + '" style="' + headerStyle + '">\n' +
                (headerIcon ? '<i class="' + headerIcon + '"></i>\n' : '') +
                '<h3 class="box-title">' + headerTitle + '</h3>\n' +
                '    <div class="box-tools pull-right">\n';

            // 由用户自己生成头工具栏
            if (typeof options.generateToolBar === 'function') {
                html += options.generateToolBar();
            }

            html += '    </div>\n' +
                '</div>\n';
        }
    }

    html += content;
    html += '</div>\n';

    return html;
}

// 生成包含box、viewForm、editForm在内的html code
function generateHtml(options) {
    var html = generateBox(options, generateViewFormHtml(options) + generateEditFormHtml(options, true));
    return html;
}

// 生成包含box、editForm在内的html code
function generateEditHtml(options) {
    var html = generateBox(options, generateEditFormHtml(options));
    return html;
}

function generateEditFormHtml(options, hide) {
    var bigScreen;
    if (options.bigScreen === true) bigScreen = true;
    else if (options.bigScreen === false) bigScreen = false;
    else bigScreen = _isBigScreen;

    var defaultConfig = bigScreen ? {
        maxColspan: 3,
        inputSize: 2,
        labelSize: 2,
        server: true, //是否服务器支持新增更新，false时提交数据不会到服务端，而是保存在前端
        columns: [],
        editBodyClass: 'box-body', //body样式
        editFormClass: '', //form样式
        url: '', //form action
        editFormStyle: 'padding: 15px 50px 15px 10px',
        formButtonBarStyle: ''
    } : {
        maxColspan: 2,
        inputSize: 3,
        labelSize: 2,
        server: true, //是否服务器支持新增更新，false时提交数据不会到服务端，而是保存在前端
        columns: [],
        editBodyClass: 'box-body', //body样式
        editFormClass: '', //form样式
        url: '', //form action
        editFormStyle: 'padding: 15px 0px 15px 100px',
        formButtonBarStyle: 'margin-left: -175px'   // 根据editFormStyle向右偏移100，为了button居中这里向左偏移100
    };

    options = $.extend(defaultConfig, options);

    var id = options.id,
        currentColspan = 0,
        currentSize = 0,
        columns = options.columns;

    var html =
        '<div id="' + id + '_edit" class="' + options.editBodyClass + '" ' + (hide == true ? 'style="display: none"' : '') + '>\n' +
        '   <form id="' + id + '_form" action="' + options.url + '" method="post" class="form-horizontal ' + options.editFormClass + '" style="' + options.editFormStyle + '">\n';

    var filedsetClosed, fieldsetIndex = -1;

    for (var i = 0; i < columns.length;) {
        var column = columns[i++],
            fieldBuilder = _FieldBuilderContainer[column.inputType];

        if (column.editable === false) {
            // 不显示编辑
            continue;
        }

        if (column.fieldset === true && i > fieldsetIndex) {
            if (currentColspan > 0) {
                html += '</div>\n';
                currentColspan = 0;
                currentSize = 0;
            }

            if (filedsetClosed === false) {
                html += '</fieldset>\n';
            }


            html += '<fieldset>\n';
            if (column.legend) {
                html += '<legend>' + column.legend + '</legend>\n';
            }

            filedsetClosed = false;
            fieldsetIndex = i;
            i--;
            continue;
        }

        var result = fieldBuilder.generateEditFormHtml(column, currentColspan == 0 ? true : false, options);
        var colSize = result.size;
        var colspan = result.colspan || 1;
        if (colspan > options.maxColspan) {
            colspan = options.maxColspan;
        }

        if (colSize > 12) {
            colSize = 12;
        }

        // 独占一行
        if (currentColspan + colspan <= options.maxColspan && currentSize + colSize <= 12) {
            if (currentColspan == 0) {
                html += '<div class="form-group">\n';
            }

            html += result.html;
            if (colspan == 0) {
                continue;
            } else if (result.back === true) {
                i--;
                currentColspan = currentColspan > 0 ? options.maxColspan : 0;
            } else {
                currentColspan += colspan;
            }
        } else {
            i--;
            currentColspan = options.maxColspan;
        }

        if (column.endRow === true || currentColspan >= options.maxColspan) {
            html += '</div>\n';
            currentColspan = 0;
            currentSize = 0;
        }
    }

    if (currentColspan > 0) {
        html += '</div>\n';
    }

    if (filedsetClosed === false) {
        html += '</fieldset>\n';
    }

    var formButtonBar = options.formButtonBar || [];
    if (options.showSubmitBtn !== false) {
        formButtonBar.push({
            id: id + '_form_submit_btn',
            type: options.server === false ? 'button' : 'submit',
            name: options.submitBtnName || '保存',
            width: options.submitBtnWidth || '120px',
            class: options.submitBtnClass || 'btn btn-success btn-flat',
            order: -1
        });
    }

    if (options.showBackBtn !== false) {
        formButtonBar.push({
            id: id + '_form_back_btn',
            type: 'button',
            width: options.backBtnWidth || '120px',
            name: options.backBtnName || '返回',
            class: options.backBtnClass || 'btn btn-default btn-flat',
            order: 9999
        });
    }

    formButtonBar.sort(function (a, b) {
        return (a.order > b.order) ? 1 : -1;
    });

    if (typeof options.generateFormButtonBar === 'function') {
        html += options.generateFormButtonBar();
    } else if (formButtonBar.length > 0) {
        html += '<div class="form-group ' + (options.formButtonBarClass ? options.formButtonBarClass : 'form-button-bar') + '" style="' + options.formButtonBarStyle + '">\n';
        html += '<div class="btn-group">\n';

        formButtonBar.forEach(function (a) {
            html += '<button type="' + a.type + '" id="' + a.id + '" class="' + a.class + '" style="margin-left: 30px; width: ' + a.width + '">' + a.name + '</button>\n';
        });

        html += '</div>\n' +
            '</div>\n';
    }

    html +=
        '</form>\n' +
        '</div>\n';
    return html;
}

function generateViewHtml(options) {
    var html = generateBox(options, generateViewFormHtml(options));
    return html;
}

function generateViewFormHtml(options) {
    var bigScreen;
    if (options.bigScreen === true) bigScreen = true;
    else if (options.bigScreen === false) bigScreen = false;
    else bigScreen = _isBigScreen;

    var defaultConfig = bigScreen ? {
        maxColspan: 3,
        inputSize: 2,
        labelSize: 2,
        columns: [],
        viewBodyClass: 'box-body', //body样式
        viewFormClass: '', //form样式
        viewFormStyle: 'padding: 15px 50px 15px 10px',
        formButtonBarStyle: ''
    } : {
        maxColspan: 2,
        inputSize: 3,
        labelSize: 2,
        columns: [],
        viewBodyClass: 'box-body', //body样式
        viewFormClass: '', //form样式
        viewFormStyle: 'padding: 15px 0px 15px 100px',
        formButtonBarStyle: 'margin-left: -175px'
    };

    options = $.extend(defaultConfig, options);

    var id = options.id,
        currentColspan = 0,
        currentSize = 0,
        columns = options.columns;


    var html =
        '<div id="' + id + '_view" class="' + options.viewBodyClass + '">\n' +
        '    <form class="form-horizontal ' + options.viewFormClass + '"  style="' + options.viewFormStyle + '">\n';

    var filedsetClosed, fieldsetIndex = -1;

    for (var i = 0; i < columns.length;) {
        var column = columns[i++],
            fieldBuilder = _FieldBuilderContainer[column.inputType];


        if (column.fieldset === true && i > fieldsetIndex) {
            if (currentColspan > 0) {
                html += '</div>\n';
                currentColspan = 0;
                currentSize = 0;
            }

            if (filedsetClosed === false) {
                html += '</fieldset>\n';
            }


            html += '<fieldset>\n';
            if (column.legend) {
                html += '<legend>' + column.legend + '</legend>\n';
            }

            filedsetClosed = false;
            fieldsetIndex = i;
            i--;
            continue;
        }

        var result = fieldBuilder.generateViewFormHtml(column, currentColspan == 0 ? true : false, options);
        var colSize = result.size;
        var colspan = result.colspan || 1;
        if (colspan > options.maxColspan) {
            colspan = options.maxColspan;
        }

        if (colSize > 12) {
            colSize = 12;
        }

        // 附件独占一行
        if (currentColspan + colspan <= options.maxColspan) {
            if (currentColspan == 0) {
                html += '<div class="form-group">\n';
            }

            html += result.html;

            if (colspan == 0) {
                continue;
            } else if (result.back === true) {
                i--;
                currentColspan = currentColspan > 0 ? options.maxColspan : 0;
            } else {
                currentColspan += colspan;
                currentSize += colSize;
            }
        } else {
            i--;
            currentColspan = options.maxColspan;
        }

        if (column.endRow === true || currentColspan >= options.maxColspan) {
            html += '</div>\n';
            currentColspan = 0;
            currentSize = 0;
        }
    }

    if (currentColspan > 0) {
        html += '</div>\n';
    }

    if (filedsetClosed === false) {
        html += '</fieldset>\n';
    }

    viewButtonBar = options.viewButtonBar || [];

    if (typeof options.generateViewButtonBar === 'function') {
        html += options.generateViewButtonBar();
    } else if (options.showBackBtn !== false) {
        viewButtonBar.push({
            id: id + '_view_back_btn',
            type: 'button',
            width: options.backBtnWidth || '150px',
            name: options.backBtnName || '返回',
            class: options.backBtnClass || 'btn btn-default btn-flat',
            order: 9999
        });
    }

    viewButtonBar.sort(function (a, b) {
        return (a.order > b.order) ? 1 : -1;
    });

    if (viewButtonBar.length > 0) {
        html += '<div class="form-group ' + (options.viewButtonBarClass ? options.viewButtonBarClass : 'form-button-bar') + '" style="' + options.formButtonBarStyle + '">\n';
        html += '<div class="btn-group">\n';

        viewButtonBar.forEach(function (a) {
            html += '<button type="' + a.type + '" id="' + a.id + '" class="' + a.class + '" style="margin-left: 30px; width: ' + a.width + '">' + a.name + '</button>\n';
        });

        html += '</div>\n</div>\n';
    }

    html +=
        '   </form>\n' +
        '</div>\n';
    return html;
}


// Model实体类对象
var _Model = function (name, column, options) {
    var that = this;
    that.name = name;
    that.status = "view";

    options = options || {};
    that.container = options.container || $("#" + name + "_container");

    if (that.container.length == 0) {
        that.container = $("body");
    }

    that.viewBody = $("#" + name + "_view");
    that.editBody = $("#" + name + "_edit");
    that.formSubmitBtn = $("#" + name + "_form_submit_btn");
    that.formBackBtn = $("#" + name + "_form_back_btn");
    that.formBody = $("#" + name + "_form");
    that.viewBackBtn = $("#" + name + "_view_back_btn");

    that.columns = column;

    // 注入域构建器
    if (that.columns) {
        that.columns.forEach(function (column) {
            column.fieldBuilder = _FieldBuilderContainer[column.inputType];
            if (!column.fieldBuilder && console) {
                console.log("找不到对应的域构造器[inputType:" + column.inputType + "]");
            }

            column.viewDisplay = column.viewDisplay || "show";
            column.editDisplay = column.editDisplay || "show";
        });
    }

    that.config = $.extend({
        pattern: "normal", // edit:只能编辑,view:只能查看
        extraParam: null, // 可以为方法或对象，用于扩展表单外提交的字段
        beforeSubmit: null, // 提交表单前调取的方法，如果返回false则不提交
        successCallback: function (data) { //成功提交表单后回调
            $.successAlert("保存成功", function () {
                if (typeof that.config.back === 'function') {
                    that.config.back();
                } else {
                    window.location = that.config.back;
                }
            });
        },
        fillViewHandler: null, //填充显示后调用
        fillEditHandler: null //填充编辑后调用
    }, options);


    // 编辑提交按钮点击事件
    if (typeof that.config.submitClick === 'function') {
        that.formSubmitBtn.click(function () {
            that.config.submitClick(that);
        });
    } else if (options.server === false) {
        // 非服务端时，不提交后台，直接前端保存
        that.formSubmitBtn.click(function () {
            if (that.formBody.valid()) {
                var d = that.getFormData();
                that.setData(d);
                //that.toView();
            }
        });
    }

    // 编辑返回按钮点击事件
    that.formBackBtn.click(function () {
        if (typeof that.config.back === 'function') {
            that.config.back();
        } else {
            window.location = that.config.back;
        }
    });

    that.viewBackBtn.click(function () {
        if (typeof that.config.back === 'function') {
            that.config.back();
        } else {
            window.location = that.config.back;
        }
    });

    // 创建表单提交
    if (that.formBody) {
        that.formBody.createForm({
            // 在表单提交前调用
            beforeCallback: function (formData) {
                // 扩展参数
                var extraParam = that.config.extraParam;
                if (extraParam) {
                    if (typeof extraParam === 'function') {
                        extraParam = extraParam();
                    }

                    for (var o in extraParam) {
                        formData.push({
                            name: o,
                            value: extraParam[o],
                            type: "text",
                            required: false
                        });
                    }
                }

                // 每列对提交表单数据处理
                for (var k = 0; k < that.columns.length; k++) {
                    if (that.columns[k].editable === false) continue;
                    if (that.columns[k].fieldBuilder.formDataHandler(that.columns[k], formData, that) === false) {
                        return false;
                    }
                }

                // 提交前调取
                var beforeSubmit = that.config.beforeSubmit;
                if (beforeSubmit && typeof beforeSubmit === 'function') {
                    return beforeSubmit(formData);
                }
            },
            successCallback: that.config.successCallback
        });
    }

    // 初始化依赖关系
    that.dependency = {};
    if (that.columns) {

        // 建立依赖关系
        that.columns.forEach(function (column) {
            if (column.dependency) {
                that.dependency[column.name] = [{
                    target: column.name,
                    dependColumn: column.dependency[0],
                    dependValue: column.dependency.slice(1, column.dependency.length + 1)
                }];
            }
        });

        for (var o in that.dependency) {
            var d = that.dependency[o];
            var fd = d[0].dependColumn;

            var p = that.dependency[fd];
            while (p) {
                d.push(p[0]);
                p = that.dependency[p[0].dependColumn];
            }
        }

        var cache = {};
        for (var o in that.dependency) {
            var depend = that.dependency[o][0];
            if (cache[depend.dependColumn]) {
                continue;
            }
            var dc = that.getColumn(depend.dependColumn);
            dc.fieldBuilder.dependTrigger(dc, that);
            cache[depend.dependColumn] = 1;
        }

        // 初始化接口
        that.columns.forEach(function (column) {
            var fun = column.fieldBuilder.initHandler;
            if (typeof fun === 'function') {
                column.fieldBuilder.initHandler(column, that);
            }
        });

        if (that.config.pattern == 'view') {

        } else if (that.config.pattern == 'edit') {
            that.toEdit(false);
        }
    }
}
// 获取列
_Model.prototype.getColumn = function (columnName) {
    for (var i = 0; i < this.columns.length; i++) {
        if (this.columns[i].name == columnName) {
            return this.columns[i];
        }
    }
    return null;
}
// 设置数据，
_Model.prototype.setData = function (data) {
    var that = this;
    that.data = data;

    if (that.data) {
        // 如果列依赖不成立时，列数据应该为空
        for (var o in that.dependency) {
            var depends = that.dependency[o];
            var tar = depends[0].target;

            if (!that.isDependencySatisfy(depends, that.data)) {
                that.data[tar] = null;
            }
        }
    }

    if (that.columns) {
        that.columns.forEach(function (column) {
            column.fieldBuilder.setDataHandler(column, data, that);
        });
    }

    if (that.status === "edit") {
        that.fillEditBody();
    } else {
        that.fillViewBody();
    }
}
// 填充视图
_Model.prototype.fillViewBody = function () {
    var that = this,
        data = that.data;
    if (that.columns) {
        that.filling = true;
        that.columns.forEach(function (column) {
            column.fieldBuilder.fillView(column, data, that);
        });

        that.filling = false;
        that.checkViewDependency();
    }

    if (typeof that.config.fillViewHandler === 'function') {
        that.config.fillViewHandler(that, that.data);
    }
}
// 填充编辑
_Model.prototype.fillEditBody = function () {
    var that = this,
        data = that.data;
    if (that.columns) {
        that.filling = true;
        that.columns.forEach(function (column) {
            if (column.editable !== false) {
                column.fieldBuilder.fillEdit(column, data, that);
            }
        });
        that.filling = false;
        that.checkEditDependency();
    }

    if (typeof that.config.fillEditHandler === 'function') {
        that.config.fillEditHandler(that, that.data);
    }
}
// 切编辑
_Model.prototype.toEdit = function (refill) {
    var that = this;
    that.container.find(".tonto-model-tool-view-btn").hide();
    that.container.find(".tonto-model-tool-edit-btn").show();
    that.viewBody.hide();
    that.editBody.show();
    refill !== false && that.fillEditBody();
    that.status = 'edit';
}
// 切视图
_Model.prototype.toView = function (refill) {
    var that = this;
    that.container.find(".tonto-model-tool-view-btn").show();
    that.container.find(".tonto-model-tool-edit-btn").hide();
    that.viewBody.show();
    that.editBody.hide();
    refill !== false && that.fillViewBody();
    that.status = 'view';
}
// 是否在依赖值中
_Model.prototype.isInDependencyValues = function (val, vals) {
    // 是否在依赖值内
    if (val != null && val != undefined && val !== "") {
        if ($.isArray(val)) {
            if (val.length > 0) {
                for (var i = 0; i < val.length; i++) {
                    var v = val[i],
                        has = false;
                    for (var j = 0; j < vals.length; j++) {
                        if (v == vals[s]) {
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            for (var i = 0; i < vals.length; i++) {
                if (val == vals[i]) {
                    return true;
                }
            }
        }
    }
    return false;
}
// 是否满足依赖
_Model.prototype.isDependencySatisfy = function (dependencies, data) {
    // 是否满足依赖
    for (var i = 0; i < dependencies.length; i++) {
        var dep = dependencies[i];
        if (!this.isInDependencyValues(data[dep.dependColumn], dep.dependValue)) {
            return false;
        }
    }
    return true;
}
// 检查视图状态下依赖
_Model.prototype.checkViewDependency = function () {
    // 检查VIEW页面依赖
    var that = this,
        data = that.data;
    for (var o in that.dependency) {
        var depends = that.dependency[o],
            targetColumn = that.getColumn(depends[0].target);
        if (!that.isDependencySatisfy(depends, data)) {
            if (targetColumn.viewDisplay == "hide") continue;
            targetColumn.fieldBuilder.hideView(targetColumn, that);
            targetColumn.viewDisplay = "hide";
        } else {
            if (targetColumn.viewDisplay == "show") continue;
            targetColumn.fieldBuilder.showView(targetColumn, that);
            targetColumn.viewDisplay = "show";
        }
    }
}
// 检查编辑状态下依赖
_Model.prototype.checkEditDependency = function () {
    // 检查EDIT页面依赖
    var that = this;
    for (var o in that.dependency) {
        var dependencies = that.dependency[o];
        var isOk = true;

        for (var i = 0; i < dependencies.length; i++) {
            var depend = dependencies[i],
                dependCol = that.getColumn(depend.dependColumn);
            var val = dependCol.editable === false ?
                (that.data ? that.data[depend.dependColumn] : null) :
                dependCol.fieldBuilder.getEditValue(dependCol, that);

            if (!that.isInDependencyValues(val, depend.dependValue)) {
                isOk = false;
                break;
            }
        }

        var targetCol = that.getColumn(dependencies[0].target);
        if (isOk) {
            if (targetCol.editDisplay == "show") continue;
            if (targetCol.editable !== false) {
                targetCol.fieldBuilder.showEdit(targetCol, that);
            }
            targetCol.editDisplay = "show";
        } else {
            if (targetCol.editDisplay == "hide") continue;
            if (targetCol.editable !== false) {
                targetCol.fieldBuilder.hideEdit(targetCol, that);
            }
            targetCol.editDisplay = "hide";
        }
    }
}
// 获取编辑中数据对象
_Model.prototype.getFormData = function () {
    // TODO 附件等处理
    var jsonData = this.formBody.serializeArray();
    var d = {},
        that = this;
    jsonData.forEach(function (item) {
        if (d[item.name]) {
            d[item.name] = d[item.name] + "," + item.value;
        } else {
            d[item.name] = item.value;
        }
    });

    that.columns.forEach(function (column) {
        column.fieldBuilder.getFormData(column, d, that);
    });

    return d;
}

var _FieldBuilderContainer = {};
// 在域构建器每个方法前插入，如果列中已经声明方法，则优先列中声明方法
var _insertBefore = function (name, face, caller) {
    return function () {
        var column = arguments[0];
        if (column && typeof column[name] === 'function') {
            return column[name].call(caller, arguments);
        } else {
            return face.apply(caller, arguments);
        }
    }
}
var _FieldBuilder = function (name, interfaces) {
    var that = this;
    that.name = name;
    that.originInterfaces = interfaces;
    var defaultInterfaces = {
        initHandler: function (column, model) {
        },
        setDataHandler: function (column, data, model) {
            // 插入数据时候调用
            if (data && column.separator) {
                // 如果有分隔符，则初始化分割字符串
                var v = data[column.name];
                if (typeof v === 'string') {
                    var arr = v.split(column.separator);
                    if (arr.length > 1 && arr[arr.length - 1] === '') {
                        arr.splice(arr.length - 1, 1);
                    }
                    data[column.name] = arr;
                }
            }
        },
        formDataHandler: function (column, formData, model) {
            // 提交表单数据调用
            return;
        },
        getFormData: function (column, data, model) {
            // 获取表单列编辑数据
            delete data[column.name];
            if (column.editDisplay !== "hide") {
                return data[column.name] = this.getEditValue(column, model);
            }
        },
        dependTrigger: function (column, model) {
            // 依赖域变化注册，监听依赖域变更
            model.editBody.find("[name='" + column.name + "']").change(function () {
                if (model.filling !== true) {
                    model.checkEditDependency();
                }
            });
        },
        getEditValue: function (column, model) {
            // 获取域EDIT页面值
            return model.editBody.find("[name='" + column.name + "']").val();
        },
        getEditTarget: function (column, model) {
            // 获取编辑目标
            return model.editBody.find("[name='" + column.name + "']");
        },
        getViewTarget: function (column, model) {
            // 获取视图目标
            return model.viewBody.find("[name='" + column.name + "']");
        },
        hideView: function (column, model, target) {
            // 默认class:form-control的div下有label和一个div，div下包含了具体控件
            var p = target || this.getViewTarget(column, model);
            if (p.length == 0) return;
            var d = p.is("div") ? p : p.parent();
            var f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
        },
        showView: function (column, model, target) {
            // 显示视图域
            var p = target || this.getViewTarget(column, model);
            var d = p.is("div") ? p : p.parent();
            d.show();
            d.prev().show();
            d.parent().show();
        },
        fillView: function (column, data, model, target) {
            // VIEW页面填充值时候调用
            var p = target || this.getViewTarget(column, model);
            if (!p || p.length == 0) return;
            var v = data ? data[column.name] : null;

            if (v || v === 0) {
                p.removeClass("text-muted");
                p.text(v);
            } else {
                p.addClass("text-muted");
                p.text("无");
            }
        },
        hideEdit: function (column, model, target) {
            // 隐藏编辑域
            var p = target || this.getEditTarget(column, model);
            if (!p || p.length == 0) return;
            var d = p.is("div") ? p : p.parent();
            var f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
        },
        showEdit: function (column, model, target) {
            // 显示编辑域
            var p = target || this.getEditTarget(column, model);
            if (!p || p.length == 0) return;
            var d = p.is("div") ? p : p.parent();
            d.show();
            d.prev().show();
            d.parent().show();
        },
        fillEdit: function (column, data, model, target) {
            // EDIT页面填充值时候调用        
            if (column.editable === false) {
                return;
            }
            var input = target || this.getEditTarget(column, model);
            if (!input && input.length == 0) return;
            var v = data ? data[column.name] : null;
            if (v || v === 0) {
                input.val(v);
            } else {
                input.val("");
            }
        },
        getRequiredIcon: function (column, options) {
            return column.required === 'required' ? '<i class="required-label fa fa-asterisk"></i>' : '';
        },
        getInputSize: function (colspan, options) {
            return (colspan - 1) * (options.labelSize + options.inputSize) + options.inputSize;
        },
        generateViewFormHtml: function (column, isFirst, options) {
            var labelSize = column.labelSize || options.labelSize,
                colspan = column.colspan || 1,
                inputSize = column.inputSize || this.getInputSize(colspan, options);

            var html = '<label class="col-sm-' + labelSize + ' control-label">' + column.title + '：</label>\n';
            html += '<div class="col-sm-' + inputSize + '">\n';
            html += '<p name="' + column.name + '" class="form-control-static description"></p>\n';
            html += '</div>\n';
            return {
                size: labelSize + inputSize,
                colspan: colspan,
                html: html
            };
        },
        generateEditFormHtml: function (column, isFirst, options) {
            var labelSize = column.labelSize || options.labelSize,
                colspan = column.colspan || 1,
                inputSize = column.inputSize || this.getInputSize(colspan, options),
                required = column.required === 'required';

            var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
            html += '<div class="col-sm-' + inputSize + '">\n';

            var inputAttr = {
                name: column.name,
                placeholder: "请输入" + column.title,
                type: "text",
                class: "form-control",
                required: required ? "required" : null
            }

            if (column.attr) {
                inputAttr = $.extend(inputAttr, column.attr);
            }

            html += generateTag("input", inputAttr);
            html += '</div>\n';
            return {
                size: labelSize + inputSize,
                colspan: colspan,
                html: html
            };
        }
    }

    interfaces = $.extend(defaultInterfaces, interfaces);

    if (_FieldBuilderContainer[name]) {
        console && console.log("存在相同名称的域构建器[name:" + name + "]");
    }

    for (var o in interfaces) {
        var face = interfaces[o];
        if (typeof face === 'function') {
            that[o] = _insertBefore(o, face, that);
        }
    }

    _FieldBuilderContainer[name] = that;
}

// 文本域构建器
var _textFieldBuilder = new _FieldBuilder("TEXT", {});

// 数字域构建器
var _numberFieldBuilder = new _FieldBuilder("NUMBER", {
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;

        if (v || v === 0) {
            p.removeClass("text-muted");
            if (column.unit) {
                p.text(v + column.unit);
            } else {
                p.text(v);
            }
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    hideEdit: function (column, model, target) {
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;

        if (column.unit || column.unitIcon) {
            var d = p.parent().parent,
                f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
        } else {
            var d = p.parent(),
                f = d.parent();
            d.hide();
            d.prev().hide();
            if (f.children(":visible").length == 0) {
                f.hide();
            }
        }
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';

        var attr = {
            placeholder: "请输入" + column.title
        }

        if (column.attr) {
            attr = $.extend(attr, column.attr);
        }

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';
        if (column.unit || column.unitIcon) {
            html += '<div class="input-group">';
            html += '<input name="' + column.name + '" class="form-control" ' + (required ? 'required="required"' : '') + ' type="number" ' + generateTagAttribute(attr) + '></input>\n';
            if (column.unitIcon) {
                html += '<div class="input-group-addon">';
                html += '       <i class="' + column.unitIcon + '"></i>';
                html += '</div>';
            } else {
                html += '   <span class="input-group-addon">' + column.unit + '</span>';
            }
            html += '</div>';
        } else {
            html += '<input name="' + column.name + '" class="form-control" ' + (required ? 'required="required"' : '') + ' type="number" ' + generateTagAttribute(attr) + '></input>\n';
        }

        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 大文本域构建器
var _textAreaFieldBuilder = new _FieldBuilder("TEXTAREA", {
    generateViewFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';
        html += '<pre name="' + column.name + '" style="min-height:150px" class="form-control-static description"></pre>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            html: html
        };
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';

        html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';

        var inputAttr = {
            name: column.name,
            placeholder: "请输入" + column.title,
            rows: column.rows || 5,
            type: "text",
            class: "form-control",
            required: required ? "required" : null
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += generateTag("textarea", inputAttr);
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 日期域构建器
var _dateFieldBuilder = new _FieldBuilder("DATE", {
    setDataHandler: function (column, data, model) {
        // 插入数据时候调用
        var v = data && data[column.name];
        if (typeof v === 'number') {
            data[column.name] = $.formatDate(v, "yyyy-MM-dd");
        } else if (v && v.length > 10) {
            // 前端可能返回 yyyy-MM-dd hh:mm:ss格式，这里简单处理
            v = v.substr(0, 10);
            data[column.name] = v;
        }
    },
    hideEdit: function (column, model, target) {
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.parent().parent(),
            f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
    },
    showEdit: function (column, model, target) {
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.parent().parent();
        d.show();
        d.prev().show();
        d.parent().show();
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';
        var inputAttr = {
            name: column.name,
            placeholder: "请输入" + column.title,
            autocomplete: "off",
            type: "text",
            class: "form-control tonto-datepicker-date",
            required: required ? "required" : null
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += generateTag("input", inputAttr);
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 时间域构建器
var _timeFieldBuilder = new _FieldBuilder("TIME", {
    setDataHandler: function (column, data, model) {
        // 插入数据时候调用
        var v = data && data[column.name];
        if (typeof v === 'number') {
            data[column.name] = $.formatDate(v, "yyyy-MM-dd hh:mm:ss");
        } else if (v && v.length == 10) {
            // 前端可能返回 yyyy-MM-dd格式，这里简单处理
            v = v + ' 00:00:00';
            data[column.name] = v;
        }
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';
        var inputAttr = {
            name: column.name,
            placeholder: "请输入" + column.title,
            autocomplete: "off",
            type: "text",
            class: "form-control tonto-datepicker-datetime",
            required: required ? "required" : null
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += generateTag("input", inputAttr);
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 下拉框域构建器
var _selectFieldBuilder = new _FieldBuilder("SELECT", {
    initHandler: function (column, model) {
        if (column.multiple === true) {
            column.separator = column.separator || ',';
        }
    },
    getDataName: function (column, v) {
        if (column.multiple === true) {
            if (v) {
                if (!$.isArray(v)) return v;
                var vs = [];
                v.forEach(function (vi) {
                    var a = $.getConstantEnumValue(column.enum, vi);
                    a && vs.push(a);
                });
                v = vs.join("，");
            }
        } else {
            if (v || v === 0) {
                v = $.getConstantEnumValue(column.enum, v);
            }
        }
        return v;
    },
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;
        v = this.getDataName(column, v);
        if (v || v === 0) {
            p.removeClass("text-muted");
            p.text(v);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function (column, data, model, target) {
        // EDIT页面填充值时候调用
        if (column.editable === false) {
            return;
        }

        var input = target || this.getEditTarget(column, model);
        if (!input && input.length == 0) return;

        var ov = data ? data[column.name] : null;

        if (ov || ov === 0) {
            if (column.multiple === true) {
                input.val(ov).trigger('change');
            } else {
                input.val(ov);
            }
        } else {
            if (column.multiple === true) {

            } else {
                input.find("option:first").prop("selected", 'selected');
            }
        }
    },
    showEdit: function (column, model, target) {
        // 显示编辑域
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.is("div") ? p : p.parent();
        if (column.multiple === true) {
            d = d.parent();
        }
        d.show();
        d.prev().show();
        d.parent().show();
    },
    hideEdit: function (column, model, target) {
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.is("div") ? p : p.parent();
        if (column.multiple === true) {
            d = d.parent();
        }
        var f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required',
            multiple = column.multiple === true;

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';

        var inputAttr = {
            name: column.name,
            placeholder: column.placeholder || null,
            class: "form-control tonto-select-constant" + (multiple ? ' tonto-multiple-select' : ''),
            required: required ? "required" : null,
            multiple: multiple ? 'multiple' : null,
            enumcode: column.enum
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += '<select ' + generateTagAttribute(inputAttr) + '>\n';
        if (column.nullable !== false && !required && !multiple) {
            html += '<option value="">请选择</option>\n';
        }
        html += '</select>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 下拉框域构建器
var _selectServerFieldBuilder = new _FieldBuilder("SELECT-SERVER", {
    initHandler: function (column, model) {
        if (column.multiple === true) {
            column.separator = column.separator || ',';
        }
        this.getDataFromServer(column, model);
    },
    getDataFromServer: function (column, model) {
        var that = this;
        if (column.ajaxType == 'get') {
            $.postAjax(column.url, function (data) {
                column.serverData = data;
                column.serverDataGot = true;
                that.fillDataFromServer(column, model);
            });
        } else {
            $.postAjax(column.url, function (data) {
                column.serverData = data;
                column.serverDataGot = true;
                that.fillDataFromServer(column, model);
            });
        }
    },
    fillDataFromServer: function (column, model) {
        var input = this.getEditTarget(column, model);
        if (column.editable !== false && column.serverData && input.length > 0) {
            var k = column.idField || 'id',
                n = column.nameField || 'name';
            column.serverData.forEach(function (d) {
                input.append('<option value="' + d[k] + '">' + d[n] + '</option>');
            });

            // input.select2({
            //     placeholder: input.attr("placeholder") || "请选择", //未选择时显示文本
            //     maximumSelectionSize: column.maxSelectionSize || null, //显示最大选项数目
            //     multiple: column.multiple !== false,
            //     width: '100%',
            //     allowClear: true
            // });

            input.selectpicker({
                width: '100%',
                liveSearch: true,        // 开启搜索框
                maxOptions: column.maxSelectionSize || false, //显示最大选项数目
                noneSelectedText: column.noneSelectedText || '没有选中任何项', //多选未选中任意选项提示
            });
        }

        if (model.status == 'view') {
            this.fillView(column, model.data, model);
        }

        if (model.status == 'edit') {
            if (column.editable === false) {
                this.fillView(column, model.data, model, this.getEditTarget(column, model));
            } else {
                this.fillEdit(column, model.data, model);
            }
        }
    },
    getDataName: function (column, v) {
        if (column.serverData) {
            var k = column.idField || 'id',
                n = column.nameField || 'name';
            if (column.multiple === true) {
                if (v) {
                    if ($.isArray(v)) {
                        var vs = [];
                        v.forEach(function (vi) {
                            for (var i = 0; i < column.serverData.length; i++) {
                                var a = column.serverData[i];
                                if (a[k] == vi) {
                                    vs.push(a[n]);
                                }
                            }
                        });
                        v = vs.length > 0 ? vs.join("，") : null;
                    }
                }
            } else {
                if (v || v === 0) {
                    for (var i = 0; i < column.serverData.length; i++) {
                        var a = column.serverData[i];
                        if (a[k] == v) {
                            v = a[n];
                            break;
                        }
                    }
                }
            }
        }
        return v;
    },
    fillView: function (column, data, model, target) {
        if (column.serverDataGot === true) {
            var p = target || this.getViewTarget(column, model);
            if (!p || p.length == 0) return;
            var v = data ? data[column.name] : null;
            v = this.getDataName(column, v);

            if (v || v === 0) {
                p.removeClass("text-muted");
                p.text(v);
            } else {
                p.addClass("text-muted");
                p.text("无");
            }
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }

        if (column.serverDataGot === true) {
            var input = target || this.getEditTarget(column, model);
            if (!input && input.length == 0) return;

            var ov = data ? data[column.name] : null;
            if (ov || ov === 0) {
                input.val(ov).trigger('change');
            } else {
                if (column.multiple === true) {

                } else {
                    input.find("option:first").prop("selected", 'selected');
                }
            }
        }
    },
    hideEdit: function (column, model, target) {
        // 隐藏编辑域
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.is("div") ? p : p.parent();
        if (d.hasClass("dropdown")) {
            d = d.parent();
        }
        var f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
    },
    showEdit: function (column, model, target) {
        // 显示编辑域
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.is("div") ? p : p.parent();
        if (d.hasClass("dropdown")) {
            d = d.parent();
        }
        d.show();
        d.prev().show();
        d.parent().show();
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required',
            multiple = column.multiple === true;

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';

        var inputAttr = {
            name: column.name,
            placeholder: column.placeholder || null,
            class: "form-control",
            required: required ? "required" : null,
            multiple: multiple ? 'multiple' : null
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += '<select ' + generateTagAttribute(inputAttr) + '>\n';
        if (column.nullable !== false && !required && !multiple) {
            html += '<option value="">请选择</option>\n';
        }
        html += '</select>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 树形下拉框域构建器
var _selectTreeServerFieldBuilder = new _FieldBuilder("SELECT-TREE-SERVER", {
    initHandler: function (column, model) {
        if (column.multiple === true) {
            column.separator = column.separator || ',';
        }
        this.getDataFromServer(column, model);
        this.initRemoveButton(column, model);
    },
    getEditValue: function (column, model) {
        return column.selectItem ? column.selectItem.value : null;
    },
    formDataHandler: function (column, formData, model) {
        if (column.editDisplay !== "hide") {
            var setted = false;
            for (var i = 0; i < formData.length; i++) {
                if (formData[i].name == column.name) {
                    formData[i].value = column.selectItem ? column.selectItem.value : null;
                    setted = true;
                    break;
                }
            }

            if (!setted) {
                formData.push({
                    name: column.name,
                    value: column.selectItem ? column.selectItem.value : null,
                    type: "text",
                    required: false
                });
            }
        }
    },
    getDataFromServer: function (column, model) {
        var that = this;
        $.getAjax(column.url, function (data) {
            if (typeof column.selectDataFilter === 'function') {
                data = column.selectDataFilter(column, data);
            }

            var k = column.idField || 'id',
                n = column.nameField || 'name',
                c = column.childrenField || 'children',
                p = column.parentField || 'parentId',
                rv = column.rootParentValue;

            var treeList = [],
                treeData = null;
            if (data) {
                if (!$.isArray(data)) data = [data];
                if (column.isListData) {
                    data.forEach(function (item) {
                        item.text = item[n];
                        item.keyValue = item[k];

                        var pid = item[k];
                        children = $.grep(data, function (n, i) {
                            return n[p] == pid;
                        });

                        item.nodes = children && children.length == 0 ? null : children;
                        treeList.push(item);
                    });

                    treeData = $.grep(data, function (n, i) {
                        if (rv) {
                            return n[p] == rv;
                        } else {
                            return !n[p];
                        }
                    });
                } else {
                    var g = function (items) {
                        var nodes = [];
                        items.forEach(function (item) {
                            var node = {
                                text: item[n],
                                keyValue: item[k],
                                data: item
                            };

                            var children = item[c];
                            if (children) {
                                node.nodes = g(children);
                                if (node.nodes.length == 0) {
                                    node.nodes = null;
                                }
                            }
                            nodes.push(node);
                            treeList.push(node);
                        });
                        return nodes;
                    }
                    treeData = g(data);
                }
            }

            column.serverTreeData = treeData;
            column.serverTreeListData = treeList;
            column.serverDataGot = true;

            that.fillDataFromServer(column, model);
        });
    },
    fillDataFromServer: function (column, model) {
        this.initTreeSelect(column, model);

        if (model.status == 'view') {
            this.fillView(column, model.data, model);
        }

        if (model.status == 'edit') {
            if (column.editable === false) {
                this.fillView(column, model.data, model, this.getEditTarget(column, model));
            } else {
                this.fillEdit(column, model.data, model);
            }
        }
    },
    initRemoveButton: function (column, model) {
        var that = this;
        $("#tonto-tree-select-remove-" + column.name).click(function () {
            that.setSelectItem(column, model, null);
        });
    },
    initTreeSelect: function (column, model) {
        if (column.editable === false) return;
        var that = this;
        var input = that.getEditTarget(column, model);
        if (!input && input.length == 0) return;
        input.click(function () {
            layer.open({
                type: 1,
                title: column.selectTitle || " ",
                content: "<div class='tonto-tree-select-div'></div>",
                area: ['350px', '460px'],
                success: function (layero, index) {
                    if (!column.serverTreeData) return;

                    var $tree = $(layero).find('.tonto-tree-select-div');
                    $tree.treeview({
                        data: column.serverTreeData,
                        levels: column.treeSelectLevel || 1
                    });

                    $tree.on('nodeSelected', function (event, data) {
                        var item = {name: data.text, value: data.keyValue};
                        if (column.viewPathName === true) {
                            item.name = that.getDataName(column, item.value);
                        }

                        if (typeof column.selectedHandler == 'function') {
                            var result = column.selectedHandler(data);
                            if (result === false) {
                                return;
                            }

                            if (result && result !== true) {
                                item = result;
                            }
                        }
                        that.setSelectItem(column, model, item);
                        layer.close(index);
                    });
                }
            });
        });
    },
    setSelectItem: function (column, model, item, target) {
        column.selectItem = item;
        var input = target || this.getEditTarget(column, model);
        if (!input && input.length == 0) return;

        var v = item ? item.name : '';
        var isP = input.is("p") || column.editable === false;

        if (isP) {
            if (v || v === 0) {
                input.removeClass("text-muted");
                input.text(v);
            } else {
                input.addClass("text-muted");
                input.text("无");
            }
        } else {
            if (v || v === 0) {
                input.val(v);
            } else {
                input.val('');
            }
        }
    },
    getDataName: function (column, v) {
        if (column.serverTreeListData) {
            if (column.multiple === true) {
                // 待做
            } else {
                if (v || v === 0) {
                    if (column.viewPathName === true) {
                        var g = function (items, str) {
                            for (var i = 0; i < items.length; i++) {
                                var item = items[i];
                                if (item.keyValue == v) {
                                    return str ? str + "-" + item.text : item.text;
                                } else {
                                    if (item.nodes) {
                                        var r = g(item.nodes, str ? str + "-" + item.text : item.text);
                                        if (r) return r;
                                    }
                                }
                            }
                        }

                        return g(column.serverTreeData);
                    } else {
                        for (var i = 0; i < column.serverTreeListData.length; i++) {
                            var a = column.serverTreeListData[i];
                            if (a.keyValue == v) {
                                v = a.text;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return v;
    },
    fillView: function (column, data, model, target) {
        if (column.serverDataGot === true) {
            var p = target || this.getViewTarget(column, model);
            if (!p || p.length == 0) return;
            var v = data ? data[column.name] : null;
            v = this.getDataName(column, v);

            if (v || v === 0) {
                p.removeClass("text-muted");
                p.text(v);
            } else {
                p.addClass("text-muted");
                p.text("无");
            }
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }

        if (column.serverDataGot === true) {
            var ov = data ? data[column.name] : null;
            var v = this.getDataName(column, ov);
            if (v || v === 0) {
                this.setSelectItem(column, model, {
                    name: v,
                    value: ov
                }, target);
            } else {
                this.setSelectItem(column, model, null, target);
            }
        }
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required',
            multiple = column.multiple === true;

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';
        html += '<div class="input-group">'

        var inputAttr = {
            name: column.name,
            placeholder: column.placeholder || "请选择" + column.title,
            class: "form-control",
            autocomplete: "off",
            readonly: "readonly",
            style: "background: rgb(255, 255, 255);",
            required: required ? "required" : null
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += '<input ' + generateTagAttribute(inputAttr) + '/>\n';
        html += '<span class="input-group-addon" id="tonto-tree-select-remove-' + column.name + '" style="cursor:pointer"><i class="glyphicon glyphicon-remove"></i></span>';
        html += '</div>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 附件域构建器
var _attachmentFieldBuilder = new _FieldBuilder("ATTACHMENT", {
    setDataHandler: function (column, data, model) {
        // 解析的附件
        if (!data) return;

        var filename = column.fileName,
            v = data[column.name];

        var files = data[filename];
        if (files && !$.isArray(files)) {
            data[filename] = [files];
        }

        if (v) {
            data[column.name] = v.split(column.separator || ",");
        }
    },
    getFormData: function (column, data, model) {
        delete data[column.name];
        delete data[column.fileName];
        if (column.editDisplay !== "hide") {
            // 有附件时，需要替换某些参数
            var previews = column.inputAttachment.fileinput('getPreview');
            var attachments = "";
            if (previews && previews.config && previews.config.length > 0) {
                previews.config.forEach(function (p) {
                    attachments += p.key + ",";
                });
            }

            data[column.name] = attachments;

            // 动态加入未上传的文件数据
            var files = column.inputAttachment.fileinput('getFileStack');
            if (files) {
                var fileArr = [];

                files.forEach(function (file) {
                    fileArr.push(file);
                });
                data[column.fileName] = fileArr;
            }
        }
    },
    formDataHandler: function (column, formData, model) {
        var maxFileCount = column.maxFileCount || 5,
            fileName = column.fileName,
            fileCount = 0,
            i = 0;

        // 原表单文件数据只有最后一个，这里需要手动从插件中获取File Object添加到表单数据中
        for (; i < formData.length; i++) {
            if (formData[i].name == fileName) {
                formData.splice(i, 1);
                i--;
            }
        }

        if (column.editDisplay !== "hide") {
            // 有附件时，需要替换某些参数
            var previews = column.inputAttachment.fileinput('getPreview');
            var attachments = "";
            if (previews && previews.config && previews.config.length > 0) {
                previews.config.forEach(function (p) {
                    attachments += p.key + ",";
                    fileCount++;
                });
            }

            // 动态加入已经上传的附件ID
            formData.push({
                name: column.name,
                value: attachments,
                type: "text",
                required: false
            });

            // 动态加入未上传的文件数据
            var files = column.inputAttachment.fileinput('getFileStack');
            if (files) {
                files.forEach(function (file) {
                    formData.push({
                        name: fileName,
                        value: file,
                        type: "file",
                        required: false
                    });
                    fileCount++;
                });
                model.formBody.attr('enctype', 'multipart/form-data');
            } else {
                model.formBody.attr('enctype', 'application/x-www-form-urlencoded');
            }


            if (fileCount > maxFileCount) {
                $.errorAlert("附件数量不能超过" + maxFileCount + "个");
                return false;
            }
        }
    },
    dependTrigger: function (column, model) {
        // 不能被依赖
        console && console.log("附件不应该被依赖");
    },
    getEditValue: function (column, model) {
        // 获取文件数据暂不支持
        console && console.log("暂不实现文件数据获取");
    },
    fillView: function (column, data, model, target) {
        var name = column.name,
            atts = data && data[column.fileName];

        var attDiv = target || this.getViewTarget(column, model);
        if (attDiv.length == 0) return;

        if (atts) {
            var html = '<ul class="mailbox-attachments clearfix">';
            for (var i = 0; i < atts.length; i++) {
                var b = atts[i];
                var k = b.name.lastIndexOf(".");
                var suffix = "";
                if (k >= 0) {
                    suffix = b.name.substring(k + 1).toLowerCase();
                }

                var header = "";
                if (suffix == "jpeg" || suffix == "jpg" || suffix == "png" || suffix == "gif") {
                    header = '<span class="mailbox-attachment-icon has-img"><img src="' + b.url + '" alt="Attachment"></span>';
                } else {
                    var iconMap = {
                        txt: "fa-file-text-o",
                        xls: "fa-file-excel-o",
                        xlsx: "fa-file-excel-o",
                        pdf: "fa-file-pdf-o",
                        doc: "fa-file-word-o",
                        docx: "fa-file-word-o",
                        rar: "fa-file-zip-o",
                        zip: "fa-file-zip-o"
                    }
                    var icon = iconMap[suffix] || "fa-file-o";
                    header = '<span class="mailbox-attachment-icon"><i class="fa ' + icon + '"></i></span>';
                }

                html +=
                    '<li>' + header +
                    '    <div class="mailbox-attachment-info">' +
                    '        <a target="_blank" href="' + b.url + '" class="mailbox-attachment-name"><i class="fa fa-camera"></i>' + b.name + '</a>' +
                    '        <span class="mailbox-attachment-size">' + (Math.floor(b.size / 1024) + "KB") + '<a target="_blank" download="' + b.name + '" href="' + b.url + '" class="btn btn-default btn-xs pull-right"><i class="fa fa-cloud-download"></i></a></span>' +
                    '    </div>' +
                    '</li>';
            }
            html += "</ul>";
            attDiv.html(html);
        } else {
            attDiv.html("");
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }

        var name = column.fileName,
            atts = data ? data[name] : null,
            fileInput = target || model.editBody.find("[name='" + column.fileName + "']");

        if (fileInput.length == 0) return;

        var initialPreview = [];
        var initialPreviewConfig = [];
        if (atts) {
            atts.forEach(function (att) {
                initialPreview.push(att.url);
                initialPreviewConfig.push({
                    caption: att.name,
                    size: att.size,
                    key: att.id
                });
            });
        }

        if (column.inputAttachment) {
            column.inputAttachment.fileinput('destroy');
        }

        column.inputAttachment = $(fileInput).fileinput({
            language: 'zh',
            uploadUrl: '/common/upload/files',
            showUpload: false,
            layoutTemplates: {
                actionUpload: '' //去除上传预览缩略图中的上传图片；
            },
            allowedPreviewTypes: ['image'],
            uploadAsync: false,
            maxFileCount: column.maxFileCount || 5,
            allowedFileExtensions: column.allowedFileExtensions || (column.isPicture === true ? ["jpeg", "jpg", "png", "gif"] :
                ["jpeg", "jpg", "png", "gif", "txt", "pdf", "xlsx", "xls", "doc", "docx", "rar", "zip", "mp4"]),
            overwriteInitial: false,
            dropZoneEnabled: false, // 禁止拖拽
            ajaxDelete: false, // 扩展定义配置，不进行后台删除操作
            initialPreview: initialPreview,
            initialPreviewAsData: true, // allows you to set a raw markup
            initialPreviewFileType: 'image', // image is the default and can be overridden in config below
            initialPreviewConfig: initialPreviewConfig
        });
    },
    generateViewFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + column.title + '：</label>\n';
        html += '<div name="' + column.name + '" class="col-sm-' + inputSize + '"></div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        var attrHtml = column.attr ? generateTagAttribute(column.attr) : "";
        html += '<div name="' + column.name + '" class="col-sm-' + inputSize + '" ' + attrHtml + '>\n';
        html += '<input type="file" name="' + column.fileName + '" ' + (column.maxFileCount === 1 ? '' : 'multiple') + '>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 单选构建器
var _radioFieldBuilder = new _FieldBuilder("RADIO", {
    getEditValue: function (column, model) {
        return model.editBody.find("input[name='" + column.name + "']:checked").val();
    },
    dependTrigger: function (column, model) {
        // 这里使用icheck 所以调用ifChecked事件
        model.editBody.find("input[name='" + column.name + "']").on('ifChecked', function () {
            if (model.filling !== true) {
                model.checkEditDependency();
            }
        });
    },
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;
        if (column.enum) {
            v = $.getConstantEnumValue(column.enum, v);
        }

        if (v || v === 0) {
            p.removeClass("text-muted");
            p.text(v);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }
        var input = target || this.getEditTarget(column, model);
        if (!input && input.length == 0) return;
        var ov = data ? data[column.name] : null;
        input.each(function () {
            var a = $(this);
            if (a.val() == ov) {
                a.iCheck('check');
            }
        });
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';
        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        var attrHtml = column.attr ? generateTagAttribute(column.attr) : "";
        html += '<div class="tonto-radio-constant col-sm-' + inputSize + '" name="' + column.name + '" ' + (required ? 'required="required"' : '') + ' enumcode="' + column.enum + '" ' + attrHtml + '></div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 多选选构建器
var _checkBoxFieldBuilder = new _FieldBuilder("CHECKBOX", {
    setDataHandler: function (column, data, model) {
        // 解析的附件
        var v = data && data[column.name];
        if (v) {
            data[column.name] = v.split(column.separator || ",");
        }
    },
    getEditValue: function (column, model) {
        var vals = [];
        model.editBody.find("input[name='" + column.name + "']:checked").each(function () {
            vals.push($(this).val());
        });
        return vals.join();
    },
    dependTrigger: function (column, model) {
        // 这里使用icheck 所以调用ifChecked事件
        model.editBody.find("input[name='" + column.name + "']").on('ifChecked', function () {
            if (model.filling !== true) {
                model.checkEditDependency();
            }
        });
    },
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;

        if (v) {
            var t = [];
            v.forEach(function (a) {
                t.push(column.enum ? $.getConstantEnumValue(column.enum, a) : a);
            });

            t = t.length > 0 ? t.join("，") : "无";

            p.removeClass("text-muted");
            p.text(t);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }
        var v = data ? data[column.name] : null;
        if (v || v === 0) {
            v.forEach(function (a) {
                model.editBody.find("input[name='" + column.name + "'][value='" + a + "']").iCheck('check');
            });
        }
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';
        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        var attrHtml = column.attr ? generateTagAttribute(column.attr) : "";
        html += '<div name="' + column.name + '" class="tonto-checkbox-constant col-sm-' + inputSize + '" ' + (required ? 'required="required"' : '') + ' enumcode="' + column.enum + '" ' + attrHtml + '></div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 标签域构建器
var _tagsinputFieldBuilder = new _FieldBuilder("TAGSINPUT", {
    setDataHandler: function (column, data, model) {
        // 解析的附件
        var v = data && data[column.name];
        if (v) {
            data[column.name] = v.split(column.separator || ",");
        }
    },
    getEditValue: function (column, model) {
        return model.editBody.find("input[name='" + column.name + "']").tagsinput("items");
    },
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;

        if (v) {
            var t = $.isArray(v) ? v.join("，") : v;
            p.removeClass("text-muted");
            p.text(t);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }
        var input = target || this.getEditTarget(column, model);
        if (!input && input.length == 0) return;
        input.tagsinput("removeAll");
        var v = data ? data[column.name] : null;

        if (v || v === 0) {
            v.forEach(function (a) {
                input.tagsinput('add', a);
            });
        }
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + inputSize + '">\n';
        var attrHtml = column.attr ? generateTagAttribute(column.attr) : "";
        html += '<input name="' + column.name + '" type="text" class="form-control" data-role="tagsinput" placeholder="输入内容后回车" ' +
            (required ? 'required="required"' : '') + ' ' + attrHtml + '/>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 子模块域构建器
var _subModelFieldBuilder = new _FieldBuilder("SUB-MODEL", {
    getEditValue: function (column, model) {
        if (column.contentMap) {
            var datas = [];
            for (var o in column.contentMap) {
                datas.push(column.contentMap[o].data);
            }
            return datas;
        }
        return null;
    },
    getFormData: function (column, data) {
        var datas = [];
        if (column.contentMap) {
            for (var o in column.contentMap) {
                datas.push(column.contentMap[o].data);
            }
        }
        data[column.name] = datas;
    },
    fillView: function (column, data, model, target) {
        var that = this,
            div = target || this.getViewTarget(column, model),
            ul = $('<ul class="products-list product-list-in-box"></ul>');
        if (div.length == 0) return;
        div.empty();
        div.append(ul);
        var subData = data ? data[column.name] : null;
        if (subData) {
            subData.forEach(function (d) {
                that.fillSubView(column, d, model, ul);
            });
        }
    },
    fillSubView: function (column, data, model, contentContainer) {
        var itemHtml, that = this;
        if (typeof column.createSubDataHtml === 'function') {
            itemHtml = column.createSubDataHtml();
        } else {
            itemHtml = '';
            var subTitleViewHtml;
            if (typeof column.subTitleViewHtml === 'function') {
                subTitleViewHtml = column.subTitleViewHtml(data);
            } else {
                subTitleViewHtml += "<h3 style='display: inline-block;font-size: 18px;margin: 0;line-height: 1;'>" + data[column.subViewField] + "</h3>";
            }

            itemHtml += subTitleViewHtml;
        }

        contentContainer.append(itemHtml);
    },
    fillSubEdit: function (column, data, model, id) {
        var contentContainer = column.contentContainer,
            itemHtml, that = this;
        if (typeof column.createSubDataHtml === 'function') {
            itemHtml = column.createSubDataHtml();

        } else {
            itemHtml = '<div class="pull-right">' +
                '<a class="btn" id="' + column.name + '_sub_edit_btn" href="javascript:void(0)"><i class="fa fa-edit"></i>编辑</a>\n' +
                '<a class="btn" id="' + column.name + '_sub_remove_btn" href="javascript:void(0)"><i class="fa fa-remove"></i>删除</a>\n' +
                '</div>';
            var subTitleViewHtml;
            if (typeof column.subTitleViewHtml === 'function') {
                subTitleViewHtml = column.subTitleViewHtml(data);
            } else {
                subTitleViewHtml += "<h3 style='display: inline-block;font-size: 18px;margin: 0;line-height: 1;'>" + data[column.subViewField] + "</h3>";
            }

            itemHtml += subTitleViewHtml;
        }

        var div, com;
        if (!id) {
            id = column.name + "_content_" + _generateId();
            div = $('<li class="item" style="background: none;"></li>');
            com = {
                id: id,
                div: div,
                data: data
            };
            column.contentMap[id] = com;
            contentContainer.append(div);
            div.html(itemHtml);
        } else {
            com = column.contentMap[id];
            div = com.div;
            com.data = data;
            div.html(itemHtml);
        }

        div.find('#' + column.name + '_sub_edit_btn').click(function () {
            that.openSubEditor(column, com, model);
        });

        div.find('#' + column.name + '_sub_remove_btn').click(function () {
            layer.confirm('确定删除吗?', function (layerIndex) {
                delete column.contentMap[id];
                div.remove();
                layer.close(layerIndex);
            });
        });
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }

        var that = this,
            div = target || this.getEditTarget(column, model);

        if (div.length == 0) return;

        if (!column.hasEdited) {
            var contentContainer = $('<ul class="products-list product-list-in-box"></ul>'),
                addSubModelBtn = column.addSubModelBtn ? column.addSubModelBtn :
                    $('<div class="dotted-line-btn"><a href="javascript:void(0)" ><i class="fa fa-plus"></i>' + (column.addSubModelBtnTitle ? column.addSubModelBtnTitle : '添加选项') + '</a></div>');
            div.append(contentContainer);
            div.append(addSubModelBtn);
            column.contentContainer = contentContainer;
            column.contentMap = {};

            var subData = data ? data[column.name] : null;
            if (subData) {
                subData.forEach(function (d) {
                    that.fillSubEdit(column, d, model, null);
                });
            }

            addSubModelBtn.click(function () {
                that.openSubEditor(column, null, model);
            });

            column.hasEdited = true;
        }
    },
    openSubEditor: function (column, com, model) {
        var that = this;
        var subOp = column.subModelOptions;
        subOp.id = subOp.id || column.name + "_" + _generateId();

        var defaultSubOp = {
            server: false,
            maxColspan: 1,
            maxColspan: 1,
            inputSize: 8,
            labelSize: 3,
            editFormStyle: 'padding-left: 10px;'
        }

        var subOp = $.extend(defaultSubOp, subOp);
        var html = generateEditFormHtml(subOp, false);
        html = "<div style='padding: 50px 10px 50px 10px;'>" + html + "</div>";
        var layerOption = subOp.layerOption || {};
        layerOption = $.extend({
                success: function (layero, index) {
                    $.initComponent($(layero));

                    var subModel = new tonto.Model(subOp.id, subOp.columns, {
                        server: false,
                        pattern: "edit",
                        back: function () {
                            layer.close(index);
                        },
                        submitClick: function () {
                            if (subModel.formBody.valid()) {
                                var d = subModel.getFormData();
                                if (typeof column.beforeAddHandler === 'function') {
                                    if (column.beforeAddHandler(d, subModel, index) === false) {
                                        return;
                                    }
                                }

                                that.fillSubEdit(column, d, model, com ? com.id : null);
                                layer.close(index);
                            }
                        }
                    });

                    if (com) {
                        subModel.setData(com.data);
                    } else {
                        subModel.setData(null);
                    }
                }
            },
            layerOption);
        var index = $.openPageLayer(html, layerOption);
    },
    generateViewFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + column.title + '：</label>\n';
        html += '<div name="' + column.name + '" class="col-sm-' + inputSize + '"></div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div name="' + column.name + '" class="col-sm-' + inputSize + '"></div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 富文本编辑域构建器
var _editorFieldBuilder = new _FieldBuilder("EDITOR", {
    initHandler: function (column, model) {
        if (model.config.pattern != 'view') {
            var that = this;
            column.editor = UE.getEditor(model.name + '_' + column.name + '_editor');
            column.editor.ready(function () {
                column.editorReady = true;
                that.fillEdit(column, model.data, model);
            })
        }

        if (model.config.pattern != 'edit') {
            $("#" + model.name + '_' + column.name + '_editor_show_btn').click(function () {
                var content = model.data ? model.data[column.name] : '';
                $.openPageLayer('<div style="padding:40px;padding-right:55px">' + (content || '') + '</div>');
            });
        }
    },
    getEditValue: function (column, model) {
        return column.editor.getContent();
    },
    getFormData: function (column, data) {
        data[column.name] = column.editor.getContent();
    },
    formDataHandler: function (column, formData, model) {
        var content = column.editor.getContent();

        if (!content && column.required === 'required') {
            $.errorMessage(column.title + "不能为空");
            return false;
        }

        formData.push({
            name: column.name,
            value: content,
            type: "text",
            required: false
        });
    },
    fillView: function (column, data, model) {

    },
    fillEdit: function (column, data, model) {
        if (column.editable === false) {
            return;
        }
        if (column.editorReady === true) {
            var content = data ? data[column.name] : '';
            column.editor.setContent(content || '');
        }
    },
    generateViewFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + column.title + '：</label>\n';
        html += '<div name="' + column.name + '" class="col-sm-' + inputSize + '"><label class="control-label">' +
            '<a href="javascript:void(0)" id="' + options.id + '_' + column.name + '_editor_show_btn">查看富文本</a>' +
            '</label></div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || options.maxColspan,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        var height = column.height || "500px";
        html += '<div name="' + column.name + '" class="col-sm-' + inputSize + '">' +
            '<script type="text/plain" id="' + options.id + '_' + column.name + '_editor" style="width:100%;height:' + height + ';"></script>' +
            '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// 省市区联动控件，需要district相关插件，pick地区插件可能有问题
var _areaPickFieldBuilder = new _FieldBuilder("AREA-PICK", {
    getDataName: function (column, v) {
        if (v || v === 0) {
            v = getDistrictFullName(v);
        }
        return v;
    },
    getEditTarget: function (column, model) {
        // 获取编辑目标
        return column.editable === false ?
            model.editBody.find("[name='" + column.name + "']") :
            model.editBody.find("[name='" + column.name + "_div']");
    },
    getEditValue: function (column, model) {
        var $pick = this.getEditTarget(column, model).find(".pick-show .pick-area");
        return $pick.attr("data-areacode");
    },
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.name] : null;
        v = this.getDataName(column, v);
        if (v || v === 0) {
            p.removeClass("text-muted");
            p.text(v);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }
        // EDIT页面填充值时候调用
        var that = this;
        var v = data ? data[column.name] : null;

        if (that.pickArea) {
            that.pickArea.empty();
        } else {
            that.pickArea = that.getEditTarget(column, model);
        }

        var format = v ? getDistrictFullName(v, "/") : "province/city/county";
        var option = {
            "format": format, //格式
            // "display":"block",
            // "width": 400,//设置“省市县”文本边框的宽度
            // "height":"48",//设置“省市县”文本边框的高度
            // "borderColor":"#435abd",//设置“省市县”文本边框的色值
            // "arrowColor":"#435abd",//设置下拉箭头颜色
            // "listBdColor": "#435abd",//设置下拉框父元素ul的border色值
            // "color":"#435abd",//设置“省市县”字体颜色
            // "fontSize":"15px",//设置字体大小
            // "hoverColor": "#435abd",
            // "paddingLeft": "10px",//设置“省”位置处的span相较于边框的距离
            // "arrowRight": "10px",//设置下拉箭头距离边框右侧的距离
            "maxHeight": column.dropdownMaxHeight || "250px",
            "getVal": function () {
                //这个方法是每次选中一个省、市或者县之后，执行的方法
                var code = that.getEditValue(column, model);
                if (code) {
                    var cs = code.split(",");
                    code = cs[cs.length - 1];
                    if (code.endsWith(',')) {
                        code = code.substring(0, code.length - 1);
                    }
                }
                model.editBody.find("[name='" + column.name + "']").val(code);
            }
        };

        var pickOption = column.pickOption;
        if (pickOption) {
            option = $.extend(option, pickOption);
        }

        that.pickArea.pickArea(option);
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options);

        var html = '<label for="' + column.name + '" class="col-sm-' + labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div name="' + column.name + '_div" class="col-sm-' + inputSize + ' pick-area">\n';
        html += '</div>\n';
        html += '<input type="hidden" name="' + column.name + '">'
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

// table选择构建域，暂时只支持单选，多选需要记录数据
var _selectTableServerFieldBuilder = new _FieldBuilder("SELECT-TABLE-SERVER", {
    initHandler: function (column, model) {
        if (column.multiple === true) {
            column.separator = column.separator || ',';
        }

        column.rowValueField = column.rowValueField || 'id';
        column.rowNameField = column.rowNameField || 'name';
        column.nameValueField = column.nameValueField || (column + 'Name');

        this.initTableSelect(column, model);
        this.initRemoveButton(column, model);
    },
    getEditValue: function (column, model) {
        return column.selectItem ? column.selectItem.value : null;
    },
    formDataHandler: function (column, formData, model) {
        if (column.editDisplay !== "hide") {
            var setted = false;
            for (var i = 0; i < formData.length; i++) {
                if (formData[i].name == column.name) {
                    formData[i].value = column.selectItem ? column.selectItem.value : null;
                    setted = true;
                    break;
                }
            }

            if (!setted) {
                formData.push({
                    name: column.name,
                    value: column.selectItem ? column.selectItem.value : null,
                    type: "text",
                    required: false
                });
            }
        }
    },
    fillDataFromServer: function (column, model) {
        this.initTreeSelect(column, model);

        if (model.status == 'view') {
            this.fillView(column, model.data, model);
        }

        if (model.status == 'edit') {
            if (column.editable === false) {
                this.fillView(column, model.data, model, this.getEditTarget(column, model));
            } else {
                this.fillEdit(column, model.data, model);
            }
        }
    },
    initRemoveButton: function (column, model) {
        var that = this;
        $("#tonto-table-select-remove-" + column.name).click(function () {
            that.setSelectItem(column, model, null);
        });
    },
    initTableSelect: function (column, model) {
        if (column.editable === false) return;
        var that = this;
        var input = that.getEditTarget(column, model);
        if (!input && input.length == 0) return;
        input.click(function () {
            var content = '<div class="tonto-tree-select-div" style="padding:10px">';

            var random = _generateId();

            var searchFormId = column.name + "_search_form" + random;
            var tableId = column.name + "_table" + random;
            var confirmBtn = column.name + '_confirm_btn' + random;
            var searchBtn = column.name + '_search_btn' + random;
            var search = column.search;

            if (search) {
                if (search.length == 1) {
                    content +=
                        '<form id="' + searchFormId + '"><div class="pull-left input-group" style="margin-bottom:10px;width:250px">' +
                        '   <input class="form-control" name="' + search[0].name + '" type="text" placeholder="' + (search[0].placeholder || '请输入') + '" autocomplete="off">' +
                        '   <span class="input-group-btn">' +
                        '        <button id="' + searchBtn + '" class="btn btn-default" type="button" title="Search"><i class="fa fa-search"></i></button>' +
                        '   </span>' +
                        '</div></form>';
                } else {
                    content += '<form id="' + searchFormId + '" class="form-horizontal form-search">';

                    var surplus = 12;
                    var cfg = {
                        colspan: 3,
                        placeholder: '请输入'
                    }

                    var hasBtn = false;

                    for (var i = 0; i < search.length; i++) {
                        var a = search[i];
                        a = $.extend(cfg, a);

                        if (surplus < a.colspan) {
                            surplus = 12;
                            content += '</div>';
                        }

                        if (surplus == 12) {
                            content += '<div class="form-group">';
                        }

                        if (a.isButton === true) {
                            content += '<div class="col-md-' + a.colspan + '">' +
                                '<button id="' + searchBtn + '" class="btn btn-primary" type="button"><i class="fa fa-search"></i>查询</button>' +
                                '</div>';
                            hasBtn = true;
                        } else {
                            content += '<div class="col-md-' + a.colspan + '">' +
                                '<input class="form-control" name="' + a.name + '" type="text" placeholder="' + a.placeholder + '" autocomplete="off">' +
                                '</div>';
                        }

                        surplus -= a.colspan;

                        if (surplus <= 0) {
                            surplus = 12;
                            content += '</div>';
                        }
                    }

                    if (!hasBtn) {
                        content += '<div class="col-md-' + surplus + '">' +
                            '<div class="pull-right">' +
                            '<button id="' + searchBtn + '" class="btn btn-primary" type="button"><i class="fa fa-search"></i>查询</button>' +
                            '</div></div>';
                    }

                    content += '</div></form>';
                }
            }

            content += '<table id="' + tableId + '"></table>';
            content += '<div class="col-sm-2 col-sm-offset-5 btn-back">';
            content += '<button class="btn btn-primary btn-block" id="' + confirmBtn + '">确定</button>';
            content += '</div>';
            content += '</div>';

            $.openPageLayer(content, {
                title: column.selectTitle || " ",
                width: column.width || 600,
                height: column.height || 400,
                success: function (layero, index) {
                    if (column.search) {
                        column.tableOption.searchbar = '#' + searchFormId;
                    }

                    column.tableOption.clickToSelect = column.tableOption.clickToSelect === false ? false : true;

                    var table = $.createTable("#" + tableId, column.tableOption);
                    $("#" + searchBtn).click(function () {
                        table.refresh();
                    });

                    $("#" + confirmBtn).click(function () {
                        var result = table.getAllSelections();
                        if (result && result.length > 0) {
                            if (typeof column.selectedHandler == 'function') {
                                var result = column.selectedHandler(selected);
                                if (result === false) {
                                    return;
                                }
                            }

                            if (result && result !== true) {
                                item = {
                                    name: result[0][column.rowNameField],
                                    value: result[0][column.rowValueField]
                                };
                            }

                            that.setSelectItem(column, model, item);
                            layer.close(index);
                        } else {
                            $.errorAlert("请至少选择一个");
                        }
                    });
                }
            });
        });
    },
    setSelectItem: function (column, model, item, target) {
        column.selectItem = item;
        var input = target || this.getEditTarget(column, model);
        if (!input && input.length == 0) return;

        var v = item ? item.name : '';
        var isP = input.is("p") || column.editable === false;

        if (isP) {
            if (v || v === 0) {
                input.removeClass("text-muted");
                input.text(v);
            } else {
                input.addClass("text-muted");
                input.text("无");
            }
        } else {
            if (v || v === 0) {
                input.val(v);
            } else {
                input.val('');
            }
        }
    },
    fillView: function (column, data, model, target) {
        var p = target || this.getViewTarget(column, model);
        if (!p || p.length == 0) return;
        var v = data ? data[column.nameValueField] : null;
        if (v || v === 0) {
            p.removeClass("text-muted");
            p.text(v);
        } else {
            p.addClass("text-muted");
            p.text("无");
        }
    },
    fillEdit: function (column, data, model, target) {
        if (column.editable === false) {
            return;
        }
        var ov = data ? data[column.name] : null;
        var v = data ? data[column.nameValueField] : null;
        if (v || v === 0) {
            this.setSelectItem(column, model, {
                name: v,
                value: ov
            }, target);
        } else {
            this.setSelectItem(column, model, null, target);
        }
    },
    hideEdit: function (column, model, target) {
        // 隐藏编辑域
        var p = target || this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.is("div") ? p : p.parent().parent();
        var f = d.parent();
        d.hide();
        d.prev().hide();
        if (f.children(":visible").length == 0) {
            f.hide();
        }
    },
    showEdit: function (column, model) {
        // 显示编辑域
        var p = this.getEditTarget(column, model);
        if (!p || p.length == 0) return;
        var d = p.is("div") ? p : p.parent().parent();
        d.show();
        d.prev().show();
        d.parent().show();
    },
    generateEditFormHtml: function (column, isFirst, options) {
        var labelSize = column.labelSize || options.labelSize,
            colspan = column.colspan || 1,
            inputSize = column.inputSize || this.getInputSize(colspan, options),
            required = column.required === 'required';

        var html = '<label for="' + column.name + '" class="col-sm-' + options.labelSize + ' control-label">' + this.getRequiredIcon(column, options) + column.title + '：</label>\n';
        html += '<div class="col-sm-' + this.getEditColSize(column, colspan, options) + '">\n';
        html += '<div class="input-group">'

        var inputAttr = {
            name: column.name,
            placeholder: column.placeholder || "请选择" + column.title,
            class: "form-control",
            autocomplete: "off",
            readonly: "readonly",
            style: "background: rgb(255, 255, 255);",
            required: required ? "required" : null
        }

        if (column.attr) {
            inputAttr = $.extend(inputAttr, column.attr);
        }

        html += '<input ' + generateTagAttribute(inputAttr) + '/>\n';
        html += '<span class="input-group-addon" id="tonto-table-select-remove-' + column.name + '" style="cursor:pointer"><i class="glyphicon glyphicon-remove"></i></span>';
        html += '</div>\n';
        html += '</div>\n';
        return {
            colspan: colspan,
            size: labelSize + inputSize,
            html: html
        };
    }
});

if (!window.toton) window.toton = {};
window.tonto.Model = _Model;
window.tonto.FieldBuilder = _FieldBuilder;