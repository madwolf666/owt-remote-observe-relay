comment on column . IS '';
 
-- コードマスタ
create table codemaster (
codeid		varchar2(5) not null,
code		varchar2(6) not null,
codename	varchar2(50),
validflag	varchar2(1),
commentary	varchar2(128),
updatetime	date,
updateuser	varchar2(10),
primary key(codeid,code)
);
comment on column codemaster.codeid IS 'コードID';
comment on column codemaster.code IS 'コード';
comment on column codemaster.codename IS 'コード名称';
comment on column codemaster.validflag IS '有効区分';
comment on column codemaster.commentary IS '備考';
comment on column codemaster.updatetime IS '更新日時';
comment on column codemaster.updateuser IS '更新者';

-- CrosCore障害情報マスタ
create table croscoretrbinfo (
num			numeric(6,0) not null,
flttype		varchar2(32),
fltcode		varchar2(8) not null,
define		varchar2(40),
fltname		varchar2(128),
module		varchar2(1),
method		varchar2(40),
alarm		varchar2(1),
stk			varchar2(1),
ph			varchar2(1),
class		varchar2(1),
detaila		varchar2(64),
detailb		varchar2(64),
summary		varchar2(300),
info		varchar2(300),
remove		varchar2(300),
commentary	varchar2(64),
primary key(fltcode)
);
comment on column croscoretrbinfo.num IS '項番';
comment on column croscoretrbinfo.flttype IS '障害タイプ';
comment on column croscoretrbinfo.fltcode IS '障害コード';
comment on column croscoretrbinfo.define IS 'define';
comment on column croscoretrbinfo.fltname IS '障害名称';
comment on column croscoretrbinfo.module IS '検出モジュール';
comment on column croscoretrbinfo.method IS '検出方法';
comment on column croscoretrbinfo.alarm IS 'アラームLED';
comment on column croscoretrbinfo.stk IS 'スタックセーブ';
comment on column croscoretrbinfo.ph IS '再開レベル';
comment on column croscoretrbinfo.class IS '再開レベル';
comment on column croscoretrbinfo.detaila IS '付加情報A';
comment on column croscoretrbinfo.detailb IS '付加情報B';
comment on column croscoretrbinfo.summary IS '概要';
comment on column croscoretrbinfo.info IS '付加情報';
comment on column croscoretrbinfo.remove IS '修復方法';
comment on column croscoretrbinfo.commentary IS '備考';

-- PBXリモート用顧客情報
create table customer (
id				number not null,
stationid		number not null,
remotesetid		number not null,
usernumber		number not null,
name			varchar2(88) not null,
idcode			varchar2(64) not null,
machinename		varchar2(24),
version			varchar2(20),
nodecode		varchar2(2),
nodesin			date,
g1code			varchar2(2),
urgentreportid	number,
sendmaillevel	number default 0,
primary key(id)
);

-- 顧客の拠点毎のデータ
create table customerstation (
id					number not null,
pbxremotecustomerid	number,
stationid			number not null,
remotesetid			numeric not null,
usernumber			number not null,
remotecontract		varchar2(1),
remoteclass			varchar2(8),
monthreport			varchar2(4),
diagnose			varchar2(1),
retransflg			varchar2(1),
reportclass			varchar2(6),
satelitename		varchar2(6),
retransfernumber	number,
primary key(id)
);
create index customerstation_index1 on customerstation(stationid);
create index customerstation_index2 on customerstation(pbxremotecustomerid);
comment on column customerstation.id IS 'ID';
comment on column customerstation.pbxremotecustomerid IS 'PBXリモート顧客ID';
comment on column customerstation.stationid IS '全国のステーションID';
comment on column customerstation.remotesetid IS 'SET番号';
comment on column customerstation.usernumber IS 'ユーザー番号(REC)';
comment on column customerstation.remotecontract IS 'リモート契約有/無';
comment on column customerstation.remoteclass IS 'リモート契約クラス';
comment on column customerstation.monthreport IS '月報要/不要';
comment on column customerstation.diagnose IS '診断の要/不要';
comment on column customerstation.retransflg IS '再転送有/無';
comment on column customerstation.reportclass IS '月報報告クラス';
comment on column customerstation.satelitename IS '拠点名';
comment on column customerstation.retransfernumber IS '再転送番号';

-- DiscoveryNeo障害情報マスタ
create table discoveryneotrbinfo (
num			number(6) not null,
msgid		varchar2(5) not null,
flttitle	varchar2(128),
class		varchar2(1),
action		varchar2(1024),
commentary	varchar2(256),
summary		varchar2(1024),
primary key(msgid)
);
--create unique index discoveryneotrbinfo_index1 on discoveryneotrbinfo(num);
alter table discoveryneotrbinfo add constraint d_neo_num2 unique(num);
comment on column discoveryneotrbinfo.num IS '項番';
comment on column discoveryneotrbinfo.msgid IS 'MsgID';
comment on column discoveryneotrbinfo.flttitle IS '障害名称';
comment on column discoveryneotrbinfo.class IS '障害レベル';
comment on column discoveryneotrbinfo.action IS '対策';
comment on column discoveryneotrbinfo.commentary IS '備考';
comment on column discoveryneotrbinfo.summary IS '概要';

