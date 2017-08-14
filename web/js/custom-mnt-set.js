$(function () {
    //$('#set-table-name').change(function(){alert($('[name=set-table-name]').val());});
    $('#set-table-name').change(function(){
        //alert($('#set-table-name').val());
        //alert($('[name=set-table-name] option:selected').val());
        make_table_list_mnt_first($('[name=set-table-name] option:selected').val());
    });
});

//セッション変数設定
var g_IsSet_Session = false;
function set_session_value_mnt_table(h_table){
    //alert(h_table);
    var a_data = {
            'Mnt_Table': h_table
    };
    //alert(m_parentURL);
    $.ajax({
        url: m_parentURL + "set_session_value.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data: a_data,
        success: function(data, dataType){
            g_IsSet_Session = true;
            //alert("set_session_value_mnt--->" + g_IsSet_Session);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
   });	
}

//セッション変数設定
function set_session_value_mnt_pageNo(h_pageNo){
    var a_data = {
            'Mnt_pageNo': h_pageNo
    };
    //alert(m_parentURL);
    $.ajax({
        url: m_parentURL + "set_session_value.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data: a_data,
        success: function(data, dataType){
            //g_IsSet_Session = true;
            //alert("set_session_value_mnt--->" + g_IsSet_Session);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
   });	
}

//リモートDBテーブル名取得
function make_table_select_mnt(){
    $("#new-mnt").hide();
    //$("#reset-mnt").hide();
    $("#confirm-mnt").hide();
    $("#entry-mnt").hide();
    $("#back-mnt").hide();
    $("#cancel-mnt").hide();
    $("#list-mnt").hide();
    $.ajax({
        url: m_parentURL + "make_table_select_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
        },
        success: function(data, dataType){
            $("#my-table-select").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
   });	
}

//一覧表示（最初）
function do_make_table_list_mnt_first(){
    if (g_IsSet_Session == false) {
        //doSomething();
        setTimeout(function(){do_make_table_list_mnt_first()}, 1000);
    }else{
        g_IsSet_Session = false;
        //alert("make_table_list_mnt_first--->" + g_IsSet_Session)
        //alert(h_table);
        //make_pager(2,h_pageNo);   //[2016.02.15]
        make_table_list_mnt(1);
    }
}

function make_table_list_mnt_first(h_table){
    //alert(h_table);
    if (h_table != ""){
        set_session_value_mnt_table(h_table);
        //alert("make_table_list_mnt_first--->" + g_IsSet_Session)
        do_make_table_list_mnt_first();
    }else{
        $("#my-pager").empty();
        $("#my-list").empty();
        $("#new-mnt").hide();
        //$("#reset-mnt").hide();
        $("#confirm-mnt").hide();
        $("#entry-mnt").hide();
        $("#back-mnt").hide();
        $("#cancel-mnt").hide();
        $("#list-mnt").hide();
    }
}

//一覧表示
function make_table_list_mnt(h_pageNo){
    //alert(m_parentURL);
    $.ajax({
        url: m_parentURL + "make_table_list_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo
        },
        success: function(data, dataType){
            make_pager(7,h_pageNo); //[2017.07.27]
            $("#my-list").empty().append(data);
            $("#new-mnt").show();
            //$("#reset-mnt").hide();
            $("#confirm-mnt").hide();
            $("#entry-mnt").hide();
            $("#back-mnt").hide();
            $("#cancel-mnt").hide();
            $("#list-mnt").hide();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
    });	
}

//テーブル情報編集
function make_table_edit_mnt(h_act, h_idx){
    //alert("make_table_edit_mnt");
    $.ajax({
        url: m_parentURL + "make_table_edit_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'ACT': h_act,
            'IDX': h_idx,
            'DB': '1',
        },
        success: function(data, dataType){
            $("#my-pager").empty().append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            $("#my-list").empty().append(data);
            $("#new-mnt").hide();
            //$("#reset-mnt").show();
            $("#confirm-mnt").show();
            $("#entry-mnt").hide();
            $("#back-mnt").hide();
            $("#cancel-mnt").hide();
            $("#list-mnt").show();
            //alert($("#my-list").text());
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
   });	
}

//テーブル情報編集内容のリセット
function reset_table_edit_mnt(){
    var a_act = $("#txt_act").val();
    var a_idx = $("#txt_idx").val();
    //alert(a_act);
    //alert(a_idx);
    if (confirm("入力内容をリセットします。よろしいですか？")){
        make_table_edit_mnt(a_act, a_idx);
    }
}

//数値とバックスペース・リターンのみを入力可能にする
function input_numOnly(){
  var a_m = String.fromCharCode(event.keyCode);
  if("0123456789\b\r".indexOf(a_m, 0) < 0) return false;
  //deleteキー
  /*
  if(event.keyCode != 46){
      return false;
  }
  */
  return true;
}

//RPT
function show_equipmenttype_name(h_id, h_name){
    //alert('show_equipmenttype_name');
    if (window.event.keyCode == 13){
        //alert("enter押下！");
        //alert($("#" + h_id).val());
        //enter押下
        var a_h_id = $("#" + h_id).val();
        if (a_h_id != null){
            if (a_h_id != ""){
                $.ajax({
                    url: m_parentURL + "get_equipmenttype_name.jsp",
                    type: 'POST',
                    dataType: "html",
                    async: false,
                    data:{
                        'id': a_h_id
                    },
                    success: function(data, dataType){
                        //alert(data);
                        $("#" + h_name).val(data.trim());
                        //$("#" + h_name).empty().append(data.trim());
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(errorThrown.message);
                    },
                   complete: function (data) {
                   }
               });
           }
       }
    }
}