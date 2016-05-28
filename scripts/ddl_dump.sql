-- MySQL dump 10.13  Distrib 5.6.30, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: crl_test
-- ------------------------------------------------------
-- Server version	5.6.30-0ubuntu0.14.04.1

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
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
-- Table structure for table `ihsdeaccessionJob`
--

DROP TABLE IF EXISTS `ihsdeaccessionJob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihsdeaccessionJob` (
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
  CONSTRAINT `fk_ihsholding_sconditionTypeOverall_conditionTypeOverallID` FOREIGN KEY (`conditionTypeOverallID`) REFERENCES `sconditionTypeOverall` (`conditionTypeOverallID`),
  CONSTRAINT `fk_ihsholding_sholdingstatus_holdingStatusID` FOREIGN KEY (`holdingStatusID`) REFERENCES `sholdingstatus` (`holdingStatusID`),
  CONSTRAINT `fk_ihsholding_sihsVarified_commitmentID` FOREIGN KEY (`commitmentID`) REFERENCES `scommitment` (`commitmentID`),
  CONSTRAINT `fk_ihsholding_sihsVarified_ihsVarifiedID` FOREIGN KEY (`ihsVarifiedID`) REFERENCES `sihsVarified` (`ihsVarifiedID`),
  CONSTRAINT `fk_ihsholding_svalidationLevel_validationLevelID` FOREIGN KEY (`validationLevelID`) REFERENCES `svalidationLevel` (`validationLevelID`)
) ENGINE=InnoDB AUTO_INCREMENT=122141 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=227334 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=867 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=356 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ihspublishingJob`
--

DROP TABLE IF EXISTS `ihspublishingJob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihspublishingJob` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
  KEY `printISSN_FI_1` (`printISSN`),
  KEY `oclcNumber_FI_1` (`oclcNumber`),
  KEY `fk_ihstitle_ihspublisher_publisherID` (`publisherID`),
  KEY `fk_ihstitle_stitletype_titleTypeID` (`titleTypeID`),
  KEY `fk_ihstitle_stitlestatus_titleStatusID` (`titleStatusID`),
  FULLTEXT KEY `title_ft` (`title`),
  CONSTRAINT `fk_ihstitle_stitletype_titleTypeID` FOREIGN KEY (`titleTypeID`) REFERENCES `stitletype` (`titleTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=867 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=47358 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
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
-- Table structure for table `sconditionTypeOverall`
--

DROP TABLE IF EXISTS `sconditionTypeOverall`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sconditionTypeOverall` (
  `conditionTypeOverallID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`conditionTypeOverallID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
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
-- Table structure for table `sihsVarified`
--

DROP TABLE IF EXISTS `sihsVarified`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sihsVarified` (
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sstateProvince`
--

DROP TABLE IF EXISTS `sstateProvince`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sstateProvince` (
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
-- Table structure for table `svalidationLevel`
--

DROP TABLE IF EXISTS `svalidationLevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `svalidationLevel` (
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-28  3:24:32
