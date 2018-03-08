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
    g_IsSet_Session == false;
    
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
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
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
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//リモートDBテーブル名取得
function make_table_select_mnt(){
    $("#delete-mnt").hide();
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
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//一覧表示（最初）
var g_IsSess = 0;
function do_make_table_list_mnt_first(){
    if (g_IsSet_Session == false) {
        //doSomething();
        setTimeout(function(){do_make_table_list_mnt_first()}, 1000);
    }else{
        g_IsSet_Session = false;
        g_IsSess++;
        make_table_find_condition();
        make_table_list_mnt(1);
    }
    /*if (g_IsSess<2){
        //alert(g_IsSess);
        set_session_value_mnt_find();
    }else{
        //alert("make_table_list_mnt_first--->" + g_IsSet_Session)
        //alert(h_table);
        //make_pager(2,h_pageNo);   //[2016.02.15]
        make_table_find_condition();
        make_table_list_mnt(1);
    }*/
}

function make_table_list_mnt_first(h_table){
    //alert(h_table);
    g_IsSess = 0;
    set_session_value_mnt_table(h_table);
    do_make_table_list_mnt_first();
    /*if (h_table != ""){
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
        //make_copyright();
    }*/
}

//一覧表示
function make_table_list_mnt(h_pageNo){
    m_ProgressMsg('処理中...<br><img src="./img/upload.gif" /> ');
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
            $("#delete-mnt").hide();
            $("#new-mnt").show();
            //$("#reset-mnt").hide();
            $("#confirm-mnt").hide();
            $("#entry-mnt").hide();
            $("#back-mnt").hide();
            $("#cancel-mnt").hide();
            $("#list-mnt").hide();
            if ($('[name=set-table-name] option:selected').val() != ''){
            }else{
                $("#my-pager").empty();
                $("#delete-mnt").hide();
                $("#new-mnt").hide();
                $("#confirm-mnt").hide();
                $("#entry-mnt").hide();
                $("#back-mnt").hide();
                $("#cancel-mnt").hide();
                $("#list-mnt").hide();
            }
            scrollTo(0,0);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
            $.unblockUI();
       }
    });	
}

//テーブル情報編集
function make_table_edit_mnt(h_act, h_idx){
    m_ProgressMsg('処理中...<br><img src="./img/upload.gif" /> ');
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
            if (h_act == "n"){
                $("#delete-mnt").hide();
            }else{
                $("#delete-mnt").show();
            }
            $("#new-mnt").hide();
            //$("#reset-mnt").show();
            $("#confirm-mnt").show();
            $("#entry-mnt").hide();
            $("#back-mnt").hide();
            $("#cancel-mnt").hide();
            $("#list-mnt").show();
            //alert($("#my-list").text());
            //window.location.hash = "my-list-top";
            scrollTo(0,0);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
            $.unblockUI();
       }
   });	
}

//テーブル情報編集内容のリセット(未使用)
function reset_table_edit_mnt(){
    var a_act = $("#txt_act").val();
    var a_idx = $("#edit_idx").val();
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

//[2018.03.08]
function windowKeyboardEvent(){
  if(window.event) return window.event.keyCode;
  var caller = arguments.callee.caller;
  while(caller){
    var ob = caller.arguments[0];
    if(ob && ob.constructor == KeyboardEvent) return ob.which;
    caller = caller.caller;
  }
  return null;
}

//RPT
function show_equipmenttype_name(h_id, h_name){
    //alert('show_equipmenttype_name');
    //[2018.03.08]
    var e = windowKeyboardEvent();
    if (e == 13){
    //if (window.event.keyCode == 13){
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
                        alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
                    },
                   complete: function (data) {
                   }
               });
           }
       }
    }
}

