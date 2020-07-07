var titles = ['标题', '内容', '用户', '类型']
$(document).ready(function () {
    var table = $("#books").DataTable({ //对表格控件进行初始化
        ordering: false,  // 关闭排序
        searching: true,  // 关闭插件自带的搜索，我们会自定义搜索
        serverSide: true,  //服务器模式
        Processing: true,  //是否显示“处理中...”
        scrollX: true,  //水平方向的滚动条
        autoWidth : false,  // 自动宽度
        lengthMenu: [10, 25, 50, 100],  // 分页器的页数
        ajax: {
            //指定数据源
            url: "/listloginfo",
            type: "POST",
            // 这里向后端传递查询参数
            data: function (d) {
                d.id = $("#id").val().trim();
                d.title = $("#title").val().trim();
                d.content = $("#content").val().trim();
                d.user = $("#user").val().trim();
                d.studyType = $("#studyType").val().trim();
            }
        },
        // 与<table>标签中的<th>标签内的字段对应
        columns: [
            {data: "id","visible": false
        } ,{
            data: "title"
        }, {
            data: "content"
        }, {
            data: "user"
        }, {
            data: "studyType"
        }],
        "pagingType": "full_numbers",
                "processing": true,
                "bSort": true,
                "language": {
                    "sProcessing": "处理中...",
                    "sLengthMenu": "显示 _MENU_ 项结果",
                    "sZeroRecords": "没有匹配结果",
                    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                    "sInfoPostFix": "",
                    "sSearch": "搜索关键字:",
                    "sUrl": "",
                    "sEmptyTable": "表中数据为空",
                    "sLoadingRecords": "载入中...",
                    "sInfoThousands": ",",
                    "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": "上页",
                        "sNext": "下页",
                        "sLast": "末页"
                    },
                    "oAria": {
                        "sSortAscending": ": 以升序排列此列",
                        "sSortDescending": ": 以降序排列此列"
                    }
                },
                "columnDefs": [{
                    "searchable": false,
                    "orderable": true,
                    "targets": 0
                }],
                "order": [[1, 'asc']]
            });
                table.on('order.dt search.dt', function() {
                table.column(0, {
                    search: 'applied',
                    order: 'applied'
                }).nodes().each(function(cell, i) {
                    cell.innerHTML = i + 1;
                });
            }).draw();



    $('#books tbody').on('click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });

    $("#cancelAdd").on('click', function() {
        console.log('cancelAdd');
        $("#addBookModal").find('.form-control').val('')
    })

    $("#addBooksInfo").on('click', function() {
        if ($("#addBookModal").find('.form-control').val() !== '') {
            var title = $("#title").val()
            var content = $("#content").val()
            var user = $("#user").val()
            var studyType = $("#studyType").val()
            var addBookInfos = [].concat(title, content, user, studyType);
            for (var i = 0; i < addBookInfos.length; i++) {
                if (addBookInfos[i] === '') {
                    alert(titles[i] + '内容不能为空');
                    return;
                }
            }
            var obj = {
                "title":title,
                "content":content,
                "user":user,
                "study_type":studyType
            }
            $.ajax({
                "dataType" : 'json',
                "contentType": "application/json; charset=utf-8",
                "type" : "post",
                "url" : "/addSearchGrid",
                "data" :JSON.stringify(obj),
                "success" : function(res){
                    if (res.state == "1"){
                        alert("添加成功");
                        $('#books').DataTable().ajax.reload();
                    }else{
                        alert("添加失败");
                    }
                }
            })
            // table.row.add([title, content, user, studyType]).draw();

        }else {
        alert('请输入内容')
        }
    })
    //$("#addBooksInfo").click();

    $("#btn_add").click(function(){
        console.log('add');
        $("#addBook").modal()
    });
    $('table tbody').on('dblclick','tr', function() {
        $("#editBookInfo").modal()
        var rowData = table.rows('.selected').data()[0];
        var editId = $("#editBookModal").find('#editId');
        var inputs = $("#editBookModal").find('.form-control');
        editId.val(rowData.id);
        $(inputs[0]).val(rowData.title);
        $(inputs[1]).val(rowData.content);
        $(inputs[2]).val(rowData.user);
        $(inputs[3]).val(rowData.studyType);
    });
    $('#btn_edit').click(function () {
        console.log('edit');
        if (table.rows('.selected').data().length) {
            $("#editBookInfo").modal()
            var rowData = table.rows('.selected').data()[0];
            var editId = $("#editBookModal").find('#editId');
            var inputs = $("#editBookModal").find('.form-control');
            editId.val(rowData.id);
            $(inputs[0]).val(rowData.title);
            $(inputs[1]).val(rowData.content);
            $(inputs[2]).val(rowData.user);
            $(inputs[3]).val(rowData.studyType);

        } else {
            alert('请选择项目');
        }
    });

    $("#saveEdit").click(function() {
        var editId = $("#editId").val()
        var editTitle = $("#editTitle").val()
        var editContent = $("#editContent").val()
        var editUser = $("#editUser").val()
        var editStudyType = $("#editStudyType").val()
        var newRowData = [].concat(editTitle, editContent, editUser, editStudyType);
        //var tds = Array.prototype.slice.call($('.selected td'))
        for (var i = 0; i < newRowData.length; i++) {
            if (newRowData[i] !== '') {
                //tds[i + 1].innerHTML = newRowData[i];
            } else {
                alert(titles[i] + '内容不能为空');
                return;
            }
        }
        var obj = {
            "title":editTitle,
            "content":editContent,
            "user":editUser,
            "study_type":editStudyType,
            "id":editId
        }
        $.ajax({
            "dataType" : 'json',
            "contentType": "application/json; charset=utf-8",
            "type" : "post",
            "url" : "/saveSearchGrid",
            "data" :JSON.stringify(obj),
            "success" : function(res){
                if (res.state == "1"){
                    alert("修改成功");
                    $('#books').DataTable().ajax.reload();
                }else if (res.state == "2") {
                    alert("内容出现特殊字符,修改失败");
                }else {
                    alert("修改失败");
                }
            }
        })

    })

    $("#cancelEdit").click(function() {
        console.log('cancelAdd');
        $("#editBookModal").find('input').val('')
    })

    $('#btn_delete').click(function () {
        if (table.rows('.selected').data().length) {
            $("#deleteBook").modal()
        } else {
            alert('请选择项目');
        }
    });

    $('#delete').click(function () {
        var data = table.row('.selected').data();
        var obj = {
            "id":data.id,
        }
        $.ajax({
            "dataType" : 'json',
            "contentType": "application/json; charset=utf-8",
            "type" : "post",
            "url" : "/deleteSearchGrid",
            "data" :JSON.stringify(obj),
            "success" : function(res){
                if (res.state == "1"){
                    alert("删除成功");
                    $('#books').DataTable().ajax.reload();
                }else{
                    alert("删除失败");
                }
            }
        })
    });
});