-- IPstage障害情報マスタ
create table ipstagetrbinfo (
num			number(6),
flttype		varchar2(32),
fltcode		varchar2(4) not null,
define		varchar2(40),
fltname		varchar2(128),
module		varchar2(12),
method		varchar2(40),
alarm		varchar2(1),
stk			varchar2(1),
ph			varchar2(1),
class		varchar2(1),
detail1		varchar2(64),
detail2		varchar2(64),
summary		varchar2(300),
info		varchar2(300),
remove		varchar2(300),
commentary	varchar2(64),
primary key(fltcode)
);
--create unique index ipstagetrbinfo_index1 on ipstagetrbinfo(num);
alter table ipstagetrbinfo add constraint ipstagetrbinfo_num2 unique(num);
-- 外部キーあり
comment on column ipstagetrbinfo.num IS '項番';
comment on column ipstagetrbinfo.flttype IS '障害タイプ';
comment on column ipstagetrbinfo.fltcode IS '障害コード';
comment on column ipstagetrbinfo.define IS 'define';
comment on column ipstagetrbinfo.fltname IS '障害名称';
comment on column ipstagetrbinfo.module IS '検出モジュール';
comment on column ipstagetrbinfo.method IS '検出方法';
comment on column ipstagetrbinfo.alarm IS 'アラームＬＥＤ';
comment on column ipstagetrbinfo.stk IS 'スタックセーブ';
comment on column ipstagetrbinfo.ph IS '再開レベル';
comment on column ipstagetrbinfo.class IS '障害レベル';
comment on column ipstagetrbinfo.detail1 IS '詳細1';
comment on column ipstagetrbinfo.detail2 IS '詳細2';
comment on column ipstagetrbinfo.summary IS '概要';
comment on column ipstagetrbinfo.info IS '付加情報、詳細情報';
comment on column ipstagetrbinfo.remove IS '修復方法';
comment on column ipstagetrbinfo.commentary IS '備考';

-- IRMSユーザ管理
create table irmsremotecustomer (
usercode			varchar2(6) not null,
ext					varchar2(1),
managerpcno			varchar2(1) default '1',
diaginterval		varchar2(2) default '1',
diagtime			varchar2(5) default '07:00',
lastdiag			date default sysdate,
remotecancel		date,
linetype			varchar2(1) default '2',
inboundtype			varchar2(1) default '0',
raspass				varchar2(16) default 'remote',
commentary			varchar2(64),
version				varchar2(20),
existmail			varchar2(1) default '0',
livemailtime		varchar2(5) default '07:00',
livemailinterval	varchar2(2) default '1',
nextlivemailtime	date,
mailalarmcount		varchar2(2) default '1',
maillaststate		varchar2(1) default '0',
ftpsend				varchar2(1) default '1',
savepath			varchar2(256),
mstat				varchar2(1) default '0',
regularrasuser		varchar2(1),
rasnotifytime		varchar2(5),
rasnotifyweek		varchar2(1),
rasnotifymonth		varchar2(2),
rasnotifyalarmcount	number,
rasnotifytype		varchar2(1),
rascheckresult		varchar2(1),
rasnextnotifydate	varchar2(19),
connectmode			varchar2(1) default '0',
rasnowalarmcount	number,
mailnowalarmcount	number,
primary key(usercode)
);
comment on column irmsremotecustomer.usercode IS 'ユーザコード';
comment on column irmsremotecustomer.ext IS '外線・内線種別';
comment on column irmsremotecustomer.managerpcno IS 'ManagerPC番号';
comment on column irmsremotecustomer.diaginterval IS '診断間隔';
comment on column irmsremotecustomer.diagtime IS '診断時間';
comment on column irmsremotecustomer.lastdiag IS '最終診断日';
comment on column irmsremotecustomer.remotecancel IS 'リモート解約年月日';
comment on column irmsremotecustomer.linetype IS '回線種別';
comment on column irmsremotecustomer.inboundtype IS '着信種別';
comment on column irmsremotecustomer.raspass IS 'RASログオンパスワード';
comment on column irmsremotecustomer.commentary IS '備考';
comment on column irmsremotecustomer.version IS 'V2～(未使用)';
comment on column irmsremotecustomer.existmail IS '生存メール有無';
comment on column irmsremotecustomer.livemailtime IS '生存メール受信日時';
comment on column irmsremotecustomer.livemailinterval IS '生存メール受信間隔';
comment on column irmsremotecustomer.nextlivemailtime IS '生存メール受信予定日時';
comment on column irmsremotecustomer.mailalarmcount IS '生存メール発報カウント';
comment on column irmsremotecustomer.maillaststate IS '生存メールステータス';
comment on column irmsremotecustomer.ftpsend IS 'FHL/USA/ELOG取得有無';
comment on column irmsremotecustomer.savepath IS 'FHL/USA/ELOG保存先';

