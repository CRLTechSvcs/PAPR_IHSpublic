use crl;

insert into smemberstatus (name, description) values ('Active', 'Active');


insert into smemberstatus (name, description) values ('Inactive', 'Inactive');



insert into ihsmembergroup (name, description) values ('PAPR_GROUP', 'PAPR_GROUP');




insert into ihsaddress(address1, city, stateOrProvence, postalCode, country) values ('address1', 'city', 'stateOrProvence', 'postalCode', 'country');

insert into ihsmember (name, description, memberStatusID, addressID, ftpdirectory, membergroupID ) values ('PAPR', 'PAPR', 1,1, 'papr', 1 );



insert into ihslocation(name,description,memberID,addressID) values ('location','location',1,1);


insert into suserstatus(name, description) values ('Active', 'Active');
insert into suserstatus(name, description) values ('Inactive', 'Inactive');

insert into ihsuser(firstName, lastName, memberID, userStatusID, userName, password) values  ('Admin', 'Admin', 1, 1, 'admin', '5f4dcc3b5aa765d61d8327deb882cf99');


insert into ihssecurityrole(name, description) values('user','user');
insert into ihssecurityrole(name, description) values('admin','admin');

insert into ihsuser_ihssecurityrole(ihsuser_userID,ihssecurityrole_securityRoleId) values (1,1);
insert into ihsuser_ihssecurityrole(ihsuser_userID,ihssecurityrole_securityRoleId) values (1,2);


insert into ihsauthorizedsource(name, description, memberID ) values ('PAPR', 'PAPR', 1);

insert into sissuestatus (name, description) values ('Default','Default');
insert into sholdingstatus(name, description) values ('Default','Default');
insert into stitlestatus(name, description) values ('Default','Default'	);
insert into stitletype(name, description) values ('Default','Default');
insert into speriodicitytype(name, description, intervalsPerYear) values ('Default','Default', 12);
insert into speriodicitytype(name, description, intervalsPerYear) values ('Monthly','Monthly 11 issue', 12);
insert into speriodicitytype(name, description, intervalsPerYear) values ('Quater','Quater 4 issues', 4);
insert into speriodicitytype(name, description, intervalsPerYear) values ('Single','Single i issue', 1);


insert into singestionjobstatus(name, description) values ('Queued', 'Queued');
insert into singestionjobstatus(name, description) values ('Processing', 'Processing');

insert into singestionjobstatus(name, description) values ('File Processing Error', 'File Processing Error');

insert into singestionjobstatus(name, description) values ('Complete', 'Complete');
insert into singestionjobstatus(name, description) values ('Incomplete', 'Incomplete'); 
insert into singestionjobstatus(name, description) values ('Internal Error', 'Internal Error');

insert into singestionrecordstatus(name, description) values ('Available', 'Available');
insert into singestionrecordstatus(name, description) values ('OnHold', 'OnHold');
insert into singestionrecordstatus(name, description) values ('Processing', 'Processing');

insert into singestionrecordstatus(name, description) values ('BadRecord', 'BadRecord');

insert into singestionrecordstatus(name, description) values ('Ingnored', 'Ingnored');
insert into singestionrecordstatus(name, description) values ('Complete', 'Complete');

insert into singestiondatatype(name, description) values ('PAPR', 'PAPR Csv File (.csv)');
insert into singestiondatatype(name, description) values ('Portico', 'Portico Csv File (.csv)');

insert into singestionexceptiontype(name, description) values ('BadRecordFormat','BadRecordFormat');
insert into singestionexceptiontype(name, description) values ('BadPrintISSN','BadPrintISSN');
insert into singestionexceptiontype(name, description) values ('BadYearFormat','BadYearFormat');
insert into singestionexceptiontype(name, description) values ('BadHoldingFormat','BadHoldingFormat');
insert into singestionexceptiontype(name, description) values ('NoAuthorizedTitle','NoAuthorizedTitle');
insert into singestionexceptiontype(name, description) values ('NoAuthorizedPublicationRange','NoAuthorizedPublicationRange');
insert into singestionexceptiontype(name, description) values ('NoAuthorizedVolume','NoAuthorizedVolume');
insert into singestionexceptiontype(name, description) values ('MissingAuthorizedVolume','MissingAuthorizedVolume');


