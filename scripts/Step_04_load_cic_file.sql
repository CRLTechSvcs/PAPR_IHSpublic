
insert into crl.ihspublisher  (name, description, startDate, addressID) 
select  distinct trim(publisher), '', now(), 1 from data.tmp_article a
left outer join  crl.ihspublisher b on a.publisher = b.name
where name is null;


insert into crl.ihstitle(titleTypeID, title, alphaTitle, printISSN, eISSN, oclcNumber ,  publisherID , description , titleStatusID, changeDate, userId, Language, Country)
select 1, title, title, Replace(print_ISSN,'-',''), Replace(electronic_issn,'-','') , oclc_Number, publisherID, '', 1, now(), 1,  Language, Country  from data.tmp_article
join  crl.ihspublisher on name = publisher;



insert into crl.ihspublicationrange(titleID, periodicityTypeID, pbrStartDate, pbrEndDate)
select titleId,1, concat(pub_start_year, '-01-01') , 
concat(pub_end_year, '-12-31') from data.tmp_article
join crl.ihstitle on oclc_Number = oclcNumber ;


insert into crl.ihsvolume(titleID, volumeNumber, vlmStartDate, vlmEndDate)
select titleID,  
first_level_number,
concat(SUBSTR(year_list,1,4),'-01-01') startYear,
concat(SUBSTR(year_list,CHAR_LENGTH(year_list)-3,4),'-12-31') endYear
from (
select  
titleID,
first_level_number,
GROUP_CONCAT(DISTINCT year ORDER BY year) AS year_list 
from data.tmp_issue_idx
join crl.ihstitle on oclc_Number = oclcNumber 
group by titleID, first_level_number
) a;





insert into crl.ihsissue( titleID, volumeID, publicationRangeID, publicationDate, issueNumber, issueStatusID)
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
	second_level_number,
	1 
from 
(
select oclc_Number, year, month, first_level_number, second_level_number 
from data.tmp_issue_idx a 
-- where id between 1 and 25000
) a
join crl.ihstitle b on oclc_Number = oclcNumber
join crl.ihsvolume c on c.volumeNumber = a.first_level_number
	and b.titleID = c.titleID
join crl.ihspublicationrange  d on b.titleID = d.titleID;