-- ログインマスタ
create table loginmaster (
account		varchar2(4) not null,
compcode	varchar2(4),
accountname	varchar2(32),
g1code		varchar2(4),
g2code		varchar2(4),
g3code		varchar2(4),
hometel		varchar2(32),
cellulartel	varchar2(32),
pockettel	varchar2(32),
email		varchar2(64),
commentary	varchar2(64),
password	varchar2(10),
roleId		varchar2(3),
logindate	date,
primary key(account)
);
-- 外部キーあり
comment on column loginmaster.account IS 'アカウント';
comment on column loginmaster.compcode IS '会社コード';
comment on column loginmaster.accountname IS '名前';
comment on column loginmaster.g1code IS '部コード';
comment on column loginmaster.g2code IS '課コード';
comment on column loginmaster.g3code IS 'グループコード';
comment on column loginmaster.hometel IS '自宅電話番号';
comment on column loginmaster.cellulartel IS '携帯電話番号';
comment on column loginmaster.pockettel IS 'ポケットベル番号';
comment on column loginmaster.email IS 'メールアドレス';
comment on column loginmaster.commentary IS '備考';
comment on column loginmaster.password IS 'パスワード';
comment on column loginmaster.roleId IS '権限種別';
comment on column loginmaster.logindate IS 'ログイン日時';

-- 保守員情報
create table mainteinfo (
empcode		varchar2(4) not null,
compcode	varchar2(4),
empname		varchar2(32),
g1code		varchar2(4),
g2code		varchar2(4),
g3code		varchar2(4),
hometel		varchar2(32),
cellulartel	varchar2(32),
pockettel	varchar2(32),
email		varchar2(64),
commentary	varchar2(64),
password	varchar2(10),
primary key(empcode)
);
-- 外部キーあり
comment on column mainteinfo.empcode is '保守員コード';
comment on column mainteinfo.compcode is '会社コード';
comment on column mainteinfo.empname is '名前';
comment on column mainteinfo.g1code is '部コード';
comment on column mainteinfo.g2code is '課コード';
comment on column mainteinfo.g3code is 'グループコード';
comment on column mainteinfo.hometel is '自宅電話番号';
comment on column mainteinfo.cellulartel is '携帯電話番号';
comment on column mainteinfo.pockettel is 'ポケットベル番号';
comment on column mainteinfo.email is 'メールアドレス';
comment on column mainteinfo.commentary is '備考';
comment on column mainteinfo.password is '保守員パスワード';

-- MSSV2用RAS通信ログ
create table mss2operationlog (
num			numeric(20) not null,
mssno		numeric not null,
usercode	character varying(6),
datetime	timestamp,
useripaddr	character varying(15),
rascode		character varying(6),
diagmethod	character varying(1),
portno		character varying(6),
report		character varying(1) default '1' not null,
primary key(num)
);
create index mss2operationlog_index1 on mss2operationlog(datetime);
create index mss2operationlog_index2 on mss2operationlog(usercode);
comment on column mss2operationlog.num is '項番';
comment on column mss2operationlog.mssno is 'MSS番号';
comment on column mss2operationlog.usercode is 'ユーザコード';
comment on column mss2operationlog.datetime is '発生時間';
comment on column mss2operationlog.useripaddr is 'ユーザIPアドレス';
comment on column mss2operationlog.rascode is 'RASコード';
comment on column mss2operationlog.diagmethod is '診断方法';
comment on column mss2operationlog.portno is 'ポート番号';
comment on column mss2operationlog.report is 'report';

