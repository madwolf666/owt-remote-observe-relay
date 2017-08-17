$(function () {
    //$('#set-table-name').change(function(){alert($('[name=set-table-name]').val());});
    $('#set-table-name').change(function(){
        //alert($('[name=set-table-name] option:selected').val());
        make_log_analyze_list_mnt_first($('[name=set-table-name] option:selected').val());
    });
});

//セッション変数設定
var g_IsSet_Session = false;
function set_session_value_mnt_log_analyze(h_table){
    //alert(h_table);
    var a_data = {
            'Mnt_Log_Analyze_Table': h_table
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
function set_session_value_mnt_log_analyze_pageNo(h_pageNo){
    var a_data = {
            'Mnt_Log_Analyze_pageNo': h_pageNo
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
function make_log_analyze_select_mnt(){
    $("#new-mnt").hide();
    //$("#reset-mnt").hide();
    $("#entry-mnt").hide();
    $("#back-mnt").hide();
    $.ajax({
        url: m_parentURL + "make_log_analyze_select_mnt.jsp",
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
function do_make_log_analyze_list_mnt_first(){
    if (g_IsSet_Session == false) {
        //doSomething();
        setTimeout(function(){do_make_log_analyze_list_mnt_first()}, 1000);
    }else{
        g_IsSet_Session = false;
        //alert("make_log_table_list_mnt_first--->" + g_IsSet_Session)
        //alert(h_table);
        //make_pager(2,h_pageNo);   //[2016.02.15]
        make_log_analyze_list_mnt(1);
    }
}

function make_log_analyze_list_mnt_first(h_table){
    //alert(h_table);
    if (h_table != ""){
        set_session_value_mnt_log_analyze(h_table);
        //alert("make_log_table_list_mnt_first--->" + g_IsSet_Session)
        do_make_log_analyze_list_mnt_first();
    }else{
        $("#my-pager").empty();
        $("#my-list").empty();
        $("#new-mnt").hide();
        //$("#reset-mnt").hide();
        $("#entry-mnt").hide();
        $("#back-mnt").hide();
    }
}

//一覧表示
function make_log_analyze_list_mnt(h_pageNo){
    //alert(m_parentURL);
    $.ajax({
        url: m_parentURL + "make_log_analyze_list_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo
        },
        success: function(data, dataType){
            //alert(data);
            make_pager(8,h_pageNo); //[2017.07.27]
            $("#my-list").empty().append(data);
            //alert($("#reset-mnt"));
            //$("#reset-mnt").hide();
            $("#new-mnt").show();
            $("#entry-mnt").hide();
            $("#back-mnt").hide();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
    });	
}

//テーブル情報編集
function make_log_analyze_edit_mnt(h_act, h_idx){
    //alert("make_log_analyze_edit_mnt");
    $.ajax({
        url: m_parentURL + "make_log_analyze_edit_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'ACT': h_act,
            'IDX': h_idx,
            'DB': '1'
        },
        success: function(data, dataType){
            $("#my-pager").empty().append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            $("#my-list").empty().append(data);
            $("#new-mnt").hide();
            //$("#reset-mnt").show();
            $("#entry-mnt").show();
            $("#back-mnt").show();
            //alert($("#my-list").text());
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown.message);
        },
       complete: function (data) {
       }
   });	
}
