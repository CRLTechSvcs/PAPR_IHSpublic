
use crl_test;

-- Populate the custome date tables

insert into spublicationdate ( publicationDateTypeId ,publicationDateVal,publicationDateSeq)  
select 1, month, 1 from (

select distinct month month from data.tmp_issue

where lower(month) not in ('january ', 'february', 'march ', 'april', 'may', 
					      'june', 'july', 'august', 'september', 'october', 
						   'november', 'december')
and month <> ''
) tmp 
where lower(month) not in (select lower(publicationDateVal) from spublicationdate);


-- Populate the publisher 
insert into ihspublisher  (name, description, startDate, addressID) 
select  distinct trim(publisher), '', now(), 1 
from data.tmp_article a
left outer join  ihspublisher b on a.publisher = b.name
where name is null;

-- Populate the title 

insert into ihstitle(titleTypeID, title, alphaTitle, printISSN, eISSN, oclcNumber ,  publisherID , description , titleStatusID, changeDate, userId, language, country, volumeLevelFlag)
select 1, title, title, Replace(print_ISSN,'-',''), Replace(electronic_issn,'-','') , oclc_Number, publisherID, '', 1, now(), 1  , language, country, volumeLevelFlag
from data.tmp_article
join  ihspublisher on name = publisher;

-- Populate the Publication Range

insert into ihspublicationrange(titleID, periodicityTypeID, pbrStartDate, pbrEndDate)
select titleId,1, concat(pub_start_year, '-01-01') , 
concat(pub_end_year, '-12-31') from data.tmp_article
join ihstitle on oclc_Number = oclcNumber;


-- Populate the Volume

insert into ihsvolume(titleID, volumeNumber, vlmStartDate, vlmEndDate)
select titleID,  
volumeNumber,
concat(SUBSTR(year_list,1,4),'-01-01') startYear,
concat(SUBSTR(year_list,CHAR_LENGTH(year_list)-3,4),'-12-31') endYear
from (
select  
titleID,
volumeNumber,
GROUP_CONCAT(DISTINCT year ORDER BY year) AS year_list 
from data.tmp_issue_idx
join ihstitle on oclc_Number = oclcNumber 
group by titleID, volumeNumber
) a;



-- issue with regular date

insert into ihsissue( titleID, volumeID, publicationRangeID, publicationDate, issueNumber, issueStatusID)
select
	b.titleID as titleID,
	c.volumeID,
	publicationRangeID,
	case lower(substr(month, 1, 3))
		when 'jan' then concat(concat(substr(year,1,4), '-01'),'-01')
		when 'feb' then concat(concat(substr(year,1,4), '-02'),'-01')
		when 'mar' then concat(concat(substr(year,1,4), '-03'),'-01')
		when 'apr' then concat(concat(substr(year,1,4), '-04'),'-01')
		when 'may' then concat(concat(substr(year,1,4), '-05'),'-01')
		when 'jun' then concat(concat(substr(year,1,4), '-06'),'-01')
		when 'jul' then concat(concat(substr(year,1,4), '-07'),'-01')
		when 'aug' then concat(concat(substr(year,1,4), '-08'),'-01')
		when 'sep' then concat(concat(substr(year,1,4), '-09'),'-01')
		when 'oct' then concat(concat(substr(year,1,4), '-10'),'-01')
		when 'nov' then concat(concat(substr(year,1,4), '-11'),'-01')
		when 'dec' then concat(concat(substr(year,1,4), '-12'),'-01')
	end as dt,
	issueNumber,
	1 
from 
(
select oclc_Number, year, month, volumeNumber, issueNumber  
from data.tmp_issue_idx a 
where lower(month) in ('january ', 'february', 'march ', 'april', 'may', 
					      'june', 'july', 'august', 'september', 'october', 
						   'november', 'december')
) a

join ihstitle b on a.oclc_Number = b.oclcNumber and  volumeLevelFlag = '0'
join ihsvolume c on c.volumeNumber = a.volumeNumber
	and b.titleID = c.titleID
join ihspublicationrange  d on b.titleID = d.titleID;

-- issue with custom date

 insert into ihsissue( titleID, volumeID, publicationRangeID, publicationDateId, issueNumber, issueStatusID)

select
	b.titleID as titleID,
	c.volumeID,
	publicationRangeID,
    publicationDateId,
	issueNumber,
	1
from 
(
select oclc_Number, year, publicationDateId, publicationDateVal, volumeNumber, issueNumber 
from data.tmp_issue_idx a 
join spublicationdate on lower(publicationDateVal) = lower(month)

where lower(month) not in ('january ', 'february', 'march ', 'april', 'may', 
					      'june', 'july', 'august', 'september', 'october', 
						   'november', 'december')

) a

join ihstitle b on a.oclc_Number = b.oclcNumber and  volumeLevelFlag = '0'
join ihsvolume c on c.volumeNumber = a.volumeNumber
	and b.titleID = c.titleID
join ihspublicationrange  d on b.titleID = d.titleID;

-- issue for volume level title 

insert into crl.ihsissue( titleID, volumeID, publicationRangeID, issueNumber, issueStatusID)
select
	b.titleID as titleID,
	c.volumeID,
    publicationRangeID,
	issueNumber,
	1
from 
(
select oclc_Number, year,  volumeNumber, issueNumber 
from data.tmp_issue_idx a 
) a

join ihstitle b on a.oclc_Number = b.oclcNumber and  volumeLevelFlag = '1'
join ihsvolume c on c.volumeNumber = a.volumeNumber
	and b.titleID = c.titleID
join ihspublicationrange  d on b.titleID = d.titleID;