-- ユーザ管理
create table mss2remotecustomer (
usercode			varchar2(6) not null,
mssno				number not null,
remotecancel		date,
diaginterval		varchar2(1),
diagday				number,
diagspec			varchar2(1),
diaglastdate		date,
office				varchar2(1) not null,
officeday			number,
officelastdate		date,
officepath			varchar2(256),
adptpw				varchar2(20) not null,
adpver				varchar2(16) not null,
rasid				varchar2(20) not null,
raspass				varchar2(20) not null,
useripaddr			varchar2(15) not null,
nodeportno			number not null,
logonname			varchar2(20),
nodepass			varchar2(20) not null,
conttype			varchar2(1),
empcode1			varchar2(4) not null,
empcode2			varchar2(4),
s_compcode			varchar2(4) not null,
empcode1a			varchar2(4),
empcode2a			varchar2(4),
empcode1b			varchar2(4),
empcode2b			varchar2(4),
empcode1c			varchar2(4),
empcode2c			varchar2(4),
lastraspingstate	varchar2(20),
lastelogflttype		varchar2(2),
lastelogfltcode		varchar2(4),
lastelogdatetime	date,
lastelogdetail		varchar2(48),
nodetype			varchar2(1) not null,
version				varchar2(16) not null,
lastelogoffset		number,
vendercode			varchar2(4),
autotime			varchar2(2) default '0',
supass				varchar2(20),
primary key(usercode)
);
-- 外部キーあり
comment on column mss2remotecustomer.usercode is 'ユーザコード';
comment on column mss2remotecustomer.mssno is 'MSS番号';		
comment on column mss2remotecustomer.remotecancel is 'リモート解約年月日';
comment on column mss2remotecustomer.diaginterval is '診断間隔';	
comment on column mss2remotecustomer.diagday is '定期診断日付';
comment on column mss2remotecustomer.diagspec is '定期診断特別体制';
comment on column mss2remotecustomer.diaglastdate is '最後の定期診断の年月日';
comment on column mss2remotecustomer.office is '局データ取得有無';
comment on column mss2remotecustomer.officeday is '局データ取得の日付';
comment on column mss2remotecustomer.officelastdate is '最後局データを取得する年月日';
comment on column mss2remotecustomer.officepath is '局データ保存先パス';
comment on column mss2remotecustomer.adptpw is 'adptpw';
comment on column mss2remotecustomer.adpver is 'ADPバージョン';
comment on column mss2remotecustomer.rasid is 'RASユーザ';
comment on column mss2remotecustomer.raspass is 'RASパスワード';
comment on column mss2remotecustomer.useripaddr is 'IPアドレス';
comment on column mss2remotecustomer.nodeportno is 'Port番号';
comment on column mss2remotecustomer.logonname is 'ログイン名';
comment on column mss2remotecustomer.nodepass is 'パスワード';
comment on column mss2remotecustomer.conttype is '契約種別';
comment on column mss2remotecustomer.empcode1 is '保守員コード1';
comment on column mss2remotecustomer.empcode2 is '保守員コード2';
comment on column mss2remotecustomer.s_compcode is 'サポート会社コード';
comment on column mss2remotecustomer.empcode1a is 'リモート受託保守員コード1';
comment on column mss2remotecustomer.empcode2a is 'リモート受託保守員コード2';
comment on column mss2remotecustomer.empcode1b is '保守受託保守員コード1';
comment on column mss2remotecustomer.empcode2b is '保守受託保守員コード2';
comment on column mss2remotecustomer.empcode1c is '保守委託保守員コード1';
comment on column mss2remotecustomer.empcode2c is '保守委託保守員コード2';
comment on column mss2remotecustomer.lastraspingstate is '前回ステータス';
comment on column mss2remotecustomer.lastelogflttype is '前回ELOGタイプ';
comment on column mss2remotecustomer.lastelogfltcode is '前回ELOGコード';
comment on column mss2remotecustomer.lastelogdatetime is '前回ELOG発生日';
comment on column mss2remotecustomer.lastelogdetail is '前回ELOG詳細';
comment on column mss2remotecustomer.nodetype is '機器種別';
comment on column mss2remotecustomer.version is 'IPstageバージョン';
comment on column mss2remotecustomer.lastelogoffset is '用途不明';	
comment on column mss2remotecustomer.vendercode is 'ベンダーコード';
comment on column mss2remotecustomer.autotime is 'autotime';
comment on column mss2remotecustomer.supass is 'supass';

-- 障害解析結果
create table multianalyzelog (
MultiManagerPCNo	character varying(1) not null,
sequenceno			numeric(20) not null,
adptype				character varying(2) not null,
machineno			character varying(4) not null,
usercode			character varying(6) not null,
datetime			timestamp not null,
ipaddress			character varying(15) not null,
fltcode				character varying(6) not null,
detail				character varying(48),
cause				character varying(128),
action				character varying(128),
commentary			character varying(128),
type				character varying(10),
reserve1			character varying(24),
reserve2			character varying(128),
reserve3			character varying(128),
reserve4			character varying(256),
reserve5			character varying(512),
reserve6			character varying(1000),
reserve7			character varying(3000),
reserve8			character varying(3000),
updatetime			timestamp with time zone,
updateuser			character varying(10),
primary key(sequenceno)
);
comment on column multianalyzelog.MultiManagerPCNo is 'MultiManagerPC番号';
comment on column multianalyzelog.sequenceno is 'シーケンス番号';
comment on column multianalyzelog.adptype is 'ADPタイプ';
comment on column multianalyzelog.machineno is '機器ID';
comment on column multianalyzelog.usercode is 'ユーザコード';
comment on column multianalyzelog.datetime is '発生日時';
comment on column multianalyzelog.ipaddress is 'IPアドレス';
comment on column multianalyzelog.fltcode is '障害コード';
comment on column multianalyzelog.detail is '詳細';
comment on column multianalyzelog.cause is '原因';
comment on column multianalyzelog.action is '対策';
comment on column multianalyzelog.commentary is '備考';
comment on column multianalyzelog.type is '障害タイプ';
comment on column multianalyzelog.reserve1 is '予備１';
comment on column multianalyzelog.reserve2 is '予備２';
comment on column multianalyzelog.reserve3 is '予備３';
comment on column multianalyzelog.reserve4 is '予備４';
comment on column multianalyzelog.reserve5 is '予備５';
comment on column multianalyzelog.reserve6 is '予備６';
comment on column multianalyzelog.reserve7 is '予備７';
comment on column multianalyzelog.reserve8 is '予備８';
comment on column multianalyzelog.updatetime is '更新日時';
comment on column multianalyzelog.updateuser is '更新者';