//LTIC・TN拠点設定
//ユーザ機器登録
//機器コード設定
function set_irms_plural(h_mode, h_is_edit, h_user_code){
    $.ajax({
        url: m_parentURL + "make_table_plural_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'mode': h_mode,
            'is_edit': h_is_edit,
            'user_code': h_user_code
        },
        success: function(data, dataType){
            //alert(data);
            a_result = data.trim();
            $("#show-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
    });
    
    var $popup = $("#popup1");

    //alert($popup);
    // ポップアップの幅と高さからmarginを計算する
    var mT = ($popup.outerHeight() / 2) * (-1) + 'px';
    var mL = ($popup.outerWidth() / 2) * (-1) + 'px';
    //alert(mT + "," + mL);
    // marginを設定して表示
    $('.popup').hide();
    $popup.css({
            'margin-top': mT,
            'margin-left': mL
    }).show();
    return false;
}

function select_plural_list(h_mode, h_is_edit, h_user_code, h_seq){
    $.ajax({
        url: m_parentURL + "make_table_plural_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'mode': h_mode,
            'is_edit': h_is_edit,
            'user_code': h_user_code,
            'seq': h_seq
        },
        success: function(data, dataType){
            a_result = data.trim();
            $("#show-list").empty().append(data);
            $("#select-plural-seq").val(h_seq);
            $("#delete-plural-mnt").show();
            change_ss9100flag();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
    });
    
    var $popup = $("#popup1");

    //alert($popup);
    // ポップアップの幅と高さからmarginを計算する
    var mT = ($popup.outerHeight() / 2) * (-1) + 'px';
    var mL = ($popup.outerWidth() / 2) * (-1) + 'px';
    //alert(mT + "," + mL);
    // marginを設定して表示
    $('.popup').hide();
    $popup.css({
            'margin-top': mT,
            'margin-left': mL
    }).show();
    return false;
}

function delete_plural_mnt(h_mode, h_is_edit, h_user_code, h_idx, h_opt1){
    //alert(h_opt1);
    var a_seq = $("#select-plural-seq").val();

    if (!confirm("削除します。よろしいですか？")){
        return false;
    }

    m_ProgressMsg('処理中...<br><img src="./img/upload.gif" /> ');

    $.ajax({
        url: m_parentURL + "delete_plural_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'mode': h_mode,
            'is_edit': h_is_edit,
            'user_code': h_user_code,
            'seq': a_seq,
            'IDX': h_idx,
            'opt1': h_opt1
        },
        success: function(data, dataType){
            a_result = data.trim();
            set_irms_plural(h_mode, h_is_edit, h_user_code);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
            $.unblockUI();
       }
    });
}

function make_table_find_condition(){
    /*
    if (h_table == ""){
        return;
    }
    */
    $.ajax({
        url: m_parentURL + "make_table_find_condition.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            //'table': h_table
        },
        success: function(data, dataType){
            a_result = data.trim();
            $("#my-find-condition").empty().append(a_result);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
    });
}

function set_session_value_mnt_find(){
    var a_table_info = $('[name=set-table-name] option:selected').val();
    var a_table = a_table_info.split("\t");
    var a_session_val = "";
    
    if ((a_table[0] == "pbxremotecustomer") || (a_table[0] == "pbxremotecustomer")){
        a_session_val += $('#f_remotesetid').val();
        
    }
    //alert("a_session_val=" + a_session_val);
    var a_data = {
            'Mnt_Find_Condition': a_session_val
    };
    //alert(m_parentURL);
    g_IsSet_Session == false;
    
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
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

function do_find_table_mnt_first(){
    if (g_IsSet_Session == false) {
        //doSomething();
        setTimeout(function(){do_find_table_mnt_first()}, 1000);
    }else{
        g_IsSet_Session = false;
        make_table_list_mnt(1);
    }
}

function find_table_mnt_first(){
    set_session_value_mnt_find();
    do_find_table_mnt_first();
}

function delete_table_mnt(){
    if (!confirm("削除します。よろしいですか？")){
         return false;
    }
    
    m_ProgressMsg('処理中...<br><img src="./img/upload.gif" /> ');

    var a_idx = $('#edit_idx').val();
    $.ajax({
        url: m_parentURL + "delete_table_mnt.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'IDX': a_idx
        },
        success: function(data, dataType){
            var a_result = data.trim();
            if (a_result == ""){
                alert("削除しました。");
                make_table_list_mnt(1);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
            $.unblockUI();
       }
   });	
}

function change_ss9100flag(){
    //alert('');
    var a_val = $('[name=p_ss9100flag] option:selected').val();
    //alert(a_val);
    if (a_val == '1'){
        //alert('SS9100');");
        $('#p_cabno').val('-99');
        $('#p_cabno').attr('readonly', true);
    }else{
        //alert('DISCOVERY');
        $('#p_cabno').attr('readonly', false);
    }
}
