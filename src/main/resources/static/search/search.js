/****定义初始化方法*****/
function init() {

}

/***
 * 获取测试
 */

function search() {
    $('#tableList').bootstrapTable('refresh');

}

/***
 * 获取测试问题
 */
function getQuestions(){ 
    $('#questionList').bootstrapTable('refresh');

}

/***
 * 测试问题列表
 */
$('#questionList').bootstrapTable({
    method: 'get',
    url: "/es/exam/questions", //请求路径
    cache: false,
    striped: true, //是否显示行间隔色
    pageNumber: 1, //初始化加载第一页
    pagination: true, //是否分
    sidePagination: 'client', //server:服务器端分页|client：前端分页
    pageSize: 10, //单页记录数
    pageList: [5, 10, 20, 30], //可选择单页记录数
    showRefresh: false, //刷新按钮
    queryParams: function (params) { //上传服务器的参数
        return { //如果是在服务器端实现分页，limit、offset这两个参数是必须的
            id: $('#id').val(),
            authorization: $('#authorization').val()

        }
    },
    columns: [
        {
            title: '类型',
            field: 'type'
        }, {
            title: '内容',
            field: 'content',
            formatter: function (value, row, index) {
                return value;
            },
        }, {
            title: '答案',
            field: 'answer',
        }]
})


/***
 * 搜索结果列表
 */
$('#tableList').bootstrapTable({
    method: 'get',
    url: "/es/exam/", //请求路径
    cache: false,
    striped: true, //是否显示行间隔色
    pageNumber: 1, //初始化加载第一页
    pagination: true, //是否分
    sidePagination: 'client', //server:服务器端分页|client：前端分页
    pageSize: 10, //单页记录数
    pageList: [5, 10, 20, 30], //可选择单页记录数
    showRefresh: false, //刷新按钮
    queryParams: function (params) { //上传服务器的参数
        return { //如果是在服务器端实现分页，limit、offset这两个参数是必须的
            title: $('#title').val(),
            smart:$("#smart").is(':checked')
        }
    },
    columns: [
        {
            title: '类型',
            field: 'type'
        }, {
            title: '内容',
            field: 'content',
            formatter: function (value, row, index) {
                return value;
            },
        }, {
            title: '答案',
            field: 'answer',
        }]
})