-- 障害定義情報
create table multitroubleinfo (
num			number(6) not null,
adptype		varchar2(2) not null,
machineno	varchar2(4) not null,
fltcode		varchar2(6) not null,
flttitle	varchar2(256),
class		varchar2(1),
action		varchar2(128),
commentary	varchar2(128),
type		varchar2(10),
reserve1	varchar2(128),
reserve2	varchar2(128),
reserve3	varchar2(256),
reserve4	varchar2(512),
reserve5	varchar2(1000),
updatetime	date,
updateuser	varchar2(10),
primary key(num)
);
comment on column multitroubleinfo.num is '項番';
comment on column multitroubleinfo.adptype is 'ADPタイプ';
comment on column multitroubleinfo.machineno is '機器ID';
comment on column multitroubleinfo.fltcode is '障害コード';
comment on column multitroubleinfo.flttitle is '障害名称';
comment on column multitroubleinfo.class is '障害レベル';
comment on column multitroubleinfo.action is '修復方法';
comment on column multitroubleinfo.commentary is '備考';
comment on column multitroubleinfo.type is '障害タイプ';
comment on column multitroubleinfo.reserve1 is '予備１';
comment on column multitroubleinfo.reserve2 is '予備２';
comment on column multitroubleinfo.reserve3 is '予備３';
comment on column multitroubleinfo.reserve4 is '予備４';
comment on column multitroubleinfo.reserve5 is '予備５';
comment on column multitroubleinfo.updatetime is '更新日時';
comment on column multitroubleinfo.updateuser is '更新者';

-- 顧客基本データ
create table newcustomermanage (
id				number not null,
usercode		varchar2(6) not null,
username		varchar2(88) not null,
maintel			varchar2(32),
chargetel		varchar2(32),
chargename		varchar2(32),
postcode		varchar2(8),
useraddr		varchar2(50),
remotecontract	varchar2(1) not null,
remoteclass		varchar2(8) not null,
monthreport		varchar2(1) not null,
remotesin		date,
diagnose		varchar2(1) not null,
linetel			varchar2(32),
remotekind		number not null,
primary key(id)
);
-- 外部キーあり
--create unique index newcustomermanage_index1 on newcustomermanage(usercode);
alter table newcustomermanage add constraint newcustomermanage_usercode2 unique(usercode);
comment on column newcustomermanage.id IS 'ID';
comment on column newcustomermanage.usercode is '顧客ID';
comment on column newcustomermanage.username is 'ユーザー名';
comment on column newcustomermanage.maintel is '代表電話番号';
comment on column newcustomermanage.chargetel is 'ユーザ担当者電話番号';
comment on column newcustomermanage.chargename is 'ユーザ担当者名';
comment on column newcustomermanage.postcode is '郵便番号';
comment on column newcustomermanage.useraddr is 'ユーザ住所';
comment on column newcustomermanage.remotecontract is 'リモート契約有/無';
comment on column newcustomermanage.remoteclass is 'リモート契約クラス';
comment on column newcustomermanage.monthreport is '月報要/不要';
comment on column newcustomermanage.remotesin is 'リモートSin年月日';
comment on column newcustomermanage.diagnose is '診断の有無';
comment on column newcustomermanage.linetel is '回線番号';
comment on column newcustomermanage.remotekind is 'リモート種類';