insert into singestionexceptionstatus(name, description) values ('Available','Available');
insert into ihspublisher(name, description, startDate,endDate,addressID) values ('apublisher', 'publisher', now(), now(), 1);
-- insert into ihspublisher(name, description, startDate,endDate,addressID) values ('bpublisher2', 'publisher', now(), now(), 1);
-- insert into ihspublisher(name, description, startDate,endDate,addressID) values ('cpublisher', 'publisher', now(), now(), 1);

insert into svalidationLevel(name, description, level) values ('None','None', 1);
insert into svalidationLevel(name, description, level) values ('TitleLevel','TitleLevel', 2);
insert into svalidationLevel(name, description, level) values ('VolumeLevel','VolumeLevel', 3);
insert into svalidationLevel(name, description, level) values ('IssueLevel','IssueLevel', 4);
insert into svalidationLevel(name, description, level) values ('PageLevel','PageLevel', 5);

insert into scommitment(name, description) values ('Uncommitted','Uncommitted');
insert into scommitment(name, description) values ('Committed','Committed');
insert into scommitment(name, description) values ('Electronic','Electronic');


insert into sihsVarified(name, description) values ('No','No');
insert into sihsVarified(name, description) values ('Yes','Yes');

insert into sconditionTypeOverall(name, description) values ('Unknown','Unknown');
insert into sconditionTypeOverall(name, description) values ('Poor','Poor');
insert into sconditionTypeOverall(name, description) values ('Good','Good');
insert into sconditionTypeOverall(name, description) values ('Excellent','Excellent');

insert into sconditiontype(name, description) values ('Acidic Paper','Acidic Paper');
insert into sconditiontype(name, description) values ('Binding patterns vary','Binding patterns vary');
insert into sconditiontype(name, description) values ('Browning pages','Browning pages');
insert into sconditiontype(name, description) values ('Faded','Faded');
insert into sconditiontype(name, description) values ('Highlighting','Highlighting');
insert into sconditiontype(name, description) values ('Loose','Loose');
insert into sconditiontype(name, description) values ('Missing','Missing');
insert into sconditiontype(name, description) values ('Obscured text Block','Obscured text Block');
insert into sconditiontype(name, description) values ('Rehouse poorly','');
insert into sconditiontype(name, description) values ('Repaired soundly','');
insert into sconditiontype(name, description) values ('Stainded','');
insert into sconditiontype(name, description) values ('Torn','');
insert into sconditiontype(name, description) values ('Warped','');
insert into sconditiontype(name, description) values ('Alkaline paper','');
insert into sconditiontype(name, description) values ('Brittle paper','');
insert into sconditiontype(name, description) values ('Cockled','');
insert into sconditiontype(name, description) values ('Marginalia','');
insert into sconditiontype(name, description) values ('Mold damaged','');
insert into sconditiontype(name, description) values ('Rebacked','');
insert into sconditiontype(name, description) values ('Repaired poorly','');
insert into sconditiontype(name, description) values ('Reprints','');
insert into sconditiontype(name, description) values ('Underlining','');
insert into sconditiontype(name, description) values ('Yellowed pages','');

insert into scountry(name) values ('United States');
insert into scountry(name) values ('Canada');

insert into sstateProvince(name,countryId) values ('Alabama','1');
insert into sstateProvince(name,countryId) values ('Alaska','1');
insert into sstateProvince(name,countryId) values ('Arizona','1');
insert into sstateProvince(name,countryId) values ('Arkansas','1');
insert into sstateProvince(name,countryId) values ('California','1');
insert into sstateProvince(name,countryId) values ('Colorado','1');
insert into sstateProvince(name,countryId) values ('Connecticut','1');
insert into sstateProvince(name,countryId) values ('Delaware','1');
insert into sstateProvince(name,countryId) values ('District of Columbia','1');
insert into sstateProvince(name,countryId) values ('Florida','1');
insert into sstateProvince(name,countryId) values ('Georgia','1');
insert into sstateProvince(name,countryId) values ('Hawaii','1');
insert into sstateProvince(name,countryId) values ('Idaho','1');
insert into sstateProvince(name,countryId) values ('Illinois','1');
insert into sstateProvince(name,countryId) values ('Indiana','1');
insert into sstateProvince(name,countryId) values ('Iowa','1');
insert into sstateProvince(name,countryId) values ('Kansas','1');
insert into sstateProvince(name,countryId) values ('Kentucky','1');
insert into sstateProvince(name,countryId) values ('Louisiana','1');
insert into sstateProvince(name,countryId) values ('Maine','1');
insert into sstateProvince(name,countryId) values ('Maryland','1');
insert into sstateProvince(name,countryId) values ('Massachusetts','1');
insert into sstateProvince(name,countryId) values ('Michigan','1');
insert into sstateProvince(name,countryId) values ('Minnesota','1');
insert into sstateProvince(name,countryId) values ('Mississippi','1');
insert into sstateProvince(name,countryId) values ('Missouri','1');
insert into sstateProvince(name,countryId) values ('Montana','1');
insert into sstateProvince(name,countryId) values ('Nebraska','1');
insert into sstateProvince(name,countryId) values ('Nevada','1');
insert into sstateProvince(name,countryId) values ('New Hampshire','1');
insert into sstateProvince(name,countryId) values ('New Jersey','1');
insert into sstateProvince(name,countryId) values ('New Mexico','1');
insert into sstateProvince(name,countryId) values ('New York','1');
insert into sstateProvince(name,countryId) values ('North Carolina','1');
insert into sstateProvince(name,countryId) values ('North Dakota','1');
insert into sstateProvince(name,countryId) values ('Ohio','1');
insert into sstateProvince(name,countryId) values ('Oklahoma','1');
insert into sstateProvince(name,countryId) values ('Oregon','1');
insert into sstateProvince(name,countryId) values ('Pennsylvania','1');

