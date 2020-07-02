var data = [['', '三体', '刘慈欣', '39.00', '重庆出版社']]
var titles = ['标题', '内容', '用户', '类型',]
$(function () {
    var table = $('#books').DataTable({
        data: data,
        "pagingType": "full_numbers",
        "processing": true,
        "serverSide": false,
        "bSort": true,
        "language": {
            "sProcessing": "处理中...",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
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
        $("#addBookModal").find('input').val('')
    })

    $("#addBooksInfo").on('click', function() {
        console.log('addBooksInfo');
        if (data.length) {
            if ($("#addBookModal").find('input').val() !== '') {
                var title = $("#title").val()
                var content = $("#content").val()
                var user = $("#user").val()
                var studyType = $("#studyType").val()
                var addBookInfos = [].concat(title, content, user, studyType);
                for (var i = 0; i < addBookInfos.length; i++) {
                    if (addBookInfos[i] === '') {
                        alert(titles[i] + '内容不能为空')
                    }
                }
                table.row.add(['', title, content, user, studyType]).draw();
                $("#addBookModal").find('input').val('')
            }
        } else {
            alert('请输入内容')
        }
    })
    $("#addBooksInfo").click();

    $("#btn_add").click(function(){
        console.log('add');
        $("#addBook").modal()
    });

    $('#btn_edit').click(function () {
        console.log('edit');
        if (table.rows('.selected').data().length) {
            $("#editBookInfo").modal()
            var rowData = table.rows('.selected').data()[0];
            var inputs = $("#editBookModal").find('.form-control')
            for (var i = 0; i < inputs.length; i++) {
                $(inputs[i]).val(rowData[i + 1])
            }
        } else {
            alert('请选择项目');
        }
    });

    $("#saveEdit").click(function() {
        var editTitle = $("#editTitle").val()
        var editContent = $("#editContent").val()
        var editUser = $("#editUser").val()
        var editStudyType = $("#editStudyType").val()
        var newRowData = [].concat(editTitle, editContent, editUser, editStudyType);
        var tds = Array.prototype.slice.call($('.selected td'))
        for (var i = 0; i < newRowData.length; i++) {
            if (newRowData[i] !== '') {
                tds[i + 1].innerHTML = newRowData[i];
            } else {
                alert(titles[i] + '内容不能为空')
            }
        }
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
        table.row('.selected').remove().draw(false);
    });

})