-- オペレーションログ
create table operationlog (
usercode	character varying(6),
datetime	timestamp,
useripaddr	character varying(15),
rascode		character varying(6),
diagmethod	character varying(1),
portno		character varying(2),
report		character varying(1) default '0',
cause		character varying(128),
action		character varying(128),
commentary	character varying(3000),
num			numeric(20) not null,
branchip	character varying(15),
primary key(num)
);
create index operationlog_index1 on operationlog(usercode,datetime);
create index operationlog_index2 on operationlog(datetime);
comment on column operationlog.usercode is 'ユーザコード';
comment on column operationlog.datetime is '発生日時';
comment on column operationlog.useripaddr is 'ＩＰアドレス';
comment on column operationlog.rascode is '障害コード';
comment on column operationlog.diagmethod is '診断方法';
comment on column operationlog.portno is 'COMポート番号';
comment on column operationlog.report is 'レポート印刷の有無';
comment on column operationlog.cause is '原因';
comment on column operationlog.action is '対策';
comment on column operationlog.commentary is '備考';

-- PBXリモートのオペレーションログ
create table pbxoperationlog (
id			numeric not null,
logdate		timestamp not null,
stationid	numeric not null,
remotesetid	numeric not null,
usernumber	numeric not null,
portnumber	numeric not null,
detail		character varying(255) not null,
usercode	character varying(6),
primary key(id)
);
create index pbxoperationlog_index1 on pbxoperationlog(logdate);
create index pbxoperationlog_index2 on pbxoperationlog(usercode);
comment on column pbxoperationlog.id is 'ID';
comment on column pbxoperationlog.logdate is 'ログ日付';
comment on column pbxoperationlog.stationid is '拠点ID';
comment on column pbxoperationlog.remotesetid is 'SETID';
comment on column pbxoperationlog.usernumber is 'REC番号';
comment on column pbxoperationlog.portnumber is 'PORT_NO.';
comment on column pbxoperationlog.detail is '詳細';
comment on column pbxoperationlog.usercode is 'ユーザーコード';

-- PBXリモートシステム用顧客データ
create table pbxremotecustomer (
id					number not null,
usercode			varchar2(6) not null,
usercode1			varchar2(6) not null,
usercode2			varchar2(6),
telcomment			varchar2(20),
userchargepart		varchar2(12),
remotememo			varchar2(20),
maintenancecenter	varchar2(6),
maintenancegroup	varchar2(6),
maintenancedirect	varchar2(20),
"COMMENT"				varchar2(20),
notes				varchar2(26),
termcode			varchar2(8),
termname			varchar2(10) not null,
syspass				varchar2(10),
patrolmethod		varchar2(4) not null,
patrolclass			varchar2(1) not null,
patroldayofweek		varchar2(8) not null,
pc					varchar2(1),
patroltime			varchar2(5),
circuitclass		varchar2(1) not null,
patrolmethod1		varchar2(6),
patrolmethod2		varchar2(9),
terminalline		varchar2(6),
iocsel				varchar2(12),
rmi					varchar2(12),
command1			varchar2(80),
command2			varchar2(80),
termdf1				varchar2(20),
termdf2				varchar2(20),
termdf3				varchar2(20),
termdf4				varchar2(20),
termdf5				varchar2(20),
termdf6				varchar2(20),
comatr				varchar2(10) not null,
ctbt				varchar2(2) not null,
r_sin				date,
passwordorg			varchar2(4),
passwordnew			varchar2(4),
syscomment			varchar2(50),
reserve				varchar2(17),
reserve1			varchar2(72),
filever				varchar2(8),
verhigh				varchar2(1),
rev1st				varchar2(1),
rev2nd				varchar2(1),
rev3rd				varchar2(1),
primary key(id)
);
create index pbxremotecustomer_index1 on pbxremotecustomer(usercode);
comment on column pbxremotecustomer.id is 'ID';
comment on column pbxremotecustomer.usercode is '顧客ID';
comment on column pbxremotecustomer.usercode1 is '管轄部署名';
comment on column pbxremotecustomer.usercode2 is '登録番号';
comment on column pbxremotecustomer.telcomment is '方式';
comment on column pbxremotecustomer.userchargepart is 'ユーザー担当部署';
comment on column pbxremotecustomer.remotememo is '記事：';
comment on column pbxremotecustomer.maintenancecenter is '保守担当者　センタ：';
comment on column pbxremotecustomer.maintenancegroup is '保守担当者　Ｇ長：';
comment on column pbxremotecustomer.maintenancedirect is '保守担当者　直接：';
comment on column pbxremotecustomer.comment is 'コメント';
comment on column pbxremotecustomer.notes is '注意事項';
comment on column pbxremotecustomer.termcode is '端末番号';
comment on column pbxremotecustomer.termname is '端末名';
comment on column pbxremotecustomer.syspass is 'システムパスワード';
comment on column pbxremotecustomer.patrolmethod is '定期診断方法（手動/自動）';
comment on column pbxremotecustomer.patrolclass is '定期診断クラス';
comment on column pbxremotecustomer.patroldayofweek is '定期診断週1の際の曜日';
comment on column pbxremotecustomer.pc is '???';
comment on column pbxremotecustomer.patroltime is '定期診断時間';
comment on column pbxremotecustomer.circuitclass is '端末DP/PB';
comment on column pbxremotecustomer.patrolmethod1 is '定期診断方式';
comment on column pbxremotecustomer.patrolmethod2 is '定期診断方法';
comment on column pbxremotecustomer.terminalline is '端末設備状況　回線所有';
comment on column pbxremotecustomer.iocsel is '端末設備状況　IOC/SEL所有';
comment on column pbxremotecustomer.rmi is '端末設備状況　RMI所有';
comment on column pbxremotecustomer.command1 is '自動パトロールコマンド1';
comment on column pbxremotecustomer.command2 is '自動パトロールコマンド2';
comment on column pbxremotecustomer.termdf1 is '発報端子１';
comment on column pbxremotecustomer.termdf2 is '発報端子２';
comment on column pbxremotecustomer.termdf3 is '発報端子３';
comment on column pbxremotecustomer.termdf4 is '発報端子４';
comment on column pbxremotecustomer.termdf5 is '発報端子５';
comment on column pbxremotecustomer.termdf6 is '発報端子６';
comment on column pbxremotecustomer.comatr is '通信属性';
comment on column pbxremotecustomer.ctbt is '通信規格';
comment on column pbxremotecustomer.r_sin is 'コメント・障害無視データ';
comment on column pbxremotecustomer.passwordorg is '旧パスワード';
comment on column pbxremotecustomer.passwordnew is '新パスワード';
comment on column pbxremotecustomer.syscomment is 'コメント・障害無視データ';
comment on column pbxremotecustomer.reserve is '予備';
comment on column pbxremotecustomer.reserve1 is '予備1';
comment on column pbxremotecustomer.filever is 'ファイルバージョン';
comment on column pbxremotecustomer.verhigh is 'バージョン情報(high)';
comment on column pbxremotecustomer.rev1st is 'バージョン情報(1st)';
comment on column pbxremotecustomer.rev2nd is 'バージョン情報(2nd)';
comment on column pbxremotecustomer.rev3rd is 'バージョン情報(3rd)';

