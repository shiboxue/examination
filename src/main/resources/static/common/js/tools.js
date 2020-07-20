/**
 * @description JS Utils Object
 * @date 2019/10/04
 * @author wangxin
 * @copyright Copyright (c) 2019中国软件与技术服务股份有限公司
 * @company 中国软件与技术服务股份有限公司
 */
var cssTools = {
    styles: {
        tableRowStyle: function (row, index) {
            var classes = ['table_row_active', 'table_row_success', 'table_row_info', 'table_row_warning', 'table_row_danger'];
            if (row.health == 'green') {
                return {
                    classes: classes[1]
                };
            } else if (row.health == 'yellow') {
                return {
                    classes: classes[3]
                };
            }
            return {
                classes: classes[0]
            };
        }
    },
    http: {
        constant: {
            SERVER_URL_PREFIX: "http://localhost:8080",
            SUCCESS_CODE: "200"

        },
        post: function ({url, data = '', success}) {
            var _this = this
            $.ajax({
                type: 'POST',
                url: url,
                data: data,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (res) {
                    var code = res.code;
                    if (_this.constant.SUCCESS_CODE == code) {
                        success(res.data)
                    } else {
                        cssTools.notifyMsg("操作失败，" + res.message)

                    }
                },
                error: function (err) {
                    var errMsg = cssTools.formater.jsonToStr(err)
                    cssTools.notifyMsg("系统内部错误：" + errMsg)
                    console.error('err=', err)
                }
            })
        },
        get: function ({url, data = '', success}) {
            console.log('requestUrl:' + this.constant.SERVER_URL_PREFIX + url);
            var _this = this
            console.log('-----------');
            console.log(this);
            $.ajax({
                type: 'GET',
                url: this.constant.SERVER_URL_PREFIX + url,
                data: data,
                success: function (res) {
                    var code = res.code;
                    if (_this.constant.SUCCESS_CODE == code) {
                        success(res.data)
                    } else {
                        alert("操作失败，" + res.message)
                    }
                },
                error: function (err) {
                    console.log('err=', err)
                }
            })
        },
        del: function ({url, data = '', success}) {
            var _this = this
            $.ajax({
                type: 'delete',
                url: url,
                data: data,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (res) {
                    var code = res.code;
                    if (_this.constant.SUCCESS_CODE == code) {
                        success(res.data)
                    } else {
                        cssTools.notifyMsg("操作失败，" + res.message)
                    }
                },
                error: function (err) {
                    cssTools.notifyMsg("操作异常，" + err)
                    console.error('操作异常：', err)
                }
            })
        }
    },
    /**
     * 显示模态框
     * @param id 模态框ID
     */
    modal:
        {
            showModal: function (id) {
                $(`#${id}`).modal('show')
            },
            hideModal: function (id) {
                $(`#${id}`).modal('hide')
            }
        }
    , /**
     * notify类型
     */
    notifyType: {
        success: 'success',
        info: 'info',
        warning: 'warning',
        danger: 'danger'
    },
    /**
     * 全局提示框
     *
     * @param {*}
     *            msg 提示信息
     * @param {*}
     *            type 提示类型
     */
    notifyMsg: function (msg, type = this.notifyType.success) {
        let iconVal, typeVal;
        let [success_icon, info_icon, warning_icon, danger_icon] = [
            'glyphicon glyphicon-ok-sign',
            'glyphicon glyphicon-info-sign',
            'glyphicon glyphicon-warning-sign',
            'glyphicon glyphicon-remove-sign'
        ]
        switch (type) {
            case this.notifyType.success:
                [iconVal, typeVal] = [success_icon, this.notifyType.success]
                break;
            case this.notifyType.info:
                [iconVal, typeVal] = [info_icon, this.notifyType.info]
                break;
            case this.notifyType.warning:
                [iconVal, typeVal] = [warning_icon, this.notifyType.warning]
                break;
            case this.notifyType.danger:
                [iconVal, typeVal] = [danger_icon, this.notifyType.danger]
                break;
            default:
                [iconVal, typeVal] = [success_icon, this.notifyType.success]
                break;
        }
        return $.notify(
            {
                // options
                icon: iconVal,
                title: ' 系统信息：',
                message: msg,
                target: '_blank'
            },
            {
                // settings
                element: 'body',
                position: null,
                type: typeVal,
                allow_dismiss: true,
                newest_on_top: true,
                showProgressbar: false,
                placement: {
                    from: "top",
                    align: "right"
                },
                offset: 20,
                spacing: 10,
                z_index: 999999,
                delay: 3000,
                timer: 1000,
                url_target: '_blank'
            }
        )
    },
    /**
     *  格式化json，显示到HTML
     */
    formater: {
        jsonToStr: function (data) {
            console.log('bengin formateJson');
            console.log(data);
            if (typeof data == 'string') {
                data = JSON.parse(data);
            }
            return JSON.stringify(data, null, "\t")
        }
    }
}