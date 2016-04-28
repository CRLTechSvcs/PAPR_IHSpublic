DROP DATABASE IF EXISTS `crl`;

CREATE DATABASE `crl`;

SET NAMES 'utf8';

SET CHARACTER SET utf8;

use `crl`;


-- Create a table for address objects

CREATE TABLE `crl`.`ihsaddress` (
	`addressID`				INT(11)			NOT NULL AUTO_INCREMENT,
	`address1`				VARCHAR(256)	NOT NULL,
	`address2`				VARCHAR(256)	DEFAULT NULL,
	`city`					VARCHAR(256)	NOT NULL,
	`stateOrProvence`		VARCHAR(128)	NOT NULL,
	`postalCode`			VARCHAR(10)		DEFAULT NULL,
	`zipPlus`				VARCHAR(4)		DEFAULT NULL,
	`country`				VARCHAR(15)		NOT NULL,
	`createdTS`				TIMESTAMP		NOT NULL DEFAULT CURRENT_TIMESTAMP,
		
	PRIMARY KEY (`addressID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for member status types

CREATE TABLE `crl`.`smemberstatus` (
	`memberStatusID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`memberStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `crl`.`ihsmembergroup` (
	`membergroupID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`membergroupID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for member objects

CREATE TABLE `crl`.`ihsmember` (
	`memberID`			 	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`name`			 		VARCHAR(128) 	NOT NULL, 
	`description`	 		VARCHAR(256)	NULL,
	`memberStatusID`		INT(11)			NOT NULL,
	`addressID`		 		INT(11)			NOT NULL, 
        `ftpdirectory`                  VARCHAR(256)	NULL,
	`membergroupID`		INT(11) NOT NULL,
		
	PRIMARY KEY (`memberID`),
	
	KEY `fk_ihsmember_ihsaddress_addressID` (`addressID`),
	CONSTRAINT `fk_ihsmember_ihsaddress_addressID` FOREIGN KEY (`addressID`) REFERENCES `ihsaddress` (`addressID`),
		
	KEY `fk_ihsmember_smemberstatus_memberStatusID` (`memberStatusID`),
	CONSTRAINT `fk_ihsmember_smemberstatus_memberStatusID` FOREIGN KEY (`memberStatusID`) REFERENCES `smemberstatus` (`memberStatusID`),

	KEY `fk_ihsmember_membergroup_membergroupID` (`membergroupID`),
	CONSTRAINT `fk_ihsmember_membergroup_membergroupID` FOREIGN KEY (`membergroupID`) REFERENCES `ihsmembergroup` (`membergroupID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;




-- Create a table for location objects

CREATE TABLE `crl`.`ihslocation` (
	`locationID`		 	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`name`			 		VARCHAR(128) 	NOT NULL, 
	`description`	 		VARCHAR(256)	NULL,
	`memberID`				INT(11)			NOT NULL,
	`addressID`		 		INT(11)			NOT NULL, 
		
	PRIMARY KEY (`locationID`),
		
	KEY `fk_ihslocation_ihsmember_memberID` (`memberID`),
	CONSTRAINT `fk_ihslocation_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`),
	
	KEY `fk_ihslocation_ihsaddress_addressID` (`addressID`),
	CONSTRAINT `fk_ihslocation_ihsaddress_addressID` FOREIGN KEY (`addressID`) REFERENCES `ihsaddress` (`addressID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for user status types

CREATE TABLE `crl`.`suserstatus` (
	`userStatusID`			INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`userStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for user objects

CREATE TABLE `crl`.`ihsuser` (
	`userID`			 	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`firstName`		 		VARCHAR(128) 	NOT NULL, 
	`lastName`		 		VARCHAR(128) 	NOT NULL, 
	`memberID`				INT(11)			NOT NULL,
	`userStatusID`			INT(11)			NOT NULL,
	`userName`                           VARCHAR(16) 	NOT NULL,
	`password`                           VARCHAR(128) 	NOT NULL, 
		
	PRIMARY KEY (`userID`),
	
	KEY `fk_ihsuser_ihsmember_memberID` (`memberID`),
	CONSTRAINT `fk_ihsuser_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`),
		
	KEY `fk_ihsuser_suserstatus_userStatusID` (`userStatusID`),
	CONSTRAINT `fk_ihsuser_suserstatus_userStatusID` FOREIGN KEY (`userStatusID`) REFERENCES `suserstatus` (`userStatusID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for authorized source objects

CREATE TABLE `crl`.`ihsauthorizedsource` (
	`authorizedSourceID`	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`name`			 		VARCHAR(128) 	NOT NULL, 
	`description`	 		VARCHAR(256)	NULL,
	`memberID`				INT(11)			NULL,
		
	PRIMARY KEY (`authorizedSourceID`),
	
	KEY `fk_ihsauthorizedsource_ihsmember_memberID` (`memberID`),
	CONSTRAINT `fk_ihsauthorizedsource_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for source types

CREATE TABLE `crl`.`singestiondatatype` (
	`ingestionDataTypeID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`ingestionDataTypeID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for ingestion job statuses

CREATE TABLE `crl`.`singestionjobstatus` (
	`ingestionJobStatusID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`ingestionJobStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `crl`.`scommitment` (
	`commitmentID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`commitmentID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;

-- Create a table for ingestion job objects

CREATE TABLE `crl`.`ihsingestionjob` (
	`ingestionJobID`		INT(11) 		NOT NULL AUTO_INCREMENT, 
	`name`			 		VARCHAR(128) 	NOT NULL, 
	`description`	 		VARCHAR(256)	NULL,
	`authorizedSourceID`	INT(11)			NOT NULL,
	`ingestionDataTypeID`	INT(11)			NOT NULL, 
	`creationDate` 			TIMESTAMP		NOT NULL,
	`ingestedByUserID`		INT(11)			NOT NULL,
	`sourceFileString`		VARCHAR(256)	NULL,
	`ingestionJobStatusID`	INT(11)			NOT NULL,
	`statusDetail`			VARCHAR(256)	NULL,	
	`commitmentID`     	INT(11),
	PRIMARY KEY (`ingestionJobID`),
	
	KEY `fk_ihsingestionjob_ihsauthorizedsource_authorizedSourceID` (`authorizedSourceID`),
	CONSTRAINT `fk_ihsingestionjob_ihsauthorizedsource_authorizedSourceID` FOREIGN KEY (`authorizedSourceID`) REFERENCES `ihsauthorizedsource` (`authorizedSourceID`),
		
	KEY `fk_ihsingestionjob_singestiondatatype_ingestionDataTypeID` (`ingestionDataTypeID`),
	CONSTRAINT `fk_ihsingestionjob_singestiondatatype_ingestionDataTypeID` FOREIGN KEY (`ingestionDataTypeID`) REFERENCES `singestiondatatype` (`ingestionDataTypeID`),
	
	KEY `fk_ihsingestionjob_ihsuser_ingestedByUserID` (`ingestedByUserID`),
	CONSTRAINT `fk_ihsingestionjob_ihsuser_ingestedByUserID` FOREIGN KEY (`ingestedByUserID`) REFERENCES `ihsuser` (`userID`),
	
	KEY `fk_ihsingestionjob_singestionjobstatus_ingestionJobStatusID` (`ingestionJobStatusID`),
	CONSTRAINT `fk_ihsingestionjob_singestionjobstatus_ingestionJobStatusID` FOREIGN KEY (`ingestionJobStatusID`) REFERENCES `singestionjobstatus` (`ingestionJobStatusID`),
	
	KEY `fk_ihsingestionjob_sihsVarified_commitmentID` (`commitmentID`),
	CONSTRAINT `fk_ihsingestionjob_sihsVarified_commitmentID` FOREIGN KEY (`commitmentID`) REFERENCES `scommitment` (`commitmentID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for ingestion record statuses

CREATE TABLE `crl`.`singestionrecordstatus` (
	`ingestionRecordStatusID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`ingestionRecordStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for ingestion record objects

CREATE TABLE `crl`.`ihsingestionrecord` (
	`ingestionRecordID`		INT(11) 		NOT NULL AUTO_INCREMENT, 
	`ingestionJobID`		INT(11) 		NOT NULL, 
	`rawRecordData`	 		TEXT			NOT NULL, 
	`ingestionRecordStatusID`	INT(11)			NOT NULL,
        `recordTitle`			VARCHAR(256)	,
	`issues`				VARCHAR(256)	,
	`jsonRecordData`	        TEXT		,
        `userID`			INT(11)			NULL,
	`lockUserID`			INT(11)			NULL,
	`lockDate` 				TIMESTAMP		NULL,
	PRIMARY KEY (`ingestionRecordID`),
	
	KEY `fk_ihsingestionrecord_ihsingestionjob_ingestionJobID` (`ingestionJobID`),
	CONSTRAINT `fk_ihsingestionrecord_ihsingestionjob_ingestionJobID` FOREIGN KEY (`ingestionJobID`) REFERENCES `ihsingestionjob` (`ingestionJobID`),
	
	KEY `fk_ihsingestionrecord_singestionrecordstatus_ingestionRecordStat` (`ingestionRecordStatusID`),
	CONSTRAINT `fk_ihsingestionrecord_singestionrecordstatus_ingestionRecordStat` FOREIGN KEY (`ingestionRecordStatusID`) REFERENCES `singestionrecordstatus` (`ingestionRecordStatusID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for ingestion exception types

CREATE TABLE `crl`.`singestionexceptiontype` (
	`ingestionExceptionTypeID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`ingestionExceptionTypeID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for ingestion exception statuses

CREATE TABLE `crl`.`singestionexceptionstatus` (
	`ingestionExceptionStatusID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`ingestionExceptionStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for ingestion exception objects

CREATE TABLE `crl`.`ihsingestionexception` (
	`ingestionExceptionID`	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`ingestionRecordID`		INT(11) 		NOT NULL, 
	`ingestionExceptionTypeID`	INT(11) 		NOT NULL, 
	`recordTitle`			VARCHAR(256)	NOT NULL,
	`issues`				VARCHAR(256)	NOT NULL,
	`ingestionExceptionStatusID`	INT(11) 		NOT NULL, 
	`userID`			INT(11)			NULL,
	`lockDate` 				TIMESTAMP		NULL,
		
	PRIMARY KEY (`ingestionExceptionID`),
	
	KEY `fk_ihsingestionexception_ihsingestionrecord_ingestionRecordID` (`ingestionRecordID`),
	CONSTRAINT `fk_ihsingestionexception_ihsingestionrecord_ingestionRecordID` FOREIGN KEY (`ingestionRecordID`) REFERENCES `ihsingestionrecord` (`ingestionRecordID`),
	
	KEY `fk_ihsingestionexception_singestionexceptiontype_ingestionExcept` (`ingestionExceptionTypeID`),
	CONSTRAINT `fk_ihsingestionexception_singestionexceptiontype_ingestionExcept` FOREIGN KEY (`ingestionExceptionTypeID`) REFERENCES `singestionexceptiontype` (`ingestionExceptionTypeID`),
	
	KEY `fk_ihsingestionexception_singestionexceptionstatus_ingestionExce` (`ingestionExceptionStatusID`),
	CONSTRAINT `fk_ihsingestionexception_singestionexceptionstatus_ingestionExce` FOREIGN KEY (`ingestionExceptionStatusID`) REFERENCES `singestionexceptionstatus` (`ingestionExceptionStatusID`),
	
	KEY `fk_ihsingestionexception_ihsuser_userID` (`userID`),
	CONSTRAINT `fk_ihsingestionexception_ihsuser_userID` FOREIGN KEY (`userID`) REFERENCES `ihsuser` (`userID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for publisher objects

CREATE TABLE `crl`.`ihspublisher` (
	`publisherID`		 	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`name`			 		VARCHAR(256) 	NOT NULL, 
	`description`	 		VARCHAR(256)	NULL, 
	`startDate` 			DATE		NOT NULL, 
	`endDate`	 			DATE	 	NULL, 
	`addressID`		 		INT(11)			NOT NULL, 
		
	PRIMARY KEY (`publisherID`),
	
	KEY `fk_ihspublisher_ihsaddress_addressID` (`addressID`),
	CONSTRAINT `fk_ihspublisher_ihsaddress_addressID` FOREIGN KEY (`addressID`) REFERENCES `ihsaddress` (`addressID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for title types

CREATE TABLE `crl`.`stitletype` (
	`titleTypeID`			INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`titleTypeID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for title statuses

CREATE TABLE `crl`.`stitlestatus` (
	`titleStatusID`			INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`titleStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;




CREATE TABLE `crl`.`ihstitle` (
	`titleID`			 	INT(11) 		NOT NULL AUTO_INCREMENT, 
	`titleTypeID`	 		INT(11)		 	NOT NULL, 
	`title`			 		VARCHAR(512) 	NOT NULL, 
	`alphaTitle` 			VARCHAR(512) 	NULL, 
	`printISSN`	 			VARCHAR(32) 	NULL, 
	`eISSN`		 			VARCHAR(32) 	NULL, 
	`oclcNumber` 			VARCHAR(32) 	NULL, 
	`lccn`					VARCHAR(32)		NULL,
	`publisherID` 			INT(11)		 	NOT NULL, 
	`description` 			VARCHAR(512) 	NULL, 
	`titleStatusID` 		INT(11)		 	NOT NULL, 
	`changeDate`			DATE not null,
	`userId`			INT(11) not null,
	`titleVersion`                  INT(11) DEFAULT 0,
	`imagePageRatio`		INT(11), 
	`language`			VARCHAR(32) 	NOT NULL,
	`country`			VARCHAR(32) 	NOT NULL,
	
	PRIMARY KEY (`titleID`),

    FULLTEXT KEY `title_ft` (`title`),
	
	INDEX  `printISSN_FI_1` (`printISSN`),  
	INDEX  `oclcNumber_FI_1` (`oclcNumber`),  
	
	KEY `fk_ihstitle_ihspublisher_publisherID` (`publisherID`),
	CONSTRAINT `fk_ihstitle_ihspublisher_publisherID` FOREIGN KEY (`publisherID`) REFERENCES `ihspublisher` (`publisherID`),
	
	KEY `fk_ihstitle_stitletype_titleTypeID` (`titleTypeID`),
	CONSTRAINT `fk_ihstitle_stitletype_titleTypeID` FOREIGN KEY (`titleTypeID`) REFERENCES `stitletype` (`titleTypeID`),
	
	KEY `fk_ihstitle_stitlestatus_titleStatusID` (`titleStatusID`),
	CONSTRAINT `fk_ihstitle_stitlestatus_titleStatusID` FOREIGN KEY (`titleStatusID`) REFERENCES `stitlestatus` (`titleStatusID`)
	

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `crl`.`ihstitleversion` (
	`titleversionID` 		INT(11) 		NOT NULL AUTO_INCREMENT, 
	`titleID`			 	INT(11) , 
	`titleTypeID`	 		INT(11)		 	NOT NULL, 
	`title`			 		VARCHAR(512) 	NOT NULL, 
	`alphaTitle` 			VARCHAR(512) 	NULL, 
	`printISSN`	 			VARCHAR(32) 	NULL, 
	`eISSN`		 			VARCHAR(32) 	NULL, 
	`oclcNumber` 			VARCHAR(32) 	NULL, 
	`lccn`					VARCHAR(32)		NULL,
	`publisherID` 			INT(11)		 	NOT NULL, 
	`description` 			VARCHAR(512) 	NULL, 
	`titleStatusID` 		INT(11)		 	NOT NULL, 
	`changeDate`                    Date,
	`userId`                        INT(11),
	`imagePageRatio`		INT(11), 
	`language`			VARCHAR(32) 	NOT NULL,
	`country`			VARCHAR(32) 	NOT NULL,
 	
	PRIMARY KEY (`titleversionID`),
	INDEX  `printISSN_FI_1` (`printISSN`),  
	INDEX  `oclcNumber_FI_1` (`oclcNumber`),  
	
	KEY `fk_ihstitleversion_ihstitle_titleID` (`titleID`),
	CONSTRAINT `fk_ihstitleversionihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`),

	KEY `fk_ihstitleversion_ihspublisher_publisherID` (`publisherID`),
	CONSTRAINT `fk_ihstitleversion_ihspublisher_publisherID` FOREIGN KEY (`publisherID`) REFERENCES `ihspublisher` (`publisherID`)
	

)	ENGINE=INNODB DEFAULT CHARSET=utf8;

-- Create a pivot table for title links

CREATE TABLE `crl`.`ptitlelink` (
	`titleLinkID`			INT(11)			NOT NULL AUTO_INCREMENT,
	`titleParentID`			INT(11)			NOT NULL,
	`titleChildID`			INT(11)			NOT NULL,
	
	PRIMARY KEY (`titleLinkID`),

	KEY `fk_ptitlelink_ihstitle_titleParentID` (`titleParentID`),
	CONSTRAINT `fk_ptitlelink_ihstitle_titleParentID` FOREIGN KEY (`titleParentID`) REFERENCES `ihstitle` (`titleID`),
	
	KEY `fk_ptitlelink_ihstitle_titleChildID` (`titleChildID`),
	CONSTRAINT `fk_ptitlelink_ihstitle_titleChildID` FOREIGN KEY (`titleChildID`) REFERENCES `ihstitle` (`titleID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for periodicity types

CREATE TABLE `crl`.`speriodicitytype` (
	`periodicityTypeID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(32)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	`intervalsPerYear`		INT(11)			NOT NULL,
	
	PRIMARY KEY (`periodicityTypeID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for title publication range objects

CREATE TABLE `crl`.`ihspublicationrange` (
	`publicationRangeID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`titleID`				INT(11)			NOT NULL,
	`periodicityTypeID`		INT(11)			NOT NULL,
	`pbrStartDate`				DATE		NULL,
	`pbrEndDate`				DATE		NULL,
	
	PRIMARY KEY (`publicationRangeID`),

	KEY `fk_ihspublicationrange_ihstitle_titleID` (`titleID`),
	CONSTRAINT `fk_ihspublicationrange_ihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`),
	
	KEY `fk_ihspublicationrange_speriodicitytype_periodicityTypeID` (`periodicityTypeID`),
	CONSTRAINT `fk_ihspublicationrange_speriodicitytype_periodicityTypeID` FOREIGN KEY (`periodicityTypeID`) REFERENCES `speriodicitytype` (`periodicityTypeID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `crl`.`ihspublicationrangever` (

	`publicationRangeID`	INT(11)			NOT NULL AUTO_INCREMENT,

	`titleversionID`				INT(11)			NOT NULL,

	`periodicityTypeID`		INT(11)			NOT NULL,

	`pbrStartDate`				DATE		NULL,

	`pbrEndDate`				DATE		NULL,


	PRIMARY KEY (`publicationRangeID`),


	KEY `fk_ihspublicationraver_ihstitle_titleID` (`titleversionID`),

	CONSTRAINT `fk_ihspublicationraver_ihstitle_titleID` FOREIGN KEY (`titleversionID`) REFERENCES `ihstitleversion` (`titleversionID`),

	KEY `fk_ihspublicationraver_speriodicitytype_periodicityTypeID` (`periodicityTypeID`),

	CONSTRAINT `fk_ihspublicationraver_speriodicitytype_periodicityTypeID` FOREIGN KEY (`periodicityTypeID`) REFERENCES `speriodicitytype` (`periodicityTypeID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;







-- Create a static table for issue status types

CREATE TABLE `crl`.`sissuestatus` (
	`issueStatusID`			INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`issueStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for volume objects

CREATE TABLE `crl`.`ihsvolume` (
	`volumeID`				INT(11)			NOT NULL AUTO_INCREMENT,
	`titleID`				INT(11)			NOT NULL,
	`volumeNumber`			        VARCHAR(32)		NOT NULL,
	`vlmStartDate`				DATE	,
	`vlmEndDate`				DATE	,
	

	PRIMARY KEY (`volumeID`),
	
	INDEX  `volumeNumber_FI_1` (`titleID`, `volumeNumber`),  
	 
	KEY `fk_ihsvolume_ihstitle_titleID` (`titleID`),
	CONSTRAINT `fk_ihsvolume_ihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for issue objects

CREATE TABLE `crl`.`ihsissue` (
	`issueID`				INT(11)			NOT NULL AUTO_INCREMENT,
	`titleID`				INT(11)			NOT NULL,
	`volumeID`				INT(11)			NULL,
	`publicationRangeID`	INT(11)			NOT NULL,
	`publicationDate`		DATE		,
	`issueNumber`			VARCHAR(32)		NULL,
	`name`					VARCHAR(64)		NULL,
	`description`			VARCHAR(256)	NULL,
	`numPages`				INT(11)			NULL,
	`issueStatusID`			INT(11)			NOT NULL,
-- QUESTION: Is there any type of bibliographic ID number for individual issues that we need to store?
	
	PRIMARY KEY (`issueID`),

	INDEX  `issueNumber_FI_1` (`issueNumber`),

	INDEX  `issueNumber_F2_2` (`titleID`, `volumeID`, `issueNumber`),
		
	KEY `fk_ihsissue_ihstitle_titleID` (`titleID`),
	CONSTRAINT `fk_ihsissue_ihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`),

	KEY `fk_ihsissue_ihsvolume_volumeID` (`volumeID`),
	CONSTRAINT `fk_ihsissue_ihsvolume_volumeID` FOREIGN KEY (`volumeID`) REFERENCES `ihsvolume` (`volumeID`),

	KEY `fk_ihsissue_ihspublicationrange_publicationRangeID` (`publicationRangeID`),
	CONSTRAINT `fk_ihsissue_ihspublicationrange_publicationRangeID` FOREIGN KEY (`publicationRangeID`) REFERENCES `ihspublicationrange` (`publicationRangeID`),

	KEY `fk_ihsissue_sissuestatus_issueStatusID` (`issueStatusID`),
	CONSTRAINT `fk_ihsissue_sissuestatus_issueStatusID` FOREIGN KEY (`issueStatusID`) REFERENCES `sissuestatus` (`issueStatusID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for holding status types

CREATE TABLE `crl`.`sholdingstatus` (
	`holdingStatusID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`holdingStatusID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `crl`.`sconditionTypeOverall` (
	`conditionTypeOverallID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`conditionTypeOverallID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `crl`.`svalidationLevel` (
	`validationLevelID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	`level`				INT(11)	NOT NULL,
	
	PRIMARY KEY (`validationLevelID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;





CREATE TABLE `crl`.`sihsVarified` (
	`ihsVarifiedID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`ihsVarifiedID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;

-- Create a table for holding objects
-- ALTER TABLE `crl`.`ihsholding` ADD `commitmentID`  INT NOT NULL DEFAULT 1;


CREATE TABLE `crl`.`ihsholding` (
	`holdingID`				INT(11)			NOT NULL AUTO_INCREMENT,
	`issueID`				INT(11)			NOT NULL,
	`memberID`				INT(11)			NOT NULL,
	`locationID`			INT(11)			NOT NULL,
	`holdingStatusID`		INT(11)			NOT NULL,
	`conditionTypeOverallID`		INT(11)	 NOT NULL DEFAULT 1,
	`validationLevelID`        INT(11)		NOT NULL DEFAULT 1,
	`ihsVarifiedID`		INT(11)		NOT NULL DEFAULT 1,
	`missingPages`          VARCHAR(128), 
	`commitmentID`     	INT(11) NOT NULL DEFAULT 1,
	

	PRIMARY KEY (`holdingID`),

	INDEX  `ihsholding_F1_1` (`issueID`, `memberID`, `locationID`),

	KEY `fk_ihsholding_ihsissue_issueID` (`issueID`),
	CONSTRAINT `fk_ihsholding_ihsissue_issueID` FOREIGN KEY (`issueID`) REFERENCES `ihsissue` (`issueID`),

	KEY `fk_ihsholding_ihsmember_memberID` (`memberID`),
	CONSTRAINT `fk_ihsholding_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`),

	KEY `fk_ihsholding_ihslocation_locationID` (`locationID`),
	CONSTRAINT `fk_ihsholding_ihslocation_locationID` FOREIGN KEY (`locationID`) REFERENCES `ihslocation` (`locationID`),

	KEY `fk_ihsholding_sholdingstatus_holdingStatusID` (`holdingStatusID`),
	CONSTRAINT `fk_ihsholding_sholdingstatus_holdingStatusID` FOREIGN KEY (`holdingStatusID`) REFERENCES `sholdingstatus` (`holdingStatusID`),
	
	KEY `fk_ihsholding_sconditionTypeOverall_conditionTypeOverallID` (`conditionTypeOverallID`),
	CONSTRAINT `fk_ihsholding_sconditionTypeOverall_conditionTypeOverallID` FOREIGN KEY (`conditionTypeOverallID`) REFERENCES `sconditionTypeOverall` (`conditionTypeOverallID`),
	
	KEY `fk_ihsholding_svalidationLevel_validationLevelID` (`validationLevelID`),
	CONSTRAINT `fk_ihsholding_svalidationLevel_validationLevelID` FOREIGN KEY (`validationLevelID`) REFERENCES `svalidationLevel` (`validationLevelID`),
	
	KEY `fk_ihsholding_sihsVarified_ihsVarifiedID` (`ihsVarifiedID`),
	CONSTRAINT `fk_ihsholding_sihsVarified_ihsVarifiedID` FOREIGN KEY (`ihsVarifiedID`) REFERENCES `sihsVarified` (`ihsVarifiedID`),
	
        KEY `fk_ihsholding_sihsVarified_commitmentID` (`commitmentID`),
	CONSTRAINT `fk_ihsholding_sihsVarified_commitmentID` FOREIGN KEY (`commitmentID`) REFERENCES `scommitment` (`commitmentID`)
	

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a static table for holding condition types

CREATE TABLE `crl`.`sconditiontype` (
	`conditiontypeID`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,
	
	PRIMARY KEY (`conditionTypeID`)

)	ENGINE=INNODB DEFAULT CHARSET=utf8;


create table ihsholding_sconditiontype (
	ihsholding_holdingID           integer not null,
	sconditiontype_conditionTypeID integer not null,
	constraint pk_ihsholding_sconditiontype primary key (ihsholding_holdingID, sconditiontype_conditionTypeID)
) ENGINE=INNODB DEFAULT CHARSET=utf8;



-- Create a pivot table for holding-condition relations

CREATE TABLE `crl`.`pholdingcondition` (
	`holdingConditionID`	INT(11)			NOT NULL AUTO_INCREMENT,
	`holdingID`				INT(11)			NOT NULL,
	`conditionTypeID`		INT(11)			NOT NULL,
	
	PRIMARY KEY (`holdingConditionID`),

	KEY `fk_pholdingcondition_ihsholding_holdingID` (`holdingID`),
	CONSTRAINT `fk_pholdingcondition_ihsholding_holdingID` FOREIGN KEY (`holdingID`) REFERENCES `ihsholding` (`holdingID`),
	
	KEY `fk_pholdingcondition_sconditiontype_conditionTypeID` (`conditionTypeID`),
	CONSTRAINT `fk_pholdingcondition_sconditiontype_conditionTypeID` FOREIGN KEY (`conditionTypeID`) REFERENCES `sconditiontype` (`conditionTypeID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- Create a table for holding note objects

CREATE TABLE `crl`.`ihsholdingnote` (
	`holdingNoteID`			INT(11)			NOT NULL AUTO_INCREMENT,
	`holdingID`				INT(11)			NOT NULL,
	`note`					TEXT			NOT NULL,
	
	PRIMARY KEY (`holdingNoteID`),

	KEY `fk_ihsholdingnote_ihsholding_holdingID` (`holdingID`),
	CONSTRAINT `fk_ihsholdingnote_ihsholding_holdingID` FOREIGN KEY (`holdingID`) REFERENCES `ihsholding` (`holdingID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;





create table `crl`.`ihsuser_ihssecurityrole` (
ihsuser_userID                 INT(11)	 not null,
ihssecurityrole_securityRoleId       INT(11)	 not null,
constraint pk_ihsuser_ihs_security_role primary key (ihsuser_userID, ihssecurityrole_securityRoleId))
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `crl`.`ihssecurityrole` (
	`securityRoleId`		INT(11)			NOT NULL AUTO_INCREMENT,
	`name`					VARCHAR(64)		NOT NULL,
	`description`			VARCHAR(128)	NOT NULL,	
	PRIMARY KEY (`securityRoleId`)
)	ENGINE=INNODB DEFAULT CHARSET=utf8;



CREATE TABLE `crl`.`pwanttitlemember` (
	`pwantTitleMember`	INT(11)			NOT NULL AUTO_INCREMENT,
	`titleID`		INT(11)			NOT NULL,
	`memberID`		INT(11)			NULL,
	
	PRIMARY KEY (`pwantTitleMember`),

	KEY `fk_ptitlecategories_pwanttitlemember_titleID` (`titleID`),
	CONSTRAINT `fk_ptitlecategories_pwanttitlemember_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`),

	KEY `fk_ptitlecategories_pwanttitlemember_memberID` (`memberID`),
	CONSTRAINT `fk_ptitlecategories_pwanttitlemember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


-- ALTER TABLE `crl`.`ihsdeaccessionJob` MODIFY jsonString MEDIUMTEXT ;

CREATE TABLE `crl`.`ihsdeaccessionJob` (
	`deaccessionJobId`	INT(11)			NOT NULL AUTO_INCREMENT,
	`dateInitiated` timestamp,
	`jbbName` VARCHAR(128),
	`userId` INT(11),
	`selected` INT(11),
	`jsonString`  MEDIUMTEXT ,
	`jobStatusId` INT(11),
	`dateCompleted` timestamp,
	`link` VARCHAR(200),
	
	PRIMARY KEY (`deaccessionJobId`),

	KEY `fk_deaccessionJob_ihsuser_ingestedByUserID` (`userId`),
	
	CONSTRAINT `fk_deaccessionJob_ihsuser_deaccessionJobId` FOREIGN KEY (`userId`) REFERENCES `ihsuser` (`userID`)
,	
	
	KEY `fk_ihsdeaccessionJob_obStatusID` (`jobStatusId`),
	CONSTRAINT `fk_ihsdeaccessionJobs_ingestionJobStatusID` FOREIGN KEY (`jobStatusId`) REFERENCES `singestionjobstatus` (`ingestionJobStatusID`)
	
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `crl`.`ihspublishingJob` (
	`publishingJobId`	INT(11)			NOT NULL AUTO_INCREMENT,
	`dateInitiated` timestamp,
	`jobName` VARCHAR(128),
	`userId` INT(11),
	`startDate` date,
	`endDate` date,
	`jsonString`   VARCHAR(500),
	`jobStatusId` INT(11),
	`link`  VARCHAR(200),
	`fileformat` int(11),
	
	PRIMARY KEY (`publishingJobId`),

	KEY `fk_publishingJob_ihsuser_ingestedByUserID` (`userId`),
	
	CONSTRAINT `fk_publishingJob_ihsuser_publishingJobId` FOREIGN KEY (`userId`) REFERENCES `ihsuser` (`userID`)
,	
	
	KEY `fk_publishingJob_obStatusID` (`jobStatusId`),
	CONSTRAINT `fk_ihspublishingJobs_ingestionJobStatusID` FOREIGN KEY (`jobStatusId`) REFERENCES `singestionjobstatus` (`ingestionJobStatusID`)
	
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `crl`.`ihsreportingjob` (
	`reportingJobId`	INT(11)			NOT NULL AUTO_INCREMENT,
	`dateInitiated` timestamp,
	`report` VARCHAR(128),
	`userId` INT(11),
	`jsonString`   VARCHAR(2000),
	`jobStatusId` INT(11),
	`link`  VARCHAR(200),
	`fileformat` VARCHAR(11),
	`parameters` VARCHAR(1000),

	PRIMARY KEY (`reportingJobId`),

	KEY `fk_reportingJob_ihsuser_ingestedByUserID` (`userId`),
	
	CONSTRAINT `fk_reportingJob_ihsuser_reportingJobId` FOREIGN KEY (`userId`) REFERENCES `ihsuser` (`userID`)
,	
	
	KEY `fk_reportingJob_obStatusID` (`jobStatusId`),
	CONSTRAINT `fk_ihsreportingJobs_ingestionJobStatusID` FOREIGN KEY (`jobStatusId`) REFERENCES `singestionjobstatus` (`ingestionJobStatusID`)
	
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `crl`.`scountry` (
	`countryId`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`          varchar(32), 
	
	PRIMARY KEY (`countryId`)
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `crl`.`sstateProvince` (
	`stateProvinceId`	INT(11)			NOT NULL AUTO_INCREMENT,
	`name`          varchar(32), 
	`countryId`	INT(11),		
	PRIMARY KEY (`stateProvinceId`),

	KEY `fk_stateProvince_county_countryId` (`countryId`),
	
	CONSTRAINT `fk_stateProvince_county_countryId_cons` FOREIGN KEY (`countryId`) REFERENCES `scountry` (`countryId`)

	
	
	
	
)	ENGINE=INNODB DEFAULT CHARSET=utf8;

