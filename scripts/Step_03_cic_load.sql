DROP DATABASE IF EXISTS `data`;

CREATE DATABASE `data`;

use `data`;
-- drop table data.tmp_article;
-- drop table data.tmp_issue;
-- drop table data.tmp_issue_idx ;

create table data.tmp_article(
OCLC_Number	varchar(1000),
Print_ISSN	varchar(1000),
Electronic_ISSN	varchar(1000),
Title	varchar(1000),
Publisher	varchar(1000),
Pub_Start_Year varchar(1000),
Pub_End_Year varchar(1000),	
Language	varchar(1000),
Country	varchar(1000),
LC_Class varchar(1000)
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

create table data.tmp_issue_idx (
id int(11) NOT NULL AUTO_INCREMENT,
oclc_number	varchar(1000),
year	varchar(1000),
month	varchar(1000),
first_level_number	varchar(1000),
second_level_number	varchar(1000),
PRIMARY KEY (`id`)
);


LOAD DATA LOCAL INFILE 'C:\\travant\\data\\ihs\\CIC_data_for_IHS_artcle_utf8.txt' into table tmp_article   FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE 'C:\\travant\\data\\ihs\\CIC_data_for_IHS_issue.txt' into table tmp_issue  CHARACTER SET 'latin1' FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\r\n';

insert into  data.tmp_issue_idx( oclc_number, year, month, first_level_number, second_level_number ) 
select oclc_number, year, month, first_level_number, second_level_number 
from data.tmp_issue;
