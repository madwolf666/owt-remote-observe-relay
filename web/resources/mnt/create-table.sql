comment on column . IS '';
 
-- �R�[�h�}�X�^
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
comment on column codemaster.codeid IS '�R�[�hID';
comment on column codemaster.code IS '�R�[�h';
comment on column codemaster.codename IS '�R�[�h����';
comment on column codemaster.validflag IS '�L���敪';
comment on column codemaster.commentary IS '���l';
comment on column codemaster.updatetime IS '�X�V����';
comment on column codemaster.updateuser IS '�X�V��';

-- CrosCore��Q���}�X�^
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
comment on column croscoretrbinfo.num IS '����';
comment on column croscoretrbinfo.flttype IS '��Q�^�C�v';
comment on column croscoretrbinfo.fltcode IS '��Q�R�[�h';
comment on column croscoretrbinfo.define IS 'define';
comment on column croscoretrbinfo.fltname IS '��Q����';
comment on column croscoretrbinfo.module IS '���o���W���[��';
comment on column croscoretrbinfo.method IS '���o���@';
comment on column croscoretrbinfo.alarm IS '�A���[��LED';
comment on column croscoretrbinfo.stk IS '�X�^�b�N�Z�[�u';
comment on column croscoretrbinfo.ph IS '�ĊJ���x��';
comment on column croscoretrbinfo.class IS '�ĊJ���x��';
comment on column croscoretrbinfo.detaila IS '�t�����A';
comment on column croscoretrbinfo.detailb IS '�t�����B';
comment on column croscoretrbinfo.summary IS '�T�v';
comment on column croscoretrbinfo.info IS '�t�����';
comment on column croscoretrbinfo.remove IS '�C�����@';
comment on column croscoretrbinfo.commentary IS '���l';

