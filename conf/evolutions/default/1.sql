# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ihsaddress (
  addressID                 integer not null,
  address1                  varchar(255),
  address2                  varchar(255),
  city                      varchar(255),
  stateOrProvence           varchar(255),
  postalCode                varchar(255),
  zipPlus                   varchar(255),
  country                   varchar(255),
  createdTS                 date,
  constraint pk_ihsaddress primary key (addressID))
;

create table ihsauthorizedsource (
  authorizedSourceID        integer not null,
  name                      varchar(255),
  description               varchar(255),
  memberId                  integer,
  constraint pk_ihsauthorizedsource primary key (authorizedSourceID))
;

create table ihscategoryconflictresolution (
  categoryConflictResolutionID integer not null,
  title_category_id         integer,
  prevailing_source_title_category_id integer,
  overridden_source_title_category_id integer,
  constraint pk_ihscategoryconflictresolution primary key (categoryConflictResolutionID))
;

create table ihsholding (
  holdingID                 integer not null,
  issueID                   integer,
  memberID                  integer,
  locationID                integer,
  holdingStatusID           integer,
  constraint pk_ihsholding primary key (holdingID))
;

create table ihsholdingnote (
  holdingNoteID             integer not null,
  holdingID                 integer,
  note                      varchar(255),
  constraint pk_ihsholdingnote primary key (holdingNoteID))
;

create table ihsingestionexception (
  ingestionExceptionID      integer not null,
  ingestionRecordID         integer,
  ingestionExceptionTypeID  integer,
  recordTitle               varchar(255),
  issues                    varchar(255),
  ingestionExceptionStatusID integer,
  userID                    integer,
  lockDate                  date,
  constraint pk_ihsingestionexception primary key (ingestionExceptionID))
;

create table ihsingestionjob (
  ingestionJobID            integer not null,
  name                      varchar(255),
  description               varchar(255),
  authorizedSourceID        integer,
  ingestionDataTypeID       integer,
  creationDate              timestamp,
  ingestedByUserID          integer,
  sourceFileString          varchar(255),
  ingestionJobStatusID      integer,
  statusDetail              varchar(255),
  constraint pk_ihsingestionjob primary key (ingestionJobID))
;

create table ihsingestionrecord (
  ingestionRecordID         integer not null,
  ingestionJobID            integer,
  rawRecordData             varchar(1000),
  ingestionRecordStatusID   integer,
  recordTitle               varchar(255),
  issues                    varchar(255),
  jsonRecordData            varchar(5000),
  userID                    integer,
  lockUserID                integer,
  lockDate                  date,
  constraint pk_ihsingestionrecord primary key (ingestionRecordID))
;

create table ihsissue (
  issueID                   integer not null,
  titleID                   integer,
  volumeID                  integer,
  publicationRangeID        integer,
  publicationDate           date,
  issueNumber               varchar(255),
  name                      varchar(255),
  description               varchar(255),
  numPages                  integer,
  imagePageRatio            integer,
  issueStatusID             integer,
  constraint pk_ihsissue primary key (issueID))
;

create table ihslocation (
  locationID                integer not null,
  name                      varchar(255),
  description               varchar(255),
  memberId                  integer,
  addressId                 integer,
  constraint pk_ihslocation primary key (locationID))
;

create table ihsmember (
  memberID                  integer not null,
  name                      varchar(255),
  description               varchar(255),
  ftpdirectory              varchar(255),
  memberStatusID            integer,
  constraint pk_ihsmember primary key (memberID))
;

create table ihspublicationrange (
  publicationRangeID        integer not null,
  titleID                   integer,
  periodicityTypeID         integer,
  pbrStartDate              integer,
  pbrEndDate                integer,
  constraint pk_ihspublicationrange primary key (publicationRangeID))
;

create table ihspublisher (
  publisherID               integer not null,
  name                      varchar(255),
  description               varchar(255),
  startDate                 date,
  endDate                   date,
  addressId                 integer,
  constraint pk_ihspublisher primary key (publisherID))
;

create table ihssecurityrole (
  securityRoleId            integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_ihssecurityrole primary key (securityRoleId))
;