-- RAS/Ping障害情報マスタ
create table raspingtrbinfo (
num			number(6),
rascode		varchar2(6) not null,
fltname		varchar2(256),
class		varchar2(1),
action		varchar2(128),
commentary	varchar2(128),
primary key(rascode)
);
-- create unique index raspingtrbinfo_index1 on raspingtrbinfo(num);
alter table raspingtrbinfo add constraint raspingtrbinfo_num2 unique(num);
comment on column raspingtrbinfo.num is '項番';
comment on column raspingtrbinfo.rascode is 'RAS/Ping障害コード';
comment on column raspingtrbinfo.fltname is 'RAS/Ping障害名称';
comment on column raspingtrbinfo.class is '障害レベル';
comment on column raspingtrbinfo.action is '対策';
comment on column raspingtrbinfo.commentary is '備考';

-- SNMPトラップ障害情報マスタ
create table snmpctrbinfo (
num			number(6) not null,
trapcode	varchar2(512) not null,
traphex		varchar2(11),
trapname	varchar2(256),
module		varchar2(32),
method		varchar2(64),
remove		varchar2(32),
reboot		varchar2(1),
onlining	varchar2(1),
alarm		varchar2(1),
trap		varchar2(1),
loging		varchar2(1),
if			varchar2(64),
class		varchar2(1),
system		varchar2(128),
action		varchar2(128),
commentary	varchar2(128),
primary key(num,trapcode)
);
comment on column snmpctrbinfo.num is '項番';
comment on column snmpctrbinfo.trapcode is 'トラップコード';
comment on column snmpctrbinfo.traphex is '障害番号HEX';
comment on column snmpctrbinfo.trapname is 'トラップ障害名称';
comment on column snmpctrbinfo.module is '検出ﾓｼﾞｭｰﾙ';
comment on column snmpctrbinfo.method is '検出理論';
comment on column snmpctrbinfo.remove is '復旧理論';
comment on column snmpctrbinfo.reboot is '検出時期（起動時）';
comment on column snmpctrbinfo.onlining is '検出時期（動作中）';
comment on column snmpctrbinfo.alarm is 'アラームＬＥＤ';
comment on column snmpctrbinfo.trap is 'Trap有無';
comment on column snmpctrbinfo.loging is 'ログ蓄積';
comment on column snmpctrbinfo.if is '他 Ｉ／Ｆへの通知';
comment on column snmpctrbinfo.class is '障害レベル';
comment on column snmpctrbinfo.system is 'システムへの影響';
comment on column snmpctrbinfo.action is '対策';
comment on column snmpctrbinfo.commentary is '備考';