-- PBX�����[�g�p�ڋq���
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

-- �ڋq�̋��_���̃f�[�^
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
comment on column customerstation.pbxremotecustomerid IS 'PBX�����[�g�ڋqID';
comment on column customerstation.stationid IS '�S���̃X�e�[�V����ID';
comment on column customerstation.remotesetid IS 'SET�ԍ�';
comment on column customerstation.usernumber IS '���[�U�[�ԍ�(REC)';
comment on column customerstation.remotecontract IS '�����[�g�_��L/��';
comment on column customerstation.remoteclass IS '�����[�g�_��N���X';
comment on column customerstation.monthreport IS '����v/�s�v';
comment on column customerstation.diagnose IS '�f�f�̗v/�s�v';
comment on column customerstation.retransflg IS '�ē]���L/��';
comment on column customerstation.reportclass IS '����񍐃N���X';
comment on column customerstation.satelitename IS '���_��';
comment on column customerstation.retransfernumber IS '�ē]���ԍ�';

-- DiscoveryNeo��Q���}�X�^
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
comment on column discoveryneotrbinfo.num IS '����';
comment on column discoveryneotrbinfo.msgid IS 'MsgID';
comment on column discoveryneotrbinfo.flttitle IS '��Q����';
comment on column discoveryneotrbinfo.class IS '��Q���x��';
comment on column discoveryneotrbinfo.action IS '�΍�';
comment on column discoveryneotrbinfo.commentary IS '���l';
comment on column discoveryneotrbinfo.summary IS '�T�v';

-- IPstage��Q���}�X�^
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
-- �O���L�[����
comment on column ipstagetrbinfo.num IS '����';
comment on column ipstagetrbinfo.flttype IS '��Q�^�C�v';
comment on column ipstagetrbinfo.fltcode IS '��Q�R�[�h';
comment on column ipstagetrbinfo.define IS 'define';
comment on column ipstagetrbinfo.fltname IS '��Q����';
comment on column ipstagetrbinfo.module IS '���o���W���[��';
comment on column ipstagetrbinfo.method IS '���o���@';
comment on column ipstagetrbinfo.alarm IS '�A���[���k�d�c';
comment on column ipstagetrbinfo.stk IS '�X�^�b�N�Z�[�u';
comment on column ipstagetrbinfo.ph IS '�ĊJ���x��';
comment on column ipstagetrbinfo.class IS '��Q���x��';
comment on column ipstagetrbinfo.detail1 IS '�ڍ�1';
comment on column ipstagetrbinfo.detail2 IS '�ڍ�2';
comment on column ipstagetrbinfo.summary IS '�T�v';
comment on column ipstagetrbinfo.info IS '�t�����A�ڍ׏��';
comment on column ipstagetrbinfo.remove IS '�C�����@';
comment on column ipstagetrbinfo.commentary IS '���l';

-- IRMS���[�U�Ǘ�
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
comment on column irmsremotecustomer.usercode IS '���[�U�R�[�h';
comment on column irmsremotecustomer.ext IS '�O���E�������';
comment on column irmsremotecustomer.managerpcno IS 'ManagerPC�ԍ�';
comment on column irmsremotecustomer.diaginterval IS '�f�f�Ԋu';
comment on column irmsremotecustomer.diagtime IS '�f�f����';
comment on column irmsremotecustomer.lastdiag IS '�ŏI�f�f��';
comment on column irmsremotecustomer.remotecancel IS '�����[�g���N����';
comment on column irmsremotecustomer.linetype IS '������';
comment on column irmsremotecustomer.inboundtype IS '���M���';
comment on column irmsremotecustomer.raspass IS 'RAS���O�I���p�X���[�h';
comment on column irmsremotecustomer.commentary IS '���l';
comment on column irmsremotecustomer.version IS 'V2�`(���g�p)';
comment on column irmsremotecustomer.existmail IS '�������[���L��';
comment on column irmsremotecustomer.livemailtime IS '�������[����M����';
comment on column irmsremotecustomer.livemailinterval IS '�������[����M�Ԋu';
comment on column irmsremotecustomer.nextlivemailtime IS '�������[����M�\�����';
comment on column irmsremotecustomer.mailalarmcount IS '�������[������J�E���g';
comment on column irmsremotecustomer.maillaststate IS '�������[���X�e�[�^�X';
comment on column irmsremotecustomer.ftpsend IS 'FHL/USA/ELOG�擾�L��';
comment on column irmsremotecustomer.savepath IS 'FHL/USA/ELOG�ۑ���';

-- ���O�C���}�X�^
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
-- �O���L�[����
comment on column loginmaster.account IS '�A�J�E���g';
comment on column loginmaster.compcode IS '��ЃR�[�h';
comment on column loginmaster.accountname IS '���O';
comment on column loginmaster.g1code IS '���R�[�h';
comment on column loginmaster.g2code IS '�ۃR�[�h';
comment on column loginmaster.g3code IS '�O���[�v�R�[�h';
comment on column loginmaster.hometel IS '����d�b�ԍ�';
comment on column loginmaster.cellulartel IS '�g�ѓd�b�ԍ�';
comment on column loginmaster.pockettel IS '�|�P�b�g�x���ԍ�';
comment on column loginmaster.email IS '���[���A�h���X';
comment on column loginmaster.commentary IS '���l';
comment on column loginmaster.password IS '�p�X���[�h';
comment on column loginmaster.roleId IS '�������';
comment on column loginmaster.logindate IS '���O�C������';

-- �ێ�����
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
-- �O���L�[����
comment on column mainteinfo.empcode is '�ێ���R�[�h';
comment on column mainteinfo.compcode is '��ЃR�[�h';
comment on column mainteinfo.empname is '���O';
comment on column mainteinfo.g1code is '���R�[�h';
comment on column mainteinfo.g2code is '�ۃR�[�h';
comment on column mainteinfo.g3code is '�O���[�v�R�[�h';
comment on column mainteinfo.hometel is '����d�b�ԍ�';
comment on column mainteinfo.cellulartel is '�g�ѓd�b�ԍ�';
comment on column mainteinfo.pockettel is '�|�P�b�g�x���ԍ�';
comment on column mainteinfo.email is '���[���A�h���X';
comment on column mainteinfo.commentary is '���l';
comment on column mainteinfo.password is '�ێ���p�X���[�h';

-- MSSV2�pRAS�ʐM���O
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
comment on column mss2operationlog.num is '����';
comment on column mss2operationlog.mssno is 'MSS�ԍ�';
comment on column mss2operationlog.usercode is '���[�U�R�[�h';
comment on column mss2operationlog.datetime is '��������';
comment on column mss2operationlog.useripaddr is '���[�UIP�A�h���X';
comment on column mss2operationlog.rascode is 'RAS�R�[�h';
comment on column mss2operationlog.diagmethod is '�f�f���@';
comment on column mss2operationlog.portno is '�|�[�g�ԍ�';
comment on column mss2operationlog.report is 'report';

-- ���[�U�Ǘ�
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
-- �O���L�[����
comment on column mss2remotecustomer.usercode is '���[�U�R�[�h';
comment on column mss2remotecustomer.mssno is 'MSS�ԍ�';		
comment on column mss2remotecustomer.remotecancel is '�����[�g���N����';
comment on column mss2remotecustomer.diaginterval is '�f�f�Ԋu';	
comment on column mss2remotecustomer.diagday is '����f�f���t';
comment on column mss2remotecustomer.diagspec is '����f�f���ʑ̐�';
comment on column mss2remotecustomer.diaglastdate is '�Ō�̒���f�f�̔N����';
comment on column mss2remotecustomer.office is '�ǃf�[�^�擾�L��';
comment on column mss2remotecustomer.officeday is '�ǃf�[�^�擾�̓��t';
comment on column mss2remotecustomer.officelastdate is '�Ō�ǃf�[�^���擾����N����';
comment on column mss2remotecustomer.officepath is '�ǃf�[�^�ۑ���p�X';
comment on column mss2remotecustomer.adptpw is 'adptpw';
comment on column mss2remotecustomer.adpver is 'ADP�o�[�W����';
comment on column mss2remotecustomer.rasid is 'RAS���[�U';
comment on column mss2remotecustomer.raspass is 'RAS�p�X���[�h';
comment on column mss2remotecustomer.useripaddr is 'IP�A�h���X';
comment on column mss2remotecustomer.nodeportno is 'Port�ԍ�';
comment on column mss2remotecustomer.logonname is '���O�C����';
comment on column mss2remotecustomer.nodepass is '�p�X���[�h';
comment on column mss2remotecustomer.conttype is '�_����';
comment on column mss2remotecustomer.empcode1 is '�ێ���R�[�h1';
comment on column mss2remotecustomer.empcode2 is '�ێ���R�[�h2';
comment on column mss2remotecustomer.s_compcode is '�T�|�[�g��ЃR�[�h';
comment on column mss2remotecustomer.empcode1a is '�����[�g����ێ���R�[�h1';
comment on column mss2remotecustomer.empcode2a is '�����[�g����ێ���R�[�h2';
comment on column mss2remotecustomer.empcode1b is '�ێ����ێ���R�[�h1';
comment on column mss2remotecustomer.empcode2b is '�ێ����ێ���R�[�h2';
comment on column mss2remotecustomer.empcode1c is '�ێ�ϑ��ێ���R�[�h1';
comment on column mss2remotecustomer.empcode2c is '�ێ�ϑ��ێ���R�[�h2';
comment on column mss2remotecustomer.lastraspingstate is '�O��X�e�[�^�X';
comment on column mss2remotecustomer.lastelogflttype is '�O��ELOG�^�C�v';
comment on column mss2remotecustomer.lastelogfltcode is '�O��ELOG�R�[�h';
comment on column mss2remotecustomer.lastelogdatetime is '�O��ELOG������';
comment on column mss2remotecustomer.lastelogdetail is '�O��ELOG�ڍ�';
comment on column mss2remotecustomer.nodetype is '�@����';
comment on column mss2remotecustomer.version is 'IPstage�o�[�W����';
comment on column mss2remotecustomer.lastelogoffset is '�p�r�s��';	
comment on column mss2remotecustomer.vendercode is '�x���_�[�R�[�h';
comment on column mss2remotecustomer.autotime is 'autotime';
comment on column mss2remotecustomer.supass is 'supass';

-- ��Q��͌���
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
comment on column multianalyzelog.MultiManagerPCNo is 'MultiManagerPC�ԍ�';
comment on column multianalyzelog.sequenceno is '�V�[�P���X�ԍ�';
comment on column multianalyzelog.adptype is 'ADP�^�C�v';
comment on column multianalyzelog.machineno is '�@��ID';
comment on column multianalyzelog.usercode is '���[�U�R�[�h';
comment on column multianalyzelog.datetime is '��������';
comment on column multianalyzelog.ipaddress is 'IP�A�h���X';
comment on column multianalyzelog.fltcode is '��Q�R�[�h';
comment on column multianalyzelog.detail is '�ڍ�';
comment on column multianalyzelog.cause is '����';
comment on column multianalyzelog.action is '�΍�';
comment on column multianalyzelog.commentary is '���l';
comment on column multianalyzelog.type is '��Q�^�C�v';
comment on column multianalyzelog.reserve1 is '�\���P';
comment on column multianalyzelog.reserve2 is '�\���Q';
comment on column multianalyzelog.reserve3 is '�\���R';
comment on column multianalyzelog.reserve4 is '�\���S';
comment on column multianalyzelog.reserve5 is '�\���T';
comment on column multianalyzelog.reserve6 is '�\���U';
comment on column multianalyzelog.reserve7 is '�\���V';
comment on column multianalyzelog.reserve8 is '�\���W';
comment on column multianalyzelog.updatetime is '�X�V����';
comment on column multianalyzelog.updateuser is '�X�V��';

-- ��Q��`���
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
comment on column multitroubleinfo.num is '����';
comment on column multitroubleinfo.adptype is 'ADP�^�C�v';
comment on column multitroubleinfo.machineno is '�@��ID';
comment on column multitroubleinfo.fltcode is '��Q�R�[�h';
comment on column multitroubleinfo.flttitle is '��Q����';
comment on column multitroubleinfo.class is '��Q���x��';
comment on column multitroubleinfo.action is '�C�����@';
comment on column multitroubleinfo.commentary is '���l';
comment on column multitroubleinfo.type is '��Q�^�C�v';
comment on column multitroubleinfo.reserve1 is '�\���P';
comment on column multitroubleinfo.reserve2 is '�\���Q';
comment on column multitroubleinfo.reserve3 is '�\���R';
comment on column multitroubleinfo.reserve4 is '�\���S';
comment on column multitroubleinfo.reserve5 is '�\���T';
comment on column multitroubleinfo.updatetime is '�X�V����';
comment on column multitroubleinfo.updateuser is '�X�V��';

-- �ڋq��{�f�[�^
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
-- �O���L�[����
--create unique index newcustomermanage_index1 on newcustomermanage(usercode);
alter table newcustomermanage add constraint newcustomermanage_usercode2 unique(usercode);
comment on column newcustomermanage.id IS 'ID';
comment on column newcustomermanage.usercode is '�ڋqID';
comment on column newcustomermanage.username is '���[�U�[��';
comment on column newcustomermanage.maintel is '��\�d�b�ԍ�';
comment on column newcustomermanage.chargetel is '���[�U�S���ғd�b�ԍ�';
comment on column newcustomermanage.chargename is '���[�U�S���Җ�';
comment on column newcustomermanage.postcode is '�X�֔ԍ�';
comment on column newcustomermanage.useraddr is '���[�U�Z��';
comment on column newcustomermanage.remotecontract is '�����[�g�_��L/��';
comment on column newcustomermanage.remoteclass is '�����[�g�_��N���X';
comment on column newcustomermanage.monthreport is '����v/�s�v';
comment on column newcustomermanage.remotesin is '�����[�gSin�N����';
comment on column newcustomermanage.diagnose is '�f�f�̗L��';
comment on column newcustomermanage.linetel is '����ԍ�';
comment on column newcustomermanage.remotekind is '�����[�g���';

-- �I�y���[�V�������O
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
comment on column operationlog.usercode is '���[�U�R�[�h';
comment on column operationlog.datetime is '��������';
comment on column operationlog.useripaddr is '�h�o�A�h���X';
comment on column operationlog.rascode is '��Q�R�[�h';
comment on column operationlog.diagmethod is '�f�f���@';
comment on column operationlog.portno is 'COM�|�[�g�ԍ�';
comment on column operationlog.report is '���|�[�g����̗L��';
comment on column operationlog.cause is '����';
comment on column operationlog.action is '�΍�';
comment on column operationlog.commentary is '���l';

-- PBX�����[�g�̃I�y���[�V�������O
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
comment on column pbxoperationlog.logdate is '���O���t';
comment on column pbxoperationlog.stationid is '���_ID';
comment on column pbxoperationlog.remotesetid is 'SETID';
comment on column pbxoperationlog.usernumber is 'REC�ԍ�';
comment on column pbxoperationlog.portnumber is 'PORT_NO.';
comment on column pbxoperationlog.detail is '�ڍ�';
comment on column pbxoperationlog.usercode is '���[�U�[�R�[�h';

-- PBX�����[�g�V�X�e���p�ڋq�f�[�^
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
comment on column pbxremotecustomer.usercode is '�ڋqID';
comment on column pbxremotecustomer.usercode1 is '�Ǌ�������';
comment on column pbxremotecustomer.usercode2 is '�o�^�ԍ�';
comment on column pbxremotecustomer.telcomment is '����';
comment on column pbxremotecustomer.userchargepart is '���[�U�[�S������';
comment on column pbxremotecustomer.remotememo is '�L���F';
comment on column pbxremotecustomer.maintenancecenter is '�ێ�S���ҁ@�Z���^�F';
comment on column pbxremotecustomer.maintenancegroup is '�ێ�S���ҁ@�f���F';
comment on column pbxremotecustomer.maintenancedirect is '�ێ�S���ҁ@���ځF';
comment on column pbxremotecustomer.comment is '�R�����g';
comment on column pbxremotecustomer.notes is '���ӎ���';
comment on column pbxremotecustomer.termcode is '�[���ԍ�';
comment on column pbxremotecustomer.termname is '�[����';
comment on column pbxremotecustomer.syspass is '�V�X�e���p�X���[�h';
comment on column pbxremotecustomer.patrolmethod is '����f�f���@�i�蓮/�����j';
comment on column pbxremotecustomer.patrolclass is '����f�f�N���X';
comment on column pbxremotecustomer.patroldayofweek is '����f�f�T1�̍ۂ̗j��';
comment on column pbxremotecustomer.pc is '???';
comment on column pbxremotecustomer.patroltime is '����f�f����';
comment on column pbxremotecustomer.circuitclass is '�[��DP/PB';
comment on column pbxremotecustomer.patrolmethod1 is '����f�f����';
comment on column pbxremotecustomer.patrolmethod2 is '����f�f���@';
comment on column pbxremotecustomer.terminalline is '�[���ݔ��󋵁@������L';
comment on column pbxremotecustomer.iocsel is '�[���ݔ��󋵁@IOC/SEL���L';
comment on column pbxremotecustomer.rmi is '�[���ݔ��󋵁@RMI���L';
comment on column pbxremotecustomer.command1 is '�����p�g���[���R�}���h1';
comment on column pbxremotecustomer.command2 is '�����p�g���[���R�}���h2';
comment on column pbxremotecustomer.termdf1 is '����[�q�P';
comment on column pbxremotecustomer.termdf2 is '����[�q�Q';
comment on column pbxremotecustomer.termdf3 is '����[�q�R';
comment on column pbxremotecustomer.termdf4 is '����[�q�S';
comment on column pbxremotecustomer.termdf5 is '����[�q�T';
comment on column pbxremotecustomer.termdf6 is '����[�q�U';
comment on column pbxremotecustomer.comatr is '�ʐM����';
comment on column pbxremotecustomer.ctbt is '�ʐM�K�i';
comment on column pbxremotecustomer.r_sin is '�R�����g�E��Q�����f�[�^';
comment on column pbxremotecustomer.passwordorg is '���p�X���[�h';
comment on column pbxremotecustomer.passwordnew is '�V�p�X���[�h';
comment on column pbxremotecustomer.syscomment is '�R�����g�E��Q�����f�[�^';
comment on column pbxremotecustomer.reserve is '�\��';
comment on column pbxremotecustomer.reserve1 is '�\��1';
comment on column pbxremotecustomer.filever is '�t�@�C���o�[�W����';
comment on column pbxremotecustomer.verhigh is '�o�[�W�������(high)';
comment on column pbxremotecustomer.rev1st is '�o�[�W�������(1st)';
comment on column pbxremotecustomer.rev2nd is '�o�[�W�������(2nd)';
comment on column pbxremotecustomer.rev3rd is '�o�[�W�������(3rd)';

-- RAS/Ping��Q���}�X�^
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
comment on column raspingtrbinfo.num is '����';
comment on column raspingtrbinfo.rascode is 'RAS/Ping��Q�R�[�h';
comment on column raspingtrbinfo.fltname is 'RAS/Ping��Q����';
comment on column raspingtrbinfo.class is '��Q���x��';
comment on column raspingtrbinfo.action is '�΍�';
comment on column raspingtrbinfo.commentary is '���l';

-- SNMP�g���b�v��Q���}�X�^
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
comment on column snmpctrbinfo.num is '����';
comment on column snmpctrbinfo.trapcode is '�g���b�v�R�[�h';
comment on column snmpctrbinfo.traphex is '��Q�ԍ�HEX';
comment on column snmpctrbinfo.trapname is '�g���b�v��Q����';
comment on column snmpctrbinfo.module is '���oӼޭ��';
comment on column snmpctrbinfo.method is '���o���_';
comment on column snmpctrbinfo.remove is '�������_';
comment on column snmpctrbinfo.reboot is '���o�����i�N�����j';
comment on column snmpctrbinfo.onlining is '���o�����i���쒆�j';
comment on column snmpctrbinfo.alarm is '�A���[���k�d�c';
comment on column snmpctrbinfo.trap is 'Trap�L��';
comment on column snmpctrbinfo.loging is '���O�~��';
comment on column snmpctrbinfo.if is '�� �h�^�e�ւ̒ʒm';
comment on column snmpctrbinfo.class is '��Q���x��';
comment on column snmpctrbinfo.system is '�V�X�e���ւ̉e��';
comment on column snmpctrbinfo.action is '�΍�';
comment on column snmpctrbinfo.commentary is '���l';

-- SS9100�v���r�W���j���O�T�[�o��Q���}�X�^
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
comment on column ss9100provtrbinfo.action is '�΍�';
comment on column ss9100provtrbinfo.commentary is '���l';
comment on column ss9100provtrbinfo.summary is '�T�v';

-- SS9100��Q���
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
comment on column ss9100trbinfo.num is '����';
comment on column ss9100trbinfo.msgid is 'MSGID';
comment on column ss9100trbinfo.flttitle is '�^�C�g��';
comment on column ss9100trbinfo.class is '��Q���x��';
comment on column ss9100trbinfo.action is '�΍�';
comment on column ss9100trbinfo.commentary is '���l';
comment on column ss9100trbinfo.summary is '�T�v';

-- SS�N���X�^��Q���}�X�^
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
comment on column ssclustertrbinfo.action is '�΍�';
comment on column ssclustertrbinfo.commentary is '���l';
comment on column ssclustertrbinfo.summary is '�T�v';

-- �����[�g����V�X�e���p�ڋq�f�[�^
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





-- ���O���
create table loganalyzeschedule (
id				number not null,
logname			varchar2(32) not null,
period			varchar2(5) not null,
starttime		date not null,
findkeyword		varchar2(32) not null,
sendmailaddress	varchar2(4096),
primary key(id)
);