create table ihssourcetitle (
  sourceTitleID             integer not null,
  ingestionRecordID         integer,
  titleID                   integer,
  titleTypeID               integer,
  title                     varchar(255),
  alphaTitle                varchar(255),
  printISSN                 varchar(255),
  eISSN                     varchar(255),
  oclcNumber                varchar(255),
  lccn                      varchar(255),
  publisherID               integer,
  description               varchar(255),
  constraint pk_ihssourcetitle primary key (sourceTitleID))
;

create table ihstitle (
  titleID                   integer not null,
  title                     varchar(255),
  titleTypeID               integer,
  alphaTitle                varchar(255),
  printISSN                 varchar(255),
  eISSN                     varchar(255),
  oclcNumber                varchar(255),
  lccn                      varchar(255),
  publisherID               integer,
  description               varchar(255),
  titleStatusID             integer,
  constraint pk_ihstitle primary key (titleID))
;

create table ihsuser (
  userID                    integer not null,
  firstName                 varchar(255),
  lastName                  varchar(255),
  memberId                  integer,
  userStatusID              integer,
  userName                  varchar(255),
  password                  varchar(255),
  constraint pk_ihsuser primary key (userID))
;

create table ihsvolume (
  volumeID                  integer not null,
  titleID                   integer,
  volumeNumber              varchar(255),
  vlmStartDate              timestamp,
  vlmEndDate                timestamp,
  constraint pk_ihsvolume primary key (volumeID))
;

create table ihssourcepublicationrange (
  sourcePublicationRangeID  integer not null,
  ingestionRecordID         integer,
  titleID                   integer,
  periodicityTypeID         integer,
  sPbrStartDate             integer,
  sPbrEndDate               integer,
  constraint pk_ihssourcepublicationrange primary key (sourcePublicationRangeID))
;

create table person (
  id                        varchar(255) not null,
  name                      varchar(255),
  constraint pk_person primary key (id))
;

create table PholdingCondition (
  holdingConditionID        integer not null,
  holdingID                 integer,
  conditionTypeID           integer,
  constraint pk_PholdingCondition primary key (holdingConditionID))
;

create table psourcetitlekeywords (
  sourceTitleKeywordID      integer not null,
  ingestion_record_id       integer,
  title_id                  integer,
  keyword_id                integer,
  constraint pk_psourcetitlekeywords primary key (sourceTitleKeywordID))
;

create table scategory (
  categoryID                integer not null,
  name                      varchar(255),
  description               varchar(255),
  category_parent_id        integer,
  constraint pk_scategory primary key (categoryID))
;

create table sconditiontype (
  conditionTypeID           integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_sconditiontype primary key (conditionTypeID))
;

create table sholdingstatus (
  holdingStatusID           integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_sholdingstatus primary key (holdingStatusID))
;

create table singestiondatatype (
  ingestionDataTypeID       integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_singestiondatatype primary key (ingestionDataTypeID))
;

create table singestionexceptionstatus (
  ingestionExceptionStatusID integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_singestionexceptionstatus primary key (ingestionExceptionStatusID))
;

create table singestionexceptiontype (
  ingestionExceptionTypeID  integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_singestionexceptiontype primary key (ingestionExceptionTypeID))
;

create table singestionjobstatus (
  ingestionJobStatusID      integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_singestionjobstatus primary key (ingestionJobStatusID))
;

create table singestionrecordstatus (
  ingestionRecordStatusID   integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_singestionrecordstatus primary key (ingestionRecordStatusID))
;

create table sissuestatus (
  issueStatusID             integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_sissuestatus primary key (issueStatusID))
;

create table smemberstatus (
  memberStatusID            integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_smemberstatus primary key (memberStatusID))
;

create table speriodicitytype (
  periodicityTypeID         integer not null,
  name                      varchar(255),
  description               varchar(255),
  intervalsPerYear          integer,
  constraint pk_speriodicitytype primary key (periodicityTypeID))
;

create table stitlestatus (
  titleStatusID             integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_stitlestatus primary key (titleStatusID))
;

create table stitletype (
  titleTypeID               integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_stitletype primary key (titleTypeID))
;

create table suserstatus (
  userStatusID              integer not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_suserstatus primary key (userStatusID))
;

create table ihskeywordconflictresolution (
  keywordConflictResolutionID integer not null,
  title_keyword_id          integer,
  prevailing_source_title_keyword_id integer,
  overridden_source_title_keyword_id integer,
  constraint pk_ihskeywordconflictresolution primary key (keywordConflictResolutionID))
