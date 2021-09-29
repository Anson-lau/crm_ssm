<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<%--    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>--%>

	<link rel="stylesheet" href="jquery/jquery-1.11.1-min.js">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){


		// 時間插件漢化
		$.fn.datetimepicker.dates['zh-CN'] = {
			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			today: "今天",
			suffix: [],
			meridiem: ["上午", "下午"]
		}

		// 時間插件
		$(".time").datetimepicker({
			minView: "month",
			language: 'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			today: true,
			pickerPosition: "bottom-left"
		});

		// 顯示結果集
		pageList(1, 3);

		// 點擊創建按鈕
		$("#openBtn").click(function (){

			// 打開添加頁面前獲取選框的所有者
			$.ajax({
				url : "workbench/activity/getUserList.do",
				dataType : "json",
				type : "get",
				success : function (data){

					var html = "<option></option>";

					$.each(data, function (i, n){
						html += '<option value="'+n.id+'">'+n.name+'</option>';
					})

					$("#create-owner").html(html);

					var id = "${user.id}";

					$("#create-owner").val(id);

					$("#createActivityModal").modal("show");
				}

			})

		})

		// 添加用戶
		$("#addBtn").click(function (){

			if (check()) {
				// 提交表單
				document.forms[0].submit();

				// 成功提交后，表單清空
				$("#activityAddForm")[0].reset();

				// 刷新結果集
				pageList(1, 3);
			}else{
				alert("表單不能爲空");
			}


		})

        // 搜索用戶
        $("#search").click(function (){

            // 將搜索框裏的關鍵詞保存至隱藏域中
            $("#hidden_name").val($.trim($("#search_name").val()));
            $("#hidden_owner").val($.trim($("#search_owner").val()));
            $("#hidden_startDate").val($.trim($("#search_startDate").val()));
            $("#hidden_endDate").val($.trim($("#search_endDate").val()));

            // 點擊搜索按鈕對搜索結果刷新
            pageList(1, 3);

        })


        // 全選
        // 點擊全選按鈕，子按鈕變爲已選
        $("#selection").click(function (){
            $("input[name=selection]").prop("checked", this.checked);
        })

        // 點擊所有子按鈕，全選按鈕變爲已選
        $("#activityTbody").on("click", $("input[name=selection]"), function (){
            $("#selection").prop("checked", $("input[name=selection]").length == $("input[name=selection]:checked").length)
        })

        // 打開編輯框，獲取編輯對象
        $("#editBtn").click(function (){

        	// 已選擇的記錄
        	var $selected = $("input[name=selection]:checked");

        	// 判斷選擇修改的記錄條數
        	if($selected.length > 1){
        		alert("最多可以選擇一條記錄進行修改");
			}else if($selected.length == 0){
        		alert("請選擇需要修改的記錄");
			}else if ($selected.length == 1){

        		var id = $selected.val();

				$.ajax({
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						"id" : id
					},
					type : "get",
					dataType : "json",
					success : function (data){
						var html = '<option></option>';

						// 獲取所有者
						$.each(data.uList, function (i ,n){
							html += '<option value="'+n.id+'">'+n.name+'</option>';
						})
						$("#edit-owner").html(html);


						$("#edit-id").val(data.a.id);
						$("#edit-owner").val(data.a.owner);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);


						$("#editActivityModal").modal("show");
					}
				})
			}





        })

		// 修改更新編輯對象
		$("#updateBtn").click(function (){
			$.ajax({
				url : "workbench/activity/updateActivity.do",
				data : {
					"id" : $("#edit-id").val(),
					"owner" : $("#edit-owner").val(),
					"name" : $("#edit-name").val(),
					"startDate" : $("#edit-startDate").val(),
					"endDate" : $("#edit-endDate").val(),
					"cost" : $("#edit-cost").val(),
					"description" : $("#edit-description").val()
				},
				type : "post",
				dataType : "json",
				success : function (data){

					if(data){

                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage'),
                            $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						$("#editActivityModal").modal("hide");
					}else{
						alert("更新信息失敗");
					}
				}
			})
		})


        // 選擇刪除的對象
        $("#deleteBtn").click(function (){

            var $selectedId = $("input[name=selection]:checked");

            if ($selectedId.length == 0){
                alert("請選擇至少一條對象進行刪除")
            }else{

                var param = "";
                for (var i = 0; i < $selectedId.length; i++){

                    param += "id=" + $($selectedId[i]).val();

                    if (i < $selectedId.length-1){
                        param += "&";
                    }
                }


                $.ajax({
                    url : "workbench/activity/deleteActivity.do",
                    data : param,
                    type : "post",
                    dataType : "json",
                    success : function (data){

                        if (data){
                            pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        }else{
                            alert("刪除對象失敗");
                        }

                    }
                })

            }



        })




		
		
	});

	// 表單非空校驗
	function check(){
		if (($("#create-name").val()
				|| $("#create-startDate").val()
				|| $("#create-endDate").val()
				|| $("#create-cost").val()
				|| $("#create-describe").val()) == "") {
			return false;
		}

		return true;
	}


	// 刷新列表
    function pageList(pageNo, pageSize){
        $("#selection").prop("checked", false);

	    // 搜索框關鍵詞為隱藏域的值
	    $("#search_name").val($.trim($("#hidden_name").val()));
        $("#search_owner").val($.trim($("#hidden_owner").val()));
        $("#search_startDate").val($.trim($("#hidden_startDate").val()));
        $("#search_endDate").val($.trim($("#hidden_endDate").val()));


	    $.ajax({
            url : "workbench/activity/pageList.do",
            data : {
                "name" : $.trim($("#search_name").val()),
                "owner" : $.trim($("#search_owner").val()),
                "startDate" : $.trim($("#search_startDate").val()),
                "endDate" : $.trim($("#search_endDate").val()),
                "pageNo" : pageNo,
                "pageSize" : pageSize
            },
            type : "get",
			dataType: "json",
            success : function(data){

                var html = "";

                // 刷新搜索結果
                $.each(data.dataList, function (i, n){

                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="selection" value="'+n.id+'"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })
                $("#activityTbody").html(html);

                // 總頁數
                var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

                // 漢化
                var rsc_bs_pag = {
                    go_to_page_title: 'Go to page',
                    rows_per_page_title: 'Rows per page',
                    current_page_label: 'Page',
                    current_page_abbr_label: 'p.',
                    total_pages_label: 'of',
                    total_pages_abbr_label: '/',
                    total_rows_label: 'of',
                    rows_info_records: 'records',
                    go_top_text: '首页',
                    go_prev_text: '上一页',
                    go_next_text: '下一页',
                    go_last_text: '末页'
                };

                // 頁碼插件
                $("#activityPage").bs_pagination({
                    currentPage: pageNo, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                })
            }

        })


    }













	
</script>
</head>
<body>

    <%-- 隱藏搜索關鍵詞--%>
    <input type="hidden" id="hidden_name">
    <input type="hidden" id="hidden_owner">
    <input type="hidden" id="hidden_startDate">
    <input type="hidden" id="hidden_endDate">


	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityAddForm" action="workbench/activity/save.do" method="post">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner" name="owner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name" name="name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" name="startDate">
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" name="endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost" name="cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description" name="description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="addBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" value="">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" value="">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search_name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search_owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search_startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search_endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="search" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="openBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" name="allSelection" id="selection" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityTbody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>