insert into sstateProvince(name,countryId) values ('Alberta',2);
insert into sstateProvince(name,countryId) values ('British Columbia',2);
insert into sstateProvince(name,countryId) values ('Manitoba',2);
insert into sstateProvince(name,countryId) values ('New Brunswick',2);
insert into sstateProvince(name,countryId) values ('Newfoundland and Labrador',2);
insert into sstateProvince(name,countryId) values ('Northwest Territories',2);
insert into sstateProvince(name,countryId) values ('Nova Scotia',2);
insert into sstateProvince(name,countryId) values ('Nunavut',2);
insert into sstateProvince(name,countryId) values ('Ontario',2);
insert into sstateProvince(name,countryId) values ('Prince Edward Island',2);
insert into sstateProvince(name,countryId) values ('Quebec',2);
insert into sstateProvince(name,countryId) values ('Saskatchewan',2);
insert into sstateProvince(name,countryId) values ('Yukon',2);
insert into sstateProvince(name,countryId) values ('Rhode Island','1');
insert into sstateProvince(name,countryId) values ('South Carolina','1');
insert into sstateProvince(name,countryId) values ('South Dakota','1');
insert into sstateProvince(name,countryId) values ('Tennessee','1');
insert into sstateProvince(name,countryId) values ('Texas','1');
insert into sstateProvince(name,countryId) values ('Utah','1');
insert into sstateProvince(name,countryId) values ('Vermont','1');
insert into sstateProvince(name,countryId) values ('Virginia','1');
insert into sstateProvince(name,countryId) values ('Washington','1');
insert into sstateProvince(name,countryId) values ('West Virginia','1');
insert into sstateProvince(name,countryId) values ('Wisconsin','1');
insert into sstateProvince(name,countryId) values ('Wyoming','1');
insert into sstateProvince(name,countryId) values ('American Samoa','1');
insert into sstateProvince(name,countryId) values ('Guam','1');
insert into sstateProvince(name,countryId) values ('Northern Marianas Islands','1');
insert into sstateProvince(name,countryId) values ('Puerto Rico','1');
insert into sstateProvince(name,countryId) values ('Virgin Islands','1');

insert into sstateProvince(name,countryId) values ('Alberta',2);
insert into sstateProvince(name,countryId) values ('British Columbia',2);
insert into sstateProvince(name,countryId) values ('Labrador',2);
insert into sstateProvince(name,countryId) values ('Manitoba',2);
insert into sstateProvince(name,countryId) values ('New Brunswick',2);
insert into sstateProvince(name,countryId) values ('Newfoundland',2);
insert into sstateProvince(name,countryId) values ('Northwest Territories',2);
insert into sstateProvince(name,countryId) values ('Nova Scotia',2);
insert into sstateProvince(name,countryId) values ('Nunavut',2);
insert into sstateProvince(name,countryId) values ('Ontario',2);
insert into sstateProvince(name,countryId) values ('Prince Edward Island',2);
insert into sstateProvince(name,countryId) values ('Quebec',2);
insert into sstateProvince(name,countryId) values ('Saskatchewan',2);
insert into sstateProvince(name,countryId) values ('Yukon',2);