;

create table ihspubrangeconflictresolution (
  pubRangeConflictResolutionID integer not null,
  publication_range_id      integer,
  prevailing_source_pub_range_id integer,
  overridden_source_pub_range_id integer,
  constraint pk_ihspubrangeconflictresolution primary key (pubRangeConflictResolutionID))
;

create table ihstitleconflictresolution (
  titleConflictResolutionID integer not null,
  title_id                  integer,
  title_field_id            integer,
  prevailing_source_title_id integer,
  overridden_source_title_id integer,
  constraint pk_ihstitleconflictresolution primary key (titleConflictResolutionID))
;

create table ppaprholdingtitlemember (
  paprHoldingTitleMember    integer not null,
  papr_holding_id           integer,
  title_id                  integer,
  member_id                 integer,
  constraint pk_ppaprholdingtitlemember primary key (paprHoldingTitleMember))
;

create table ppaprtitletile (
  papr_title_title_id       integer not null,
  papr_title_id             integer,
  title_id                  integer,
  constraint pk_ppaprtitletile primary key (papr_title_title_id))
;

create table psourcetitlecategories (
  source_title_category_id  integer not null,
  ingestion_record_id       integer,
  title_id                  integer,
  category_id               integer,
  constraint pk_psourcetitlecategories primary key (source_title_category_id))
;

create table ptitlecategories (
  titleCategoryID           integer not null,
  title_id                  integer,
  category_id               integer,
  constraint pk_ptitlecategories primary key (titleCategoryID))
;

create table ptitlekeywords (
  keywordID                 integer not null,
  keyword                   varchar(255),
  constraint pk_ptitlekeywords primary key (keywordID))
;

create table ptitlelink (
  title_link_id             integer not null,
  title_parent_id           integer,
  title_child_id            integer,
  constraint pk_ptitlelink primary key (title_link_id))
;

create table stitlefield (
  endDate                   integer not null,
  field_name                integer,
  field_enum_value          varchar(255),
  constraint pk_stitlefield primary key (endDate))
;


create table ihsholding_sconditiontype (
  ihsholding_holdingID           integer not null,
  sconditiontype_conditionTypeID integer not null,
  constraint pk_ihsholding_sconditiontype primary key (ihsholding_holdingID, sconditiontype_conditionTypeID))
;

create table ihsuser_ihssecurityrole (
  ihsuser_userID                 integer not null,
  ihssecurityrole_securityRoleId integer not null,
  constraint pk_ihsuser_ihssecurityrole primary key (ihsuser_userID, ihssecurityrole_securityRoleId))
;
create sequence ihsaddress_seq;

create sequence ihsauthorizedsource_seq;

create sequence ihscategoryconflictresolution_seq;

create sequence ihsholding_seq;

create sequence ihsholdingnote_seq;

create sequence ihsingestionexception_seq;

create sequence ihsingestionjob_seq;

create sequence ihsingestionrecord_seq;

create sequence ihsissue_seq;

create sequence ihslocation_seq;

create sequence ihsmember_seq;

create sequence ihspublicationrange_seq;

create sequence ihspublisher_seq;

create sequence ihssecurityrole_seq;

create sequence ihssourcetitle_seq;

create sequence ihstitle_seq;

create sequence ihsuser_seq;

create sequence ihsvolume_seq;

create sequence ihssourcepublicationrange_seq;

create sequence person_seq;

create sequence PholdingCondition_seq;

create sequence psourcetitlekeywords_seq;

create sequence scategory_seq;

create sequence sconditiontype_seq;

create sequence sholdingstatus_seq;

create sequence singestiondatatype_seq;

create sequence singestionexceptionstatus_seq;

create sequence singestionexceptiontype_seq;

create sequence singestionjobstatus_seq;

create sequence singestionrecordstatus_seq;

create sequence sissuestatus_seq;

create sequence smemberstatus_seq;

create sequence speriodicitytype_seq;

create sequence stitlestatus_seq;

create sequence stitletype_seq;

create sequence suserstatus_seq;

create sequence ihskeywordconflictresolution_seq;

create sequence ihspubrangeconflictresolution_seq;

create sequence ihstitleconflictresolution_seq;

create sequence ppaprholdingtitlemember_seq;

