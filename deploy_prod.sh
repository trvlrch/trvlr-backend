# copy war file to production machine
rsync -avz  -e "ssh -p 7454" build/libs/trvlr-0.0.1-SNAPSHOT.war centos@5.148.165.52:/var/www/trvlr_backend/java/trvlr_backend.war
ssh -p 7454 centos@5.148.165.52 '/bin/sh /var/www/trvlr_backend/java/restart.sh'