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
$('#tableList').bootstrapTable({
    method: 'get',
    url: "/searchGridByEs", //请求路径//searchGridByEs
    contentType: "application/x-www-form-urlencoded",
    dataType: "json",
    pagination: true, //是否显示分页（*）
    striped: true,//隔行换色
    sidePagination: 'server',//分页方式
    pageNumber: 1,//初始化table时显示的页码
    pageSize: 10,//每页条目
    pageList: [10, 20, 50, 100],
    //queryParams: queryParams,
    locale: 'zh-CN',
    sortable: true,//排序
    showColumns: true,//是否显示 内容列下拉框
    clickToSelect: true,//点击选中checkbox
    singleSelect: true,//启用单行选中
    showExport: true,                     //是否显示导出
    exportDataType: "all",              //basic', 'all', 'selected'.
    queryParamsType: "", //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
    // 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
    queryParams: function queryParams(params) {   //设置查询参数
        var param = {
            pageNumber: params.pageNumber,
            pageSize: params.pageSize,
            search:$('#searchContent').val()
        };
        return param;

    },
    responseHandler: function (data) {
        return {
            "total": data.total,
            "rows": data.rows
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