# PAPR_IHS

git clone https://github.com/CRLTechSvcs/PAPR_IHS.git

-- Change to PAPR_IHS/public/javascripts

-- unzip dojo-release-1.9.0.7z using 7zip 

 p7zip -d dojo-release-1.9.0.7z

-- unzip jquery-ui-1.11.3.7z using 7zip

 p7zip -d jquery-ui-1.11.3.7z
 
-- change back to PAPR_IHS

-- make activator executable
chmod 755 activator

-- For compile 
./activator clean compile

-- For running the app

./activator -jvm-debug 9999 run