create sequence ppaprtitletile_seq;

create sequence psourcetitlecategories_seq;

create sequence ptitlecategories_seq;

create sequence ptitlekeywords_seq;

create sequence ptitlelink_seq;

create sequence stitlefield_seq;

alter table ihsauthorizedsource add constraint fk_ihsauthorizedsource_ihsMemb_1 foreign key (memberId) references ihsmember (memberID) on delete restrict on update restrict;
create index ix_ihsauthorizedsource_ihsMemb_1 on ihsauthorizedsource (memberId);
alter table ihsholding add constraint fk_ihsholding_ihsIssue_2 foreign key (issueID) references ihsissue (issueID) on delete restrict on update restrict;
create index ix_ihsholding_ihsIssue_2 on ihsholding (issueID);
alter table ihsholding add constraint fk_ihsholding_ihsMember_3 foreign key (memberID) references ihsmember (memberID) on delete restrict on update restrict;
create index ix_ihsholding_ihsMember_3 on ihsholding (memberID);
alter table ihsholding add constraint fk_ihsholding_ihsLocation_4 foreign key (locationID) references ihslocation (locationID) on delete restrict on update restrict;
create index ix_ihsholding_ihsLocation_4 on ihsholding (locationID);
alter table ihsholding add constraint fk_ihsholding_sholdingStatus_5 foreign key (holdingStatusID) references sholdingstatus (holdingStatusID) on delete restrict on update restrict;
create index ix_ihsholding_sholdingStatus_5 on ihsholding (holdingStatusID);
alter table ihsholdingnote add constraint fk_ihsholdingnote_ihsholding_6 foreign key (holdingID) references ihsholding (holdingID) on delete restrict on update restrict;
create index ix_ihsholdingnote_ihsholding_6 on ihsholdingnote (holdingID);
alter table ihsingestionexception add constraint fk_ihsingestionexception_ihsIn_7 foreign key (ingestionRecordID) references ihsingestionrecord (ingestionRecordID) on delete restrict on update restrict;
create index ix_ihsingestionexception_ihsIn_7 on ihsingestionexception (ingestionRecordID);
alter table ihsingestionexception add constraint fk_ihsingestionexception_singe_8 foreign key (ingestionExceptionTypeID) references singestionexceptiontype (ingestionExceptionTypeID) on delete restrict on update restrict;
create index ix_ihsingestionexception_singe_8 on ihsingestionexception (ingestionExceptionTypeID);
alter table ihsingestionexception add constraint fk_ihsingestionexception_singe_9 foreign key (ingestionExceptionStatusID) references singestionexceptionstatus (ingestionExceptionStatusID) on delete restrict on update restrict;
create index ix_ihsingestionexception_singe_9 on ihsingestionexception (ingestionExceptionStatusID);
alter table ihsingestionexception add constraint fk_ihsingestionexception_ishU_10 foreign key (userID) references ihsuser (userID) on delete restrict on update restrict;
create index ix_ihsingestionexception_ishU_10 on ihsingestionexception (userID);
alter table ihsingestionjob add constraint fk_ihsingestionjob_authorized_11 foreign key (authorizedSourceID) references ihsauthorizedsource (authorizedSourceID) on delete restrict on update restrict;
create index ix_ihsingestionjob_authorized_11 on ihsingestionjob (authorizedSourceID);
alter table ihsingestionjob add constraint fk_ihsingestionjob_singestion_12 foreign key (ingestionDataTypeID) references singestiondatatype (ingestionDataTypeID) on delete restrict on update restrict;
create index ix_ihsingestionjob_singestion_12 on ihsingestionjob (ingestionDataTypeID);
alter table ihsingestionjob add constraint fk_ihsingestionjob_ihsUser_13 foreign key (ingestedByUserID) references ihsuser (userID) on delete restrict on update restrict;
create index ix_ihsingestionjob_ihsUser_13 on ihsingestionjob (ingestedByUserID);
alter table ihsingestionjob add constraint fk_ihsingestionjob_singestion_14 foreign key (ingestionJobStatusID) references singestionjobstatus (ingestionJobStatusID) on delete restrict on update restrict;
create index ix_ihsingestionjob_singestion_14 on ihsingestionjob (ingestionJobStatusID);
alter table ihsingestionrecord add constraint fk_ihsingestionrecord_ihsInge_15 foreign key (ingestionJobID) references ihsingestionjob (ingestionJobID) on delete restrict on update restrict;
create index ix_ihsingestionrecord_ihsInge_15 on ihsingestionrecord (ingestionJobID);
alter table ihsingestionrecord add constraint fk_ihsingestionrecord_singest_16 foreign key (ingestionRecordStatusID) references singestionrecordstatus (ingestionRecordStatusID) on delete restrict on update restrict;
create index ix_ihsingestionrecord_singest_16 on ihsingestionrecord (ingestionRecordStatusID);
alter table ihsingestionrecord add constraint fk_ihsingestionrecord_ihsUser_17 foreign key (userID) references ihsuser (userID) on delete restrict on update restrict;
create index ix_ihsingestionrecord_ihsUser_17 on ihsingestionrecord (userID);
alter table ihsingestionrecord add constraint fk_ihsingestionrecord_locIshU_18 foreign key (lockUserID) references ihsuser (userID) on delete restrict on update restrict;
create index ix_ihsingestionrecord_locIshU_18 on ihsingestionrecord (lockUserID);
alter table ihsissue add constraint fk_ihsissue_ihsTitle_19 foreign key (titleID) references ihstitle (titleID) on delete restrict on update restrict;
create index ix_ihsissue_ihsTitle_19 on ihsissue (titleID);
alter table ihsissue add constraint fk_ihsissue_ihsVolume_20 foreign key (volumeID) references ihsvolume (volumeID) on delete restrict on update restrict;
create index ix_ihsissue_ihsVolume_20 on ihsissue (volumeID);
alter table ihsissue add constraint fk_ihsissue_ihsPublicationRan_21 foreign key (publicationRangeID) references ihspublicationrange (publicationRangeID) on delete restrict on update restrict;
create index ix_ihsissue_ihsPublicationRan_21 on ihsissue (publicationRangeID);
alter table ihsissue add constraint fk_ihsissue_sissueStatus_22 foreign key (issueStatusID) references sissuestatus (issueStatusID) on delete restrict on update restrict;
create index ix_ihsissue_sissueStatus_22 on ihsissue (issueStatusID);
alter table ihslocation add constraint fk_ihslocation_ihsMember_23 foreign key (memberId) references ihsmember (memberID) on delete restrict on update restrict;
create index ix_ihslocation_ihsMember_23 on ihslocation (memberId);
alter table ihslocation add constraint fk_ihslocation_ihsAddress_24 foreign key (addressId) references ihsaddress (addressID) on delete restrict on update restrict;
create index ix_ihslocation_ihsAddress_24 on ihslocation (addressId);
alter table ihsmember add constraint fk_ihsmember_smemberStatus_25 foreign key (memberStatusID) references smemberstatus (memberStatusID) on delete restrict on update restrict;
create index ix_ihsmember_smemberStatus_25 on ihsmember (memberStatusID);
alter table ihspublicationrange add constraint fk_ihspublicationrange_ihsTit_26 foreign key (titleID) references ihstitle (titleID) on delete restrict on update restrict;
create index ix_ihspublicationrange_ihsTit_26 on ihspublicationrange (titleID);
alter table ihspublicationrange add constraint fk_ihspublicationrange_sperio_27 foreign key (periodicityTypeID) references speriodicitytype (periodicityTypeID) on delete restrict on update restrict;
create index ix_ihspublicationrange_sperio_27 on ihspublicationrange (periodicityTypeID);
alter table ihspublisher add constraint fk_ihspublisher_ihsAddress_28 foreign key (addressId) references ihsaddress (addressID) on delete restrict on update restrict;
create index ix_ihspublisher_ihsAddress_28 on ihspublisher (addressId);
alter table ihssourcetitle add constraint fk_ihssourcetitle_ihsIngestio_29 foreign key (ingestionRecordID) references ihsingestionrecord (ingestionRecordID) on delete restrict on update restrict;
create index ix_ihssourcetitle_ihsIngestio_29 on ihssourcetitle (ingestionRecordID);
alter table ihssourcetitle add constraint fk_ihssourcetitle_ihsTitle_30 foreign key (titleID) references ihstitle (titleID) on delete restrict on update restrict;
create index ix_ihssourcetitle_ihsTitle_30 on ihssourcetitle (titleID);
alter table ihssourcetitle add constraint fk_ihssourcetitle_stitleType_31 foreign key (titleTypeID) references stitletype (titleTypeID) on delete restrict on update restrict;
create index ix_ihssourcetitle_stitleType_31 on ihssourcetitle (titleTypeID);
alter table ihssourcetitle add constraint fk_ihssourcetitle_ihsPublishe_32 foreign key (publisherID) references ihspublisher (publisherID) on delete restrict on update restrict;
create index ix_ihssourcetitle_ihsPublishe_32 on ihssourcetitle (publisherID);
alter table ihstitle add constraint fk_ihstitle_stitleType_33 foreign key (titleTypeID) references stitletype (titleTypeID) on delete restrict on update restrict;
create index ix_ihstitle_stitleType_33 on ihstitle (titleTypeID);
alter table ihstitle add constraint fk_ihstitle_ihsPublisher_34 foreign key (publisherID) references ihspublisher (publisherID) on delete restrict on update restrict;
create index ix_ihstitle_ihsPublisher_34 on ihstitle (publisherID);
alter table ihstitle add constraint fk_ihstitle_stitleStatus_35 foreign key (titleStatusID) references stitlestatus (titleStatusID) on delete restrict on update restrict;
create index ix_ihstitle_stitleStatus_35 on ihstitle (titleStatusID);
alter table ihsuser add constraint fk_ihsuser_ihsMember_36 foreign key (memberId) references ihsmember (memberID) on delete restrict on update restrict;
create index ix_ihsuser_ihsMember_36 on ihsuser (memberId);
alter table ihsuser add constraint fk_ihsuser_suserStatus_37 foreign key (userStatusID) references suserstatus (userStatusID) on delete restrict on update restrict;
create index ix_ihsuser_suserStatus_37 on ihsuser (userStatusID);
alter table ihsvolume add constraint fk_ihsvolume_ihsTitle_38 foreign key (titleID) references ihstitle (titleID) on delete restrict on update restrict;
create index ix_ihsvolume_ihsTitle_38 on ihsvolume (titleID);
alter table ihssourcepublicationrange add constraint fk_ihssourcepublicationrange__39 foreign key (ingestionRecordID) references ihsingestionrecord (ingestionRecordID) on delete restrict on update restrict;
create index ix_ihssourcepublicationrange__39 on ihssourcepublicationrange (ingestionRecordID);
alter table ihssourcepublicationrange add constraint fk_ihssourcepublicationrange__40 foreign key (titleID) references ihstitle (titleID) on delete restrict on update restrict;
create index ix_ihssourcepublicationrange__40 on ihssourcepublicationrange (titleID);
alter table ihssourcepublicationrange add constraint fk_ihssourcepublicationrange__41 foreign key (periodicityTypeID) references speriodicitytype (periodicityTypeID) on delete restrict on update restrict;
create index ix_ihssourcepublicationrange__41 on ihssourcepublicationrange (periodicityTypeID);
alter table PholdingCondition add constraint fk_PholdingCondition_ihsHoldi_42 foreign key (holdingID) references ihsholding (holdingID) on delete restrict on update restrict;
create index ix_PholdingCondition_ihsHoldi_42 on PholdingCondition (holdingID);
alter table PholdingCondition add constraint fk_PholdingCondition_sconditi_43 foreign key (conditionTypeID) references sconditiontype (conditionTypeID) on delete restrict on update restrict;
create index ix_PholdingCondition_sconditi_43 on PholdingCondition (conditionTypeID);



