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
 * 搜索结果列表
 */
var TOTAL = 0;//总数
$('#tableList').bootstrapTable({
    method: 'get',
    url: "/searchGrid", //请求路径
    cache: false,
    striped: true, //是否显示行间隔色
    pageNumber: 1, //初始化加载第一页
    pagination: true, //是否分
    sidePagination: 'server', //server:服务器端分页|client：前端分页
    pageSize: 10, //单页记录数
    pageList: [5, 10, 20, 30], //可选择单页记录数
    showRefresh: false, //刷新按钮
    queryParams: function (params) { //上传服务器的参数
        return { //如果是在服务器端实现分页，limit、offset这两个参数是必须的
            search: $('#searchContent').val(),
            size: params.limit, // 每页显示数量
            page: (params.offset / params.limit) + 1, // 当前页码
            total: TOTAL,//回去总数保留起来，用于之后显示条数总数，由于后台只有在第
        }
    },
    responseHandler: function (res) {
        TOTAL = data.total;
        var rows = data.rows;
        return {
            "total": TOTAL,
            "rows": rows
        }
    },
    columns: [
        {
            title: '标题',
            field: 'title'
        }, {
            title: '内容',
            field: 'content',
            formatter: function (value, row, index) {
                return value;
            },
        }, {
            title: '创建人',
            field: 'user',
        }]
})