-- DROP DATABASE IF EXISTS `data`;

-- CREATE DATABASE `data`;

use `data`;
drop table tmp_article;
drop table tmp_issue;
drop table tmp_issue_idx ;

create table tmp_article(
OCLC_Number	varchar(1000),
Print_ISSN	varchar(1000),
Electronic_ISSN	varchar(1000),
Title	varchar(1000),
Publisher	varchar(1000),
Pub_Start_Year varchar(1000),
Pub_End_Year varchar(1000),	
Language	varchar(1000),
Country	varchar(1000),
LC_Class varchar(1000),
volumeLevelFlag varchar(1000)
);

create table tmp_issue (
oclc_number	varchar(1000),
year	varchar(1000),
month	varchar(1000),
day	varchar(1000),
series_caption	varchar(1000),
series_number	varchar(1000),
first_level_caption	varchar(1000),
first_level_number	varchar(1000),
second_level_caption	varchar(1000),
second_level_number	varchar(1000),
third_level_caption	varchar(1000),
third_level_number	varchar(1000),
issue_type	varchar(1000),
issue_title varchar(1000)
);

create table tmp_issue_idx (
id int(11) NOT NULL AUTO_INCREMENT,
oclc_number	varchar(1000),
year	varchar(1000),
month	varchar(1000),
volumeNumber	varchar(1000),
issueNumber	varchar(1000),
PRIMARY KEY (`id`)
);


-- LOAD DATA LOCAL INFILE 'C:\\travant\\data\\release\\data\\bib-level-data.csv' into table tmp_article   FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';

-- LOAD DATA LOCAL INFILE 'C:\\travant\\data\\release\\data\\issue-level-data.csv' into table tmp_issue  CHARACTER SET 'latin1' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';


LOAD DATA LOCAL INFILE 'C:\\travant\\data\\release\\data\\volume-only-bib-data.csv' into table tmp_article   FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE 'C:\\travant\\data\\release\\data\\volume-only-issue-data.csv' into table tmp_issue  CHARACTER SET 'latin1' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';


insert into  tmp_issue_idx( oclc_number, year, month, volumeNumber, issueNumber ) 
select oclc_number, year, month, first_level_number, second_level_number 
from tmp_issue;


-- check the value of the tmp tables

-- select * from tmp_article;
-- select * from data.tmp_issue;
-- select * from tmp_issue_idx;