alter table ihsholding_sconditiontype add constraint fk_ihsholding_sconditiontype__01 foreign key (ihsholding_holdingID) references ihsholding (holdingID) on delete restrict on update restrict;

alter table ihsholding_sconditiontype add constraint fk_ihsholding_sconditiontype__02 foreign key (sconditiontype_conditionTypeID) references sconditiontype (conditionTypeID) on delete restrict on update restrict;

alter table ihsuser_ihssecurityrole add constraint fk_ihsuser_ihssecurityrole_ih_01 foreign key (ihsuser_userID) references ihsuser (userID) on delete restrict on update restrict;

alter table ihsuser_ihssecurityrole add constraint fk_ihsuser_ihssecurityrole_ih_02 foreign key (ihssecurityrole_securityRoleId) references ihssecurityrole (securityRoleId) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists ihsaddress;

drop table if exists ihsauthorizedsource;

drop table if exists ihscategoryconflictresolution;

drop table if exists ihsholding;

drop table if exists ihsholding_sconditiontype;

drop table if exists ihsholdingnote;

drop table if exists ihsingestionexception;

drop table if exists ihsingestionjob;

drop table if exists ihsingestionrecord;

drop table if exists ihsissue;

drop table if exists ihslocation;

drop table if exists ihsmember;

