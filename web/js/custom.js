var m_parentURL = "//localhost:8080/owt-remote-observe-relay/";
//var m_parentURL = "http://localhost:8080/owt-remote-observe-relay/";
//
//------------------------------------------------------------------------------
//共通
//------------------------------------------------------------------------------
//QyeryStringを配列化
function getUrlVars() 
{ 
    var vars = [], hash; 
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&'); 
    for(var i = 0; i < hashes.length; i++) { 
        hash = hashes[i].split('='); 
        vars.push(hash[0]); 
        vars[hash[0]] = hash[1]; 
    } 
    return vars; 
}

//メニュー作成
function make_menu(){
    var a_queryStr = getUrlVars();
    var a_CaseNo = '';
    //alert(a_queryStr[0]);
    //alert(a_queryStr[a_queryStr[0]]);
    if (a_queryStr[0] == 'CASENO'){
        a_CaseNo = a_queryStr[a_queryStr[0]];
    }
    //alert("make_menu");
    //alert(m_parentURL + "check_auth.jsp");
    $.ajax({
        url: m_parentURL + "check_auth.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'CASENO': a_CaseNo
        },
        success: function(data, dataType){
            //alert(data);
            $("#ul-nav").empty().append(data);
            //$("#hpb-nav").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//メニュー遷移
function submit_menu(h_url){
    document.frmBody.submit();
}

function resize_tbl_list(){
   //out.print("alert($('#tbl_edit_warn_list').width());");
    //if (($(window).width()<1200) && ($('#tbl_list').width() < $('#hpb-container').width())){
    //alert($('#tbl_list').width());
    //alert($('#hpb-container').width());
    if ($('#tbl_list').width() > $('#hpb-container').width()){
        //alert('chappy1');
        $('#hpb-container').width($('#tbl_list').width()+100);
        $('#hpb-container').css('margin-left','4px');
        $('#hpb-container').css('padding-right','8px');
    }else{
        //alert($('#hpb-container').width());
        //alert($('#hpb-nav').width());
        if ($('#hpb-nav').width() < $('#hpb-container').width()){
            $('#hpb-container').css('width','99%');
        }else{
            $('#hpb-container').width($('#hpb-nav').width());
        }
    }
}

function check_IsRequired(h_obj,h_msg) {
    if ($(h_obj).val() == '') {
        show_Alert(h_obj,h_msg);
        return false;
    }
    return true;
}

function check_IsNumeric(h_obj, h_msg) {
    if ($(h_obj).val() != '') {
        if (jQuery.isNumeric($(h_obj).val()) == false) {
            show_Alert(h_obj, h_msg);
            return false;
        }
    }
    return true;
}

function show_Alert(h_obj, h_msg) {
    alert(h_msg);
    $(h_obj).css("background-color","#ffff00");
    $(h_obj).focus();
}

//ページング
function make_pager(h_kind,h_pageNo){
    //alert(h_kind + "," + h_pageNo);
    //1：SFケース詳細
    //2：リモート発報障害検索
    //3：リモート月報作成
    //4：作業・停電スケジュール
    //5：LOG_CHECK
    //6：ユーザ選択
    //7：リモートDB設定[2017.07.27]
    //8：保全[2017.08.02]
    //9：リスト表示[2017.08.09]
    
    //h_kind：3,4,5
    var a_data = {
            'Kind': h_kind,
            'PageNo': h_pageNo
    };

    switch (h_kind){
        case 2: //リモート発報障害
            a_data = {
                'Kind': h_kind,
                'PageNo': h_pageNo
            };
            break;
        case 6: //ユーザ選択
            a_data = {
                'Kind': h_kind,
                'PageNo': h_pageNo,
                'txt_UserName': $('#txt_UserName').val()
            };
            break;
        case 7: //リモートDB設定[2017.07.27]
            a_data = {
                'Kind': h_kind,
                'PageNo': h_pageNo,
            };
            break;
        case 8: //保全[2017.08.02]
            a_data = {
                'Kind': h_kind,
                'PageNo': h_pageNo,
            };
            break;
        case 9: //リスト表示[2017.08.09]
            a_data = {
                'Kind': h_kind,
                'PageNo': h_pageNo,
                'col_name': $('#show_col_name').val(),
            };
            break;
        default:
            break;
    }
    
    //alert(a_data);
    //alert(m_parentURL);

    $.ajax({
        url: m_parentURL + "make_pager.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data: a_data,
        success: function(data, dataType){
            var a_result = data.trim();
            if (h_kind != 6){   //[2016.03.03]bug-fixed.
                $("#my-pager").empty().append(a_result);
                //[2017.07.28][2017.08.02]
                if ((h_kind == 7) || (h_kind == 8)){
                    //alert(a_result.length);
                    //alert("'" + a_result + "'");
                    if (a_result == ''){
                        $("#new-mnt").hide();
                    }else{
                        $("#new-mnt").show();
                    }
                }
            }else{
                $("#my-pager-user").empty().append(data);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });

}

//セッション変数設定
function set_session_value(h_kind){
    //1：SFケース詳細
    //2：リモート発報障害検索
    //3：リモート月報作成
    //4：作業・停電スケジュール
    //5：LOG_CHECK
    var a_data = {
            'Kind': h_kind
    };

    switch (h_kind){
        case 2: //リモート発報障害
            a_data = {
                'Kind': h_kind,
                //'txt_UserCode': $('#txt_UserCode').val(), //[2016.06.09]USERCODEでのリレーションは現状ない
                'txt_SetNo': $('#txt_SetNo').val(),
                'txt_RecNo': $('#txt_RecNo').val(),
                'txt_UserName': $('#txt_UserName').val(),
                'entry_timeS': $('#entry_timeS').val(),
                'entry_timeE': $('#entry_timeE').val(),
                'txt_TroubleMsg': $('#txt_TroubleMsg').val(),
                'chk_TroubleKind_MJ': $('#chk_TroubleKind_MJ').prop('checked'),
                'chk_TroubleKind_MN': $('#chk_TroubleKind_MN').prop('checked'),
                'chk_TroubleKind_GN': $('#chk_TroubleKind_GN').prop('checked'),
                'chk_Contact': $('#chk_Contact').prop('checked')
            };
            break;
        case 6: //ユーザ選択
            break;
        default:
            break;
    }
    
    $.ajax({
        url: m_parentURL + "set_session_value.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data: a_data,
        success: function(data, dataType){
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//------------------------------------------------------------------------------
//SFケース詳細
//------------------------------------------------------------------------------
var g_ORDER_SF_REMOTE_TROUBLE_INITIATIONTIME = 'DESC';

function click_sf_remote_trouble_initiationtime(h_pageNo){
    if (g_ORDER_SF_REMOTE_TROUBLE_INITIATIONTIME == 'ASC'){
        g_ORDER_SF_REMOTE_TROUBLE_INITIATIONTIME = 'DESC';
    }else{
        g_ORDER_SF_REMOTE_TROUBLE_INITIATIONTIME = 'ASC';
    }
    find_sf_remote_trouble(h_pageNo);
}

function find_sf_remote_trouble(h_pageNo){
    //make_pager(2,h_pageNo);   //[2016.02.25]
    make_sf_remote_trouble_list(h_pageNo);
}

function make_sf_remote_trouble_list(h_pageNo){
    $.ajax({
        url: m_parentURL + "make_sf_remote_trouble_list.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'order_InitiationTime': g_ORDER_SF_REMOTE_TROUBLE_INITIATIONTIME
        },
        success: function(data, dataType){
            make_pager(1,h_pageNo); //[2016.02.25]
            $("#my-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });
}

//SFケース詳細情報
function make_sf_info(){
    $.ajax({
        url: m_parentURL + "make_sf_info.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
        },
        success: function(data, dataType){
            $("#my-sf").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//------------------------------------------------------------------------------
//リモート発報障害検索
//------------------------------------------------------------------------------
//障害情報一覧
var g_ORDER_REMOTE_TROUBLE_INITIATIONTIME = 'DESC';

function click_remote_trouble_initiationtime(h_pageNo){
    if (g_ORDER_REMOTE_TROUBLE_INITIATIONTIME == 'ASC'){
        g_ORDER_REMOTE_TROUBLE_INITIATIONTIME = 'DESC';
    }else{
        g_ORDER_REMOTE_TROUBLE_INITIATIONTIME = 'ASC';
    }
    find_remote_trouble(h_pageNo);
}

function find_remote_trouble_first(h_pageNo){
    var a_IsSetNo = false;
    var a_IsRecNo = false;
    if (($('#txt_SetNo').val() != null) && ($('#txt_SetNo').val() != '')){
        a_IsSetNo = true;
    }
    if (($('#txt_RecNo').val() != null) && ($('#txt_RecNo').val() != '')){
        a_IsRecNo = true;
    }
    if ((a_IsSetNo == true) && (a_IsRecNo == false)){
        show_Alert('#txt_RecNo','REC番号を指定して下さい。');
        return false;
    }
    if ((a_IsSetNo == false) && (a_IsRecNo == true)){
        show_Alert('#txt_SetNo','SET-NOを指定して下さい。');
        return false;
    }
    
    set_session_value(2);
    //make_pager(2,h_pageNo);   //[2016.02.15]
    make_remote_trouble_list(h_pageNo);
}

function find_remote_trouble(h_pageNo){
    //make_pager(2,h_pageNo);   //[2016.02.25]
    make_remote_trouble_list(h_pageNo);
}

function make_remote_trouble_list(h_pageNo){
    $.ajax({
        url: m_parentURL + "make_remote_trouble_list.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'order_InitiationTime': g_ORDER_REMOTE_TROUBLE_INITIATIONTIME
        },
        success: function(data, dataType){
            make_pager(2,h_pageNo); //[2016.02.25]
            $("#my-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });
}

//検索条件：未使用
function input_findData(){
    m_ProgressMsg('aaaaa...<br><img src="./img/upload.gif" /> ');
//    m_ProgressMsg('ただいま処理中です...<br><img src="./img/upload.gif" /> ');
    //$.unblockUI();

    //alert('input_findData');
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
	$('#overlay').show();
	return false;
}

//ユーザ選択
function select_user(h_pageNo){
    //alert('select_user');
    $.ajax({
        url: m_parentURL + "select_user.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'txt_UserName': $('#txt_UserName').val()
        },
        success: function(data, dataType){
            $("#select-user").empty().append(data);
            //make_pager(6,h_pageNo); //[2016.03.03]bug-fixed.
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
    //$('#overlay').show();
    return false;
}

function click_user(h_userName){
    $('#txt_UserName').val(h_userName);
    $('.popup').hide();
}

//------------------------------------------------------------------------------
//月報
//------------------------------------------------------------------------------
//月報作成用ユーザ情報一覧
var g_ORDER_REPORT_USERCODE = 'ASC';

function click_report_usercode(h_pageNo){
    if (g_ORDER_REPORT_USERCODE == 'ASC'){
        g_ORDER_REPORT_USERCODE = 'DESC';
    }else{
        g_ORDER_REPORT_USERCODE = 'ASC';
    }
    make_report_user_list(h_pageNo);
}

function get_report_status(){
    $.ajax({
        url: m_parentURL + "get_report_status.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
        },
        success: function(data, dataType){
            //文字列の先頭および末尾の連続する「半角空白・タブ文字・全角空白」を削除します
            var a_data = data.replace(/^[\s　]+|[\s　]+$/g, "");
            //文字列の先頭の連続する「半角空白・タブ文字・全角空白」を削除します
            //this.replace(/^[\s　]+/g, "");
            //文字列の末尾の連続する「半角空白・タブ文字・全角空白」を削除します
            //this.replace(/[\s　]+$/g, "");
            //文字列の先頭および末尾の連続する「半角空白・タブ文字」を削除します
            //this.replace(/^\s+|\s+$/g, "");
            //this.replace(/^\s+|\s+$/g, "");
            //alert(a_data.length);
            if (a_data.length <= 0){
                $('#my-report-message').empty().append("&nbsp;&nbsp;");
                //alert('not exists');
                $('#change_submit').prop('disabled', false);
            }else{
                $('#my-report-message').empty().append("&nbsp;&nbsp;" + a_data);
                //alert('exists');
                $('#change_submit').prop('disabled', true);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

function make_report_user_list(h_pageNo){
    $.ajax({
        url: m_parentURL + "make_report_user_list.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'OrderUserCode': g_ORDER_REPORT_USERCODE
        },
        success: function(data, dataType){
            make_pager(3,h_pageNo); //[2016.02.25]
            $("#my-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//月報作成実施
function make_report_monthly(){
    if ($("input[name='rdo_SelectUser']:checked").val() == null){
            alert("作成対象のユーザが選択されていません！");
            return false;
    }
    var a_SelectUser = $("input[name='rdo_SelectUser']:checked").val();
    //alert(s_SelectUser);
    //--------------------------------------------------------------------------
    //必須チェック
    //--------------------------------------------------------------------------
    if (($('#chk_System_IRMS').prop('checked') == false) &&
        ($('#chk_System_MSSV2').prop('checked') == false) &&
        ($('#chk_System_PBX').prop('checked') == false)){
        alert("作成対象のシステムが選択されていません！");
        return false;
    }
    if (!check_IsRequired("#entry_dateS", "開始日が入力されていません！"))
            return false;
    if (a_SelectUser == null){
        alert("印刷ユーザが選択されていません！");
        return false;
    }else if (a_SelectUser == '1'){
    if (!check_IsRequired("#exec_time", "作成日時が入力されていません！"))
            return false;
    }

    var a_radioList = document.getElementsByName("rdo_user");
    var a_SelectUserCode = "";
    for(var a_i=0; a_i<a_radioList.length; a_i++){
        if (a_radioList[a_i].checked) {
            a_SelectUserCode = a_radioList[a_i].value;
            break;
        }
    }

    if (a_SelectUser == '2'){
        if (a_SelectUserCode == ""){
            alert("作成対象のユーザが選択されていません！");
            return false;
        }
    }
    if (!confirm("月報を作成します。よろしいですか？")) return;
    //[2016.04.27]
//    m_ProgressMsg('ただいま処理中です...<br><img src="./img/upload.gif" /> ');
    
    $.ajax({
        url: m_parentURL + "make_report_monthly.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'chk_System_IRMS': $('#chk_System_IRMS').prop('checked'),
            'chk_System_MSSV2': $('#chk_System_MSSV2').prop('checked'),
            'chk_System_PBX': $('#chk_System_PBX').prop('checked'),
            'entry_dateS': $('#entry_dateS').val(),
            'entry_dateE': $('#entry_dateE').val(),
            'rdo_SelectUser': a_SelectUser,
            'exec_time': $('#exec_time').val(),
            'SelectUserCode': a_SelectUserCode
        },
        success: function(data, dataType){
            alert("作成しました。");
            $.unblockUI();
            //document.location.href = './schedule.xhtml';
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	

    return true;
}

//------------------------------------------------------------------------------
//作業・停電
//------------------------------------------------------------------------------
//作業停電スケジュール：一覧
var g_ORDER_SCHEDULE_STARTTIME = 'ASC';

function click_schedule_starttime(h_pageNo){
    if (g_ORDER_SCHEDULE_STARTTIME == 'ASC'){
        g_ORDER_SCHEDULE_STARTTIME = 'DESC';
    }else{
        g_ORDER_SCHEDULE_STARTTIME = 'ASC';
    }
    make_schedule_list(h_pageNo);
}

function make_schedule_list(h_pageNo){
    $.ajax({
        url: m_parentURL + "make_schedule_list.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'OrderStartTime': g_ORDER_SCHEDULE_STARTTIME
        },
        success: function(data, dataType){
            make_pager(4,h_pageNo); //[2016.02.25]
            $("#my-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//作業停電スケジュール：警告情報編集
function make_edit_warn_list(){
    //alert(params[1]);
    $.ajax({
        url: m_parentURL + "make_edit_warn_list.jsp?" + params[1],
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
        },
        success: function(data, dataType){
            $("#my-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

//作業停電スケジュール：LOG ChECK
var g_ORDER_LOG_CHECK_START_DATE = 'DESC';

function click_log_check_start_date(h_pageNo){
    if (g_ORDER_LOG_CHECK_START_DATE == 'ASC'){
        g_ORDER_LOG_CHECK_START_DATE = 'DESC';
    }else{
        g_ORDER_LOG_CHECK_START_DATE = 'ASC';
    }
    make_log_check_list(h_pageNo);
}

function make_log_check_list(h_pageNo){
    $.ajax({
        url: m_parentURL + "make_log_check_list.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'OrderStartDate': g_ORDER_LOG_CHECK_START_DATE
        },
        success: function(data, dataType){
            make_pager(5,h_pageNo); //[2016.02.25]
            $("#my-list").empty().append(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	
}

function init_edit_warn(){
    $(function () {
        $("#entry_date").datepicker();
    });
    
 //   (function(){
 //       alert('1');
 //       jQuery(function ($) {
 //           alert('2');
 //           $("#entry_date").datepicker();
 //       });
 //       alert('3');
 //   })();
}

//警告情報新規登録
function check_WarnInput(){
    var a_JobId = $('#txt_JobId').val();
    //alert(a_JobId);
    var a_sKind = '';
    var a_JobKind = $("input[name='rdo_JobKind']:checked").val();
    
    if (a_JobId == ''){
        a_sKind = '登録';
    }else{
        a_sKind = '更新';
    }

    //--------------------------------------------------------------------------
    //必須チェック
    //--------------------------------------------------------------------------
    if (!check_IsRequired("#txt_CustomerName", "顧客名が入力されていません！"))
        return false;
    if (!check_IsRequired("#txt_JobContents", "警告内容が入力されていません！"))
        return false;
    if ($('#chk_ExecKind_St').prop('checked') == true){
        if (!check_IsRequired("#entry_timeS", "開始日時が入力されていません！"))
            return false;
    }
    if ($('#chk_ExecKind_Ed').prop('checked') == true){
        if (!check_IsRequired("#entry_timeE", "終了日時が入力されていません！"))
            return false;
    }
    
    if (!confirm("警告情報を" + a_sKind + "します。よろしいですか？")) return;
    //[2016.04.27]
//    m_ProgressMsg('ただいま処理中です...<br><img src="./img/upload.gif" /> ');

    //[2016.05.17]bug-fixed.↓
    var a_Chk_All = $('#chk_ExecDays_All').prop('checked');
    
    var a_Chk_Sun = $('#chk_ExecDays_Sun').prop('checked');
    var a_Chk_Mon = $('#chk_ExecDays_Mon').prop('checked');
    var a_Chk_Tue = $('#chk_ExecDays_Tue').prop('checked');
    var a_Chk_Wed = $('#chk_ExecDays_Wed').prop('checked');
    var a_Chk_Thr = $('#chk_ExecDays_Thr').prop('checked');
    var a_Chk_Fri = $('#chk_ExecDays_Fri').prop('checked');
    var a_Chk_Sat = $('#chk_ExecDays_Sat').prop('checked');

    var a_Chk_St = $('#chk_ExecKind_St').prop('checked');
    var a_Chk_Ed = $('#chk_ExecKind_Ed').prop('checked');
    
    if ($('#chk_ExecDays_All').prop('disabled') == true){
        a_Chk_All = false;
    }else{
        if (a_Chk_All == true){
            a_Chk_Sun = false;
            a_Chk_Mon = false;
            a_Chk_Tue = false;
            a_Chk_Wed = false;
            a_Chk_Thr = false;
            a_Chk_Fri = false;
            a_Chk_Sat = false;
        }
    }
    
    if ($('#chk_ExecDays_Sun').prop('disabled') == true){
        a_Chk_Sun = false;
        a_Chk_Mon = false;
        a_Chk_Tue = false;
        a_Chk_Wed = false;
        a_Chk_Thr = false;
        a_Chk_Fri = false;
        a_Chk_Sat = false;
    }

    if ($('#chk_ExecKind_St').prop('disabled') == true){
        a_Chk_St = true;
        a_Chk_Ed = false;
    }
    //[2016.05.17]bug-fixed.↑
    
    $.ajax({
        url: m_parentURL + "entry_warn.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'JobId': a_JobId,
            'txt_CustomerName': $('#txt_CustomerName').val(),
            'txt_BranchName': $('#txt_BranchName').val(),
            'rdo_JobKind': a_JobKind,
            'txt_JobContents': $('#txt_JobContents').val(),
            'entry_timeS': $('#entry_timeS').val(),
            'entry_timeE': $('#entry_timeE').val(),
            'chk_ExecDays_All': a_Chk_All,
            'chk_ExecDays_Sun': a_Chk_Sun,
            'chk_ExecDays_Mon': a_Chk_Mon,
            'chk_ExecDays_Tue': a_Chk_Tue,
            'chk_ExecDays_Wed': a_Chk_Wed,
            'chk_ExecDays_Thr': a_Chk_Thr,
            'chk_ExecDays_Fri': a_Chk_Fri,
            'chk_ExecDays_Sat': a_Chk_Sat,
            'chk_ExecKind_St': a_Chk_St,
            'chk_ExecKind_Ed': a_Chk_Ed
        },
        success: function(data, dataType){
            alert(a_sKind + "しました。");
            $.unblockUI();
            document.location.href = './schedule.xhtml';
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	

    return true;
}

//開始日時・終了日時チェック
function chek_WarnStEd(){
    var a_isOK_D = true;
    var a_isOK_T = true;
    var a_dateS = null;
    var a_dateE = null;
    var a_timeS = null;
    var a_timeE = null;
    
    if (($('#entry_timeS').val() != '') && ($('#entry_timeE').val() != '')){
        a_dateS = $('#entry_timeS').val().split('/');
        a_dateE = $('#entry_timeE').val().split('/');
        if ((parseInt(a_dateS[0]) == parseInt(a_dateE[0])) &&
            (parseInt(a_dateS[1]) == parseInt(a_dateE[1]))){
            //年月が同じ
            //alert('年が同じ');
            a_timeS = a_dateS[2].split(' ');
            a_timeE = a_dateE[2].split(' ');
            if (parseInt(a_timeS[0]) == parseInt(a_timeE[0])){
                //日が同じ
                //alert('日が同じ');
                $('#chk_ExecDays_All').attr('disabled', true);
                $('#chk_ExecDays_Sun').attr('disabled', true);
                $('#chk_ExecDays_Mon').attr('disabled', true);
                $('#chk_ExecDays_Tue').attr('disabled', true);
                $('#chk_ExecDays_Wed').attr('disabled', true);
                $('#chk_ExecDays_Thr').attr('disabled', true);
                $('#chk_ExecDays_Fri').attr('disabled', true);
                $('#chk_ExecDays_Sat').attr('disabled', true);
                
                a_isOK_D = false;
                
                a_timeS = a_timeS[1].split(':');
                a_timeE = a_timeE[1].split(':');
                if ((parseInt(a_timeS[0]) == parseInt(a_timeE[0])) &&
                    (parseInt(a_timeS[1]) == parseInt(a_timeE[1]))){
                    //時刻が同じ
                    //alert('時刻が同じ');
                    $('#chk_ExecKind_St').attr('disabled', true);
                    $('#chk_ExecKind_Ed').attr('disabled', true);
                    
                    a_isOK_T = false;
                }
            }
        }
    }

    if (a_isOK_D == true){
        $('#chk_ExecDays_All').attr('disabled', false);
        $('#chk_ExecDays_Sun').attr('disabled', false);
        $('#chk_ExecDays_Mon').attr('disabled', false);
        $('#chk_ExecDays_Tue').attr('disabled', false);
        $('#chk_ExecDays_Wed').attr('disabled', false);
        $('#chk_ExecDays_Thr').attr('disabled', false);
        $('#chk_ExecDays_Fri').attr('disabled', false);
        $('#chk_ExecDays_Sat').attr('disabled', false);
    }
    if (a_isOK_T == true){
        $('#chk_ExecKind_St').attr('disabled', false);
        $('#chk_ExecKind_Ed').attr('disabled', false);
    }
}

//警告情報新規削除
function delete_Warn(){
    var a_JobId = $('#txt_JobId').val();

    if (!confirm("警告情報を削除します。よろしいですか？")) return;
    //[2016.04.27]
//    m_ProgressMsg('ただいま処理中です...<br><img src="./img/upload.gif" /> ');
    
    $.ajax({
        url: m_parentURL + "delete_warn.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'JobId': a_JobId
        },
        success: function(data, dataType){
            alert("削除しました。");
            $.unblockUI();
            document.location.href = './schedule.xhtml';
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	

    return true;
}

//発報試験
function test_Alarm(){
    var a_JobId = $('#txt_JobId').val();

    if (!confirm("発報試験を実施します。よろしいですか？")) return;
    //[2016.04.27]
//    m_ProgressMsg('ただいま処理中です...<br><img src="./img/upload.gif" /> ');
    
    $.ajax({
        url: m_parentURL + "test_alarm.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'JobId': a_JobId
        },
        success: function(data, dataType){
            alert("実施しました。");
            $.unblockUI();
            //document.location.href = './schedule.xhtml';
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
   });	

    return true;
}

//リスト表示
function set_select_show_list(h_colName){
    //alert(h_colName);
    $('#select-show-key').val(h_colName);
}

//function make_show_list(h_pageNo, h_colName, h_find_key, h_select_key, h_find_sql){
function make_show_list(h_pageNo){
    //alert($('#select-show-key').val());
    //alert('show_list--->' + h_pageNo + "," + h_colName + "," + h_find_key + "," + h_select_key + "," + h_find_sql);
    //alert('show_list--->' + h_pageNo + "," + $('#show_col_name_' + h_colName).val() + "," + $('#show_find_key_' + h_colName).val());
    var a_colName = $('#select-show-key').val();
    //alert(a_colName);
    var a_find_key = '';
    var a_result = '';
    if ($('#' + $('#show_find_key_' + a_colName).val()).val() != null){
        a_find_key = $('#' + $('#show_find_key_' + a_colName).val()).val();
    }
    //alert(a_find_key);
    $.ajax({
        url: m_parentURL + "make_show_list.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'col_name': $('#show_col_name_' + a_colName).val(),
            'find_key': a_find_key
        },
        success: function(data, dataType){
            a_result = data.trim();
            if (a_result != 'NO_FIND_KEY'){
                //alert(data);
                $("#show-list").empty().append(data);
            }
            //make_pager(6,h_pageNo); //[2016.03.03]bug-fixed.
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
    });	

    if (a_result != 'NO_FIND_KEY'){
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
        //$('#overlay').show();
    }
    return false;
}

function make_show_list2(h_pageNo){
    //alert($('#select-show-key').val());
    //alert('show_list--->' + h_pageNo + "," + h_colName + "," + h_find_key + "," + h_select_key + "," + h_find_sql);
    //alert('show_list--->' + h_pageNo + "," + $('#show_col_name_' + h_colName).val() + "," + $('#show_find_key_' + h_colName).val());
    var a_colName = $('#select-show-key').val();
    //alert(a_colName);
    var a_find_key = '';
    var a_result = '';
    if ($('#' + $('#show_find_key_' + a_colName).val()).val() != null){
        a_find_key = $('#' + $('#show_find_key_' + a_colName).val()).val();
    }
    //alert(a_find_key);
    $.ajax({
        url: m_parentURL + "make_show_list2.jsp",
        type: 'POST',
        dataType: "html",
        async: false,
        data:{
            'PageNo': h_pageNo,
            'col_name': $('#show_col_name_' + a_colName).val(),
            'find_key': a_find_key
        },
        success: function(data, dataType){
            a_result = data.trim();
            if (a_result != 'NO_FIND_KEY'){
                //alert(data);
                $("#show-list2").empty().append(data);
            }
            //make_pager(6,h_pageNo); //[2016.03.03]bug-fixed.
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("[" + XMLHttpRequest.status + "][" + textStatus + "][" + errorThrown + "]");
        },
       complete: function (data) {
       }
    });	

    if (a_result != 'NO_FIND_KEY'){
        var $popup = $("#popup1s");

        //alert($popup);
        // ポップアップの幅と高さからmarginを計算する
        var mT = ($popup.outerHeight() / 2) * (-1) + 'px';
        var mL = ($popup.outerWidth() / 2) * (-1) + 'px';
        //alert(mT + "," + mL);
        // marginを設定して表示
        $('.popup2').hide();
        $popup.css({
                'margin-top': mT,
                'margin-left': mL
        }).show();
        //$('#overlay').show();
    }
    return false;
}

//リスト上の選択
function select_show_list(h_selectKey, h_selectVal){
    //alert(h_selectKey + "," + h_selectVal);
    $('#' + h_selectKey).val(h_selectVal);
}

//フィールドの追加
function append_field(h_table_id, h_idx, h_colNames){
    alert('append_field--->' + h_colNames);
    var a_iCnt = 0;
    var a_colName = h_colNames.split(":");
    alert(a_colName.length);
    var a_field = "<tr>";
    for (a_iCnt=0; a_iCnt<a_colName.length; a_iCnt++){
        alert(a_colName[a_iCnt]);
        a_field += "<td bgcolor='transparent' style='text-align:left;'>";
        a_field += "<input type='text' value='' style='width:100%;'>"
        a_field += "</td>";
    }
    a_field += "</tr>";
    
    alert(h_table_id + "_div" + h_idx);
    alert($("#" + h_table_id + "_div" + h_idx));
    $("#" + h_table_id + "_div" + h_idx).hide();

    $("#" + h_table_id).append(a_field);
}
