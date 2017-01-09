-- MySQL dump 10.13  Distrib 5.7.13, for Win32 (AMD64)
--
-- Host: localhost    Database: crl
-- ------------------------------------------------------
-- Server version	5.7.13-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `5_load_holdings_failures`
--

DROP TABLE IF EXISTS `5_load_holdings_failures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `5_load_holdings_failures` (
  `oclcNumber` varchar(32) DEFAULT NULL,
  `member_name` varchar(128) NOT NULL,
  `holdings_statement` varchar(512) DEFAULT NULL,
  `failure_cause` varchar(512) DEFAULT NULL,
  `failure_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `5_load_holdings_success`
--

DROP TABLE IF EXISTS `5_load_holdings_success`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `5_load_holdings_success` (
  `oclcNumber` varchar(32) DEFAULT NULL,
  `member_name` varchar(128) NOT NULL,
  `holdings_statement` varchar(512) DEFAULT NULL,
  `count_holdings_inserted` int(11) DEFAULT NULL,
  `success_note` varchar(512) DEFAULT NULL,
  `success_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crl_titles_in_ihs`
--

DROP TABLE IF EXISTS `crl_titles_in_ihs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crl_titles_in_ihs` (
  `ihs_titleID` int(11) NOT NULL AUTO_INCREMENT,
  `ihs_oclcNumber` varchar(32) DEFAULT NULL,
  `crl_oclcNumber` varchar(32) DEFAULT NULL,
  `ihs_title` varchar(512) NOT NULL,
  `ihs_alphaTitle` varchar(512) NOT NULL,
  `crl_title` varchar(512) NOT NULL,
  `title_match_YN` varchar(32) DEFAULT NULL,
  `title_correction_needed_YN` varchar(32) DEFAULT NULL,
  `ihs_printISSN` varchar(32) DEFAULT NULL,
  `crl_printISSN` varchar(32) DEFAULT NULL,
  `issn_match_YN` varchar(32) DEFAULT NULL,
  `issn_correction_needed_YN` varchar(32) DEFAULT NULL,
  `eISSN` varchar(32) DEFAULT NULL,
  `lccn` varchar(32) DEFAULT NULL,
  `publisherID` int(11) NOT NULL,
  `ihs_publisher` varchar(512) DEFAULT NULL,
  `crl_publisher` varchar(512) DEFAULT NULL,
  `publisher_match_YN` varchar(32) DEFAULT NULL,
  `publisher_correction_needed_YN` varchar(32) DEFAULT NULL,
  `ihs_description` varchar(512) DEFAULT NULL,
  `crl_description` varchar(512) DEFAULT NULL,
  `titleStatusID` int(11) NOT NULL,
  `changeDate` date NOT NULL,
  `userId` int(11) NOT NULL,
  `titleVersion` int(11) DEFAULT '0',
  `imagePageRatio` int(11) DEFAULT NULL,
  `ihs_language` varchar(32) NOT NULL,
  `crl_language` varchar(32) NOT NULL,
  `language_match_YN` varchar(32) DEFAULT NULL,
  `language_correction_needed_YN` varchar(32) DEFAULT NULL,
  `ihs_country` varchar(32) NOT NULL,
  `crl_country` varchar(32) NOT NULL,
  `country_match_YN` varchar(32) DEFAULT NULL,
  `country_correction_needed_YN` varchar(32) DEFAULT NULL,
  `ihs_volumeLevelFlag` char(1) DEFAULT '0',
  `crl_volumeLevelFlag` char(1) DEFAULT '0',
  `titleTypeID` int(11) NOT NULL,
  PRIMARY KEY (`ihs_titleID`)
) ENGINE=InnoDB AUTO_INCREMENT=80080 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data`
--

DROP TABLE IF EXISTS `data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data` (
  `i` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsaddress`
--

DROP TABLE IF EXISTS `ihsaddress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsaddress` (
  `addressID` int(11) NOT NULL AUTO_INCREMENT,
  `address1` varchar(256) NOT NULL,
  `address2` varchar(256) DEFAULT NULL,
  `city` varchar(256) NOT NULL,
  `stateOrProvence` varchar(128) NOT NULL,
  `postalCode` varchar(10) DEFAULT NULL,
  `zipPlus` varchar(4) DEFAULT NULL,
  `country` varchar(15) NOT NULL,
  `createdTS` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`addressID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsauthorizedsource`
--

DROP TABLE IF EXISTS `ihsauthorizedsource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsauthorizedsource` (
  `authorizedSourceID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `memberID` int(11) DEFAULT NULL,
  PRIMARY KEY (`authorizedSourceID`),
  KEY `fk_ihsauthorizedsource_ihsmember_memberID` (`memberID`),
  CONSTRAINT `fk_ihsauthorizedsource_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsdeaccessionjob`
--

DROP TABLE IF EXISTS `ihsdeaccessionjob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsdeaccessionjob` (
  `deaccessionJobId` int(11) NOT NULL AUTO_INCREMENT,
  `dateInitiated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `jbbName` varchar(128) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `selected` int(11) DEFAULT NULL,
  `jsonString` mediumtext,
  `jobStatusId` int(11) DEFAULT NULL,
  `dateCompleted` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `link` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`deaccessionJobId`),
  KEY `fk_deaccessionJob_ihsuser_ingestedByUserID` (`userId`),
  KEY `fk_ihsdeaccessionJob_obStatusID` (`jobStatusId`),
  CONSTRAINT `fk_deaccessionJob_ihsuser_deaccessionJobId` FOREIGN KEY (`userId`) REFERENCES `ihsuser` (`userID`),
  CONSTRAINT `fk_ihsdeaccessionJobs_ingestionJobStatusID` FOREIGN KEY (`jobStatusId`) REFERENCES `singestionjobstatus` (`ingestionJobStatusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsholding`
--

DROP TABLE IF EXISTS `ihsholding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsholding` (
  `holdingID` int(11) NOT NULL AUTO_INCREMENT,
  `issueID` int(11) NOT NULL,
  `memberID` int(11) NOT NULL,
  `locationID` int(11) NOT NULL,
  `holdingStatusID` int(11) NOT NULL,
  `conditionTypeOverallID` int(11) NOT NULL DEFAULT '1',
  `validationLevelID` int(11) NOT NULL DEFAULT '1',
  `ihsVarifiedID` int(11) NOT NULL DEFAULT '1',
  `missingPages` varchar(128) DEFAULT NULL,
  `commitmentID` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`holdingID`),
  KEY `ihsholding_F1_1` (`issueID`,`memberID`,`locationID`),
  KEY `fk_ihsholding_ihsissue_issueID` (`issueID`),
  KEY `fk_ihsholding_ihsmember_memberID` (`memberID`),
  KEY `fk_ihsholding_ihslocation_locationID` (`locationID`),
  KEY `fk_ihsholding_sholdingstatus_holdingStatusID` (`holdingStatusID`),
  KEY `fk_ihsholding_sconditionTypeOverall_conditionTypeOverallID` (`conditionTypeOverallID`),
  KEY `fk_ihsholding_svalidationLevel_validationLevelID` (`validationLevelID`),
  KEY `fk_ihsholding_sihsVarified_ihsVarifiedID` (`ihsVarifiedID`),
  KEY `fk_ihsholding_sihsVarified_commitmentID` (`commitmentID`),
  CONSTRAINT `fk_ihsholding_ihsissue_issueID` FOREIGN KEY (`issueID`) REFERENCES `ihsissue` (`issueID`),
  CONSTRAINT `fk_ihsholding_ihslocation_locationID` FOREIGN KEY (`locationID`) REFERENCES `ihslocation` (`locationID`),
  CONSTRAINT `fk_ihsholding_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`),
  CONSTRAINT `fk_ihsholding_sconditionTypeOverall_conditionTypeOverallID` FOREIGN KEY (`conditionTypeOverallID`) REFERENCES `sconditiontypeoverall` (`conditionTypeOverallID`),
  CONSTRAINT `fk_ihsholding_sholdingstatus_holdingStatusID` FOREIGN KEY (`holdingStatusID`) REFERENCES `sholdingstatus` (`holdingStatusID`),
  CONSTRAINT `fk_ihsholding_sihsVarified_commitmentID` FOREIGN KEY (`commitmentID`) REFERENCES `scommitment` (`commitmentID`),
  CONSTRAINT `fk_ihsholding_sihsVarified_ihsVarifiedID` FOREIGN KEY (`ihsVarifiedID`) REFERENCES `sihsvarified` (`ihsVarifiedID`),
  CONSTRAINT `fk_ihsholding_svalidationLevel_validationLevelID` FOREIGN KEY (`validationLevelID`) REFERENCES `svalidationlevel` (`validationLevelID`)
) ENGINE=InnoDB AUTO_INCREMENT=621710 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsholding_sconditiontype`
--

DROP TABLE IF EXISTS `ihsholding_sconditiontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsholding_sconditiontype` (
  `ihsholding_holdingID` int(11) NOT NULL,
  `sconditiontype_conditionTypeID` int(11) NOT NULL,
  PRIMARY KEY (`ihsholding_holdingID`,`sconditiontype_conditionTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsholdingnote`
--

DROP TABLE IF EXISTS `ihsholdingnote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsholdingnote` (
  `holdingNoteID` int(11) NOT NULL AUTO_INCREMENT,
  `holdingID` int(11) NOT NULL,
  `note` text NOT NULL,
  PRIMARY KEY (`holdingNoteID`),
  KEY `fk_ihsholdingnote_ihsholding_holdingID` (`holdingID`),
  CONSTRAINT `fk_ihsholdingnote_ihsholding_holdingID` FOREIGN KEY (`holdingID`) REFERENCES `ihsholding` (`holdingID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsingestionexception`
--

DROP TABLE IF EXISTS `ihsingestionexception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsingestionexception` (
  `ingestionExceptionID` int(11) NOT NULL AUTO_INCREMENT,
  `ingestionRecordID` int(11) NOT NULL,
  `ingestionExceptionTypeID` int(11) NOT NULL,
  `recordTitle` varchar(256) NOT NULL,
  `issues` varchar(256) NOT NULL,
  `ingestionExceptionStatusID` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `lockDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ingestionExceptionID`),
  KEY `fk_ihsingestionexception_ihsingestionrecord_ingestionRecordID` (`ingestionRecordID`),
  KEY `fk_ihsingestionexception_singestionexceptiontype_ingestionExcept` (`ingestionExceptionTypeID`),
  KEY `fk_ihsingestionexception_singestionexceptionstatus_ingestionExce` (`ingestionExceptionStatusID`),
  KEY `fk_ihsingestionexception_ihsuser_userID` (`userID`),
  CONSTRAINT `fk_ihsingestionexception_ihsingestionrecord_ingestionRecordID` FOREIGN KEY (`ingestionRecordID`) REFERENCES `ihsingestionrecord` (`ingestionRecordID`),
  CONSTRAINT `fk_ihsingestionexception_ihsuser_userID` FOREIGN KEY (`userID`) REFERENCES `ihsuser` (`userID`),
  CONSTRAINT `fk_ihsingestionexception_singestionexceptionstatus_ingestionExce` FOREIGN KEY (`ingestionExceptionStatusID`) REFERENCES `singestionexceptionstatus` (`ingestionExceptionStatusID`),
  CONSTRAINT `fk_ihsingestionexception_singestionexceptiontype_ingestionExcept` FOREIGN KEY (`ingestionExceptionTypeID`) REFERENCES `singestionexceptiontype` (`ingestionExceptionTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsingestionjob`
--

DROP TABLE IF EXISTS `ihsingestionjob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsingestionjob` (
  `ingestionJobID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `authorizedSourceID` int(11) NOT NULL,
  `ingestionDataTypeID` int(11) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ingestedByUserID` int(11) NOT NULL,
  `sourceFileString` varchar(256) DEFAULT NULL,
  `ingestionJobStatusID` int(11) NOT NULL,
  `statusDetail` varchar(256) DEFAULT NULL,
  `commitmentID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ingestionJobID`),
  KEY `fk_ihsingestionjob_ihsauthorizedsource_authorizedSourceID` (`authorizedSourceID`),
  KEY `fk_ihsingestionjob_singestiondatatype_ingestionDataTypeID` (`ingestionDataTypeID`),
  KEY `fk_ihsingestionjob_ihsuser_ingestedByUserID` (`ingestedByUserID`),
  KEY `fk_ihsingestionjob_singestionjobstatus_ingestionJobStatusID` (`ingestionJobStatusID`),
  KEY `fk_ihsingestionjob_sihsVarified_commitmentID` (`commitmentID`),
  CONSTRAINT `fk_ihsingestionjob_ihsauthorizedsource_authorizedSourceID` FOREIGN KEY (`authorizedSourceID`) REFERENCES `ihsauthorizedsource` (`authorizedSourceID`),
  CONSTRAINT `fk_ihsingestionjob_ihsuser_ingestedByUserID` FOREIGN KEY (`ingestedByUserID`) REFERENCES `ihsuser` (`userID`),
  CONSTRAINT `fk_ihsingestionjob_sihsVarified_commitmentID` FOREIGN KEY (`commitmentID`) REFERENCES `scommitment` (`commitmentID`),
  CONSTRAINT `fk_ihsingestionjob_singestiondatatype_ingestionDataTypeID` FOREIGN KEY (`ingestionDataTypeID`) REFERENCES `singestiondatatype` (`ingestionDataTypeID`),
  CONSTRAINT `fk_ihsingestionjob_singestionjobstatus_ingestionJobStatusID` FOREIGN KEY (`ingestionJobStatusID`) REFERENCES `singestionjobstatus` (`ingestionJobStatusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsingestionrecord`
--

DROP TABLE IF EXISTS `ihsingestionrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsingestionrecord` (
  `ingestionRecordID` int(11) NOT NULL AUTO_INCREMENT,
  `ingestionJobID` int(11) NOT NULL,
  `rawRecordData` text NOT NULL,
  `ingestionRecordStatusID` int(11) NOT NULL,
  `recordTitle` varchar(256) DEFAULT NULL,
  `issues` varchar(256) DEFAULT NULL,
  `jsonRecordData` text,
  `userID` int(11) DEFAULT NULL,
  `lockUserID` int(11) DEFAULT NULL,
  `lockDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ingestionRecordID`),
  KEY `fk_ihsingestionrecord_ihsingestionjob_ingestionJobID` (`ingestionJobID`),
  KEY `fk_ihsingestionrecord_singestionrecordstatus_ingestionRecordStat` (`ingestionRecordStatusID`),
  CONSTRAINT `fk_ihsingestionrecord_ihsingestionjob_ingestionJobID` FOREIGN KEY (`ingestionJobID`) REFERENCES `ihsingestionjob` (`ingestionJobID`),
  CONSTRAINT `fk_ihsingestionrecord_singestionrecordstatus_ingestionRecordStat` FOREIGN KEY (`ingestionRecordStatusID`) REFERENCES `singestionrecordstatus` (`ingestionRecordStatusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsissue`
--

DROP TABLE IF EXISTS `ihsissue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsissue` (
  `issueID` int(11) NOT NULL AUTO_INCREMENT,
  `titleID` int(11) NOT NULL,
  `volumeID` int(11) DEFAULT NULL,
  `publicationRangeID` int(11) NOT NULL,
  `publicationDate` date DEFAULT NULL,
  `issueNumber` varchar(32) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `numPages` int(11) DEFAULT NULL,
  `issueStatusID` int(11) NOT NULL,
  `publicationDateId` int(11) DEFAULT NULL,
  PRIMARY KEY (`issueID`),
  KEY `issueNumber_FI_1` (`issueNumber`),
  KEY `issueNumber_F2_2` (`titleID`,`volumeID`,`issueNumber`),
  KEY `fk_ihsissue_ihstitle_titleID` (`titleID`),
  KEY `fk_ihsissue_ihsvolume_volumeID` (`volumeID`),
  KEY `fk_ihsissue_ihspublicationrange_publicationRangeID` (`publicationRangeID`),
  KEY `fk_ihsissue_sissuestatus_issueStatusID` (`issueStatusID`),
  KEY `fk_ihsIssue_spublicationdate` (`publicationDateId`),
  CONSTRAINT `fk_ihsIssue_spublicationdate` FOREIGN KEY (`publicationDateId`) REFERENCES `spublicationdate` (`publicationDateId`),
  CONSTRAINT `fk_ihsissue_ihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`),
  CONSTRAINT `fk_ihsissue_ihsvolume_volumeID` FOREIGN KEY (`volumeID`) REFERENCES `ihsvolume` (`volumeID`),
  CONSTRAINT `fk_ihsissue_sissuestatus_issueStatusID` FOREIGN KEY (`issueStatusID`) REFERENCES `sissuestatus` (`issueStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=657119 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihslocation`
--

DROP TABLE IF EXISTS `ihslocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihslocation` (
  `locationID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `memberID` int(11) NOT NULL,
  `addressID` int(11) NOT NULL,
  PRIMARY KEY (`locationID`),
  KEY `fk_ihslocation_ihsmember_memberID` (`memberID`),
  KEY `fk_ihslocation_ihsaddress_addressID` (`addressID`),
  CONSTRAINT `fk_ihslocation_ihsmember_memberID` FOREIGN KEY (`memberID`) REFERENCES `ihsmember` (`memberID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsmember`
--

DROP TABLE IF EXISTS `ihsmember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsmember` (
  `memberID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `memberStatusID` int(11) NOT NULL,
  `addressID` int(11) NOT NULL,
  `ftpdirectory` varchar(256) DEFAULT NULL,
  `membergroupID` int(11) NOT NULL,
  PRIMARY KEY (`memberID`),
  KEY `fk_ihsmember_ihsaddress_addressID` (`addressID`),
  KEY `fk_ihsmember_smemberstatus_memberStatusID` (`memberStatusID`),
  KEY `fk_ihsmember_membergroup_membergroupID` (`membergroupID`),
  CONSTRAINT `fk_ihsmember_smemberstatus_memberStatusID` FOREIGN KEY (`memberStatusID`) REFERENCES `smemberstatus` (`memberStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsmembergroup`
--

DROP TABLE IF EXISTS `ihsmembergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsmembergroup` (
  `membergroupID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`membergroupID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihspublicationrange`
--

DROP TABLE IF EXISTS `ihspublicationrange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihspublicationrange` (
  `publicationRangeID` int(11) NOT NULL AUTO_INCREMENT,
  `titleID` int(11) NOT NULL,
  `periodicityTypeID` int(11) NOT NULL,
  `pbrStartDate` date DEFAULT NULL,
  `pbrEndDate` date DEFAULT NULL,
  PRIMARY KEY (`publicationRangeID`),
  KEY `fk_ihspublicationrange_ihstitle_titleID` (`titleID`),
  KEY `fk_ihspublicationrange_speriodicitytype_periodicityTypeID` (`periodicityTypeID`),
  CONSTRAINT `fk_ihspublicationrange_speriodicitytype_periodicityTypeID` FOREIGN KEY (`periodicityTypeID`) REFERENCES `speriodicitytype` (`periodicityTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=49807 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihspublicationrangever`
--

DROP TABLE IF EXISTS `ihspublicationrangever`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihspublicationrangever` (
  `publicationRangeID` int(11) NOT NULL AUTO_INCREMENT,
  `titleversionID` int(11) NOT NULL,
  `periodicityTypeID` int(11) NOT NULL,
  `pbrStartDate` date DEFAULT NULL,
  `pbrEndDate` date DEFAULT NULL,
  PRIMARY KEY (`publicationRangeID`),
  KEY `fk_ihspublicationraver_ihstitle_titleID` (`titleversionID`),
  KEY `fk_ihspublicationraver_speriodicitytype_periodicityTypeID` (`periodicityTypeID`),
  CONSTRAINT `fk_ihspublicationraver_speriodicitytype_periodicityTypeID` FOREIGN KEY (`periodicityTypeID`) REFERENCES `speriodicitytype` (`periodicityTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihspublisher`
--

DROP TABLE IF EXISTS `ihspublisher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihspublisher` (
  `publisherID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `addressID` int(11) NOT NULL,
  PRIMARY KEY (`publisherID`),
  KEY `fk_ihspublisher_ihsaddress_addressID` (`addressID`),
  CONSTRAINT `fk_ihspublisher_ihsaddress_addressID` FOREIGN KEY (`addressID`) REFERENCES `ihsaddress` (`addressID`)
) ENGINE=InnoDB AUTO_INCREMENT=64888 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihspublishingjob`
--

DROP TABLE IF EXISTS `ihspublishingjob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihspublishingjob` (
  `publishingJobId` int(11) NOT NULL AUTO_INCREMENT,
  `dateInitiated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `jobName` varchar(128) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `jsonString` varchar(500) DEFAULT NULL,
  `jobStatusId` int(11) DEFAULT NULL,
  `link` varchar(200) DEFAULT NULL,
  `fileformat` int(11) DEFAULT NULL,
  PRIMARY KEY (`publishingJobId`),
  KEY `fk_publishingJob_ihsuser_ingestedByUserID` (`userId`),
  KEY `fk_publishingJob_obStatusID` (`jobStatusId`),
  CONSTRAINT `fk_publishingJob_ihsuser_publishingJobId` FOREIGN KEY (`userId`) REFERENCES `ihsuser` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsreportingjob`
--

DROP TABLE IF EXISTS `ihsreportingjob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsreportingjob` (
  `reportingJobId` int(11) NOT NULL AUTO_INCREMENT,
  `dateInitiated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `report` varchar(128) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `jsonString` varchar(2000) DEFAULT NULL,
  `jobStatusId` int(11) DEFAULT NULL,
  `link` varchar(200) DEFAULT NULL,
  `fileformat` varchar(11) DEFAULT NULL,
  `parameters` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`reportingJobId`),
  KEY `fk_reportingJob_ihsuser_ingestedByUserID` (`userId`),
  KEY `fk_reportingJob_obStatusID` (`jobStatusId`),
  CONSTRAINT `fk_reportingJob_ihsuser_reportingJobId` FOREIGN KEY (`userId`) REFERENCES `ihsuser` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihssecurityrole`
--

DROP TABLE IF EXISTS `ihssecurityrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihssecurityrole` (
  `securityRoleId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`securityRoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihstitle`
--

DROP TABLE IF EXISTS `ihstitle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihstitle` (
  `titleID` int(11) NOT NULL AUTO_INCREMENT,
  `titleTypeID` int(11) NOT NULL,
  `title` varchar(512) NOT NULL,
  `alphaTitle` varchar(512) DEFAULT NULL,
  `printISSN` varchar(32) DEFAULT NULL,
  `eISSN` varchar(32) DEFAULT NULL,
  `oclcNumber` varchar(32) DEFAULT NULL,
  `lccn` varchar(32) DEFAULT NULL,
  `publisherID` int(11) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `titleStatusID` int(11) NOT NULL,
  `changeDate` date NOT NULL,
  `userId` int(11) NOT NULL,
  `titleVersion` int(11) DEFAULT '0',
  `imagePageRatio` int(11) DEFAULT NULL,
  `language` varchar(32) NOT NULL,
  `country` varchar(32) NOT NULL,
  `volumeLevelFlag` char(1) DEFAULT '0',
  PRIMARY KEY (`titleID`),
  UNIQUE KEY `AJE_oclc_volFlag` (`oclcNumber`),
  KEY `printISSN_FI_1` (`printISSN`),
  KEY `oclcNumber_FI_1` (`oclcNumber`),
  KEY `fk_ihstitle_ihspublisher_publisherID` (`publisherID`),
  KEY `fk_ihstitle_stitletype_titleTypeID` (`titleTypeID`),
  KEY `fk_ihstitle_stitlestatus_titleStatusID` (`titleStatusID`),
  FULLTEXT KEY `title_ft` (`title`),
  CONSTRAINT `fk_ihstitle_ihspublisher_publisherID` FOREIGN KEY (`publisherID`) REFERENCES `ihspublisher` (`publisherID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_ihstitle_stitletype_titleTypeID` FOREIGN KEY (`titleTypeID`) REFERENCES `stitletype` (`titleTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=119353 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihstitleversion`
--

DROP TABLE IF EXISTS `ihstitleversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihstitleversion` (
  `titleversionID` int(11) NOT NULL AUTO_INCREMENT,
  `titleID` int(11) DEFAULT NULL,
  `titleTypeID` int(11) NOT NULL,
  `title` varchar(512) NOT NULL,
  `alphaTitle` varchar(512) DEFAULT NULL,
  `printISSN` varchar(32) DEFAULT NULL,
  `eISSN` varchar(32) DEFAULT NULL,
  `oclcNumber` varchar(32) DEFAULT NULL,
  `lccn` varchar(32) DEFAULT NULL,
  `publisherID` int(11) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `titleStatusID` int(11) NOT NULL,
  `changeDate` date DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `imagePageRatio` int(11) DEFAULT NULL,
  `language` varchar(32) NOT NULL,
  `country` varchar(32) NOT NULL,
  PRIMARY KEY (`titleversionID`),
  KEY `printISSN_FI_1` (`printISSN`),
  KEY `oclcNumber_FI_1` (`oclcNumber`),
  KEY `fk_ihstitleversion_ihstitle_titleID` (`titleID`),
  KEY `fk_ihstitleversion_ihspublisher_publisherID` (`publisherID`),
  CONSTRAINT `fk_ihstitleversionihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsuser`
--

DROP TABLE IF EXISTS `ihsuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsuser` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(128) NOT NULL,
  `lastName` varchar(128) NOT NULL,
  `memberID` int(11) NOT NULL,
  `userStatusID` int(11) NOT NULL,
  `userName` varchar(16) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`userID`),
  KEY `fk_ihsuser_ihsmember_memberID` (`memberID`),
  KEY `fk_ihsuser_suserstatus_userStatusID` (`userStatusID`),
  CONSTRAINT `fk_ihsuser_suserstatus_userStatusID` FOREIGN KEY (`userStatusID`) REFERENCES `suserstatus` (`userStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsuser_ihssecurityrole`
--

DROP TABLE IF EXISTS `ihsuser_ihssecurityrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsuser_ihssecurityrole` (
  `ihsuser_userID` int(11) NOT NULL,
  `ihssecurityrole_securityRoleId` int(11) NOT NULL,
  PRIMARY KEY (`ihsuser_userID`,`ihssecurityrole_securityRoleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihsvolume`
--

DROP TABLE IF EXISTS `ihsvolume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsvolume` (
  `volumeID` int(11) NOT NULL AUTO_INCREMENT,
  `titleID` int(11) NOT NULL,
  `volumeNumber` varchar(32) NOT NULL,
  `vlmStartDate` date DEFAULT NULL,
  `vlmEndDate` date DEFAULT NULL,
  PRIMARY KEY (`volumeID`),
  KEY `volumeNumber_FI_1` (`titleID`,`volumeNumber`),
  KEY `fk_ihsvolume_ihstitle_titleID` (`titleID`),
  CONSTRAINT `fk_ihsvolume_ihstitle_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`)
) ENGINE=InnoDB AUTO_INCREMENT=475867 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lhl_junk_holdings_statements`
--

DROP TABLE IF EXISTS `lhl_junk_holdings_statements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lhl_junk_holdings_statements` (
  `holdingID` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(512) NOT NULL,
  `ISSN` varchar(32) DEFAULT NULL,
  `imprint` varchar(256) DEFAULT NULL,
  `oclcNumber` varchar(32) DEFAULT NULL,
  `program` varchar(256) DEFAULT NULL,
  `holder_name` varchar(256) DEFAULT NULL,
  `holder_code` varchar(32) DEFAULT NULL,
  `holdings_statement` mediumtext,
  `some_date` date DEFAULT NULL,
  PRIMARY KEY (`holdingID`)
) ENGINE=InnoDB AUTO_INCREMENT=29263 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pholdingcondition`
--

DROP TABLE IF EXISTS `pholdingcondition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pholdingcondition` (
  `holdingConditionID` int(11) NOT NULL AUTO_INCREMENT,
  `holdingID` int(11) NOT NULL,
  `conditionTypeID` int(11) NOT NULL,
  PRIMARY KEY (`holdingConditionID`),
  KEY `fk_pholdingcondition_ihsholding_holdingID` (`holdingID`),
  KEY `fk_pholdingcondition_sconditiontype_conditionTypeID` (`conditionTypeID`),
  CONSTRAINT `fk_pholdingcondition_sconditiontype_conditionTypeID` FOREIGN KEY (`conditionTypeID`) REFERENCES `sconditiontype` (`conditiontypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ptitlelink`
--

DROP TABLE IF EXISTS `ptitlelink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ptitlelink` (
  `titleLinkID` int(11) NOT NULL AUTO_INCREMENT,
  `titleParentID` int(11) NOT NULL,
  `titleChildID` int(11) NOT NULL,
  PRIMARY KEY (`titleLinkID`),
  KEY `fk_ptitlelink_ihstitle_titleParentID` (`titleParentID`),
  KEY `fk_ptitlelink_ihstitle_titleChildID` (`titleChildID`),
  CONSTRAINT `fk_ptitlelink_ihstitle_titleParentID` FOREIGN KEY (`titleParentID`) REFERENCES `ihstitle` (`titleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pwanttitlemember`
--

DROP TABLE IF EXISTS `pwanttitlemember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pwanttitlemember` (
  `pwantTitleMember` int(11) NOT NULL AUTO_INCREMENT,
  `titleID` int(11) NOT NULL,
  `memberID` int(11) DEFAULT NULL,
  PRIMARY KEY (`pwantTitleMember`),
  KEY `fk_ptitlecategories_pwanttitlemember_titleID` (`titleID`),
  KEY `fk_ptitlecategories_pwanttitlemember_memberID` (`memberID`),
  CONSTRAINT `fk_ptitlecategories_pwanttitlemember_titleID` FOREIGN KEY (`titleID`) REFERENCES `ihstitle` (`titleID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scommitment`
--

DROP TABLE IF EXISTS `scommitment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scommitment` (
  `commitmentID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`commitmentID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sconditiontype`
--

DROP TABLE IF EXISTS `sconditiontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sconditiontype` (
  `conditiontypeID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`conditiontypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sconditiontypeoverall`
--

DROP TABLE IF EXISTS `sconditiontypeoverall`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sconditiontypeoverall` (
  `conditionTypeOverallID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`conditionTypeOverallID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scountry`
--

DROP TABLE IF EXISTS `scountry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scountry` (
  `countryId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`countryId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sholdingstatus`
--

DROP TABLE IF EXISTS `sholdingstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sholdingstatus` (
  `holdingStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`holdingStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sihsvarified`
--

DROP TABLE IF EXISTS `sihsvarified`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sihsvarified` (
  `ihsVarifiedID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`ihsVarifiedID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `singestiondatatype`
--

DROP TABLE IF EXISTS `singestiondatatype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `singestiondatatype` (
  `ingestionDataTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`ingestionDataTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `singestionexceptionstatus`
--

DROP TABLE IF EXISTS `singestionexceptionstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `singestionexceptionstatus` (
  `ingestionExceptionStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`ingestionExceptionStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `singestionexceptiontype`
--

DROP TABLE IF EXISTS `singestionexceptiontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `singestionexceptiontype` (
  `ingestionExceptionTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`ingestionExceptionTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `singestionjobstatus`
--

DROP TABLE IF EXISTS `singestionjobstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `singestionjobstatus` (
  `ingestionJobStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`ingestionJobStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `singestionrecordstatus`
--

DROP TABLE IF EXISTS `singestionrecordstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `singestionrecordstatus` (
  `ingestionRecordStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`ingestionRecordStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sissuestatus`
--

DROP TABLE IF EXISTS `sissuestatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sissuestatus` (
  `issueStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`issueStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `smemberstatus`
--

DROP TABLE IF EXISTS `smemberstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smemberstatus` (
  `memberStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`memberStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `speriodicitytype`
--

DROP TABLE IF EXISTS `speriodicitytype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `speriodicitytype` (
  `periodicityTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(128) NOT NULL,
  `intervalsPerYear` int(11) NOT NULL,
  PRIMARY KEY (`periodicityTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `spublicationdate`
--

DROP TABLE IF EXISTS `spublicationdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spublicationdate` (
  `publicationDateId` int(11) NOT NULL AUTO_INCREMENT,
  `publicationDateTypeId` int(11) NOT NULL,
  `publicationDateVal` varchar(32) NOT NULL,
  `publicationDateSeq` int(11) NOT NULL,
  PRIMARY KEY (`publicationDateId`),
  KEY `fk_spublicationdate_spublicationdateType_id` (`publicationDateTypeId`),
  CONSTRAINT `fk_spublicationdate_spublicationdateType` FOREIGN KEY (`publicationDateTypeId`) REFERENCES `spublicationdatetype` (`publicationDateTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `spublicationdatetype`
--

DROP TABLE IF EXISTS `spublicationdatetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spublicationdatetype` (
  `publicationDateTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `publicationDateTypeVal` varchar(32) NOT NULL,
  `numeberOfval` int(11) DEFAULT NULL,
  PRIMARY KEY (`publicationDateTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sstateprovince`
--

DROP TABLE IF EXISTS `sstateprovince`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sstateprovince` (
  `stateProvinceId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `countryId` int(11) DEFAULT NULL,
  PRIMARY KEY (`stateProvinceId`),
  KEY `fk_stateProvince_county_countryId` (`countryId`),
  CONSTRAINT `fk_stateProvince_county_countryId_cons` FOREIGN KEY (`countryId`) REFERENCES `scountry` (`countryId`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stitlestatus`
--

DROP TABLE IF EXISTS `stitlestatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stitlestatus` (
  `titleStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`titleStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stitletype`
--

DROP TABLE IF EXISTS `stitletype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stitletype` (
  `titleTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`titleTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `suserstatus`
--

DROP TABLE IF EXISTS `suserstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suserstatus` (
  `userStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`userStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `svalidationlevel`
--

DROP TABLE IF EXISTS `svalidationlevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `svalidationlevel` (
  `validationLevelID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`validationLevelID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tmp_issue`
--

DROP TABLE IF EXISTS `tmp_issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmp_issue` (
  `oclc_number` varchar(1000) DEFAULT NULL,
  `year` varchar(1000) DEFAULT NULL,
  `month` varchar(1000) DEFAULT NULL,
  `day` varchar(1000) DEFAULT NULL,
  `series_caption` varchar(1000) DEFAULT NULL,
  `series_number` varchar(1000) DEFAULT NULL,
  `first_level_caption` varchar(1000) DEFAULT NULL,
  `first_level_number` varchar(1000) DEFAULT NULL,
  `second_level_caption` varchar(1000) DEFAULT NULL,
  `second_level_number` varchar(1000) DEFAULT NULL,
  `third_level_caption` varchar(1000) DEFAULT NULL,
  `third_level_number` varchar(1000) DEFAULT NULL,
  `issue_type` varchar(1000) DEFAULT NULL,
  `issue_title` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tmp_title`
--

DROP TABLE IF EXISTS `tmp_title`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmp_title` (
  `oclcNumber` varchar(1000) DEFAULT NULL,
  `printISSN` varchar(1000) DEFAULT NULL,
  `eISSN` varchar(1000) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `publisherName` varchar(1000) DEFAULT NULL,
  `Pub_Start_Year` varchar(1000) DEFAULT NULL,
  `Pub_End_Year` varchar(1000) DEFAULT NULL,
  `language` varchar(1000) DEFAULT NULL,
  `country` varchar(1000) DEFAULT NULL,
  `LC_Class` varchar(1000) DEFAULT NULL,
  `volumeLevelFlag` varchar(1000) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tmp_volume`
--

DROP TABLE IF EXISTS `tmp_volume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmp_volume` (
  `oclcNumber` varchar(512) DEFAULT NULL,
  `year` varchar(512) DEFAULT NULL,
  `month` varchar(512) DEFAULT NULL,
  `day` varchar(512) DEFAULT NULL,
  `series_caption` varchar(512) DEFAULT NULL,
  `series_number` varchar(512) DEFAULT NULL,
  `first_level_caption` varchar(512) DEFAULT NULL,
  `volumeNumber` varchar(512) DEFAULT NULL,
  `second_level_caption` varchar(512) DEFAULT NULL,
  `second_level_number` varchar(512) DEFAULT NULL,
  `third_level_caption` varchar(512) DEFAULT NULL,
  `third_level_number` varchar(512) DEFAULT NULL,
  `issue_type` varchar(512) DEFAULT NULL,
  `issue_title` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'crl'
--

--
-- Dumping routines for database 'crl'
--
/*!50003 DROP PROCEDURE IF EXISTS `extract_first_issue` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `extract_first_issue`(
	IN holdings_statement TEXT,
    IN this_title_id INT, 
    IN this_volume_number TEXT, 
	OUT return_value TEXT
)
BEGIN
	DECLARE issueStartPos INT; 
	DECLARE issueEndPos INT;
	DECLARE colonPos INT; 
	DECLARE dotVpos INT; 
    DECLARE dashVpos INT; 
    DECLARE this_volume_number TEXT; DECLARE this_volume_year TEXT;
	DECLARE targetLength INT;

    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 

	DECLARE output_message TEXT;


	SET @return_value = -9; 
	 SET this_volume_year = '-9';  SET issueStartPos = -9; SET issueEndPos = -9; SET targetLength = -9;

	
	SET colonPos = LOCATE(':', holdings_statement, 1);
	SET dotVpos = LOCATE('v.', holdings_statement, 1);
	SET dashVpos = LOCATE('-v.', holdings_statement, 1);
    SET issueStartPos = dashVpos -4;
    SET issueEndPos = colonPos +4;

	CALL extract_first_volume(holdings_statement, this_title_id, this_volume_number);
    IF this_volume_number IS NULL THEN
		SET this_volume_number = '-9';
	END IF;
	CALL extract_first_year(holdings_statement, this_title_id, this_volume_number, this_volume_year);
    IF this_volume_year IS NULL THEN
		SET this_volume_year = '-9';
	END IF;

	

    
    
	
    IF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]*:no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
    OR holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
    THEN 
		
		SET issueStartPos = colonPos+4;
		SET issueEndPos = dashVpos;
		
    END IF;
    
    
	
	IF holdings_statement REGEXP '^v.[0-9]* [(][0-9]*[)]$'  
	OR holdings_statement REGEXP '^v.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' 
	OR holdings_statement REGEXP '^no.[0-9]*-no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' 
	OR holdings_statement REGEXP '^v.[0-9]*-v.[0-9]*$' 
	OR holdings_statement REGEXP '^[(][0-9]{4}-[0-9]{4}[)]$' 
    OR holdings_statement REGEXP '^v.[0-9]*-v.[0-9]*:no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' 
    THEN 
		
		CALL get_MINMAX_issue_for_volume( this_title_id, this_volume_number, this_volume_year, 'MIN', @return_value);
	END IF;

	

	IF @return_value = -9 THEN 

		SET targetLength = issueEndPos - issueStartPos; 

		

		SELECT SUBSTRING(holdings_statement,
			issueStartPos,
			targetLength
		) INTO @return_value;

		SET @return_value = REPLACE(@return_value, 'no.', '');
        SET @return_value = REPLACE(@return_value, 'pp.', '');

       	

	END IF;

	SET return_value = @return_value;
    
	IF @return_value IS NOT NULL THEN
		SELECT CONCAT('extract_first_issue(', CAST(holdings_statement AS CHAR), ') @return_value = "', CAST(@return_value AS CHAR), '".') AS extract_first_issue_result;
	ELSE
		SELECT CONCAT('extract_first_issue(', CAST(holdings_statement AS CHAR), ') @return_value = NULL.') AS extract_first_issue_result;
	END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `extract_first_volume` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `extract_first_volume`(
	IN holdings_statement TEXT,
    IN this_title_id INT,
	OUT return_value TEXT
)
BEGIN
	DECLARE volStartPos INT; 
	DECLARE volEndPos INT;
	DECLARE colonPos INT; 
	DECLARE dashVpos INT; 
    DECLARE spacePos INT; 
    DECLARE year_string TEXT;
	DECLARE targetLength INT;

	DECLARE output_message TEXT;


	SET return_value = -9; 
	SET volStartPos = -9; SET volEndPos = -9; SET targetLength = -9;

	
	SET colonPos = LOCATE(':', holdings_statement, 1);
	SET spacePos = LOCATE(' ', holdings_statement, 1);
    SET dashVpos = LOCATE('-v.', holdings_statement, 1);
	

	IF holdings_statement REGEXP '^[(][0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('(', holdings_statement, 1)+1;
        SET volEndPos = LOCATE(')', holdings_statement, 1);
		
	ELSEIF holdings_statement REGEXP '^[0-9]*[(][0-9]{4}[)]$' 
		OR holdings_statement REGEXP '^[0-9]*[(][0-9]{4}$' 
    THEN 
		SET volStartPos = 1;
        SET volEndPos = LOCATE('(', holdings_statement, 1);
		
	ELSEIF holdings_statement REGEXP '^v.[0-9]* [(][0-9]{4}[)]$' 
		OR holdings_statement REGEXP '^v.[0-9]*[[:blank:]][(][0-9]{4}[[.slash.]][0-9]{4}[)]$' 
    THEN
		SET volStartPos = LOCATE('v.', holdings_statement, 1)+2;
        SET volEndPos = spacePos;
		
	ELSEIF holdings_statement REGEXP '^no.[0-9]* [(][0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('no.', holdings_statement, 1)+3;
        SET volEndPos = spacePos;
		
	ELSEIF holdings_statement REGEXP '^v.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('v.', holdings_statement, 1)+2;
        SET volEndPos = dashVpos;
		
	ELSEIF holdings_statement REGEXP '^no.[0-9]*-no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('no.', holdings_statement, 1)+3;
        SET volEndPos = LOCATE('-no.', holdings_statement, 1);
		
	ELSEIF holdings_statement REGEXP '^v.[0-9]*-v.[0-9]*$' THEN 
		SET volStartPos = LOCATE('v.', holdings_statement, 1)+2;
        SET volEndPos = dashVpos;
		
	ELSEIF holdings_statement REGEXP '^[(][0-9]{4}-[0-9]{4}[)]$'  
		OR holdings_statement REGEXP '^[(][0-9]{4}[[.slash.]][0-9]{4}-[0-9\-]*[)]$' 
    THEN
		SET volStartPos = LOCATE('(', holdings_statement, 1)+1;
		SET volEndPos = LOCATE('-', holdings_statement, 1);
        SET year_string = SUBSTRING(holdings_statement, volStartPos, 4);
        
        
	ELSEIF holdings_statement REGEXP '^[0-9]{4}-[0-9]{4}$' THEN 
		SET volStartPos = 1;
		SET volEndPos = LOCATE('-', holdings_statement, 1);   
    ELSEIF holdings_statement REGEXP '^v[0-9]*[[:blank:]]n[0-9]*[(][0-9\-]*[)]$' THEN 
		SET volStartPos = 2;
		SET volEndPos = LOCATE(' ', holdings_statement, 1);           
    ELSEIF holdings_statement REGEXP '^v[0-9]*[[.hyphen.]]n[0-9]*[(][0-9\-]*[)]$' THEN 
		SET volStartPos = 2;
		SET volEndPos = LOCATE('-', holdings_statement, 1);         
    ELSEIF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]*:no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
		OR holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
        OR holdings_statement REGEXP '^v.[0-9]*:no.[0-9]* [(][A-Za-z\.]* [0-9]{4}[)]$' 
    THEN 
		
		SET volStartPos = 3;
		SET volEndPos = colonPos;
	ELSEIF holdings_statement REGEXP '^[(][0-9]{4}-[0-9]{4}[)]$' THEN
		SELECT holdings_statement AS IF_REGEXP_parensYYYYdashYYYY;
	END IF;

	SET targetLength = volEndPos - volStartPos;
	

	
	IF return_value = -9 THEN 
	SELECT SUBSTRING(holdings_statement,
		volStartPos,
		targetLength
	) INTO return_value;
    END IF;

	SELECT CONCAT('extract_first_volume(', CAST(holdings_statement AS CHAR), ') return_value = ', CAST(return_value AS CHAR), '.') AS extract_first_volume_result;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `extract_first_year` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `extract_first_year`(
	IN holdings_statement TEXT,
    IN this_title_id INT,
    IN this_volume_number TEXT,
	INOUT this_volume_year TEXT
)
BEGIN
	DECLARE beginPubYear TEXT;

	DECLARE output_message TEXT;


	SET @return_value = -9; 

    IF  holdings_statement REGEXP '^([0-9]{4}-[0-9]{4})$' OR
    		holdings_statement REGEXP '^([0-9]{4})$' OR 
            holdings_statement REGEXP '^v.[0-9]* [(][0-9]{4}[)]' OR  
			holdings_statement REGEXP '^v.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' OR 
            holdings_statement REGEXP '^no.[0-9]*-no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' 
    THEN
		SELECT SUBSTRING(holdings_statement, 	LOCATE("(", holdings_statement, 1)+1, 4) INTO this_volume_year;
	ELSEIF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]* [(][A-Za-z\.]* [0-9]{4}[)]$' 
    THEN 
		SELECT SUBSTRING(holdings_statement, 	LOCATE(")", holdings_statement, 1)-4, 4) INTO this_volume_year;
    ELSE
		
		SET @thisSQL = CONCAT('SELECT MIN(LEFT(vlmStartDate, 4)) INTO @this_volume_year ');
        SET @thisSQL = CONCAT(@thisSQL, 'FROM ihsvolume V WHERE V.titleID = ', CAST(this_title_id AS CHAR), ' ');
        SET @thisSQL = CONCAT(@thisSQL, 'AND V.volumeNumber = \'', this_volume_number, '\'' );
		
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
    END IF;

	SELECT CONCAT('extract_first_year(', CAST(holdings_statement AS CHAR), ', ', CAST(this_title_id AS CHAR), ', ', this_volume_number, ') this_volume_year = ', CAST(this_volume_year AS CHAR), '.')
    AS extract_first_year_result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `extract_last_issue` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `extract_last_issue`(
	IN holdings_statement TEXT,
    IN this_title_id INT, 
    IN this_volume_number TEXT, 
	OUT return_value TEXT
)
BEGIN
	DECLARE issueStartPos INT; 
	DECLARE issueEndPos INT;
	DECLARE colonPos INT; 
	DECLARE dotVpos INT; 
    DECLARE dashVpos INT; 
    DECLARE this_volume_number TEXT; DECLARE this_volume_year TEXT;
	DECLARE targetLength INT;

    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 

	DECLARE output_message TEXT;


	SET @return_value = -9; 
	SET this_volume_year = '-9';  SET issueStartPos = -9; SET issueEndPos = -9; SET targetLength = -9;

	
	SET dashVpos = LOCATE('-v.', holdings_statement, 1);
    SET dotVpos = LOCATE('v.', holdings_statement, 2); 
    SET colonPos = LOCATE(':', holdings_statement, dashVpos);
    SET issueStartPos = colonPos+1;
    SET issueEndPos = LOCATE(' ', holdings_statement, issueStartPos);

	CALL extract_last_volume(holdings_statement, this_title_id, this_volume_number);
    IF this_volume_number IS NULL THEN
		SET this_volume_number = '-9';
	END IF;
	CALL extract_last_year(holdings_statement, this_title_id, this_volume_number, this_volume_year);
    IF this_volume_year IS NULL THEN
		SET this_volume_year = '-9';
	END IF;

	

	
    IF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]*:no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
    THEN 
		
		SET issueStartPos = LOCATE(':', holdings_statement, dashVpos)+4;
		SET issueEndPos = LOCATE(' (', holdings_statement, dashVpos);
		
    END IF;


	
	IF holdings_statement REGEXP '^v.[0-9]* [(][0-9]*[)]$'  
	OR holdings_statement REGEXP '^v.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' 
	OR holdings_statement REGEXP '^no.[0-9]*-no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' 
	OR holdings_statement REGEXP '^v.[0-9]*-v.[0-9]*$' 
	OR holdings_statement REGEXP '^[(][0-9]{4}-[0-9]{4}[)]$' 
    OR holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
    THEN 
		
		CALL get_MINMAX_issue_for_volume( this_title_id, this_volume_number, this_volume_year, 'MAX', @return_value);
	END IF;

	

	IF @return_value = -9 THEN 

		SET targetLength = issueEndPos - issueStartPos; 

		

		SELECT SUBSTRING(holdings_statement,
			issueStartPos,
			targetLength
		) INTO @return_value;

		SET @return_value = REPLACE(@return_value, 'no.', '');
        SET @return_value = REPLACE(@return_value, 'pp.', '');

       	

	END IF;

	SET return_value = @return_value;

	IF @return_value IS NOT NULL THEN
		SELECT CONCAT('extract_last_issue(', CAST(holdings_statement AS CHAR), ') @return_value = "', CAST(@return_value AS CHAR), '".') AS extract_last_issue_result;
	ELSE
		SELECT CONCAT('extract_last_issue(', CAST(holdings_statement AS CHAR), ') @return_value = NULL.') AS extract_last_issue_result;
	END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `extract_last_volume` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `extract_last_volume`(
	IN holdings_statement TEXT,
    IN this_title_id INT,
	OUT return_value TEXT
)
BEGIN
	DECLARE volStartPos INT; 
	DECLARE volEndPos INT;
	DECLARE colonPos INT; 
	DECLARE dashVpos INT; 
    DECLARE spacePos INT; 
    DECLARE year_string TEXT;
	DECLARE targetLength INT;

	DECLARE output_message TEXT;


	SET return_value = -9; 
	SET volStartPos = -9; SET volEndPos = -9; SET targetLength = -9;

	
	SET colonPos = LOCATE(':', holdings_statement, 1);
	SET dashVpos = LOCATE('-v.', holdings_statement, 1);
	SET spacePos = LOCATE(' ', holdings_statement, 1);

	

	IF holdings_statement REGEXP '^[(][0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('(', holdings_statement, 1)+1;
        SET volEndPos = LOCATE(')', holdings_statement, 1);
		
	ELSEIF holdings_statement REGEXP '^[0-9]*[(][0-9]{4}[)]$' 
		OR holdings_statement REGEXP '^[0-9]*[(][0-9]{4}$' 
    THEN 
		SET volStartPos = 1;
        SET volEndPos = LOCATE('(', holdings_statement, 1);
		
	ELSEIF holdings_statement REGEXP '^v.[0-9]* [(][0-9]{4}[)]$' 
		OR holdings_statement REGEXP '^v.[0-9]*[[:blank:]][(][0-9]{4}[[.slash.]][0-9]{4}[)]$' 
    THEN
		SET volStartPos = LOCATE('v.', holdings_statement, 1)+2;
        SET volEndPos = spacePos;
		
	ELSEIF holdings_statement REGEXP '^no.[0-9]* [(][0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('no.', holdings_statement, 1)+3;
        SET volEndPos = spacePos;
		
	ELSEIF holdings_statement REGEXP '^v.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('-v.', holdings_statement, 1)+3;
        SET volEndPos = spacePos;
		
	ELSEIF holdings_statement REGEXP '^no.[0-9]*-no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' THEN 
		SET volStartPos = LOCATE('-no.', holdings_statement, 1)+4;
        SET volEndPos = spacePos;
		
	ELSEIF holdings_statement REGEXP '^v.[0-9]*-v.[0-9]*$' THEN 
		SET volStartPos = LOCATE('-v.', holdings_statement, 1)+3;
        SET volEndPos = LENGTH(holdings_statement)+1;
		
	ELSEIF holdings_statement REGEXP '^[(][0-9]{4}-[0-9]{4}[)]$'  
		OR holdings_statement REGEXP '^[(][0-9]{4}[[.slash.]][0-9]{4}-[0-9\-]*[)]$'  
    THEN
		SET volStartPos = LOCATE('-', holdings_statement, 1)+3;
		SET volEndPos = LOCATE(')', holdings_statement, 1);
        SET year_string = SUBSTRING(holdings_statement, 7, 4);
        
        
        SET return_value = SUBSTRING(holdings_statement, 7, 4);
	ELSEIF holdings_statement REGEXP '^[0-9]{4}-[0-9]{4}$' THEN  
		SET volStartPos = LOCATE('-', holdings_statement, 1)+1;
		SET volEndPos = LENGTH(holdings_statement)+1;
    ELSEIF holdings_statement REGEXP '^v[0-9]*[[:blank:]]n[0-9]*[(][0-9\-]*[)]$' THEN 
		SET volStartPos = 2;
		SET volEndPos = LOCATE(' ', holdings_statement, 1);           
    ELSEIF holdings_statement REGEXP '^v[0-9]*[[.hyphen.]]n[0-9]*[(][0-9\-]*[)]$' THEN 
		SET volStartPos = 2;
		SET volEndPos = LOCATE('-', holdings_statement, 1);           
	ELSEIF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]* [(][A-Za-z\.]* [0-9]{4}[)]$' THEN 
		SET volStartPos = 3;
		SET volEndPos = colonPos;
    ELSEIF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]*:no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
    THEN 
		SELECT 'lastvol can handle 1' AS junk;
		SET volStartPos = dashVpos+3;
		SET volEndPos = LOCATE(':', holdings_statement, dashVpos);        
    ELSEIF holdings_statement REGEXP '^v.[0-9]*:no.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$'  
    THEN 
		SELECT 'lastvol can handle 2' AS junk;
		SET volStartPos = dashVpos+3;
		SET volEndPos = spacePos;   
	END IF;

	SET targetLength = volEndPos - volStartPos;

	
	IF return_value = -9 THEN 
		SELECT SUBSTRING(holdings_statement,
			volStartPos,
			targetLength
		) INTO return_value;
    END IF;

	SELECT CONCAT('extract_last_volume(', CAST(holdings_statement AS CHAR), ') return_value = ', CAST(return_value AS CHAR), '.')
    AS extract_last_volume_result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `extract_last_year` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `extract_last_year`(
	IN holdings_statement TEXT,
    IN this_title_id INT,
    IN this_volume_number TEXT,
	INOUT this_volume_year TEXT
)
BEGIN
	DECLARE beginPubYear TEXT;

	DECLARE output_message TEXT;


    IF  holdings_statement REGEXP '^([0-9]{4}-[0-9]{4})$' OR
    		holdings_statement REGEXP '^([0-9]{4})$' OR 
            holdings_statement REGEXP '^v.[0-9]* [(][0-9]{4}[)]' OR  
			holdings_statement REGEXP '^v.[0-9]*-v.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' OR 
            holdings_statement REGEXP '^no.[0-9]*-no.[0-9]* [(][0-9]{4}-[0-9]{4}[)]$' OR 
            holdings_statement REGEXP '^v.[0-9]*:no.[0-9]* [(][A-Za-z\.]* [0-9]{4}[)]$' 
    THEN
		SELECT SUBSTRING(holdings_statement, 	LOCATE(")", holdings_statement, 1)-4, 4) INTO this_volume_year;
    ELSE
		
		SET @thisSQL = CONCAT('SELECT MAX(LEFT(vlmStartDate, 4)) INTO this_volume_year ');
        SET @thisSQL = CONCAT(@thisSQL, 'FROM ihsvolume V WHERE V.titleID = ', CAST(this_title_id AS CHAR), ' ');
        SET @thisSQL = CONCAT(@thisSQL, 'AND V.volumeNumber = \'', this_volume_number, '\'' );
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
    END IF;

	SELECT CONCAT('extract_last_year(', CAST(holdings_statement AS CHAR), ', ', CAST(this_title_id AS CHAR), ', ', this_volume_number, ') this_volume_year = ', CAST(this_volume_year AS CHAR), '.')
    AS extract_last_year_result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_first_volume_year` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_first_volume_year`(
	IN this_title_id TEXT,
	IN year_string TEXT,
	INOUT return_value TEXT
)
BEGIN
    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 

    SET @return_value = 'blort';

	SET @thisSQL = CONCAT('SELECT LEFT(MIN(vlmStartDate), 4) INTO @return_value FROM ihsvolume ');
	SET @thisSQL = CONCAT(@thisSQL, 'WHERE titleID = ', this_title_id ,' ');
	SET @thisSQL = CONCAT(@thisSQL, 'AND vlmStartDate LIKE \'', year_string, '%\' ');
	
	PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
	SELECT @return_value AS gOt_first_volume_from_YEAR;

    SELECT 'get_first_volume_year' AS this_SP, this_title_id, year_string, @return_value AS gOt_first_volume_year_FINAL;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_last_volume_year` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_last_volume_year`(
	IN this_title_id TEXT,
	IN year_string TEXT,
	INOUT return_value TEXT
)
BEGIN
    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 

    SET @return_value = 'blort';

	SET @thisSQL = CONCAT('SELECT LEFT(MAX(vlmStartDate), 4) INTO @return_value FROM ihsvolume ');
	SET @thisSQL = CONCAT(@thisSQL, 'WHERE titleID = ', this_title_id ,' ');
	SET @thisSQL = CONCAT(@thisSQL, 'AND vlmStartDate LIKE \'', year_string, '%\' ');
	
	PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
	SELECT @return_value AS gOt_first_volume_from_YEAR;

    SELECT 'get_last_volume_year' AS this_SP, this_title_id, year_string, @return_value AS gOt_last_volume_year_FINAL;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_max_issue_for_volume` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_max_issue_for_volume`(
	IN this_title_id TEXT,
    IN this_volume_number TEXT,
    IN this_volume_year TEXT,
	INOUT return_value TEXT
)
BEGIN
    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 

	
	SET @thisSQL = CONCAT('SELECT MAX(issueNumber) INTO @return_value FROM ihsissue ');
		SET @thisSQL = CONCAT(@thisSQL, 'WHERE titleID =  ', this_title_id ,' ');
		SET @thisSQL = CONCAT(@thisSQL, 'AND volumeID = ( ');
			SET @thisSQL = CONCAT(@thisSQL, 'SELECT volumeID FROM ihsvolume ');
			SET @thisSQL = CONCAT(@thisSQL, 'WHERE titleID = ', this_title_id ,' ');
			SET @thisSQL = CONCAT(@thisSQL, 'AND volumeNumber = \'', this_volume_number, '\' ');
		 SET @thisSQL = CONCAT(@thisSQL, ') ');
	
	PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
	

	IF @return_value IS NULL THEN 
		SET @thisSQL = CONCAT('SELECT MAX(issueNumber) INTO @return_value FROM ihsissue ');
			SET @thisSQL = CONCAT(@thisSQL, 'WHERE titleID =  ', this_title_id ,' ');
			SET @thisSQL = CONCAT(@thisSQL, 'AND volumeID = ( ');
				SET @thisSQL = CONCAT(@thisSQL, 'SELECT volumeID FROM ihsvolume ');
				SET @thisSQL = CONCAT(@thisSQL, 'WHERE titleID = ', this_title_id ,' ');
				SET @thisSQL = CONCAT(@thisSQL, 'AND vlmStartDate LIKE \'', this_volume_year, '%\' ');
			 SET @thisSQL = CONCAT(@thisSQL, ') ');
		
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
        
    END IF;

    SELECT 'get_max_issue_for_volume' AS this_SP, this_title_id, this_volume_number, this_volume_year, @return_value AS gOt_max_issue_for_volume_FINAL;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_MINMAX_issue_for_volume` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_MINMAX_issue_for_volume`(
	IN this_title_id TEXT,
    IN this_volume_number TEXT,
    IN this_volume_year TEXT,
    IN min_or_max_wanted TEXT,
	INOUT return_value TEXT
)
BEGIN
    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 
    DECLARE minmaxClause TEXT;
    DECLARE titleIDclause TEXT;
    DECLARE volumeIDclause TEXT;
    DECLARE volumeNumberClause TEXT;
    DECLARE volumeYearClause TEXT;
    
    
    SET titleIDclause = CONCAT('WHERE titleID =  ', this_title_id , ' ');
    
    IF min_or_max_wanted = 'MIN' THEN 
		SET minmaxClause = CONCAT('SELECT MIN(issueNumber) INTO @return_value FROM ihsissue ', titleIDclause);
	ELSE 
		SET minmaxClause = CONCAT('SELECT MAX(issueNumber) INTO @return_value FROM ihsissue ', titleIDclause);
    END IF;

	SET volumeIDclause = CONCAT('SELECT volumeID FROM ihsvolume ', titleIDclause, ' ');
   	SET volumeNumberClause = CONCAT('volumeNumber = \'', this_volume_number, '\' ');
	SET volumeYearClause = CONCAT('vlmStartDate LIKE \'', this_volume_year, '%\' ');
	

	SET @thisSQL = CONCAT(minmaxClause, 'AND volumeID = ( ', volumeIDclause, 'AND ', volumeNumberClause, ') ');
	SELECT @thisSQL; 
	PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
    SET return_value = @return_value;
	SELECT return_value AS gOt_min_issue_for_volume_NUMBER;

	IF @return_value IS NULL THEN 
		SET @thisSQL = CONCAT(minmaxClause, 'AND volumeID = ( ', volumeIDclause, 'AND ', volumeYearClause, ') ');
		SELECT @thisSQL AS second_try_with_year; 
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;
        SET return_value = @return_value;
		SELECT return_value AS gOt_min_issue_for_volume_YEAR; 
    END IF;

    SELECT 'get_MINMAX_issue_for_volume' AS this_SP, this_title_id, this_volume_number, this_volume_year, min_or_max_wanted, return_value AS gOt_MINMAX_issue_for_volume_FINAL;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_minmax_volume_and_issue_from_OCLC` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_minmax_volume_and_issue_from_OCLC`(
	IN this_oclc_number TEXT
)
BEGIN 	
    DECLARE this_title_id INT; 	
    DECLARE min_volume_id INT; DECLARE max_volume_id INT; 
    DECLARE min_volume_number TEXT; DECLARE min_volume_year TEXT;
    DECLARE max_volume_number TEXT; DECLARE max_volume_year TEXT;
	DECLARE min_issue_number TEXT; 
	DECLARE max_issue_number TEXT; 
    
    DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 
	
	DECLARE output_message TEXT;


	SET @thisSQL = CONCAT("SELECT titleID FROM ihstitle WHERE oclcNumber = '", this_oclc_number, "' INTO @this_title_id ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	

	SET @thisSQL = CONCAT("SELECT LEFT(MIN(vlmStartDate), 4) INTO @min_volume_year FROM ihsvolume WHERE titleID = ", @this_title_id, " ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	

	SET @thisSQL = CONCAT("SELECT LEFT(MAX(vlmEndDate), 4) INTO @max_volume_year FROM ihsvolume WHERE titleID =  ", @this_title_id, " ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	

	SET @thisSQL = CONCAT("SELECT MIN(volumeID) INTO @min_volume_id FROM ihsvolume WHERE titleID = ", @this_title_id, " AND vlmStartDate LIKE '", @min_volume_year, "%' ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
    
    
	SET @thisSQL = CONCAT("SELECT MAX(volumeID) INTO @max_volume_id FROM ihsvolume WHERE titleID =  ", @this_title_id, "  AND vlmStartDate LIKE '", @max_volume_year, "%' ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	

	SET @thisSQL = CONCAT("SELECT MIN(volumeNumber) INTO @min_volume_number FROM ihsvolume WHERE titleID = ", @this_title_id, " AND volumeID = ", @min_volume_id, " ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
    
    
	SET @thisSQL = CONCAT("SELECT MAX(volumeNumber) INTO @max_volume_number FROM ihsvolume WHERE titleID = ", @this_title_id, " AND volumeID = ", @max_volume_id, " ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
    

	SET @thisSQL = CONCAT("SELECT MIN(issueNumber) INTO @min_issue_number FROM ihsissue WHERE titleID =  ", @this_title_id, "  AND volumeID = ", @min_volume_id, " ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	
    
	SET @thisSQL = CONCAT("SELECT MAX(issueNumber) INTO @max_issue_number FROM ihsissue WHERE titleID =  ", @this_title_id, " AND volumeID = ", @max_volume_id, " ");
    
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	

SELECT this_oclc_number, @this_title_id, @min_volume_id, @min_volume_year, @min_volume_number, @min_issue_number, @max_volume_id, @max_volume_year, @max_volume_number, @max_issue_number;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `IHS_insert_holdings_from_statement` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `IHS_insert_holdings_from_statement`(
	IN oclcNumber TEXT, 	
	IN memberName TEXT, 	
	IN holdings_statement TEXT
)
IHS_insert_holdings_from_statement_label:BEGIN

	DECLARE totalExecutionTimer TEXT;	DECLARE totalExecutionBegin	TIME;	DECLARE totalExecutionEnd	TIME;
	DECLARE queryExecutionBegin	TIME;	DECLARE queryExecutionEnd	TIME;	DECLARE queryExecutionTimer TEXT;

	DECLARE this_title_id INT; 	DECLARE titleIDclause TEXT;
    DECLARE min_volume_id INT; DECLARE max_volume_id INT; 
	DECLARE beginVolumeNumber TEXT; 
    DECLARE endVolumeNumber TEXT; 
    DECLARE volumeClause TEXT;
    DECLARE memberID INT;     	
    DECLARE locationID INT; 	
    
    
	DECLARE beginIssueNumber TEXT; 
	DECLARE endIssueNumber TEXT; 
    
	DECLARE beginPubYear TEXT; 
	DECLARE endPubYear TEXT; 
	DECLARE minDateClause TEXT; DECLARE minIssueClause TEXT;
	DECLARE maxDateClause TEXT; DECLARE maxIssueClause TEXT;
	
	DECLARE thisSQL 	TEXT; 	
    DECLARE thisStatement TEXT; 
	DECLARE insertClause TEXT;
	DECLARE selectClause TEXT;    
    DECLARE issueIDclause TEXT;
	
    DECLARE count_holdings_inserted INT;

	DECLARE strDebug	TEXT;	 


	SET totalExecutionBegin = CURRENT_TIME();
	
    SET totalExecutionTimer = '';

	
    SET @thisSQL = CONCAT('SELECT memberID INTO @memberID FROM ihsmember M WHERE name = \'', memberName ,'\'');
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
    
        
    SET @thisSQL = CONCAT('SELECT locationID INTO @locationID FROM ihslocation L WHERE memberID = ', CAST(@memberID AS CHAR),' LIMIT 1');
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 

	
	SET @thisSQL = CONCAT('SELECT titleID INTO @this_title_id FROM ihstitle WHERE oclcNumber = \'', oclcNumber, '\'' );
    PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	SET titleIDclause = CONCAT(' AND S.titleID = ', CAST(@this_title_id AS CHAR), ' ');

	
    
    
	SET insertClause = CONCAT('INSERT INTO crl.ihsholding ( ');
		SET insertClause = CONCAT(insertClause, 'holdingID, '); 
		SET insertClause = CONCAT(insertClause, 'issueID, '); 
		SET insertClause = CONCAT(insertClause, 'memberID, '); 
		SET insertClause = CONCAT(insertClause, 'locationID, ');  
		SET insertClause = CONCAT(insertClause, 'holdingStatusID, ');  
		SET insertClause = CONCAT(insertClause, 'conditionTypeOverallID, ');  
		SET insertClause = CONCAT(insertClause, 'validationLevelID, ');  
		SET insertClause = CONCAT(insertClause, 'ihsVarifiedID, '); 
		SET insertClause = CONCAT(insertClause, 'missingPages, '); 
		SET insertClause = CONCAT(insertClause, 'commitmentID ');  
		SET insertClause = CONCAT(insertClause, ') '); 

	SET selectClause = CONCAT('SELECT 0 AS holdingID, ');  
		SET selectClause = CONCAT(selectClause, 'issueID AS issueID, '); 
		SET selectClause = CONCAT(selectClause, CAST(@memberID AS CHAR), ' AS memberID, '); 
		SET selectClause = CONCAT(selectClause, CAST(@locationID AS CHAR), ' AS locationID, '); 
		SET selectClause = CONCAT(selectClause, '1 AS holdingStatusID, '); 
		SET selectClause = CONCAT(selectClause, '1 AS conditionTypeOverallID, ');  
		SET selectClause = CONCAT(selectClause, '1 AS validationLevelID, ');  
		SET selectClause = CONCAT(selectClause, '1 AS ihsVarifiedID, ');  
		SET selectClause = CONCAT(selectClause, 'NULL AS missingPages, ');  
		SET selectClause = CONCAT(selectClause, '1 AS commitmentID ');  
		SET selectClause = CONCAT(selectClause, 'FROM ihsissue WHERE ');     
    
    
    
	
	CALL extract_first_volume(holdings_statement, @this_title_id, beginVolumeNumber);
    IF holdings_statement LIKE '%-v.%' OR holdings_statement LIKE 'v.%' THEN 
		CALL extract_last_volume(holdings_statement, @this_title_id, endVolumeNumber);
	ELSE 
		SET endVolumeNumber = beginVolumeNumber;
    END IF;
	SELECT CONCAT(beginVolumeNumber, ' and ', endVolumeNumber, ' are the volumes in holdings_statement ', holdings_statement) AS got_volumes;
    
    
    IF ((NOT @beginVolumeNumber OR NOT @endVolumeNumber) OR (@beginVolumeNumber < 0) OR (@endVolumeNumber < 0)) THEN 
        
        SET strDebug = CONCAT('No valid volume: ');
		IF NOT @beginVolumeNumber THEN 
			SET strDebug = CONCAT(strDebug, '[not beginVolumeNumber] ');
		ELSEIF NOT @endVolumeNumber THEN 
			SET strDebug = CONCAT(strDebug, '[not endVolumeNumber] ');
        ELSEIF (@beginVolumeNumber < 0) THEN 
			SET strDebug = CONCAT(strDebug, '[beginVolumeNumber = "', CAST(@beginVolumeNumber AS CHAR),'"] ');
        ELSEIF (@endVolumeNumber < 0) THEN 
			SET strDebug = CONCAT(strDebug, '[endVolumeNumber = "', CAST(@endVolumeNumber AS CHAR),'"] ');
		END IF;
		SELECT CONCAT("No valid volumes for IHS_insert_holdings_from_statement('", oclcNumber, '", "', memberName, '", "', holdings_statement, '", "', strDebug,'" );') AS 5_load_failed_for_lack_of_volumes;
			SET @thisSQL = CONCAT('INSERT INTO 5_load_holdings_failures (`oclcNumber`,`member_name`,`holdings_statement`,`failure_cause`,`failure_date`) ');
			SET @thisSQL = CONCAT(@thisSQL, 'VALUES ("', oclcNumber, '", "', memberName, '", "', holdings_statement, '", ');
			SET @thisSQL = CONCAT(@thisSQL, '"', strDebug,'", \'', NOW(), '\' ) ');
		SELECT @thisSQL AS sql_5_load_holdings_failures;
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
        LEAVE IHS_insert_holdings_from_statement_label; 
	END IF; 
    SET strDebug = '';
    
	
	SET volumeClause = CONCAT(
		'SELECT V.volumeID FROM ihsvolume V WHERE V.titleID = ', CAST(@this_title_id AS CHAR), ' AND (V.volumeNumber BETWEEN \'', beginVolumeNumber,'\' AND \'', endVolumeNumber,'\' ) '
	);
	

	
    IF holdings_statement REGEXP '([0-9]{4}-[0-9]{4})' THEN 
		SELECT CONCAT(holdings_statement, ' appears to match pattern A: call extract_first_year/extract_last_year.') AS call_extr_years_from_5_load;
		CALL extract_first_year(holdings_statement, @this_title_id, beginVolumeNumber, beginPubYear);
		CALL extract_last_year(holdings_statement, @this_title_id, endVolumeNumber, endPubYear);
    ELSE 
		
		SET @thisSQL = CONCAT('SELECT MIN(LEFT(vlmStartDate, 4)) INTO @beginPubYear FROM ihsvolume V WHERE V.titleID = ', CAST(@this_title_id AS CHAR), ' AND V.volumeNumber = \'', beginVolumeNumber, '\'' );
SELECT @thisSQL AS bgnPubYear_sql;        
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
		SET beginPubYear = @beginPubYear;
        SET @thisSQL = CONCAT('SELECT MAX(LEFT(vlmStartDate, 4)) INTO @endPubYear FROM ihsvolume V WHERE V.titleID = ', CAST(@this_title_id AS CHAR), ' AND volumeNumber = \'', endVolumeNumber, '\'' );
SELECT @thisSQL AS endPubYear_sql;        
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
        SET endPubYear = @endPubYear;
    END IF;
	SELECT CONCAT(beginPubYear, ' and ', endPubYear, ' are the years in holdings_statement ', holdings_statement) AS got_years;	
	SET minDateClause = CONCAT('SELECT MIN(publicationDate) FROM ihsissue S WHERE S.publicationDate >= \'', DATE(CONCAT(beginPubYear, '-01-01')), '\' ');
	SET maxDateClause = CONCAT('SELECT MAX(publicationDate) FROM ihsissue S WHERE S.publicationDate <= \'', DATE(CONCAT(endPubYear, '-12-31')), '\' ');



	
	CALL extract_first_issue(holdings_statement, @this_title_id, beginVolumeNumber, @beginIssueNumber);
	CALL extract_last_issue(holdings_statement, @this_title_id, endVolumeNumber, @endIssueNumber);
	SELECT CONCAT(CAST(@beginIssueNumber AS CHAR), ' and ', CAST(@endIssueNumber AS CHAR), ' are the issue numbers in ', holdings_statement) 
    	AS got_issue_numbers;
        
    IF ((NOT @beginIssueNumber OR NOT @endIssueNumber) OR (@beginIssueNumber < 0) OR (@endIssueNumber < 0)) THEN 
        
        SET strDebug = CONCAT('No valid issue: ');
		IF NOT @beginIssueNumber THEN 
			SET strDebug = CONCAT(strDebug, '[not beginIssueNumber] ');
		ELSEIF NOT @endIssueNumber THEN 
			SET strDebug = CONCAT(strDebug, '[not endIssueNumber] ');
        ELSEIF (@beginIssueNumber < 0) THEN 
			SET strDebug = CONCAT(strDebug, '[beginIssueNumber = "', CAST(@beginIssueNumber AS CHAR),'"] ');
        ELSEIF (@endIssueNumber < 0) THEN 
			SET strDebug = CONCAT(strDebug, '[endIssueNumber = "', CAST(@endIssueNumber AS CHAR),'"] ');
		END IF;
		SELECT CONCAT("No valid issue for IHS_insert_holdings_from_statement('", oclcNumber, '", "', memberName, '", "', holdings_statement, '", "', strDebug,'" );') AS 5_load_failed_for_lack_of_issue;
			SET @thisSQL = CONCAT('INSERT INTO 5_load_holdings_failures (`oclcNumber`,`member_name`,`holdings_statement`,`failure_cause`,`failure_date`) ');
			SET @thisSQL = CONCAT(@thisSQL, 'VALUES ("', oclcNumber, '", "', memberName, '", "', holdings_statement, '", ');
			SET @thisSQL = CONCAT(@thisSQL, '"', strDebug,'", \'', NOW(), '\' ) ');
		SELECT @thisSQL AS sql_5_load_holdings_failures;
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
        LEAVE IHS_insert_holdings_from_statement_label; 
    ELSE 
    
	
		SET minIssueClause = CONCAT('AND S.issueNumber = \'', @beginIssueNumber, '\' ');
		SET maxIssueClause = CONCAT('AND S.issueNumber = \'', @endIssueNumber, '\' ');

		
		
		
		SET issueIDclause = 'issueID IN ( '; 
		SET issueIDclause = CONCAT(issueIDclause, 'SELECT issueID FROM ihsissue S '); 
		SET issueIDclause = CONCAT(issueIDclause, 'WHERE ( S.titleID = ', @this_title_id, ' ) '); 
		SET issueIDclause = CONCAT(issueIDclause, 'AND ( S.volumeID IN ( ', volumeClause, ') ');
		
		SET issueIDclause = CONCAT(issueIDclause, 'AND (  ');
		SET issueIDclause = CONCAT(issueIDclause, ' S.publicationDate ');
		SET issueIDclause = CONCAT(issueIDclause, ' BETWEEN ( ', minDateClause, ' ');
		

		IF @beginIssueNumber > 0 THEN 
			SET issueIDclause = CONCAT(issueIDclause, minIssueClause, ' ');
		END IF;
		SET issueIDclause = CONCAT(issueIDclause, titleIDclause, ' ');
		SET issueIDclause = CONCAT(issueIDclause, ' ) '); 
		SET issueIDclause = CONCAT(issueIDclause, '  AND ( ', maxDateClause, ' ');

		

		IF @endIssueNumber < 99999999 THEN 
			SET issueIDclause = CONCAT(issueIDclause, maxIssueClause, ' ');
		END IF;
		SET issueIDclause = CONCAT(issueIDclause, titleIDclause, ' ');
		SET issueIDclause = CONCAT(issueIDclause, ' ) '); 
		SET issueIDclause = CONCAT(issueIDclause, ' ) '); 
		SET issueIDclause = CONCAT(issueIDclause, ' ) '); 
		SET issueIDclause = CONCAT(issueIDclause, ') '); 
		

		
		SET @thisSQL = CONCAT(insertClause, ' ', selectClause, ' ', issueIDclause);
		
		SET queryExecutionBegin = CURRENT_TIME();
	PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	
		SET queryExecutionEnd = CURRENT_TIME(); 

		
		SET @thisSQL = CONCAT('SELECT COUNT(*) INTO @count_holdings_inserted FROM ihsholding ');
		SET @thisSQL = CONCAT(@thisSQL, 'WHERE memberID = ', CAST(@memberID AS CHAR), ' AND ', issueIDclause);
		
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	
    END IF;

        
	SET totalExecutionEnd = CURRENT_TIME();
	SET totalExecutionTimer = CONCAT('IHS_insert_holdings_from_statement INSERTED ', CAST(@count_holdings_inserted AS CHAR) ,' records for ', oclcNumber, ', ', memberName, ', ', holdings_statement, '. '); 
	SET totalExecutionTimer = CONCAT(totalExecutionTimer, CAST(TIMEDIFF(totalExecutionEnd, totalExecutionBegin) AS CHAR), ' elapsed. EXIT.');

    IF ((@beginIssueNumber AND @endIssueNumber) AND (@beginIssueNumber > 0) AND (@endIssueNumber > 0)) THEN 
			SET @thisSQL = CONCAT('INSERT INTO 5_load_holdings_success (`oclcNumber`,`member_name`,`holdings_statement`, ');
            SET @thisSQL = CONCAT(@thisSQL, '`count_holdings_inserted`,`success_note`,`success_date`) ');
			SET @thisSQL = CONCAT(@thisSQL, 'VALUES ("', oclcNumber, '", "', memberName, '", "', holdings_statement, '", ');
			SET @thisSQL = CONCAT(@thisSQL, @count_holdings_inserted, ', "', totalExecutionTimer ,'", \'', NOW(), '\' ) ');
		SELECT @thisSQL AS sql_5_load_holdings_success;
		PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement; 
	END IF;
    
	SELECT totalExecutionTimer;    
    
	END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `IHS_insert_holdings_volume_to_volume` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `IHS_insert_holdings_volume_to_volume`(
	IN oclcNumber TEXT, 	
	IN memberName TEXT, 	
	IN beginVolumeNumber TEXT, 
	IN endVolumeNumber TEXT 
)
BEGIN

	DECLARE totalExecutionTimer TEXT;	DECLARE totalExecutionBegin	TIME;	DECLARE totalExecutionEnd	TIME;
	DECLARE queryExecutionBegin	TIME;	DECLARE queryExecutionEnd	TIME;	DECLARE queryExecutionTimer TEXT;

	DECLARE table_name	TEXT;	DECLARE outfileName	TEXT;	DECLARE outputTableName TEXT;

	DECLARE thisSQL 	TEXT; 	
	DECLARE insertClause TEXT;
	DECLARE selectClause TEXT;
	DECLARE thisStatement TEXT; 

	DECLARE strDebug	TEXT;	 


	SET totalExecutionBegin = CURRENT_TIME();
	SET totalExecutionTimer = CONCAT('IHS_insert_holdings ', oclcNumber, ', ', memberName, ', ', beginVolumeNumber, '-', endVolumeNumber,' ; from ', CAST(totalExecutionBegin AS CHAR), '-');

	SET table_name = 'ihsholding';

	SET insertClause = CONCAT('INSERT INTO crl.ihsholding ( ');
		SET insertClause = CONCAT(insertClause, 'holdingID, '); 
		SET insertClause = CONCAT(insertClause, 'issueID, '); 
		SET insertClause = CONCAT(insertClause, 'memberID, ');
		SET insertClause = CONCAT(insertClause, 'locationID, '); 
		SET insertClause = CONCAT(insertClause, 'holdingStatusID, '); 
		SET insertClause = CONCAT(insertClause, 'conditionTypeOverallID, '); 
		SET insertClause = CONCAT(insertClause, 'validationLevelID, '); 
		SET insertClause = CONCAT(insertClause, 'ihsVarifiedID, '); 
		SET insertClause = CONCAT(insertClause, 'missingPages, '); 
		SET insertClause = CONCAT(insertClause, 'commitmentID '); 
		SET insertClause = CONCAT(insertClause, ') '); 

	SET selectClause = CONCAT('SELECT 0 AS holdingID, '); 
	SET selectClause = CONCAT(selectClause, 'issueID AS issueID, '); 
	SET selectClause = CONCAT(selectClause, '(SELECT memberID FROM ihsmember M WHERE name = \'', memberName ,'\' ) AS memberID, '); 
			
	SET selectClause = CONCAT(selectClause, '(SELECT locationID FROM ihslocation L WHERE memberID = ');
		SET selectClause = CONCAT(selectClause, '(SELECT memberID FROM ihsmember M WHERE name = \'', memberName ,'\' ) LIMIT 1) ');
		SET selectClause = CONCAT(selectClause, 'AS locationID, ');	
	SET selectClause = CONCAT(selectClause, '1 AS holdingStatusID, '); 
	SET selectClause = CONCAT(selectClause, '1 AS conditionTypeOverallID, '); 
	SET selectClause = CONCAT(selectClause, '1 AS validationLevelID, '); 
	SET selectClause = CONCAT(selectClause, '1 AS ihsVarifiedID, '); 
	SET selectClause = CONCAT(selectClause, 'NULL AS missingPages, '); 
	SET selectClause = CONCAT(selectClause, '1 AS commitmentID '); 
	SET selectClause = CONCAT(selectClause, 'FROM ihsissue ');
	SET selectClause = CONCAT(selectClause, 'WHERE issueID IN ( ');
	SET selectClause = CONCAT(selectClause, 'SELECT issueID FROM ihsissue S ');
	SET selectClause = CONCAT(selectClause, 'WHERE ( S.volumeID IN ( ');
						
					SET selectClause = CONCAT(selectClause, 'SELECT V.volumeID FROM ihsvolume V ');
					SET selectClause = CONCAT(selectClause, ' WHERE V.volumeNumber BETWEEN \'', beginVolumeNumber ,'\' AND \'', endVolumeNumber,'\' ');
					SET selectClause = CONCAT(selectClause, ' ) ');
						
			SET selectClause = CONCAT(selectClause, 'AND titleID = ');
				SET selectClause = CONCAT(selectClause, '(SELECT T.titleID FROM ihstitle T WHERE T.oclcNumber = \'', oclcNumber ,'\' ) ');
		SET selectClause = CONCAT(selectClause, ' ) ');
SET selectClause = CONCAT(selectClause, ' ) ');


	
	SET @thisSQL = CONCAT(insertClause, ' ', selectClause);
	
	SELECT CAST(@totalExecutionTimer AS CHAR) AS sql_has_been_built_and_will_run_next;
	SET queryExecutionBegin = CURRENT_TIME();
	PREPARE thisStatement FROM @thisSQL; EXECUTE thisStatement; DEALLOCATE PREPARE thisStatement;

	SET queryExecutionEnd = CURRENT_TIME();
	SET queryExecutionTimer = CONCAT('IHS_insert_holdings INSERT complete: ', oclcNumber, ', ', memberName, ', vols.', beginVolumeNumber, '-', endVolumeNumber,'.');
	SET totalExecutionEnd = CURRENT_TIME();
	SET totalExecutionTimer = CONCAT(queryExecutionTimer, ' ; ', totalExecutionTimer, '-', 	CAST(totalExecutionEnd AS CHAR), ' ; elapsed: ', CAST(TIMEDIFF(totalExecutionEnd, totalExecutionBegin) AS CHAR), '. EXIT.');
	SELECT totalExecutionTimer;
	END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-09 13:56:45