drop table if exists ihspublicationrange;

drop table if exists ihspublisher;

drop table if exists ihssecurityrole;

drop table if exists ihssourcetitle;

drop table if exists ihstitle;

drop table if exists ihsuser;

drop table if exists ihsuser_ihssecurityrole;

drop table if exists ihsvolume;

drop table if exists ihssourcepublicationrange;

drop table if exists person;

drop table if exists PholdingCondition;

drop table if exists psourcetitlekeywords;

drop table if exists scategory;

drop table if exists sconditiontype;

drop table if exists sholdingstatus;

drop table if exists singestiondatatype;

drop table if exists singestionexceptionstatus;

drop table if exists singestionexceptiontype;

drop table if exists singestionjobstatus;

drop table if exists singestionrecordstatus;

drop table if exists sissuestatus;

drop table if exists smemberstatus;

drop table if exists speriodicitytype;

drop table if exists stitlestatus;

drop table if exists stitletype;

drop table if exists suserstatus;

drop table if exists ihskeywordconflictresolution;

drop table if exists ihspubrangeconflictresolution;

drop table if exists ihstitleconflictresolution;

drop table if exists ppaprholdingtitlemember;

drop table if exists ppaprtitletile;

drop table if exists psourcetitlecategories;

drop table if exists ptitlecategories;

