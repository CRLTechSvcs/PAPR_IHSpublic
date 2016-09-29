# PAPR_IHS

git clone https://github.com/CRLTechSvcs/PAPR_IHS.git

-- Change to PAPR_IHS/public/javascripts

-- unzip dojo-release-1.9.0.7z using 7zip

 p7zip -d dojo-release-1.9.0.7z

-- unzip jquery-ui-1.11.3.7z using 7zip

 p7zip -d jquery-ui-1.11.3.7z

-- change back to PAPR_IHS

-- NEW 2016-08-29 for output of pdf reports:
mkdir ./public/reports
-- NEW 2016-09-29 other folders referenced in application.conf but found to be missing after clone to reset AJE mistakes:
mkdir ./data
mkdir ./data/httpdata
mkdir ./data/ftpdata
mkdir ./data/ftpprocess
mkdir ./data/pubprocess

-- make activator executable
chmod 755 activator

-- For compile
./activator clean compile

-- For running the app

./activator -jvm-debug 9999 run