-- SS9100プロビジョニングサーバ障害情報マスタ
create table ss9100provtrbinfo (
num			number(6),
msgid		varchar2(6) not null,
flttitle	varchar2(128),
class		varchar2(1),
action		varchar2(1024),
commentary	varchar2(256),
summary		varchar2(1024),
primary key(msgid)
);
-- create unique index ss9100provtrbinfo_index1 on ss9100provtrbinfo(num);
alter table ss9100provtrbinfo add constraint ss9100provtrbinfo_num2 unique(num);
comment on column ss9100provtrbinfo.num is 'num';
comment on column ss9100provtrbinfo.msgid is 'MsgID';
comment on column ss9100provtrbinfo.flttitle is 'flttitle';
comment on column ss9100provtrbinfo.class is 'class';
comment on column ss9100provtrbinfo.action is '対策';
comment on column ss9100provtrbinfo.commentary is '備考';
comment on column ss9100provtrbinfo.summary is '概要';

-- SS9100障害情報
create table ss9100trbinfo (
num			number(6) not null,
msgid		varchar2(5) not null,
flttitle	varchar2(128),
class		varchar2(1),
action		varchar2(1024),
commentary	varchar2(128),
summary		varchar2(1024),
primary key(msgid)
);
-- create unique index ss9100trbinfo_index1 on ss9100trbinfo(num);
alter table ss9100trbinfo add constraint ss9100trbinfo_num2 unique(num);
comment on column ss9100trbinfo.num is '項番';
comment on column ss9100trbinfo.msgid is 'MSGID';
comment on column ss9100trbinfo.flttitle is 'タイトル';
comment on column ss9100trbinfo.class is '障害レベル';
comment on column ss9100trbinfo.action is '対策';
comment on column ss9100trbinfo.commentary is '備考';
comment on column ss9100trbinfo.summary is '概要';

-- SSクラスタ障害情報マスタ
create table ssclustertrbinfo (
num			number(6),
msgid		varchar2(5) not null,
flttitle	varchar2(128),
class		varchar2(1),
action		varchar2(1024),
commentary	varchar2(256),
summary		varchar2(1024),
primary key(msgid)
);
-- create unique index ssclustertrbinfo_index1 on ssclustertrbinfo(num);
alter table ssclustertrbinfo add constraint ssclustertrbinfo_num2 unique(num);
comment on column ssclustertrbinfo.num is 'num';
comment on column ssclustertrbinfo.msgid is 'MsgID';
comment on column ssclustertrbinfo.flttitle is 'flttitle';
comment on column ssclustertrbinfo.class is 'class';
comment on column ssclustertrbinfo.action is '対策';
comment on column ssclustertrbinfo.commentary is '備考';
comment on column ssclustertrbinfo.summary is '概要';

-- リモート発報システム用顧客データ
create table remotemonitoringcustomer (
id				number not null,
usercode		varchar2(6) not null,
stationid		number,
remotesetid		number,
usernumber		number,
idcode			varchar2(64) not null,
machinename		varchar2(24),
version			varchar2(20),
nodecode		varchar2(2),
g1code			varchar2(2),
urgentreportid	number not null,
sendmaillevel	number not null,
autoalarmreset	number,
primary key(id)
);
create index remotemonitoringcustomer_index1 on remotemonitoringcustomer(usercode);

create table irmsremotecustomer(
usercode		varchar2(6),
ext			varchar2(1),
managerpcno		varchar2(1) default '1',
diaginterval		varchar2(2) default '1',
diagtime		varchar2(5) default '07:00',
lastdiag		date default sysdate,
remotecancel		date,
linetype		varchar2(1) default '2',
inboundtype		varchar2(1) default '0',
raspass			varchar2(16) default 'remote',
commentary		varchar2(64),
version			varchar2(20),
existmail		varchar2(1) default '0',
livemailtime		varchar2(5) default '07:00',
livemailinterval	varchar2(2) default '1',
nextlivemailtime	date,
mailalarmcount		varchar2(2) default '1',
maillaststate		varchar2(1) default '0',
ftpsend			varchar2(1) default '1',
savepath		varchar2(256),
mstat			varchar2(1) default '0',
regularrasuser		varchar2(1),
rasnotifytime		varchar2(5),
rasnotifyweek		varchar2(1),
rasnotifymonth		varchar2(2),
rasnotifyalarmcount	number,
rasnotifytype		varchar2(1),
rascheckresult		varchar2(1),
rasnextnotifydate	varchar2(19),
connectmode		varchar2(1) default '0',
rasnowalarmcount	number,
mailnowalarmcount	number,
primary key(usercode)
);





-- ログ解析
create table loganalyzeschedule (
id				number not null,
logname			varchar2(32) not null,
period			varchar2(5) not null,
starttime		date not null,
findkeyword		varchar2(32) not null,
sendmailaddress	varchar2(4096),
primary key(id)
);