drop table if exists ptitlekeywords;

drop table if exists ptitlelink;

drop table if exists stitlefield;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists ihsaddress_seq;

drop sequence if exists ihsauthorizedsource_seq;

drop sequence if exists ihscategoryconflictresolution_seq;

drop sequence if exists ihsholding_seq;

drop sequence if exists ihsholdingnote_seq;

drop sequence if exists ihsingestionexception_seq;

drop sequence if exists ihsingestionjob_seq;

drop sequence if exists ihsingestionrecord_seq;

drop sequence if exists ihsissue_seq;

drop sequence if exists ihslocation_seq;

drop sequence if exists ihsmember_seq;

drop sequence if exists ihspublicationrange_seq;

drop sequence if exists ihspublisher_seq;

drop sequence if exists ihssecurityrole_seq;

drop sequence if exists ihssourcetitle_seq;

drop sequence if exists ihstitle_seq;

drop sequence if exists ihsuser_seq;

drop sequence if exists ihsvolume_seq;

drop sequence if exists ihssourcepublicationrange_seq;

drop sequence if exists person_seq;

drop sequence if exists PholdingCondition_seq;

drop sequence if exists psourcetitlekeywords_seq;

drop sequence if exists scategory_seq;

drop sequence if exists sconditiontype_seq;

drop sequence if exists sholdingstatus_seq;

drop sequence if exists singestiondatatype_seq;

drop sequence if exists singestionexceptionstatus_seq;

drop sequence if exists singestionexceptiontype_seq;

drop sequence if exists singestionjobstatus_seq;

drop sequence if exists singestionrecordstatus_seq;

drop sequence if exists sissuestatus_seq;

drop sequence if exists smemberstatus_seq;

drop sequence if exists speriodicitytype_seq;

drop sequence if exists stitlestatus_seq;

drop sequence if exists stitletype_seq;

drop sequence if exists suserstatus_seq;

drop sequence if exists ihskeywordconflictresolution_seq;

drop sequence if exists ihspubrangeconflictresolution_seq;

drop sequence if exists ihstitleconflictresolution_seq;

drop sequence if exists ppaprholdingtitlemember_seq;

drop sequence if exists ppaprtitletile_seq;

drop sequence if exists psourcetitlecategories_seq;

drop sequence if exists ptitlecategories_seq;

drop sequence if exists ptitlekeywords_seq;

drop sequence if exists ptitlelink_seq;

drop sequence if exists stitlefield_seq;

